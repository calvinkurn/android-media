package com.tokopedia.purchase_platform.common.analytics

import com.tokopedia.analyticconstant.DataLayer
import com.tokopedia.purchase_platform.common.analytics.ConstantTransactionAnalytics.ExtraKey
import com.tokopedia.track.TrackApp
import javax.inject.Inject

/**
 * @author anggaprasetiyo on 06/06/18.
 */
class CheckoutAnalyticsCourierSelection @Inject constructor() : TransactionAnalytics() {
    fun eventClickAtcCourierSelectionClickBackArrow() {
        sendGeneralEvent(
                ConstantTransactionAnalytics.EventName.CLICK_ATC,
                ConstantTransactionAnalytics.EventCategory.COURIER_SELECTION,
                ConstantTransactionAnalytics.EventAction.CLICK_BACK_ARROW
        )
    }

    fun eventClickAtcCourierSelectionClickGantiAlamatAtauKirimKeBeberapaAlamat() {
        sendGeneralEvent(
                ConstantTransactionAnalytics.EventName.CLICK_ATC,
                ConstantTransactionAnalytics.EventCategory.COURIER_SELECTION,
                ConstantTransactionAnalytics.EventAction.CLICK_GANTI_ALAMAT_ATAU_KIRIM_KE_BEBERAPA_ALAMAT
        )
    }

    fun eventClickAtcCourierSelectionClickKuponSayaFromGunakanKodePromoAtauKupon() {
        sendGeneralEvent(
                ConstantTransactionAnalytics.EventName.CLICK_ATC,
                ConstantTransactionAnalytics.EventCategory.COURIER_SELECTION,
                ConstantTransactionAnalytics.EventAction.CLICK_KUPON_SAYA_FROM_GUNAKAN_KODE_PROMO_ATAU_KUPON
        )
    }

    fun eventClickAtcCourierSelectionClickKodePromoFromGunakanKodePromoAtauKupon() {
        sendGeneralEvent(
                ConstantTransactionAnalytics.EventName.CLICK_ATC,
                ConstantTransactionAnalytics.EventCategory.COURIER_SELECTION,
                ConstantTransactionAnalytics.EventAction.CLICK_KODE_PROMO_FROM_GUNAKAN_KODE_PROMO_ATAU_KUPON
        )
    }

    fun eventClickAtcCourierSelectionClickSubtotal() {
        sendGeneralEvent(
                ConstantTransactionAnalytics.EventName.CLICK_ATC,
                ConstantTransactionAnalytics.EventCategory.COURIER_SELECTION,
                ConstantTransactionAnalytics.EventAction.CLICK_SUBTOTAL
        )
    }

    fun eventClickAtcCourierSelectionClickAsuransiPengiriman() {
        sendGeneralEvent(
                ConstantTransactionAnalytics.EventName.CLICK_ATC,
                ConstantTransactionAnalytics.EventCategory.COURIER_SELECTION,
                ConstantTransactionAnalytics.EventAction.CLICK_ASURANSI_PENGIRIMAN
        )
    }

    fun eventClickAtcCourierSelectionClickDropship() {
        sendGeneralEvent(
                ConstantTransactionAnalytics.EventName.CLICK_ATC,
                ConstantTransactionAnalytics.EventCategory.COURIER_SELECTION,
                ConstantTransactionAnalytics.EventAction.CLICK_DROPSHIP
        )
    }

    @Deprecated("")
    fun eventClickAtcCourierSelectionClickKuponFromGunakanKodePromoAtauKupon() {
        sendGeneralEvent(
                ConstantTransactionAnalytics.EventName.CLICK_ATC,
                ConstantTransactionAnalytics.EventCategory.COURIER_SELECTION,
                ConstantTransactionAnalytics.EventAction.CLICK_KUPON_FROM_GUNAKAN_KODE_PROMO_ATAU_KUPON
        )
    }

    fun eventClickAtcCourierSelectionClickPilihMetodePembayaranNotSuccess(errorMessage: String?) {
        sendGeneralEvent(
                ConstantTransactionAnalytics.EventName.CLICK_ATC,
                ConstantTransactionAnalytics.EventCategory.COURIER_SELECTION,
                ConstantTransactionAnalytics.EventAction.CLICK_PILIH_METODE_PEMBAYARAN,
                ConstantTransactionAnalytics.EventLabel.NOT_SUCCESS + " - " + errorMessage
        )
    }

    fun eventViewAtcCourierSelectionImpressionOnPopUpKupon() {
        sendGeneralEvent(
                ConstantTransactionAnalytics.EventName.VIEW_ATC,
                ConstantTransactionAnalytics.EventCategory.COURIER_SELECTION,
                ConstantTransactionAnalytics.EventAction.IMPRESSION_ON_POP_UP_KUPON,
                ConstantTransactionAnalytics.EventLabel.KUOTA_PENUKARAN
        )
    }

    @Deprecated("")
    fun eventClickAtcCourierSelectionClickGunakanKodeFormGunakanKodePromoAtauKupon() {
        sendGeneralEvent(
                ConstantTransactionAnalytics.EventName.CLICK_ATC,
                ConstantTransactionAnalytics.EventCategory.COURIER_SELECTION,
                ConstantTransactionAnalytics.EventAction.CLICK_GUNAKAN_KODE_FROM_GUNAKAN_KODE_PROMO_ATAU_KUPON
        )
    }

