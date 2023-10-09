package com.tokopedia.logisticcart.shipping.analytics

import android.os.Bundle
import com.tokopedia.logisticCommon.data.entity.ratescourierrecommendation.RangePriceData
import com.tokopedia.logisticcart.shipping.model.LogisticPromoUiModel
import com.tokopedia.logisticcart.shipping.model.ShippingDurationUiModel
import com.tokopedia.track.TrackApp

object ShippingBottomSheetAnalytic {
    private const val EVENT_NAME = "checkout_progress"
    private const val EVENT_NAME_IMPRESSION = "viewPGIris"
    private const val EVENT_CATEGORY = "courier selection"
    private const val TRACKER_ID_KEY = "trackerId"
    private const val BUSINESS_UNIT = "physical goods"
    private const val CURRENT_SITE = "tokopediamarketplace"
    private const val ITEMS_KEY = "items"
    private const val DIMENSION_SHIPPER_PRODUCT_ID = "dimension14"
    private const val DIMENSION_CART_ID = "dimension45"
    private const val DIMENSION_PRODUCT_ID = "item_id"
    private const val DIMENSION_SHOP_ID = "shop_id"
    private const val KEY_CHECKOUT_STEP = "checkout_step"
    private const val VALUE_CHECKOUT_STEP = "3"
    private const val KEY_CHECKOUT_OPTION = "checkout_option"
    private const val VALUE_CHECKOUT_OPTION = "-"

    private const val EVENT_ACTION_IMPRESSION_BEBAS_ONGKIR =
        "impression bebas ongkir - shipping option"
    private const val EVENT_ACTION_CLICK_BEBAS_ONGKIR =
        "click bebas ongkir - shipping option"
    private const val EVENT_ACTION_CLICK_SHIPPING_OPTION =
        "click checklist shipping option"
    private const val EVENT_ACTION_VIEW_DURATION =
        "view duration"

    private const val TRACKER_ID_IMPRESSION_BEBAS_ONGKIR = "46712"
    private const val TRACKER_ID_CLICK_BEBAS_ONGKIR = "46713"
    private const val TRACKER_ID_CLICK_SHIPPING_OPTION = "46717"
    private const val TRACKER_ID_VIEW_SHIPPING_OPTION = "46723"

    private const val KEY_EVENT = "event"
    private const val KEY_EVENT_CATEGORY = "eventCategory"
    private const val KEY_EVENT_ACTION = "eventAction"
    private const val KEY_EVENT_LABEL = "eventLabel"
    private const val KEY_USER_ID = "userId"
    private const val KEY_BUSINESS_UNIT = "businessUnit"
    private const val KEY_CURRENT_SITE = "currentSite"

