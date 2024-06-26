package com.tokopedia.search.result.product.cpm

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.analytics.byteio.EntranceForm.SEARCH_SHOP_CARD_BIG
import com.tokopedia.analytics.byteio.EntranceForm.SEARCH_SHOP_CARD_SMALL
import com.tokopedia.analytics.byteio.SourcePageType
import com.tokopedia.analytics.byteio.search.AppLogSearch
import com.tokopedia.analytics.byteio.search.AppLogSearch.ParamValue.SHOP_BIG
import com.tokopedia.analytics.byteio.search.AppLogSearch.ParamValue.SHOP_SMALL
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.search.result.presentation.view.typefactory.ProductListTypeFactory
import com.tokopedia.search.result.product.addtocart.AddToCartConstant
import com.tokopedia.search.result.product.byteio.ByteIORanking
import com.tokopedia.search.result.product.byteio.ByteIORankingImpl
import com.tokopedia.search.result.product.byteio.ByteIOTrackingData
import com.tokopedia.search.result.product.separator.VerticalSeparable
import com.tokopedia.search.result.product.separator.VerticalSeparator
import com.tokopedia.topads.sdk.common.constants.TopAdsConstants
import com.tokopedia.topads.sdk.domain.model.CpmModel
import com.tokopedia.topads.sdk.domain.model.Product
import com.tokopedia.topads.sdk.domain.model.Product as CPMProduct

data class CpmDataView(
    val cpmModel: CpmModel = CpmModel(),
    override val verticalSeparator: VerticalSeparator = VerticalSeparator.None,
    var byteIOTrackingData: ByteIOTrackingData,
) : Visitable<ProductListTypeFactory>,
    VerticalSeparable,
    ByteIORanking by ByteIORankingImpl() {

    val byteIOImpressHolder = ImpressHolder()

    private val byteIOTrackingLayout = listOf(TopAdsConstants.LAYOUT_1, TopAdsConstants.LAYOUT_2)

    private fun byteIOTokenType() =
        if (isShopSmall()) SHOP_SMALL else SHOP_BIG

    fun isShopBig() = layout() == TopAdsConstants.LAYOUT_1

    private fun isShopSmall() = layout() == TopAdsConstants.LAYOUT_2

    private fun layout() = cpmModel.data.getOrNull(0)?.cpm?.layout

    fun isTrackByteIO() = byteIOTrackingLayout.contains(layout())

    fun asByteIOSearchResult(aladdinButtonType: String?, isIncludeResultType : Boolean = true): AppLogSearch.SearchResult {
        val shopId = cpmModel.data.getOrNull(0)?.cpm?.cpmShop?.id ?: ""
        return AppLogSearch.SearchResult(
            imprId = byteIOTrackingData.imprId,
            searchId = byteIOTrackingData.searchId,
            searchEntrance = byteIOTrackingData.searchEntrance,
            searchResultId = shopId,
            listItemId = null,
            itemRank = null,
            listResultType = if (isIncludeResultType) AppLogSearch.ParamValue.SHOP else null,
            productID = "",
            searchKeyword = byteIOTrackingData.keyword,
            rank = getRank(),
            isAd = true,
            tokenType = byteIOTokenType(),
            isFirstPage = byteIOTrackingData.isFirstPage,
            shopId = shopId,
            aladdinButtonType = aladdinButtonType,
        )
    }

    fun shopItemAsByteIOSearchResult(
        itemRank: Int,
        aladdinButtonType: String?
    ): AppLogSearch.SearchResult {
        val cpmShop = cpmModel.data.getOrNull(0)?.cpm?.cpmShop
        val shopId = cpmShop?.id ?: ""
        return AppLogSearch.SearchResult(
            imprId = byteIOTrackingData.imprId,
            searchId = byteIOTrackingData.searchId,
            searchEntrance = byteIOTrackingData.searchEntrance,
            searchResultId = shopId,
            listItemId = shopId,
            itemRank = itemRank,
            listResultType = AppLogSearch.ParamValue.SHOP,
            productID = "",
            searchKeyword = byteIOTrackingData.keyword,
            rank = getRank(),
            isAd = true,
            tokenType = byteIOTokenType(),
            isFirstPage = byteIOTrackingData.isFirstPage,
            shopId = shopId,
            aladdinButtonType = aladdinButtonType,
        )
    }

    fun productItemAsByteIOSearchResult(
        cpmProduct: CPMProduct,
        productPosition: Int,
        aladdinButtonType: String?,
    ): AppLogSearch.SearchResult {
        val cpmShop = cpmModel.data.getOrNull(0)?.cpm?.cpmShop
        val shopId = cpmShop?.id ?: ""
        return AppLogSearch.SearchResult(
            imprId = byteIOTrackingData.imprId,
            searchId = byteIOTrackingData.searchId,
            searchEntrance = byteIOTrackingData.searchEntrance,
            searchResultId = shopId,
            listItemId = byteIOProductId(cpmProduct),
            itemRank = productPosition,
            listResultType = AppLogSearch.ParamValue.GOODS,
            productID = byteIOProductId(cpmProduct),
            searchKeyword = byteIOTrackingData.keyword,
            rank = getRank(),
            isAd = true,
            tokenType = byteIOTokenType(),
            isFirstPage = byteIOTrackingData.isFirstPage,
            shopId = shopId,
            aladdinButtonType = aladdinButtonType,
        )
    }

    fun productItemAsByteIOProduct(
        cpmProduct: CPMProduct,
        productPosition: Int,
    ): AppLogSearch.Product {
        val cpmShop = cpmModel.data.getOrNull(0)?.cpm?.cpmShop
        val shopId = cpmShop?.id ?: ""
        return AppLogSearch.Product(
            entranceForm = if (isShopSmall()) SEARCH_SHOP_CARD_SMALL else SEARCH_SHOP_CARD_BIG,
            isAd = true,
            productID = byteIOProductId(cpmProduct),
            searchID = byteIOTrackingData.searchId,
            requestID = byteIOTrackingData.imprId,
            searchResultID = shopId,
            listItemId = byteIOProductId(cpmProduct),
            itemRank = productPosition,
            listResultType = AppLogSearch.ParamValue.GOODS,
            searchKeyword = byteIOTrackingData.keyword,
            tokenType = byteIOTokenType(),
            rank = getRank(),
            shopID = shopId,
            searchEntrance = byteIOTrackingData.searchEntrance,
            sourcePageType = SourcePageType.PRODUCT_CARD,
        )
    }

    private fun byteIOProductId(product: Product): String =
        if (hasParent(product)) product.parentId
        else product.id

    private fun hasParent(product: Product) = product.parentId != "" && product.parentId != AddToCartConstant.DEFAULT_PARENT_ID

    override fun type(typeFactory: ProductListTypeFactory?): Int {
        return typeFactory?.type(this) ?: 0
    }

    override fun addTopSeparator(): VerticalSeparable = this

    override fun addBottomSeparator(): VerticalSeparable = this
}
