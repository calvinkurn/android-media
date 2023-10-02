package com.tokopedia.smartbills.analytics

import android.os.Bundle
import com.tokopedia.analyticconstant.DataLayer
import com.tokopedia.common.topupbills.analytics.CommonSmartBillsConstant.addGeneralClick
import com.tokopedia.common.topupbills.analytics.CommonSmartBillsConstant.addGeneralView
import com.tokopedia.common.topupbills.analytics.CommonSmartBillsConstant.addGeneralViewAddBills
import com.tokopedia.smartbills.analytics.SmartBillsAnalyticConstants.*
import com.tokopedia.smartbills.analytics.SmartBillsAnalyticConstants.EnhanceEccomerce.Companion.NONE
import com.tokopedia.smartbills.analytics.SmartBillsAnalyticConstants.Event.Companion.EVENT_VALUE_CHECKOUT_PROGRESS
import com.tokopedia.smartbills.analytics.SmartBillsAnalyticConstants.Key.Companion.ITEMS
import com.tokopedia.smartbills.analytics.SmartBillsAnalyticConstants.Key.Companion.TRACKER_ID
import com.tokopedia.smartbills.analytics.SmartBillsAnalyticConstants.Action.Companion.CLICK_ON_HIGHLIGHT_CATEGORY
import com.tokopedia.smartbills.analytics.SmartBillsAnalyticConstants.Action.Companion.CLICK_X_ON_HIGHLIGHT_CATEGORY
import com.tokopedia.smartbills.analytics.SmartBillsAnalyticConstants.Action.Companion.VIEW_ON_HIGHLIGHT_CATEGORY
import com.tokopedia.common.topupbills.analytics.CommonSmartBillsConstant
import com.tokopedia.common.topupbills.analytics.CommonSmartBillsConstant.addGeneralDigitalClick
import com.tokopedia.common.topupbills.analytics.CommonSmartBillsConstant.addGeneralDigitalView
import com.tokopedia.smartbills.data.RechargeBills
import com.tokopedia.track.TrackApp
import com.tokopedia.track.TrackAppUtils

/**
 * @author by resakemal on 17/05/20
 */

class SmartBillsAnalytics {

    var userId: String = ""

    fun eventOpenScreen(isSBMEmpty: Boolean, totalProduct: Int) {
        val loginStatus = if (userId.isEmpty()) "false" else "true"
        val mapOpenScreen = mutableMapOf(
            Key.IS_LOGIN_STATUS to loginStatus,
            Key.USER_ID to userId,
            Key.PRODUCT_STATUS to "$isSBMEmpty - $totalProduct"
        )
        mapOpenScreen.putAll(ADDITIONAL_INFO_MAP)

        TrackApp.getInstance().gtm.sendScreenAuthenticated(SCREEN_NAME_INITAL, mapOpenScreen)
    }

    fun clickSubscription() {
        val map = TrackAppUtils.gtmData(
            Event.CLICK_SMART_BILLS,
            CATEGORY_SMART_BILLS,
            Action.CLICK_LANGGANAN,
            Label.LANGGANAN
        )
        map[Key.USER_ID] = userId
        map[Key.SCREEN_NAME] = SCREEN_NAME_INITAL
        map.putAll(ADDITIONAL_INFO_MAP)
        TrackApp.getInstance().gtm.sendGeneralEvent(map)
    }

    fun clickAllBills(value: Boolean, listBills: List<RechargeBills>) {
        if (value) clickTickAllBills(listBills) else clickUntickAllBills(listBills)
    }

    private fun clickTickAllBills(listBills: List<RechargeBills>) {
        val trackingData = Bundle().apply {
            putString(TrackAppUtils.EVENT, Event.CLICK_SMART_BILLS)
            putString(TrackAppUtils.EVENT_ACTION, Action.CLICK_ALL_TAGIHAN)
            putString(TrackAppUtils.EVENT_CATEGORY, CATEGORY_SMART_BILLS)
            putString(TrackAppUtils.EVENT_LABEL, Label.ALL_TAGIHAN)
            putString(Key.SCREEN_NAME, SCREEN_NAME_INITAL)
            putString(Key.ITEM_LIST, LIST_VALUE)
            putString(Key.USER_ID, userId)
            putAll(getAdditionalData(BUSINESS_UNIT_SBM_VALUE))
            putParcelableArrayList(
                Key.ITEMS,
                ArrayList(listBills.mapIndexed { index, rechargeBills ->
                    buildItemBundle(index, rechargeBills)
                })
            )
        }

        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(Event.CLICK_SMART_BILLS, trackingData)
    }