    fun eventViewPromoCourierSelectionValidationErrorVoucherPromoFromGunakanKodePromoAtauKupon(errorMessage: String?) {
        sendEventCategoryActionLabel(
                ConstantTransactionAnalytics.EventName.VIEW_PROMO,
                ConstantTransactionAnalytics.EventCategory.COURIER_SELECTION,
                ConstantTransactionAnalytics.EventAction.VALIDATION_ERROR_VOUCHER_PROMO_FROM_GUNAKAN_KODE_PROMO_ATAU_KUPON,
                errorMessage
        )
    }

    fun eventClickCouponCourierSelectionClickKuponFromKuponSaya(couponName: String?) {
        sendEventCategoryActionLabel(
                ConstantTransactionAnalytics.EventName.CLICK_COUPON,
                ConstantTransactionAnalytics.EventCategory.COURIER_SELECTION,
                ConstantTransactionAnalytics.EventAction.CLICK_KUPON_FROM_KUPON_SAYA,
                couponName
        )
    }

    fun eventClickCourierSelectionClickBackArrowFromGunakanKodePromoAtauKupon() {
        sendGeneralEvent(
                ConstantTransactionAnalytics.EventName.CLICK_COURIER,
                ConstantTransactionAnalytics.EventCategory.COURIER_SELECTION,
                ConstantTransactionAnalytics.EventAction.CLICK_BACK_ARROW_FROM_GUNAKAN_KODE_PROMO_ATAU_KUPON
        )
    }

    fun sendEnhancedECommerceCheckout(cartMap: Map<String, Any>,
                                      tradeInCustomDimension: Map<String, String>?,
                                      transactionId: String?,
                                      eventCategory: String,
                                      eventAction: String,
                                      eventLabel: String) {
        val dataLayer = getGtmData(
                ConstantTransactionAnalytics.EventName.CHECKOUT,
                eventCategory,
                eventAction,
                eventLabel
        )
        dataLayer[ConstantTransactionAnalytics.Key.E_COMMERCE] = cartMap
        dataLayer[ConstantTransactionAnalytics.Key.CURRENT_SITE] = ConstantTransactionAnalytics.CustomDimension.DIMENSION_CURRENT_SITE_MARKETPLACE
        if (tradeInCustomDimension != null && tradeInCustomDimension.isNotEmpty()) {
            dataLayer.putAll(tradeInCustomDimension)
        }
        if (transactionId?.isNotEmpty() == true) {
            dataLayer[ConstantTransactionAnalytics.Key.PAYMENT_ID] = transactionId
        }
        sendEnhancedEcommerce(dataLayer)
    }

    fun flushEnhancedECommerceCheckout() {
        val dataLayer = DataLayer.mapOf(
                ConstantTransactionAnalytics.Key.E_COMMERCE, null,
                ConstantTransactionAnalytics.Key.CURRENT_SITE, null
        )
        sendEnhancedEcommerce(dataLayer)
    }

    fun eventClickCourierSelectionClickPilihAlamatLain() {
        sendGeneralEvent(
                ConstantTransactionAnalytics.EventName.CLICK_ATC,
                ConstantTransactionAnalytics.EventCategory.COURIER_SELECTION,
                ConstantTransactionAnalytics.EventAction.CLICK_PILIH_ALAMAT_LAIN
        )
    }

    fun eventClickCourierSelectionClickTopDonasi() {
        sendGeneralEvent(
                ConstantTransactionAnalytics.EventName.CLICK_ATC,
                ConstantTransactionAnalytics.EventCategory.COURIER_SELECTION,
                ConstantTransactionAnalytics.EventAction.CLICK_TOP_DONASI
        )
    }

    //PHASE 2
    fun eventClickCouponCourierSelectionClickGunakanKodeFormGunakanKodePromoAtauKuponSuccess() {
        sendGeneralEvent(
                ConstantTransactionAnalytics.EventName.CLICK_COUPON,
                ConstantTransactionAnalytics.EventCategory.COURIER_SELECTION,
                ConstantTransactionAnalytics.EventAction.CLICK_GUNAKAN_KODE_FROM_GUNAKAN_KODE_PROMO_ATAU_KUPON,
                ConstantTransactionAnalytics.EventLabel.SUCCESS
        )
    }

    fun eventClickCouponCourierSelectionClickGunakanKodeFormGunakanKodePromoAtauKuponFailed() {
        sendGeneralEvent(
                ConstantTransactionAnalytics.EventName.CLICK_COUPON,
                ConstantTransactionAnalytics.EventCategory.COURIER_SELECTION,
                ConstantTransactionAnalytics.EventAction.CLICK_GUNAKAN_KODE_FROM_GUNAKAN_KODE_PROMO_ATAU_KUPON,
                ConstantTransactionAnalytics.EventLabel.FAILED
        )
    }

    fun eventClickCouponCourierSelectionClickKuponFromGunakanKodePromoAtauKuponSuccess() {
        sendGeneralEvent(
                ConstantTransactionAnalytics.EventName.CLICK_COUPON,
                ConstantTransactionAnalytics.EventCategory.COURIER_SELECTION,
                ConstantTransactionAnalytics.EventAction.CLICK_KUPON_FROM_GUNAKAN_KODE_PROMO_ATAU_KUPON,
                ConstantTransactionAnalytics.EventLabel.SUCCESS
        )
    }

    fun eventClickCouponCourierSelectionClickKuponFromGunakanKodePromoAtauKuponFailed() {
        sendGeneralEvent(
                ConstantTransactionAnalytics.EventName.CLICK_COUPON,
                ConstantTransactionAnalytics.EventCategory.COURIER_SELECTION,
                ConstantTransactionAnalytics.EventAction.CLICK_KUPON_FROM_GUNAKAN_KODE_PROMO_ATAU_KUPON,
                ConstantTransactionAnalytics.EventLabel.FAILED
        )
    }