    private const val BACKSLASH = """\"""
    private const val STRIP = "-"

    // Tracker URL: https://mynakama.tokopedia.com/datatracker/requestdetail/view/4193
    // Tracker ID: 46712
    fun sendImpressionBebasOngkirShippingOptionEvent(
        userId: String,
        cartId: String,
        products: String,
        shopId: String,
        bebasOngkir: LogisticPromoUiModel
    ) {
        val bundle = Bundle()
        bundle.run {
            putString(KEY_EVENT, EVENT_NAME_IMPRESSION)
            putString(KEY_EVENT_ACTION, EVENT_ACTION_IMPRESSION_BEBAS_ONGKIR)
            putString(KEY_EVENT_CATEGORY, EVENT_CATEGORY)
            putString(KEY_EVENT_LABEL, bebasOngkir.eventLabel())
            putString(TRACKER_ID_KEY, TRACKER_ID_IMPRESSION_BEBAS_ONGKIR)
            putString(KEY_BUSINESS_UNIT, BUSINESS_UNIT)
            putString(KEY_CURRENT_SITE, CURRENT_SITE)
            putString(KEY_USER_ID, userId)
            putString(KEY_CHECKOUT_STEP, VALUE_CHECKOUT_STEP)
            putString(KEY_CHECKOUT_OPTION, VALUE_CHECKOUT_OPTION)
            putBundle(
                ITEMS_KEY,
                Bundle().apply {
                    putString(DIMENSION_CART_ID, cartId)
                    putInt(DIMENSION_SHIPPER_PRODUCT_ID, bebasOngkir.shipperProductId)
                    putString(DIMENSION_SHOP_ID, shopId)
                    putString(DIMENSION_PRODUCT_ID, products)
                }
            )
        }
        sendTracker(EVENT_NAME_IMPRESSION, bundle)
    }

    // Tracker URL: https://mynakama.tokopedia.com/datatracker/requestdetail/view/4193
    // Tracker ID: 46713
    fun sendClickBebasOngkirShippingOptionEvent(
        userId: String,
        cartId: String,
        products: String,
        shopId: String,
        bebasOngkir: LogisticPromoUiModel
    ) {
        val bundle = Bundle()
        bundle.run {
            putString(KEY_EVENT, EVENT_NAME)
            putString(KEY_EVENT_ACTION, EVENT_ACTION_CLICK_BEBAS_ONGKIR)
            putString(KEY_EVENT_CATEGORY, EVENT_CATEGORY)
            putString(KEY_EVENT_LABEL, bebasOngkir.eventLabel())
            putString(TRACKER_ID_KEY, TRACKER_ID_CLICK_BEBAS_ONGKIR)
            putString(KEY_BUSINESS_UNIT, BUSINESS_UNIT)
            putString(KEY_CURRENT_SITE, CURRENT_SITE)
            putString(KEY_CHECKOUT_STEP, VALUE_CHECKOUT_STEP)
            putString(KEY_CHECKOUT_OPTION, VALUE_CHECKOUT_OPTION)
            putString(KEY_USER_ID, userId)
            putBundle(
                ITEMS_KEY,
                Bundle().apply {
                    putString(DIMENSION_CART_ID, cartId)
                    putInt(DIMENSION_SHIPPER_PRODUCT_ID, bebasOngkir.shipperProductId)
                    putString(DIMENSION_SHOP_ID, shopId)
                    putString(DIMENSION_PRODUCT_ID, products)
                }
            )
        }
        sendTracker(EVENT_NAME, bundle)
    }

    // Tracker URL: https://mynakama.tokopedia.com/datatracker/requestdetail/view/4193
// Tracker ID: 46717
    fun sendClickChecklistShippingOptionEvent(
        userId: String,
        cartId: String,
        shippingService: ShippingDurationUiModel,
        products: String,
        shopId: String
    ) {
        val bundle = Bundle()
        bundle.run {
            putString(KEY_EVENT, EVENT_NAME)
            putString(KEY_EVENT_ACTION, EVENT_ACTION_CLICK_SHIPPING_OPTION)
            putString(KEY_EVENT_CATEGORY, EVENT_CATEGORY)
            putString(KEY_EVENT_LABEL, shippingService.eventLabel())
            putString(TRACKER_ID_KEY, TRACKER_ID_CLICK_SHIPPING_OPTION)
            putString(KEY_BUSINESS_UNIT, BUSINESS_UNIT)
            putString(KEY_CURRENT_SITE, CURRENT_SITE)
            putString(KEY_CHECKOUT_STEP, VALUE_CHECKOUT_STEP)
            putString(KEY_CHECKOUT_OPTION, VALUE_CHECKOUT_OPTION)
            putString(KEY_USER_ID, userId)
            putBundle(
                ITEMS_KEY,
                Bundle().apply {
                    putString(DIMENSION_CART_ID, cartId)
                    putString(DIMENSION_SHOP_ID, shopId)
                    putString(DIMENSION_PRODUCT_ID, products)
                }
            )
        }
        sendTracker(EVENT_NAME, bundle)
    }

    // Tracker URL: https://mynakama.tokopedia.com/datatracker/requestdetail/view/4193
    // Tracker ID: 46723
    fun sendViewDurationEvent(
        userId: String,
        cartId: String,
        products: String,
        shopId: String,
        shippingService: ShippingDurationUiModel
    ) {
        val bundle = Bundle()
        bundle.run {
            putString(KEY_EVENT, EVENT_NAME_IMPRESSION)
            putString(KEY_EVENT_ACTION, EVENT_ACTION_VIEW_DURATION)
            putString(KEY_EVENT_CATEGORY, EVENT_CATEGORY)
            putString(KEY_EVENT_LABEL, shippingService.eventLabel())
            putString(TRACKER_ID_KEY, TRACKER_ID_VIEW_SHIPPING_OPTION)
            putString(KEY_BUSINESS_UNIT, BUSINESS_UNIT)
            putString(KEY_CURRENT_SITE, CURRENT_SITE)
            putString(KEY_CHECKOUT_STEP, VALUE_CHECKOUT_STEP)
            putString(KEY_CHECKOUT_OPTION, VALUE_CHECKOUT_OPTION)
            putString(KEY_USER_ID, userId)
            putBundle(
                ITEMS_KEY,
                Bundle().apply {
                    putString(DIMENSION_CART_ID, cartId)
                    putString(DIMENSION_SHOP_ID, shopId)
                    putString(DIMENSION_PRODUCT_ID, products)
                }
            )
        }
        sendTracker(EVENT_NAME_IMPRESSION, bundle)
    }

    private fun sendTracker(eventName: String, dataMap: Bundle) {
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(eventName, dataMap)
    }

    private fun LogisticPromoUiModel.eventLabel(): String {
        val promo = this
        return mutableListOf<String>().apply {
            add("${!promo.disabled}")
            add(promo.etaData.textEta)
            add(promo.discountedRate.toString())
        }.joinToString(STRIP)
    }

    private fun ShippingDurationUiModel.eventLabel(): String {
        val shipping = this
        return mutableListOf<String>().apply {
            add(shipping.serviceData.serviceName.ifEmpty { " " })
            add(shipping.serviceData.texts.textEtaSummarize.ifEmpty { "Estimasi tidak tersedia" })
            add(shipping.serviceData.rangePrice.toAnalyticLabel())
            add(shipping.message())
        }.joinToString(STRIP)
    }

    private fun ShippingDurationUiModel.message(): String {
        val duration = this
        return mutableListOf<String>().apply {
            appendWithDefault(duration.errorMessage)
            appendWithDefault(duration.serviceData.texts.textServiceDesc)
            appendWithDefault(duration.serviceData.orderPriority.staticMessage.durationMessage)
            appendWithDefault(
                duration.merchantVoucherModel.mvcErrorMessage,
                duration.merchantVoucherModel.isMvc == -1
            )
            appendWithDefault(duration.codText, duration.isCodAvailable)
            appendWithDefault(duration.dynamicPriceModel.textLabel)
        }.joinToString(BACKSLASH)
    }

    private fun MutableList<String>.appendWithDefault(text: String?, condition: Boolean = true) {
        if (text?.isNotEmpty() == true && condition) {
            add(text)
        } else {
            add(STRIP)
        }
    }

    private fun RangePriceData.toAnalyticLabel(): String {
        return "$minPrice to $maxPrice"
    }
}
