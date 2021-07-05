package com.tokopedia.atc_common.domain.analytics

import android.os.Bundle
import com.tokopedia.atc_common.domain.model.response.atcexternal.AddToCartExternalDataModel
import com.tokopedia.track.TrackApp
import com.tokopedia.track.TrackAppUtils
import javax.inject.Inject

class AddToCartExternalAnalytics @Inject constructor() {

    companion object {
        // Enhanced Ecommerce constants
        const val EE_PARAM_ITEM_ID = "item_id"
        const val EE_PARAM_ITEM_NAME = "item_name"
        const val EE_PARAM_ITEM_BRAND = "item_brand"
        const val EE_PARAM_ITEM_CATEGORY = "item_category"
        const val EE_PARAM_ITEM_VARIANT = "item_variant"
        const val EE_PARAM_SHOP_ID = "shop_id"
        const val EE_PARAM_SHOP_NAME = "shop_name"
        const val EE_PARAM_SHOP_TYPE = "shop_type"
        const val EE_PARAM_CATEGORY_ID = "category_id"
        const val EE_PARAM_QUANTITY = "quantity"
        const val EE_PARAM_PRICE = "price"
        const val EE_PARAM_PICTURE = "picture"
        const val EE_PARAM_URL = "url"
        const val EE_PARAM_DIMENSION_38 = "dimension38"
        const val EE_PARAM_DIMENSION_45 = "dimension45"
        const val EE_PARAM_DIMENSION_54 = "dimension54"
        const val EE_PARAM_DIMENSION_83 = "dimension83"

        const val EE_VALUE_EVENT_NAME = "add_to_cart"
        const val EE_VALUE_EVENT_CATEGORY = "cart"
        const val EE_VALUE_EVENT_ACTION = "click add to cart"

        const val EE_VALUE_ITEMS = "items"
        const val EE_VALUE_TOKOPEDIA = "tokopedia"
        const val EE_VALUE_REGULAR = "regular"
        const val EE_VALUE_BEBAS_ONGKIR = "bebas ongkir"
        const val EE_VALUE_BEBAS_ONGKIR_EXTRA = "bebas ongkir ekstra"
        const val EE_VALUE_NONE_OTHER = "none/other"
    }

    fun sendEnhancedEcommerceTracking(data: AddToCartExternalDataModel) {
        val itemBundle = Bundle().apply {
            putString(EE_PARAM_ITEM_ID, setValueOrDefault(data.productId.toString()))
            putString(EE_PARAM_ITEM_NAME, setValueOrDefault(data.productName))
            putString(EE_PARAM_ITEM_BRAND, setValueOrDefault(data.brand))
            putString(EE_PARAM_ITEM_CATEGORY, setValueOrDefault(data.category))
            putString(EE_PARAM_ITEM_VARIANT, setValueOrDefault(data.variant))
            putString(EE_PARAM_SHOP_ID, setValueOrDefault(data.shopId.toString()))
            putString(EE_PARAM_SHOP_NAME, setValueOrDefault(data.shopName))
            putString(EE_PARAM_SHOP_TYPE, setValueOrDefault(data.shopType))
            putString(EE_PARAM_CATEGORY_ID, setValueOrDefault(data.categoryId))
            putInt(EE_PARAM_QUANTITY, data.quantity.coerceAtLeast(1))
            putInt(EE_PARAM_PRICE, data.price)
            putString(EE_PARAM_PICTURE, data.picture)
            putString(EE_PARAM_URL, data.url)
            putString(EE_PARAM_DIMENSION_38, setValueOrDefault(data.trackerAttribution))
            putString(EE_PARAM_DIMENSION_45, setValueOrDefault(data.cartId.toString()))
            putString(EE_PARAM_DIMENSION_54, if (data.isMultiOrigin) EE_VALUE_TOKOPEDIA else EE_VALUE_REGULAR)
            putString(EE_PARAM_DIMENSION_83, when {
                data.isFreeOngkirExtra -> EE_VALUE_BEBAS_ONGKIR_EXTRA
                data.isFreeOngkir -> EE_VALUE_BEBAS_ONGKIR
                else -> EE_VALUE_NONE_OTHER
            })
        }

        val eventDataLayer = Bundle().apply {
            putString(TrackAppUtils.EVENT, EE_VALUE_EVENT_NAME)
            putString(TrackAppUtils.EVENT_CATEGORY, EE_VALUE_EVENT_CATEGORY)
            putString(TrackAppUtils.EVENT_ACTION, EE_VALUE_EVENT_ACTION)
            putString(TrackAppUtils.EVENT_LABEL, "")
            putParcelableArrayList(EE_VALUE_ITEMS, arrayListOf(itemBundle))
        }

        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(EE_VALUE_EVENT_NAME, eventDataLayer)
    }

    private fun setValueOrDefault(value: String): String {
        if (value.isBlank()) {
            return EE_VALUE_NONE_OTHER
        }
        return value
    }
}