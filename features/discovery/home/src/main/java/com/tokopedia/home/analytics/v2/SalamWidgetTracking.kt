package com.tokopedia.home.analytics.v2

import com.tokopedia.analyticconstant.DataLayer
import com.tokopedia.home.beranda.domain.model.salam_widget.SalamWidgetData
import com.tokopedia.trackingoptimizer.TrackingQueue

object SalamWidgetTracking : BaseTracking(){
    private const val SALAM_WIDGET_NAME = "salam"
    private const val SALAM_WIDGET_EVENT_CATEGORY = "homepage-salam"
    private const val SALAM_WIDGET_CLICK_EVENT = "clickHomepage"
    private const val CLICK_CLOSE_ON = "click close on %s"

    fun homeSalamWidgetImpressionTracker(
            trackingQueue: TrackingQueue,
            salamWidgetData: SalamWidgetData,
            userID:String
    ){
        val productName = salamWidgetData.appLink
        trackingQueue.putEETracking(getBasicPromotionView(
                Event.PROMO_VIEW,
                SALAM_WIDGET_EVENT_CATEGORY,
                Action.IMPRESSION_ON.format(SALAM_WIDGET_NAME),
                salamWidgetData.id.toString(),
                listOf(mapToPromotionTracker(salamWidgetData)),
                userID
        ) as? HashMap<String, Any>)
    }


    fun homeSalamWidgetOnClickTracker(
            trackingQueue: TrackingQueue,
            salamWidgetData: SalamWidgetData
    ){
        val productName = salamWidgetData.appLink
        val promotionClickData = DataLayer.mapOf(
                Event.KEY, Event.PROMO_CLICK,
                Category.KEY, SALAM_WIDGET_EVENT_CATEGORY,
                Action.KEY, Action.CLICK_ON.format(SALAM_WIDGET_NAME),
                Label.KEY, salamWidgetData.id.toString(),
                Ecommerce.KEY, Ecommerce.getEcommercePromoClick(listOf(mapToPromotionTracker(salamWidgetData)))
        )
        trackingQueue.putEETracking(promotionClickData as? HashMap<String, Any>)
    }

    fun homeSalamWidgetOnCloseTracker(salamWidgetData: SalamWidgetData){
        val productName = salamWidgetData.appLink
        getTracker().sendGeneralEvent(
                SALAM_WIDGET_CLICK_EVENT,
                SALAM_WIDGET_EVENT_CATEGORY,
                CLICK_CLOSE_ON.format(SALAM_WIDGET_NAME),
                salamWidgetData.id.toString()
        )
    }

    private fun mapToPromotionTracker(model: SalamWidgetData) = Promotion(
            id = model.id.toString(),
            creative = model.title,
            name = "$SALAM_WIDGET_NAME - ${model.id}",
            position = "none/other")


}