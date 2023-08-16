package com.tokopedia.shop.home.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel
import com.tokopedia.shop.common.constant.ShopPageConstant
import com.tokopedia.shop.home.view.model.ShopHomeProductCarouselProductCard
import com.tokopedia.shop.home.view.model.ShopHomeProductCarouselUiModel
import com.tokopedia.shop.home.view.model.ShopHomeProductCarouselVerticalBannerItemType
import com.tokopedia.shop.home.view.model.ShopHomeProductCarouselVerticalBannerVerticalBanner
import com.tokopedia.shop.product.data.model.ShopProduct
import com.tokopedia.shop.product.data.source.cloud.model.ShopProductFilterInput
import com.tokopedia.shop.product.domain.interactor.GqlGetShopProductUseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject
import com.tokopedia.shop.home.view.model.ShopHomeProductCarouselUiModel.Tab.ComponentList.ComponentType

class ShopProductCarouselTabViewModel @Inject constructor(
    private val dispatcherProvider: CoroutineDispatchers,
    private val getShopProductUseCase: GqlGetShopProductUseCase
) : BaseViewModel(dispatcherProvider.main) {

    companion object {
        private const val FIRST_LABEL_INDEX = 0
        private const val FIRST_PAGE = 1
        private const val PRODUCT_COUNT_TO_FETCH = 5
        private const val SORT_ID_MOST_SOLD = 8
        private const val SORT_ID_NEWEST = 2
        private const val LABEL_TITLE_PRODUCT_SOLD_COUNT = "Terjual"
    }

    private val _carouselWidgets = MutableLiveData<Result<List<ShopHomeProductCarouselVerticalBannerItemType>>>()
    val carouselWidgets: LiveData<Result<List<ShopHomeProductCarouselVerticalBannerItemType>>>
        get() = _carouselWidgets

    fun getCarouselWidgets(
        widgets: List<ShopHomeProductCarouselUiModel.Tab.ComponentList>,
        shopId: String,
        userAddress: LocalCacheModel
    ) {
        val firstProductWidget = getProductWidgets(widgets) ?: return

        launchCatchError(
            context = dispatcherProvider.io,
            block = {
                val verticalBanner = getVerticalBanner(firstProductWidget)
                val products = getProducts(shopId, userAddress, firstProductWidget)

                val hasVerticalBanner = firstProductWidget.bannerType == ShopHomeProductCarouselUiModel.Tab.ComponentList.Data.BannerType.VERTICAL
                val carouselWidgets = if (hasVerticalBanner) {
                    verticalBanner + products
                } else {
                    products
                }

                _carouselWidgets.postValue(Success(carouselWidgets))
            } ,
            onError = { throwable ->
                _carouselWidgets.postValue(Fail(throwable))
            }
        )
    }

    private fun getProductWidgets(widgets: List<ShopHomeProductCarouselUiModel.Tab.ComponentList>): ShopHomeProductCarouselUiModel.Tab.ComponentList.Data? {
        val productWidgets = widgets
            .filter { widget -> widget.componentType == ComponentType.PRODUCT }

        val firstComponent = productWidgets.getOrNull(0)
        val firstWidget = firstComponent?.data?.getOrNull(0)
        return firstWidget
    }

    private suspend fun getProducts(
        shopId: String,
        userAddress: LocalCacheModel,
        firstWidget: ShopHomeProductCarouselUiModel.Tab.ComponentList.Data
    ): List<ShopHomeProductCarouselProductCard> {
        val productLinkType = firstWidget.linkType

        val sortId = when (productLinkType) {
            "terlaris" -> SORT_ID_MOST_SOLD
            "terbaru" -> SORT_ID_NEWEST
            else -> SORT_ID_MOST_SOLD
        }

        getShopProductUseCase.params = GqlGetShopProductUseCase.createParams(
            shopId,
            ShopProductFilterInput().apply {
                etalaseMenu = ShopPageConstant.ALL_SHOWCASE_ID
                this.page = FIRST_PAGE
                sort = sortId
                perPage = PRODUCT_COUNT_TO_FETCH
                userDistrictId = userAddress.district_id
                userCityId = userAddress.city_id
                userLat = userAddress.lat
                userLong = userAddress.long
            }
        )
        val response = getShopProductUseCase.executeOnBackground()

        val products = response.data.map { product ->
            val soldLabel = product.soldCount()

            ShopHomeProductCarouselProductCard(
                product.productId,
                product.primaryImage.thumbnail,
                product.name,
                product.price.textIdr,
                product.campaign.originalPriceFmt,
                product.campaign.discountedPercentage.toIntOrZero(),
                product.stats.averageRating,
                soldLabel,
                product.appLink
            )
        }

        return products
    }

    private fun ShopProduct.soldCount() : String {
        val soldLabels = labelGroupList
            .filter { labelGroup ->
                labelGroup.title.contains(
                    LABEL_TITLE_PRODUCT_SOLD_COUNT,
                    true
                )
            }
        val soldLabel = soldLabels.getOrNull(FIRST_LABEL_INDEX)
        val soldLabelTitle = soldLabel?.title.orEmpty()
        return soldLabelTitle
    }

    private fun getVerticalBanner(
        widget: ShopHomeProductCarouselUiModel.Tab.ComponentList.Data
    ): List<ShopHomeProductCarouselVerticalBannerVerticalBanner> {
        return listOf(
            ShopHomeProductCarouselVerticalBannerVerticalBanner(
                widget.imageUrl,
                widget.bannerType,
                widget.ctaLink,
                widget.imageUrl
            )
        )
    }
}
