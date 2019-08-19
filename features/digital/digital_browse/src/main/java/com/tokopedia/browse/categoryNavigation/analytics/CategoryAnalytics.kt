package com.tokopedia.browse.categoryNavigation.analytics

import android.content.Context
import com.google.android.gms.tagmanager.DataLayer
import com.tokopedia.browse.categoryNavigation.data.model.category.CategoriesItem
import com.tokopedia.browse.categoryNavigation.data.model.category.ChildItem
import com.tokopedia.browse.categoryNavigation.data.model.hotlist.ListItem
import com.tokopedia.browse.categoryNavigation.view.CategoryBrowseActivity
import com.tokopedia.track.TrackApp
import com.tokopedia.track.interfaces.Analytics

class CategoryAnalytics {

    val EVENT_CATEGORY_BELANJA_PAGE = "belanja page"
    val LAUNCH_PAGE_HOME = "belanja/home"

    val pageName = "/kategori-belanja"
    val nameWithPageName = "$pageName - %s"

    companion object {
        fun createInstance(): CategoryAnalytics {
            return CategoryAnalytics()
        }

        fun getTracker(): Analytics {
            return TrackApp.getInstance().gtm
        }
    }

    //3

    fun eventSearchBarClick() {
        val tracker = getTracker()
        val map = DataLayer.mapOf(
                "event", "clickTopNav",
                "eventCategory", "top nav - $EVENT_CATEGORY_BELANJA_PAGE",
                "eventAction", "click search box",
                "eventLabel", ""
        )
        tracker.sendEnhanceEcommerceEvent(map)
    }


    // 53

    fun eventPromoClick(product: ChildItem, position: Int) {
        val tracker = getTracker()
        val map = DataLayer.mapOf(
                "event", "promoClick",
                "eventCategory", EVENT_CATEGORY_BELANJA_PAGE,
                "eventAction", "click banner inside kategori pilihan",
                "eventLabel", product.name,
                "ecommerce", DataLayer.mapOf(
                "promoClick", DataLayer.mapOf(
                "promotions", DataLayer.listOf(DataLayer.mapOf(
                "id", product.id,
                "name", String.format(nameWithPageName, product.parentCategoryname),
                "creative", product.name,
                "creative_url", product.iconImageUrl,
                "position", position + 1)))
        ))
        tracker.sendEnhanceEcommerceEvent(map)
    }

    // 54


    fun eventPromoView(product: ChildItem, position: Int) {
        val tracker = getTracker()
        val map = DataLayer.mapOf(
                "event", "promoView",
                "eventCategory", EVENT_CATEGORY_BELANJA_PAGE,
                "eventAction", "impression banner inside kategori pilihan",
                "eventLabel", "",
                "ecommerce", DataLayer.mapOf(
                "promoView", DataLayer.mapOf(
                "promotions", DataLayer.listOf(DataLayer.mapOf(
                "id", product.id,
                "name", String.format(nameWithPageName, product.parentCategoryname),
                "creative", product.name,
                "creative_url", product.iconImageUrl,
                "position", position + 1)))
        ))
        tracker.sendEnhanceEcommerceEvent(map)
    }

    // 55

    fun eventCategoryLevelOneClick(product: CategoriesItem, position: Int) {
        val tracker = getTracker()
        val map = DataLayer.mapOf(
                "event", "promoClick",
                "eventCategory", EVENT_CATEGORY_BELANJA_PAGE,
                "eventAction", "click category on side bar",
                "eventLabel", product.name,
                "ecommerce", DataLayer.mapOf(
                "promoClick", DataLayer.mapOf(
                "promotions", DataLayer.listOf(DataLayer.mapOf(
                "id", product.id,
                "name", String.format(nameWithPageName, "side bar"),
                "creative", product.name,
                "creative_url", product.iconImageUrl,
                "position", position + 1)))
        ))
        tracker.sendEnhanceEcommerceEvent(map)
    }

    //56

    fun eventCategoryLevelOneView(product: CategoriesItem, position: Int) {
        val tracker = getTracker()
        val map = DataLayer.mapOf(
                "event", "promoView",
                "eventCategory", EVENT_CATEGORY_BELANJA_PAGE,
                "eventAction", "impression category on side bar",
                "eventLabel", "",
                "ecommerce", DataLayer.mapOf(
                "promoView", DataLayer.mapOf(
                "promotions", DataLayer.listOf(DataLayer.mapOf(
                "id", product.id,
                "name", String.format(nameWithPageName, "side bar"),
                "creative", product.name,
                "creative_url", product.iconImageUrl,
                "position", position + 1)))
        ))
        tracker.sendEnhanceEcommerceEvent(map)
    }

    // 57

    fun eventClickLihatSemua(categoryName: String) {

        val tracker = getTracker()
        val map = DataLayer.mapOf(
                "event", "clickCategory",
                "eventCategory", EVENT_CATEGORY_BELANJA_PAGE,
                "eventAction", "click lihat semua on category level 1",
                "eventLabel", categoryName
        )
        tracker.sendEnhanceEcommerceEvent(map)
    }

