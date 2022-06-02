package com.tokopedia.play.widget.sample.analytic.global

import com.tokopedia.config.GlobalConfig
import com.tokopedia.play.widget.analytic.list.small.PlayWidgetInListSmallAnalyticListener
import com.tokopedia.play.widget.ui.PlayWidgetSmallView
import com.tokopedia.play.widget.ui.model.PlayWidgetChannelUiModel
import com.tokopedia.play.widget.ui.type.PlayWidgetChannelType
import com.tokopedia.play.widget.ui.type.PlayWidgetPromoType
import com.tokopedia.track.TrackApp
import com.tokopedia.user.session.UserSessionInterface
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject

/**
 * Created by kenny.hadisaputra on 31/05/22
 */
class PlayWidgetSmallGlobalAnalytic @AssistedInject constructor(
    @Assisted val model: PlayWidgetAnalyticModel,
    private val userSession: UserSessionInterface,
) : PlayWidgetInListSmallAnalyticListener {

    @AssistedFactory
    interface Factory {
        fun create(model: PlayWidgetAnalyticModel): PlayWidgetSmallGlobalAnalytic
    }

    private val irisSessionId: String
        get() = TrackApp.getInstance().gtm.irisSessionId

    private val userId: String
        get() = if (userSession.isLoggedIn) userSession.userId else ""

    private val currentSite: String
        get() = if (GlobalConfig.isSellerApp()) "tokopediaseller" else "tokopediamarketplace"

    private val PlayWidgetPromoType.isRilisanSpesial: Boolean
        get() = when (this) {
            is PlayWidgetPromoType.Default -> isRilisanSpesial
            is PlayWidgetPromoType.LiveOnly -> isRilisanSpesial
            else -> false
        }

    override fun onImpressChannelCard(
        view: PlayWidgetSmallView,
        item: PlayWidgetChannelUiModel,
        channelPositionInList: Int,
        isAutoPlay: Boolean,
        verticalWidgetPosition: Int,
        businessWidgetPosition: Int,
    ) {
        TrackApp.getInstance().gtm
            .sendGeneralEvent(
                mapOf(
                    "event" to EVENT_VIEW_ITEM,
                    "eventAction" to "impression on play sgc channel",
                    "eventCategory" to model.category,
                    "eventLabel" to eventLabel(
                        model.prefix, /** prefix **/
                        item.channelType.toTrackingType(), /** videoType **/
                        item.partner.id, /** partnerID **/
                        item.channelId, /** channelID **/
                        channelPositionInList + 1, /** position **/
                        businessWidgetPosition, /** businessPosition **/
                        "is autoplay $isAutoPlay", /** isAutoPlay **/
                        "", /** duration **/ //TODO("Ask Aryo")
                        item.promoType.toTrackingString(), /** promoType **/
                        item.recommendationType, /** recommendationType **/
                        "is rilisanspesial ${item.promoType.isRilisanSpesial}", /** isRilisanSpesial **/
                        "is giveaway ${item.hasGiveaway}", /** isGiveaway **/
                        WIDGET_SIZE, /** widgetSize **/
                    ),
                    "businessUnit" to "play",
                    "currentSite" to currentSite,
                    "promotions" to listOf(
                        mapOf(
                            "creative_name" to model.promotionsCreativeName,
                            "creative_slot" to channelPositionInList + 1,
                            "item_id" to item.channelId,
                            "item_name" to model.promotionsItemName,
                        ),
                    ),
                    "sessionIris" to irisSessionId,
                    "userId" to userId,
                )
            )
    }

    override fun onClickChannelCard(
        view: PlayWidgetSmallView,
        item: PlayWidgetChannelUiModel,
        channelPositionInList: Int,
        isAutoPlay: Boolean,
        verticalWidgetPosition: Int,
        businessWidgetPosition: Int,
    ) {
        TrackApp.getInstance().gtm
            .sendGeneralEvent(
                mapOf(
                    "event" to EVENT_CLICK_ITEM,
                    "eventAction" to "click",
                    "eventCategory" to model.category,
                    "eventLabel" to eventLabel(
                        model.prefix, /** prefix **/
                        item.channelType.toTrackingType(), /** videoType **/
                        item.partner.id, /** partnerID **/
                        item.channelId, /** channelID **/
                        channelPositionInList + 1, /** position **/
                        businessWidgetPosition, /** businessPosition **/
                        "is autoplay $isAutoPlay", /** isAutoPlay **/
                        "", /** duration **/ //TODO("Ask Aryo")
                        item.promoType.toTrackingString(), /** promoType **/
                        item.recommendationType, /** recommendationType **/
                        "is rilisanspesial ${item.promoType.isRilisanSpesial}", /** isRilisanSpesial **/
                        "is giveaway ${item.hasGiveaway}", /** isGiveaway **/
                        WIDGET_SIZE, /** widgetSize **/
                    ),
                    "businessUnit" to "play",
                    "currentSite" to currentSite,
                    "promotions" to listOf(
                        mapOf(
                            "creative_name" to model.promotionsCreativeName,
                            "creative_slot" to channelPositionInList + 1,
                            "item_id" to item.channelId,
                            "item_name" to model.promotionsItemName,
                        ),
                    ),
                    "sessionIris" to irisSessionId,
                    "userId" to userId,
                )
            )
    }

    /**
     * View all
     */
    override fun onImpressViewAll(
        view: PlayWidgetSmallView,
        verticalWidgetPosition: Int,
        businessWidgetPosition: Int,
    ) {
        TrackApp.getInstance().gtm
            .sendGeneralEvent(
                mapOf(
                    "event" to EVENT_VIEW,
                    "eventAction" to "impress view all",
                    "eventCategory" to model.category,
                    "eventLabel" to eventLabel(
                        model.prefix, /** prefix **/
                        "", /** partnerId **/ //TODO("Ask")
                    ),
                    "businessUnit" to "play",
                    "currentSite" to currentSite,
                    "sessionIris" to irisSessionId,
                    "userId" to userId,
                )
            )
    }

    override fun onClickViewAll(
        view: PlayWidgetSmallView,
        verticalWidgetPosition: Int,
        businessWidgetPosition: Int,
    ) {
        TrackApp.getInstance().gtm
            .sendGeneralEvent(
                mapOf(
                    "event" to EVENT_CLICK,
                    "eventAction" to "click view all",
                    "eventCategory" to model.category,
                    "eventLabel" to eventLabel(
                        model.prefix, /** prefix **/
                        "", /** partnerId **/ //TODO("Ask")
                    ),
                    "businessUnit" to "play",
                    "currentSite" to currentSite,
                    "sessionIris" to irisSessionId,
                    "userId" to userId,
                )
            )
    }

    override fun onImpressBannerCard(
        view: PlayWidgetSmallView,
        verticalWidgetPosition: Int,
        businessWidgetPosition: Int,
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
                    "currentSite" to currentSite,
                    "sessionIris" to irisSessionId,
                    "userId" to userId,
                )
            )
    }

    override fun onClickBannerCard(
        view: PlayWidgetSmallView,
        verticalWidgetPosition: Int,
        businessWidgetPosition: Int,
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
                    "currentSite" to currentSite,
                    "sessionIris" to irisSessionId,
                    "userId" to userId,
                )
            )
    }

    override fun onLabelPromoClicked(
        view: PlayWidgetSmallView,
        item: PlayWidgetChannelUiModel,
        channelPositionInList: Int,
        businessWidgetPosition: Int,
        isAutoPlay: Boolean
    ) {
        super.onLabelPromoClicked(
            view,
            item,
            channelPositionInList,
            businessWidgetPosition,
            isAutoPlay
        )
    }

    override fun onLabelPromoImpressed(
        view: PlayWidgetSmallView,
        item: PlayWidgetChannelUiModel,
        channelPositionInList: Int,
        businessWidgetPosition: Int,
        isAutoPlay: Boolean
    ) {
        super.onLabelPromoImpressed(
            view,
            item,
            channelPositionInList,
            businessWidgetPosition,
            isAutoPlay
        )
    }

    private fun PlayWidgetChannelType.toTrackingType() = when (this) {
        PlayWidgetChannelType.Live -> "live"
        PlayWidgetChannelType.Vod -> "vod"
        PlayWidgetChannelType.Upcoming -> "upcoming"
        else -> "null"
    }

    private fun PlayWidgetPromoType.toTrackingString(): String {
        return promoText.ifBlank { "no promo" }
    }

    private fun eventLabel(vararg label: Any): String {
        return label.joinToString { " - " }
    }

    companion object {
        private const val EVENT_VIEW_ITEM = "view_item"
        private const val EVENT_CLICK_ITEM = "select_content"

        private const val EVENT_VIEW = "viewContentIris"
        private const val EVENT_CLICK = "clickContent"

        private const val WIDGET_SIZE = "small"
    }
}