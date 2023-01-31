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
import com.tokopedia.pdpsimulation.paylater.domain.model.Detail
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
    var finalProductPrice: Double = 0.0
    var shopId: String? = null
    var cardDetailSelected:Detail?= null

    fun getPayLaterAvailableDetail(price: Double, productId: String, shopId: String?) {
        finalProductPrice = price
        paylaterGetSimulationV3UseCase.cancelJobs()
        paylaterGetSimulationV3UseCase.getPayLaterSimulationDetails(
            ::onAvailableDetailSuccess,
            ::onAvailableDetailFail,
            price,
            productId,
            shopId ?: "",
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
                shopId = data.shopDetail?.shopId
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


    fun addProductToCart(detailOfSelected: Detail, productId: String) {
        cardDetailSelected = detailOfSelected
        shopId?.let { shopId->
            addToCartUseCase.setParams(
                AddToCartOccMultiRequestParams(
                    carts = arrayListOf(
                        AddToCartOccMultiCartParam(
                            productId = productId,
                            shopId = shopId,
                            quantity = PRODUCT_QUANTITY,
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

    private fun onErrorAddToCartForCheckout(throwable: Throwable) {
        _addToCartLiveData.value = Fail(throwable)
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


    override fun onCleared() {
        paylaterGetSimulationV3UseCase.cancelJobs()
        productDetailUseCase.cancelJobs()
        mapperUseCase.cancelJobs()
        super.onCleared()
    }

    companion object {
        const val PRODUCT_QUANTITY = "1"
    }

}
