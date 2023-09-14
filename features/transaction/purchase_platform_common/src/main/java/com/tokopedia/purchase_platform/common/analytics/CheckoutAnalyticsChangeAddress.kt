package com.tokopedia.purchase_platform.common.analytics

import javax.inject.Inject

/**
 * @author anggaprasetiyo on 05/06/18.
 */
class CheckoutAnalyticsChangeAddress @Inject constructor() : TransactionAnalytics() {

    fun eventClickShippingCartChangeAddressClickChecklistKotaAtauKecamatanPadaTambahAddress(districtName: String) {
        sendGeneralEvent(
            ConstantTransactionAnalytics.EventName.CLICK_SHIPPING,
            ConstantTransactionAnalytics.EventCategory.CART_CHANGE_ADDRESS,
            ConstantTransactionAnalytics.EventAction.CLICK_CHECKLIST_KOTA_ATAU_KECAMATAN_PADA_TAMBAH_ADDRESS,
            districtName
        )
    }

    fun eventClickShippingCartChangeAddressClickXPojokKananKotaAtauKecamatanPadaTambahAddress() {
        sendGeneralEvent(
            ConstantTransactionAnalytics.EventName.CLICK_SHIPPING,
            ConstantTransactionAnalytics.EventCategory.CART_CHANGE_ADDRESS,
            ConstantTransactionAnalytics.EventAction.CLICK_X_POJOK_KANAN_KOTA_ATAU_KECAMATAN_PADA_TAMBAH_ADDRESS
        )
    }

    fun eventClickShippingCartChangeAddressClickXPojokKiriKotaAtauKecamatanPadaTambahAddress() {
        sendGeneralEvent(
            ConstantTransactionAnalytics.EventName.CLICK_SHIPPING,
            ConstantTransactionAnalytics.EventCategory.CART_CHANGE_ADDRESS,
            ConstantTransactionAnalytics.EventAction.CLICK_X_POJOK_KIRI_KOTA_ATAU_KECAMATAN_PADA_TAMBAH_ADDRESS
        )
    }

    fun eventViewShippingCartChangeAddressViewValidationErrorTandaiLokasi(errorMessage: String) {
        sendGeneralEvent(
            ConstantTransactionAnalytics.EventName.VIEW_SHIPPING,
            ConstantTransactionAnalytics.EventCategory.CART_CHANGE_ADDRESS,
            ConstantTransactionAnalytics.EventAction.VIEW_VALIDATION_ERROR_TANDAI_LOKASI,
            errorMessage
        )
    }
}
