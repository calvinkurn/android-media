package com.tokopedia.play.widget.sample.analytic.global

import com.tokopedia.play.widget.analytic.list.large.PlayWidgetInListLargeAnalyticListener
import com.tokopedia.play.widget.sample.analytic.const.BUSINESS_UNIT
import com.tokopedia.play.widget.sample.analytic.const.CURRENT_SITE
import com.tokopedia.play.widget.sample.analytic.const.EVENT_CLICK
import com.tokopedia.play.widget.sample.analytic.const.EVENT_VIEW
import com.tokopedia.play.widget.sample.analytic.const.PROMO_CLICK
import com.tokopedia.play.widget.sample.analytic.const.PROMO_VIEW
import com.tokopedia.play.widget.sample.analytic.const.eventLabel
import com.tokopedia.play.widget.sample.analytic.const.toTrackingString
import com.tokopedia.play.widget.sample.analytic.const.toTrackingType
import com.tokopedia.play.widget.sample.analytic.global.model.PlayWidgetAnalyticModel
import com.tokopedia.play.widget.ui.PlayWidgetLargeView
import com.tokopedia.play.widget.ui.model.PlayWidgetBannerUiModel
import com.tokopedia.play.widget.ui.model.PlayWidgetChannelUiModel
import com.tokopedia.play.widget.ui.model.PlayWidgetConfigUiModel
import com.tokopedia.play.widget.ui.model.PlayWidgetType
import com.tokopedia.play.widget.ui.type.PlayWidgetPromoType
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

    private val irisSessionId: String
        get() = TrackApp.getInstance().gtm.irisSessionId

    private val userId: String
        get() = if (userSession.isLoggedIn) userSession.userId else ""

    private val PlayWidgetPromoType.isRilisanSpesial: Boolean
        get() = when (this) {
            is PlayWidgetPromoType.Default -> isRilisanSpesial
            is PlayWidgetPromoType.LiveOnly -> isRilisanSpesial
            else -> false
        }

    override fun onImpressChannelCard(
        view: PlayWidgetLargeView,
        item: PlayWidgetChannelUiModel,
        config: PlayWidgetConfigUiModel,
        channelPositionInList: Int,
        verticalWidgetPosition: Int,
        businessWidgetPosition: Int
    ) {
        val trackerMap = BaseTrackerBuilder().constructBasicPromotionView(
            event = PROMO_VIEW,
            eventCategory = model.category,
            eventAction = "impression on play sgc channel",
            eventLabel = eventLabel(
                model.prefix, /** prefix **/
                item.channelType.toTrackingType(), /** videoType **/
                item.partner.id, /** partnerID **/
                item.channelId, /** channelID **/
                channelPositionInList + 1, /** position **/
                businessWidgetPosition, /** businessPosition **/
                "is autoplay ${config.autoPlay}", /** isAutoPlay **/
                config.maxAutoPlayCellularDuration, /** duration **/
                item.promoType.toTrackingString(), /** promoType **/
                item.recommendationType, /** recommendationType **/
                "is rilisanspesial ${item.promoType.isRilisanSpesial}", /** isRilisanSpesial **/
                "is giveaway ${item.hasGiveaway}", /** isGiveaway **/
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
            .appendBusinessUnit(BUSINESS_UNIT)
            .appendCurrentSite(CURRENT_SITE)
            .build()

        if (trackerMap is HashMap<String, Any>) trackingQueue.putEETracking(trackerMap)
    }

    override fun onClickChannelCard(
        view: PlayWidgetLargeView,
        item: PlayWidgetChannelUiModel,
        config: PlayWidgetConfigUiModel,
        channelPositionInList: Int,
        verticalWidgetPosition: Int,
        businessWidgetPosition: Int
    ) {
        val trackerMap = BaseTrackerBuilder().constructBasicPromotionClick(
            event = PROMO_CLICK,
            eventCategory = model.category,
            eventAction = "click",
            eventLabel = eventLabel(
                model.prefix, /** prefix **/
                item.channelType.toTrackingType(), /** videoType **/
                item.partner.id, /** partnerID **/
                item.channelId, /** channelID **/
                channelPositionInList + 1, /** position **/
                businessWidgetPosition, /** businessPosition **/
                "is autoplay ${config.autoPlay}", /** isAutoPlay **/
                config.maxAutoPlayCellularDuration, /** duration **/
                item.promoType.toTrackingString(), /** promoType **/
                item.recommendationType, /** recommendationType **/
                "is rilisanspesial ${item.promoType.isRilisanSpesial}", /** isRilisanSpesial **/
                "is giveaway ${item.hasGiveaway}", /** isGiveaway **/
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
            .appendBusinessUnit(BUSINESS_UNIT)
            .appendCurrentSite(CURRENT_SITE)
            .build()

        if (trackerMap is HashMap<String, Any>) trackingQueue.putEETracking(trackerMap)
    }

    override fun onImpressBannerCard(
        view: PlayWidgetLargeView,
        item: PlayWidgetBannerUiModel,
        channelPositionInList: Int,
        verticalWidgetPosition: Int,
        businessWidgetPosition: Int
    ) {
        TrackApp.getInstance().gtm
            .sendGeneralEvent(
                mapOf(
                    "event" to EVENT_VIEW,
                    "eventAction" to "view other content",
                    "eventCategory" to model.category,
                    "eventLabel" to eventLabel(
                        model.prefix, /** prefix **/
                        "", /** partnerId **/ //TODO("Ask")
                    ),
                    "businessUnit" to "play",
                    "currentSite" to CURRENT_SITE,
                    "sessionIris" to irisSessionId,
                    "userId" to userId,
                )
            )
    }

    override fun onClickBannerCard(
        view: PlayWidgetLargeView,
        item: PlayWidgetBannerUiModel,
        channelPositionInList: Int,
        verticalWidgetPosition: Int,
        businessWidgetPosition: Int
    ) {
        TrackApp.getInstance().gtm
            .sendGeneralEvent(
                mapOf(
                    "event" to EVENT_CLICK,
                    "eventAction" to "click other content",
                    "eventCategory" to model.category,
                    "eventLabel" to eventLabel(
                        model.prefix, /** prefix **/
                        "", /** partnerId **/ //TODO("Ask")
                    ),
                    "businessUnit" to "play",
                    "currentSite" to CURRENT_SITE,
                    "sessionIris" to irisSessionId,
                    "userId" to userId,
                )
            )
    }

    override fun onImpressReminderIcon(
        view: PlayWidgetLargeView,
        item: PlayWidgetChannelUiModel,
        channelPositionInList: Int,
        isRemindMe: Boolean,
        verticalWidgetPosition: Int,
        businessWidgetPosition: Int
    ) {
        TrackApp.getInstance().gtm
            .sendGeneralEvent(
                mapOf(
                    "event" to EVENT_VIEW,
                    "eventAction" to "view remind me",
                    "eventCategory" to model.category,
                    "eventLabel" to eventLabel(
                        model.prefix, /** prefix **/
                        item.channelType.toTrackingType(), /** videoType **/
                        item.partner.id, /** partnerId **/
                        item.channelId, /** channelId **/
                        channelPositionInList + 1, /** position **/
                        item.recommendationType, /** recommendationType **/
                        item.promoType.toTrackingString(), /** promoType **/
                    ),
                    "businessUnit" to "play",
                    "currentSite" to CURRENT_SITE,
                    "sessionIris" to irisSessionId,
                    "userId" to userId,
                )
            )
    }

    override fun onClickToggleReminderChannel(
        view: PlayWidgetLargeView,
        item: PlayWidgetChannelUiModel,
        channelPositionInList: Int,
        isRemindMe: Boolean,
        verticalWidgetPosition: Int,
        businessWidgetPosition: Int
    ) {
        TrackApp.getInstance().gtm
            .sendGeneralEvent(
                mapOf(
                    "event" to EVENT_CLICK,
                    "eventAction" to "click remind me",
                    "eventCategory" to model.category,
                    "eventLabel" to eventLabel(
                        model.prefix, /** prefix **/
                        item.channelType.toTrackingType(), /** videoType **/
                        item.partner.id, /** partnerId **/
                        item.channelId, /** channelId **/
                        channelPositionInList + 1, /** position **/
                        item.recommendationType, /** recommendationType **/
                        item.promoType.toTrackingString(), /** promoType **/
                    ),
                    "businessUnit" to "play",
                    "currentSite" to CURRENT_SITE,
                    "sessionIris" to irisSessionId,
                    "userId" to userId,
                )
            )
    }
}