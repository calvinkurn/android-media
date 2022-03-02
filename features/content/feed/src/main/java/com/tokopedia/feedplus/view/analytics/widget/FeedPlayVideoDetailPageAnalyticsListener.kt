package com.tokopedia.feedplus.view.analytics.widget

import com.tokopedia.play.widget.analytic.PlayWidgetAnalyticListener
import com.tokopedia.play.widget.ui.PlayWidgetLargeView
import com.tokopedia.play.widget.ui.model.PlayWidgetChannelUiModel
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.videoTabComponent.analytics.tracker.PlayAnalyticsTracker
import javax.inject.Inject


class FeedPlayVideoDetailPageAnalyticsListener @Inject constructor(
    private val tracker: PlayAnalyticsTracker,
    private val userSession: UserSessionInterface
) : PlayWidgetAnalyticListener {

    var filterCategory: String = ""
    var entryPoint: String = ""
    private val shopId: String = userSession.shopId

    companion object{
         const val DEFAULT_FILTER_VALUE = "Untukmu"
    }


    override fun onClickChannelCard(
        view: PlayWidgetLargeView,
        item: PlayWidgetChannelUiModel,
        channelPositionInList: Int,
        isAutoPlay: Boolean
    ) {
        super.onClickChannelCard(view, item, channelPositionInList, isAutoPlay)

        val filterCategory = if (filterCategory.isNotEmpty()) filterCategory else DEFAULT_FILTER_VALUE
            tracker.clickOnContentCardsInContentListPageForLagiLive(
                item.channelId, shopId, listOf(item.video.coverUrl),
                item.channelType.toString().toLowerCase(), filterCategory, channelPositionInList,
                entryPoint
            )


    }

    override fun onImpressChannelCard(
        view: PlayWidgetLargeView,
        item: PlayWidgetChannelUiModel,
        channelPositionInList: Int,
        isAutoPlay: Boolean
    ) {
        val filterCategory = if (filterCategory.isNotEmpty()) filterCategory else DEFAULT_FILTER_VALUE

        super.onImpressChannelCard(view, item, channelPositionInList, isAutoPlay)
            tracker.impressOnContentCardsInContentListPageForLagiLive(
                item.channelId, shopId, listOf(item.video.coverUrl),
                item.channelType.toString().toLowerCase(), filterCategory, channelPositionInList,
                entryPoint
            )


    }
}