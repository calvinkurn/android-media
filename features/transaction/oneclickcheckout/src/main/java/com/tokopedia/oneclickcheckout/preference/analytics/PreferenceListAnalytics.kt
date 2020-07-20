package com.tokopedia.oneclickcheckout.preference.analytics

import com.tokopedia.purchase_platform.common.analytics.ConstantTransactionAnalytics.*
import com.tokopedia.purchase_platform.common.analytics.TransactionAnalytics

class PreferenceListAnalytics : TransactionAnalytics() {

    fun eventAddPreferenceFromPurchaseSetting() {
        sendEventCategoryAction(
                EventName.CLICK_CHECKOUT_EXPRESS,
                EventCategory.PURCHASE_SETTING,
                EventAction.ADD_PREFERENCE_OCC
        )
    }

    fun eventClickTrashBinInEditPreference() {
        sendEventCategoryAction(
                EventName.CLICK_CHECKOUT_EXPRESS,
                EventCategory.PURCHASE_SETTING,
                EventAction.CLICK_TRASH_ICON_OCC
        )
    }

    fun eventClickDeletePreferenceFromTrashBin() {
        sendEventCategoryAction(
                EventName.CLICK_CHECKOUT_EXPRESS,
                EventCategory.PURCHASE_SETTING,
                EventAction.CLICK_HAPUS_ON_TRASH_ICON_OCC
        )
    }

    fun eventClickPilihDurasiInANAFlow() {
        sendEventCategoryAction(
                EventName.CLICK_CHECKOUT_EXPRESS,
                EventCategory.PURCHASE_SETTING,
                EventAction.CLICK_PILIH_DURASI_PENGIRIMAN_IN_ANA_OCC
        )
    }

    fun eventClickOnDurasiOptionInPilihDurasiPengirimanPage(productId: String) {
        sendEventCategoryActionLabel(
                EventName.CLICK_CHECKOUT_EXPRESS,
                EventCategory.PURCHASE_SETTING,
                EventAction.CLICK_DURASI_OPTION_IN_DURASI_PAGE,
                productId
        )
    }

    fun eventClickPilihMetodePembayaranInDuration(productId: String) {
        sendEventCategoryActionLabel(
                EventName.CLICK_CHECKOUT_EXPRESS,
                EventCategory.PURCHASE_SETTING,
                EventAction.CLICK_PILIH_METODE_PEMBAYARAN_IN_DURATION_PAGE,
                productId
        )
    }

    fun eventClickPaymentMethodOptionInPilihMetodePembayaranPage(gatewayId: String) {
        sendEventCategoryActionLabel(
                EventName.CLICK_CHECKOUT_EXPRESS,
                EventCategory.PURCHASE_SETTING,
                EventAction.CLICK_PAYMENT_METHOD_OPTION_IN_PAYMENT_METHOD_PAGE,
                gatewayId
        )
    }

    fun eventClickUbahAddressInPreferenceSettingPage() {
        sendEventCategoryAction(
                EventName.CLICK_CHECKOUT_EXPRESS,
                EventCategory.PURCHASE_SETTING,
                EventAction.CLICK_UBAH_ADDRESS_IN_PREFERENCE_SETTING_PAGE
        )
    }

    fun eventClickUbahShippingInPreferenceSettingPage() {
        sendEventCategoryAction(
                EventName.CLICK_CHECKOUT_EXPRESS,
                EventCategory.PURCHASE_SETTING,
                EventAction.CLICK_UBAH_SHIPPING_IN_PREFERENCE_SETTING_PAGE
        )
    }

    fun eventClickUbahPaymentInPreferenceSettingPage() {
        sendEventCategoryAction(
                EventName.CLICK_CHECKOUT_EXPRESS,
                EventCategory.PURCHASE_SETTING,
                EventAction.CLICK_UBAH_PAYMENT_IN_PREFERENCE_SETTING_PAGE
        )
    }

    fun eventClickAddressOptionInPilihAlamatPage() {
        sendEventCategoryAction(
                EventName.CLICK_CHECKOUT_EXPRESS,
                EventCategory.PURCHASE_SETTING,
                EventAction.CLICK_ADDRESS_OPTION_IN_PILIH_ALAMAT_PAGE
        )
    }

    fun eventClickSimpanAlamatInPilihAlamatPage() {
        sendEventCategoryAction(
                EventName.CLICK_CHECKOUT_EXPRESS,
                EventCategory.PURCHASE_SETTING,
                EventAction.CLICK_SIMPAN_ALAMAT_IN_PILIH_ALAMAT_PAGE
        )
    }

    fun eventClickSettingPreferenceGearInPreferenceListPage() {
        sendEventCategoryAction(
                EventName.CLICK_CHECKOUT_EXPRESS,
                EventCategory.PURCHASE_SETTING,
                EventAction.CLICK_GEAR_LOGO_IN_PREFERENCE_LIST_PAGE
        )
    }

    fun eventClickJadikanPilihanUtama() {
        sendEventCategoryAction(
                EventName.CLICK_CHECKOUT_EXPRESS,
                EventCategory.PURCHASE_SETTING,
                EventAction.CLICK_JADIKAN_PILIHAN_UTAMA
        )
    }

    fun eventClickBackArrowInEditPreference() {
        sendEventCategoryAction(
                EventName.CLICK_CHECKOUT_EXPRESS,
                EventCategory.PURCHASE_SETTING,
                EventAction.CLICK_BACK_ARROW_IN_EDIT_PREFERENCE
        )
    }

    fun eventClickBackArrowInPilihAlamat() {
        sendEventCategoryAction(
                EventName.CLICK_CHECKOUT_EXPRESS,
                EventCategory.PURCHASE_SETTING,
                EventAction.CLICK_BACK_ARROW_IN_PILIH_ALAMAT
        )
    }

    fun eventClickBackArrowInPilihDurasi() {
        sendEventCategoryAction(
                EventName.CLICK_CHECKOUT_EXPRESS,
                EventCategory.PURCHASE_SETTING,
                EventAction.CLICK_BACK_ARROW_IN_PILIH_DURASI
        )
    }

    fun eventClickBackArrowInPilihPembayaran() {
        sendEventCategoryAction(
                EventName.CLICK_CHECKOUT_EXPRESS,
                EventCategory.PURCHASE_SETTING,
                EventAction.CLICK_BACK_ARROW_IN_PILIH_METHOD_PAYMENT
        )
    }

    fun eventClickSimpanOnSummaryPurchaseSetting() {
        sendEventCategoryAction(
                EventName.CLICK_CHECKOUT_EXPRESS,
                EventCategory.PURCHASE_SETTING,
                EventAction.CLICK_SIMPAN_ON_SUMMARY_PURCHASE_SETTING
        )
    }
}