    private fun clickUntickAllBills(listBills: List<RechargeBills>) {
        val trackingData = Bundle().apply {
            putString(TrackAppUtils.EVENT, Event.CLICK_SMART_BILLS)
            putString(TrackAppUtils.EVENT_ACTION, Action.UNCLICK_ALL_TAGIHAN)
            putString(TrackAppUtils.EVENT_CATEGORY, CATEGORY_SMART_BILLS)
            putString(TrackAppUtils.EVENT_LABEL, Label.ALL_TAGIHAN)
            putString(Key.SCREEN_NAME, SCREEN_NAME_INITAL)
            putString(Key.ITEM_LIST, LIST_VALUE)
            putString(Key.USER_ID, userId)
            putAll(getAdditionalData(BUSINESS_UNIT_SBM_VALUE))
            putParcelableArrayList(
                Key.ITEMS,
                ArrayList(listBills.mapIndexed { index, rechargeBills ->
                    buildItemBundle(index, rechargeBills)
                })
            )
        }

        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(Event.CLICK_SMART_BILLS, trackingData)
    }

    fun clickTickBill(tickedBill: RechargeBills, index: Int) {
        val trackingData = Bundle().apply {
            putString(TrackAppUtils.EVENT, Event.SELECT_CONTENT)
            putString(TrackAppUtils.EVENT_ACTION, Action.CLICK_TICK_BILL)
            putString(TrackAppUtils.EVENT_CATEGORY, CATEGORY_SMART_BILLS)
            putString(
                TrackAppUtils.EVENT_LABEL,
                "${tickedBill.categoryName} - ${tickedBill.operatorName}"
            )
            putString(Key.SCREEN_NAME, SCREEN_NAME_INITAL)
            putString(Key.ITEM_LIST, LIST_VALUE)
            putString(Key.USER_ID, userId)
            putAll(getAdditionalData(BUSINESS_UNIT_SBM_VALUE))
            putParcelableArrayList(
                Key.ITEMS,
                arrayListOf(buildItemBundle(index, tickedBill))
            )
        }
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(Event.SELECT_CONTENT, trackingData)
    }

    fun clickUntickBill(untickedBill: RechargeBills, index: Int) {
        val trackingData = Bundle().apply {
            putString(TrackAppUtils.EVENT, Event.CLICK_SMART_BILLS)
            putString(TrackAppUtils.EVENT_ACTION, Action.CLICK_UNTICK_BILL)
            putString(TrackAppUtils.EVENT_CATEGORY, CATEGORY_SMART_BILLS)
            putString(
                TrackAppUtils.EVENT_LABEL,
                "${untickedBill.categoryName} - ${untickedBill.operatorName}"
            )
            putString(Key.SCREEN_NAME, SCREEN_NAME_INITAL)
            putString(Key.ITEM_LIST, LIST_VALUE)
            putString(Key.USER_ID, userId)
            putAll(getAdditionalData(BUSINESS_UNIT_SBM_VALUE))
            putParcelableArrayList(
                Key.ITEMS,
                arrayListOf(buildItemBundle(index, untickedBill))
            )
        }
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(Event.SELECT_CONTENT, trackingData)
    }

    fun impressionAllProducts(bills: List<RechargeBills>) {
        val trackingData = Bundle().apply {
            putString(TrackAppUtils.EVENT, Event.VIEW_ITEM_LIST)
            putString(TrackAppUtils.EVENT_ACTION, Action.IMPRESSION_ALL_PRODUCT)
            putString(TrackAppUtils.EVENT_CATEGORY, CATEGORY_SMART_BILLS)
            putString(TrackAppUtils.EVENT_LABEL, "")
            putString(Key.SCREEN_NAME, SCREEN_NAME_INITAL)
            putString(Key.ITEM_LIST, LIST_VALUE)
            putString(Key.USER_ID, userId)
            putAll(getAdditionalData(BUSINESS_UNIT_RECHARGE_VALUE))
            putParcelableArrayList(
                Key.ITEMS,
                ArrayList(bills.mapIndexed { index, rechargeBills ->
                    buildItemBundle(index, rechargeBills)
                })
            )
        }
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(Event.VIEW_ITEM_LIST, trackingData)
    }

