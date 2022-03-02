package com.tokopedia.pdpsimulation.activateCheckout.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.atc_common.data.model.request.AddToCartOccMultiCartParam
import com.tokopedia.atc_common.data.model.request.AddToCartOccMultiRequestParams
import com.tokopedia.atc_common.domain.model.response.AddToCartOccMultiDataModel
import com.tokopedia.atc_common.domain.usecase.coroutine.AddToCartOccMultiUseCase
import com.tokopedia.pdpsimulation.activateCheckout.domain.model.CheckoutData
import com.tokopedia.pdpsimulation.activateCheckout.domain.model.PaylaterGetOptimizedModel
import com.tokopedia.pdpsimulation.activateCheckout.domain.usecase.PaylaterActivationUseCase
import com.tokopedia.pdpsimulation.common.di.qualifier.CoroutineBackgroundDispatcher
import com.tokopedia.pdpsimulation.common.domain.model.BaseProductDetailClass
import com.tokopedia.pdpsimulation.common.domain.model.GetProductV3
import com.tokopedia.pdpsimulation.common.domain.usecase.ProductDetailUseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class PayLaterActivationViewModel @Inject constructor(
    private val paylaterActivationUseCase: PaylaterActivationUseCase,
    private val productDetailUseCase: ProductDetailUseCase,
    private val addToCartUseCase: AddToCartOccMultiUseCase,
    @CoroutineBackgroundDispatcher val dispatcher: CoroutineDispatcher
) :
    BaseViewModel(dispatcher) {

    var gatewayToChipMap: MutableMap<Int, CheckoutData> = HashMap()

    private val _productDetailLiveData = MutableLiveData<Result<GetProductV3>>()
    val productDetailLiveData: LiveData<Result<GetProductV3>> = _productDetailLiveData

    private val _payLaterActivationDetailLiveData =
        MutableLiveData<Result<PaylaterGetOptimizedModel>>()
    val payLaterActivationDetailLiveData: LiveData<Result<PaylaterGetOptimizedModel>> =
        _payLaterActivationDetailLiveData

    private val _addToCartLiveData = MutableLiveData<Result<AddToCartOccMultiDataModel>>()
    val addToCartLiveData: LiveData<Result<AddToCartOccMultiDataModel>> = _addToCartLiveData

    var price = 0.0
    var shopId: String? = null

    var selectedProductId = ""
    var selectedGatewayId = "0"
    var selectedTenureSelected = "0"
    var selectedGatewayCode = ""
    var occRedirectionUrl = ""

    fun setProductId(productId: String) {
        selectedProductId = productId
    }

    fun setGatewayId(gatewayId: String) {
        selectedGatewayId = gatewayId
    }

    fun setTenure(tenure: String) {
        selectedTenureSelected = tenure
    }

    fun setGatewayCode(gatewayCode: String) {
        selectedGatewayCode = gatewayCode
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
            if (it.pictures?.size == 0 || it.productName.isNullOrEmpty() || it.price?.equals(0.0) == true)
                onFailProductDetail(IllegalStateException("Data invalid"))
            it.price?.let { productPrice ->
                price = productPrice
            }
            it.shopDetail?.shopId?.let { shopID ->
                shopId = shopID
            }
            _productDetailLiveData.value = Success(it)
        }
    }

    private fun onFailProductDetail(throwable: Throwable) {
        _productDetailLiveData.value = Fail(throwable)
    }


    fun getOptimizedCheckoutDetail(productId: String, price: Double, gatewayCode: String) {
        paylaterActivationUseCase.cancelJobs()
        paylaterActivationUseCase.getPayLaterActivationDetail(
            ::onSuccessActivationData,
            ::onFailActivationData,
            price,
            productId,
            gatewayCode
        )
    }

    private fun onSuccessActivationData(paylaterGetOptimizedModel: PaylaterGetOptimizedModel) {
        paylaterGetOptimizedModel.let {
            if (it.checkoutData.isNotEmpty()) {
                it.checkoutData.map { checkoutData ->
                    gatewayToChipMap[checkoutData.gateway_id] = checkoutData
                }
                _payLaterActivationDetailLiveData.postValue(Success(it))

            } else
                onFailActivationData(IllegalStateException("Empty State"))
        }

    }

    private fun onFailActivationData(throwable: Throwable) {
        _payLaterActivationDetailLiveData.postValue(Fail(throwable))
    }


    fun addProductToCart(productId: String, productQuantity: Int) {
        shopId?.let {

            addToCartUseCase.setParams(
                AddToCartOccMultiRequestParams(
                    carts = arrayListOf(
                        AddToCartOccMultiCartParam(
                            productId = productId,
                            shopId = it,
                            quantity = productQuantity.toString()
                        )
                    )
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
            occRedirectionUrl =
                ApplinkConstInternalMarketplace.ONE_CLICK_CHECKOUT + "?selectedTenure=${selectedTenureSelected}" +
                        "&gateway_code=${gatewayToChipMap[selectedGatewayId.toInt()]?.paymentGatewayCode ?: ""}" +
                        "&fintech"
            _addToCartLiveData.value = Success(addToCartOcc)

        }
    }

}

class ShowToasterException(message: String) : Exception(message)