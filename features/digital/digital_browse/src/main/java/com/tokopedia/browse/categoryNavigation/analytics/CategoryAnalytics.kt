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

    val LAUNCH_PAGE_CATEGORY = "belanja/category"
    val LAUNCH_PAGE_HOME = "belanja/home"


    companion object {
        fun createInstance(): CategoryAnalytics {
            return CategoryAnalytics()
        }

        fun getTracker(): Analytics {
            return TrackApp.getInstance().gtm
        }
    }


    //3

    fun eventSearchBarClick(context: Context) {
        val tracker = getTracker()
        val map = DataLayer.mapOf(
                "event", "clickTopNav",
                "eventCategory", "top nav - " + getLaunchPageName(context),
                "eventAction", "click search box",
                "eventLabel", ""
        )
        tracker.sendEnhanceEcommerceEvent(map)
    }


    // 53

    fun eventPromoClick(context: Context, product: ChildItem, position: Int) {
        val tracker = getTracker()
        val map = DataLayer.mapOf(
                "event", "promoClick",
                "eventCategory", getLaunchPageName(context),
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


    fun eventPromoView(context: Context, product: ChildItem, position: Int) {
        val tracker = getTracker()
        val map = DataLayer.mapOf(
                "event", "promoView",
                "eventCategory", getLaunchPageName(context),
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


    fun eventCategoryLevelOneClick(context: Context, product: CategoriesItem, position: Int) {
        val tracker = getTracker()
        val map = DataLayer.mapOf(
                "event", "promoClick",
                "eventCategory", getLaunchPageName(context),
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


    fun eventCategoryLevelOneView(context: Context, product: CategoriesItem, position: Int) {
        val tracker = getTracker()
        val map = DataLayer.mapOf(
                "event", "promoView",
                "eventCategory", getLaunchPageName(context),
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


    fun eventClickLihatSemua(context: Context, categoryName: String) {

        val tracker = getTracker()
        val map = DataLayer.mapOf(
                "event", "clickCategory",
                "eventCategory", getLaunchPageName(context),
                "eventAction", "click lihat semua on category level 1",
                "eventLabel", categoryName
        )
        tracker.sendEnhanceEcommerceEvent(map)
    }

    //58

    fun eventDropDownPromoClick(context: Context, product: ChildItem, position: Int) {

        val tracker = getTracker()
        val map = DataLayer.mapOf(
                "event", "promoClick",
                "eventCategory", getLaunchPageName(context),
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


    fun eventDropDownPromoView(context: Context, product: ChildItem, position: Int) {
        val tracker = getTracker()
        val map = DataLayer.mapOf(
                "event", "promoView",
                "eventCategory", getLaunchPageName(context),
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


    fun eventBannerInsideLevelTwoClick(context: Context, product: ChildItem, position: Int) {

        val tracker = getTracker()
        val map = DataLayer.mapOf(
                "event", "promoClick",
                "eventCategory", getLaunchPageName(context),
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


    fun eventBannerInsideLevelTwoView(context: Context, product: ChildItem, position: Int) {

        val tracker = getTracker()
        val map = DataLayer.mapOf(
                "event", "promoView",
                "eventCategory", getLaunchPageName(context),
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


    fun eventHotlistBannerClick(context: Context, product: ListItem, position: Int) {

        val tracker = getTracker()
        val map = DataLayer.mapOf(
                "event", "promoClick",
                "eventCategory", getLaunchPageName(context),
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


    fun eventHotlistBannerView(context: Context, product: ListItem, position: Int) {

        val tracker = getTracker()
        val map = DataLayer.mapOf(
                "event", "promoView",
                "eventCategory", getLaunchPageName(context),
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

    fun eventBackButtonClick(context: Context) {

        val tracker = getTracker()
        val map = DataLayer.mapOf(
                "event", "clickCategory",
                "eventCategory", getLaunchPageName(context),
                "eventAction", "click back",
                "eventLabel", ""
        )
        tracker.sendEnhanceEcommerceEvent(map)

    }


    // 65

    private fun getLaunchPageName(context: Context): String {
        return if (context is CategoryBrowseActivity) {
            LAUNCH_PAGE_CATEGORY
        } else {
            LAUNCH_PAGE_HOME
        }
    }

}