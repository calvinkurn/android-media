package com.tokopedia.videoTabComponent.analytics

import com.tokopedia.play.widget.analytic.PlayWidgetAnalyticListener
import com.tokopedia.play.widget.ui.PlayWidgetJumboView
import com.tokopedia.play.widget.ui.PlayWidgetLargeView
import com.tokopedia.play.widget.ui.PlayWidgetMediumView
import com.tokopedia.play.widget.ui.model.PlayWidgetChannelUiModel
import com.tokopedia.play.widget.ui.model.PlayWidgetConfigUiModel
import com.tokopedia.play.widget.ui.type.PlayWidgetChannelType
import com.tokopedia.videoTabComponent.analytics.tracker.PlayAnalyticsTracker
import javax.inject.Inject

//slot click and impression

class PlayWidgetAnalyticsListenerImp @Inject constructor(
    private val tracker: PlayAnalyticsTracker,
) : PlayWidgetAnalyticListener {

    var filterCategory: String = ""

    private var mOnClickChannelCardListener: ((channelId: String, position: Int) -> Unit)? = null

    fun setOnClickChannelCard(callback: (channelId: String, position: Int) -> Unit) {
        mOnClickChannelCardListener = callback
    }

    override fun onClickChannelCard(
        view: PlayWidgetJumboView,
        item: PlayWidgetChannelUiModel,
        config: PlayWidgetConfigUiModel,
        channelPositionInList: Int,
    ) {
        tracker.clickOnContentHighlightCardsInVideoTab(
            item.channelId, item.partner.id,
            listOf(item.video.coverUrl),
            item.channelType.toString().lowercase(), channelPositionInList
        )
        mOnClickChannelCardListener?.invoke(item.channelId, channelPositionInList)
    }

    override fun onImpressChannelCard(
        view: PlayWidgetJumboView,
        item: PlayWidgetChannelUiModel,
        config: PlayWidgetConfigUiModel,
        channelPositionInList: Int,
    ) {
        tracker.impressOnContentHighlightWidgetInVideoTab(
            item.channelId, item.partner.id, item.channelType.toString().lowercase()
        )
        tracker.impressOnContentHighlightCard(
            item.channelId, item.partner.id,
            listOf(item.video.coverUrl), item.channelType.toString().lowercase(),
            channelPositionInList
        )
    }

    override fun onClickChannelCard(
        view: PlayWidgetLargeView,
        item: PlayWidgetChannelUiModel,
        config: PlayWidgetConfigUiModel,
        channelPositionInList: Int,
    ) {
        tracker.clickOnContentCardsInVideoTabBelowTheChips(
            item.channelId, item.partner.id, listOf(item.video.coverUrl),
            item.channelType.toString().lowercase(), filterCategory, channelPositionInList
        )
        mOnClickChannelCardListener?.invoke(item.channelId, channelPositionInList)
    }

    override fun onImpressChannelCard(
        view: PlayWidgetLargeView,
        item: PlayWidgetChannelUiModel,
        config: PlayWidgetConfigUiModel,
        channelPositionInList: Int,
    ) {
        tracker.impressOnContentCardsInVideoTabBelowTheChips(
            item.channelId, item.partner.id, listOf(item.video.coverUrl),
            item.channelType.toString().lowercase(), filterCategory, channelPositionInList
        )
    }

    override fun onClickChannelCard(
        view: PlayWidgetMediumView,
        item: PlayWidgetChannelUiModel,
        config: PlayWidgetConfigUiModel,
        channelPositionInList: Int,
    ) {
        if (item.channelType == PlayWidgetChannelType.Live) {
        tracker.clickOnLagiLiveCarouselContentCards(
            item.channelId, item.partner.id, listOf(item.video.coverUrl),
            item.channelType.toString().lowercase(), channelPositionInList
        )}
        if (item.channelType == PlayWidgetChannelType.Upcoming) {
            tracker.clickOnUpcomingCarouselContentCards(
                item.channelId, item.partner.id, listOf(item.video.coverUrl),
                item.channelType.toString().lowercase(), filterCategory, channelPositionInList
            )
        }
        mOnClickChannelCardListener?.invoke(item.channelId, channelPositionInList)
    }

    override fun onImpressChannelCard(
        view: PlayWidgetMediumView,
        item: PlayWidgetChannelUiModel,
        config: PlayWidgetConfigUiModel,
        channelPositionInList: Int,
    ) {
        if (item.channelType == PlayWidgetChannelType.Upcoming) {
            impressMediumUpcomingWidget(channelPositionInList)
            tracker.impressOnUpcomingCarouselContentCards(
                item.channelId, item.partner.id, listOf(item.video.coverUrl),
                item.channelType.toString().lowercase(), filterCategory, channelPositionInList
            )
        } else if (item.channelType == PlayWidgetChannelType.Live) {
            impressMediumLiveWidget(channelPositionInList)
            tracker.impressOnLagiLiveCarouselContentCards(
                item.channelId,
                item.partner.id,
                listOf(item.video.coverUrl),
                item.channelType.toString().lowercase(), channelPositionInList
            )
        }
    }

    /**
     * only send impression when the very first card is upcoming
     */
    private fun impressMediumUpcomingWidget(channelPositionInList: Int) {
        if (channelPositionInList == FIRST_CHANNEL_POSITION_IN_LIST) {
            tracker.impressOnUpcomingContentCarouselWidget(filterCategory)
        }
    }

    /**
     * only send impression when the very first card is live
     */
    private fun impressMediumLiveWidget(channelPositionInList: Int) {
        if (channelPositionInList == FIRST_CHANNEL_POSITION_IN_LIST) {
            tracker.impressOnLagiLiveContentCarouselWidget()
        }
    }

    override fun onClickToggleReminderChannel(
        view: PlayWidgetJumboView,
        item: PlayWidgetChannelUiModel,
        channelPositionInList: Int,
        isRemindMe: Boolean
    ) {
        super.onClickToggleReminderChannel(view, item, channelPositionInList, isRemindMe)
        if (isRemindMe) {
            tracker.clickOnRemindMeButtonOnPlayCardInContentHighlight(
                item.channelId, item.partner.id, listOf(item.video.coverUrl),
                item.channelType.toString().lowercase(), filterCategory
            )
        } else {
            tracker.clickOnUnRemindMeButtonOnPlayCardInContentHighlight(
                item.channelId, item.partner.id, listOf(item.video.coverUrl),
                item.channelType.toString().lowercase(), filterCategory
            )
        }
    }

    override fun onClickToggleReminderChannel(
        view: PlayWidgetMediumView,
        item: PlayWidgetChannelUiModel,
        channelPositionInList: Int,
        isRemindMe: Boolean
    ) {
        super.onClickToggleReminderChannel(view, item, channelPositionInList, isRemindMe)

        if (item.channelType == PlayWidgetChannelType.Upcoming) {
            if (isRemindMe) {
                tracker.clickOnRemindMeButtonOnPlayCardInUpcomingCarousel(
                    item.channelId, item.partner.id, item.channelType.toString().lowercase(), filterCategory
                )
            } else {
                tracker.clickOnUnRemindMeButtonOnPlayCardInUpcomingCarousel(
                    item.channelId, item.partner.id, item.channelType.toString().lowercase(), filterCategory
                )
            }
        }
    }

    override fun onClickToggleReminderChannel(view: PlayWidgetLargeView, item: PlayWidgetChannelUiModel, channelPositionInList: Int, isRemindMe: Boolean) {
        super.onClickToggleReminderChannel(view, item, channelPositionInList, isRemindMe)
        if (isRemindMe) {
            tracker.clickOnRemindMeButtonOnPlayCardsWithinChip(
                    item.channelId, item.partner.id, item.channelType.toString().lowercase(), filterCategory
            )
        } else {
            tracker.clickOnUnRemindMeButtonOnPlayCardsWithinChip(
                    item.channelId, item.partner.id, item.channelType.toString().lowercase(), filterCategory
            )
        }
    }

    companion object {
        private const val FIRST_CHANNEL_POSITION_IN_LIST = 1
    }
}