    fun eventClickBuyCourierSelectionClickPilihMetodePembayaranCourierNotComplete() {
        sendGeneralEvent(
                ConstantTransactionAnalytics.EventName.CLICK_BUY,
                ConstantTransactionAnalytics.EventCategory.COURIER_SELECTION,
                ConstantTransactionAnalytics.EventAction.CLICK_PILIH_METODE_PEMBAYARAN,
                ConstantTransactionAnalytics.EventLabel.COURIER_NOT_COMPLETE
        )
    }

    fun eventClickBuyPromoRedState() {
        sendGeneralEvent(
                ConstantTransactionAnalytics.EventName.CLICK_BUY,
                ConstantTransactionAnalytics.EventCategory.COURIER_SELECTION,
                ConstantTransactionAnalytics.EventAction.CLICK_PILIH_METODE_PEMBAYARAN,
                ConstantTransactionAnalytics.EventLabel.NOT_SUCCESS + " - " + ConstantTransactionAnalytics.EventLabel.PROMO_RED_STATE
        )
    }

    fun eventClickBuyCourierSelectionClickBayarFailedDropshipper() {
        sendGeneralEvent(
                ConstantTransactionAnalytics.EventName.CLICK_BUY,
                ConstantTransactionAnalytics.EventCategory.COURIER_SELECTION,
                ConstantTransactionAnalytics.EventAction.CLICK_BAYAR,
                ConstantTransactionAnalytics.EventLabel.FAILED_DROPSHIPPER
        )
    }

    //Robin Hood
    fun eventClickCourierCourierSelectionClickXPadaDurasiPengiriman() {
        sendGeneralEvent(
                ConstantTransactionAnalytics.EventName.CLICK_COURIER,
                ConstantTransactionAnalytics.EventCategory.COURIER_SELECTION,
                ConstantTransactionAnalytics.EventAction.CLICK_X_PADA_DURASI_PENGIRIMAN
        )
    }

    fun eventClickCourierCourierSelectionClickChecklistPilihDurasiPengiriman(eventLabelDuration: String?) {
        sendEventCategoryActionLabel(
                ConstantTransactionAnalytics.EventName.CLICK_COURIER,
                ConstantTransactionAnalytics.EventCategory.COURIER_SELECTION,
                ConstantTransactionAnalytics.EventAction.CLICK_CHECKLIST_PILIH_DURASI_PENGIRIMAN,
                eventLabelDuration
        )
    }

    fun eventViewCourierCourierSelectionViewPreselectedCourierOption(eventLabelPreselectedCourierPartner: String?) {
        sendEventCategoryActionLabel(
                ConstantTransactionAnalytics.EventName.VIEW_COURIER,
                ConstantTransactionAnalytics.EventCategory.COURIER_SELECTION,
                ConstantTransactionAnalytics.EventAction.VIEW_PRESELECTED_COURIER_OPTION,
                eventLabelPreselectedCourierPartner
        )
    }

    fun eventClickCourierCourierSelectionClickUbahKurir(label: String) {
        sendGeneralEvent(
                ConstantTransactionAnalytics.EventName.CLICK_COURIER,
                ConstantTransactionAnalytics.EventCategory.COURIER_SELECTION,
                ConstantTransactionAnalytics.EventAction.CLICK_UBAH_KURIR,
                label
        )
    }

    fun eventClickCourierCourierSelectionClickXPadaKurirPengiriman() {
        sendGeneralEvent(
                ConstantTransactionAnalytics.EventName.CLICK_COURIER,
                ConstantTransactionAnalytics.EventCategory.COURIER_SELECTION,
                ConstantTransactionAnalytics.EventAction.CLICK_X_PADA_KURIR_PENGIRIMAN
        )
    }

    fun eventClickCourierCourierSelectionClickUbahDurasi() {
        sendGeneralEvent(
                ConstantTransactionAnalytics.EventName.CLICK_COURIER,
                ConstantTransactionAnalytics.EventCategory.COURIER_SELECTION,
                ConstantTransactionAnalytics.EventAction.CLICK_UBAH_DURASI
        )
    }

    // Year End Promo
    fun eventViewPreselectedCourierOption(shippingProductId: Int) {
        sendGeneralEvent(
                ConstantTransactionAnalytics.EventName.VIEW_COURIER,
                ConstantTransactionAnalytics.EventCategory.COURIER_SELECTION,
                ConstantTransactionAnalytics.EventAction.VIEW_PRESELECTED_COURIER_OPTION, shippingProductId.toString())
    }

    fun eventViewDuration(isCourierPromo: Boolean, duration: String?) {
        val eventLabel = if (isCourierPromo) ConstantTransactionAnalytics.EventLabel.PROMO + ConstantTransactionAnalytics.EventLabel.SEPARATOR + duration else ConstantTransactionAnalytics.EventLabel.NON_PROMO + ConstantTransactionAnalytics.EventLabel.SEPARATOR + duration
        sendGeneralEvent(
                ConstantTransactionAnalytics.EventName.VIEW_COURIER,
                ConstantTransactionAnalytics.EventCategory.COURIER_SELECTION,
                ConstantTransactionAnalytics.EventAction.VIEW_DURATION,
                eventLabel
        )
    }

