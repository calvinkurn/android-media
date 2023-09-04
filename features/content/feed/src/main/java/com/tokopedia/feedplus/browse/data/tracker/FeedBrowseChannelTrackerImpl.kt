package com.tokopedia.feedplus.browse.data.tracker

import com.tokopedia.content.analytic.BusinessUnit
import com.tokopedia.content.analytic.CurrentSite
import com.tokopedia.content.analytic.Event
import com.tokopedia.content.analytic.EventCategory
import com.tokopedia.content.analytic.Key
import com.tokopedia.feedplus.browse.presentation.model.FeedBrowseChipUiModel
import com.tokopedia.feedplus.browse.presentation.model.FeedBrowseUiModel
import com.tokopedia.play.widget.analytic.const.toTrackingType
import com.tokopedia.play.widget.analytic.const.trackerMultiFields
import com.tokopedia.play.widget.ui.model.PlayWidgetChannelUiModel
import com.tokopedia.play.widget.ui.model.PlayWidgetConfigUiModel
import com.tokopedia.track.builder.BaseTrackerBuilder
import com.tokopedia.track.builder.util.BaseTrackerConst
import com.tokopedia.trackingoptimizer.TrackingQueue
import com.tokopedia.user.session.UserSessionInterface
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject

/**
 * Created by meyta.taliti on 01/09/23.
 */
class FeedBrowseChannelTrackerImpl @AssistedInject constructor(
    @Assisted private val prefix: String,
    private val userSession: UserSessionInterface,
    private val trackingQueue: TrackingQueue
) : FeedBrowseChannelTracker {

    @AssistedFactory
    interface Factory : FeedBrowseChannelTracker.Factory {
        override fun create(prefix: String): FeedBrowseChannelTrackerImpl
    }

    override fun sendViewChannelCardEvent(
        item: PlayWidgetChannelUiModel,
        config: PlayWidgetConfigUiModel,
        widget: FeedBrowseUiModel.Channel,
        channelPositionInList: Int,
        verticalWidgetPosition: Int
    ) {
        val widgetPosition = verticalWidgetPosition + 1
        val channelPosition = channelPositionInList + 1
        val trackerMap = BaseTrackerBuilder().constructBasicPromotionClick(
            event = Event.promoView,
            eventCategory = EventCategory.browseFeed,
            eventAction = "view - channel card",
            eventLabel = trackerMultiFields(
                prefix, /** prefix **/
                item.channelType.toTrackingType(), /** videoType **/
                item.partner.id, /** partnerID **/
                item.channelId, /** channelID **/
                channelPositionInList, /** position **/
                config.businessWidgetPosition, /** businessPosition **/
                "null", /** isAutoPlay **/
                config.maxAutoPlayCellularDuration, /** duration **/
                item.recommendationType, /** recommendationType **/
            ),
            promotions = listOf(
                BaseTrackerConst.Promotion(
                    id = "${item.channelId} - ${widget.title}",
                    name = "/ - $prefix - $widgetPosition - channel card - ${widget.title}",
                    creative = widgetPosition.toString(),
                    position = channelPosition.toString()
                )
            )
        )
            .appendCustomKeyValue(Key.trackerId, "45741")
            .appendBusinessUnit(BusinessUnit.content)
            .appendCurrentSite(CurrentSite.tokopediaMarketplace)
            .appendUserId(userSession.userId)
            .build()

        if (trackerMap is HashMap<String, Any>) trackingQueue.putEETracking(trackerMap)
    }

    override fun sendViewChipsWidgetEvent(
        item: FeedBrowseChipUiModel,
        widget: FeedBrowseUiModel.Channel,
        chipPositionInList: Int,
        verticalWidgetPosition: Int
    ) {
        val widgetPosition = verticalWidgetPosition + 1
        val channelPosition = chipPositionInList + 1
        val trackerMap = BaseTrackerBuilder().constructBasicPromotionClick(
            event = Event.promoView,
            eventCategory = EventCategory.browseFeed,
            eventAction = "view - chips recom widget",
            eventLabel = trackerMultiFields(
                prefix,
                item.label,
                widget.title
            ),
            promotions = listOf(
                BaseTrackerConst.Promotion(
                    id = "${item.id} - ${widget.title}",
                    name = item.label,
                    creative = widgetPosition.toString(),
                    position = channelPosition.toString()
                )
            )
        )
            .appendCustomKeyValue(Key.trackerId, "45742")
            .appendBusinessUnit(BusinessUnit.content)
            .appendCurrentSite(CurrentSite.tokopediaMarketplace)
            .appendUserId(userSession.userId)
            .build()

        if (trackerMap is HashMap<String, Any>) trackingQueue.putEETracking(trackerMap)
    }

    override fun sendClickChannelCardEvent(
        item: PlayWidgetChannelUiModel,
        config: PlayWidgetConfigUiModel,
        widget: FeedBrowseUiModel.Channel,
        channelPositionInList: Int,
        verticalWidgetPosition: Int
    ) {
        val widgetPosition = verticalWidgetPosition + 1
        val channelPosition = channelPositionInList + 1
        val trackerMap = BaseTrackerBuilder().constructBasicPromotionClick(
            event = Event.promoClick,
            eventCategory = EventCategory.browseFeed,
            eventAction = "click - channel card",
            eventLabel = trackerMultiFields(
                prefix, /** prefix **/
                item.channelType.toTrackingType(), /** videoType **/
                item.partner.id, /** partnerID **/
                item.channelId, /** channelID **/
                channelPositionInList, /** position **/
                config.businessWidgetPosition, /** businessPosition **/
                "null", /** isAutoPlay **/
                config.maxAutoPlayCellularDuration, /** duration **/
                item.recommendationType, /** recommendationType **/
            ),
            promotions = listOf(
                BaseTrackerConst.Promotion(
                    id = "${item.channelId} - ${widget.title}",
                    name = "/ - $prefix - $widgetPosition - channel card - ${widget.title}",
                    creative = widgetPosition.toString(),
                    position = channelPosition.toString()
                )
            )
        )
            .appendCustomKeyValue(Key.trackerId, "45743")
            .appendBusinessUnit(BusinessUnit.content)
            .appendCurrentSite(CurrentSite.tokopediaMarketplace)
            .appendUserId(userSession.userId)
            .build()

        if (trackerMap is HashMap<String, Any>) trackingQueue.putEETracking(trackerMap)
    }

    override fun sendClickChipsWidgetEvent(
        item: FeedBrowseChipUiModel,
        widget: FeedBrowseUiModel.Channel,
        chipPositionInList: Int,
        verticalWidgetPosition: Int
    ) {
        val widgetPosition = verticalWidgetPosition + 1
        val channelPosition = chipPositionInList + 1
        val trackerMap = BaseTrackerBuilder().constructBasicPromotionClick(
            event = Event.promoClick,
            eventCategory = EventCategory.browseFeed,
            eventAction = "click - chips recom widget",
            eventLabel = trackerMultiFields(
                prefix,
                item.label,
                widget.title
            ),
            promotions = listOf(
                BaseTrackerConst.Promotion(
                    id = "${item.id} - ${widget.title}",
                    name = item.label,
                    creative = widgetPosition.toString(),
                    position = channelPosition.toString()
                )
            )
        )
            .appendCustomKeyValue(Key.trackerId, "45744")
            .appendBusinessUnit(BusinessUnit.content)
            .appendCurrentSite(CurrentSite.tokopediaMarketplace)
            .appendUserId(userSession.userId)
            .build()

        if (trackerMap is HashMap<String, Any>) trackingQueue.putEETracking(trackerMap)
    }
}
