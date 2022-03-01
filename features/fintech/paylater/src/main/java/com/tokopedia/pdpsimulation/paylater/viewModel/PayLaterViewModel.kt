package com.tokopedia.pdpsimulation.paylater.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.pdpsimulation.common.di.qualifier.CoroutineMainDispatcher
import com.tokopedia.pdpsimulation.common.domain.model.BaseProductDetailClass
import com.tokopedia.pdpsimulation.common.domain.model.GetProductV3
import com.tokopedia.pdpsimulation.common.domain.usecase.ProductDetailUseCase
import com.tokopedia.pdpsimulation.paylater.domain.model.PayLaterGetSimulation
import com.tokopedia.pdpsimulation.paylater.domain.model.SimulationUiModel
import com.tokopedia.pdpsimulation.paylater.domain.usecase.PayLaterSimulationV3UseCase
import com.tokopedia.pdpsimulation.paylater.domain.usecase.PayLaterUiMapperUseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class PayLaterViewModel @Inject constructor(
        private val paylaterGetSimulationV3UseCase: PayLaterSimulationV3UseCase,
        private val productDetailUseCase: ProductDetailUseCase,
        private val mapperUseCase: PayLaterUiMapperUseCase,
        @CoroutineMainDispatcher dispatcher: CoroutineDispatcher,
) : BaseViewModel(dispatcher) {

    private val _payLaterOptionsDetailLiveData =
            MutableLiveData<Result<ArrayList<SimulationUiModel>>>()
    val payLaterOptionsDetailLiveData: LiveData<Result<ArrayList<SimulationUiModel>>> =
            _payLaterOptionsDetailLiveData

    private val _productDetailLiveData = MutableLiveData<Result<GetProductV3>>()
    val productDetailLiveData: LiveData<Result<GetProductV3>> = _productDetailLiveData

    // tenure to be auto-selected by default coming from applink
    var defaultTenure = 0

    // index of selected tenure/simulation in list
    // if failure -> 0
    // if tenure found -> then intended simulation
    // if tenure not found -> then last simulation
    var defaultSelectedSimulation: Int = 0

    // (K,V) -> Tenure -> Index of Simulation Item for corresponding tenure
    var tenureMap: Map<Int?, Int?> = mapOf()

    fun getPayLaterAvailableDetail(price: Double, productId: String) {
        paylaterGetSimulationV3UseCase.cancelJobs()
        paylaterGetSimulationV3UseCase.getPayLaterSimulationDetails(
                ::onAvailableDetailSuccess,
                ::onAvailableDetailFail,
                price, productId
        )
    }

    fun getProductDetail(productId: String) {
        productDetailUseCase.cancelJobs()
        productDetailUseCase.getProductDetail(
                ::onAvailableProductDetail,
                ::onFailProductDetail,
                productId
        )
    }

    private fun onAvailableProductDetail(baseProductDetailClass: BaseProductDetailClass) {
        baseProductDetailClass.getProductV3?.let { data ->
            if (data.pictures?.size == 0 || data.productName.isNullOrEmpty() || data.price?.equals(0.0) == true)
                onFailProductDetail(IllegalStateException("Data invalid"))
            else _productDetailLiveData.postValue(Success(data))
        }
    }

    private fun onFailProductDetail(throwable: Throwable) {
        _productDetailLiveData.postValue(Fail(throwable))
    }

    private fun onAvailableDetailFail(throwable: Throwable) {
        _payLaterOptionsDetailLiveData.postValue(Fail(throwable))
    }

    private fun onAvailableDetailSuccess(paylaterGetSimulation: PayLaterGetSimulation?) {
        mapperUseCase.cancelJobs()
        mapperUseCase.mapResponseToUi({ data ->
            if (data.isNotEmpty()) {
                tenureMap =
                        data.mapIndexed { index, simulationUiModel -> simulationUiModel.tenure to index }
                                .toMap()
                // set selection
                defaultSelectedSimulation = tenureMap[defaultTenure] ?: data.size - 1
            }
            _payLaterOptionsDetailLiveData.postValue(Success(data))
        }, paylaterGetSimulation, defaultTenure
        )
    }

    override fun onCleared() {
        paylaterGetSimulationV3UseCase.cancelJobs()
        productDetailUseCase.cancelJobs()
        mapperUseCase.cancelJobs()
        super.onCleared()
    }

}