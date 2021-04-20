package com.tokopedia.kategori.analytics

import com.tokopedia.analyticconstant.DataLayer
import com.tokopedia.common_category.data.kategorymodel.CategoriesItem
import com.tokopedia.kategori.model.CategoryChildItem
import com.tokopedia.track.TrackApp
import com.tokopedia.track.interfaces.Analytics
import java.util.HashMap

private const val EVENT_CATEGORY_BELANJA_PAGE = "belanja page"

private const val KEY_EVENT = "event"
private const val KEY_EVENT_CATEGORY = "eventCategory"
private const val KEY_EVENT_ACTION = "eventAction"
private const val KEY_EVENT_LABEL = "eventLabel"
private const val KEY_PROMOTIONS = "promotions"
private const val KEY_PROMO_VIEW = "promoView"
private const val KEY_PROMO_CLICK = "promoClick"
private const val KEY_CREATIVE = "creative"
private const val KEY_CREATIVE_URL = "creative_url"
private const val KEY_PROMO_ID = "promo_id"
private const val KEY_POSITION = "position"
private const val KEY_ECOMMERCE = "ecommerce"
private const val KEY_PROMO_CODE = "promo_code"
private const val KEY_NAME = "name"
private const val KEY_ID = "id"
private const val EVENT_PROMO_CLICK = "promoClick"
private const val EVENT_PROMO_VIEW = "promoView"

class CategoryAnalytics {

    companion object {
        val categoryAnalytics: CategoryAnalytics by lazy { CategoryAnalytics() }
    }

    private fun getTracker(): Analytics {
        return TrackApp.getInstance().gtm
    }

    // 3
    fun eventYangLagiHitClick(product: CategoryChildItem, position: Int) {
        getTracker().sendEnhanceEcommerceEvent(
                DataLayer.mapOf(
                        KEY_EVENT, EVENT_PROMO_CLICK,
                        KEY_EVENT_CATEGORY, EVENT_CATEGORY_BELANJA_PAGE,
                        KEY_EVENT_ACTION, "click banner inside kategori hits pilihan",
                        KEY_EVENT_LABEL, "",
                        KEY_ECOMMERCE, DataLayer.mapOf(
                        KEY_PROMO_CLICK, DataLayer.mapOf(
                        KEY_PROMOTIONS, DataLayer.listOf(
                        DataLayer.mapOf(
                                KEY_ID, product.id.toString(),
                                KEY_NAME, "banner inside kategori hits pilihan",
                                KEY_CREATIVE, product.id + " - " + product.name,
                                KEY_CREATIVE_URL, product.iconImageUrl,
                                KEY_POSITION, (position).toString(),
                                KEY_PROMO_ID, "",
                                KEY_PROMO_CODE, ""
                        ))))))
    }

    // 4
    fun eventYangLagiHitView(itemList: ArrayList<CategoryChildItem>) {

        val list = ArrayList<Map<String, Any>>()
        for (item in itemList) {
            val map = HashMap<String, String>()
            map[KEY_ID] = item.id.toString()
            map[KEY_NAME] = "banner inside kategori hits pilihan"
            map[KEY_CREATIVE] = item.id + " - " + item.name
            map[KEY_CREATIVE_URL] = item.iconImageUrl.toString()
            map[KEY_POSITION] = item.categoryPosition.toString()
            map[KEY_PROMO_ID] = ""
            map[KEY_PROMO_CODE] = ""
            list.add(map)
        }

        val ecommerce: MutableMap<String, Any>? = DataLayer.mapOf(
                "promoView", DataLayer.mapOf(
                "promotions", list))

        val map = DataLayer.mapOf(
                KEY_EVENT, EVENT_PROMO_VIEW,
                KEY_EVENT_CATEGORY, EVENT_CATEGORY_BELANJA_PAGE,
                KEY_EVENT_ACTION, "impression banner inside kategori hits pilihan",
                KEY_EVENT_LABEL, "",
                KEY_ECOMMERCE, ecommerce)
        getTracker().sendEnhanceEcommerceEvent(map)
    }

    //5