    fun clickPay(selectedBills: List<RechargeBills>, totalBillsCount: Int, totalPrice: Long) {
        val areAllBills = selectedBills.size == totalBillsCount
        val dataLayer = Bundle().apply {
            putString(TrackAppUtils.EVENT, EVENT_VALUE_CHECKOUT_PROGRESS)
            putString(
                TrackAppUtils.EVENT_ACTION,
                if (areAllBills) Action.CLICK_BAYAR_FULL else Action.CLICK_BAYAR_PARTIAL
            )
            putString(TrackAppUtils.EVENT_CATEGORY, CATEGORY_SMART_BILLS)
            putString(
                TrackAppUtils.EVENT_LABEL,
                "${Label.BAYAR} - $totalBillsCount - ${selectedBills.size} - $totalPrice"
            )
            putString(Key.CURRENT_SITE, CURRENT_SITE_VALUE)
            putString(Key.BUSINESS_UNIT, BUSINESS_UNIT_RECHARGE_VALUE)
            putString(Key.USER_ID, userId)
            putString(EnhanceEccomerce.CHECKOUT_STEP, STEP_VALUE)
            putString(EnhanceEccomerce.CHECKOUT_OPTION, OPTION_VALUE)
            putParcelableArrayList(
                ITEMS,
                ArrayList(selectedBills.mapIndexed { index, rechargeBills ->
                    buildItemBundle(
                        index,
                        rechargeBills
                    )
                })
            )
        }

        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(
            EVENT_VALUE_CHECKOUT_PROGRESS,
            dataLayer
        )
    }

    fun clickPayFailed(
        selectedBills: List<RechargeBills>,
        totalBillsCount: Int
    ) {
        val trackingData = Bundle().apply {
            putString(TrackAppUtils.EVENT, Event.CLICK_SMART_BILLS)
            putString(TrackAppUtils.EVENT_ACTION, Action.CLICK_BAYAR_FAILED)
            putString(TrackAppUtils.EVENT_CATEGORY, CATEGORY_SMART_BILLS)
            putString(
                TrackAppUtils.EVENT_LABEL,
                "${Label.GAGAL} - $totalBillsCount - ${selectedBills.size}"
            )
            putString(Key.CURRENT_SITE, CURRENT_SITE_VALUE)
            putString(Key.BUSINESS_UNIT, BUSINESS_UNIT_RECHARGE_VALUE)
            putString(Key.USER_ID, userId)
            putParcelableArrayList(
                ITEMS,
                ArrayList(selectedBills.mapIndexed { index, rechargeBills ->
                    buildItemBundle(
                        index,
                        rechargeBills
                    )
                })
            )
        }

        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(
            Event.VIEW_ITEM_LIST,
            trackingData
        )
    }

    fun clickBillDetail(bill: RechargeBills, index: Int) {
        val trackingData = Bundle().apply {
            putString(TrackAppUtils.EVENT, Event.CLICK_SMART_BILLS)
            putString(TrackAppUtils.EVENT_ACTION, Action.CLICK_DETAIL)
            putString(TrackAppUtils.EVENT_CATEGORY, CATEGORY_SMART_BILLS)
            putString(
                TrackAppUtils.EVENT_LABEL,
                "${bill.categoryName} - ${bill.operatorName}"
            )
            putString(Key.SCREEN_NAME, SCREEN_NAME_DETAIL)
            putString(Key.ITEM_LIST, LIST_VALUE)
            putString(Key.USER_ID, userId)
            putAll(getAdditionalData(BUSINESS_UNIT_SBM_VALUE))
            putParcelableArrayList(
                Key.ITEMS,
                arrayListOf(buildItemBundle(index, bill))
            )
        }
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(Event.SELECT_CONTENT, trackingData)
    }

