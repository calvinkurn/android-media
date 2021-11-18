package com.tokopedia.pdpsimulation.paylater.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.pdpsimulation.TkpdIdlingResourceProvider
import com.tokopedia.pdpsimulation.common.di.qualifier.CoroutineMainDispatcher
import com.tokopedia.pdpsimulation.paylater.domain.model.BaseProductDetailClass
import com.tokopedia.pdpsimulation.paylater.domain.model.GetProductV3
import com.tokopedia.pdpsimulation.paylater.domain.model.PayLaterGetSimulation
import com.tokopedia.pdpsimulation.paylater.domain.usecase.PayLaterSimulationV2UseCase
import com.tokopedia.pdpsimulation.paylater.domain.usecase.ProductDetailUseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class PayLaterViewModel @Inject constructor(
    private val paylaterGetSimulationV2usecase: PayLaterSimulationV2UseCase,
    private val productDetailUseCase: ProductDetailUseCase,

    @CoroutineMainDispatcher dispatcher: CoroutineDispatcher,
) : BaseViewModel(dispatcher) {


    private val _payLaterOptionsDetailLiveData = MutableLiveData<Result<PayLaterGetSimulation>>()
    val payLaterOptionsDetailLiveData: LiveData<Result<PayLaterGetSimulation>> =
        _payLaterOptionsDetailLiveData

    /**
     * @param refreshData -> This parameter is to check when to refresh data in on resume
     * @param sortPosition -> Give the old filter position when so after refresh can go to previous state
     * @param partnerDisplayPosition -> Give the old viewpager position so after refresh can go to previous state
     */
    var refreshData = false
    var sortPosition = 0
    var partnerDisplayPosition = 0

    private val _productDetailLiveData = MutableLiveData<Result<GetProductV3>>()
    val productDetailLiveData: LiveData<Result<GetProductV3>> = _productDetailLiveData


    private var idlingResourceProvider =
        TkpdIdlingResourceProvider.provideIdlingResource("SIMULATION")


    fun getPayLaterAvailableDetail(price: Long) {
        idlingResourceProvider?.increment()
        paylaterGetSimulationV2usecase.cancelJobs()
        paylaterGetSimulationV2usecase.getPayLaterProductDetails(
            ::onAvailableDetailSuccess,
            ::onAvailableDetailFail,
            price
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
        baseProductDetailClass.getProductV3?.let {
            _productDetailLiveData.value = Success(it)
        }
    }

    private fun onFailProductDetail(throwable: Throwable) {
        _productDetailLiveData.value = Fail(throwable)
    }


    private fun onAvailableDetailFail(throwable: Throwable) {
        idlingResourceProvider?.decrement()
        _payLaterOptionsDetailLiveData.value = Fail(throwable)
    }

    private fun onAvailableDetailSuccess(paylaterGetSimulation: PayLaterGetSimulation?) {
        idlingResourceProvider?.decrement()
        paylaterGetSimulation?.let {
            _payLaterOptionsDetailLiveData.value = Success(it)
        }
    }


    override fun onCleared() {
        paylaterGetSimulationV2usecase.cancelJobs()
        productDetailUseCase.cancelJobs()
        super.onCleared()
    }


    companion object {
        const val DATA_FAILURE = "NULL DATA"
        const val PAY_LATER_NOT_APPLICABLE = "Pay Later Not Applicable"
    }
}