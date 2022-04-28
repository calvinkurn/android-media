package com.tokopedia.pdpsimulation.paylater.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.atc_common.data.model.request.AddToCartOccMultiCartParam
import com.tokopedia.atc_common.data.model.request.AddToCartOccMultiRequestParams
import com.tokopedia.atc_common.domain.model.response.AddToCartOccMultiDataModel
import com.tokopedia.atc_common.domain.usecase.coroutine.AddToCartOccMultiUseCase
import com.tokopedia.pdpsimulation.activateCheckout.viewmodel.ShowToasterException
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
    private val addToCartUseCase: AddToCartOccMultiUseCase,
    @CoroutineMainDispatcher dispatcher: CoroutineDispatcher,
) : BaseViewModel(dispatcher) {

    private val _payLaterOptionsDetailLiveData =
        MutableLiveData<Result<ArrayList<SimulationUiModel>>>()
    val payLaterOptionsDetailLiveData: LiveData<Result<ArrayList<SimulationUiModel>>> =
        _payLaterOptionsDetailLiveData

    private val _productDetailLiveData = MutableLiveData<Result<GetProductV3>>()
    val productDetailLiveData: LiveData<Result<GetProductV3>> = _productDetailLiveData

    private val _addToCartLiveData = MutableLiveData<Result<AddToCartOccMultiDataModel>>()
    val addToCartLiveData: LiveData<Result<AddToCartOccMultiDataModel>> = _addToCartLiveData

    // tenure to be auto-selected by default coming from applink
    var defaultTenure = 0

    // index of selected tenure/simulation in list
    // if failure -> 0
    // if tenure found -> then intended simulation
    // if tenure not found -> then max simulation
    var defaultSelectedSimulation: Int = 0
    var finalProductPrice:Double = 0.0
    var shopId:String? = null

    fun getPayLaterAvailableDetail(price: Double, productId: String) {
        finalProductPrice = price
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
            if (data.pictures?.size == 0 || data.productName.isNullOrEmpty() || ((data.campaingnDetail?.discountedPrice?.equals(
                    0.0
                ) == true) && data.price?.equals(0.0) == true)
            )
                onFailProductDetail(IllegalStateException("Data invalid"))
            else {
                data.shopDetail?.shopId?.let {
                    shopId = it
                }
                _productDetailLiveData.postValue(Success(data))
            }
        }
    }

    private fun onFailProductDetail(throwable: Throwable) {
        _productDetailLiveData.postValue(Fail(throwable))
    }

    private fun onAvailableDetailFail(throwable: Throwable) {
        _payLaterOptionsDetailLiveData.postValue(Fail(throwable))
    }

    private fun onAvailableDetailSuccess(payLaterGetSimulation: PayLaterGetSimulation?) {
        mapperUseCase.cancelJobs()
        mapperUseCase.mapResponseToUi(
            { data ->
                if (data.isNotEmpty()) {
                    data.mapIndexed { index, simulationUiModel ->
                        if (simulationUiModel.isSelected)
                            defaultSelectedSimulation = index
                    }
                }
                _payLaterOptionsDetailLiveData.postValue(Success(data))
            }, payLaterGetSimulation, defaultTenure
        )
    }

    override fun onCleared() {
        paylaterGetSimulationV3UseCase.cancelJobs()
        productDetailUseCase.cancelJobs()
        mapperUseCase.cancelJobs()
        super.onCleared()
    }


    fun addProductToCart(productId: String) {
        shopId?.let {
            addToCartUseCase.setParams(
                AddToCartOccMultiRequestParams(
                    carts = arrayListOf(
                        AddToCartOccMultiCartParam(
                            productId = productId,
                            shopId = it,
                            quantity = DelfaultProductQuantity,
                        )
                    ),
                    source = AddToCartOccMultiRequestParams.SOURCE_FINTECH
                )
            )
            addToCartUseCase.execute(
                onSuccess = {
                    onSuccessAddToCartForCheckout(it)
                },
                onError = {
                    onErrorAddToCartForCheckout(it)
                }
            )
        }
    }

    private fun onSuccessAddToCartForCheckout(addToCartOcc: AddToCartOccMultiDataModel) {
        if (addToCartOcc.isStatusError())
            _addToCartLiveData.value = Fail(
                ShowToasterException(
                    addToCartOcc.getAtcErrorMessage()
                        ?: ""
                )
            )
        else {
            _addToCartLiveData.value = Success(addToCartOcc)
        }
    }

    private fun onErrorAddToCartForCheckout(throwable: Throwable) {
        _addToCartLiveData.value = Fail(throwable)
    }

    companion object{
        const val DelfaultProductQuantity = "1"
    }

}