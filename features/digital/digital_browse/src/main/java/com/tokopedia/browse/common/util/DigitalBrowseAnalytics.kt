package com.tokopedia.browse.common.util

import com.google.android.gms.tagmanager.DataLayer
import com.tokopedia.browse.common.constant.DigitalBrowseEventTracking.Action
import com.tokopedia.browse.common.constant.DigitalBrowseEventTracking.Event
import com.tokopedia.browse.common.data.DigitalBrowseServiceAnalyticsModel
import com.tokopedia.trackingoptimizer.TrackingQueue
import java.util.*
import javax.inject.Inject
import com.tokopedia.track.TrackApp;
import com.tokopedia.track.TrackAppUtils;
import kotlin.collections.HashMap

/**
 * @author by furqan on 30/08/18.
 */

class DigitalBrowseAnalytics @Inject
constructor() {

    private val GENERIC_CATEGORY = "homepage"
    private val EVENT_ACTION_LAYANAN_CLICK = "click on %s"
    private val EVENT_ACTION_LAYANAN_IMPRESSION = "impression on %s"


    fun eventClickBackOnBelanjaPage() {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                Event.CLICK_BACK,
                GENERIC_CATEGORY,
                Action.CLICK_BACK_BELANJA,
                ""))
    }

    fun eventClickViewAllOnBelanjaPage() {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                Event.CLICK_HOME_PAGE,
                GENERIC_CATEGORY,
                Action.CLICK_VIEW_ALL_BELANJA,
                ""))
    }

    fun eventImpressionHomePage(iconName: String, iconPosition: Int) {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                Event.IMPRESSION_HOME_PAGE,
                GENERIC_CATEGORY,
                Action.IMPRESSION_CATEGORY_BELANJA,
                iconName + "_" + iconPosition))
    }

    fun eventClickOnCategoryBelanja(iconName: String, iconPosition: Int) {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                Event.CLICK_HOME_PAGE,
                GENERIC_CATEGORY,
                Action.CLICK_CATEGORY_BELANJA,
                iconName + "_" + iconPosition))
    }

    fun eventClickBackOnLayananPage() {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                Event.CLICK_HOME_PAGE,
                GENERIC_CATEGORY,
                Action.CLICK_BACK_LAYANAN,
                ""))
    }

    fun eventClickHeaderTabLayanan(tabName: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                Event.CLICK_HOME_PAGE,
                GENERIC_CATEGORY,
                Action.CLICK_TAB_LAYANAN,
                tabName))
    }

    fun eventClickIconLayanan(analyticsModel: DigitalBrowseServiceAnalyticsModel) {
        try {
            val promotions = arrayListOf(analyticsModel.getPromoFieldObject())

            TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(DataLayer.mapOf(
                    "event", "promoClick",
                    "eventCategory", GENERIC_CATEGORY,
                    "eventAction", String.format(EVENT_ACTION_LAYANAN_CLICK,
                    analyticsModel.headerName),
                    "eventLabel", "",
                    "ecommerce", DataLayer.mapOf(
                    "promoClick", DataLayer.mapOf(
                    "promotions", promotions)
            )
            ))
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun eventImpressionIconLayanan(trackingQueue: TrackingQueue,
                                   promotionDatas: List<DigitalBrowseServiceAnalyticsModel>,
                                   headerName: String) {
        try {
            val promotions = arrayListOf<Any>()

            for (promotionItem in promotionDatas) {
                val promotion = promotionItem.getPromoFieldObject()
                promotions.add(promotion)
            }

            trackingQueue.putEETracking(
                    DataLayer.mapOf(
                            "event", "promoView",
                            "eventCategory", GENERIC_CATEGORY,
                            "eventAction", String.format(EVENT_ACTION_LAYANAN_IMPRESSION, headerName),
                            "eventLabel", "",
                            "ecommerce", DataLayer.mapOf(
                            "promoView", DataLayer.mapOf(
                            "promotions", promotions)
                    )) as HashMap<String, Any>)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