    fun eventViewCourierOption(isCourierPromo: Boolean, shippingProductId: Int) {
        val eventLabel = if (isCourierPromo) ConstantTransactionAnalytics.EventLabel.PROMO + ConstantTransactionAnalytics.EventLabel.SEPARATOR + shippingProductId else ConstantTransactionAnalytics.EventLabel.NON_PROMO + ConstantTransactionAnalytics.EventLabel.SEPARATOR + shippingProductId
        sendGeneralEvent(
                ConstantTransactionAnalytics.EventName.VIEW_COURIER,
                ConstantTransactionAnalytics.EventCategory.COURIER_SELECTION,
                ConstantTransactionAnalytics.EventAction.VIEW_COURIER_OPTION,
                eventLabel
        )
    }

    fun eventClickChecklistPilihDurasiPengiriman(isCourierPromo: Boolean, duration: String?, isCod: Boolean, shippingPriceMin: String, shippingPriceHigh: String) {
        val eventLabel = ((if (isCourierPromo) ConstantTransactionAnalytics.EventLabel.PROMO else ConstantTransactionAnalytics.EventLabel.NON_PROMO)
                + ConstantTransactionAnalytics.EventLabel.SEPARATOR + duration
                + ConstantTransactionAnalytics.EventLabel.SEPARATOR + (if (isCod) ConstantTransactionAnalytics.EventLabel.COD else "")
                + ConstantTransactionAnalytics.EventLabel.SEPARATOR + shippingPriceMin
                + ConstantTransactionAnalytics.EventLabel.SEPARATOR + shippingPriceHigh)
        sendGeneralEvent(
                ConstantTransactionAnalytics.EventName.CLICK_COURIER,
                ConstantTransactionAnalytics.EventCategory.COURIER_SELECTION,
                ConstantTransactionAnalytics.EventAction.CLICK_CHECKLIST_PILIH_DURASI_PENGIRIMAN,
                eventLabel
        )
    }

    fun eventClickChangeCourierOption(isCourierPromo: Boolean, shippingProductId: Int, isCod: Boolean) {
        val eventLabel = ((if (isCourierPromo) ConstantTransactionAnalytics.EventLabel.PROMO else ConstantTransactionAnalytics.EventLabel.NON_PROMO)
                + ConstantTransactionAnalytics.EventLabel.SEPARATOR + shippingProductId
                + ConstantTransactionAnalytics.EventLabel.SEPARATOR + if (isCod) ConstantTransactionAnalytics.EventLabel.COD else "")
        sendGeneralEvent(
                ConstantTransactionAnalytics.EventName.CLICK_COURIER,
                ConstantTransactionAnalytics.EventCategory.COURIER_SELECTION,
                ConstantTransactionAnalytics.EventAction.CLICK_CHANGE_COURIER_OPTION,
                eventLabel
        )
    }

    //on success use merchant voucher from list
    fun eventClickUseMerchantVoucherSuccess(promoCode: String, promoId: String, isFromList: Boolean) {
        val eventAction = if (isFromList) ConstantTransactionAnalytics.EventAction.CLICK_GUNAKAN_ON_MERCHANT_VOUCHER_FROM_PILIH_MERCHANT_VOUCHER else ConstantTransactionAnalytics.EventAction.CLICK_GUNAKAN_FROM_PILIH_MERCHANT_VOUCHER
        val eventMap = getGtmData(
                ConstantTransactionAnalytics.EventName.CLICK_COURIER,
                ConstantTransactionAnalytics.EventCategory.COURIER_SELECTION,
                eventAction,
                ConstantTransactionAnalytics.EventLabel.SUCCESS + " - " + promoCode
        )
        eventMap[ConstantTransactionAnalytics.Key.PROMO_ID] = promoId
        TrackApp.getInstance().gtm.sendGeneralEvent(eventMap)
    }

    //impression on user merchant voucher list
    fun eventImpressionUseMerchantVoucher(voucherId: String, ecommerceMap: Map<String, Any>) {
        val eventMap = getGtmData(
                ConstantTransactionAnalytics.EventName.PROMO_VIEW,
                ConstantTransactionAnalytics.EventCategory.COURIER_SELECTION,
                ConstantTransactionAnalytics.EventAction.IMPRESSION_MERCHANT_VOUCHER_FROM_PILIH_MERCHANT_VOUCHER,
                ""
        )
        eventMap[ConstantTransactionAnalytics.Key.PROMO_ID] = voucherId
        eventMap[ConstantTransactionAnalytics.Key.E_COMMERCE] = ecommerceMap
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(eventMap)
    }

    //on merchant voucher click detail
    fun eventClickDetailMerchantVoucher(ecommerceMap: Map<String, Any>, voucherId: String, promoCode: String) {
        val eventMap = getGtmData(
                ConstantTransactionAnalytics.EventName.PROMO_CLICK,
                ConstantTransactionAnalytics.EventCategory.COURIER_SELECTION,
                ConstantTransactionAnalytics.EventAction.CLICK_MERCHANT_VOUCHER_FROM_PILIH_MERCHANT_VOUCHER,
                promoCode
        )
        eventMap[ConstantTransactionAnalytics.Key.PROMO_ID] = voucherId
        eventMap[ConstantTransactionAnalytics.Key.E_COMMERCE] = ecommerceMap
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(eventMap)
    }

