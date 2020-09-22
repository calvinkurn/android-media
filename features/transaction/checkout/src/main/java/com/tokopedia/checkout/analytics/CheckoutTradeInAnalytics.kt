package com.tokopedia.checkout.analytics

import android.app.Activity
import com.tokopedia.purchase_platform.common.analytics.ConstantTransactionAnalytics.*
import com.tokopedia.purchase_platform.common.analytics.ConstantTransactionAnalytics.EventName.CLICK_TRADEIN
import com.tokopedia.purchase_platform.common.analytics.TransactionAnalytics

class CheckoutTradeInAnalytics constructor(val userId: String) : TransactionAnalytics() {

    companion object {
        const val KEY_EVENT = "event"
        const val KEY_LOGIN_STATUS = "isLoggedInStatus"
        const val KEY_BUSINESS_UNIT = "businessUnit"
        const val KEY_CURRENT_SITE = "currentSite"
        const val KEY_USER_ID = "userId"
        const val KEY_SCREEN_NAME = "screenName"

        const val VALUE_OPEN_SCREEN = "openScreen"
        const val VALUE_TRADE_IN = "trade-in"
        const val VALUE_TOKOPEDIA_MARKETPLACE = "tokopediamarketplace"

        const val SCREEN_NAME_NORMAL_ADDRESS = "trade in - checkout page - tukar di alamatmu"
        const val SCREEN_NAME_DROP_OFF_ADDRESS = "trade in - checkout page - titik ambil"

        const val SELECTED_TAB_NORMAL_ADDRESS = 0;
        const val SELECTED_TAB_DROP_OFF_ADDRESS = 1;

        // Event Category
        const val EVENT_CATEGORY_SELF_PICKUP_ADDRESS_SELECTION_TRADE_IN = "self pickup address selection trade in"

        // Event Action
        const val EVENT_ACTION_CLICK_BACK_BUTTON = "click back button"
        const val EVENT_ACTION_CLICK_INFORMATION = "click information"
        const val EVENT_ACTION_CLICK_TUKAR_DI_INDOMARET = "click tukar di indomaret"
        const val EVENT_ACTION_CLICK_TUKAR_DI_ALAMATMU = "click tukar di alamatmu tab"
        const val EVENT_ACTION_CLICK_PILIH_INDOMARET = "click choose pickup location"
        const val EVENT_ACTION_CLICK_CHANGE_ADDRESS = "click change address"
        const val EVENT_ACTION_CLICK_COURIER_OPTION = "click courier option"
        const val EVENT_ACTION_TICK_DONATION_OPTION = "tick donation"
        const val EVENT_ACTION_UNTICK_DONATION_OPTION = "untick donation"
        const val EVENT_ACTION_TICK_GOLD_SAVING = "tick gold saving"
        const val EVENT_ACTION_UNTICK_GOLD_SAVING = "untick gold saving"
        const val EVENT_ACTION_CLICK_PROMO = "click promo"
        const val EVENT_ACTION_ERROR = "error"

        // Event Label
        const val EVENT_LABEL_OPSI_PENUKARAN = "opsi penukaran"
        const val EVENT_LABEL_INDOMARET = "indomaret"
        const val EVENT_LABEL_OUT_OF_COVERAGE = "out of coverage"
    }

    // Trade In
    fun eventViewCheckoutPageTradeIn() {
        sendEventCategoryAction(EventName.VIEW_TRADEIN,
                EventCategory.COURIER_SELECTION_TRADE_IN,
                EventAction.VIEW_CHECKOUYT_PAGE_TRADE_IN
        )
    }

    fun eventClickGantiNomor(tradeIn: Boolean) {
        sendEventCategoryAction(if (tradeIn) EventName.CLICK_TRADEIN else "",
                EventCategory.COURIER_SELECTION_TRADE_IN,
                EventAction.CLICK_GANTI_NOMOR
        )
    }

    fun eventClickButtonPilihDurasi() {
        sendEventCategoryAction(EventName.CLICK_TRADEIN,
                EventCategory.COURIER_SELECTION_TRADE_IN,
                EventAction.CLICK_BUTTON_PILIH_DURASI
        )
    }

