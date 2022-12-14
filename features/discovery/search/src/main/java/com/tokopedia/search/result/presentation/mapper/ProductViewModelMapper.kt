package com.tokopedia.search.result.presentation.mapper

import com.tokopedia.discovery.common.constants.SearchConstant.InspirationCarousel.TYPE_ANNOTATION_PRODUCT_COLOR_CHIPS
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.search.result.domain.model.SearchProductModel
import com.tokopedia.search.result.domain.model.SearchProductModel.InspirationCarouselData
import com.tokopedia.search.result.domain.model.SearchProductModel.InspirationCarouselOption
import com.tokopedia.search.result.domain.model.SearchProductModel.Product
import com.tokopedia.search.result.domain.model.SearchProductModel.ProductBadge
import com.tokopedia.search.result.domain.model.SearchProductModel.ProductFreeOngkir
import com.tokopedia.search.result.domain.model.SearchProductModel.ProductLabelGroup
import com.tokopedia.search.result.domain.model.SearchProductModel.ProductLabelGroupVariant
import com.tokopedia.search.result.domain.model.SearchProductModel.SearchInspirationCarousel
import com.tokopedia.search.result.domain.model.SearchProductModel.SearchProductData
import com.tokopedia.search.result.presentation.model.BadgeItemDataView
import com.tokopedia.search.result.presentation.model.FreeOngkirDataView
import com.tokopedia.search.result.presentation.model.LabelGroupDataView
import com.tokopedia.search.result.presentation.model.LabelGroupVariantDataView
import com.tokopedia.search.result.presentation.model.ProductDataView
import com.tokopedia.search.result.presentation.model.ProductItemDataView
import com.tokopedia.search.result.presentation.model.TickerDataView
import com.tokopedia.search.result.product.banner.BannerDataView
import com.tokopedia.search.result.product.broadmatch.RelatedDataView
import com.tokopedia.search.result.product.globalnavwidget.GlobalNavDataView
import com.tokopedia.search.result.product.inspirationcarousel.InspirationCarouselDataView
import com.tokopedia.search.result.product.inspirationcarousel.InspirationCarouselProductDataViewMapper
import com.tokopedia.search.result.product.inspirationwidget.InspirationWidgetVisitable
import com.tokopedia.search.result.product.lastfilter.LastFilterDataView
import com.tokopedia.search.result.product.suggestion.SuggestionDataView
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
        isLocalSearchRecommendation: Boolean,
        externalReference: String,
    ): ProductDataView {
        val (searchProductHeader, searchProductData) = searchProductModel.searchProduct

        val productDataView = ProductDataView()

        productDataView.adsModel = searchProductModel.topAdsModel
        productDataView.globalNavDataView = GlobalNavDataView.create(
            searchProductModel.globalSearchNavigation,
            dimension90,
        )
        productDataView.cpmModel = searchProductModel.cpmModel
        productDataView.relatedDataView = RelatedDataView.create(
            searchProductData.related,
            isLocalSearch,
            dimension90,
            keyword,
            externalReference,
        )
        productDataView.productList = convertToProductItemDataViewList(
            lastProductItemPosition,
            searchProductData.productList,
            pageTitle,
            dimension90,
            isLocalSearchRecommendation,
            searchProductModel.getProductListType(),
            externalReference,
            searchProductData.keywordIntention,
            searchProductModel.isShowButtonAtc,
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
        productDataView.pageComponentId = searchProductHeader.componentId
        productDataView.isQuerySafe = searchProductData.isQuerySafe
        productDataView.inspirationCarouselDataView = convertToInspirationCarouselViewModel(
            searchProductModel.searchInspirationCarousel,
            dimension90,
            externalReference,
            keyword,
        )
        productDataView.inspirationWidgetDataView = InspirationWidgetVisitable.create(
            searchProductModel.searchInspirationWidget,
            keyword,
            dimension90,
        )
        productDataView.additionalParams = searchProductHeader.additionalParams
        productDataView.autocompleteApplink = searchProductData.autocompleteApplink
        productDataView.defaultView = searchProductHeader.defaultView
        productDataView.bannerDataView = BannerDataView.create(
            searchProductData.banner,
            keyword,
            dimension90,
            pageTitle,
        )
        productDataView.lastFilterDataView = convertToLastFilterDataView(
            searchProductModel,
            keyword,
            dimension90,
        )
        productDataView.categoryIdL2 = searchProductModel.lastFilter.data.categoryIdL2
        productDataView.violation = convertToViolationView(searchProductData.violation)
        productDataView.backendFilters = searchProductModel.backendFilters
        productDataView.keywordIntention = searchProductModel.keywordIntention

        return productDataView
    }

    private fun convertToProductItemDataViewList(
            lastProductItemPosition: Int,
            productModels: List<Product>,
            pageTitle: String,
            dimension90: String,
            isLocalSearchRecommendation: Boolean,
            productListType: String,
            externalReference: String,
            keywordIntention: Int,
            showButtonAtc: Boolean,
    ): List<ProductItemDataView> {
        return productModels.mapIndexed { index, productModel ->
            val position = lastProductItemPosition + index + 1
            convertToProductItem(
                productModel,
                position,
                pageTitle,
                dimension90,
                isLocalSearchRecommendation,
                productListType,
                externalReference,
                keywordIntention,
                showButtonAtc
            )
        }
    }

    private fun convertToProductItem(
            productModel: Product,
            position: Int,
            pageTitle: String,
            dimension90: String,
            isLocalSearchRecommendation: Boolean,
            productListType: String,
            externalReference: String,
            keywordIntention: Int,
            showButtonAtc: Boolean,
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
        productItem.pageTitle = if(isLocalSearchRecommendation) pageTitle else ""
        productItem.dimension90 = dimension90
        productItem.applink = productModel.applink
        productItem.customVideoURL = productModel.customVideoURL
        productItem.productListType = productListType
        productItem.dimension131 = externalReference
        productItem.keywordIntention = keywordIntention
        productItem.showButtonAtc = showButtonAtc
        productItem.parentId = productModel.parentId
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
            externalReference: String,
            keyword: String,
    ): List<InspirationCarouselDataView> {
        return searchInspirationCarousel.data.map { data ->
            InspirationCarouselDataView(
                data.title,
                data.type,
                data.position,
                data.layout,
                data.trackingOption.toIntOrZero(),
                convertToInspirationCarouselOptionViewModel(
                    data,
                    dimension90,
                    externalReference,
                    keyword,
                ),
            )
        }
    }

    private fun convertToInspirationCarouselOptionViewModel(
            data: InspirationCarouselData,
            dimension90: String,
            externalReference: String,
            keyword: String,
    ): List<InspirationCarouselDataView.Option> {
        val mapper = InspirationCarouselProductDataViewMapper()

        return data.inspirationCarouselOptions.mapIndexed { index, opt ->
            val position = index + 1
            val isChipsActive = index == 0
            InspirationCarouselDataView.Option(
                opt.title,
                opt.subtitle,
                opt.iconSubtitle,
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
                    externalReference,
                    data.trackingOption.toIntOrZero()
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
                createInspirationCarouselCardButtonViewModel(opt),
                InspirationCarouselDataView.Bundle.create(opt),
                keyword,
                externalReference,
            )
        }
    }

    private fun createInspirationCarouselCardButtonViewModel(
        option: InspirationCarouselOption,
    ): InspirationCarouselDataView.CardButton {
        return InspirationCarouselDataView.CardButton(
            option.cardButton.title,
            option.cardButton.applink,
        )
    }

    private fun convertToLastFilterDataView(
        searchProductModel: SearchProductModel,
        keyword: String,
        dimension90: String,
    ): LastFilterDataView {
        return LastFilterDataView.create(
            searchProductModel.lastFilter,
            keyword,
            dimension90
        )
    }

    private fun convertToViolationView(violation: SearchProductModel.Violation) : ViolationDataView? {
        return ViolationDataView.create(violation)
    }
}
