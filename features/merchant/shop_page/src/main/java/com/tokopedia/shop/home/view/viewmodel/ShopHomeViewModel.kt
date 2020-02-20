package com.tokopedia.shop.home.view.viewmodel

import androidx.lifecycle.LiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.shop.common.constant.ShopPageConstant
import com.tokopedia.shop.common.util.Util
import com.tokopedia.shop.home.util.CoroutineDispatcherProvider
import com.tokopedia.shop.newproduct.utils.mapper.ShopPageProductListMapper
import com.tokopedia.shop.newproduct.view.datamodel.ShopProductViewModel
import com.tokopedia.shop.product.data.source.cloud.model.ShopProductFilterInput
import com.tokopedia.shop.product.domain.interactor.GqlGetShopProductUseCase
import com.tokopedia.shop.product.domain.interactor.ShopPageHomeGetlayoutUseCase
import com.tokopedia.user.session.UserSessionInterface

class ShopHomeViewModel(
        private val userSession: UserSessionInterface,
        private val getShopProductUseCase: GqlGetShopProductUseCase,
        private val shopPageHomeGetlayoutUseCase: ShopPageHomeGetlayoutUseCase,
        private val coroutineDispatcherProvider: CoroutineDispatcherProvider
) : BaseViewModel(coroutineDispatcherProvider.main()) {

    companion object {
        const val ALL_SHOWCASE_ID = "etalase"
    }

//    val shopHomeLayoutData: LiveData<List<>>
    private val userSessionShopId = userSession.shopId ?: ""

//    fun getHomeLayout(): List<ShopHomeLayout> {
//
//    }

    fun getShopPageHomeData() {
        launchCatchError (block =  {
//            getAllShowcaseProductList()
        }) {
        }
    }

    suspend fun getAllShowcaseProductList(
            shopId: String,
            page: Int
    ): Pair<Boolean, List<ShopProductViewModel>> {
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
                    ShopPageProductListMapper.mapShopProductToProductViewModel(
                            it,
                            Util.isMyShop(shopId, userSessionShopId)
                    )
                }
        )
    }
}