    //on error use merchant voucher
    fun eventClickUseMerchantVoucherError(errorMsg: String?, promoId: String, isFromList: Boolean) {
        val eventAction = if (isFromList) ConstantTransactionAnalytics.EventAction.CLICK_GUNAKAN_ON_MERCHANT_VOUCHER_FROM_PILIH_MERCHANT_VOUCHER else ConstantTransactionAnalytics.EventAction.CLICK_GUNAKAN_FROM_PILIH_MERCHANT_VOUCHER
        val eventMap = getGtmData(
                ConstantTransactionAnalytics.EventName.CLICK_COURIER,
                ConstantTransactionAnalytics.EventCategory.COURIER_SELECTION,
                eventAction,
                ConstantTransactionAnalytics.EventLabel.ERROR + " - " + errorMsg
        )
        eventMap[ConstantTransactionAnalytics.Key.PROMO_ID] = promoId
        TrackApp.getInstance().gtm.sendGeneralEvent(eventMap)
    }

    fun eventCancelPromoStackingLogistic() {
        sendGeneralEvent(
                "",
                ConstantTransactionAnalytics.EventCategory.COURIER_SELECTION,
                ConstantTransactionAnalytics.EventAction.CLICK_X_ON_PROMO_STACKING_LOGISTIC
        )
    }

    // Logistic Promo
    fun eventClickPromoLogisticTicker(promoCode: String?) {
        sendEventCategoryActionLabel(
                ConstantTransactionAnalytics.EventName.CLICK_COURIER,
                ConstantTransactionAnalytics.EventCategory.COURIER_SELECTION,
                ConstantTransactionAnalytics.EventAction.CLICK_PROMO_LOGISTIC_TICKER,
                promoCode
        )
    }

    fun eventClickLanjutkanTerapkanPromoError(errorMsg: String?) {
        val finalErrorMsg = errorMsg ?: ""
        val label = ConstantTransactionAnalytics.EventLabel.ERROR + " - " + finalErrorMsg
        sendGeneralEvent(
                ConstantTransactionAnalytics.EventName.CLICK_COURIER,
                ConstantTransactionAnalytics.EventCategory.COURIER_SELECTION,
                ConstantTransactionAnalytics.EventAction.CLICK_LANJUTKAN_TERAPKAN_PROMO,
                label
        )
    }

    // Promo not eligible bottomsheet
    fun eventClickLanjutkanOnErrorPromoConfirmation() {
        sendGeneralEvent(
                ConstantTransactionAnalytics.EventName.CLICK_COURIER,
                ConstantTransactionAnalytics.EventCategory.COURIER_SELECTION,
                ConstantTransactionAnalytics.EventAction.CLICK_LANJUTKAN_ON_ERROR_PROMO_CONFIRMATION
        )
    }

    fun eventClickBatalOnErrorPromoConfirmation() {
        sendGeneralEvent(
                ConstantTransactionAnalytics.EventName.CLICK_COURIER,
                ConstantTransactionAnalytics.EventCategory.COURIER_SELECTION,
                ConstantTransactionAnalytics.EventAction.CLICK_BATAL_ON_ERROR_PROMO_CONFIRMATION
        )
    }

    fun eventViewPopupErrorPromoConfirmation() {
        sendGeneralEvent(
                ConstantTransactionAnalytics.EventName.VIEW_COURIER,
                ConstantTransactionAnalytics.EventCategory.COURIER_SELECTION,
                ConstantTransactionAnalytics.EventAction.VIEW_POP_UP_ERROR_PROMO_CONFIRMATION
        )
    }

    fun eventViewPromoLogisticTicker(promoCode: String) {
        sendGeneralEvent(
                ConstantTransactionAnalytics.EventName.VIEW_COURIER,
                ConstantTransactionAnalytics.EventCategory.COURIER_SELECTION,
                ConstantTransactionAnalytics.EventAction.VIEW_PROMO_LOGISTIC_TICKER,
                promoCode
        )
    }

    fun eventViewCourierImpressionErrorCourierNoAvailable() {
        sendGeneralEvent(
                ConstantTransactionAnalytics.EventName.VIEW_COURIER,
                ConstantTransactionAnalytics.EventCategory.COURIER_SELECTION,
                ConstantTransactionAnalytics.EventAction.IMPRESSION_ERROR_COURIER_NO_AVAILABLE
        )
    }

    fun eventViewInformationAndWarningTickerInCheckout(tickerId: String) {
        sendGeneralEvent(
                ConstantTransactionAnalytics.EventName.VIEW_COURIER_IRIS,
                ConstantTransactionAnalytics.EventCategory.COURIER_SELECTION,
                ConstantTransactionAnalytics.EventAction.VIEW_INFORMATION_AND_WARNING_TICKER_IN_CHECKOUT,
                tickerId
        )
    }

    fun eventViewPromoAfterAdjustItem(msg: String) {
        sendGeneralEvent(
                ConstantTransactionAnalytics.EventName.VIEW_COURIER_IRIS,
                ConstantTransactionAnalytics.EventCategory.COURIER_SELECTION,
                ConstantTransactionAnalytics.EventAction.VIEW_PROMO_MESSAGE,
                msg
        )
    }

    fun eventViewPromoLogisticTickerDisable(promoCode: String) {
        sendGeneralEvent(
                ConstantTransactionAnalytics.EventName.VIEW_COURIER_IRIS,
                ConstantTransactionAnalytics.EventCategory.COURIER_SELECTION,
                ConstantTransactionAnalytics.EventAction.VIEW_PROMO_LOGISTIC_TICKER_DISABLE,
                promoCode
        )
    }