    fun clickToolTip() {
        val map = TrackAppUtils.gtmData(
            Event.CLICK_SMART_BILLS,
            CATEGORY_SMART_BILLS,
            Action.CLICK_TOOL_TIP,
            ""
        )
        map[Key.USER_ID] = userId
        map.putAll(
            mapOf(
                Key.CURRENT_SITE to CURRENT_SITE_VALUE,
                Key.BUSINESS_UNIT to BUSINESS_UNIT_RECHARGE_VALUE
            )
        )
        TrackApp.getInstance().gtm.sendGeneralEvent(map)
    }

    fun clickMoreLearn() {
        val map = TrackAppUtils.gtmData(
            Event.CLICK_SMART_BILLS,
            CATEGORY_SMART_BILLS,
            Action.CLICK_MORE_LEARN,
            ""
        )
        map[Key.USER_ID] = userId
        map.putAll(
            mapOf(
                Key.CURRENT_SITE to CURRENT_SITE_VALUE,
                Key.BUSINESS_UNIT to BUSINESS_UNIT_RECHARGE_VALUE
            )
        )
        TrackApp.getInstance().gtm.sendGeneralEvent(map)
    }

    private fun createEcommerceProducts(bills: List<RechargeBills>): List<Map<String, Any>> {
        with(EnhanceEccomerce) {
            return bills.map {
                DataLayer.mapOf(
                    NAME, it.productName,
                    ID, it.productID,
                    PRICE, it.amount.toString(),
                    BRAND, NONE,
                    CATEGORY, it.categoryName,
                    VARIANT, NONE
                )
            }
        }
    }

    fun clickExpandAccordion(titleAccordion: String) {
        val map = TrackAppUtils.gtmData(
            Event.CLICK_SMART_BILLS,
            CATEGORY_SMART_BILLS,
            Action.CLICK_EXPAND_ACCORDION,
            titleAccordion
        )
        map[Key.USER_ID] = userId
        map.putAll(ADDITIONAL_INFO_MAP)
        TrackApp.getInstance().gtm.sendGeneralEvent(map)
    }

    fun clickCollapseAccordion(titleAccordion: String) {
        val map = TrackAppUtils.gtmData(
            Event.CLICK_SMART_BILLS,
            CATEGORY_SMART_BILLS,
            Action.CLICK_COLLAPSE_ACCORDION,
            titleAccordion
        )
        map[Key.USER_ID] = userId
        map.putAll(ADDITIONAL_INFO_MAP)
        TrackApp.getInstance().gtm.sendGeneralEvent(map)
    }

    fun clickRefreshAccordion(titleAccordion: String) {
        val map = TrackAppUtils.gtmData(
            Event.CLICK_SMART_BILLS,
            CATEGORY_SMART_BILLS,
            Action.CLICK_REFRESH_ACCORDION,
            titleAccordion
        )
        map[Key.USER_ID] = userId
        map.putAll(ADDITIONAL_INFO_MAP)
        TrackApp.getInstance().gtm.sendGeneralEvent(map)
    }

    private fun getProductBundle(items: List<RechargeBills>): ArrayList<Bundle> {
        val list = arrayListOf<Bundle>()
        items.forEachIndexed { index, it ->
            val itemBundle = Bundle().apply {
                putString(EnhanceEccomerce.ITEM_NAME, it.productName)
                putString(EnhanceEccomerce.ITEM_ID, it.productID)
                putString(EnhanceEccomerce.PRICE, it.amount.toString())
                putString(EnhanceEccomerce.ITEM_BRAND, NONE)
                putString(EnhanceEccomerce.ITEM_CATEGORY, it.categoryName)
                putString(EnhanceEccomerce.ITEM_VARIANT, NONE)
                putString(EnhanceEccomerce.QUANTITY, "1")
                putString(EnhanceEccomerce.SHOP_ID, NONE)
                putString(EnhanceEccomerce.SHOP_NAME, NONE)
                putString(EnhanceEccomerce.SHOP_TYPE, NONE)
            }
            list.add(itemBundle)
        }
        return list
    }

    //Add Bills Tracking
    //#1
    fun clickTambahTagihan() {
        val data = DataLayer.mapOf(
            TrackAppUtils.EVENT_ACTION, "click tambah tagihan button",
            TrackAppUtils.EVENT_LABEL, ""
        )
        data.addGeneralClick()
        TrackApp.getInstance().gtm.sendGeneralEvent(data)
    }

