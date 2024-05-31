package com.tokopedia.shop.home.view.viewmodel

import android.annotation.SuppressLint
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.kotlin.extensions.orFalse
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel
import com.tokopedia.shop.common.constant.ShopParamApiConstant
import com.tokopedia.shop.common.data.source.cloud.model.FreeOngkir
import com.tokopedia.shop.common.data.source.cloud.model.LabelGroup
import com.tokopedia.shop.common.data.source.cloud.model.LabelGroupStyle
import com.tokopedia.shop.common.view.model.ShopPageColorSchema
import com.tokopedia.shop.home.view.model.banner_product_group.BannerProductGroupUiModel
import com.tokopedia.shop.home.view.model.banner_product_group.BannerProductGroupUiModel.Tab.ComponentList.ComponentName
import com.tokopedia.shop.home.view.model.banner_product_group.BannerProductGroupUiModel.Tab.ComponentList.Data.LinkType
import com.tokopedia.shop.home.view.model.banner_product_group.appearance.ProductItemType
import com.tokopedia.shop.home.view.model.banner_product_group.appearance.ShopHomeBannerProductGroupItemType
import com.tokopedia.shop.home.view.model.banner_product_group.appearance.VerticalBannerItemType
import com.tokopedia.shop.product.data.model.ShopFeaturedProductParams
import com.tokopedia.shop.product.data.source.cloud.model.ShopProductFilterInput
import com.tokopedia.shop.product.domain.interactor.GetShopFeaturedProductUseCase
import com.tokopedia.shop.product.domain.interactor.GqlGetShopProductUseCase
import com.tokopedia.shop.product.view.datamodel.ShopBadgeUiModel
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.SupervisorJob
import javax.inject.Inject
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
        private const val FIRST_PAGE = 1
        private const val PRODUCT_COUNT_TO_FETCH = 10
    }

    private val _carouselWidgets = MutableLiveData<UiState>()
    val carouselWidgets: LiveData<UiState>
        get() = _carouselWidgets

    private val _verticalProductCarousel = MutableLiveData<List<ShopHomeBannerProductGroupItemType>>()
    val verticalProductCarousel: LiveData<List<ShopHomeBannerProductGroupItemType>>
        get() = _verticalProductCarousel

    sealed class UiState {
        object Loading : UiState()
        data class Success(val data: List<ShopHomeBannerProductGroupItemType>) : UiState()
        data class Error(val error: Throwable) : UiState()
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

        val listProductMetadata = getListProductMetadata(widgets)

        if (!listProductMetadata.isNullOrEmpty()) {
            launchCatchError(
                context = dispatcherProvider.io,
                block = {

                    val products = getProductsByProductMetadata(shopId, userAddress, listProductMetadata, overrideTheme, colorSchema)

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

            listOf(VerticalBannerItemType(componentId, componentName, bannerImageUrl, ctaLink, Int.ZERO))
        }
    }

    fun refreshVerticalBannerHeight(productCardHeight: Int) {
        val currentCarouselWidgets = _carouselWidgets.value

        val widgets = if (currentCarouselWidgets is UiState.Success) {
            currentCarouselWidgets.data
        } else {
            emptyList()
        }

        val updatedProductCarousels = widgets.map { itemType ->
            if (itemType is VerticalBannerItemType) {
                itemType.copy(verticalBannerHeight = productCardHeight)
            } else {
                itemType
            }
        }

        _verticalProductCarousel.postValue(updatedProductCarousels)
    }

    private fun getListProductMetadata(
        widgets: List<BannerProductGroupUiModel.Tab.ComponentList>
    ): List<BannerProductGroupUiModel.Tab.ComponentList.Data>? {
        val productComponents = widgets.filter { widget -> widget.componentName == ComponentName.PRODUCT }

        val product = productComponents.getOrNull(0) ?: return null

        return product.data
    }

    private suspend fun getProductsByProductMetadata(
        shopId: String,
        userAddress: LocalCacheModel,
        listProductMetaData: List<BannerProductGroupUiModel.Tab.ComponentList.Data>,
        overrideTheme: Boolean,
        colorSchema: ShopPageColorSchema
    ): List<ProductItemType> {
        val showProductInfo = listProductMetaData.firstOrNull()?.isShowProductInfo.orFalse()
        return when (listProductMetaData.firstOrNull()?.linkType) {
            LinkType.FEATURED_PRODUCT -> {
                val featuredProducts = getFeaturedProducts(
                    shopId,
                    userSession.userId,
                    userAddress,
                    showProductInfo,
                    overrideTheme,
                    colorSchema
                )
                featuredProducts
            }

            else -> {
                var sortId = ""
                var showcaseId = ""
                listProductMetaData.find { it.linkType == LinkType.SHOWCASE }?.let {
                    showcaseId = it.linkId
                }
                listProductMetaData.find { it.linkType == LinkType.PRODUCT }?.let {
                    sortId = it.linkId
                }
                listProductMetaData.find { it.linkType == LinkType.SORT }?.let {
                    sortId = it.linkId
                }
                getShowcaseProduct(
                    shopId,
                    showcaseId,
                    sortId,
                    userAddress,
                    showProductInfo,
                    overrideTheme,
                    colorSchema
                )
            }
        }
    }

    private suspend fun getShowcaseProduct(
        shopId: String,
        showcaseId: String,
        sortId: String,
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
                sort = sortId.toIntOrZero()
                perPage = PRODUCT_COUNT_TO_FETCH
                userDistrictId = userAddress.district_id
                userCityId = userAddress.city_id
                userLat = userAddress.lat
                userLong = userAddress.long
                usecase = ShopParamApiConstant.SHOP_GET_PRODUCT_V2
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
                productId = product.productId,
                imageUrl = product.imageUri,
                name = product.name,
                price = product.price,
                slashedPrice = product.originalPrice,
                slashedPricePercent = product.percentageAmount,
                rating = product.ratingAverage,
                appLink = "tokopedia://product/${product.productId}",
                showProductInfo = showProductInfo,
                labelGroups = product.labelGroupList.map { labelGroup ->
                    LabelGroup(
                        position = labelGroup.position,
                        type = labelGroup.type,
                        title = labelGroup.title,
                        url = labelGroup.url,
                        styles = labelGroup.styles.map { style ->
                            LabelGroupStyle(key = style.key, value = style.value)
                        }
                    )
                },
                badges = emptyList(),
                id = product.productId,
                overrideTheme = overrideTheme,
                colorSchema = colorSchema,
                freeOngkir = FreeOngkir(
                    isActive = product.freeOngkir.isActive,
                    imgUrl = product.freeOngkir.imgUrl
                )
            )
        }

        return featuredProducts
    }

    private suspend fun getProducts(
        showProductInfo: Boolean,
        overrideTheme: Boolean,
        colorSchema: ShopPageColorSchema,
        params: Map<String, Any>
    ): List<ProductItemType> {
        getShopProductUseCase.params = params

        val response = getShopProductUseCase.executeOnBackground()

        val products = response.data.map { product ->

            ProductItemType(
                productId = product.productId,
                imageUrl = product.primaryImage.thumbnail,
                name = product.name,
                price = product.price.textIdr,
                slashedPrice = product.campaign.originalPriceFmt,
                slashedPricePercent = product.campaign.discountedPercentage.toIntOrZero(),
                rating = product.stats.averageRating,
                appLink = product.appLink,
                showProductInfo = showProductInfo,
                labelGroups = product.labelGroupList.map { labelGroup ->
                    LabelGroup(
                        position = labelGroup.position,
                        type = labelGroup.type,
                        title = labelGroup.title,
                        url = labelGroup.url,
                        styles = labelGroup.styles.map { style ->
                            LabelGroupStyle(key = style.key, value = style.value)
                        }
                    )
                },
                badges = listOf(
                    ShopBadgeUiModel(
                        title = product.badge.title,
                        imageUrl = product.badge.imageUrl,
                        show = product.badge.show
                    ))
                ,
                id = product.productId,
                overrideTheme = overrideTheme,
                colorSchema = colorSchema,
                freeOngkir = FreeOngkir(
                    isActive = product.freeOngkir.isActive,
                    imgUrl = product.freeOngkir.imgUrl
                )
            )
        }

        return products
    }
}
