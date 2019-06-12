package com.tokopedia.browse.categoryNavigation.analytics

import com.google.android.gms.tagmanager.DataLayer
import com.tokopedia.browse.categoryNavigation.data.model.category.CategoriesItem
import com.tokopedia.browse.categoryNavigation.data.model.category.ChildItem
import com.tokopedia.browse.categoryNavigation.data.model.hotlist.ListItem
import com.tokopedia.track.TrackApp
import com.tokopedia.track.interfaces.Analytics

class CategoryAnalytics {


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
                "eventCategory", "top nav - belanja page",
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
                "eventCategory", "belanja page",
                "eventAction", "click banner inside kategori pilihan",
                "eventLabel", product.name,
                "ecommerce", DataLayer.mapOf(
                "promoClick", DataLayer.mapOf(
                "promotions", DataLayer.listOf(DataLayer.mapOf(
                "id", product.id,
                "name", product.parentCategoryname,
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
                "eventCategory", "belanja page",
                "eventAction", "impression banner inside kategori pilihan",
                "eventLabel", "",
                "ecommerce", DataLayer.mapOf(
                "promoView", DataLayer.mapOf(
                "promotions", DataLayer.listOf(DataLayer.mapOf(
                "id", product.id,
                "name", product.parentCategoryname,
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
                "eventCategory", "belanja page",
                "eventAction", "click category on side bar",
                "eventLabel", product.name,
                "ecommerce", DataLayer.mapOf(
                "promoClick", DataLayer.mapOf(
                "promotions", DataLayer.listOf(DataLayer.mapOf(
                "id", product.id,
                "name", product.name,
                "creative", product.name,
                "creative_url", product.iconImageUrl,
                "position", position + 1)))
        ))
        tracker.sendEnhanceEcommerceEvent(map)
    }


    fun eventCategoryLevelOneView(product: CategoriesItem, position: Int) {
        val tracker = getTracker()
        val map = DataLayer.mapOf(
                "event", "promoView",
                "eventCategory", "belanja page",
                "eventAction", "impression category on side bar",
                "eventLabel", product.name,
                "ecommerce", DataLayer.mapOf(
                "promoView", DataLayer.mapOf(
                "promotions", DataLayer.listOf(DataLayer.mapOf(
                "id", product.id,
                "name", product.name,
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
                "eventCategory", "belanja page",
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
                "eventCategory", "belanja page",
                "eventAction", "click banner inside category level 1",
                "eventLabel", product.name,
                "ecommerce", DataLayer.mapOf(
                "promoClick", DataLayer.mapOf(
                "promotions", DataLayer.listOf(DataLayer.mapOf(
                "id", product.id,
                "name", product.parentCategoryname,
                "creative", product.name,
                "creative_url", product.iconImageUrl,
                "position", position + 1)))
        ))
        tracker.sendEnhanceEcommerceEvent(map)
    }


    fun eventDropDownPromoView(product: ChildItem, position: Int) {
        val tracker = getTracker()
        val map = DataLayer.mapOf(
                "event", "promoView",
                "eventCategory", "belanja page",
                "eventAction", "impression inside category level 1",
                "eventLabel", "",
                "ecommerce", DataLayer.mapOf(
                "promoView", DataLayer.mapOf(
                "promotions", DataLayer.listOf(DataLayer.mapOf(
                "id", product.id,
                "name", product.parentCategoryname,
                "creative", product.name,
                "creative_url", product.iconImageUrl,
                "position", position + 1)))
        ))
        tracker.sendEnhanceEcommerceEvent(map)
    }


    fun eventBannerInsideLevelTwoClick(product: ChildItem, position: Int) {

        val tracker = getTracker()
        val map = DataLayer.mapOf(
                "event", "promoClick",
                "eventCategory", "belanja page",
                "eventAction", "click banner inside category level 2",
                "eventLabel", "",
                "ecommerce", DataLayer.mapOf(
                "promoClick", DataLayer.mapOf(
                "promotions", DataLayer.listOf(DataLayer.mapOf(
                "id", product.id,
                "name", product.parentCategoryname,
                "creative", product.name,
                "creative_url", product.iconImageUrl,
                "position", position + 1)))
        ))
        tracker.sendEnhanceEcommerceEvent(map)
    }


    fun eventBannerInsideLevelTwoView(product: ChildItem, position: Int) {

        val tracker = getTracker()
        val map = DataLayer.mapOf(
                "event", "promoView",
                "eventCategory", "belanja page",
                "eventAction", "impression inside category level 2",
                "eventLabel", "",
                "ecommerce", DataLayer.mapOf(
                "promoView", DataLayer.mapOf(
                "promotions", DataLayer.listOf(DataLayer.mapOf(
                "id", product.id,
                "name", product.parentCategoryname,
                "creative", product.name,
                "creative_url", product.iconImageUrl,
                "position", position + 1)))
        ))
        tracker.sendEnhanceEcommerceEvent(map)
    }


    fun eventHotlistBannerClick(product: ListItem, position: Int) {

        val tracker = getTracker()
        val map = DataLayer.mapOf(
                "event", "promoClick",
                "eventCategory", "belanja page",
                "eventAction", "click on hotlist banner",
                "eventLabel", "",
                "ecommerce", DataLayer.mapOf(
                "promoClick", DataLayer.mapOf(
                "promotions", DataLayer.listOf(DataLayer.mapOf(
                "id", product.id,
                "name", product.parentName,
                "creative", product.title,
                "creative_url", product.imgSquare,
                "position", position + 1)))
        ))
        tracker.sendEnhanceEcommerceEvent(map)

    }


    fun eventHotlistBannerView(product: ListItem, position: Int) {

        val tracker = getTracker()
        val map = DataLayer.mapOf(
                "event", "promoView",
                "eventCategory", "belanja page",
                "eventAction", "impression on hotlist banner",
                "eventLabel", "",
                "ecommerce", DataLayer.mapOf(
                "promoView", DataLayer.mapOf(
                "promotions", DataLayer.listOf(DataLayer.mapOf(
                "id", product.id,
                "name", product.parentName,
                "creative", product.title,
                "creative_url", product.imgSquare,
                "position", position + 1)))
        ))
        tracker.sendEnhanceEcommerceEvent(map)

    }

    fun eventBackButtonClick() {

        val tracker = getTracker()
        val map = DataLayer.mapOf(
                "event", "clickCategory",
                "eventCategory", "belanja page",
                "eventAction", "click back",
                "eventLabel", ""
        )
        tracker.sendEnhanceEcommerceEvent(map)

    }


    // 65


}