    fun eventSeringKamuLihatClick(product: CategoryChildItem, position: Int) {
        getTracker().sendEnhanceEcommerceEvent(
                DataLayer.mapOf(
                        KEY_EVENT, EVENT_PROMO_CLICK,
                        KEY_EVENT_CATEGORY, EVENT_CATEGORY_BELANJA_PAGE,
                        KEY_EVENT_ACTION, "click banner inside sering kamu lihat",
                        KEY_EVENT_LABEL, "",
                        KEY_ECOMMERCE, DataLayer.mapOf(
                        KEY_PROMO_CLICK, DataLayer.mapOf(
                        KEY_PROMOTIONS, DataLayer.listOf(
                        DataLayer.mapOf(
                                KEY_ID, product.id.toString(),
                                KEY_NAME, "banner inside sering kamu lihat",
                                KEY_CREATIVE, product.id + " - " + product.name,
                                KEY_CREATIVE_URL, product.iconImageUrl,
                                KEY_POSITION, (position).toString(),
                                KEY_PROMO_ID, "",
                                KEY_PROMO_CODE, ""
                        ))))))
    }

    // 6

    fun eventSeringkamuLihatView(itemList: ArrayList<CategoryChildItem>) {
        val list = ArrayList<Map<String, Any>>()
        for (item in itemList) {
            val map = HashMap<String, String>()
            map[KEY_ID] = item.id.toString()
            map[KEY_NAME] = "banner inside sering kamu lihat"
            map[KEY_CREATIVE] = item.id + " - " + item.name
            map[KEY_CREATIVE_URL] = item.iconImageUrl.toString()
            map[KEY_POSITION] = item.categoryPosition.toString()
            map[KEY_PROMO_ID] = ""
            map[KEY_PROMO_CODE] = ""
            list.add(map)
        }
        val ecommerce: MutableMap<String, Any>? = DataLayer.mapOf(
                "promoView", DataLayer.mapOf(
                "promotions", list))

        val map = DataLayer.mapOf(
                KEY_EVENT, EVENT_PROMO_VIEW,
                KEY_EVENT_CATEGORY, EVENT_CATEGORY_BELANJA_PAGE,
                KEY_EVENT_ACTION, "impression banner inside sering kamu lihat",
                KEY_EVENT_LABEL, "",
                KEY_ECOMMERCE, ecommerce)
        getTracker().sendEnhanceEcommerceEvent(map)
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
                KEY_EVENT, EVENT_PROMO_CLICK,
                KEY_EVENT_CATEGORY, EVENT_CATEGORY_BELANJA_PAGE,
                KEY_EVENT_ACTION, "click category on side bar",
                KEY_EVENT_LABEL, "",
                KEY_ECOMMERCE, DataLayer.mapOf(
                KEY_PROMO_CLICK, DataLayer.mapOf(
                KEY_PROMOTIONS, DataLayer.listOf(DataLayer.mapOf(
                KEY_ID, product.id.toString(),
                KEY_NAME, "category on side bar",
                KEY_CREATIVE, product.id + " - " + product.name,
                KEY_CREATIVE_URL, product.iconImageUrl,
                KEY_POSITION, (position + 1).toString(),
                KEY_PROMO_ID, "",
                KEY_PROMO_CODE, "")))
        ))
        tracker.sendEnhanceEcommerceEvent(map)
    }

    //8

    fun eventSideCategoryView( itemList: ArrayList<CategoriesItem>) {
        val tracker = getTracker()
        val list = ArrayList<Map<String, Any>>()
        for (item in itemList) {
            val map = HashMap<String, String>()
            map[KEY_ID] = item.id.toString()
            map[KEY_NAME] = "category on side bar"
            map[KEY_CREATIVE] = item.id + " - " + item.name
            map[KEY_CREATIVE_URL] = item.iconImageUrl.toString()
            map[KEY_POSITION] = item.position.toString()
            map[KEY_PROMO_ID] = ""
            map[KEY_PROMO_CODE] = ""
            list.add(map)
        }

        val ecommerce: MutableMap<String, Any>? = DataLayer.mapOf(
                "promoView", DataLayer.mapOf(
                "promotions", list))

        val map = DataLayer.mapOf(
                KEY_EVENT, EVENT_PROMO_VIEW,
                KEY_EVENT_CATEGORY, EVENT_CATEGORY_BELANJA_PAGE,
                KEY_EVENT_ACTION, "impression category on side bar",
                KEY_EVENT_LABEL, "",
                KEY_ECOMMERCE, ecommerce)
        tracker.sendEnhanceEcommerceEvent(map)

    }

    // 9

    fun eventBannerCategoryLevelOneClick(product: CategoryChildItem) {
        val tracker = getTracker()
        val map = DataLayer.mapOf(
                KEY_EVENT, EVENT_PROMO_CLICK,
                KEY_EVENT_CATEGORY, EVENT_CATEGORY_BELANJA_PAGE,
                KEY_EVENT_ACTION, "click banner category level 1",
                KEY_EVENT_LABEL, "",
                KEY_ECOMMERCE, DataLayer.mapOf(
                KEY_PROMO_CLICK, DataLayer.mapOf(
                KEY_PROMOTIONS, DataLayer.listOf(DataLayer.mapOf(
                KEY_ID, product.id.toString(),
                KEY_NAME, "banner category level 1",
                KEY_CREATIVE, product.id + " - " + product.name,
                KEY_CREATIVE_URL, product.iconImageUrl,
                KEY_PROMO_ID, "",
                KEY_PROMO_CODE, "")))
        ))
        tracker.sendEnhanceEcommerceEvent(map)
    }

    // 10  banner category level 2

    fun eventBannerCategoryLevelTwoClick(product: CategoryChildItem, position: Int) {
        val tracker = getTracker()
        val map = DataLayer.mapOf(
                KEY_EVENT, EVENT_PROMO_CLICK,
                KEY_EVENT_CATEGORY, EVENT_CATEGORY_BELANJA_PAGE,
                KEY_EVENT_ACTION, "click banner category level 2",
                KEY_EVENT_LABEL, "",
                KEY_ECOMMERCE, DataLayer.mapOf(
                KEY_PROMO_CLICK, DataLayer.mapOf(
                KEY_PROMOTIONS, DataLayer.listOf(DataLayer.mapOf(
                KEY_ID, product.id.toString(),
                KEY_NAME, "banner category level 2",
                KEY_CREATIVE, product.id + " - " + product.name,
                "category", "",
                KEY_POSITION, position,
                KEY_CREATIVE_URL, product.iconImageUrl,
                KEY_PROMO_ID, "",
                KEY_PROMO_CODE, "")))
        ))
        tracker.sendEnhanceEcommerceEvent(map)
    }

    // 11
    fun eventCategoryLevelOneBannerView(product: CategoryChildItem, position: Int) {
        val map = DataLayer.mapOf(
                KEY_EVENT, EVENT_PROMO_VIEW,
                KEY_EVENT_CATEGORY, EVENT_CATEGORY_BELANJA_PAGE,
                KEY_EVENT_ACTION, "impression banner category level 1",
                KEY_EVENT_LABEL, "",
                KEY_ECOMMERCE, DataLayer.mapOf(
                KEY_PROMO_VIEW, DataLayer.mapOf(
                KEY_PROMOTIONS, DataLayer.listOf(DataLayer.mapOf(
                KEY_ID, product.id.toString(),
                KEY_NAME, "banner category level 1",
                KEY_CREATIVE, product.id + " - " + product.name,
                KEY_CREATIVE_URL, product.iconImageUrl,
                KEY_POSITION, (position + 1).toString(),
                KEY_PROMO_ID, "",
                KEY_PROMO_CODE, ""
        )))
        ))
        getTracker().sendEnhanceEcommerceEvent(map)

    }

    // 12

    fun eventCategoryLevelTwoView( itemList: ArrayList<CategoryChildItem>) {

        val list = ArrayList<Map<String, Any>>()
        for (item in itemList) {
            val map = HashMap<String, String>()
            map[KEY_ID] = item.id.toString()
            map[KEY_NAME] = "banner category level 2"
            map[KEY_CREATIVE] = item.id + " - " + item.name
            map[KEY_CREATIVE_URL] = item.iconImageUrl.toString()
            map[KEY_POSITION] = item.categoryPosition.toString()
            map[KEY_PROMO_ID] = ""
            map[KEY_PROMO_CODE] = ""
            list.add(map)
        }

        val ecommerce: MutableMap<String, Any>? = DataLayer.mapOf(
                "promoView", DataLayer.mapOf(
                "promotions", list))

        val map = DataLayer.mapOf(
                KEY_EVENT, EVENT_PROMO_VIEW,
                KEY_EVENT_CATEGORY, EVENT_CATEGORY_BELANJA_PAGE,
                KEY_EVENT_ACTION, "impression banner category level 2",
                KEY_EVENT_LABEL, "",
                KEY_ECOMMERCE, ecommerce)
        getTracker().sendEnhanceEcommerceEvent(map)

    }


    // 13

    fun eventBackButtonClick() {
        val tracker = getTracker()
        val map = DataLayer.mapOf(
                KEY_EVENT, "clickCategory",
                KEY_EVENT_CATEGORY, EVENT_CATEGORY_BELANJA_PAGE,
                KEY_EVENT_ACTION, "click back button",
                KEY_EVENT_LABEL, ""
        )
        tracker.sendEnhanceEcommerceEvent(map)
    }


}