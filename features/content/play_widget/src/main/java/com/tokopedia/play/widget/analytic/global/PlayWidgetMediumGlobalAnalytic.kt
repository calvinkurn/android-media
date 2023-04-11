package com.tokopedia.play.widget.analytic.global

import com.tokopedia.play.widget.analytic.list.medium.PlayWidgetInListMediumAnalyticListener
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
import com.tokopedia.play.widget.ui.PlayWidgetMediumView
import com.tokopedia.play.widget.ui.model.PlayWidgetBackgroundUiModel
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
class PlayWidgetMediumGlobalAnalytic @AssistedInject constructor(
    @Assisted val model: PlayWidgetAnalyticModel,
    @Assisted val trackingQueue: TrackingQueue,
    private val userSession: UserSessionInterface,
) : PlayWidgetInListMediumAnalyticListener {

    @AssistedFactory
    interface Factory {
        fun create(
            model: PlayWidgetAnalyticModel,
            trackingQueue: TrackingQueue,
        ): PlayWidgetMediumGlobalAnalytic
    }

    private val userId: String
        get() = if (userSession.isLoggedIn) userSession.userId else ""

    override fun onImpressChannelCard(
        view: PlayWidgetMediumView,
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
                channelPositionInList, /** position **/
                config.businessWidgetPosition, /** businessPosition **/
                "is autoplay ${config.autoPlay}", /** isAutoPlay **/
                config.maxAutoPlayCellularDuration, /** duration **/
                item.promoType.toTrackingString(), /** promoType **/
                item.recommendationType, /** recommendationType **/
                "is rilisanspesial ${item.promoType.isRilisanSpesial}", /** isRilisanSpesial **/
                "is giveaway ${item.hasGame}", /** isGiveaway **/
                PlayWidgetType.Medium.typeString, /** widgetSize **/
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
        view: PlayWidgetMediumView,
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
                channelPositionInList, /** position **/
                config.businessWidgetPosition, /** businessPosition **/
                "is autoplay ${config.autoPlay}", /** isAutoPlay **/
                config.maxAutoPlayCellularDuration, /** duration **/
                item.promoType.toTrackingString(), /** promoType **/
                item.recommendationType, /** recommendationType **/
                "is rilisanspesial ${item.promoType.isRilisanSpesial}", /** isRilisanSpesial **/
                "is giveaway ${item.hasGame}", /** isGiveaway **/
                PlayWidgetType.Medium.typeString, /** widgetSize **/
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

    override fun onImpressViewAll(
        view: PlayWidgetMediumView,
        verticalWidgetPosition: Int,
    ) {
        TrackApp.getInstance().gtm
            .sendGeneralEvent(
                mapOf(
                    KEY_EVENT to EVENT_VIEW,
                    KEY_EVENT_ACTION to "impress view all",
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

    override fun onClickViewAll(
        view: PlayWidgetMediumView,
        verticalWidgetPosition: Int,
    ) {
        TrackApp.getInstance().gtm
            .sendGeneralEvent(
                mapOf(
                    KEY_EVENT to EVENT_CLICK,
                    KEY_EVENT_ACTION to "click view all",
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

    override fun onImpressOverlayCard(
        view: PlayWidgetMediumView,
        item: PlayWidgetBackgroundUiModel,
        channelPositionInList: Int,
        verticalWidgetPosition: Int,
    ) {
        val trackerMap = BaseTrackerBuilder().constructBasicPromotionView(
            event = PROMO_VIEW,
            eventCategory = model.category,
            eventAction = "impression on play sgc banner",
            eventLabel = trackerMultiFields(
                model.prefix, /** prefix **/
                verticalWidgetPosition + 1, /** widgetPosition **/
            ),
            promotions = listOf(
                BaseTrackerConst.Promotion(
                    id = "",
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

    override fun onClickOverlayCard(
        view: PlayWidgetMediumView,
        item: PlayWidgetBackgroundUiModel,
        channelPositionInList: Int,
        verticalWidgetPosition: Int,
    ) {
        val trackerMap = BaseTrackerBuilder().constructBasicPromotionClick(
            event = PROMO_CLICK,
            eventCategory = model.category,
            eventAction = "click on play sgc banner",
            eventLabel = trackerMultiFields(
                model.prefix, /** prefix **/
                verticalWidgetPosition + 1, /** widgetPosition **/
            ),
            promotions = listOf(
                BaseTrackerConst.Promotion(
                    id = "",
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
        view: PlayWidgetMediumView,
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
        view: PlayWidgetMediumView,
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
        view: PlayWidgetMediumView,
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
        view: PlayWidgetMediumView,
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

    override fun onClickMoreActionChannel(
        view: PlayWidgetMediumView,
        item: PlayWidgetChannelUiModel,
        channelPositionInList: Int,
        verticalWidgetPosition: Int,
    ) {
        TrackApp.getInstance().gtm
            .sendGeneralEvent(
                mapOf(
                    KEY_EVENT to EVENT_CLICK,
                    KEY_EVENT_ACTION to "click option button on card",
                    KEY_EVENT_CATEGORY to model.category,
                    KEY_EVENT_LABEL to trackerMultiFields(
                        model.prefix, /** prefix **/
                        item.partner.id, /** partnerId **/
                    ),
                    KEY_BUSINESS_UNIT to VAL_BUSINESS_UNIT,
                    KEY_CURRENT_SITE to VAL_CURRENT_SITE,
                    KEY_SESSION_IRIS to irisSessionId,
                    KEY_USER_ID to userId,
                )
            )
    }

    override fun onClickDeleteChannel(
        view: PlayWidgetMediumView,
        item: PlayWidgetChannelUiModel,
        channelPositionInList: Int,
        verticalWidgetPosition: Int,
    ) {
        TrackApp.getInstance().gtm
            .sendGeneralEvent(
                mapOf(
                    KEY_EVENT to EVENT_CLICK,
                    KEY_EVENT_ACTION to "click delete on card",
                    KEY_EVENT_CATEGORY to model.category,
                    KEY_EVENT_LABEL to trackerMultiFields(
                        model.prefix, /** prefix **/
                        item.partner.id, /** partnerId **/
                    ),
                    KEY_BUSINESS_UNIT to VAL_BUSINESS_UNIT,
                    KEY_CURRENT_SITE to VAL_CURRENT_SITE,
                    KEY_SESSION_IRIS to irisSessionId,
                    KEY_USER_ID to userId,
                )
            )
    }
}