    fun eventClickKurirTradeIn(label: String?) {
        sendEventCategoryActionLabel(EventName.CLICK_TRADEIN,
                EventCategory.COURIER_SELECTION_TRADE_IN,
                EventAction.CLICK_KURIR_TRADE_IN,
                label
        )
    }

    fun eventClickBayarTradeInFailed() {
        sendEventCategoryActionLabel(EventName.CLICK_TRADEIN,
                EventCategory.COURIER_SELECTION_TRADE_IN,
                EventAction.CLICK_BAYAR,
                EventLabel.FAILED
        )
    }

    fun eventClickBayarCourierNotComplete() {
        sendEventCategoryActionLabel(EventName.CLICK_TRADEIN,
                EventCategory.COURIER_SELECTION_TRADE_IN,
                EventAction.CLICK_BAYAR,
                EventLabel.COURIER_NOT_COMPLETE
        )
    }

    fun eventClickJemputTab() {
        sendEventCategoryAction(EventName.CLICK_COURIER,
                EventCategory.COURIER_SELECTION,
                EventAction.CLICK_JEMPUT_TAB
        )
    }

    fun eventClickDropOffTab() {
        sendEventCategoryAction(EventName.CLICK_COURIER,
                EventCategory.COURIER_SELECTION,
                EventAction.CLICK_DROP_OFF_TAB
        )
    }

    fun eventClickUbahTitikDropoffButton() {
        sendEventCategoryAction(EventName.CLICK_COURIER,
                EventCategory.COURIER_SELECTION,
                EventAction.CLICK_UBAH_TITIK_DROP_OFF_BUTTON
        )
    }

    // Trade in revamp (2.0)
    fun sendTradeInCheckoutTracker(screen: Int, gtmData: MutableMap<String, Any>) {
        gtmData[KEY_BUSINESS_UNIT] = VALUE_TRADE_IN
        gtmData[KEY_CURRENT_SITE] = VALUE_TOKOPEDIA_MARKETPLACE
        gtmData[KEY_USER_ID] = userId
        when (screen) {
            SELECTED_TAB_NORMAL_ADDRESS -> {
                gtmData[KEY_SCREEN_NAME] = SCREEN_NAME_NORMAL_ADDRESS
            }
            SELECTED_TAB_DROP_OFF_ADDRESS -> {
                gtmData[KEY_SCREEN_NAME] = SCREEN_NAME_DROP_OFF_ADDRESS
            }
        }

        sendGeneralEvent(gtmData)
    }

    // 28, 39
    fun sendOpenScreenName(screen: Int, activity: Activity) {
        val gtmData = mutableMapOf<String, String>()
        gtmData[KEY_BUSINESS_UNIT] = VALUE_TRADE_IN
        gtmData[KEY_CURRENT_SITE] = VALUE_TOKOPEDIA_MARKETPLACE
        gtmData[KEY_USER_ID] = userId

        var screenName = ""
        when (screen) {
            SELECTED_TAB_NORMAL_ADDRESS -> {
                screenName = SCREEN_NAME_NORMAL_ADDRESS
            }
            SELECTED_TAB_DROP_OFF_ADDRESS -> {
                screenName = SCREEN_NAME_DROP_OFF_ADDRESS
            }
        }

        sendScreenName(activity, screenName, gtmData)
    }

    // 29, 40
    fun eventTradeInClickBackButton(screen: Int) {
        val gtmData = getGtmData(
                CLICK_TRADEIN,
                EVENT_CATEGORY_SELF_PICKUP_ADDRESS_SELECTION_TRADE_IN,
                EVENT_ACTION_CLICK_BACK_BUTTON,
                ""
        )

        sendTradeInCheckoutTracker(screen, gtmData)
    }

    // 30, 41
    fun eventTradeInClickInformation(screen: Int) {
        val gtmData = getGtmData(
                CLICK_TRADEIN,
                EVENT_CATEGORY_SELF_PICKUP_ADDRESS_SELECTION_TRADE_IN,
                EVENT_ACTION_CLICK_INFORMATION,
                EVENT_LABEL_OPSI_PENUKARAN
        )

        sendTradeInCheckoutTracker(screen, gtmData)
    }

