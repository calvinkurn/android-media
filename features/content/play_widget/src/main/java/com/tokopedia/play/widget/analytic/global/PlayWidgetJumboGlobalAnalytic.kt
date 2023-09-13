package com.tokopedia.play.widget.analytic.global

import com.tokopedia.content.analytic.BusinessUnit
import com.tokopedia.content.analytic.Event
import com.tokopedia.content.analytic.Key
import com.tokopedia.play.widget.analytic.const.VAL_CURRENT_SITE
import com.tokopedia.play.widget.analytic.const.irisSessionId
import com.tokopedia.play.widget.analytic.const.isRilisanSpesial
import com.tokopedia.play.widget.analytic.const.toTrackingString
import com.tokopedia.play.widget.analytic.const.toTrackingType
import com.tokopedia.play.widget.analytic.const.trackerMultiFields
import com.tokopedia.play.widget.analytic.global.model.PlayWidgetAnalyticModel
import com.tokopedia.play.widget.analytic.list.jumbo.PlayWidgetInListJumboAnalyticListener
import com.tokopedia.play.widget.ui.PlayWidgetJumboView
import com.tokopedia.play.widget.ui.model.PlayWidgetChannelUiModel
import com.tokopedia.play.widget.ui.model.PlayWidgetConfigUiModel
import com.tokopedia.play.widget.ui.model.PlayWidgetType
import com.tokopedia.track.TrackApp
import com.tokopedia.track.builder.BaseTrackerBuilder
import com.tokopedia.track.builder.util.BaseTrackerConst
import com.tokopedia.trackingoptimizer.TrackingQueue
import com.tokopedia.user.session.UserSessionInterface
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import java.util.HashMap

/**
 * Created by kenny.hadisaputra on 31/05/22
 */