    fun eventViewPopupPriceIncrease(eventLabel: String) {
        sendGeneralEvent(ConstantTransactionAnalytics.EventName.VIEW_COURIER_IRIS,
                ConstantTransactionAnalytics.EventCategory.COURIER_SELECTION,
                ConstantTransactionAnalytics.EventAction.VIEW_POP_UP_PRICE_INCREASE,
                eventLabel)
    }

    fun eventClickCheckboxDonation(check: Boolean) {
        sendGeneralEvent(
                ConstantTransactionAnalytics.EventName.CLICK_COURIER,
                ConstantTransactionAnalytics.EventCategory.COURIER_SELECTION,
                ConstantTransactionAnalytics.EventAction.CLICK_DONATION,
                if (check) "check" else "uncheck"
        )
    }

    fun eventViewAutoCheckDonation(userId: String) {
        val gtmMap = getGtmData(
                ConstantTransactionAnalytics.EventName.VIEW_COURIER_IRIS,
                ConstantTransactionAnalytics.EventCategory.COURIER_SELECTION,
                ConstantTransactionAnalytics.EventAction.VIEW_AUTO_CHECK_ON_DONATION,
                ""
        )
        gtmMap[ExtraKey.USER_ID] = userId
        sendGeneralEvent(gtmMap)
    }

    fun eventViewAutoCheckCrossSell(userId: String, position: String, eventLabel: String, digitalProductName: String, childCategoryIds: ArrayList<Long>) {
        val listPromotions: MutableList<Map<String, Any?>> = ArrayList()
        for (childCategoryId in childCategoryIds) {
            listPromotions.add(mapOf(
                    ConstantTransactionAnalytics.Key.CREATIVE to digitalProductName,
                    ConstantTransactionAnalytics.Key.ID to "",
                    ConstantTransactionAnalytics.Key.NAME to childCategoryId.toString(),
                    ConstantTransactionAnalytics.Key.POSITION to position)
            )
        }
        val data = getGtmData(
                ConstantTransactionAnalytics.EventName.PROMO_VIEW,
                ConstantTransactionAnalytics.EventCategory.COURIER_SELECTION,
                ConstantTransactionAnalytics.EventAction.IMPRESSION_CROSS_SELL_ICON,
                eventLabel)
        data[ExtraKey.BUSINESS_UNIT] = ConstantTransactionAnalytics.EventCategory.BU_RECHARGE
        data[ExtraKey.CURRENT_SITE] = ConstantTransactionAnalytics.CustomDimension.DIMENSION_CURRENT_SITE_MARKETPLACE
        data[ExtraKey.USER_ID] = userId
        data[ConstantTransactionAnalytics.Key.E_COMMERCE] = mapOf(
                ConstantTransactionAnalytics.EventName.PROMO_VIEW to mapOf<String, Any?>(
                        ConstantTransactionAnalytics.Key.PROMOTIONS to listPromotions
                )
        )
        sendEnhancedEcommerce(data)
    }

    fun eventClickCheckboxCrossSell(check: Boolean, userId: String, position: String, eventLabel: String, digitalProductName: String, childCategoryIds: ArrayList<Long>) {
        val listPromotions: MutableList<Map<String, Any?>> = ArrayList()
        for (childCategoryId in childCategoryIds) {
            listPromotions.add(mapOf(
                    ConstantTransactionAnalytics.Key.CREATIVE to digitalProductName,
                    ConstantTransactionAnalytics.Key.ID to "",
                    ConstantTransactionAnalytics.Key.NAME to childCategoryId.toString(),
                    ConstantTransactionAnalytics.Key.POSITION to position)
            )
        }
        val data = getGtmData(
                ConstantTransactionAnalytics.EventName.PROMO_CLICK,
                ConstantTransactionAnalytics.EventCategory.COURIER_SELECTION,
                if (check) ConstantTransactionAnalytics.EventAction.CHECK_CROSS_SELL_ICON else ConstantTransactionAnalytics.EventAction.UNCHECK_CROSS_SELL_ICON,
                eventLabel)
        data[ExtraKey.BUSINESS_UNIT] = ConstantTransactionAnalytics.EventCategory.BU_RECHARGE
        data[ExtraKey.CURRENT_SITE] = ConstantTransactionAnalytics.CustomDimension.DIMENSION_CURRENT_SITE_MARKETPLACE
        data[ExtraKey.USER_ID] = userId
        data[ConstantTransactionAnalytics.Key.E_COMMERCE] = mapOf(
                ConstantTransactionAnalytics.EventName.PROMO_CLICK to mapOf<String, Any?>(
                        ConstantTransactionAnalytics.Key.PROMOTIONS to listPromotions
                )
        )
        sendEnhancedEcommerce(data)
    }

    fun sendCrossSellClickPilihPembayaran(eventLabel: String, userId: String, listProducts: List<Any?>?) {
        val data = getGtmData(
                ConstantTransactionAnalytics.EventName.CHECKOUT,
                ConstantTransactionAnalytics.EventCategory.COURIER_SELECTION,
                ConstantTransactionAnalytics.EventAction.CLICK_PILIH_METODE_PEMBAYARAN_CROSS_SELL,
                eventLabel)
        data[ExtraKey.BUSINESS_UNIT] = ConstantTransactionAnalytics.EventCategory.BU_RECHARGE
        data[ExtraKey.CURRENT_SITE] = ConstantTransactionAnalytics.CustomDimension.DIMENSION_CURRENT_SITE_MARKETPLACE
        data[ExtraKey.USER_ID] = userId
        data[ConstantTransactionAnalytics.Key.E_COMMERCE] = mapOf(
                ConstantTransactionAnalytics.EventName.CHECKOUT to mapOf(
                        ConstantTransactionAnalytics.EventCategory.ACTION_FIELD to mapOf<String, Any?>(
                                ConstantTransactionAnalytics.EventCategory.OPTION to ConstantTransactionAnalytics.EventAction.CLICK_PAYMENT_OPTION_BUTTON,
                                ConstantTransactionAnalytics.EventCategory.STEP to "4"
                        ),
                        ConstantTransactionAnalytics.EventCategory.PRODUCTS to listProducts
                )
        )
        sendEnhancedEcommerce(data)
    }

