package com.tokopedia.home.analytics.v2

import com.google.android.gms.tagmanager.DataLayer

abstract class BaseTracking {
    protected abstract class Event{

        val NONE = ""
        val KEY = "event"
        val CLICK = "click"
        val IMPRESSION = "impression"
        val PROMO_VIEW = "promoView"
        val PROMO_CLICK = "promoClick"
        val PROMO_VIEW_IRIS = "promoViewIris"
    }
    protected  object Category{
        val KEY = "eventCategory"
        val HOMEPAGE = "homepage"
    }

    protected  object Action{
        const val KEY = "eventAction"
        const val IMPRESSION = "%s impression"
        const val CLICK = "%s click"
    }

    protected  object Label{
        const val KEY = "eventLabel"
        const val CHANNEL_LABEL = "channelId"
        const val AFFINITY_LABEL = "affinityLabel"
        const val ATTRIBUTION_LABEL = "attribution"
        const val CATEGORY_LABEL = "categoryId"
        const val SHOP_LABEL = "shopId"
        const val NONE = ""
    }

    protected  object Ecommerce {
        const val KEY = "ecommerce"
        const val PROMOTION_NAME = "/ - p%s - %s - %s"
        private const val PROMO_VIEW = "promoView"
        private const val PROMO_CLICK = "promoClick"
        private const val PROMOTIONS = "promotions"
        private const val KEY_ID = "id"
        private const val KEY_NAME = "name"
        private const val KEY_CREATIVE = "creative"
        private const val KEY_CREATIVE_URL = "creative_url"
        private const val KEY_POSITION = "position"


        fun getEcommercePromoView(promotions: List<Promotion>): Map<String, Any> {
            return DataLayer.mapOf(
                    PROMO_VIEW, DataLayer.listOf(
                    PROMOTIONS, getPromotions(promotions)
            )
            )
        }

        fun getEcommercePromoClick(promotions: List<Promotion>): Map<String, Any> {
            return DataLayer.mapOf(
                    PROMO_CLICK, DataLayer.listOf(
                    PROMOTIONS, getPromotions(promotions)
            )
            )
        }

        private fun getPromotions(promotions: List<Promotion>): List<Any>{
            val list = ArrayList<Map<String,Any>>()
            promotions.forEach { list.add(createPromotionMap(it)) }
            return DataLayer.listOf(*list.toTypedArray<Any>())
        }

        private fun createPromotionMap(promotion: Promotion) : Map<String, Any>{
            val map = HashMap<String, Any>()
            map[KEY_ID] = promotion.id
            map[KEY_NAME] = promotion.name
            map[KEY_CREATIVE] = promotion.creative
            map[KEY_CREATIVE_URL] = promotion.creativeUrl
            map[KEY_POSITION] = promotion.position
            return map
        }
    }

    class Promotion(val id: String, val name: String, val creative: String, val creativeUrl: String, val position: String)
}