    // 31
    fun eventTradeInClickTukarDiIndomaret(screen: Int) {
        val gtmData = getGtmData(
                CLICK_TRADEIN,
                EVENT_CATEGORY_SELF_PICKUP_ADDRESS_SELECTION_TRADE_IN,
                EVENT_ACTION_CLICK_TUKAR_DI_INDOMARET,
                EVENT_LABEL_INDOMARET
        )

        sendTradeInCheckoutTracker(screen, gtmData)
    }

    // 32
    fun eventTradeInClickChangeAddress(screen: Int) {
        val gtmData = getGtmData(
                CLICK_TRADEIN,
                EVENT_CATEGORY_SELF_PICKUP_ADDRESS_SELECTION_TRADE_IN,
                EVENT_ACTION_CLICK_CHANGE_ADDRESS,
                ""
        )

        sendTradeInCheckoutTracker(screen, gtmData)
    }

    // 33, 45
    fun eventTradeInClickCourierOption(screen: Int) {
        val gtmData = getGtmData(
                CLICK_TRADEIN,
                EVENT_CATEGORY_SELF_PICKUP_ADDRESS_SELECTION_TRADE_IN,
                EVENT_ACTION_CLICK_COURIER_OPTION,
                ""
        )

        sendTradeInCheckoutTracker(screen, gtmData)
    }

    // 34, 46
    fun eventTradeInClickDonationOption(screen: Int, tick: Boolean) {
        val gtmData = getGtmData(
                CLICK_TRADEIN,
                EVENT_CATEGORY_SELF_PICKUP_ADDRESS_SELECTION_TRADE_IN,
                if (tick) EVENT_ACTION_TICK_DONATION_OPTION else EVENT_ACTION_UNTICK_DONATION_OPTION,
                ""
        )

        sendTradeInCheckoutTracker(screen, gtmData)
    }

    // 35, 47
    fun eventTradeInClickEgoldOption(screen: Int, tick: Boolean) {
        val gtmData = getGtmData(
                CLICK_TRADEIN,
                EVENT_CATEGORY_SELF_PICKUP_ADDRESS_SELECTION_TRADE_IN,
                if (tick) EVENT_ACTION_TICK_GOLD_SAVING else EVENT_ACTION_UNTICK_GOLD_SAVING,
                ""
        )

        sendTradeInCheckoutTracker(screen, gtmData)
    }

    // 36, 48
    fun eventTradeInClickPromo(screen: Int) {
        val gtmData = getGtmData(
                CLICK_TRADEIN,
                EVENT_CATEGORY_SELF_PICKUP_ADDRESS_SELECTION_TRADE_IN,
                EVENT_ACTION_CLICK_PROMO,
                ""
        )

        sendTradeInCheckoutTracker(screen, gtmData)
    }

    // 38
    fun eventTradeInClickCourierGetOutOfCoverageError(screen: Int) {
        val gtmData = getGtmData(
                CLICK_TRADEIN,
                EVENT_CATEGORY_SELF_PICKUP_ADDRESS_SELECTION_TRADE_IN,
                EVENT_ACTION_ERROR,
                EVENT_LABEL_OUT_OF_COVERAGE
        )

        sendTradeInCheckoutTracker(screen, gtmData)
    }

    // 42
    fun eventTradeInClickTukarDiAlamatmu(screen: Int) {
        val gtmData = getGtmData(
                CLICK_TRADEIN,
                EVENT_CATEGORY_SELF_PICKUP_ADDRESS_SELECTION_TRADE_IN,
                EVENT_ACTION_CLICK_TUKAR_DI_ALAMATMU,
                ""
        )

        sendTradeInCheckoutTracker(screen, gtmData)
    }

    // 43
    fun eventTradeInClickPilihIndomaret(screen: Int) {
        val gtmData = getGtmData(
                CLICK_TRADEIN,
                EVENT_CATEGORY_SELF_PICKUP_ADDRESS_SELECTION_TRADE_IN,
                EVENT_ACTION_CLICK_PILIH_INDOMARET,
                EVENT_LABEL_INDOMARET
        )

        sendTradeInCheckoutTracker(screen, gtmData)
    }

    // Notes :
    // 37, 49 : EE
    // 44 Double
    // 50 Invalid UI

}