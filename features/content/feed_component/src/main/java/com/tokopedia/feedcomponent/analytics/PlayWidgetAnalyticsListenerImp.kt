package com.tokopedia.feedcomponent.analytics

import com.tokopedia.feedcomponent.analytics.tracker.PlayAnalyticsTracker
import com.tokopedia.play.widget.analytic.PlayWidgetAnalyticListener
import com.tokopedia.play.widget.ui.PlayWidgetJumboView
import com.tokopedia.play.widget.ui.PlayWidgetLargeView
import com.tokopedia.play.widget.ui.PlayWidgetMediumView
import com.tokopedia.play.widget.ui.model.PlayWidgetChannelUiModel
import com.tokopedia.play.widget.ui.type.PlayWidgetChannelType
import javax.inject.Inject

//slot click and impression

class PlayWidgetAnalyticsListenerImp @Inject constructor(private val tracker: PlayAnalyticsTracker) :
    PlayWidgetAnalyticListener {

    val shopId = ""

    override fun onClickChannelCard(
        view: PlayWidgetJumboView,
        item: PlayWidgetChannelUiModel,
        channelPositionInList: Int,
        isAutoPlay: Boolean
    ) {
        super.onClickChannelCard(view, item, channelPositionInList, isAutoPlay)
        tracker.clickOnContentHighlightCardsInVideoTab(
            item.channelId,
            shopId,
            listOf(item.promoType),
            item.channelType.toString().lowercase()
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
            item.channelId,
            shopId,
            item.channelType.toString().lowercase()
        )
    }

    override fun onClickChannelCard(
        view: PlayWidgetLargeView,
        item: PlayWidgetChannelUiModel,
        channelPositionInList: Int,
        isAutoPlay: Boolean
    ) {
        super.onClickChannelCard(view, item, channelPositionInList, isAutoPlay)
        if (item.channelType == PlayWidgetChannelType.Live) {
            tracker.clickOnContentCardsInContentListPageForLagiLive(
                item.channelId,
                shopId,
                listOf(item.promoType),
                item.channelType.toString().lowercase()
            )
        }
        tracker.clickOnContentCardsInVideoTabBelowTheChips(
            item.channelId,
            shopId,
            listOf(item.promoType),
            item.channelType.toString().lowercase()
        )
    }

    override fun onImpressChannelCard(
        view: PlayWidgetLargeView,
        item: PlayWidgetChannelUiModel,
        channelPositionInList: Int,
        isAutoPlay: Boolean
    ) {
        super.onImpressChannelCard(view, item, channelPositionInList, isAutoPlay)
        if (item.channelType == PlayWidgetChannelType.Live) {
            tracker.impressOnContentCardsInContentListPageForLagiLive(
                item.channelId,
                shopId,
                listOf(item.promoType),
                item.channelType.toString().lowercase()
            )
        }
        tracker.impressOnContentCardsInVideoTabBelowTheChips(
            item.channelId,
            shopId,
            listOf(item.promoType),
            item.channelType.toString().lowercase()
        )
    }

    override fun onClickChannelCard(
        view: PlayWidgetMediumView,
        item: PlayWidgetChannelUiModel,
        channelPositionInList: Int,
        isAutoPlay: Boolean
    ) {
        super.onClickChannelCard(view, item, channelPositionInList, isAutoPlay)
        tracker.clickOnLagiLiveCarouselContentCards(
            item.channelId, shopId, listOf(item.promoType),
            item.channelType.toString().lowercase()
        )
        if (item.channelType == PlayWidgetChannelType.Upcoming) {
            tracker.clickOnUpcomingCarouselContentCards(
                item.channelId,
                shopId,
                listOf(item.promoType),
                item.channelType.toString().lowercase()
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
        tracker.impressOnLagiLiveCarouselContentCards(
            item.channelId,
            shopId,
            listOf(item.promoType),
            item.channelType.toString().lowercase()
        )
        if (item.channelType == PlayWidgetChannelType.Upcoming) {
            tracker.impressOnUpcomingCarouselContentCards(
                item.channelId,
                shopId,
                listOf(item.promoType),
                item.channelType.toString().lowercase()
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
        if (isRemindMe) {
            tracker.clickOnRemindMeButtonOnPlayCardsWithinChip(
                item.channelId,
                shopId,
                listOf(item.promoType),
                item.channelType.toString().lowercase()
            )
        } else {
            tracker.clickOnUnRemindMeButtonOnPlayCardsWithinChip(
                item.channelId,
                shopId,
                listOf(item.promoType),
                item.channelType.toString().lowercase()
            )
        }

        if (item.channelType == PlayWidgetChannelType.Upcoming) {
            if (isRemindMe) {
                tracker.clickOnRemindMeButtonOnPlayCardInUpcomingCarousel(
                    item.channelId,
                    shopId,
                    listOf(item.promoType),
                    item.channelType.toString().lowercase()
                )
            } else {
                tracker.clickOnUnRemindMeButtonOnPlayCardInUpcomingCarousel(
                    item.channelId,
                    shopId,
                    listOf(item.promoType),
                    item.channelType.toString().lowercase()
                )
            }
        }
    }

    override fun onClickViewAll(view: PlayWidgetMediumView) {
        super.onClickViewAll(view)
        tracker.clickOnSeeAllOnLagiLiveCarousel()
    }
}