    //#2
    fun viewBottomsheetCatalog() {
        val data = DataLayer.mapOf(
            TrackAppUtils.EVENT_ACTION, "view - mau tambah tagihan",
            TrackAppUtils.EVENT_LABEL, "bottom sheet sbm add bills"
        )
        data.addGeneralView()
        TrackApp.getInstance().gtm.sendGeneralEvent(data)
    }

    //#3
    fun clickCloseBottomsheetCatalog() {
        val data = DataLayer.mapOf(
            TrackAppUtils.EVENT_ACTION, "click x - mau tambah tagihan",
            TrackAppUtils.EVENT_LABEL, "bottom sheet sbm add bills"
        )
        data.addGeneralClick()
        TrackApp.getInstance().gtm.sendGeneralEvent(data)
    }

    //#4
    fun clickCategoryBottomsheetCatalog(category: String) {
        val data = DataLayer.mapOf(
            TrackAppUtils.EVENT_ACTION,
            "click category - mau tambah tagihan",
            TrackAppUtils.EVENT_LABEL,
            String.format("%s - %s", "bottom sheet sbm add bills", category)
        )
        data.addGeneralClick()
        TrackApp.getInstance().gtm.sendGeneralEvent(data)
    }

    //#14
    fun clickViewShowToasterTelcoAddBills(category: String) {
        val data = DataLayer.mapOf(
            TrackAppUtils.EVENT_ACTION, "view add bills success - toaster box",
            TrackAppUtils.EVENT_LABEL, category
        )
        data.addGeneralViewAddBills()
        TrackApp.getInstance().gtm.sendGeneralEvent(data)
    }

    //#16
    fun clickKebab(category: String) {
        val data = DataLayer.mapOf(
            TrackAppUtils.EVENT_ACTION, "click kebab menu",
            TrackAppUtils.EVENT_LABEL, category
        )
        data.addGeneralClick()
        TrackApp.getInstance().gtm.sendGeneralEvent(data)
    }

    //#17
    fun clickHapusTagihan(category: String) {
        val data = DataLayer.mapOf(
            TrackAppUtils.EVENT_ACTION, "click hapus tagihan",
            TrackAppUtils.EVENT_LABEL, category
        )
        data.addGeneralClick()
        TrackApp.getInstance().gtm.sendGeneralEvent(data)
    }

    //#18
    fun clickBatalHapusTagihan() {
        val data = DataLayer.mapOf(
            TrackAppUtils.EVENT_ACTION, "click batal",
            TrackAppUtils.EVENT_LABEL, "delete confirmation pop up"
        )
        data.addGeneralClick()
        TrackApp.getInstance().gtm.sendGeneralEvent(data)
    }

    //#19
    fun clickConfirmHapusTagihan() {
        val data = DataLayer.mapOf(
            TrackAppUtils.EVENT_ACTION, "click hapus",
            TrackAppUtils.EVENT_LABEL, "delete confirmation pop up"
        )
        data.addGeneralClick()
        TrackApp.getInstance().gtm.sendGeneralEvent(data)
    }

    //#20
    fun viewDeleteBillSuccess() {
        val data = DataLayer.mapOf(
            TrackAppUtils.EVENT_ACTION, "view delete bill success",
            TrackAppUtils.EVENT_LABEL, "delete bottom sheet"
        )
        data.addGeneralClick()
        TrackApp.getInstance().gtm.sendGeneralEvent(data)
    }

    //#23
    fun viewCloseBottomSheet() {
        val data = DataLayer.mapOf(
            TrackAppUtils.EVENT_ACTION, "click x",
            TrackAppUtils.EVENT_LABEL, "delete bottom sheet"
        )
        data.addGeneralClick()
        TrackApp.getInstance().gtm.sendGeneralEvent(data)
    }

    fun viewHighlightWidget(productCategory: String) {
        val eventDataLayer = Bundle().apply {
            putString(TrackAppUtils.EVENT_ACTION, VIEW_ON_HIGHLIGHT_CATEGORY)
            putString(TrackAppUtils.EVENT_LABEL, String.format(STRING_FORMAT_FOR_HIGHLIGHT_PRODUCT,
                productCategory))
            putString(TRACKER_ID, TRACKER_ID_VIEW_HIGHLIGHT_PRODUCT)
        }

        eventDataLayer.addGeneralDigitalView()
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(CommonSmartBillsConstant.VIEW_DIGITAL_IRIS, eventDataLayer)
    }

