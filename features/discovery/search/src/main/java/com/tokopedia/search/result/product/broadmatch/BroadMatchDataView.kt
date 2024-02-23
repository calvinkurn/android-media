package com.tokopedia.search.result.product.broadmatch

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.analytics.byteio.search.AppLogSearch
import com.tokopedia.discovery.common.analytics.SearchComponentTracking
import com.tokopedia.discovery.common.analytics.searchComponentTracking
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.search.result.domain.model.SearchProductModel.OtherRelated
import com.tokopedia.search.result.domain.model.SearchProductV5
import com.tokopedia.search.result.presentation.view.typefactory.ProductListTypeFactory
import com.tokopedia.search.result.product.byteio.ByteIOTrackingData
import com.tokopedia.search.result.product.deduplication.Deduplication
import com.tokopedia.search.result.product.inspirationcarousel.InspirationCarouselDataView
import com.tokopedia.search.result.product.inspirationcarousel.InspirationCarouselDataView.Option
import com.tokopedia.search.result.product.separator.VerticalSeparable
import com.tokopedia.search.result.product.separator.VerticalSeparator
import com.tokopedia.search.result.product.wishlist.Wishlistable

data class BroadMatchDataView(
    val keyword: String = "",
    val subtitle: String = "",
    val iconSubtitle: String = "",
    val url: String = "",
    val applink: String = "",
    val isAppendTitleInTokopedia: Boolean = false,
    val broadMatchItemDataViewList: List<BroadMatchItemDataView> = listOf(),
    val dimension90: String = "",
    val carouselOptionType: CarouselOptionType = BroadMatch,
    val componentId: String = "",
    val trackingOption: Int = 0,
    val actualKeyword: String = "",
    val cardButton: BroadMatchCardButton = BroadMatchCardButton(),
    override val verticalSeparator: VerticalSeparator = VerticalSeparator.None,
    val byteIOTrackingData: ByteIOTrackingData,
) : ImpressHolder(),
    Visitable<ProductListTypeFactory>,
    VerticalSeparable,
    Wishlistable,
    SearchComponentTracking by searchComponentTracking(
        trackingOption = trackingOption,
        keyword = actualKeyword,
        valueName = keyword,
        componentId = componentId,
        applink = applink,
        dimension90 = dimension90,
    ) {

    override fun type(typeFactory: ProductListTypeFactory): Int {
        return typeFactory.type(this)
    }

    override fun addTopSeparator(): VerticalSeparable =
        this.copy(verticalSeparator = VerticalSeparator.Top)

    override fun addBottomSeparator(): VerticalSeparable =
        this.copy(verticalSeparator = VerticalSeparator.Bottom)

    override val isWishlisted: Boolean
        get() = broadMatchItemDataViewList.any { it.isWishlisted }

    override fun setWishlist(productID: String, isWishlisted: Boolean) {
        broadMatchItemDataViewList.forEach {
            it.setWishlist(productID, isWishlisted)
        }
    }

    fun asByteIOSearchResult(adapterPosition: Int) =
        AppLogSearch.SearchResult(
            imprId = byteIOTrackingData.imprId,
            searchId = byteIOTrackingData.searchId,
            searchEntrance = byteIOTrackingData.searchEntrance,
            enterFrom = byteIOTrackingData.enterFrom,
            searchResultId = adapterPosition.toString(),
            listItemId = null,
            itemRank = null,
            listResultType = null,
            productID = null,
            searchKeyword = byteIOTrackingData.keyword,
            tokenType = AppLogSearch.ParamValue.GOODS_COLLECT,
            rank = adapterPosition,
            isAd = false,
            isFirstPage = byteIOTrackingData.isFirstPage,
            shopId = null,
            aladdinButtonType = null,
        )

    companion object {

        fun create(
            otherRelated: OtherRelated,
            isLocalSearch: Boolean,
            dimension90: String,
            trackingOption: Int,
            actualKeyword: String,
            externalReference: String,
            byteIOTrackingData: ByteIOTrackingData,
        ): BroadMatchDataView = BroadMatchDataView(
            keyword = otherRelated.keyword,
            url = otherRelated.url,
            applink = otherRelated.applink,
            isAppendTitleInTokopedia = isLocalSearch,
            broadMatchItemDataViewList = otherRelated.productList.mapIndexed { index, product ->
                BroadMatchItemDataView.create(
                    product,
                    index + 1,
                    otherRelated.keyword,
                    dimension90,
                    externalReference,
                    byteIOTrackingData,
                )
            },
            dimension90 = dimension90,
            carouselOptionType = BroadMatch,
            componentId = otherRelated.componentId,
            trackingOption = trackingOption,
            actualKeyword = actualKeyword,
            byteIOTrackingData = byteIOTrackingData,
        )

        fun createList(
            carousel: InspirationCarouselDataView,
            externalReference: String,
            addTopSeparator: Boolean,
            deduplication: Deduplication,
            byteIOTrackingData: ByteIOTrackingData,
        ): List<BroadMatchDataView> {
            val options = carousel.options
            return options.mapIndexedNotNull { index, option ->
                create(
                    option,
                    carousel.type,
                    externalReference,
                    determineCarouselSeparator(index, addTopSeparator, options.lastIndex),
                    deduplication,
                    byteIOTrackingData,
                )
            }
        }

        private fun create(
            option: Option,
            type: String,
            externalReference: String,
            verticalSeparator: VerticalSeparator,
            deduplication: Deduplication,
            byteIOTrackingData: ByteIOTrackingData,
        ): BroadMatchDataView? {
            val productList = deduplication.removeDuplicate(option.product)

            if (!deduplication.isCarouselWithinThreshold(option, productList)) return null

            return BroadMatchDataView(
                keyword = option.title,
                subtitle = option.subtitle,
                iconSubtitle = option.iconSubtitle,
                applink = option.applink,
                carouselOptionType = CarouselOptionType.of(type, option),
                broadMatchItemDataViewList = productList.mapIndexed { index, product ->
                    BroadMatchItemDataView.create(
                        product,
                        type,
                        option,
                        index,
                        externalReference,
                        byteIOTrackingData,
                    )
                },
                trackingOption = option.trackingOption,
                cardButton = BroadMatchCardButton.create(option.cardButton),
                verticalSeparator = verticalSeparator,
                byteIOTrackingData = byteIOTrackingData,
            )
        }

        private fun determineCarouselSeparator(
            index: Int,
            addTopSeparator: Boolean,
            lastIndex: Int,
        ): VerticalSeparator {
            val hasTop = index == 0 && addTopSeparator
            val hasBottom = index == lastIndex

            return if (hasTop && hasBottom) VerticalSeparator.Both
            else if (hasTop) VerticalSeparator.Top
            else if (hasBottom) VerticalSeparator.Bottom
            else VerticalSeparator.None
        }

        fun create(
            otherRelated: SearchProductV5.Data.Related.OtherRelated,
            isLocalSearch: Boolean,
            dimension90: String,
            trackingOption: Int,
            actualKeyword: String,
            externalReference: String,
            byteIOTrackingData: ByteIOTrackingData,
        ): BroadMatchDataView = BroadMatchDataView(
            keyword = otherRelated.keyword,
            url = otherRelated.url,
            applink = otherRelated.applink,
            isAppendTitleInTokopedia = isLocalSearch,
            broadMatchItemDataViewList = otherRelated.productList.mapIndexed { index, product ->
                BroadMatchItemDataView.create(
                    product,
                    index + 1,
                    otherRelated.keyword,
                    dimension90,
                    externalReference,
                    byteIOTrackingData,
                )
            },
            dimension90 = dimension90,
            carouselOptionType = BroadMatch,
            componentId = otherRelated.componentID,
            trackingOption = trackingOption,
            actualKeyword = actualKeyword,
            byteIOTrackingData = byteIOTrackingData,
        )
    }
}
