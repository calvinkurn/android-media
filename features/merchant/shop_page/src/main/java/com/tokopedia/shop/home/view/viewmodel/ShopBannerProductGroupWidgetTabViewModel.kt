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
import com.tokopedia.shop.home.view.model.ShopWidgetComponentBannerProductGroupUiModel
import com.tokopedia.shop.home.view.model.ShopHomeProductCarouselVerticalBannerItemType
import com.tokopedia.shop.home.view.model.ShopHomeProductCarouselVerticalBannerVerticalBanner
import com.tokopedia.shop.product.data.source.cloud.model.ShopProductFilterInput
import com.tokopedia.shop.product.domain.interactor.GqlGetShopProductUseCase
import javax.inject.Inject
import com.tokopedia.shop.home.view.model.ShopWidgetComponentBannerProductGroupUiModel.Tab.ComponentList.ComponentType
import com.tokopedia.shop.product.data.model.ShopFeaturedProductParams
import com.tokopedia.shop.product.domain.interactor.GetShopFeaturedProductUseCase
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.shop.common.data.source.cloud.model.LabelGroup
import com.tokopedia.shop.home.view.model.ShopWidgetComponentBannerProductGroupUiModel.Tab.ComponentList.Data.LinkType
import com.tokopedia.shop.home.view.model.ShopWidgetComponentBannerProductGroupUiModel.Tab.ComponentList.Data.BannerType

