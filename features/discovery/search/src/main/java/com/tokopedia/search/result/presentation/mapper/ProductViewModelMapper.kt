package com.tokopedia.search.result.presentation.mapper

import com.tokopedia.search.result.domain.model.SearchProductModel
import com.tokopedia.search.result.domain.model.SearchProductModel.*
import com.tokopedia.search.result.presentation.model.*
import java.util.*

class ProductViewModelMapper {

    fun convertToProductViewModel(
            lastProductItemPosition: Int,
            searchProductModel: SearchProductModel,
            pageTitle: String,
            isLocalSearch: Boolean
    ): ProductDataView {
        val (searchProductHeader, searchProductData) = searchProductModel.searchProduct

        val productDataView = ProductDataView()

        productDataView.adsModel = searchProductModel.topAdsModel
        productDataView.globalNavDataView = convertToViewModel(searchProductModel.globalSearchNavigation)
        productDataView.cpmModel = searchProductModel.cpmModel
        productDataView.relatedDataView = convertToRelatedViewModel(searchProductData.related, isLocalSearch)
        productDataView.productList = convertToProductItemDataViewList(
                lastProductItemPosition, searchProductData.productList, pageTitle
        )
        productDataView.adsModel = searchProductModel.topAdsModel
        productDataView.tickerModel = convertToTickerDataView(searchProductData)
        productDataView.suggestionModel = convertToSuggestionDataView(searchProductData)
        productDataView.totalData = searchProductHeader.totalData
        productDataView.totalDataText = searchProductHeader.totalDataText
        productDataView.responseCode = searchProductHeader.responseCode
        productDataView.keywordProcess = searchProductHeader.keywordProcess
        productDataView.errorMessage = searchProductHeader.errorMessage
        productDataView.isQuerySafe = searchProductData.isQuerySafe
        productDataView.inspirationCarouselDataView = convertToInspirationCarouselViewModel(
                searchProductModel.searchInspirationCarousel
        )
        productDataView.inspirationCardDataView = convertToInspirationCardViewModel(searchProductModel.searchInspirationWidget)
        productDataView.additionalParams = searchProductHeader.additionalParams
        productDataView.autocompleteApplink = searchProductData.autocompleteApplink
        productDataView.defaultView = searchProductHeader.defaultView
        productDataView.bannerDataView = convertToBannerDataView(searchProductData.banner)

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

    private fun convertToRelatedViewModel(related: Related, isLocalSearch: Boolean): RelatedDataView {
        val broadMatchDataViewList: MutableList<BroadMatchDataView> = ArrayList()
        for (otherRelated in related.otherRelatedList) {
            broadMatchDataViewList.add(convertToBroadMatchViewModel(otherRelated, isLocalSearch))
        }
        return RelatedDataView(
                related.relatedKeyword,
                related.position,
                broadMatchDataViewList
        )
    }

    private fun convertToBroadMatchViewModel(otherRelated: OtherRelated, isLocalSearch: Boolean): BroadMatchDataView {
        val broadMatchItemDataViewList = otherRelated.productList.mapIndexed { index, otherRelatedProduct ->
            val position = index + 1
            convertToBroadMatchItemViewModel(otherRelatedProduct, position, otherRelated.keyword)
        }

        return BroadMatchDataView(
                otherRelated.keyword,
                otherRelated.url,
                otherRelated.applink,
                isLocalSearch,
                broadMatchItemDataViewList
        )
    }

    private fun convertToBroadMatchItemViewModel(
            otherRelatedProduct: OtherRelatedProduct,
            position: Int,
            alternativeKeyword: String
    ): BroadMatchItemDataView {
        val isOrganicAds = otherRelatedProduct.isOrganicAds()

        return BroadMatchItemDataView(
                otherRelatedProduct.id,
                otherRelatedProduct.name,
                otherRelatedProduct.price,
                otherRelatedProduct.imageUrl,
                otherRelatedProduct.url,
                otherRelatedProduct.applink,
                otherRelatedProduct.priceString,
                otherRelatedProduct.shop.city,
                otherRelatedProduct.badgeList.mapToBadgeItemDataViewList(),
                otherRelatedProduct.freeOngkir.mapToFreeOngkirDataView(),
                otherRelatedProduct.isWishlisted,
                position,
                alternativeKeyword,
                isOrganicAds,
                otherRelatedProduct.ads.productViewUrl,
                otherRelatedProduct.ads.productClickUrl,
                otherRelatedProduct.ads.productWishlistUrl,
                otherRelatedProduct.ratingAverage,
                otherRelatedProduct.labelGroupList.mapToLabelGroupDataViewList(),
                BroadMatchProduct(isOrganicAds),
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
            pageTitle: String
    ): List<ProductItemDataView> {
        return productModels.mapIndexed { index, productModel ->
            val position = lastProductItemPosition + index + 1
            convertToProductItem(productModel, position, pageTitle)
        }
    }

    private fun convertToProductItem(
            productModel: Product,
            position: Int,
            pageTitle: String
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
        productItem.minOrder = productModel.minOrder
        productItem.productUrl = productModel.url
        productItem.pageTitle = pageTitle
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

    private fun convertToLabelGroupList(labelGroupModelList: List<ProductLabelGroup>): List<LabelGroupDataView> {
        val labelGroupDataViewList: MutableList<LabelGroupDataView> = ArrayList()
        for (labelGroupModel in labelGroupModelList) {
            labelGroupDataViewList.add(convertToLabelGroupViewModel(labelGroupModel))
        }
        return labelGroupDataViewList
    }

    private fun convertToLabelGroupViewModel(labelGroupModel: ProductLabelGroup): LabelGroupDataView {
        return LabelGroupDataView(
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

    private fun convertToTickerDataView(searchProductData: SearchProductData): TickerDataView {
        val (text, query, typeId) = searchProductData.ticker
        return TickerDataView(
                text,
                query,
                typeId
        )
    }

    private fun convertToSuggestionDataView(searchProduct: SearchProductData): SuggestionDataView {
        val (suggestion, query, text) = searchProduct.suggestion
        return SuggestionDataView(
                text,
                query,
                suggestion
        )
    }

    private fun convertToInspirationCarouselViewModel(
            searchInspirationCarousel: SearchInspirationCarousel
    ): List<InspirationCarouselDataView> {
        return searchInspirationCarousel.data.map { data ->
            InspirationCarouselDataView(
                    data.title,
                    data.type,
                    data.position,
                    data.layout,
                    convertToInspirationCarouselOptionViewModel(data)
            )
        }
    }

    private fun convertToInspirationCarouselOptionViewModel(
            data: InspirationCarouselData
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
                            opt.title
                    ),
                    data.type,
                    data.layout,
                    data.position,
                    data.title,
                    position,
                    isChipsActive
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
}