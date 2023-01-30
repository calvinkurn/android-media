package com.tokopedia.feedplus.oldFeed.view.analytics.widget

import com.tokopedia.play.widget.analytic.PlayWidgetAnalyticListener
import com.tokopedia.play.widget.ui.PlayWidgetLargeView
import com.tokopedia.play.widget.ui.model.PlayWidgetChannelUiModel
import com.tokopedia.play.widget.ui.model.PlayWidgetConfigUiModel
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

    private var mOnClickChannelCardListener: ((channelId: String, position: Int) -> Unit)? = null

    fun setOnClickChannelCard(callback: (channelId: String, position: Int) -> Unit) {
        mOnClickChannelCardListener = callback
    }

    companion object {
        const val DEFAULT_FILTER_VALUE = ""
    }


    override fun onClickChannelCard(
        view: PlayWidgetLargeView,
        item: PlayWidgetChannelUiModel,
        config: PlayWidgetConfigUiModel,
        channelPositionInList: Int,
    ) {
        val filterCategory =
            if (filterCategory.isNotEmpty()) filterCategory else DEFAULT_FILTER_VALUE
        tracker.clickOnContentCardsInContentListPageForLagiLive(
            item.channelId, shopId, listOf(item.video.coverUrl),
            item.channelType.toString().toLowerCase(), filterCategory, channelPositionInList,
            entryPoint
        )

        mOnClickChannelCardListener?.invoke(item.channelId, channelPositionInList)
    }

    override fun onImpressChannelCard(
        view: PlayWidgetLargeView,
        item: PlayWidgetChannelUiModel,
        config: PlayWidgetConfigUiModel,
        channelPositionInList: Int,
    ) {
        val filterCategory =
            if (filterCategory.isNotEmpty()) filterCategory else DEFAULT_FILTER_VALUE

        tracker.impressOnContentCardsInContentListPageForLagiLive(
            item.channelId, shopId, listOf(item.video.coverUrl),
            item.channelType.toString().toLowerCase(), filterCategory, channelPositionInList,
            entryPoint
        )


    }
}