    fun eventViewCampaignDialog(productId: Long, userId: String) {
        val gtmMap = getGtmData(
                ConstantTransactionAnalytics.EventName.VIEW_COURIER_IRIS,
                ConstantTransactionAnalytics.EventCategory.COURIER_SELECTION,
                ConstantTransactionAnalytics.EventAction.VIEW_POP_UP_MESSAGE_TIMER, productId.toString())
        gtmMap[ExtraKey.USER_ID] = userId
        sendGeneralEvent(gtmMap)
    }

    fun eventClickBelanjaLagiOnDialog(productId: Long, userId: String) {
        val gtmMap = getGtmData(
                ConstantTransactionAnalytics.EventName.VIEW_COURIER_IRIS,
                ConstantTransactionAnalytics.EventCategory.COURIER_SELECTION,
                ConstantTransactionAnalytics.EventAction.CLICK_BELANJA_LAGI_ON_POP_UP, productId.toString())
        gtmMap[ExtraKey.USER_ID] = userId
        sendGeneralEvent(gtmMap)
    }

    fun eventViewSummaryTransactionTickerCourierNotComplete(userId: String) {
        val gtmMap = getGtmData(
                ConstantTransactionAnalytics.EventName.VIEW_COURIER_IRIS,
                ConstantTransactionAnalytics.EventCategory.COURIER_SELECTION,
                ConstantTransactionAnalytics.EventAction.VIEW_SUMMARY_TRANSACTION_TICKER_COURIER_NOT_COMPLETE,
                ""
        )
        gtmMap[ExtraKey.USER_ID] = userId
        sendGeneralEvent(gtmMap)
    }

    fun clickCekOnSummaryTransactionTickerCourierNotComplete(userId: String) {
        val gtmMap = getGtmData(
                ConstantTransactionAnalytics.EventName.CLICK_COURIER,
                ConstantTransactionAnalytics.EventCategory.COURIER_SELECTION,
                ConstantTransactionAnalytics.EventAction.CLICK_CEK_ON_SUMMARY_TRANSACTION_TICKER_COURIER_NOT_COMPLETE,
                ""
        )
        gtmMap[ExtraKey.USER_ID] = userId
        sendGeneralEvent(gtmMap)
    }

    // TokoNow
    fun eventViewTickerProductLevelErrorInCheckoutPage(shopId: String, errorMessage: String) {
        val gtmMap = getGtmData(
                ConstantTransactionAnalytics.EventName.VIEW_COURIER_IRIS,
                ConstantTransactionAnalytics.EventCategory.COURIER_SELECTION,
                ConstantTransactionAnalytics.EventAction.VIEW_TICKER_PRODUCT_LEVEL_ERROR_IN_CHECKOUT_PAGE,
                "$shopId - $errorMessage"
        )
        gtmMap[ExtraKey.BUSINESS_UNIT] = ConstantTransactionAnalytics.CustomDimension.DIMENSION_BUSINESS_UNIT_PURCHASE_PLATFORM
        gtmMap[ExtraKey.CURRENT_SITE] = ConstantTransactionAnalytics.CustomDimension.DIMENSION_CURRENT_SITE_MARKETPLACE
        sendGeneralEvent(gtmMap)
    }

    fun eventViewTickerOrderLevelErrorInCheckoutPage(shopId: String, errorMessage: String) {
        val gtmMap = getGtmData(
                ConstantTransactionAnalytics.EventName.VIEW_COURIER_IRIS,
                ConstantTransactionAnalytics.EventCategory.COURIER_SELECTION,
                ConstantTransactionAnalytics.EventAction.VIEW_TICKER_ORDER_LEVEL_ERROR_IN_CHECKOUT_PAGE,
                "$shopId - $errorMessage"
        )
        gtmMap[ExtraKey.BUSINESS_UNIT] = ConstantTransactionAnalytics.CustomDimension.DIMENSION_BUSINESS_UNIT_PURCHASE_PLATFORM
        gtmMap[ExtraKey.CURRENT_SITE] = ConstantTransactionAnalytics.CustomDimension.DIMENSION_CURRENT_SITE_MARKETPLACE
        sendGeneralEvent(gtmMap)
    }

    fun eventViewTickerPaymentLevelErrorInCheckoutPage(shopId: String, errorMessage: String) {
        val gtmMap = getGtmData(
                ConstantTransactionAnalytics.EventName.CLICK_COURIER,
                ConstantTransactionAnalytics.EventCategory.COURIER_SELECTION,
                ConstantTransactionAnalytics.EventAction.VIEW_TICKER_PAYMENT_LEVEL_ERROR_IN_CHECKOUT_PAGE,
                "$shopId - $errorMessage"
        )
        gtmMap[ExtraKey.BUSINESS_UNIT] = ConstantTransactionAnalytics.CustomDimension.DIMENSION_BUSINESS_UNIT_PURCHASE_PLATFORM
        gtmMap[ExtraKey.CURRENT_SITE] = ConstantTransactionAnalytics.CustomDimension.DIMENSION_CURRENT_SITE_MARKETPLACE
        sendGeneralEvent(gtmMap)
    }

