package com.tokopedia.pms.analytics

import com.tokopedia.track.TrackApp
import com.tokopedia.track.TrackAppUtils
import com.tokopedia.track.interfaces.ContextAnalytics
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

class PmsAnalytics @Inject constructor(
    userSession: dagger.Lazy<UserSessionInterface>,
) {
    private val analyticTracker: ContextAnalytics
        get() = TrackApp.getInstance().gtm
    private val userId = userSession.get().userId

    fun sendPmsAnalyticsEvent(event: PmsEvents) {
        when (event) {
            is PmsEvents.ChevronTapClickEvent -> prepareCommonMap(ACTION_TAP_THREE_DOTS)
            is PmsEvents.ShowTransactionDetailEvent -> prepareCommonMap(
                ACTION_CLICK_TRANSACTION_DETAIL
            )
            is PmsEvents.InvokeEditBcaUserIdEvent -> prepareCommonMap(ACTION_CLICK_EDIT_KLIC_BCA)
            is PmsEvents.ConfirmAccountDetailsEvent -> prepareCommonMap(
                ACTION_CLICK_CONFIRM_BANK_DETAIL
            )
            is PmsEvents.UploadPaymentProofEvent -> prepareCommonMap(
                ACTION_CLICK_UPLOAD_PAYMENT_PROOF
            )
            is PmsEvents.SelectImageEvent -> prepareCommonMap(ACTION_CLICK_SELECT_IMAGE)
            is PmsEvents.SelectAnotherImageEvent -> prepareCommonMap(
                ACTION_CLICK_SELECT_ANOTHER_IMAGE
            )
            is PmsEvents.ConfirmSelectedImageEvent -> prepareCommonMap(ACTION_CLICK_CONFIRM_IMAGE)
            is PmsEvents.HowToPayRedirectionEvent -> prepareCommonMap(ACTION_CLICK_HTP_REDIRECTION)
            is PmsEvents.WaitingCardClickEvent -> prepareCommonMap(
                ACTION_CLICK_WAITING_CARD,
                "${LABEL_PAYMENT_CARD_TYPE}: ${event.paymentType}"
            )
            is PmsEvents.InvokeCancelTransactionBottomSheetEvent -> prepareCommonMap(
                ACTION_CLICK_CANCEL_TRANSACTION
            )
            is PmsEvents.ConfirmCancelTransactionEvent -> prepareCommonMap(
                ACTION_CLICK_CONFIRM_CANCEL_TRANSACTION
            )
            is PmsEvents.InvokeCancelTransactionOnDetailEvent -> prepareCommonMap(
                ACTION_CLICK_CANCEL_ON_TRANSACTION_DETAIL
            )
            is PmsEvents.ConfirmEditBcaUserIdEvent -> prepareCommonMap(
                ACTION_CLICK_CONFIRM_EDIT_KLIC_BCA
            )
            is PmsEvents.InvokeChangeAccountDetailsEvent -> prepareCommonMap(
                ACTION_CLICK_CHANGE_BANK_DETAIL
            )
        }
    }

    private fun prepareCommonMap(action: String, label: String? = "") {
        val map = TrackAppUtils.gtmData(
            EVENT_NAME,
            getCategoryName(),
            action,
            label
        )
        sendAnalytics(map)
    }

    private fun sendAnalytics(map: MutableMap<String, Any>) {
        map[KEY_BUSINESS_UNIT] = BUSINESS_UNIT_PAYMENT
        map[KEY_CURRENT_SITE] = CURRENT_SITE_PAYMENT
        map[KEY_USER_ID] = userId
        analyticTracker.sendGeneralEvent(map)
    }

    companion object {
        private fun getCategoryName(): String = "pms page"

        const val KEY_USER_ID = "userId"
        const val KEY_BUSINESS_UNIT = "businessUnit"
        const val KEY_CURRENT_SITE = "currentSite"

        const val BUSINESS_UNIT_PAYMENT = "Payment"
        const val CURRENT_SITE_PAYMENT = "tokopedia"

        const val SCREEN_NAME = "PMS page"
        const val EVENT_NAME = "clickPMS"
        const val ACTION_TAP_THREE_DOTS = "tap three dots"

        const val ACTION_CLICK_CANCEL_TRANSACTION = "click batalkan transaksi"
        const val ACTION_CLICK_CONFIRM_CANCEL_TRANSACTION = "click confirm batalkan transaksi"
        const val ACTION_CLICK_CANCEL_ON_TRANSACTION_DETAIL = "click batalkan on detail"
        const val ACTION_CLICK_TRANSACTION_DETAIL = "click detail on detail"

        const val ACTION_CLICK_UPLOAD_PAYMENT_PROOF = "click ubah bukti pembayaran"

        const val ACTION_CLICK_EDIT_KLIC_BCA = "click ubah user klikbca"
        const val ACTION_CLICK_CONFIRM_EDIT_KLIC_BCA = "click confirm ubah user klikbca"

        const val ACTION_CLICK_CHANGE_BANK_DETAIL = "click ubah detail rekening"
        const val ACTION_CLICK_CONFIRM_BANK_DETAIL = "click confirm ubah detail rekening"

        const val ACTION_CLICK_SELECT_IMAGE = "click pilih gambar"
        const val ACTION_CLICK_SELECT_ANOTHER_IMAGE = "click pilih gambar lain"
        const val ACTION_CLICK_CONFIRM_IMAGE = "click confirm selesai pilih gambar"

        const val ACTION_CLICK_WAITING_CARD = "click payment waiting card"
        const val ACTION_CLICK_HTP_REDIRECTION = "click lihat cara bayar"

        const val LABEL_PAYMENT_CARD_TYPE = "payment cards"
    }
}