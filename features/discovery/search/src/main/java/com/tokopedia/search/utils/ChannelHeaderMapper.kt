package com.tokopedia.search.utils

import android.content.Context
import com.tokopedia.home_component_header.model.ChannelHeader
import com.tokopedia.search.R
import com.tokopedia.search.result.product.broadmatch.BroadMatchDataView
import com.tokopedia.search.result.product.inspirationcarousel.InspirationCarouselDataView
import com.tokopedia.search.result.product.inspirationcarousel.LAYOUT_INSPIRATION_CAROUSEL_CHIPS
import com.tokopedia.search.result.product.inspirationcarousel.LAYOUT_INSPIRATION_CAROUSEL_GRID

internal fun BroadMatchDataView.convertToChannelHeader(context: Context) =
    ChannelHeader(
        name = context.getTitle(this),
        subtitle = this.subtitle,
        applink = this.applink,
        url = this.url,
        iconSubtitleUrl = this.iconSubtitle,
        headerType = ChannelHeader.HeaderType.REVAMP,
        pageSource = ChannelHeader.PageSource.SRP
    )

internal fun InspirationCarouselDataView.convertToChannelHeader(): ChannelHeader {
    val options = this.options.getOrNull(0)
    return ChannelHeader(
        name = this.title,
        applink = if(isLayoutInspirationCarouselGrid(this)) options?.applink.orEmpty() else "" ,
        url = if(isLayoutInspirationCarouselGrid(this)) options?.url.orEmpty() else "",
        headerType = ChannelHeader.HeaderType.REVAMP,
        pageSource = ChannelHeader.PageSource.SRP
    )
}

private fun Context.getTitle(broadMatchDataView: BroadMatchDataView) =
    broadMatchDataView.keyword +
        if (broadMatchDataView.isAppendTitleInTokopedia)
            " " + getString(R.string.broad_match_in_tokopedia)
        else ""

private fun isLayoutInspirationCarouselGrid(element: InspirationCarouselDataView): Boolean {
    return element.layout == LAYOUT_INSPIRATION_CAROUSEL_GRID
}