    fun eventClickLihatOnTickerErrorOrderLevelErrorInCheckoutPage(shopId: String, errorMessage: String) {
        val gtmMap = getGtmData(
                ConstantTransactionAnalytics.EventName.CLICK_COURIER,
                ConstantTransactionAnalytics.EventCategory.COURIER_SELECTION,
                ConstantTransactionAnalytics.EventAction.CLICK_LIHAT_ON_TICKER_ORDER_LEVEL_ERROR_IN_CHECKOUT_PAGE,
                "$shopId - $errorMessage"
        )
        gtmMap[ExtraKey.BUSINESS_UNIT] = ConstantTransactionAnalytics.CustomDimension.DIMENSION_BUSINESS_UNIT_PURCHASE_PLATFORM
        gtmMap[ExtraKey.CURRENT_SITE] = ConstantTransactionAnalytics.CustomDimension.DIMENSION_CURRENT_SITE_MARKETPLACE
        sendGeneralEvent(gtmMap)
    }

    fun eventClickRefreshWhenErrorLoadCourier() {
        val gtmMap = getGtmData(
                ConstantTransactionAnalytics.EventName.VIEW_COURIER_IRIS,
                ConstantTransactionAnalytics.EventCategory.COURIER_SELECTION,
                ConstantTransactionAnalytics.EventAction.CLICK_REFRESH_WHEN_ERROR_LOAD_COURIER,
                ""
        )
        gtmMap[ExtraKey.BUSINESS_UNIT] = ConstantTransactionAnalytics.CustomDimension.DIMENSION_BUSINESS_UNIT_PURCHASE_PLATFORM
        gtmMap[ExtraKey.CURRENT_SITE] = ConstantTransactionAnalytics.CustomDimension.DIMENSION_CURRENT_SITE_MARKETPLACE
        sendGeneralEvent(gtmMap)
    }

    fun eventViewErrorInCourierSection(errorMessage: String) {
        val gtmMap = getGtmData(
                ConstantTransactionAnalytics.EventName.VIEW_COURIER_IRIS,
                ConstantTransactionAnalytics.EventCategory.COURIER_SELECTION,
                ConstantTransactionAnalytics.EventAction.VIEW_ERROR_IN_COURIER_SECTION,
                errorMessage
        )
        gtmMap[ExtraKey.BUSINESS_UNIT] = ConstantTransactionAnalytics.CustomDimension.DIMENSION_BUSINESS_UNIT_PURCHASE_PLATFORM
        gtmMap[ExtraKey.CURRENT_SITE] = ConstantTransactionAnalytics.CustomDimension.DIMENSION_CURRENT_SITE_MARKETPLACE
        sendGeneralEvent(gtmMap)
    }

    // gifting
    // tracker id : 28312
    fun eventViewAddOnsWidget(productId: String) {
        val gtmData = getGtmData(
                ConstantTransactionAnalytics.EventName.VIEW_PP_IRIS,
                ConstantTransactionAnalytics.EventCategory.COURIER_SELECTION,
                ConstantTransactionAnalytics.EventAction.VIEW_ADD_ONS_WIDGET,
                productId
        )
        gtmData[ExtraKey.CURRENT_SITE] = ConstantTransactionAnalytics.CustomDimension.DIMENSION_CURRENT_SITE_MARKETPLACE
        gtmData[ExtraKey.BUSINESS_UNIT] = ConstantTransactionAnalytics.CustomDimension.DIMENSION_BUSINESS_UNIT_PURCHASE_PLATFORM
        sendGeneralEvent(gtmData)
    }

    // tracker id : 28313
    fun eventClickAddOnsWidget(productId: String) {
        val gtmData = getGtmData(
                ConstantTransactionAnalytics.EventName.CLICK_PP,
                ConstantTransactionAnalytics.EventCategory.COURIER_SELECTION,
                ConstantTransactionAnalytics.EventAction.CLICK_ADD_ONS_WIDGET,
                productId
        )
        gtmData[ExtraKey.CURRENT_SITE] = ConstantTransactionAnalytics.CustomDimension.DIMENSION_CURRENT_SITE_MARKETPLACE
        gtmData[ExtraKey.BUSINESS_UNIT] = ConstantTransactionAnalytics.CustomDimension.DIMENSION_BUSINESS_UNIT_PURCHASE_PLATFORM
        sendGeneralEvent(gtmData)
    }

    // tracker id : 28314
    fun eventSaveAddOnsBottomSheet(isChecked: Boolean, source: String) {
        val label = if (isChecked) ConstantTransactionAnalytics.EventLabel.ADD_ON_CHECKED
        else ConstantTransactionAnalytics.EventLabel.ADD_ON_NOT_CHECKED
        val gtmData = getGtmData(
                ConstantTransactionAnalytics.EventName.CLICK_PP,
                source,
                ConstantTransactionAnalytics.EventAction.CLICK_SIMPAN_ON_ADD_ONS_BOTTOMSHEET,
                label
        )
        gtmData[ExtraKey.CURRENT_SITE] = ConstantTransactionAnalytics.CustomDimension.DIMENSION_CURRENT_SITE_MARKETPLACE
        gtmData[ExtraKey.BUSINESS_UNIT] = ConstantTransactionAnalytics.CustomDimension.DIMENSION_BUSINESS_UNIT_PURCHASE_PLATFORM
        sendGeneralEvent(gtmData)
    }
}