package com.tokopedia.shop.home.view.viewmodel

import android.annotation.SuppressLint
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel
import com.tokopedia.shop.common.constant.ShopPageConstant
import com.tokopedia.shop.home.view.model.banner_product_group.appearance.ProductItemType
import com.tokopedia.shop.home.view.model.banner_product_group.BannerProductGroupUiModel
import com.tokopedia.shop.home.view.model.banner_product_group.appearance.ShopHomeBannerProductGroupItemType
import com.tokopedia.shop.home.view.model.banner_product_group.appearance.VerticalBannerItemType
import com.tokopedia.shop.product.data.source.cloud.model.ShopProductFilterInput
import com.tokopedia.shop.product.domain.interactor.GqlGetShopProductUseCase
import javax.inject.Inject
import com.tokopedia.shop.home.view.model.banner_product_group.BannerProductGroupUiModel.Tab.ComponentList.ComponentName
import com.tokopedia.shop.product.data.model.ShopFeaturedProductParams
import com.tokopedia.shop.product.domain.interactor.GetShopFeaturedProductUseCase
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.shop.common.data.source.cloud.model.LabelGroup
import com.tokopedia.shop.common.view.model.ShopPageColorSchema
import com.tokopedia.shop.home.view.model.banner_product_group.BannerProductGroupUiModel.Tab.ComponentList.Data.LinkType
import kotlinx.coroutines.SupervisorJob
import kotlin.coroutines.CoroutineContext