class PlayWidgetJumboGlobalAnalytic @AssistedInject constructor(
    @Assisted val model: PlayWidgetAnalyticModel,
    @Assisted val trackingQueue: TrackingQueue,
    private val userSession: UserSessionInterface
) : PlayWidgetInListJumboAnalyticListener {

    @AssistedFactory
    interface Factory {
        fun create(
            model: PlayWidgetAnalyticModel,
            trackingQueue: TrackingQueue
        ): PlayWidgetJumboGlobalAnalytic
    }

    private val userId: String
        get() = if (userSession.isLoggedIn) userSession.userId else ""

    override fun onImpressChannelCard(
        view: PlayWidgetJumboView,
        item: PlayWidgetChannelUiModel,
        config: PlayWidgetConfigUiModel,
        channelPositionInList: Int,
        verticalWidgetPosition: Int
    ) {
        val trackerMap = BaseTrackerBuilder().constructBasicPromotionView(
            event = Event.promoView,
            eventCategory = model.category,
            eventAction = model.eventActionChannel("impression on play sgc channel"),
            eventLabel = trackerMultiFields(
                model.prefix, /** prefix **/
                item.channelType.toTrackingType(), /** videoType **/
                item.partner.id, /** partnerID **/
                item.channelId, /** channelID **/
                channelPositionInList + 1, /** position **/
                config.businessWidgetPosition, /** businessPosition **/
                "is autoplay ${config.autoPlay}", /** isAutoPlay **/
                config.maxAutoPlayCellularDuration, /** duration **/
                item.promoType.toTrackingString(), /** promoType **/
                item.recommendationType, /** recommendationType **/
                "is rilisanspesial ${item.promoType.isRilisanSpesial}", /** isRilisanSpesial **/
                "is giveaway ${item.hasGame}", /** isGiveaway **/
                PlayWidgetType.Jumbo.typeString, /** widgetSize **/
            ),
            promotions = listOf(
                BaseTrackerConst.Promotion(
                    id = item.channelId,
                    name = model.promotionsItemName,
                    creative = model.promotionsCreativeName,
                    position = (channelPositionInList + 1).toString()
                )
            )
        ).appendUserId(userId)
            .appendBusinessUnit(BusinessUnit.play)
            .appendCurrentSite(VAL_CURRENT_SITE)
            .build()

        if (trackerMap is HashMap<String, Any>) trackingQueue.putEETracking(trackerMap)
    }

    override fun onClickChannelCard(
        view: PlayWidgetJumboView,
        item: PlayWidgetChannelUiModel,
        config: PlayWidgetConfigUiModel,
        channelPositionInList: Int,
        verticalWidgetPosition: Int
    ) {
        val trackerMap = BaseTrackerBuilder().constructBasicPromotionClick(
            event = Event.promoClick,
            eventCategory = model.category,
            eventAction = model.eventActionChannel("click"),
            eventLabel = trackerMultiFields(
                model.prefix, /** prefix **/
                item.channelType.toTrackingType(), /** videoType **/
                item.partner.id, /** partnerID **/
                item.channelId, /** channelID **/
                channelPositionInList + 1, /** position **/
                config.businessWidgetPosition, /** businessPosition **/
                "is autoplay ${config.autoPlay}", /** isAutoPlay **/
                config.maxAutoPlayCellularDuration, /** duration **/
                item.promoType.toTrackingString(), /** promoType **/
                item.recommendationType, /** recommendationType **/
                "is rilisanspesial ${item.promoType.isRilisanSpesial}", /** isRilisanSpesial **/
                "is giveaway ${item.hasGame}", /** isGiveaway **/
                PlayWidgetType.Jumbo.typeString, /** widgetSize **/
            ),
            promotions = listOf(
                BaseTrackerConst.Promotion(
                    id = item.channelId,
                    name = model.promotionsItemName,
                    creative = model.promotionsCreativeName,
                    position = (channelPositionInList + 1).toString()
                )
            )
        ).appendUserId(userId)
            .appendBusinessUnit(BusinessUnit.play)
            .appendCurrentSite(VAL_CURRENT_SITE)
            .build()

        if (trackerMap is HashMap<String, Any>) trackingQueue.putEETracking(trackerMap)
    }

    override fun onImpressReminderIcon(
        view: PlayWidgetJumboView,
        item: PlayWidgetChannelUiModel,
        channelPositionInList: Int,
        isRemindMe: Boolean,
        verticalWidgetPosition: Int
    ) {
        TrackApp.getInstance().gtm
            .sendGeneralEvent(
                mapOf(
                    Key.event to Event.viewContentIris,
                    Key.eventAction to "view remind me",
                    Key.eventCategory to model.category,
                    Key.eventLabel to trackerMultiFields(
                        model.prefix, /** prefix **/
                        item.channelType.toTrackingType(), /** videoType **/
                        item.partner.id, /** partnerId **/
                        item.channelId, /** channelId **/
                        channelPositionInList + 1, /** position **/
                        item.recommendationType, /** recommendationType **/
                        item.promoType.toTrackingString(), /** promoType **/
                    ),
                    Key.businessUnit to BusinessUnit.play,
                    Key.currentSite to VAL_CURRENT_SITE,
                    Key.sessionIris to irisSessionId,
                    Key.userId to userId
                )
            )
    }

    override fun onClickToggleReminderChannel(
        view: PlayWidgetJumboView,
        item: PlayWidgetChannelUiModel,
        channelPositionInList: Int,
        isRemindMe: Boolean,
        verticalWidgetPosition: Int
    ) {
        TrackApp.getInstance().gtm
            .sendGeneralEvent(
                mapOf(
                    Key.event to Event.clickContent,
                    Key.eventAction to "click remind me",
                    Key.eventCategory to model.category,
                    Key.eventLabel to trackerMultiFields(
                        model.prefix, /** prefix **/
                        item.channelType.toTrackingType(), /** videoType **/
                        item.partner.id, /** partnerId **/
                        item.channelId, /** channelId **/
                        channelPositionInList + 1, /** position **/
                        item.recommendationType, /** recommendationType **/
                        item.promoType.toTrackingString(), /** promoType **/
                    ),
                    Key.businessUnit to BusinessUnit.play,
                    Key.currentSite to VAL_CURRENT_SITE,
                    Key.sessionIris to irisSessionId,
                    Key.userId to userId
                )
            )
    }
}
