package com.tokopedia.pdpsimulation.activateCheckout.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.atc_common.data.model.request.AddToCartOccMultiCartParam
import com.tokopedia.atc_common.data.model.request.AddToCartOccMultiRequestParams
import com.tokopedia.atc_common.domain.model.response.AddToCartOccMultiDataModel
import com.tokopedia.atc_common.domain.usecase.coroutine.AddToCartOccMultiUseCase
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
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
            _payLaterActivationDetailLiveData.postValue( Success(it))
        }

    }

    private fun onFailActivationData(throwable: Throwable) {
        _payLaterActivationDetailLiveData.postValue(  Fail(throwable))
    }



    fun addProductToCart(productId: String, productQuantity: Int) {
        shopId?.let {

            launchCatchError(block = {
                val response = addToCartUseCase.setParams(
                    AddToCartOccMultiRequestParams(
                        carts = arrayListOf(
                            AddToCartOccMultiCartParam(
                                productId = productId,
                                shopId = it,
                                quantity = productQuantity.toString()
                            )
                        )
                    )
                ).executeOnBackground()
                _addToCartLiveData.value = Success(response)
            }) {
                _addToCartLiveData.value = Fail(it)
            }
        }
    }


}