    fun clickHighlightWidget(productCategory: String) {
        val eventDataLayer = Bundle().apply {
            putString(TrackAppUtils.EVENT_ACTION, CLICK_ON_HIGHLIGHT_CATEGORY)
            putString(TrackAppUtils.EVENT_LABEL, String.format(STRING_FORMAT_FOR_HIGHLIGHT_PRODUCT,
                productCategory))
            putString(TRACKER_ID, TRACKER_ID_CLICK_HIGHLIGHT_PRODUCT)
        }

        eventDataLayer.addGeneralDigitalClick()
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(CommonSmartBillsConstant.CLICK_DIGITAl, eventDataLayer)
    }

    fun closeHighlightWidget(productCategory: String) {
        val eventDataLayer = Bundle().apply {
            putString(TrackAppUtils.EVENT_ACTION, CLICK_X_ON_HIGHLIGHT_CATEGORY)
            putString(TrackAppUtils.EVENT_LABEL, String.format(STRING_FORMAT_FOR_HIGHLIGHT_PRODUCT,
                productCategory))
            putString(TRACKER_ID, TRACKER_ID_CLOSE_HIGHLIGHT_PRODUCT)
        }

        eventDataLayer.addGeneralDigitalClick()
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(CommonSmartBillsConstant.CLICK_DIGITAl, eventDataLayer)
    }


    private fun getAdditionalData(businessUnit: String): Bundle =
        Bundle().apply {
            putString(Key.CURRENT_SITE, CURRENT_SITE_VALUE)
            putString(Key.BUSINESS_UNIT, businessUnit)
        }

    private fun buildItemBundle(index: Int, item: RechargeBills): Bundle =
        Bundle().apply {
            putString(EnhanceEccomerce.INDEX, index.toString())
            putString(EnhanceEccomerce.ITEM_BRAND, item.operatorName)
            putString(EnhanceEccomerce.ITEM_CATEGORY, item.categoryName)
            putString(EnhanceEccomerce.ITEM_ID, item.productID)
            putString(EnhanceEccomerce.ITEM_NAME, item.productName)
            putString(
                EnhanceEccomerce.ITEM_VARIANT,
                if (item.newBillLabel.isNewLabel) NEW_BILL_LABEL else EXISTING_BILL_LABEL
            )
            putLong(EnhanceEccomerce.PRICE, item.amount)
        }

    companion object {

        const val CATEGORY_SMART_BILLS_ADD_BILLS = "digital - smart bill management - add bills"

        const val SCREEN_NAME_INITAL = "/initial-sbm-page"
        const val SCREEN_NAME_DETAIL = "/detail-sbm-page"
        const val CURRENT_SITE_VALUE = "tokopediaDigitalRecharge"
        const val BUSINESS_UNIT_VALUE = "top up and tagihan"
        const val CURRENCY_CODE_VALUE = "IDR"
        const val STEP_VALUE = "4"
        const val OPTION_VALUE = "click bayar"

        private const val CATEGORY_SMART_BILLS = "digital - smart bill management"

        private const val LIST_VALUE = "/smartbill"

        private const val NEW_BILL_LABEL = "new bill"
        private const val EXISTING_BILL_LABEL = "existing bill"

        private const val BUSINESS_UNIT_RECHARGE_VALUE = "recharge"
        private const val BUSINESS_UNIT_SBM_VALUE = "sbm"

        private const val STRING_FORMAT_FOR_HIGHLIGHT_PRODUCT = "highlighted %s"
        private const val TRACKER_ID_VIEW_HIGHLIGHT_PRODUCT = "34680"
        private const val TRACKER_ID_CLOSE_HIGHLIGHT_PRODUCT = "34681"
        private const val TRACKER_ID_CLICK_HIGHLIGHT_PRODUCT = "34682"


        val ADDITIONAL_INFO_MAP = mapOf(
            Key.CURRENT_SITE to CURRENT_SITE_VALUE,
            Key.BUSINESS_UNIT to BUSINESS_UNIT_VALUE
        )
    }
}
