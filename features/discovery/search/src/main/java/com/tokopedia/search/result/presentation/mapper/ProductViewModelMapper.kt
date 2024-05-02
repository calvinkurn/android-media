package com.tokopedia.search.result.presentation.mapper

import com.tokopedia.discovery.common.constants.SearchConstant.InspirationCarousel.TYPE_ANNOTATION_PRODUCT_COLOR_CHIPS
import com.tokopedia.discovery.common.constants.SearchConstant.ProductListType.FIXED_GRID
import com.tokopedia.discovery.common.reimagine.ReimagineRollence
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationAdsLog
import com.tokopedia.search.result.domain.model.SearchProductModel
import com.tokopedia.search.result.domain.model.SearchProductModel.InspirationCarouselData
import com.tokopedia.search.result.domain.model.SearchProductModel.InspirationCarouselOption
import com.tokopedia.search.result.domain.model.SearchProductModel.Product
import com.tokopedia.search.result.domain.model.SearchProductModel.ProductBadge
import com.tokopedia.search.result.domain.model.SearchProductModel.ProductFreeOngkir
import com.tokopedia.search.result.domain.model.SearchProductModel.ProductLabelGroup
import com.tokopedia.search.result.domain.model.SearchProductModel.ProductLabelGroupVariant
import com.tokopedia.search.result.domain.model.SearchProductModel.SearchInspirationCarousel
import com.tokopedia.search.result.domain.model.SearchProductV5
import com.tokopedia.search.result.presentation.model.BadgeItemDataView
import com.tokopedia.search.result.presentation.model.FreeOngkirDataView
import com.tokopedia.search.result.presentation.model.LabelGroupDataView
import com.tokopedia.search.result.presentation.model.LabelGroupVariantDataView
import com.tokopedia.search.result.presentation.model.ProductDataView
import com.tokopedia.search.result.presentation.model.ProductItemDataView
import com.tokopedia.search.result.presentation.model.StyleDataView
import com.tokopedia.search.result.presentation.model.TickerDataView
import com.tokopedia.search.result.product.banner.BannerDataView
import com.tokopedia.search.result.product.broadmatch.RelatedDataView
import com.tokopedia.search.result.product.byteio.ByteIOTrackingData
import com.tokopedia.search.result.product.globalnavwidget.GlobalNavDataView
import com.tokopedia.search.result.product.inspirationcarousel.InspirationCarouselDataView
import com.tokopedia.search.result.product.inspirationcarousel.InspirationCarouselProductDataViewMapper
import com.tokopedia.search.result.product.inspirationwidget.InspirationWidgetVisitable
import com.tokopedia.search.result.product.lastfilter.LastFilterDataView
import com.tokopedia.search.result.product.suggestion.SuggestionDataView
import com.tokopedia.search.result.product.violation.ViolationDataView