class ShopBannerProductGroupWidgetTabViewModel @Inject constructor(
    private val dispatcherProvider: CoroutineDispatchers,
    private val getShopProductUseCase: GqlGetShopProductUseCase,
    private val getShopFeaturedProductUseCase: GetShopFeaturedProductUseCase,
    private val userSession: UserSessionInterface
) : BaseViewModel(dispatcherProvider.main) {

    companion object {
        private const val FIRST_LABEL_INDEX = 0
        private const val FIRST_PAGE = 1
        private const val PRODUCT_COUNT_TO_FETCH = 5
        private const val LABEL_TITLE_PRODUCT_SOLD_COUNT = "Terjual"
    }

    private val _carouselWidgets = MutableLiveData<UiState>()
    val carouselWidgets: LiveData<UiState>
        get() = _carouselWidgets

    sealed class UiState {
        object Loading: UiState()
        data class Success(val data: List<ShopHomeProductCarouselVerticalBannerItemType>): UiState()
        data class Error(val error: Throwable): UiState()
    }

    fun getCarouselWidgets(
        widgets: List<ShopWidgetComponentBannerProductGroupUiModel.Tab.ComponentList>,
        shopId: String,
        userAddress: LocalCacheModel
    ) {
        _carouselWidgets.postValue(UiState.Loading)

        val firstProductWidget = getProductWidgets(widgets) ?: return

        launchCatchError(
            context = dispatcherProvider.io,
            block = {
                val verticalBanner = getVerticalBanner(firstProductWidget)
                val products = getProducts(shopId, userAddress, firstProductWidget)

                val hasVerticalBanner = firstProductWidget.bannerType == BannerType.VERTICAL
                val carouselWidgets = if (hasVerticalBanner) {
                    verticalBanner + products
                } else {
                    products
                }

                _carouselWidgets.postValue(UiState.Success(carouselWidgets))
            } ,
            onError = { throwable ->
                _carouselWidgets.postValue(UiState.Error(throwable))
            }
        )
    }

    private fun getProductWidgets(
        widgets: List<ShopWidgetComponentBannerProductGroupUiModel.Tab.ComponentList>
    ): ShopWidgetComponentBannerProductGroupUiModel.Tab.ComponentList.Data? {
        val productWidgets =
            widgets.filter { widget -> widget.componentType == ComponentType.PRODUCT }

        val firstComponent = productWidgets.getOrNull(0)
        val firstWidget = firstComponent?.data?.getOrNull(0)
        return firstWidget
    }

    private suspend fun getProducts(
        shopId: String,
        userAddress: LocalCacheModel,
        productWidget: ShopWidgetComponentBannerProductGroupUiModel.Tab.ComponentList.Data,
    ): List<ShopHomeProductCarouselProductCard> {
        val showProductInfo = productWidget.isShowProductInfo

        return when(productWidget.linkType) {
            LinkType.FEATURED_PRODUCT -> {
                val featuredProducts = getFeaturedProducts(shopId, userSession.userId, userAddress, showProductInfo)
                featuredProducts
            }
            LinkType.PRODUCT -> {
                val sortId = productWidget.linkId
                val showcaseId = ShopPageConstant.ALL_SHOWCASE_ID
                val sortedProducts = getSortedProducts(shopId, showcaseId, userAddress, sortId, showProductInfo)
                sortedProducts
            }
            LinkType.SHOWCASE -> {
                val sortId = productWidget.linkId
                val showcaseId = productWidget.linkId.toString()
                val showCaseProducts = getSortedProducts(shopId, showcaseId, userAddress, sortId, showProductInfo)
                showCaseProducts
            }
        }
    }

    private suspend fun getSortedProducts(
        shopId: String,
        showcaseId: String,
        userAddress: LocalCacheModel,
        sortId: Long,
        showProductInfo: Boolean
    ): List<ShopHomeProductCarouselProductCard> {
        getShopProductUseCase.params = GqlGetShopProductUseCase.createParams(
            shopId,
            ShopProductFilterInput().apply {
                etalaseMenu = showcaseId
                this.page = FIRST_PAGE
                sort = sortId.toInt()
                perPage = PRODUCT_COUNT_TO_FETCH
                userDistrictId = userAddress.district_id
                userCityId = userAddress.city_id
                userLat = userAddress.lat
                userLong = userAddress.long
            }
        )
        val response = getShopProductUseCase.executeOnBackground()

        val products = response.data.map { product ->
            val soldLabel = product.labelGroupList.soldCount()

            ShopHomeProductCarouselProductCard(
                product.productId,
                product.primaryImage.thumbnail,
                product.name,
                product.price.textIdr,
                product.campaign.originalPriceFmt,
                product.campaign.discountedPercentage.toIntOrZero(),
                product.stats.averageRating,
                soldLabel,
                product.appLink,
                showProductInfo
            )
        }

        return products
    }

    private suspend fun getFeaturedProducts(
        shopId: String,
        userId: String,
        userAddress: LocalCacheModel,
        showProductInfo: Boolean
    ): List<ShopHomeProductCarouselProductCard> {
        getShopFeaturedProductUseCase.params = GetShopFeaturedProductUseCase.createParams(
            ShopFeaturedProductParams(
                shopId,
                userId,
                userAddress.district_id,
                userAddress.city_id,
                userAddress.lat,
                userAddress.long
            )
        )
        val response = getShopFeaturedProductUseCase.executeOnBackground()
        val featuredProducts = response.map { product ->
            ShopHomeProductCarouselProductCard(
                product.productId,
                product.imageUri,
                product.name,
                product.price,
                product.originalPrice,
                product.percentageAmount,
                product.ratingAverage,
                product.labelGroupList.soldCount(),
                "tokopedia://product/${product.productId}",
                showProductInfo,
                product.productId
            )
        }

        return featuredProducts
    }


    private fun List<LabelGroup>.soldCount() : String {
        val soldLabels = filter { labelGroup ->
            labelGroup.title.contains(LABEL_TITLE_PRODUCT_SOLD_COUNT, true)
        }
        val soldLabel = soldLabels.getOrNull(FIRST_LABEL_INDEX)
        val soldLabelTitle = soldLabel?.title.orEmpty()
        return soldLabelTitle
    }

    private fun getVerticalBanner(
        widget: ShopWidgetComponentBannerProductGroupUiModel.Tab.ComponentList.Data
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
