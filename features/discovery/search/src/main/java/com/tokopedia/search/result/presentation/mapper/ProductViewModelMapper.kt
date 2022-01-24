package com.tokopedia.search.result.presentation.mapper

import com.tokopedia.discovery.common.constants.SearchConstant.InspirationCarousel.TYPE_ANNOTATION_PRODUCT_COLOR_CHIPS
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.search.result.domain.model.SearchProductModel
import com.tokopedia.search.result.domain.model.SearchProductModel.Banner
import com.tokopedia.search.result.domain.model.SearchProductModel.GlobalNavItem
import com.tokopedia.search.result.domain.model.SearchProductModel.GlobalSearchNavigation
import com.tokopedia.search.result.domain.model.SearchProductModel.InspirationCardOption
import com.tokopedia.search.result.domain.model.SearchProductModel.InspirationCarouselData
import com.tokopedia.search.result.domain.model.SearchProductModel.OtherRelated
import com.tokopedia.search.result.domain.model.SearchProductModel.OtherRelatedProduct
import com.tokopedia.search.result.domain.model.SearchProductModel.OtherRelatedProductBadge
import com.tokopedia.search.result.domain.model.SearchProductModel.OtherRelatedProductFreeOngkir
import com.tokopedia.search.result.domain.model.SearchProductModel.Product
import com.tokopedia.search.result.domain.model.SearchProductModel.ProductBadge
import com.tokopedia.search.result.domain.model.SearchProductModel.ProductFreeOngkir
import com.tokopedia.search.result.domain.model.SearchProductModel.ProductLabelGroup
import com.tokopedia.search.result.domain.model.SearchProductModel.ProductLabelGroupVariant
import com.tokopedia.search.result.domain.model.SearchProductModel.Related
import com.tokopedia.search.result.domain.model.SearchProductModel.SearchInspirationCarousel
import com.tokopedia.search.result.domain.model.SearchProductModel.SearchInspirationWidget
import com.tokopedia.search.result.domain.model.SearchProductModel.SearchProductData
import com.tokopedia.search.result.presentation.model.BadgeItemDataView
import com.tokopedia.search.result.presentation.model.BannerDataView
import com.tokopedia.search.result.presentation.model.BroadMatch
import com.tokopedia.search.result.presentation.model.BroadMatchDataView
import com.tokopedia.search.result.presentation.model.BroadMatchItemDataView
import com.tokopedia.search.result.presentation.model.BroadMatchProduct
import com.tokopedia.search.result.presentation.model.FreeOngkirDataView
import com.tokopedia.search.result.presentation.model.GlobalNavDataView
import com.tokopedia.search.result.presentation.model.InspirationCardDataView
import com.tokopedia.search.result.presentation.model.InspirationCardOptionDataView
import com.tokopedia.search.result.presentation.model.InspirationCarouselDataView
import com.tokopedia.search.result.presentation.model.LabelGroupDataView
import com.tokopedia.search.result.presentation.model.LabelGroupVariantDataView
import com.tokopedia.search.result.presentation.model.LastFilterDataView
import com.tokopedia.search.result.presentation.model.ProductDataView
import com.tokopedia.search.result.presentation.model.ProductItemDataView
import com.tokopedia.search.result.presentation.model.RelatedDataView
import com.tokopedia.search.result.presentation.model.SuggestionDataView
import com.tokopedia.search.result.presentation.model.TickerDataView
import com.tokopedia.search.result.product.violation.ViolationDataView

class ProductViewModelMapper {

    @Suppress("LongParameterList")
    fun convertToProductViewModel(
        lastProductItemPosition: Int,
        searchProductModel: SearchProductModel,
        pageTitle: String,
        isLocalSearch: Boolean,
        dimension90: String,
        keyword: String,
    ): ProductDataView {
        val (searchProductHeader, searchProductData) = searchProductModel.searchProduct

        val productDataView = ProductDataView()

        productDataView.adsModel = searchProductModel.topAdsModel
        productDataView.globalNavDataView = convertToViewModel(
            searchProductModel.globalSearchNavigation
        )
        productDataView.cpmModel = searchProductModel.cpmModel
        productDataView.relatedDataView = convertToRelatedViewModel(
            searchProductData.related,
            isLocalSearch,
            dimension90,
            keyword,
        )
        productDataView.productList = convertToProductItemDataViewList(
            lastProductItemPosition,
            searchProductData.productList,
            pageTitle,
            dimension90,
        )
        productDataView.tickerModel = convertToTickerDataView(
            searchProductData,
            keyword,
            dimension90,
        )
        productDataView.suggestionModel = convertToSuggestionDataView(
            searchProductModel,
            keyword,
            dimension90,
        )
        productDataView.totalData = searchProductHeader.totalData
        productDataView.totalDataText = searchProductHeader.totalDataText
        productDataView.responseCode = searchProductHeader.responseCode
        productDataView.keywordProcess = searchProductHeader.keywordProcess
        productDataView.errorMessage = searchProductHeader.errorMessage
        productDataView.pageComponentId = searchProductHeader.componentId
        productDataView.isQuerySafe = searchProductData.isQuerySafe
        productDataView.inspirationCarouselDataView = convertToInspirationCarouselViewModel(
            searchProductModel.searchInspirationCarousel,
            dimension90,
        )
        productDataView.inspirationCardDataView = convertToInspirationCardViewModel(
            searchProductModel.searchInspirationWidget
        )
        productDataView.additionalParams = searchProductHeader.additionalParams
        productDataView.autocompleteApplink = searchProductData.autocompleteApplink
        productDataView.defaultView = searchProductHeader.defaultView
        productDataView.bannerDataView = convertToBannerDataView(searchProductData.banner)
        productDataView.lastFilterDataView = convertToLastFilterDataView(searchProductModel)
        productDataView.categoryIdL2 = searchProductModel.lastFilter.data.categoryIdL2
        productDataView.violation = convertToViolationView(searchProductData.violation)

        return productDataView
    }