class ProductViewModelMapper(
    private val reimagineRollence: ReimagineRollence,
) {

    private val isUseAceSearchProductV5: Boolean
        get() = reimagineRollence.search3ProductCard().isUseAceSearchProductV5()

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
        newCardType: String = "",
        isEnableAdultContent: Boolean,
        byteIOTrackingData: ByteIOTrackingData,
    ): ProductDataView {
        val productDataView = ProductDataView()
        val productListType =
            if (isUseAceSearchProductV5) FIXED_GRID
            else newCardType.ifBlank { searchProductModel.productListType() }

        productDataView.adsModel = searchProductModel.topAdsModel
        productDataView.globalNavDataView = GlobalNavDataView.create(
            searchProductModel.globalSearchNavigation,
            dimension90,
        )
        productDataView.cpmModel = searchProductModel.cpmModel
        productDataView.productList = convertToProductItemDataViewList(
            lastProductItemPosition,
            searchProductModel,
            pageTitle,
            dimension90,
            isLocalSearchRecommendation,
            productListType,
            externalReference,
            searchProductModel.keywordIntention(isUseAceSearchProductV5),
            searchProductModel.isShowButtonAtc(isUseAceSearchProductV5),
            if (isUseAceSearchProductV5) isEnableAdultContent else true,
            byteIOTrackingData,
        )

        val productCount =
            productDataView.productList.size + searchProductModel.topAdsModel.data.size

        productDataView.relatedDataView = relatedDataView(
            searchProductModel,
            isLocalSearch,
            dimension90,
            keyword,
            externalReference,
            byteIOTrackingData,
            productCount,
        )
        productDataView.tickerModel = convertToTickerDataView(
            searchProductModel,
            keyword,
            dimension90,
        )
        productDataView.suggestionModel = convertToSuggestionDataView(
            searchProductModel,
            keyword,
            dimension90,
        )
        productDataView.totalData = searchProductModel.totalData(isUseAceSearchProductV5)
        productDataView.responseCode = searchProductModel.responseCode(isUseAceSearchProductV5)
        productDataView.keywordProcess = searchProductModel.keywordProcess(isUseAceSearchProductV5)
        productDataView.pageComponentId = searchProductModel.componentId(isUseAceSearchProductV5)
        productDataView.isQuerySafe = searchProductModel.isQuerySafe(isUseAceSearchProductV5)
        val inspirationCarouselDataView = convertToInspirationCarouselDataView(
            searchProductModel.searchInspirationCarousel,
            dimension90,
            externalReference,
            keyword,
            byteIOTrackingData,
            productCount,
        )
        productDataView.inspirationCarouselDataView =
            inspirationCarouselDataView.filter { !it.isCarouselSeamlessLayout() }
        productDataView.seamlessCarouselDataViewList =
            inspirationCarouselDataView.filter(InspirationCarouselDataView::isCarouselSeamlessLayout)
        productDataView.inspirationWidgetDataView = InspirationWidgetVisitable.create(
            searchProductModel.searchInspirationWidget,
            keyword,
            dimension90,
        )
        productDataView.additionalParams =
            searchProductModel.additionalParams(isUseAceSearchProductV5)
        productDataView.autocompleteApplink =
            searchProductModel.autocompleteApplink(isUseAceSearchProductV5)
        productDataView.bannerDataView = BannerDataView.create(
            searchProductModel.banner(isUseAceSearchProductV5),
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
        productDataView.violation = convertToViolationView(searchProductModel)
        productDataView.backendFilters = searchProductModel.backendFilters(isUseAceSearchProductV5)
        productDataView.keywordIntention =
            searchProductModel.keywordIntention(isUseAceSearchProductV5)
        productDataView.isPostProcessing =
            searchProductModel.isPostProcessing(isUseAceSearchProductV5)
        productDataView.redirectApplink =
            searchProductModel.redirectApplink(isUseAceSearchProductV5)
        productDataView.productListType = productListType
        productDataView.isShowButtonAtc =
            searchProductModel.isShowButtonAtc(isUseAceSearchProductV5)
        productDataView.isReimagineProductCard =
            reimagineRollence.search3ProductCard().isReimagineProductCard()
        productDataView.keyword = keyword

        return productDataView
    }

    private fun relatedDataView(
        searchProductModel: SearchProductModel,
        isLocalSearch: Boolean,
        dimension90: String,
        keyword: String,
        externalReference: String,
        byteIOTrackingData: ByteIOTrackingData,
        productCount: Int,
    ): RelatedDataView =
        if (isUseAceSearchProductV5)
            RelatedDataView.create(
                searchProductModel.searchProductV5.data.related,
                isLocalSearch,
                dimension90,
                keyword,
                externalReference,
                byteIOTrackingData.copy(
                    isFirstPage = isWidgetInFirstPage(
                        byteIOTrackingData.isFirstPage,
                        searchProductModel.searchProductV5.data.related.position,
                        productCount,
                    ),
                )
            )
        else
            RelatedDataView.create(
                searchProductModel.searchProduct.data.related,
                isLocalSearch,
                dimension90,
                keyword,
                externalReference,
                byteIOTrackingData.copy(
                    isFirstPage = isWidgetInFirstPage(
                        byteIOTrackingData.isFirstPage,
                        searchProductModel.searchProductV5.data.related.position,
                        productCount,
                    ),
                )
            )

    private fun convertToProductItemDataViewList(
        lastProductItemPosition: Int,
        searchProductModel: SearchProductModel,
        pageTitle: String,
        dimension90: String,
        isLocalSearchRecommendation: Boolean,
        productListType: String,
        externalReference: String,
        keywordIntention: Int,
        showButtonAtc: Boolean,
        isEnableAdultContent: Boolean,
        byteIOTrackingData: ByteIOTrackingData,
    ): List<ProductItemDataView> {
        return if (isUseAceSearchProductV5) {
            searchProductModel.searchProductV5.data.productList.mapIndexed { index, productModel ->
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
                    isEnableAdultContent,
                    byteIOTrackingData,
                )
            }
        } else {
            searchProductModel.searchProduct.data.productList.mapIndexed { index, productModel ->
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
                    showButtonAtc,
                    byteIOTrackingData,
                )
            }
        }
    }

    private fun convertToProductItem(
        productModel: SearchProductV5.Data.Product,
        position: Int,
        pageTitle: String,
        dimension90: String,
        isLocalSearchRecommendation: Boolean,
        productListType: String,
        externalReference: String,
        keywordIntention: Int,
        isEnableAdultContent: Boolean,
        byteIOTrackingData: ByteIOTrackingData,
    ): ProductItemDataView {
        val productItem = ProductItemDataView()

        setExternalValues(
            productItem,
            position,
            isLocalSearchRecommendation,
            pageTitle,
            dimension90,
            productListType,
            externalReference,
            keywordIntention,
            byteIOTrackingData,
        )

        productItem.productID = productModel.id
        productItem.warehouseID = productModel.meta.warehouseID
        productItem.productName = productModel.name
        productItem.imageUrl = productModel.mediaURL.image
        productItem.imageUrl300 = productModel.mediaURL.image300
        productItem.imageUrl700 = productModel.mediaURL.image700
        productItem.ratingString = productModel.rating
        productItem.discountPercentage = productModel.price.discountPercentage.toInt()
        productItem.originalPrice = productModel.price.original
        productItem.price = productModel.price.text
        productItem.priceInt = productModel.price.number
        productItem.priceRange = productModel.price.range
        productItem.shopID = productModel.shop.id
        productItem.shopName = productModel.shop.name
        productItem.shopCity = productModel.shop.city
        productItem.shopUrl = productModel.shop.url
        productItem.isWishlisted = productModel.wishlist
        productItem.badgesList = listOf(BadgeItemDataView.create(productModel.badge))
        productItem.categoryID = productModel.category.id
        productItem.categoryName = productModel.category.name
        productItem.categoryBreadcrumb = productModel.category.breadcrumb
        productItem.labelGroupList = productModel.labelGroupList.map(LabelGroupDataView::create)
        productItem.labelGroupVariantList = productModel.labelGroupVariantList.map(
            LabelGroupVariantDataView.Companion::create
        )
        productItem.freeOngkirDataView = FreeOngkirDataView.create(productModel.freeShipping)
        productItem.isOrganicAds = productModel.isOrganicAds()
        productItem.topadsImpressionUrl = productModel.ads.productViewURL
        productItem.topadsClickUrl = productModel.ads.productClickURL
        productItem.topadsWishlistUrl = productModel.ads.productWishlistURL
        productItem.topadsTag = productModel.ads.tag
        productItem.productUrl = productModel.url
        productItem.applink = productModel.applink
        productItem.customVideoURL = productModel.mediaURL.videoCustom
        productItem.parentId = productModel.meta.parentID
        productItem.isPortrait = productModel.meta.isPortrait
        productItem.isImageBlurred = productModel.meta.isImageBlurred && !isEnableAdultContent
        productItem.recommendationAdsLog = RecommendationAdsLog(productModel.ads.creativeID, productModel.ads.logExtra)
        return productItem
    }

    private fun setExternalValues(
        productItem: ProductItemDataView,
        position: Int,
        isLocalSearchRecommendation: Boolean,
        pageTitle: String,
        dimension90: String,
        productListType: String,
        externalReference: String,
        keywordIntention: Int,
        byteIOTrackingData: ByteIOTrackingData,
    ) {
        productItem.position = position
        productItem.pageTitle = if (isLocalSearchRecommendation) pageTitle else ""
        productItem.dimension90 = dimension90
        productItem.productListType = productListType
        productItem.dimension131 = externalReference
        productItem.keywordIntention = keywordIntention
        productItem.byteIOTrackingData = byteIOTrackingData
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
        byteIOTrackingData: ByteIOTrackingData,
    ): ProductItemDataView {
        val productItem = ProductItemDataView()

        setExternalValues(
            productItem,
            position,
            isLocalSearchRecommendation,
            pageTitle,
            dimension90,
            productListType,
            externalReference,
            keywordIntention,
            byteIOTrackingData,
        )

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
        productItem.isWishlisted = productModel.isWishlist
        productItem.badgesList = productModel.badgeList.mapToBadgeItemList()
        productItem.categoryID = productModel.categoryId
        productItem.categoryName = productModel.categoryName
        productItem.categoryBreadcrumb = productModel.categoryBreadcrumb
        productItem.labelGroupList = productModel.labelGroupList.mapToLabelGroupDataViewList()
        productItem.labelGroupVariantList =
            productModel.labelGroupVariantList.mapToLabelGroupVariantList()
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
        productItem.applink = productModel.applink
        productItem.customVideoURL = productModel.customVideoURL
        productItem.showButtonAtc = showButtonAtc
        productItem.parentId = productModel.parentId
        productItem.isPortrait = productModel.isPortrait
        productItem.recommendationAdsLog = RecommendationAdsLog(productModel.ads.creativeID, productModel.ads.logExtra)
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
                labelGroupModel.url,
                labelGroupModel.styleList.map(StyleDataView::create)
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
        searchProductModel: SearchProductModel,
        keyword: String,
        dimension90: String,
    ): TickerDataView {
        val ticker = searchProductModel.ticker(isUseAceSearchProductV5)

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
        val suggestion = searchProductModel.suggestion(isUseAceSearchProductV5)
        val responseCode = searchProductModel.responseCode(isUseAceSearchProductV5)

        val trackingValue =
            if (responseCode == "3")
                searchProductModel.relatedKeyword(isUseAceSearchProductV5)
            else
                suggestion.suggestion

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

    private fun convertToInspirationCarouselDataView(
        searchInspirationCarousel: SearchInspirationCarousel,
        dimension90: String,
        externalReference: String,
        keyword: String,
        byteIOTrackingData: ByteIOTrackingData,
        productCount: Int,
    ): List<InspirationCarouselDataView> =
        searchInspirationCarousel
            .data
            .map { data ->
                inspirationCarouselDataView(
                    data,
                    dimension90,
                    externalReference,
                    keyword,
                    byteIOTrackingData,
                    productCount,
                )
            }

    private fun inspirationCarouselDataView(
        data: InspirationCarouselData,
        dimension90: String,
        externalReference: String,
        keyword: String,
        byteIOTrackingData: ByteIOTrackingData,
        productCount: Int,
    ) = InspirationCarouselDataView(
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
            byteIOTrackingData,
            productCount,
        ),
    )

    private fun convertToInspirationCarouselOptionViewModel(
        data: InspirationCarouselData,
        dimension90: String,
        externalReference: String,
        keyword: String,
        byteIOTrackingData: ByteIOTrackingData,
        productCount: Int,
    ): List<InspirationCarouselDataView.Option> {
        val mapper = InspirationCarouselProductDataViewMapper()
        val isCarouselInFirstPage = isWidgetInFirstPage(
            byteIOTrackingData.isFirstPage,
            data.position,
            productCount,
        )
        val carouselByteIOTrackingData = byteIOTrackingData.copy(
            isFirstPage = isCarouselInFirstPage
        )

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
                    data.trackingOption.toIntOrZero(),
                    carouselByteIOTrackingData,
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
                carouselByteIOTrackingData,
            )
        }
    }

    private fun isWidgetInFirstPage(
        isFirstPage: Boolean,
        position: Int,
        productCount: Int,
    ): Boolean = isFirstPage && position <= productCount

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

    private fun convertToViolationView(searchProductModel: SearchProductModel): ViolationDataView? {
        val violation = searchProductModel.violation(isUseAceSearchProductV5)
        return ViolationDataView.create(violation)
    }
}
