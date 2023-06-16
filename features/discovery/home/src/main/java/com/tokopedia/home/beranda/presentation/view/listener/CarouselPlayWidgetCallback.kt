package com.tokopedia.home.beranda.presentation.view.listener

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import com.tokopedia.home.analytics.HomePageTracking.PROMO_CLICK
import com.tokopedia.home.analytics.HomePageTracking.PROMO_VIEW
import com.tokopedia.play.widget.analytic.const.irisSessionId
import com.tokopedia.play.widget.analytic.const.toTrackingType
import com.tokopedia.play.widget.analytic.const.trackerMultiFields
import com.tokopedia.play.widget.analytic.global.model.PlayWidgetHomeAnalyticModel
import com.tokopedia.play.widget.analytic.list.PlayWidgetInListAnalyticListener
import com.tokopedia.play.widget.ui.carousel.PlayWidgetCarouselView
import com.tokopedia.play.widget.ui.model.PlayWidgetChannelUiModel
import com.tokopedia.play.widget.ui.model.PlayWidgetConfigUiModel
import com.tokopedia.play.widget.ui.model.PlayWidgetProduct
import com.tokopedia.track.builder.BaseTrackerBuilder
import com.tokopedia.track.builder.Tracker
import com.tokopedia.track.builder.util.BaseTrackerConst
import com.tokopedia.trackingoptimizer.TrackingQueue
import com.tokopedia.user.session.UserSessionInterface

/**
 * Created by kenny.hadisaputra on 25/05/23
 */
