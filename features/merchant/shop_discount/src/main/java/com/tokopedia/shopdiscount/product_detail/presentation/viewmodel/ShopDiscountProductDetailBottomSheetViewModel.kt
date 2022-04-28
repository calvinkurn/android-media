package com.tokopedia.shopdiscount.product_detail.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.shopdiscount.common.data.request.DoSlashPriceReservationRequest
import com.tokopedia.shopdiscount.common.data.response.DoSlashPriceProductReservationResponse
import com.tokopedia.shopdiscount.common.domain.MutationDoSlashPriceProductReservationUseCase
import com.tokopedia.shopdiscount.manage.data.response.DeleteDiscountResponse
import com.tokopedia.shopdiscount.manage.domain.usecase.DeleteDiscountUseCase
import com.tokopedia.shopdiscount.product_detail.ShopDiscountProductDetailMapper
import com.tokopedia.shopdiscount.product_detail.data.response.GetSlashPriceProductDetailResponse
import com.tokopedia.shopdiscount.product_detail.data.uimodel.ShopDiscountDetailReserveProductUiModel
import com.tokopedia.shopdiscount.product_detail.data.uimodel.ShopDiscountProductDetailDeleteUiModel
import com.tokopedia.shopdiscount.product_detail.data.uimodel.ShopDiscountProductDetailUiModel
import com.tokopedia.shopdiscount.product_detail.domain.GetSlashPriceProductDetailUseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import java.util.*
import javax.inject.Inject

class ShopDiscountProductDetailBottomSheetViewModel @Inject constructor(
    private val dispatcherProvider: CoroutineDispatchers,
    private val getSlashPriceProductDetailUseCase: GetSlashPriceProductDetailUseCase,
    private val reserveProductUseCase: MutationDoSlashPriceProductReservationUseCase,
    private val deleteDiscountUseCase: DeleteDiscountUseCase,
    private val userSession: UserSessionInterface
) : BaseViewModel(dispatcherProvider.main) {

    val productDetailListLiveData: LiveData<Result<ShopDiscountProductDetailUiModel>>
        get() = _productDetailListLiveData
    private val _productDetailListLiveData =
        MutableLiveData<Result<ShopDiscountProductDetailUiModel>>()

    private val _reserveProduct = MutableLiveData<Result<ShopDiscountDetailReserveProductUiModel>>()
    val reserveProduct: LiveData<Result<ShopDiscountDetailReserveProductUiModel>>
        get() = _reserveProduct

    private val _deleteProductDiscount = MutableLiveData<Result<ShopDiscountProductDetailDeleteUiModel>>()
    val deleteProductDiscount: LiveData<Result<ShopDiscountProductDetailDeleteUiModel>>
        get() = _deleteProductDiscount


    fun getProductDetailListData(productId: String, status: Int) {
        launchCatchError(dispatcherProvider.io, block = {
            val response = getSLashPriceProductDetailResponse(productId, status)
            val mappedUiModel =
                ShopDiscountProductDetailMapper.mapToShopDiscountProductDetailUiModel(
                    response.getSlashPriceProductDetail
                )
            _productDetailListLiveData.postValue(Success(mappedUiModel))
        }) {
            _productDetailListLiveData.postValue(Fail(it))
        }
    }

    fun reserveProduct(
        productParentId: String,
        productParentPosition: Int,
        selectedProductVariantId: String
    ) {
        launchCatchError(dispatcherProvider.io, block = {
            val requestId = userSession.shopId + Date().time
            val response = reserveProductToUpdate(
                ShopDiscountProductDetailMapper.mapToDoSlashPriceReservationRequest(
                    requestId,
                    productParentId,
                    productParentPosition
                )
            )
            val uiModel =
                ShopDiscountProductDetailMapper.mapToShopDiscountDetailReserveProductUiModel(
                    response,
                    requestId,
                    selectedProductVariantId
                )
            _reserveProduct.postValue(Success(uiModel))
        }, onError = {
            _reserveProduct.postValue(Fail(it))
        })
    }

    private suspend fun reserveProductToUpdate(
        request: DoSlashPriceReservationRequest
    ): DoSlashPriceProductReservationResponse {
        reserveProductUseCase.setParams(request)
        return reserveProductUseCase.executeOnBackground()
    }

    private suspend fun getSLashPriceProductDetailResponse(
        productId: String,
        status: Int
    ): GetSlashPriceProductDetailResponse {
        getSlashPriceProductDetailUseCase.setParams(
            ShopDiscountProductDetailMapper.getGetSlashPriceProductDetailRequestData(
                productId,
                status
            )
        )
        return getSlashPriceProductDetailUseCase.executeOnBackground()
    }

    fun deleteSelectedProductDiscount(productId: String, status: Int) {
        launchCatchError(dispatcherProvider.io, block = {
            val result = doSlashPriceStop(productId, status)
            val uiModel = ShopDiscountProductDetailMapper.mapToShopDiscountProductDetailDeleteUiModel(
                result,
                productId
            )
            _deleteProductDiscount.postValue(Success(uiModel))
        }, onError = {
            _deleteProductDiscount.postValue(Fail(it))
        })
    }

    private suspend fun doSlashPriceStop(productId: String, status: Int): DeleteDiscountResponse {
        deleteDiscountUseCase.setParams(
            discountStatusId = status,
            productIds = listOf(productId)
        )
        return deleteDiscountUseCase.executeOnBackground()
    }

}