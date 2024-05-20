package com.tokopedia.home_component.viewholders.shorten.viewholder.listener

import com.tokopedia.home_component.visitable.shorten.ItemThumbnailWidgetUiModel

interface ThumbnailWidgetListener {
    // container
    fun thumbnailChannelHeaderClicked(appLink: String)
    // fun thumbnailContainerImpressed(data: ThumbnailWidgetUiModel)

    // items
    fun thumbnailImpressed(data: ItemThumbnailWidgetUiModel, position: Int)
    fun thumbnailClicked(data: ItemThumbnailWidgetUiModel, position: Int)
}
