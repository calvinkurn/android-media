package com.tokopedia.feedplus.oldFeed.view.analytics

import com.tokopedia.analyticconstant.DataLayer
import java.util.*

/**
 * @author by astidhiyaa on 30/08/22
 */
object FeedEnhancedTracking {

    private const val EVENT = "event"
    private const val KEY_USER_ID = "userId"
    private const val KEY_USER_ID_MOD = "userIdmodulo"
    private const val ECOMMERCE = "ecommerce"

    fun getClickTracking(
        listPromotion: List<Promotion>,
        userId: Long
    ): Map<String, Any> {
        return DataLayer.mapOf(
            EVENT, Event.PROMO_CLICK,
            KEY_USER_ID, userId.toString(),
            KEY_USER_ID_MOD, (userId % 50).toString(),
            ECOMMERCE, Ecommerce.getEcommerceClick(listPromotion)
        )
    }

    object Event {
        const val PROMO_VIEW = "promoView"
        const val PROMO_CLICK = "promoClick"
    }

    object Ecommerce {
        private const val PROMOTIONS = "promotions"
        private const val KEY_ID = "id"
        private const val KEY_NAME = "name"
        private const val KEY_CREATIVE = "creative"
        private const val KEY_CREATIVE_URL = "creative_url"
        private const val KEY_POSITION = "position"
        private const val KEY_CATEGORY = "category"
        private const val KEY_PROMO_ID = "promo_id"
        private const val KEY_PROMO_CODE = "promo_code"
        fun getEcommerceView(listPromotion: List<Promotion>): Map<String, Any> {
            return DataLayer.mapOf(Event.PROMO_VIEW, getListPromotions(listPromotion))
        }

        fun getEcommerceClick(listPromotion: List<Promotion>): Map<String, Any> {
            return DataLayer.mapOf(Event.PROMO_CLICK, getListPromotions(listPromotion))
        }

        private fun getListPromotions(list: List<Promotion>): Map<String, Any> {
            return DataLayer.mapOf(PROMOTIONS, createList(list))
        }

        private fun createList(listPromotion: List<Promotion>): List<Any> {
            val list: MutableList<Map<String, Any>> = ArrayList()
            for (promo in listPromotion) {
                val map = createPromotionMap(promo)
                list.add(map)
            }
            return list
        }

        private fun createPromotionMap(promo: Promotion): Map<String, Any> {
            val map: MutableMap<String, Any> = HashMap()
            map[KEY_ID] = promo.id
            map[KEY_NAME] = promo.name
            map[KEY_CREATIVE] = promo.creative
            map[KEY_CREATIVE_URL] = promo.creativeUrl
            map[KEY_POSITION] = promo.position.toString()
            map[KEY_CATEGORY] = promo.category
            map[KEY_PROMO_ID] = promo.promoId
            map[KEY_PROMO_CODE] = promo.promoCode
            return map
        }
    }

    data class Promotion(
        val id: String = "",
        val name: String = "",
        val creative: String = "",
        val creativeUrl: String = "",
        val position: Int = 0,
        val category: String = "",
        val promoId: String = "",
        val promoCode: String = ""
    ) {
        companion object {
            private const val CONTENT_FEED = "content feed"
            private const val TOPADS = "topads"
            private const val PRODUCT = "product"
            private const val SHOP = "shop"
            const val TRACKING_NONE = "none"
            const val TRACKING_EMPTY = "-"
            fun createContentNameTopadsProduct(): String {
                return String.format(Locale.getDefault(),"/%s - %s - %s", CONTENT_FEED, TOPADS, PRODUCT)
            }

            fun createContentNameTopadsShop(): String {
                return String.format(Locale.getDefault(),"/%s - %s - %s", CONTENT_FEED, TOPADS, SHOP)
            }
        }
    }
}
