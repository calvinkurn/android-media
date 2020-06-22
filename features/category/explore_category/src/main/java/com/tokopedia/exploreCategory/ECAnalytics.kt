package com.tokopedia.exploreCategory

import com.tokopedia.analyticconstant.DataLayer
import com.tokopedia.exploreCategory.model.ECDynamicHomeIconData.DynamicHomeIcon.CategoryGroup
import com.tokopedia.track.TrackApp
import com.tokopedia.trackingoptimizer.TrackingQueue
import java.util.*
import kotlin.collections.ArrayList

class ECAnalytics {
    companion object {
        private const val EVENT_CLICK = "promoClick"
        private const val EVENT_CLICK_CATEGORY = "clickCategory"
        private const val EVENT_VIEW = "promoView"
        private const val EVENT_CATEGORY = "semua kategori page"
        private const val EVENT_ACTION_CLICK_BACK = "click back button"
        private const val EVENT_ACTION_CLICK_ACCORDION = "click accordion"
        private const val PROMO_SLOT_NAME = "promo slot name"

        private const val KEY_EVENT = "event"
        private const val KEY_EVENT_CATEGORY = "eventCategory"
        private const val KEY_EVENT_ACTION = "eventAction"
        private const val KEY_EVENT_LABEL = "eventLabel"
        private const val KEY_ECOMMERCE = "ecommerce"

        private const val NONE_OTHER = "none/other"

        //1
        @JvmStatic
        fun trackEventClickFeature(categoryGroup: CategoryGroup?, position: Int) {
            categoryGroup?.let {
                val tracker = TrackApp.getInstance().gtm
                val ecommerce: MutableMap<String, Any>? = DataLayer.mapOf(
                        "promoClick", DataLayer.mapOf(
                        "promotions", DataLayer.listOf(DataLayer.mapOf(
                        "id", it.id.toString(),
                        "name", it.title?.toLowerCase(Locale.getDefault()),
                        "creative", it.title?.toLowerCase(Locale.getDefault()),
                        "creative_url", NONE_OTHER,
                        "position", position + 1,
                        "category", NONE_OTHER,
                        "promo_id", NONE_OTHER,
                        "promo_code", NONE_OTHER))))
                val map = DataLayer.mapOf(
                        KEY_EVENT, EVENT_CLICK,
                        KEY_EVENT_CATEGORY, EVENT_CATEGORY,
                        KEY_EVENT_ACTION, "click category - ${it.title?.toLowerCase(Locale.getDefault())}",
                        KEY_EVENT_LABEL, it.id,
                        KEY_ECOMMERCE, ecommerce)
                tracker.sendEnhanceEcommerceEvent(map)
            }
        }

        //2
        @JvmStatic
        fun trackEventImpressionFeature(categoryGroups: List<CategoryGroup?>, defaultCategoryName: String?, defaultCategoryId: Int?) {
            val tracker = TrackApp.getInstance().gtm
            val list = ArrayList<Map<String, Any>>()
            for (categoryGroup in categoryGroups) {
                val map = HashMap<String, Any>()
                categoryGroup?.let {
                    map["id"] = it.id.toString()
                    map["name"] = defaultCategoryName?.toLowerCase(Locale.getDefault()) ?: NONE_OTHER
                    map["creative"] = it.title?.toLowerCase(Locale.getDefault())
                            ?: NONE_OTHER
                    map["creative_url"] = NONE_OTHER
                    map["position"] = categoryGroups.indexOf(it) + 1
                    map["category"] = NONE_OTHER
                    map["promo_id"] = NONE_OTHER
                    map["promo_code"] = NONE_OTHER
                }
                list.add(map)
            }

            val ecommerce: MutableMap<String, Any>? = DataLayer.mapOf(
                    "promoView", DataLayer.mapOf(
                    "promotions", list))

            val map = DataLayer.mapOf(
                    KEY_EVENT, EVENT_VIEW,
                    KEY_EVENT_CATEGORY, EVENT_CATEGORY,
                    KEY_EVENT_ACTION, "impression - ${defaultCategoryName?.toLowerCase(Locale.getDefault())}",
                    KEY_EVENT_LABEL, defaultCategoryId,
                    KEY_ECOMMERCE, ecommerce)
            tracker.sendEnhanceEcommerceEvent(map)
        }

        //3
        fun trackEventClickAccordion(tabName: String?) {
            val tracker = TrackApp.getInstance().gtm
            val map = DataLayer.mapOf(
                    KEY_EVENT, EVENT_CLICK_CATEGORY,
                    KEY_EVENT_CATEGORY, EVENT_CATEGORY,
                    KEY_EVENT_ACTION, EVENT_ACTION_CLICK_ACCORDION,
                    KEY_EVENT_LABEL, tabName?.toLowerCase(Locale.getDefault()) ?: "")
            tracker.sendEnhanceEcommerceEvent(map)
        }

        //4
        @JvmStatic
        fun trackEventClickIcon(categoryTitle: String?, categoryId: String?, icon: CategoryGroup.CategoryRow?, position: Int) {
            val tracker = TrackApp.getInstance().gtm
            val ecommerce: MutableMap<String, Any>? = icon?.let {
                DataLayer.mapOf(
                        "promoClick", DataLayer.mapOf(
                        "promotions", DataLayer.listOf(DataLayer.mapOf(
                        "id", it.id.toString(),
                        "name", categoryTitle?.toLowerCase(Locale.getDefault()),
                        "creative", it.name?.toLowerCase(Locale.getDefault()),
                        "creative_url", NONE_OTHER,
                        "position", position + 1,
                        "category", NONE_OTHER,
                        "promo_id", NONE_OTHER,
                        "promo_code", NONE_OTHER))))
            }
            val map = DataLayer.mapOf(
                    KEY_EVENT, EVENT_CLICK,
                    KEY_EVENT_CATEGORY, EVENT_CATEGORY,
                    KEY_EVENT_ACTION, "click category - ${categoryTitle?.toLowerCase(Locale.getDefault())}",
                    KEY_EVENT_LABEL, categoryId,
                    KEY_ECOMMERCE, ecommerce)
            tracker.sendEnhanceEcommerceEvent(map)
        }

        //5
        @JvmStatic
        fun trackEventImpressionIcon(categoryGroup: CategoryGroup?, trackingQueue: TrackingQueue) {
            if (categoryGroup?.categoryRows?.isNullOrEmpty() == false) {
                val list = ArrayList<Map<String, Any>>()
                for (icon in categoryGroup.categoryRows) {
                    val map = HashMap<String, Any>()
                    icon?.let {
                        map["id"] = it.id.toString()
                        map["name"] = categoryGroup.title?.toLowerCase(Locale.getDefault()) ?: NONE_OTHER
                        map["creative"] = it.name?.toLowerCase(Locale.getDefault())
                                ?: NONE_OTHER
                        map["creative_url"] = it.imageUrl ?: NONE_OTHER
                        map["position"] = categoryGroup.categoryRows.indexOf(it) + 1
                        map["category"] = NONE_OTHER
                        map["promo_id"] = NONE_OTHER
                        map["promo_code"] = NONE_OTHER
                    }
                    list.add(map)
                }

                val ecommerce: MutableMap<String, Any>? = DataLayer.mapOf(
                        "promoView", DataLayer.mapOf(
                        "promotions", list))

                val map = DataLayer.mapOf(
                        KEY_EVENT, EVENT_VIEW,
                        KEY_EVENT_CATEGORY, EVENT_CATEGORY,
                        KEY_EVENT_ACTION, "impression - ${categoryGroup.title?.toLowerCase(Locale.getDefault())}",
                        KEY_EVENT_LABEL, categoryGroup.id,
                        KEY_ECOMMERCE, ecommerce)
                trackingQueue.putEETracking(map as HashMap<String, Any>)
            }
        }

        //6
        @JvmStatic
        fun trackEventClickBack() {
            val tracker = TrackApp.getInstance().gtm
            val map = DataLayer.mapOf(
                    KEY_EVENT, EVENT_CLICK_CATEGORY,
                    KEY_EVENT_CATEGORY, EVENT_CATEGORY,
                    KEY_EVENT_ACTION, EVENT_ACTION_CLICK_BACK,
                    KEY_EVENT_LABEL, "")
            tracker.sendEnhanceEcommerceEvent(map)
        }
    }
}