@SuppressLint("PII Data Exposure")
class ShopBannerProductGroupWidgetTabViewModel @Inject constructor(
    private val dispatcherProvider: CoroutineDispatchers,
    private val getShopProductUseCase: GqlGetShopProductUseCase,
    private val getShopFeaturedProductUseCase: GetShopFeaturedProductUseCase,
    private val userSession: UserSessionInterface
) : BaseViewModel(dispatcherProvider.main) {

    override val coroutineContext: CoroutineContext
        get() = SupervisorJob() + dispatcherProvider.main

    companion object {
        private const val FIRST_LABEL_INDEX = 0
        private const val FIRST_PAGE = 1
        private const val PRODUCT_COUNT_TO_FETCH = 10
        private const val LABEL_TITLE_PRODUCT_SOLD_COUNT = "Terjual"
        private const val SORT_ID_SORT_BY_SOLD_DESC = 8
    }

    private val _carouselWidgets = MutableLiveData<UiState>()
    val carouselWidgets: LiveData<UiState>
        get() = _carouselWidgets

    sealed class UiState {
        object Loading: UiState()
        data class Success(val data: List<ShopHomeBannerProductGroupItemType>): UiState()
        data class Error(val error: Throwable): UiState()
    }

    fun getCarouselWidgets(
        widgets: List<BannerProductGroupUiModel.Tab.ComponentList>,
        shopId: String,
        userAddress: LocalCacheModel,
        widgetStyle: String,
        overrideTheme: Boolean,
        colorSchema: ShopPageColorSchema
    ) {
        getShopProductUseCase.clearCache()
        _carouselWidgets.postValue(UiState.Loading)

        val productMetadata = getProductMetadata(widgets)

        if (productMetadata != null) {
            launchCatchError(
                context = dispatcherProvider.io,
                block = {

                    val products = getProductsByProductMetadata(shopId, userAddress, productMetadata, overrideTheme, colorSchema)

                    val hasVerticalBanner = widgetStyle == BannerProductGroupUiModel.WidgetStyle.VERTICAL.id
                    val carouselWidgets = if (hasVerticalBanner) {
                        val verticalBanner = getVerticalBanner(widgets)
                        verticalBanner + products
                    } else {
                        products
                    }

                    _carouselWidgets.postValue(UiState.Success(carouselWidgets))

                },
                onError = { throwable ->
                    _carouselWidgets.postValue(UiState.Error(throwable))
                }
            )
        }
    }

    private fun getVerticalBanner(widgets: List<BannerProductGroupUiModel.Tab.ComponentList>): List<VerticalBannerItemType> {
        val bannerComponents = widgets.filter { widget -> widget.componentName == ComponentName.DISPLAY_SINGLE_COLUMN }
        val banner = bannerComponents.getOrNull(0)

        return if (banner?.data == null) {
            listOf()
        } else {
            val bannerWidgets = banner.data
            if (bannerWidgets.isEmpty()) return listOf()

            val componentId = banner.componentId
            val componentName = banner.componentName
            val bannerImageUrl = bannerWidgets[0].imageUrl
            val ctaLink = bannerWidgets[0].ctaLink

            listOf(VerticalBannerItemType(componentId, componentName, bannerImageUrl, ctaLink))
        }
    }

    private fun getProductMetadata(
        widgets: List<BannerProductGroupUiModel.Tab.ComponentList>
    ): BannerProductGroupUiModel.Tab.ComponentList.Data? {
        val productComponents = widgets.filter { widget -> widget.componentName == ComponentName.PRODUCT }

        val product = productComponents.getOrNull(0) ?: return null

        val productMetadata = product.data.getOrNull(0)
        return productMetadata
    }

    private suspend fun getProductsByProductMetadata(
        shopId: String,
        userAddress: LocalCacheModel,
        productWidget: BannerProductGroupUiModel.Tab.ComponentList.Data,
        overrideTheme: Boolean,
        colorSchema: ShopPageColorSchema
    ): List<ProductItemType> {
        val showProductInfo = productWidget.isShowProductInfo

        return when(productWidget.linkType) {
            LinkType.FEATURED_PRODUCT -> {
                val featuredProducts = getFeaturedProducts(shopId, userSession.userId, userAddress, showProductInfo, overrideTheme, colorSchema)
                featuredProducts
            }
            LinkType.PRODUCT -> {
                val sortId = productWidget.linkId
                val sortedProducts = getSortedProducts(shopId, userAddress, sortId, showProductInfo, overrideTheme, colorSchema)
                sortedProducts
            }
            LinkType.SHOWCASE -> {
                val showcaseId = productWidget.linkId.toString()
                val showCaseProducts = getShowcaseProduct(shopId, showcaseId, userAddress, showProductInfo, overrideTheme, colorSchema)
                showCaseProducts
            }
        }
    }

    private suspend fun getShowcaseProduct(
        shopId: String,
        showcaseId: String,
        userAddress: LocalCacheModel,
        showProductInfo: Boolean,
        overrideTheme: Boolean,
        colorSchema: ShopPageColorSchema
    ): List<ProductItemType> {
        val params = GqlGetShopProductUseCase.createParams(
            shopId,
            ShopProductFilterInput().apply {
                etalaseMenu = showcaseId
                this.page = FIRST_PAGE
                sort = SORT_ID_SORT_BY_SOLD_DESC
                perPage = PRODUCT_COUNT_TO_FETCH
                userDistrictId = userAddress.district_id
                userCityId = userAddress.city_id
                userLat = userAddress.lat
                userLong = userAddress.long
            }
        )

        return getProducts(showProductInfo, overrideTheme, colorSchema, params)
    }

    private suspend fun getSortedProducts(
        shopId: String,
        userAddress: LocalCacheModel,
        sortId: Long,
        showProductInfo: Boolean,
        overrideTheme: Boolean,
        colorSchema: ShopPageColorSchema
    ): List<ProductItemType> {
        val params = GqlGetShopProductUseCase.createParams(
            shopId,
            ShopProductFilterInput().apply {
                etalaseMenu = ShopPageConstant.ALL_SHOWCASE_ID
                this.page = FIRST_PAGE
                sort = sortId.toInt()
                perPage = PRODUCT_COUNT_TO_FETCH
                userDistrictId = userAddress.district_id
                userCityId = userAddress.city_id
                userLat = userAddress.lat
                userLong = userAddress.long
            }
        )

        return getProducts(showProductInfo, overrideTheme, colorSchema, params)
    }

    private suspend fun getFeaturedProducts(
        shopId: String,
        userId: String,
        userAddress: LocalCacheModel,
        showProductInfo: Boolean,
        overrideTheme: Boolean,
        colorSchema: ShopPageColorSchema
    ): List<ProductItemType> {
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
            ProductItemType(
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
                product.productId,
                overrideTheme,
                colorSchema
            )
        }

        return featuredProducts
    }

    private suspend fun getProducts(
        showProductInfo: Boolean,
        overrideTheme: Boolean,
        colorSchema: ShopPageColorSchema,
        params: Map<String, Any>
    ) : List<ProductItemType> {
        getShopProductUseCase.params = params

        val response = getShopProductUseCase.executeOnBackground()

        val products = response.data.map { product ->
            val soldLabel = product.labelGroupList.soldCount()

            ProductItemType(
                product.productId,
                product.primaryImage.thumbnail,
                product.name,
                product.price.textIdr,
                product.campaign.originalPriceFmt,
                product.campaign.discountedPercentage.toIntOrZero(),
                product.stats.averageRating,
                soldLabel,
                product.appLink,
                showProductInfo,
                product.productId,
                overrideTheme,
                colorSchema
            )
        }

        return products
    }

    private fun List<LabelGroup>.soldCount() : String {
        val soldLabels = filter { labelGroup ->
            labelGroup.title.contains(LABEL_TITLE_PRODUCT_SOLD_COUNT, true)
        }
        val soldLabel = soldLabels.getOrNull(FIRST_LABEL_INDEX)
        val soldLabelTitle = soldLabel?.title.orEmpty()
        return soldLabelTitle
    }
}
