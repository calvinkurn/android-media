package com.tokopedia.browse.categoryNavigation.analytics

import com.tokopedia.analyticconstant.DataLayer
import com.tokopedia.browse.categoryNavigation.data.model.newcategory.CategoriesItem
import com.tokopedia.browse.categoryNavigation.data.model.newcategory.CategoryChildItem
import com.tokopedia.track.TrackApp
import com.tokopedia.track.interfaces.Analytics
import com.tokopedia.trackingoptimizer.TrackingQueue
import java.util.HashMap

private const val EVENT_CATEGORY_BELANJA_PAGE = "belanja page"

class CategoryAnalytics {

    companion object {
        fun createInstance(): CategoryAnalytics {
            return CategoryAnalytics()
        }

        fun getTracker(): Analytics {
            return TrackApp.getInstance().gtm
        }
    }


    // 3
    fun eventYangLagiHitClick(product: CategoryChildItem, position: Int) {
        getTracker().sendEnhanceEcommerceEvent(
                DataLayer.mapOf(
                        "event", "promoClick",
                        "eventCategory", EVENT_CATEGORY_BELANJA_PAGE,
                        "eventAction", "click banner inside kategori hits pilihan",
                        "eventLabel", product.id + " - " + product.name,
                        "ecommerce", DataLayer.mapOf(
                        "promoClick", DataLayer.mapOf(
                        "promotions", DataLayer.listOf(
                        DataLayer.mapOf(
                                "id", product.id.toString(),
                                "name", "category/" + product.name + " - " + product.id,
                                "creative", product.name,
                                "creative_url", product.iconImageUrl,
                                "position", (position).toString(),
                                "promo_id", "",
                                "promo_code", ""
                        ))))))
    }

    // 4
    fun eventYangLagiHitView(trackingQueue: TrackingQueue, product: CategoryChildItem, position: Int) {
        val map = DataLayer.mapOf(
                "event", "promoView",
                "eventCategory", EVENT_CATEGORY_BELANJA_PAGE,
                "eventAction", "impression banner inside kategori hits pilihan",
                "eventLabel", product.id + " - " + product.name,
                "ecommerce", DataLayer.mapOf(
                "promoView", DataLayer.mapOf(
                "promotions", DataLayer.listOf(DataLayer.mapOf(
                "id", product.id.toString(),
                "name", "category/" + product.name + " - " + product.id,
                "creative", product.name,
                "creative_url", product.iconImageUrl,
                "position", (position).toString(),
                "promo_id", "",
                "promo_code", ""
        )))
        ))
        trackingQueue.putEETracking(map as HashMap<String, Any>)
    }

    //5

    fun eventSeringKamuLihatClick(product: CategoryChildItem, position: Int) {
        getTracker().sendEnhanceEcommerceEvent(
                DataLayer.mapOf(
                        "event", "promoClick",
                        "eventCategory", EVENT_CATEGORY_BELANJA_PAGE,
                        "eventAction", "click banner inside sering kamu lihat",
                        "eventLabel", product.id + " - " + product.name,
                        "ecommerce", DataLayer.mapOf(
                        "promoClick", DataLayer.mapOf(
                        "promotions", DataLayer.listOf(
                        DataLayer.mapOf(
                                "id", product.id.toString(),
                                "name", "category/" + product.name + " - " + product.id,
                                "creative", product.name,
                                "creative_url", product.iconImageUrl,
                                "position", (position).toString(),
                                "promo_id", "",
                                "promo_code", ""
                        ))))))
    }

    // 6
    fun eventSeringkkamuLihatView(trackingQueue: TrackingQueue, product: CategoryChildItem, position: Int) {
        val map = DataLayer.mapOf(
                "event", "promoView",
                "eventCategory", EVENT_CATEGORY_BELANJA_PAGE,
                "eventAction", "impression banner inside sering kamu lihat",
                "eventLabel", product.id + " - " + product.name,
                "ecommerce", DataLayer.mapOf(
                "promoView", DataLayer.mapOf(
                "promotions", DataLayer.listOf(DataLayer.mapOf(
                "id", product.id.toString(),
                "name", "category/" + product.name + " - " + product.id,
                "creative", product.name,
                "creative_url", product.iconImageUrl,
                "position", (position).toString(),
                "promo_id", "",
                "promo_code", ""
        )))
        ))
        trackingQueue.putEETracking(map as HashMap<String, Any>)
    }

    // 7

    fun eventSideBarCategoryClick(product: CategoriesItem, position: Int) {
        val eventLabel = if (position == 0) {
            product.id + " - " + product.name + "/ spesial untukmu"
        } else {
            product.id + " - " + product.name
        }
        val tracker = getTracker()
        val map = DataLayer.mapOf(
                "event", "promoClick",
                "eventCategory", EVENT_CATEGORY_BELANJA_PAGE,
                "eventAction", "click category on side bar",
                "eventLabel", eventLabel,
                "ecommerce", DataLayer.mapOf(
                "promoClick", DataLayer.mapOf(
                "promotions", DataLayer.listOf(DataLayer.mapOf(
                "id", product.id.toString(),
                "name", "category/" + product.name + " - " + product.id,
                "creative", product.name,
                "creative_url", product.iconImageUrl,
                "position", (position + 1).toString(),
                "promo_id", "",
                "promo_code", "")))
        ))
        tracker.sendEnhanceEcommerceEvent(map)
    }

