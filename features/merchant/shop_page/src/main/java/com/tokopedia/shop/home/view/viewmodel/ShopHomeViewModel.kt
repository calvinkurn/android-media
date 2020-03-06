package com.tokopedia.shop.home.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.shop.common.constant.ShopPageConstant
import com.tokopedia.shop.common.util.Util
import com.tokopedia.shop.home.util.CoroutineDispatcherProvider
import com.tokopedia.shop.product.data.source.cloud.model.ShopProductFilterInput
import com.tokopedia.shop.product.domain.interactor.GqlGetShopProductUseCase
import com.tokopedia.shop.home.domain.GetShopPageHomeLayoutUseCase
import com.tokopedia.shop.home.util.asyncCatchError
import com.tokopedia.shop.home.util.mapper.ShopPageHomeMapper
import com.tokopedia.shop.home.view.model.BaseShopHomeWidgetUiModel
import com.tokopedia.shop.home.view.model.ShopHomeProductViewModel
import com.tokopedia.shop.home.view.model.ShopPageHomeLayoutUiModel
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.delay
import javax.inject.Inject

class ShopHomeViewModel @Inject constructor(
        userSession: UserSessionInterface,
        private val getShopPageHomeLayoutUseCase: GetShopPageHomeLayoutUseCase,
        private val getShopProductUseCase: GqlGetShopProductUseCase,
        private val dispatcherProvider: CoroutineDispatcherProvider
) : BaseViewModel(dispatcherProvider.main()) {

    companion object {
        const val ALL_SHOWCASE_ID = "etalase"
    }

    val productListData: LiveData<Result<Pair<Boolean, List<ShopHomeProductViewModel>>>>
        get() = _productListData
    private val _productListData = MutableLiveData<Result<Pair<Boolean, List<ShopHomeProductViewModel>>>>()

    val shopHomeLayoutData: LiveData<Result<ShopPageHomeLayoutUiModel>>
        get() = _shopHomeLayoutData
    private val _shopHomeLayoutData = MutableLiveData<Result<ShopPageHomeLayoutUiModel>>()

    private val userSessionShopId = userSession.shopId ?: ""

    fun getShopPageHomeData(shopId: String) {
        launchCatchError(block = {
            val shopLayoutWidget = asyncCatchError(
                    dispatcherProvider.io(),
                    block = { getShopPageHomeLayout(shopId) },
                    onError = { null }
            )
            val productList = asyncCatchError(
                    dispatcherProvider.io(),
                    block = { getProductList(shopId, 1) },
                    onError = { null }
            )
            shopLayoutWidget.await()?.let {
                _shopHomeLayoutData.postValue(Success(it))
            }
            productList.await()?.let {
                _productListData.postValue(Success(it))
            }
        }) {
        }
    }

    private suspend fun getShopPageHomeLayout(shopId: String): ShopPageHomeLayoutUiModel {
        getShopPageHomeLayoutUseCase.params = GetShopPageHomeLayoutUseCase.createParams(shopId)
        return ShopPageHomeMapper.mapToShopPageHomeLayoutUiModel(
                getShopPageHomeLayoutUseCase.executeOnBackground(),
                Util.isMyShop(shopId, userSessionShopId)
        )
    }

    private suspend fun getProductList(
            shopId: String,
            page: Int
    ): Pair<Boolean, List<ShopHomeProductViewModel>> {
        getShopProductUseCase.params = GqlGetShopProductUseCase.createParams(
                shopId,
                ShopProductFilterInput().apply {
                    etalaseMenu = ALL_SHOWCASE_ID
                    this.page = page
                }
        )
        val productListResponse = getShopProductUseCase.executeOnBackground()
        val isHasNextPage = Util.isHasNextPage(page, ShopPageConstant.DEFAULT_PER_PAGE, productListResponse.totalData)
        return Pair(
                isHasNextPage,
                productListResponse.data.map {
                    ShopPageHomeMapper.mapShopProductToProductViewModel(
                            it,
                            Util.isMyShop(shopId, userSessionShopId)
                    )
                }
        )
    }
}