    //58

    fun eventDropDownPromoClick(product: ChildItem, position: Int) {

        val tracker = getTracker()
        val map = DataLayer.mapOf(
                "event", "promoClick",
                "eventCategory", EVENT_CATEGORY_BELANJA_PAGE,
                "eventAction", "click banner inside category level 1",
                "eventLabel", product.name,
                "ecommerce", DataLayer.mapOf(
                "promoClick", DataLayer.mapOf(
                "promotions", DataLayer.listOf(DataLayer.mapOf(
                "id", product.id,
                "name", String.format(nameWithPageName, "inside category level 1"),
                "creative", product.name,
                "creative_url", product.iconImageUrl,
                "position", position + 1)))
        ))
        tracker.sendEnhanceEcommerceEvent(map)
    }

    //59

    fun eventDropDownPromoView(product: ChildItem, position: Int) {
        val tracker = getTracker()
        val map = DataLayer.mapOf(
                "event", "promoView",
                "eventCategory", EVENT_CATEGORY_BELANJA_PAGE,
                "eventAction", "impression inside category level 1",
                "eventLabel", "",
                "ecommerce", DataLayer.mapOf(
                "promoView", DataLayer.mapOf(
                "promotions", DataLayer.listOf(DataLayer.mapOf(
                "id", product.id,
                "name", String.format(nameWithPageName, "inside category level 1"),
                "creative", product.name,
                "creative_url", product.iconImageUrl,
                "position", position + 1)))
        ))
        tracker.sendEnhanceEcommerceEvent(map)
    }

    //60

    fun eventBannerInsideLevelTwoClick(product: ChildItem, position: Int) {

        val tracker = getTracker()
        val map = DataLayer.mapOf(
                "event", "promoClick",
                "eventCategory", EVENT_CATEGORY_BELANJA_PAGE,
                "eventAction", "click banner inside category level 2",
                "eventLabel", "",
                "ecommerce", DataLayer.mapOf(
                "promoClick", DataLayer.mapOf(
                "promotions", DataLayer.listOf(DataLayer.mapOf(
                "id", product.id,
                "name", String.format(nameWithPageName, "inside category level 2"),
                "creative", product.name,
                "creative_url", product.iconImageUrl,
                "position", position + 1)))
        ))
        tracker.sendEnhanceEcommerceEvent(map)
    }

    // 61

    fun eventBannerInsideLevelTwoView(product: ChildItem, position: Int) {

        val tracker = getTracker()
        val map = DataLayer.mapOf(
                "event", "promoView",
                "eventCategory", EVENT_CATEGORY_BELANJA_PAGE,
                "eventAction", "impression inside category level 2",
                "eventLabel", "",
                "ecommerce", DataLayer.mapOf(
                "promoView", DataLayer.mapOf(
                "promotions", DataLayer.listOf(DataLayer.mapOf(
                "id", product.id,
                "name", String.format(nameWithPageName, "inside category level 2"),
                "creative", product.name,
                "creative_url", product.iconImageUrl,
                "position", position + 1)))
        ))
        tracker.sendEnhanceEcommerceEvent(map)
    }

    // 62

    fun eventHotlistBannerClick(product: ListItem, position: Int) {

        val tracker = getTracker()
        val map = DataLayer.mapOf(
                "event", "promoClick",
                "eventCategory", EVENT_CATEGORY_BELANJA_PAGE,
                "eventAction", "click on hotlist banner",
                "eventLabel", "",
                "ecommerce", DataLayer.mapOf(
                "promoClick", DataLayer.mapOf(
                "promotions", DataLayer.listOf(DataLayer.mapOf(
                "id", product.id,
                "name", String.format(nameWithPageName, product.parentName),
                "creative", product.title,
                "creative_url", product.imgSquare,
                "position", position + 1)))
        ))
        tracker.sendEnhanceEcommerceEvent(map)

    }

    //63

    fun eventHotlistBannerView(product: ListItem, position: Int) {

        val tracker = getTracker()
        val map = DataLayer.mapOf(
                "event", "promoView",
                "eventCategory", EVENT_CATEGORY_BELANJA_PAGE,
                "eventAction", "impression on hotlist banner",
                "eventLabel", "",
                "ecommerce", DataLayer.mapOf(
                "promoView", DataLayer.mapOf(
                "promotions", DataLayer.listOf(DataLayer.mapOf(
                "id", product.id,
                "name", String.format(nameWithPageName, product.parentName),
                "creative", product.title,
                "creative_url", product.imgSquare,
                "position", position + 1)))
        ))
        tracker.sendEnhanceEcommerceEvent(map)

    }

    // 64

    fun eventBackButtonClick() {

        val tracker = getTracker()
        val map = DataLayer.mapOf(
                "event", "clickCategory",
                "eventCategory", EVENT_CATEGORY_BELANJA_PAGE,
                "eventAction", "click back",
                "eventLabel", ""
        )
        tracker.sendEnhanceEcommerceEvent(map)

    }
}