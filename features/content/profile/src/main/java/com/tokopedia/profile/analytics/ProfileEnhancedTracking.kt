package com.tokopedia.profile.analytics

import com.google.android.gms.tagmanager.DataLayer
import com.tokopedia.profile.analytics.ProfileAnalytics.Companion.PROMO_CLICK
import com.tokopedia.profile.analytics.ProfileAnalytics.Companion.PROMO_VIEW
import java.util.*


/**
 * @author by nisie on 1/2/18.
 */

class ProfileEnhancedTracking {

    object Ecommerce {
        private const val PROMOTIONS = "promotions"
        private const val KEY_ID = "id"
        private const val KEY_NAME = "name"
        private const val KEY_CREATIVE = "creative"
        private const val KEY_POSITION = "position"
        private const val KEY_CATEGORY = "category"
        private const val KEY_PROMO_ID = "promo_id"
        private const val KEY_PROMO_CODE = "promo_code"


        fun getEcommerceView(listPromotion: List<Promotion>): Map<String, Any> {
            return DataLayer.mapOf(PROMO_VIEW, getListPromotions(listPromotion))
        }

        fun getEcommerceClick(listPromotion: List<Promotion>): Map<String, Any> {
            return DataLayer.mapOf(PROMO_CLICK, getListPromotions(listPromotion))

        }

        private fun getListPromotions(list: List<Promotion>): Map<String, Any> {
            return DataLayer.mapOf(PROMOTIONS, createList(list))
        }

        private fun createList(listPromotion: List<Promotion>): List<Any> {
            val list = ArrayList<Map<String, Any>>()
            for (promo in listPromotion) {
                val map = createPromotionMap(promo)
                list.add(map)
            }
            return DataLayer.listOf(*list.toTypedArray<Any>())
        }

        private fun createPromotionMap(promo: Promotion): Map<String, Any> {
            val map = HashMap<String, Any>()
            map[KEY_ID] = promo.id.toString()
            map[KEY_NAME] = promo.name
            map[KEY_CREATIVE] = promo.creative
            map[KEY_POSITION] = promo.position.toString()
            map[KEY_CATEGORY] = promo.category
            map[KEY_PROMO_ID] = promo.promoId.toString()
            map[KEY_PROMO_CODE] = promo.promoCode
            return map
        }

    }

    class Promotion(id: Int, name: String, creative: String, position: Int,
                    category: String, promoId: Int, promoCode: String) {
        var id: Int = 0
            internal set
        var name: String
            internal set
        var creative: String
            internal set
        var position: Int = 0
            internal set
        var category: String
            internal set
        var promoId: Int = 0
            internal set
        var promoCode: String
            internal set

        init {
            this.id = id
            this.name = name
            this.creative = creative
            this.position = position
            this.category = category
            this.promoId = promoId
            this.promoCode = promoCode
        }

    }
}
