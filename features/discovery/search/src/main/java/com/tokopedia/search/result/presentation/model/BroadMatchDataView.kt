package com.tokopedia.search.result.presentation.model

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.discovery.common.analytics.SearchComponentTracking
import com.tokopedia.discovery.common.analytics.searchComponentTracking
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.search.result.presentation.view.typefactory.ProductListTypeFactory
import com.tokopedia.search.result.product.separator.VerticalSeparable
import com.tokopedia.search.result.product.separator.VerticalSeparator

data class BroadMatchDataView(
    val keyword: String = "",
    val subtitle: String = "",
    val url: String = "",
    val applink: String = "",
    val isAppendTitleInTokopedia: Boolean = false,
    val broadMatchItemDataViewList: List<BroadMatchItemDataView> = listOf(),
    val dimension90: String = "",
    val carouselOptionType: CarouselOptionType,
    val componentId: String = "",
    val trackingOption: Int = 0,
    val actualKeyword: String = "",
    val cardButton: CardButton = CardButton(),
    override val verticalSeparator: VerticalSeparator = VerticalSeparator.None,
) : ImpressHolder(),
    Visitable<ProductListTypeFactory>,
    VerticalSeparable,
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

    data class CardButton(val title: String = "", val applink: String = "")
}