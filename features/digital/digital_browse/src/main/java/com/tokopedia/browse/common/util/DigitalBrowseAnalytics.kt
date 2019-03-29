package com.tokopedia.browse.common.util

import com.google.android.gms.tagmanager.DataLayer
import com.tokopedia.abstraction.common.data.model.analytic.AnalyticTracker
import com.tokopedia.browse.common.constant.DigitalBrowseEventTracking.Action
import com.tokopedia.browse.common.constant.DigitalBrowseEventTracking.Event
import com.tokopedia.browse.common.data.DigitalBrowsePopularAnalyticsModel
import com.tokopedia.browse.common.data.DigitalBrowseServiceAnalyticsModel
import java.util.*
import javax.inject.Inject

/**
 * @author by furqan on 30/08/18.
 */

class DigitalBrowseAnalytics @Inject
constructor(private val analyticTracker: AnalyticTracker) {

    private val GENERIC_CATEGORY = "homepage"

    fun eventClickBackOnBelanjaPage() {
        analyticTracker.sendEventTracking(
                Event.CLICK_BACK,
                GENERIC_CATEGORY,
                Action.CLICK_BACK_BELANJA,
                "")
    }

    fun eventClickViewAllOnBelanjaPage() {
        analyticTracker.sendEventTracking(
                Event.CLICK_HOME_PAGE,
                GENERIC_CATEGORY,
                Action.CLICK_VIEW_ALL_BELANJA,
                "")
    }

    fun eventPromoImpressionPopularBrand(promotionDatas: List<DigitalBrowsePopularAnalyticsModel>) {
        try {
            val promotions = ArrayList<Any>()

            for (promotionItem in promotionDatas) {
                val promotion = tranformPromotionModel(promotionItem)

                promotions.add(promotion)
            }

            analyticTracker.sendEnhancedEcommerce(
                    DataLayer.mapOf(
                            "event", Event.IMPRESSION_PROMO,
                            "eventCategory", GENERIC_CATEGORY,
                            "eventAction", Action.IMPRESSION_BRAND_BELANJA,
                            "eventLabel", "",
                            "ecommerce", DataLayer.mapOf(
                            "promoView", DataLayer.mapOf(
                            "promotions", DataLayer.listOf(*promotions.toTypedArray()))
                    )
                    )
            )
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    fun eventPromoClickPopularBrand(promotionItem: DigitalBrowsePopularAnalyticsModel) {
        try {
            val promotion = tranformPromotionModel(promotionItem)

            val promotions = ArrayList<Any>()
            promotions.add(promotion)

            analyticTracker.sendEnhancedEcommerce(
                    DataLayer.mapOf(
                            "event", Event.CLICK_PROMO,
                            "eventCategory", GENERIC_CATEGORY,
                            "eventAction", Action.CLICK_BRAND_BELANJA,
                            "eventLabel", promotionItem.brandName,
                            "ecommerce", DataLayer.mapOf(
                            "promoClick", DataLayer.mapOf(
                            "promotions", DataLayer.listOf(*promotions.toTypedArray()))
                        )
                    )
            )
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    fun eventImpressionHomePage(iconName: String, iconPosition: Int) {
        analyticTracker.sendEventTracking(
                Event.IMPRESSION_HOME_PAGE,
                GENERIC_CATEGORY,
                Action.IMPRESSION_CATEGORY_BELANJA,
                iconName + "_" + iconPosition)
    }

    fun eventClickOnCategoryBelanja(iconName: String, iconPosition: Int) {
        analyticTracker.sendEventTracking(
                Event.CLICK_HOME_PAGE,
                GENERIC_CATEGORY,
                Action.CLICK_CATEGORY_BELANJA,
                iconName + "_" + iconPosition)
    }

    fun eventClickBackOnLayananPage() {
        analyticTracker.sendEventTracking(
                Event.CLICK_HOME_PAGE,
                GENERIC_CATEGORY,
                Action.CLICK_BACK_LAYANAN,
                "")
    }

    fun eventClickHeaderTabLayanan(tabName: String) {
        analyticTracker.sendEventTracking(
                Event.CLICK_HOME_PAGE,
                GENERIC_CATEGORY,
                Action.CLICK_TAB_LAYANAN,
                tabName)
    }

    fun eventImpressionIconLayanan(analyticsModel: DigitalBrowseServiceAnalyticsModel) {
        analyticTracker.sendEventTracking(
                Event.IMPRESSION_HOME_PAGE,
                GENERIC_CATEGORY,
                String.format(Action.IMPRESSION_ICON_LAYANAN, analyticsModel.headerName),
                analyticsModel.iconName + "_" + analyticsModel.headerPosition
                        + "_" + analyticsModel.iconPosition)
    }

    fun eventClickIconLayanan(analyticsModel: DigitalBrowseServiceAnalyticsModel) {
        analyticTracker.sendEventTracking(
                Event.CLICK_HOME_PAGE,
                GENERIC_CATEGORY,
                String.format(Action.CLICK_ICON_LAYANAN, analyticsModel.headerName),
                analyticsModel.iconName + "_" + analyticsModel.headerPosition
                        + "_" + analyticsModel.iconPosition)
    }

    private fun tranformPromotionModel(promotionItem: DigitalBrowsePopularAnalyticsModel): Any {
        return DataLayer.mapOf(
                "id", java.lang.Long.toString(promotionItem.bannerId),
                "name", "/belanja - Brand Pilihan",
                "creative", promotionItem.brandName,
                "position", Integer.toString(promotionItem.position))
    }

}
