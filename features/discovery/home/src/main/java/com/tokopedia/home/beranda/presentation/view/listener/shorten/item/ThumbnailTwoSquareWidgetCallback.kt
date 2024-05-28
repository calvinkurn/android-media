package com.tokopedia.home.beranda.presentation.view.listener.shorten.item

import com.tokopedia.analytics.byteio.AppLogAnalytics
import com.tokopedia.analytics.byteio.AppLogParam
import com.tokopedia.analytics.byteio.home.AppLogHomeChannel
import com.tokopedia.home.analytics.v2.TwoSquareWidgetTracking
import com.tokopedia.home.beranda.listener.HomeCategoryListener
import com.tokopedia.home_component.analytics.TwoThumbnailSquareTrackingMapper.asCardModel
import com.tokopedia.home_component.analytics.TwoThumbnailSquareTrackingMapper.asProductModel
import com.tokopedia.home_component.viewholders.shorten.viewholder.listener.ThumbnailWidgetListener
import com.tokopedia.home_component.visitable.shorten.ItemThumbnailWidgetUiModel
import java.util.HashMap

class ThumbnailTwoSquareWidgetCallback(val listener: HomeCategoryListener) : ThumbnailWidgetListener {

    override fun thumbnailChannelHeaderClicked(appLink: String) {
        listener.onDynamicChannelClicked(appLink)
    }

    override fun thumbnailClicked(data: ItemThumbnailWidgetUiModel, position: Int) {
        listener.onDynamicChannelClicked(data.appLink)
        TwoSquareWidgetTracking.clickCardDeals(data, position, listener.userId)

        if (data.tracker.isProduct()) { // check if is product
            AppLogHomeChannel.sendProductClick(data.asProductModel())
        } else {
            AppLogHomeChannel.sendCardClick(data.asCardModel())
        }

        AppLogAnalytics.putPageData(
            key = AppLogParam.ENTER_METHOD,
            value = AppLogHomeChannel.getEnterMethod(
                data.tracker.layoutTrackerType,
                data.tracker.recomPageName,
                position
            )
        )
    }

    override fun thumbnailImpressed(data: ItemThumbnailWidgetUiModel, position: Int) {
        listener.getTrackingQueueObj()?.putEETracking(
            TwoSquareWidgetTracking.impressCardDeals(data, position, listener.userId) as HashMap<String, Any>
        )

        if (data.tracker.isProduct()) { // check if is product
            AppLogHomeChannel.sendProductShow(data.asProductModel())
        } else {
            AppLogHomeChannel.sendCardShow(data.asCardModel())
        }
    }
}
