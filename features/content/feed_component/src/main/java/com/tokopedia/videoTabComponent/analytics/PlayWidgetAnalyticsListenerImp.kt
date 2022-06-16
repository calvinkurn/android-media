package com.tokopedia.videoTabComponent.analytics

import com.tokopedia.play.widget.analytic.PlayWidgetAnalyticListener
import com.tokopedia.play.widget.ui.PlayWidgetJumboView
import com.tokopedia.play.widget.ui.PlayWidgetLargeView
import com.tokopedia.play.widget.ui.PlayWidgetMediumView
import com.tokopedia.play.widget.ui.model.PlayWidgetChannelUiModel
import com.tokopedia.play.widget.ui.type.PlayWidgetChannelType
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.videoTabComponent.analytics.tracker.PlayAnalyticsTracker
import javax.inject.Inject

//slot click and impression

class PlayWidgetAnalyticsListenerImp @Inject constructor(
    private val tracker: PlayAnalyticsTracker,
    private val userSession: UserSessionInterface
) : PlayWidgetAnalyticListener {

    var filterCategory: String = ""

    override fun onClickChannelCard(
        view: PlayWidgetJumboView,
        item: PlayWidgetChannelUiModel,
        channelPositionInList: Int,
        isAutoPlay: Boolean
    ) {
        super.onClickChannelCard(view, item, channelPositionInList, isAutoPlay)
        tracker.clickOnContentHighlightCardsInVideoTab(
            item.channelId, item.partner.id,
            listOf(item.video.coverUrl),
            item.channelType.toString().lowercase(), channelPositionInList
        )
    }

    override fun onImpressChannelCard(
        view: PlayWidgetJumboView,
        item: PlayWidgetChannelUiModel,
        channelPositionInList: Int,
        isAutoPlay: Boolean
    ) {
        super.onImpressChannelCard(view, item, channelPositionInList, isAutoPlay)
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
        channelPositionInList: Int,
        isAutoPlay: Boolean
    ) {
        super.onClickChannelCard(view, item, channelPositionInList, isAutoPlay)

        tracker.clickOnContentCardsInVideoTabBelowTheChips(
            item.channelId, item.partner.id, listOf(item.video.coverUrl),
            item.channelType.toString().lowercase(), filterCategory, channelPositionInList
        )
    }

    override fun onImpressChannelCard(
        view: PlayWidgetLargeView,
        item: PlayWidgetChannelUiModel,
        channelPositionInList: Int,
        isAutoPlay: Boolean
    ) {
        super.onImpressChannelCard(view, item, channelPositionInList, isAutoPlay)
        tracker.impressOnContentCardsInVideoTabBelowTheChips(
            item.channelId, item.partner.id, listOf(item.video.coverUrl),
            item.channelType.toString().lowercase(), filterCategory, channelPositionInList
        )
    }

    override fun onClickChannelCard(
        view: PlayWidgetMediumView,
        item: PlayWidgetChannelUiModel,
        channelPositionInList: Int,
        isAutoPlay: Boolean
    ) {
        super.onClickChannelCard(view, item, channelPositionInList, isAutoPlay)
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
    }

    override fun onImpressChannelCard(
        view: PlayWidgetMediumView,
        item: PlayWidgetChannelUiModel,
        channelPositionInList: Int,
        isAutoPlay: Boolean
    ) {
        super.onImpressChannelCard(view, item, channelPositionInList, isAutoPlay)
        if (item.channelType == PlayWidgetChannelType.Upcoming) {
            if (channelPositionInList == 0)
                tracker.impressOnUpcomingContentCarouselWidget(filterCategory)
            tracker.impressOnUpcomingCarouselContentCards(
                item.channelId, item.partner.id, listOf(item.video.coverUrl),
                item.channelType.toString().lowercase(), filterCategory, channelPositionInList
            )
        } else if (item.channelType == PlayWidgetChannelType.Live) {
            if (channelPositionInList == 0)
                tracker.impressOnLagiLiveContentCarouselWidget()
            tracker.impressOnLagiLiveCarouselContentCards(
                item.channelId,
                item.partner.id,
                listOf(item.video.coverUrl),
                item.channelType.toString().lowercase(), channelPositionInList
            )
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

    override fun onClickViewAll(view: PlayWidgetMediumView) {
        super.onClickViewAll(view)


    }
}