    private fun convertToViewModel(globalSearchNavigation: GlobalSearchNavigation): GlobalNavDataView? {
        return if (globalSearchNavigation.data.globalNavItems.isNotEmpty())
            GlobalNavDataView(
                    globalSearchNavigation.data.source,
                    globalSearchNavigation.data.title,
                    globalSearchNavigation.data.keyword,
                    globalSearchNavigation.data.navTemplate,
                    globalSearchNavigation.data.background,
                    globalSearchNavigation.data.seeAllApplink,
                    globalSearchNavigation.data.seeAllUrl,
                    globalSearchNavigation.data.isShowTopAds,
                    convertToViewModel(globalSearchNavigation.data.globalNavItems)
            )
        else null
    }

    private fun convertToViewModel(globalNavItems: List<GlobalNavItem>): List<GlobalNavDataView.Item> {
        return globalNavItems.mapIndexed { index, globalNavItem ->
            val position = index + 1

            GlobalNavDataView.Item(
                    globalNavItem.categoryName,
                    globalNavItem.name,
                    globalNavItem.info,
                    globalNavItem.imageUrl,
                    globalNavItem.applink,
                    globalNavItem.url,
                    globalNavItem.subtitle,
                    globalNavItem.strikethrough,
                    globalNavItem.backgroundUrl,
                    globalNavItem.logoUrl,
                    position,
            )
        }
    }

    private fun convertToRelatedViewModel(
        related: Related,
        isLocalSearch: Boolean,
        dimension90: String,
        actualKeyword: String,
    ): RelatedDataView =
        RelatedDataView(
            related.relatedKeyword,
            related.position,
            related.otherRelatedList.map {
                convertToBroadMatchViewModel(
                    it,
                    isLocalSearch,
                    dimension90,
                    related.trackingOption,
                    actualKeyword,
                )
            }
        )

    private fun convertToBroadMatchViewModel(
        otherRelated: OtherRelated,
        isLocalSearch: Boolean,
        dimension90: String,
        trackingOption: Int,
        actualKeyword: String,
    ): BroadMatchDataView {
        val broadMatchItemDataViewList = otherRelated.productList.mapIndexed { index, otherRelatedProduct ->
            val position = index + 1
            convertToBroadMatchItemViewModel(
                otherRelatedProduct,
                position,
                otherRelated.keyword,
                dimension90,
            )
        }

        return BroadMatchDataView(
            keyword = otherRelated.keyword,
            url = otherRelated.url,
            applink = otherRelated.applink,
            isAppendTitleInTokopedia = isLocalSearch,
            broadMatchItemDataViewList = broadMatchItemDataViewList,
            dimension90 = dimension90,
            carouselOptionType = BroadMatch,
            componentId = otherRelated.componentId,
            trackingOption = trackingOption,
            actualKeyword = actualKeyword,
        )
    }

