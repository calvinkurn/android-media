package com.tokopedia.smartbills.analytics

import com.tokopedia.analyticconstant.DataLayer
import com.tokopedia.smartbills.analytics.SmartBillsAnalyticConstants.*
import com.tokopedia.smartbills.data.RechargeBills
import com.tokopedia.track.TrackApp
import com.tokopedia.track.TrackAppUtils

/**
 * @author by resakemal on 17/05/20
 */

class SmartBillsAnalytics {

    var userId: String = ""

    fun clickSubscription() {
        val map = TrackAppUtils.gtmData(
                Event.SMART_BILLS,
                CATEGORY_SMART_BILLS,
                Action.CLICK_LANGGANAN,
                Label.LANGGANAN)
        map[Key.USER_ID] = userId
        map[Key.SCREEN_NAME] = SCREEN_NAME_INITAL
        map.putAll(ADDITIONAL_INFO_MAP)
        TrackApp.getInstance().gtm.sendGeneralEvent(map)
    }

    fun clickAllBills() {
        val map = TrackAppUtils.gtmData(
                Event.SMART_BILLS,
                CATEGORY_SMART_BILLS,
                Action.CLICK_ALL_TAGIHAN,
                Label.ALL_TAGIHAN)
        map[Key.USER_ID] = userId
        map[Key.SCREEN_NAME] = SCREEN_NAME_INITAL
        map.putAll(ADDITIONAL_INFO_MAP)
        TrackApp.getInstance().gtm.sendGeneralEvent(map)
    }

    fun clickTickBill(tickedBill: RechargeBills, selectedBills: List<RechargeBills>) {
        val actionField = mapOf(EnhanceEccomerce.LIST to LIST_VALUE)
        val ecommerce = with (EnhanceEccomerce) {
            val products = createEcommerceProducts(selectedBills)
            products.mapIndexed { index, item ->
                val map = item.toMutableMap()
                map[POSITION] = index
                return@mapIndexed map
            }
            DataLayer.mapOf(ECOMMERCE, DataLayer.mapOf(CLICK, DataLayer.mapOf(
                    ACTION_FIELD, actionField,
                    PRODUCTS, DataLayer.listOf(products)
            )))
        }

        val map = TrackAppUtils.gtmData(
                Event.SMART_BILLS,
                CATEGORY_SMART_BILLS,
                Action.CLICK_TICK_BILL,
                "${tickedBill.categoryID} - ${tickedBill.productID}")
        map[Key.USER_ID] = userId
        map[Key.SCREEN_NAME] = SCREEN_NAME_INITAL
        map.putAll(ADDITIONAL_INFO_MAP)
        map.putAll(ecommerce)
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(map)
    }

    fun clickUntickBill(untickedBill: RechargeBills) {
        val map = TrackAppUtils.gtmData(
                Event.SMART_BILLS,
                CATEGORY_SMART_BILLS,
                Action.CLICK_UNTICK_BILL,
                "${untickedBill.categoryID} - ${untickedBill.productID}")
        map[Key.USER_ID] = userId
        map[Key.SCREEN_NAME] = SCREEN_NAME_INITAL
        map.putAll(ADDITIONAL_INFO_MAP)
        TrackApp.getInstance().gtm.sendGeneralEvent(map)
    }

    fun impressionAllProducts(bills: List<RechargeBills>) {
        val ecommerce = with (EnhanceEccomerce) {
            val impressions = createEcommerceProducts(bills)
            DataLayer.mapOf(ECOMMERCE, DataLayer.mapOf(
                    CURRENCY_CODE, CURRENCY_CODE_VALUE,
                    IMPRESSIONS, DataLayer.listOf(impressions)
            ))
        }

        val map = TrackAppUtils.gtmData(
                Event.PRODUCT_VIEW,
                CATEGORY_SMART_BILLS,
                Action.IMPRESSION_ALL_PRODUCT,
                "")
        map[Key.USER_ID] = userId
        map[Key.SCREEN_NAME] = SCREEN_NAME_INITAL
        map.putAll(ADDITIONAL_INFO_MAP)
        map.putAll(ecommerce)
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(map)
    }

    fun clickPay(selectedBills: List<RechargeBills>) {
        val actionField = mapOf(EnhanceEccomerce.STEP to STEP_VALUE, EnhanceEccomerce.OPTION to OPTION_VALUE)
        val ecommerce = with (EnhanceEccomerce) {
            val products = createEcommerceProducts(selectedBills)
            products.map {
                val map = it.toMutableMap()
                map[QUANTITY] = 1
                return@map map
            }
            DataLayer.mapOf(ECOMMERCE, DataLayer.mapOf(CLICK, DataLayer.mapOf(
                    ACTION_FIELD, actionField,
                    PRODUCTS, DataLayer.listOf(products)
            )))
        }

        val map = TrackAppUtils.gtmData(
                Event.SMART_BILLS,
                CATEGORY_SMART_BILLS,
                Action.CLICK_BAYAR,
                Label.BAYAR)
        map[Key.USER_ID] = userId
        map[Key.SCREEN_NAME] = SCREEN_NAME_INITAL
        map.putAll(ADDITIONAL_INFO_MAP)
        map.putAll(ecommerce)
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(map)
    }

    fun clickPayFailed() {
        val map = TrackAppUtils.gtmData(
                Event.SMART_BILLS,
                CATEGORY_SMART_BILLS,
                Action.CLICK_BAYAR,
                Label.GAGAL)
        map[Key.USER_ID] = userId
        map[Key.SCREEN_NAME] = SCREEN_NAME_DETAIL
        map.putAll(ADDITIONAL_INFO_MAP)
        TrackApp.getInstance().gtm.sendGeneralEvent(map)
    }

    fun clickBillDetail(bill: RechargeBills) {
        val map = TrackAppUtils.gtmData(
                Event.SMART_BILLS,
                CATEGORY_SMART_BILLS,
                Action.CLICK_DETAIL,
                "${bill.categoryID} - ${bill.productID}")
        map[Key.USER_ID] = userId
        map[Key.SCREEN_NAME] = SCREEN_NAME_DETAIL
        map.putAll(ADDITIONAL_INFO_MAP)
        TrackApp.getInstance().gtm.sendGeneralEvent(map)
    }

    private fun createEcommerceProducts(bills: List<RechargeBills>): List<Map<String, Any>> {
        with (EnhanceEccomerce) {
            return bills.map {
                DataLayer.mapOf(
                        NAME, it.productName,
                        ID, it.productID,
                        PRICE, it.amount,
                        BRAND, NONE,
                        CATEGORY, it.categoryName,
                        VARIANT, NONE
                )
            }
        }
    }

    companion object {

        const val CATEGORY_SMART_BILLS = "digital - smart bill management"

        const val SCREEN_NAME_INITAL = "/initial-sbm-page"
        const val SCREEN_NAME_DETAIL = "/detail-sbm-page"
        const val CURRENT_SITE_VALUE = "tokopediaDigitalRecharge"
        const val BUSINESS_UNIT_VALUE = "top up and tagihan"
        const val CURRENCY_CODE_VALUE = "IDR"
        const val LIST_VALUE = "/smartbill"
        const val STEP_VALUE = "1"
        const val OPTION_VALUE = "click bayar"

        val ADDITIONAL_INFO_MAP = mapOf(
                Key.CURRENT_SITE to CURRENT_SITE_VALUE,
                Key.BUSINESS_UNIT to BUSINESS_UNIT_VALUE
        )
    }
}