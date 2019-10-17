package com.tokopedia.common.travel.utils

import com.google.android.gms.tagmanager.DataLayer
import com.tokopedia.common.travel.data.entity.TravelCrossSelling
import com.tokopedia.track.TrackApp
import com.tokopedia.track.TrackAppUtils.*

/**
 * @author by jessica on 2019-10-16
 */

class TrackingCrossSellUtil {

    fun crossSellImpression(crossSellingItems: List<TravelCrossSelling.Item>, firstSeenItemPosition: Int,
                            lastSeenItemPosition: Int) {
        val map = mutableMapOf<String, Any?>()
        map[EVENT] = PROMO_VIEW
        map[EVENT_CATEGORY] = CROSS_SELL_WIDGET_NATIVE
        map[EVENT_ACTION] = WIDGET_IMPRESSION
        map[EVENT_LABEL] = LENGKAPI_PERJALANANMU_LABEL
        map[ECOMMERCE_LABEL] = DataLayer.mapOf(
                PROMO_VIEW, DataLayer.mapOf(
                PROMOTIONS_LABEL, getPromoList(crossSellingItems, firstSeenItemPosition, lastSeenItemPosition)
        )
        )
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(map)

    }

    fun crossSellClick(crossSellItems: List<TravelCrossSelling.Item>, position: Int) {
        val map = mutableMapOf<String, Any?>()
        map[EVENT] = PROMO_CLICK
        map[EVENT_CATEGORY] = CROSS_SELL_WIDGET_NATIVE
        map[EVENT_ACTION] = WIDGET_CLICK
        map[EVENT_LABEL] = String.format("%s - %s", position + 1, crossSellItems.get(position).product)
        map[ECOMMERCE_LABEL] = DataLayer.mapOf(
                PROMO_VIEW, DataLayer.mapOf(
                PROMOTIONS_LABEL, getPromoList(crossSellItems, position, position)
        )
        )
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(map)
    }

    fun getPromoList(crossSellingItems: List<TravelCrossSelling.Item>, firstSeenItemPosition: Int, lastSeenItemPosition: Int): List<Any> {
        val list = ArrayList<Map<String, Any>>()

        for (position in firstSeenItemPosition..lastSeenItemPosition) {
            val map = HashMap<String, Any>()
            val item = crossSellingItems.get(position)
            map[ID_LABEL] = position + 1
            map[NAME_LABEL] = item.product
            map[CREATIVE_LABEL] = item.product
            map[POSITION_LABEL] = position + 1
            list.add(map)
        }
        return DataLayer.listOf(*list.toTypedArray<Any>())
    }

    companion object {
        val ECOMMERCE_LABEL = "ecommerce"
        val PROMO_VIEW = "promoView"
        val PROMO_CLICK = "promoClick"
        val CROSS_SELL_WIDGET_NATIVE = "digital - cross sell widget"
        val WIDGET_IMPRESSION = "view widget"
        val WIDGET_CLICK = "click widget"
        val LENGKAPI_PERJALANANMU_LABEL = "view lengkapi perjalananu"
        val PROMOTIONS_LABEL = "promotions"

        val ID_LABEL = "id"
        val NAME_LABEL = "name"
        val CREATIVE_LABEL = "creative"
        val CREATIVE_URL_LABEL = "creative_url"
        val POSITION_LABEL = "position"
        val CATEGORY_LABEL = "category"
        val PROMO_ID_LABEL = "promo_id"
        val PROMO_CODE_LABEL = "promo_code"
    }
}