package com.tokopedia.profilecompletion.addphone.data.analitycs

import com.tokopedia.track.builder.Tracker
import javax.inject.Inject

class NewAddPhoneNumberTracker @Inject constructor() {

    fun sendView2FaMluAddPhoneNumberPageEvent() {
        Tracker.Builder()
            .setEvent("viewAuthIris")
            .setEventAction("view 2fa mlu add phone number page")
            .setEventCategory("2fa mlu add phone number page")
            .setEventLabel("")
            .setBusinessUnit("user account")
            .setCurrentSite("tokopediamarketplace")
            .build()
            .send()
    }

    fun sendClickOnButtonTambahNomorHpEvent(action: String, errorMessage: String = "") {
        Tracker.Builder()
            .setEvent("clickAccount")
            .setEventAction("click on button tambah nomor hp")
            .setEventCategory("2fa mlu add phone number page")
            .setEventLabel(
                getActionLabel(action, errorMessage)
            )
            .setCustomProperty("trackerId", "2697")
            .setBusinessUnit("user account")
            .setCurrentSite("tokopediamarketplace")
            .build()
            .send()
    }

    fun sendClickOnButtonCloseEvent() {
        Tracker.Builder()
            .setEvent("clickAuth")
            .setEventAction("click on button close")
            .setEventCategory("2fa mlu add phone number page")
            .setEventLabel("")
            .setBusinessUnit("user account")
            .setCurrentSite("tokopediamarketplace")
            .build()
            .send()
    }

    private fun getActionLabel(
        action: String,
        errorMessage: String
    ): String {
        return "$action${if (action == ACTION_FAILED) " - $errorMessage" else ""}"
    }

    companion object {
        const val ACTION_CLICK = "click"
        const val ACTION_SUCCESS = "success"
        const val ACTION_FAILED = "failed"
        const val MESSAGE_FAILED_OTP = "gagal verifikasi OTP"
    }

}
