package com.tokopedia.search.result.product.cpm

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.analytics.byteio.search.AppLogSearch
import com.tokopedia.analytics.byteio.search.AppLogSearch.ParamValue.SHOP_BIG
import com.tokopedia.analytics.byteio.search.AppLogSearch.ParamValue.SHOP_SMALL
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.search.result.presentation.view.typefactory.ProductListTypeFactory
import com.tokopedia.search.result.product.ByteIOTrackingData
import com.tokopedia.search.result.product.separator.VerticalSeparable
import com.tokopedia.search.result.product.separator.VerticalSeparator
import com.tokopedia.topads.sdk.domain.model.CpmModel
import com.tokopedia.topads.sdk.domain.model.Product as CPMProduct

data class CpmDataView(
    val cpmModel: CpmModel = CpmModel(),
    override val verticalSeparator: VerticalSeparator = VerticalSeparator.None,
    val byteIOTrackingData: ByteIOTrackingData,
) : Visitable<ProductListTypeFactory>,
    VerticalSeparable {

    val byteIOImpressHolder = ImpressHolder()

    private fun byteIOSearchResultTokenType() =
        if (cpmModel.data.getOrNull(0)?.cpm?.layout == 2) SHOP_SMALL else SHOP_BIG

    fun asByteIOSearchResult(
        adapterPosition: Int,
        aladdinButtonType: String?,
    ): AppLogSearch.SearchResult {
        val shopId = cpmModel.data.getOrNull(0)?.cpm?.cpmShop?.id ?: ""
        return AppLogSearch.SearchResult(
            imprId = byteIOTrackingData.imprId,
            searchId = byteIOTrackingData.searchId,
            searchEntrance = byteIOTrackingData.searchEntrance,
            enterFrom = byteIOTrackingData.enterFrom,
            searchResultId = shopId,
            listItemId = null,
            itemRank = null,
            listResultType = AppLogSearch.ParamValue.SHOP,
            productID = null,
            searchKeyword = byteIOTrackingData.keyword,
            rank = adapterPosition,
            isAd = true,
            tokenType = byteIOSearchResultTokenType(),
            isFirstPage = byteIOTrackingData.isFirstPage,
            shopId = shopId,
            aladdinButtonType = aladdinButtonType,
        )
    }

    fun asByteIOProductSearchResult(
        cpmProduct: CPMProduct,
        adapterPosition: Int,
        productPosition: Int,
        aladdinButtonType: String?,
    ): AppLogSearch.SearchResult {
        val cpmShop = cpmModel.data.getOrNull(0)?.cpm?.cpmShop
        val shopId = cpmShop?.id ?: ""
        return AppLogSearch.SearchResult(
            imprId = byteIOTrackingData.imprId,
            searchId = byteIOTrackingData.searchId,
            searchEntrance = byteIOTrackingData.searchEntrance,
            enterFrom = byteIOTrackingData.enterFrom,
            searchResultId = shopId,
            listItemId = cpmProduct.id,
            itemRank = productPosition,
            listResultType = AppLogSearch.ParamValue.GOODS,
            productID = cpmProduct.id,
            searchKeyword = byteIOTrackingData.keyword,
            rank = adapterPosition,
            isAd = true,
            tokenType = byteIOSearchResultTokenType(),
            isFirstPage = byteIOTrackingData.isFirstPage,
            shopId = shopId,
            aladdinButtonType = aladdinButtonType,
        )
    }

    override fun type(typeFactory: ProductListTypeFactory?): Int {
        return typeFactory?.type(this) ?: 0
    }

    override fun addTopSeparator(): VerticalSeparable = this

    override fun addBottomSeparator(): VerticalSeparable = this
}
