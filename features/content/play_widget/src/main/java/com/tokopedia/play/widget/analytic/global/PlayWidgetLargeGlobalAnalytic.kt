package com.tokopedia.play.widget.analytic.global

import com.tokopedia.play.widget.analytic.list.large.PlayWidgetInListLargeAnalyticListener
import com.tokopedia.play.widget.analytic.const.VAL_BUSINESS_UNIT
import com.tokopedia.play.widget.analytic.const.VAL_CURRENT_SITE
import com.tokopedia.play.widget.analytic.const.EVENT_CLICK
import com.tokopedia.play.widget.analytic.const.EVENT_VIEW
import com.tokopedia.play.widget.analytic.const.KEY_BUSINESS_UNIT
import com.tokopedia.play.widget.analytic.const.KEY_CURRENT_SITE
import com.tokopedia.play.widget.analytic.const.KEY_EVENT
import com.tokopedia.play.widget.analytic.const.KEY_EVENT_ACTION
import com.tokopedia.play.widget.analytic.const.KEY_EVENT_CATEGORY
import com.tokopedia.play.widget.analytic.const.KEY_EVENT_LABEL
import com.tokopedia.play.widget.analytic.const.KEY_SESSION_IRIS
import com.tokopedia.play.widget.analytic.const.KEY_USER_ID
import com.tokopedia.play.widget.analytic.const.PROMO_CLICK
import com.tokopedia.play.widget.analytic.const.PROMO_VIEW
import com.tokopedia.play.widget.analytic.const.trackerMultiFields
import com.tokopedia.play.widget.analytic.const.irisSessionId
import com.tokopedia.play.widget.analytic.const.isRilisanSpesial
import com.tokopedia.play.widget.analytic.const.toTrackingString
import com.tokopedia.play.widget.analytic.const.toTrackingType
import com.tokopedia.play.widget.analytic.global.model.PlayWidgetAnalyticModel
import com.tokopedia.play.widget.ui.PlayWidgetLargeView
import com.tokopedia.play.widget.ui.model.PlayWidgetBannerUiModel
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
class PlayWidgetLargeGlobalAnalytic @AssistedInject constructor(
    @Assisted val model: PlayWidgetAnalyticModel,
    @Assisted val trackingQueue: TrackingQueue,
    private val userSession: UserSessionInterface,
) : PlayWidgetInListLargeAnalyticListener {

    @AssistedFactory
    interface Factory {
        fun create(
            model: PlayWidgetAnalyticModel,
            trackingQueue: TrackingQueue,
        ): PlayWidgetLargeGlobalAnalytic
    }

    private val userId: String
        get() = if (userSession.isLoggedIn) userSession.userId else ""

    override fun onImpressChannelCard(
        view: PlayWidgetLargeView,
        item: PlayWidgetChannelUiModel,
        config: PlayWidgetConfigUiModel,
        channelPositionInList: Int,
        verticalWidgetPosition: Int,
    ) {
        val trackerMap = BaseTrackerBuilder().constructBasicPromotionView(
            event = PROMO_VIEW,
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
                PlayWidgetType.Large.typeString, /** widgetSize **/
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
            .appendBusinessUnit(VAL_BUSINESS_UNIT)
            .appendCurrentSite(VAL_CURRENT_SITE)
            .build()

        if (trackerMap is HashMap<String, Any>) trackingQueue.putEETracking(trackerMap)
    }

    override fun onClickChannelCard(
        view: PlayWidgetLargeView,
        item: PlayWidgetChannelUiModel,
        config: PlayWidgetConfigUiModel,
        channelPositionInList: Int,
        verticalWidgetPosition: Int,
    ) {
        val trackerMap = BaseTrackerBuilder().constructBasicPromotionClick(
            event = PROMO_CLICK,
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
                PlayWidgetType.Large.typeString, /** widgetSize **/
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
            .appendBusinessUnit(VAL_BUSINESS_UNIT)
            .appendCurrentSite(VAL_CURRENT_SITE)
            .build()

        if (trackerMap is HashMap<String, Any>) trackingQueue.putEETracking(trackerMap)
    }

    override fun onImpressBannerCard(
        view: PlayWidgetLargeView,
        item: PlayWidgetBannerUiModel,
        channelPositionInList: Int,
        verticalWidgetPosition: Int,
    ) {
        TrackApp.getInstance().gtm
            .sendGeneralEvent(
                mapOf(
                    KEY_EVENT to EVENT_VIEW,
                    KEY_EVENT_ACTION to "view other content",
                    KEY_EVENT_CATEGORY to model.category,
                    KEY_EVENT_LABEL to trackerMultiFields(
                        model.prefix, /** prefix **/
                    ),
                    KEY_BUSINESS_UNIT to VAL_BUSINESS_UNIT,
                    KEY_CURRENT_SITE to VAL_CURRENT_SITE,
                    KEY_SESSION_IRIS to irisSessionId,
                    KEY_USER_ID to userId,
                )
            )
    }

    override fun onClickBannerCard(
        view: PlayWidgetLargeView,
        item: PlayWidgetBannerUiModel,
        channelPositionInList: Int,
        verticalWidgetPosition: Int,
    ) {
        TrackApp.getInstance().gtm
            .sendGeneralEvent(
                mapOf(
                    KEY_EVENT to EVENT_CLICK,
                    KEY_EVENT_ACTION to "click other content",
                    KEY_EVENT_CATEGORY to model.category,
                    KEY_EVENT_LABEL to trackerMultiFields(
                        model.prefix, /** prefix **/
                    ),
                    KEY_BUSINESS_UNIT to VAL_BUSINESS_UNIT,
                    KEY_CURRENT_SITE to VAL_CURRENT_SITE,
                    KEY_SESSION_IRIS to irisSessionId,
                    KEY_USER_ID to userId,
                )
            )
    }

    override fun onImpressReminderIcon(
        view: PlayWidgetLargeView,
        item: PlayWidgetChannelUiModel,
        channelPositionInList: Int,
        isRemindMe: Boolean,
        verticalWidgetPosition: Int,
    ) {
        TrackApp.getInstance().gtm
            .sendGeneralEvent(
                mapOf(
                    KEY_EVENT to EVENT_VIEW,
                    KEY_EVENT_ACTION to "view remind me",
                    KEY_EVENT_CATEGORY to model.category,
                    KEY_EVENT_LABEL to trackerMultiFields(
                        model.prefix, /** prefix **/
                        item.channelType.toTrackingType(), /** videoType **/
                        item.partner.id, /** partnerId **/
                        item.channelId, /** channelId **/
                        channelPositionInList + 1, /** position **/
                        item.recommendationType, /** recommendationType **/
                        item.promoType.toTrackingString(), /** promoType **/
                    ),
                    KEY_BUSINESS_UNIT to VAL_BUSINESS_UNIT,
                    KEY_CURRENT_SITE to VAL_CURRENT_SITE,
                    KEY_SESSION_IRIS to irisSessionId,
                    KEY_USER_ID to userId,
                )
            )
    }

    override fun onClickToggleReminderChannel(
        view: PlayWidgetLargeView,
        item: PlayWidgetChannelUiModel,
        channelPositionInList: Int,
        isRemindMe: Boolean,
        verticalWidgetPosition: Int,
    ) {
        TrackApp.getInstance().gtm
            .sendGeneralEvent(
                mapOf(
                    KEY_EVENT to EVENT_CLICK,
                    KEY_EVENT_ACTION to "click remind me",
                    KEY_EVENT_CATEGORY to model.category,
                    KEY_EVENT_LABEL to trackerMultiFields(
                        model.prefix, /** prefix **/
                        item.channelType.toTrackingType(), /** videoType **/
                        item.partner.id, /** partnerId **/
                        item.channelId, /** channelId **/
                        channelPositionInList + 1, /** position **/
                        item.recommendationType, /** recommendationType **/
                        item.promoType.toTrackingString(), /** promoType **/
                    ),
                    KEY_BUSINESS_UNIT to VAL_BUSINESS_UNIT,
                    KEY_CURRENT_SITE to VAL_CURRENT_SITE,
                    KEY_SESSION_IRIS to irisSessionId,
                    KEY_USER_ID to userId,
                )
            )
    }

    override fun onClickDeleteChannel(
        view: PlayWidgetLargeView,
        item: PlayWidgetChannelUiModel,
        channelPositionInList: Int,
        verticalWidgetPosition: Int
    ) {
        /** No Global Analytic for Delete Channel in Large Widget for now */
    }
}