    private fun convertToBroadMatchItemViewModel(
        otherRelatedProduct: OtherRelatedProduct,
        position: Int,
        alternativeKeyword: String,
        dimension90: String,
    ): BroadMatchItemDataView {
        val isOrganicAds = otherRelatedProduct.isOrganicAds()

        return BroadMatchItemDataView(
            id = otherRelatedProduct.id,
            name = otherRelatedProduct.name,
            price = otherRelatedProduct.price,
            imageUrl = otherRelatedProduct.imageUrl,
            url = otherRelatedProduct.url,
            applink = otherRelatedProduct.applink,
            priceString = otherRelatedProduct.priceString,
            shopLocation = otherRelatedProduct.shop.city,
            badgeItemDataViewList = otherRelatedProduct.badgeList.mapToBadgeItemDataViewList(),
            freeOngkirDataView = otherRelatedProduct.freeOngkir.mapToFreeOngkirDataView(),
            isWishlisted = otherRelatedProduct.isWishlisted,
            position = position,
            alternativeKeyword = alternativeKeyword,
            isOrganicAds = isOrganicAds,
            topAdsViewUrl = otherRelatedProduct.ads.productViewUrl,
            topAdsClickUrl = otherRelatedProduct.ads.productClickUrl,
            topAdsWishlistUrl = otherRelatedProduct.ads.productWishlistUrl,
            ratingAverage = otherRelatedProduct.ratingAverage,
            labelGroupDataList = otherRelatedProduct.labelGroupList.mapToLabelGroupDataViewList(),
            carouselProductType = BroadMatchProduct(),
            dimension90 = dimension90,
            componentId = otherRelatedProduct.componentId,
        )
    }

    private fun List<OtherRelatedProductBadge>.mapToBadgeItemDataViewList(): List<BadgeItemDataView> {
        return map { BadgeItemDataView(it.imageUrl, "", it.isShown) }
    }

    private fun OtherRelatedProductFreeOngkir.mapToFreeOngkirDataView() =
            FreeOngkirDataView(isActive, imageUrl)

    private fun convertToProductItemDataViewList(
            lastProductItemPosition: Int,
            productModels: List<Product>,
            pageTitle: String,
            dimension90: String,
    ): List<ProductItemDataView> {
        return productModels.mapIndexed { index, productModel ->
            val position = lastProductItemPosition + index + 1
            convertToProductItem(productModel, position, pageTitle, dimension90,)
        }
    }

    private fun convertToProductItem(
            productModel: Product,
            position: Int,
            pageTitle: String,
            dimension90: String,
    ): ProductItemDataView {
        val productItem = ProductItemDataView()
        productItem.productID = productModel.id
        productItem.warehouseID = productModel.warehouseIdDefault
        productItem.productName = productModel.name
        productItem.imageUrl = productModel.imageUrl
        productItem.imageUrl300 = productModel.imageUrl300
        productItem.imageUrl700 = productModel.imageUrl700
        productItem.ratingString = productModel.ratingAverage
        productItem.discountPercentage = productModel.discountPercentage
        productItem.originalPrice = productModel.originalPrice
        productItem.price = productModel.price
        productItem.priceInt = productModel.priceInt
        productItem.priceRange = productModel.priceRange
        productItem.shopID = productModel.shop.id
        productItem.shopName = productModel.shop.name
        productItem.shopCity = productModel.shop.city
        productItem.shopUrl = productModel.shop.url
        productItem.isShopOfficialStore = productModel.shop.isOfficial
        productItem.isShopPowerMerchant = productModel.shop.isPowerBadge
        productItem.isWishlisted = productModel.isWishlist
        productItem.badgesList = productModel.badgeList.mapToBadgeItemList()
        productItem.position = position
        productItem.categoryID = productModel.categoryId
        productItem.categoryName = productModel.categoryName
        productItem.categoryBreadcrumb = productModel.categoryBreadcrumb
        productItem.labelGroupList = productModel.labelGroupList.mapToLabelGroupDataViewList()
        productItem.labelGroupVariantList = productModel.labelGroupVariantList.mapToLabelGroupVariantList()
        productItem.freeOngkirDataView = productModel.freeOngkir.mapToFreeOngkirDataView()
        productItem.boosterList = productModel.boosterList
        productItem.sourceEngine = productModel.sourceEngine
        productItem.isOrganicAds = productModel.isOrganicAds()
        productItem.topadsImpressionUrl = productModel.ads.productViewUrl
        productItem.topadsClickUrl = productModel.ads.productClickUrl
        productItem.topadsWishlistUrl = productModel.ads.productWishlistUrl
        productItem.topadsTag = productModel.ads.tag
        productItem.minOrder = productModel.minOrder
        productItem.productUrl = productModel.url
        productItem.pageTitle = pageTitle
        productItem.dimension90 = dimension90
        productItem.applink = productModel.applink
        return productItem
    }

    private fun List<ProductBadge>.mapToBadgeItemList() =
            this.map { badgeModel ->
                BadgeItemDataView(badgeModel.imageUrl, badgeModel.title, badgeModel.isShown)
            }

    private fun List<ProductLabelGroup>.mapToLabelGroupDataViewList() =
            this.map { labelGroupModel ->
                LabelGroupDataView(
                        labelGroupModel.position,
                        labelGroupModel.type,
                        labelGroupModel.title,
                        labelGroupModel.url
                )
            }

    private fun List<ProductLabelGroupVariant>.mapToLabelGroupVariantList() =
            this.map { labelGroupVariant ->
                LabelGroupVariantDataView(
                        labelGroupVariant.title,
                        labelGroupVariant.type,
                        labelGroupVariant.typeVariant,
                        labelGroupVariant.hexColor
                )
            }