    //8

    fun eventSideCategoryView(trackingQueue: TrackingQueue, product: CategoriesItem, position: Int) {
        val map = DataLayer.mapOf(
                "event", "promoView",
                "eventCategory", EVENT_CATEGORY_BELANJA_PAGE,
                "eventAction", "impression category on side bar",
                "eventLabel", product.id + " - " + product.name,
                "ecommerce", DataLayer.mapOf(
                "promoView", DataLayer.mapOf(
                "promotions", DataLayer.listOf(DataLayer.mapOf(
                "id", product.id.toString(),
                "name", "category/" + product.name + " - " + product.id,
                "creative", product.name,
                "creative_url", product.iconImageUrl,
                "position", (position).toString(),
                "promo_id", "",
                "promo_code", ""
        )))
        ))
        trackingQueue.putEETracking(map as HashMap<String, Any>)
    }

    // 9

    fun eventBannerCategoryLevelOneClick(product: CategoryChildItem) {
        val tracker = getTracker()
        val map = DataLayer.mapOf(
                "event", "promoClick",
                "eventCategory", EVENT_CATEGORY_BELANJA_PAGE,
                "eventAction", "click banner category level 1",
                "eventLabel", product.id + " - " + product.name,
                "ecommerce", DataLayer.mapOf(
                "promoClick", DataLayer.mapOf(
                "promotions", DataLayer.listOf(DataLayer.mapOf(
                "id", product.id.toString(),
                "name", "category/" + product.name + " - " + product.id,
                "creative", product.name,
                "creative_url", product.iconImageUrl,
                "promo_id", "",
                "promo_code", "")))
        ))
        tracker.sendEnhanceEcommerceEvent(map)
    }

    // 10  banner category level 2

    fun eventBannerCategoryLevelTwoClick(product: CategoryChildItem, position: Int) {
        val tracker = getTracker()
        val map = DataLayer.mapOf(
                "event", "promoClick",
                "eventCategory", EVENT_CATEGORY_BELANJA_PAGE,
                "eventAction", "cclick banner category level 2",
                "eventLabel", product.id + " - " + product.name,
                "ecommerce", DataLayer.mapOf(
                "promoClick", DataLayer.mapOf(
                "promotions", DataLayer.listOf(DataLayer.mapOf(
                "id", product.id.toString(),
                "name", "category/" + product.name + " - " + product.id,
                "creative", product.name,
                "category", "",
                "position", position,
                "creative_url", product.iconImageUrl,
                "promo_id", "",
                "promo_code", "")))
        ))
        tracker.sendEnhanceEcommerceEvent(map)
    }

    // 11
    fun eventCategoryLevelOneBannerView(trackingQueue: TrackingQueue, product: CategoryChildItem, position: Int) {
        val map = DataLayer.mapOf(
                "event", "promoView",
                "eventCategory", EVENT_CATEGORY_BELANJA_PAGE,
                "eventAction", "impression banner category level 1",
                "eventLabel", product.id + " - " + product.name,
                "ecommerce", DataLayer.mapOf(
                "promoView", DataLayer.mapOf(
                "promotions", DataLayer.listOf(DataLayer.mapOf(
                "id", product.id.toString(),
                "name", "category/" + product.name + " - " + product.id,
                "creative", product.name,
                "creative_url", product.iconImageUrl,
                "position", (position).toString(),
                "promo_id", "",
                "promo_code", ""
        )))
        ))
        trackingQueue.putEETracking(map as HashMap<String, Any>)
    }

    // 12

    fun eventCategoryLevelTwoView(trackingQueue: TrackingQueue, product: CategoryChildItem, position: Int) {
        val map = DataLayer.mapOf(
                "event", "promoView",
                "eventCategory", EVENT_CATEGORY_BELANJA_PAGE,
                "eventAction", "impression banner category level 2",
                "eventLabel", product.id + " - " + product.name,
                "ecommerce", DataLayer.mapOf(
                "promoView", DataLayer.mapOf(
                "promotions", DataLayer.listOf(DataLayer.mapOf(
                "id", product.id.toString(),
                "name", "category/" + product.name + " - " + product.id,
                "creative", product.name,
                "creative_url", product.iconImageUrl,
                "position", (position).toString(),
                "promo_id", "",
                "promo_code", ""
        )))
        ))
        trackingQueue.putEETracking(map as HashMap<String, Any>)
    }


    // 13

    fun eventBackButtonClick() {
        val tracker = getTracker()
        val map = DataLayer.mapOf(
                "event", "clickCategory",
                "eventCategory", EVENT_CATEGORY_BELANJA_PAGE,
                "eventAction", "click back button",
                "eventLabel", ""
        )
        tracker.sendEnhanceEcommerceEvent(map)
    }


}