package com.tokopedia.pms.analytics

import com.tokopedia.track.TrackApp
import com.tokopedia.track.TrackAppUtils
import com.tokopedia.track.interfaces.ContextAnalytics
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

class PmsAnalytics @Inject constructor(
    private val userSession: dagger.Lazy<UserSessionInterface>,
) {

    private val analyticTracker: ContextAnalytics
        get() = TrackApp.getInstance().gtm

    fun sendPmsAnalyticsEvent(event: PmsEvents) {
        when (event) {
            is PmsEvents.ChevronTapClickEvent -> sendPmsEvent(ACTION_TAP_THREE_DOTS)
            is PmsEvents.ShowTransactionDetailEvent -> sendPmsEvent(ACTION_CLICK_TRANSACTION_DETAIL)
            is PmsEvents.InvokeEditBcaUserIdEvent -> sendPmsEvent(ACTION_CLICK_EDIT_KLIC_BCA)
            is PmsEvents.ConfirmAccountDetailsEvent -> sendPmsEvent(ACTION_CLICK_CONFIRM_BANK_DETAIL)
            is PmsEvents.UploadPaymentProofEvent -> sendPmsEvent(ACTION_CLICK_UPLOAD_PAYMENT_PROOF)
            is PmsEvents.SelectImageEvent -> sendPmsEvent(ACTION_CLICK_SELECT_IMAGE)
            is PmsEvents.SelectAnotherImageEvent -> sendPmsEvent(ACTION_CLICK_SELECT_ANOTHER_IMAGE)
            is PmsEvents.ConfirmSelectedImageEvent -> sendPmsEvent(ACTION_CLICK_CONFIRM_IMAGE)
            is PmsEvents.HowToPayRedirectionEvent -> sendPmsEvent(ACTION_CLICK_HTP_REDIRECTION)
            is PmsEvents.WaitingCardClickEvent -> sendPmsEvent(
                ACTION_CLICK_WAITING_CARD,
                "${LABEL_PAYMENT_CARD_TYPE}: ${event.paymentType}"
            )
            is PmsEvents.DeferredPaymentsShownEvent -> sendPmsEvent(
                ACTION_OPEN_PMS_INITIAL,
                "$LABEL_WAITING_PAYMENT_COUNT: ${event.waitingPaymentCount ?: 0}"

            )
            is PmsEvents.InvokeCancelTransactionBottomSheetEvent -> sendPmsEvent(
                ACTION_CLICK_CANCEL_TRANSACTION
            )
            is PmsEvents.ConfirmCancelTransactionEvent -> sendPmsEvent(
                ACTION_CLICK_CONFIRM_CANCEL_TRANSACTION
            )
            is PmsEvents.InvokeCancelTransactionOnDetailEvent -> sendPmsEvent(
                ACTION_CLICK_CANCEL_ON_TRANSACTION_DETAIL
            )
            is PmsEvents.ConfirmEditBcaUserIdEvent -> sendPmsEvent(
                ACTION_CLICK_CONFIRM_EDIT_KLIC_BCA
            )
            is PmsEvents.InvokeChangeAccountDetailsEvent -> sendPmsEvent(
                ACTION_CLICK_CHANGE_BANK_DETAIL
            )
        }
    }

    private fun sendPmsEvent(action: String, label: String? = "") {
        val map = TrackAppUtils.gtmData(
            EVENT_NAME,
            getCategoryName(),
            action,
            label
        )
        map[KEY_BUSINESS_UNIT] = BUSINESS_UNIT_PAYMENT
        map[KEY_CURRENT_SITE] = CURRENT_SITE_PAYMENT
        map[KEY_USER_ID] = userSession.get().userId
        analyticTracker.sendGeneralEvent(map)
    }

    companion object {
        private fun getCategoryName(): String = "pms page"

        const val KEY_USER_ID = "userId"
        const val KEY_BUSINESS_UNIT = "businessUnit"
        const val KEY_CURRENT_SITE = "currentSite"

        const val BUSINESS_UNIT_PAYMENT = "Payment"
        const val CURRENT_SITE_PAYMENT = "tokopedia"

        const val SCREEN_NAME = "pms page"
        const val EVENT_NAME = "clickPMS"
        const val ACTION_OPEN_PMS_INITIAL = "open pms page"
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
        const val LABEL_WAITING_PAYMENT_COUNT = "number of waiting payment"
    }
}