class CarouselPlayWidgetCallback(
    private val trackingQueue: TrackingQueue?,
    private val userSession: UserSessionInterface,
    lifecycleOwner: LifecycleOwner
) : PlayWidgetInListAnalyticListener, BaseTrackerConst() {

    private val userId: String
        get() = if (userSession.isLoggedIn) userSession.userId else ""

    private var mHomeChannelId = ""
    private var mHeaderTitle = ""

    private val channelImpressions = mutableListOf<String>()
    private val productImpressions = mutableListOf<ProductInChannelKey>()

    init {
        lifecycleOwner.lifecycle.addObserver(object : LifecycleEventObserver {
            override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
                channelImpressions.clear()
                productImpressions.clear()
            }
        })
    }

    fun setHomeChannelId(channelId: String) {
        mHomeChannelId = channelId
    }

    fun setHeaderTitle(headerTitle: String) {
        mHeaderTitle = headerTitle
    }

    private val model = PlayWidgetHomeAnalyticModel()

    // Tracker URL: https://mynakama.tokopedia.com/datatracker/requestdetail/view/3947
    // Tracker ID: 43568
    override fun onImpressChannelCard(
        view: PlayWidgetCarouselView,
        item: PlayWidgetChannelUiModel,
        config: PlayWidgetConfigUiModel,
        channelPositionInList: Int,
        verticalWidgetPosition: Int
    ) = impressChannel(item.channelId) {
        val channelPosition = channelPositionInList + 1

        BaseTrackerBuilder().constructBasicPromotionView(
            event = PROMO_VIEW,
            eventCategory = model.category,
            eventAction = "view - video widget",
            eventLabel = trackerMultiFields(
                model.prefix, /** prefix **/
                item.channelType.toTrackingType(), /** videoType **/
                item.partner.id, /** partnerID **/
                item.channelId, /** channelID **/
                channelPosition, /** position **/
                verticalWidgetPosition, /** widgetPosition **/
                "is autoplay ${config.autoPlay}", /** isAutoPlay **/
                item.recommendationType, /** recommendationType **/
                mHomeChannelId
            ),
            promotions = listOf(
                Promotion(
                    id = "${mHomeChannelId}_0_${item.channelId}",
                    name = trackerMultiFields(
                        "/",
                        verticalWidgetPosition,
                        "dynamic channel play home widget btf",
                        "banner",
                        mHeaderTitle
                    ),
                    creative = model.promotionsCreativeName,
                    position = channelPosition.toString()
                )
            )
        ).appendUserId(userId)
            .appendBusinessUnit(VAL_BUSINESS_UNIT)
            .appendCurrentSite(CurrentSite.DEFAULT)
            .appendCustomKeyValue(TrackerId.KEY, "43568")
            .appendCustomKeyValue(KEY_SESSION_IRIS, irisSessionId)
            .appendCustomKeyValue(ChannelId.KEY, mHomeChannelId)
            .build()
    }

    // Tracker URL: https://mynakama.tokopedia.com/datatracker/requestdetail/view/3947
    // Tracker ID: 43569
    override fun onClickChannelCard(
        view: PlayWidgetCarouselView,
        item: PlayWidgetChannelUiModel,
        config: PlayWidgetConfigUiModel,
        channelPositionInList: Int,
        verticalWidgetPosition: Int
    ) {
        val channelPosition = channelPositionInList + 1

        val trackerMap = BaseTrackerBuilder().constructBasicPromotionClick(
            event = PROMO_CLICK,
            eventCategory = model.category,
            eventAction = "click - video widget",
            eventLabel = trackerMultiFields(
                model.prefix, /** prefix **/
                item.channelType.toTrackingType(), /** videoType **/
                item.partner.id, /** partnerID **/
                item.channelId, /** channelID **/
                channelPosition, /** position **/
                verticalWidgetPosition, /** widgetPosition **/
                "is autoplay ${config.autoPlay}", /** isAutoPlay **/
                item.recommendationType, /** recommendationType **/
                mHomeChannelId
            ),
            promotions = listOf(
                Promotion(
                    id = "${mHomeChannelId}_0_${item.channelId}",
                    name = trackerMultiFields(
                        "/",
                        verticalWidgetPosition,
                        "dynamic channel play home widget btf",
                        "banner",
                        mHeaderTitle
                    ),
                    creative = model.promotionsCreativeName,
                    position = channelPosition.toString()
                )
            )
        ).appendUserId(userId)
            .appendBusinessUnit(VAL_BUSINESS_UNIT)
            .appendCurrentSite(CurrentSite.DEFAULT)
            .appendCustomKeyValue(CampaignCode.KEY, "homewidget_${item.channelId}")
            .appendCustomKeyValue(TrackerId.KEY, "43569")
            .appendCustomKeyValue(KEY_SESSION_IRIS, irisSessionId)
            .appendCustomKeyValue(ChannelId.KEY, mHomeChannelId)
            .build()

        trackingQueue?.putEETracking(HashMap(trackerMap))
    }

    // Tracker URL: https://mynakama.tokopedia.com/datatracker/requestdetail/view/3947
    // Tracker ID: 43571
    fun onClickChevron(
        verticalWidgetPosition: Int
    ) {
        Tracker.Builder()
            .setEvent(EVENT_CLICK_CONTENT)
            .setEventAction("click - chevron video widget")
            .setEventCategory(model.category)
            .setEventLabel(
                trackerMultiFields(
                    model.prefix, /** prefix **/
                    verticalWidgetPosition, /** widgetPosition **/
                    mHomeChannelId
                )
            )
            .setCustomProperty(TrackerId.KEY, "43571")
            .setBusinessUnit(VAL_BUSINESS_UNIT)
            .setCurrentSite(CurrentSite.DEFAULT)
            .setCustomProperty(KEY_SESSION_IRIS, irisSessionId)
            .setUserId(userId)
            .build()
            .send()
    }

    // Tracker URL: https://mynakama.tokopedia.com/datatracker/requestdetail/view/3947
    // Tracker ID: 43572
    override fun onClickPartnerName(
        view: PlayWidgetCarouselView,
        item: PlayWidgetChannelUiModel,
        config: PlayWidgetConfigUiModel,
        channelPositionInList: Int,
        verticalWidgetPosition: Int
    ) {
        val channelPosition = channelPositionInList + 1

        Tracker.Builder()
            .setEvent(EVENT_CLICK_CONTENT)
            .setEventAction("click - creator name")
            .setEventCategory(model.category)
            .setEventLabel(
                trackerMultiFields(
                    model.prefix, /** prefix **/
                    item.channelType.toTrackingType(), /** videoType **/
                    item.partner.id, /** partnerID **/
                    item.channelId, /** channelID **/
                    channelPosition, /** position **/
                    verticalWidgetPosition, /** widgetPosition **/
                    "is autoplay ${config.autoPlay}", /** isAutoPlay **/
                    item.recommendationType, /** recommendationType **/
                    mHomeChannelId
                )
            )
            .setCustomProperty(TrackerId.KEY, "43572")
            .setBusinessUnit(VAL_BUSINESS_UNIT)
            .setCurrentSite(CurrentSite.DEFAULT)
            .setCustomProperty(KEY_SESSION_IRIS, irisSessionId)
            .setUserId(userId)
            .build()
            .send()
    }

    // Tracker URL: https://mynakama.tokopedia.com/datatracker/requestdetail/view/3947
    // Tracker ID: 43574
    override fun onClickToggleMuteButton(
        view: PlayWidgetCarouselView,
        item: PlayWidgetChannelUiModel,
        config: PlayWidgetConfigUiModel,
        channelPositionInList: Int,
        verticalWidgetPosition: Int
    ) {
        val channelPosition = channelPositionInList + 1

        Tracker.Builder()
            .setEvent(EVENT_CLICK_CONTENT)
            .setEventAction("click - content mute button")
            .setEventCategory(model.category)
            .setEventLabel(
                trackerMultiFields(
                    model.prefix, /** prefix **/
                    item.channelType.toTrackingType(), /** videoType **/
                    item.partner.id, /** partnerID **/
                    item.channelId, /** channelID **/
                    channelPosition, /** position **/
                    verticalWidgetPosition, /** widgetPosition **/
                    "is autoplay ${config.autoPlay}", /** isAutoPlay **/
                    item.recommendationType, /** recommendationType **/
                    mHomeChannelId
                )
            )
            .setCustomProperty(TrackerId.KEY, "43574")
            .setBusinessUnit(VAL_BUSINESS_UNIT)
            .setCurrentSite(CurrentSite.DEFAULT)
            .setCustomProperty(KEY_SESSION_IRIS, irisSessionId)
            .setUserId(userId)
            .build()
            .send()
    }

    // Tracker URL: https://mynakama.tokopedia.com/datatracker/requestdetail/view/3947
    // Tracker ID: 43576
    override fun onClickToggleReminderChannel(
        view: PlayWidgetCarouselView,
        item: PlayWidgetChannelUiModel,
        config: PlayWidgetConfigUiModel,
        channelPositionInList: Int,
        isRemindMe: Boolean,
        verticalWidgetPosition: Int
    ) {
        val channelPosition = channelPositionInList + 1

        Tracker.Builder()
            .setEvent(EVENT_CLICK_CONTENT)
            .setEventAction("click - upcoming content bell")
            .setEventCategory(model.category)
            .setEventLabel(
                trackerMultiFields(
                    model.prefix, /** prefix **/
                    item.channelType.toTrackingType(), /** videoType **/
                    item.partner.id, /** partnerID **/
                    item.channelId, /** channelID **/
                    channelPosition, /** position **/
                    verticalWidgetPosition, /** widgetPosition **/
                    "is autoplay ${config.autoPlay}", /** isAutoPlay **/
                    item.recommendationType, /** recommendationType **/
                    mHomeChannelId
                )
            )
            .setCustomProperty(TrackerId.KEY, "43576")
            .setBusinessUnit(VAL_BUSINESS_UNIT)
            .setCurrentSite(CurrentSite.DEFAULT)
            .setCustomProperty(KEY_SESSION_IRIS, irisSessionId)
            .setUserId(userId)
            .build()
            .send()
    }

    // Tracker URL: https://mynakama.tokopedia.com/datatracker/requestdetail/view/3947
    // Tracker ID: 43577
    override fun onImpressProductCard(
        view: PlayWidgetCarouselView,
        item: PlayWidgetChannelUiModel,
        config: PlayWidgetConfigUiModel,
        product: PlayWidgetProduct,
        productPosition: Int,
        channelPositionInList: Int,
        verticalWidgetPosition: Int
    ) = impressProductInChannel(product.id, item.channelId) {
        val channelPosition = channelPositionInList + 1

        val itemList = trackerMultiFields(
            "/",
            verticalWidgetPosition,
            "dynamic channel play home widget btf",
            "product",
            "non topads",
            "carousel",
            item.recommendationType,
            "",
            VAL_BUSINESS_UNIT,
            mHeaderTitle
        )

        mapOf(
            Event.KEY to EVENT_PRODUCT_VIEW,
            Category.KEY to model.category,
            Action.KEY to "view - product card video widget",
            Label.KEY to trackerMultiFields(
                model.prefix, /** prefix **/
                item.channelType.toTrackingType(), /** videoType **/
                item.partner.id, /** partnerID **/
                item.channelId, /** channelID **/
                channelPosition, /** position **/
                verticalWidgetPosition, /** widgetPosition **/
                "is autoplay ${config.autoPlay}", /** isAutoPlay **/
                item.recommendationType, /** recommendationType **/
                mHomeChannelId,
                product.id
            ),
            "item_list" to itemList,
            "ecommerce" to mapOf(
                "impressions" to listOf(
                    mapOf(
                        "dimension40" to itemList,
                        "dimension84" to mHomeChannelId,
                        "index" to productPosition + 1,
                        "item_brand" to "",
                        "item_category" to "",
                        "item_id" to product.id,
                        "item_name" to product.name,
                        "item_variant" to "",
                        "price" to product.price
                    )
                )
            ),
            UserId.KEY to userId,
            BusinessUnit.KEY to VAL_BUSINESS_UNIT,
            CurrentSite.KEY to CurrentSite.DEFAULT,
            KEY_SESSION_IRIS to irisSessionId,
            TrackerId.KEY to "43577"
        )
    }

    // Tracker URL: https://mynakama.tokopedia.com/datatracker/requestdetail/view/3947
    // Tracker ID: 43578
    override fun onClickProductCard(
        view: PlayWidgetCarouselView,
        item: PlayWidgetChannelUiModel,
        config: PlayWidgetConfigUiModel,
        product: PlayWidgetProduct,
        productPosition: Int,
        channelPositionInList: Int,
        verticalWidgetPosition: Int
    ) {
        val channelPosition = channelPositionInList + 1

        val itemList = trackerMultiFields(
            "/",
            verticalWidgetPosition,
            "dynamic channel play home widget btf",
            "product",
            "non topads",
            "carousel",
            item.recommendationType,
            "",
            VAL_BUSINESS_UNIT,
            mHeaderTitle
        )

        val trackerMap = mapOf(
            Event.KEY to EVENT_PRODUCT_CLICK,
            Category.KEY to model.category,
            Action.KEY to "click - product card video widget",
            Label.KEY to trackerMultiFields(
                model.prefix, /** prefix **/
                item.channelType.toTrackingType(), /** videoType **/
                item.partner.id, /** partnerID **/
                item.channelId, /** channelID **/
                channelPosition, /** position **/
                verticalWidgetPosition, /** widgetPosition **/
                "is autoplay ${config.autoPlay}", /** isAutoPlay **/
                item.recommendationType, /** recommendationType **/
                mHomeChannelId,
                product.id
            ),
            "item_list" to itemList,
            "ecommerce" to mapOf(
                "click" to mapOf(
                    "products" to listOf(
                        mapOf(
                            "dimension40" to itemList,
                            "dimension84" to mHomeChannelId,
                            "index" to productPosition + 1,
                            "item_brand" to "",
                            "item_category" to "",
                            "item_id" to product.id,
                            "item_name" to product.name,
                            "item_variant" to "",
                            "price" to product.price
                        )
                    )
                )
            ),
            UserId.KEY to userId,
            BusinessUnit.KEY to VAL_BUSINESS_UNIT,
            CurrentSite.KEY to CurrentSite.DEFAULT,
            KEY_SESSION_IRIS to irisSessionId,
            TrackerId.KEY to "43578"
        )

        trackingQueue?.putEETracking(HashMap(trackerMap))
    }

    private fun impressChannel(channelId: String, onNotImpressed: () -> Map<String, Any>) {
        if (channelImpressions.contains(channelId)) return
        channelImpressions.add(channelId)
        trackingQueue?.putEETracking(HashMap(onNotImpressed()))
    }

    private fun impressProductInChannel(
        productId: String,
        channelId: String,
        onNotImpressed: () -> Map<String, Any>
    ) {
        val key = ProductInChannelKey(productId, channelId)
        if (productImpressions.contains(key)) return
        productImpressions.add(key)
        trackingQueue?.putEETracking(HashMap(onNotImpressed()))
    }

    companion object {
        private const val EVENT_CLICK_CONTENT = "clickContent"
        private const val EVENT_PRODUCT_VIEW = "productView"
        private const val EVENT_PRODUCT_CLICK = "productClick"

        private const val KEY_SESSION_IRIS = "sessionIris"

        private const val VAL_BUSINESS_UNIT = "play"
    }

    private data class ProductInChannelKey(
        val channelId: String,
        val productId: String
    )
}
