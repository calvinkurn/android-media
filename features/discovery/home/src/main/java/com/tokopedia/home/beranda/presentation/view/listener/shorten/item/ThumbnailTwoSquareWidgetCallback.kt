package com.tokopedia.home.beranda.presentation.view.listener.shorten.item

import com.tokopedia.home.beranda.listener.HomeCategoryListener
import com.tokopedia.home_component.viewholders.shorten.viewholder.listener.ThumbnailWidgetListener
import com.tokopedia.home_component.visitable.shorten.ItemThumbnailWidgetUiModel

class ThumbnailTwoSquareWidgetCallback(val listener: HomeCategoryListener) : ThumbnailWidgetListener {

    override fun thumbnailClicked(data: ItemThumbnailWidgetUiModel, position: Int) {
        listener.onDynamicChannelClicked(data.appLink)
    }

    override fun thumbnailImpressed(data: ItemThumbnailWidgetUiModel, position: Int) {

    }
}