    private fun ProductFreeOngkir.mapToFreeOngkirDataView() = FreeOngkirDataView(isActive, imageUrl)

    private fun convertToTickerDataView(
        searchProductData: SearchProductData,
        keyword: String,
        dimension90: String,
    ): TickerDataView {
        val ticker = searchProductData.ticker
        return TickerDataView(
            text = ticker.text,
            query = ticker.query,
            typeId = ticker.typeId,
            componentId = ticker.componentId,
            trackingOption = ticker.trackingOption,
            keyword = keyword,
            dimension90 = dimension90,
        )
    }

    private fun convertToSuggestionDataView(
        searchProductModel: SearchProductModel,
        keyword: String,
        dimension90: String,
    ): SuggestionDataView {
        val (searchProductHeader, searchProductData) = searchProductModel.searchProduct
        val suggestion = searchProductData.suggestion
        val trackingValue =
            if (searchProductHeader.responseCode == "3")
                searchProductData.related.relatedKeyword
            else
                searchProductData.suggestion.suggestion

        return SuggestionDataView(
            suggestionText = suggestion.text,
            suggestedQuery = suggestion.query,
            suggestion = suggestion.suggestion,
            componentId = suggestion.componentId,
            trackingOption = suggestion.trackingOption,
            keyword = keyword,
            dimension90 = dimension90,
            trackingValue = trackingValue,
        )
    }

    private fun convertToInspirationCarouselViewModel(
            searchInspirationCarousel: SearchInspirationCarousel,
            dimension90: String,
    ): List<InspirationCarouselDataView> {
        return searchInspirationCarousel.data.map { data ->
            InspirationCarouselDataView(
                    data.title,
                    data.type,
                    data.position,
                    data.layout,
                    data.trackingOption.toIntOrZero(),
                    convertToInspirationCarouselOptionViewModel(data, dimension90),
            )
        }
    }

    private fun convertToInspirationCarouselOptionViewModel(
            data: InspirationCarouselData,
            dimension90: String,
    ): List<InspirationCarouselDataView.Option> {
        val mapper = InspirationCarouselProductDataViewMapper()

        return data.inspirationCarouselOptions.mapIndexed { index, opt ->
            val position = index + 1
            val isChipsActive = index == 0
            InspirationCarouselDataView.Option(
                    opt.title,
                    opt.url,
                    opt.applink,
                    opt.bannerImageUrl,
                    opt.bannerLinkUrl,
                    opt.bannerApplinkUrl,
                    opt.identifier,
                    mapper.convertToInspirationCarouselProductDataView(
                        opt.inspirationCarouselProducts,
                        position,
                        data.type,
                        data.layout,
                        { it.mapToLabelGroupDataViewList() },
                        opt.title,
                        data.title,
                        dimension90,
                    ),
                    data.type,
                    data.layout,
                    data.position,
                    data.title,
                    position,
                    isChipsActive,
                    if (data.type == TYPE_ANNOTATION_PRODUCT_COLOR_CHIPS) opt.meta else "",
                    if (data.type != TYPE_ANNOTATION_PRODUCT_COLOR_CHIPS) opt.meta else "",
                    opt.componentId,
                    data.trackingOption.toIntOrZero(),
                    dimension90,
            )
        }
    }

    private fun convertToInspirationCardViewModel(
            searchInspirationWidget: SearchInspirationWidget
    ): List<InspirationCardDataView> {
        return searchInspirationWidget.data.map { data ->
            InspirationCardDataView(
                    data.title,
                    data.type,
                    data.position,
                    data.inspiratioWidgetOptions.mapToInspirationCardOptionDataView(data.type)
            )
        }
    }

    private fun List<InspirationCardOption>.mapToInspirationCardOptionDataView(
            inspirationCardType: String
    ) = this.map { optionModel ->
            InspirationCardOptionDataView(
                    optionModel.text,
                    optionModel.img,
                    optionModel.url,
                    optionModel.color,
                    optionModel.applink,
                    inspirationCardType,
            )
        }

    private fun convertToBannerDataView(bannerModel: Banner): BannerDataView {
        return BannerDataView(
                bannerModel.position,
                bannerModel.text,
                bannerModel.applink,
                bannerModel.imageUrl
        )
    }

    private fun convertToLastFilterDataView(searchProductModel: SearchProductModel): LastFilterDataView {
        val lastFilterData = searchProductModel.lastFilter.data

        return LastFilterDataView(
            filterList = lastFilterData.filters,
            title = lastFilterData.title,
        )
    }

    private fun convertToViolationView(violation: SearchProductModel.Violation) : ViolationDataView? {
        return ViolationDataView.create(violation)
    }
}