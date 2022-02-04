package com.tokopedia.shop_widget.mvc_locked_to_product.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.kotlin.extensions.view.isMoreThanZero
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.minicart.common.domain.data.MiniCartSimplifiedData
import com.tokopedia.minicart.common.domain.usecase.GetMiniCartListSimplifiedUseCase
import com.tokopedia.shop_widget.mvc_locked_to_product.domain.model.MvcLockedToProductRequest
import com.tokopedia.shop_widget.mvc_locked_to_product.domain.model.MvcLockedToProductResponse
import com.tokopedia.shop_widget.mvc_locked_to_product.domain.usecase.MvcLockedToProductUseCase
import com.tokopedia.shop_widget.mvc_locked_to_product.util.MvcLockedToProductMapper
import com.tokopedia.shop_widget.mvc_locked_to_product.util.MvcLockedToProductUtil
import com.tokopedia.shop_widget.mvc_locked_to_product.view.uimodel.*
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

class MvcLockedToProductViewModel @Inject constructor(
    private val userSession: UserSessionInterface,
    private val mvcLockedToProductUseCase: MvcLockedToProductUseCase,
    private val getMiniCartUseCase: GetMiniCartListSimplifiedUseCase,
    private val dispatcherProvider: CoroutineDispatchers
) : BaseViewModel(dispatcherProvider.main) {
    val isUserLogin: Boolean
        get() = userSession.isLoggedIn
    val userId: String
        get() = userSession.userId

    val hasNextPageLiveData: LiveData<Boolean>
        get() = _hasNextPageLiveData
    private val _hasNextPageLiveData = MutableLiveData<Boolean>()

    val mvcLockToProductLiveData: LiveData<Result<MvcLockedToProductLayoutUiModel>>
        get() = _mvcLockToProductLiveData
    private val _mvcLockToProductLiveData =
        MutableLiveData<Result<MvcLockedToProductLayoutUiModel>>()

    val productListDataProduct: LiveData<Result<List<MvcLockedToProductGridProductUiModel>>>
        get() = _productListData
    private val _productListData =
        MutableLiveData<Result<List<MvcLockedToProductGridProductUiModel>>>()

    private var miniCartSimplifiedData: MiniCartSimplifiedData? = null

    fun getMvcLockedToProductData(mvcLockedToProductRequestUiModel: MvcLockedToProductRequestUiModel) {
        launchCatchError(dispatcherProvider.io, block = {
            val response = getMvcLockToProductResponse(mvcLockedToProductRequestUiModel)
            val uiModel = MvcLockedToProductMapper.mapToMvcLockedToProductLayoutUiModel(
                response.shopPageMVCProductLock,
                mvcLockedToProductRequestUiModel.selectedSortData
            )
            _mvcLockToProductLiveData.postValue(Success(uiModel))
            _hasNextPageLiveData.postValue(checkHasNextPage(response.shopPageMVCProductLock.nextPage))
        }) {
            _mvcLockToProductLiveData.postValue(Fail(it))
        }
    }

    fun getProductListData(mvcLockedToProductRequestUiModel: MvcLockedToProductRequestUiModel) {
        launchCatchError(dispatcherProvider.io, block = {
            val response = getMvcLockToProductResponse(mvcLockedToProductRequestUiModel)
            val uiModel = MvcLockedToProductMapper.mapToMvcLockedToProductProductListUiModel(
                response.shopPageMVCProductLock.productList
            )
            _productListData.postValue(Success(uiModel))
            _hasNextPageLiveData.postValue(checkHasNextPage(response.shopPageMVCProductLock.nextPage))
        }) {
            _productListData.postValue(Fail(it))
        }
    }

    fun isSellerView(shopId: String): Boolean {
        return MvcLockedToProductUtil.isSellerView(shopId, userSession.shopId)
    }

    private suspend fun getMvcLockToProductResponse(
        mvcLockedToProductRequestUiModel: MvcLockedToProductRequestUiModel
    ): MvcLockedToProductResponse {
        mvcLockedToProductUseCase.setParams(
            MvcLockedToProductRequest(
                mvcLockedToProductRequestUiModel.shopID,
                mvcLockedToProductRequestUiModel.promoID,
                mvcLockedToProductRequestUiModel.page,
                mvcLockedToProductRequestUiModel.perPage,
                mvcLockedToProductRequestUiModel.selectedSortData.value,
                mvcLockedToProductRequestUiModel.localCacheModel.district_id,
                mvcLockedToProductRequestUiModel.localCacheModel.city_id,
                mvcLockedToProductRequestUiModel.localCacheModel.lat,
                mvcLockedToProductRequestUiModel.localCacheModel.long
            )
        )
        return mvcLockedToProductUseCase.executeOnBackground()
    }

    private fun checkHasNextPage(nextPage: Int): Boolean {
        return nextPage.isMoreThanZero()
    }

    fun getMiniCart(shopId: List<String>, warehouseId: String?) {
        if (!shopId.isNullOrEmpty() && warehouseId.toLongOrZero() != 0L && userSession.isLoggedIn) {
            launchCatchError(block = {
                getMiniCartUseCase.setParams(shopId)
                getMiniCartUseCase.execute({
                    val ggz = -1
//                    setMiniCartAndProductQuantity(it)
//                    _miniCart.postValue(Success(it))
                }, {
//                    _miniCart.postValue(Fail(it))
                })
            }) {
//                _miniCart.postValue(Fail(it))
            }
        }
    }
}