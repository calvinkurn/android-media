package com.tokopedia.updateinactivephone.common.analytics

import android.text.TextUtils
import com.tokopedia.track.TrackApp
import com.tokopedia.track.TrackAppUtils
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

class UpdateInactivePhoneAnalytics @Inject constructor(private val userSessionInterface: UserSessionInterface) {

    fun eventInactivePhoneClick() {
        val gtmData = TrackAppUtils.gtmData(
                UpdateInactivePhoneEventConstants.INACTIVE_PHONE_CLICK,
                UpdateInactivePhoneEventConstants.INACTIVE_PHONE_PAGE,
                UpdateInactivePhoneEventConstants.CLICK_SELUNJANTYA,
                ""
        )

        if(TextUtils.isEmpty(userSessionInterface.userId)) {
            gtmData[USER_ID] = "0"
        } else {
            gtmData[USER_ID] = userSessionInterface.userId
        }

        TrackApp.getInstance().gtm.sendGeneralEvent(gtmData)
    }

    fun eventLoginDialogClick() {
        val gtmData = TrackAppUtils.gtmData(
                UpdateInactivePhoneEventConstants.INACTIVE_PHONE_CLICK,
                UpdateInactivePhoneEventConstants.CHANGE_NOT_ACTIVE_PHONE,
                UpdateInactivePhoneEventConstants.CLICK_MASUK,
                ""
        )

        if(TextUtils.isEmpty(userSessionInterface.userId)) {
            gtmData[USER_ID] = "0"
        } else {
            gtmData[USER_ID] = userSessionInterface.userId
        }

        TrackApp.getInstance().gtm.sendGeneralEvent(gtmData)
    }

    fun eventCancelDialogClick() {
        val gtmData = TrackAppUtils.gtmData(
                UpdateInactivePhoneEventConstants.INACTIVE_PHONE_CLICK,
                UpdateInactivePhoneEventConstants.INACTIVE_PHONE_PAGE,
                UpdateInactivePhoneEventConstants.CLICK_BATAL,
                ""
        )

        if(TextUtils.isEmpty(userSessionInterface.userId)) {
            gtmData[USER_ID] = "0"
        } else {
            gtmData[USER_ID] = userSessionInterface.userId
        }

        TrackApp.getInstance().gtm.sendGeneralEvent(gtmData)
    }

    fun eventViewWaitingForConfirmationPage() {
        val gtmData = TrackAppUtils.gtmData(
                UpdateInactivePhoneEventConstants.INACTIVE_PHONE_VIEW,
                UpdateInactivePhoneEventConstants.WAITING_CONFIRMATION_PAGE,
                UpdateInactivePhoneEventConstants.VIEW_ON_WAITING_CONFIRMATION_APGE,
                UpdateInactivePhoneEventConstants.CHANGE_NOT_ACTIVE_PHONE
        )

        if(TextUtils.isEmpty(userSessionInterface.userId)) {
            gtmData[USER_ID] = "0"
        } else {
            gtmData[USER_ID] = userSessionInterface.userId
        }

        TrackApp.getInstance().gtm.sendGeneralEvent(gtmData)
    }

    fun eventViewSubmitSuccessPage() {
        val gtmData = TrackAppUtils.gtmData(
                UpdateInactivePhoneEventConstants.INACTIVE_PHONE_VIEW,
                UpdateInactivePhoneEventConstants.SUBMIT_SUCCESS_PAGE,
                UpdateInactivePhoneEventConstants.VIEW_ON_SUBMIT_SUCCESS_PAGE,
                UpdateInactivePhoneEventConstants.CHANGE_NOT_ACTIVE_PHONE
        )

        if(TextUtils.isEmpty(userSessionInterface.userId)) {
            gtmData[USER_ID] = "0"
        } else {
            gtmData[USER_ID] = userSessionInterface.userId
        }

        TrackApp.getInstance().gtm.sendGeneralEvent(gtmData)
    }

    fun eventClickKirimPengajuan() {
        val gtmData = TrackAppUtils.gtmData(
                UpdateInactivePhoneEventConstants.INACTIVE_PHONE_CLICK,
                UpdateInactivePhoneEventConstants.CHANGE_NOT_ACTIVE_PHONE,
                UpdateInactivePhoneEventConstants.CLICK_KIRIM_PENGAJUAN,
                ""
        )

        if(TextUtils.isEmpty(userSessionInterface.userId)) {
            gtmData[USER_ID] = "0"
        } else {
            gtmData[USER_ID] = userSessionInterface.userId
        }

        TrackApp.getInstance().gtm.sendGeneralEvent(gtmData)
    }

    fun eventViewKirimPengajuan() {
        val gtmData = TrackAppUtils.gtmData(
                UpdateInactivePhoneEventConstants.INACTIVE_PHONE_VIEW,
                UpdateInactivePhoneEventConstants.INPUT_ID_AND_BANK_ACCOUNT,
                UpdateInactivePhoneEventConstants.VIEW_KIRIM_PENGAJUAN,
                ""
        )

        if(TextUtils.isEmpty(userSessionInterface.userId)) {
            gtmData[USER_ID] = "0"
        } else {
            gtmData[USER_ID] = userSessionInterface.userId
        }

        TrackApp.getInstance().gtm.sendGeneralEvent(gtmData)
    }

    fun eventViewPhotoUploadScreen() {
        val gtmData = TrackAppUtils.gtmData(
                UpdateInactivePhoneEventConstants.INACTIVE_PHONE_VIEW,
                UpdateInactivePhoneEventConstants.INPUT_ID_AND_BANK_ACCOUNT,
                UpdateInactivePhoneEventConstants.VIEW_ON_LANJUTKAN,
                ""
        )

        if(TextUtils.isEmpty(userSessionInterface.userId)) {
            gtmData[USER_ID] = "0"
        } else {
            gtmData[USER_ID] = userSessionInterface.userId
        }

        TrackApp.getInstance().gtm.sendGeneralEvent(gtmData)
    }

    fun eventClickPhotoProceed() {
        val gtmData = TrackAppUtils.gtmData(
                UpdateInactivePhoneEventConstants.INACTIVE_PHONE_CLICK,
                UpdateInactivePhoneEventConstants.INPUT_ID_AND_BANK_ACCOUNT,
                UpdateInactivePhoneEventConstants.CLICK_ON_LANJUTKAN,
                ""
        )

        if(TextUtils.isEmpty(userSessionInterface.userId)) {
            gtmData[USER_ID] = "0"
        } else {
            gtmData[USER_ID] = userSessionInterface.userId
        }

        TrackApp.getInstance().gtm.sendGeneralEvent(gtmData)
    }

    fun eventClickButtonNext() {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                UpdateInactivePhoneEventConstants.EVENT_CLICK_OTP,
                UpdateInactivePhoneEventConstants.CATEGORY_INACTIVE_PHONE,
                UpdateInactivePhoneEventConstants.CLICK_BUTTON_SELUNJANTYA,
                UpdateInactivePhoneEventConstants.LABEL_CLICK
        ))
    }

    fun eventSuccessClickButtonNext() {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                UpdateInactivePhoneEventConstants.EVENT_CLICK_OTP,
                UpdateInactivePhoneEventConstants.CATEGORY_INACTIVE_PHONE,
                UpdateInactivePhoneEventConstants.CLICK_BUTTON_SELUNJANTYA,
                UpdateInactivePhoneEventConstants.LABEL_SUCCESS
        ))
    }

    fun eventFailedClickButtonNext(message: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                UpdateInactivePhoneEventConstants.EVENT_CLICK_OTP,
                UpdateInactivePhoneEventConstants.CATEGORY_INACTIVE_PHONE,
                UpdateInactivePhoneEventConstants.CLICK_BUTTON_SELUNJANTYA,
                UpdateInactivePhoneEventConstants.LABEL_FAILED + message
        ))
    }

    fun eventClickButtonSubmission() {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                UpdateInactivePhoneEventConstants.EVENT_CLICK_OTP,
                UpdateInactivePhoneEventConstants.CATEGORY_INACTIVE_PHONE,
                UpdateInactivePhoneEventConstants.CLICK_BUTTON_KIRIM_PENGAJUAN,
                UpdateInactivePhoneEventConstants.LABEL_CLICK
        ))
    }

    fun eventSuccessClickButtonSubmission() {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                UpdateInactivePhoneEventConstants.EVENT_CLICK_OTP,
                UpdateInactivePhoneEventConstants.CATEGORY_INACTIVE_PHONE,
                UpdateInactivePhoneEventConstants.CLICK_BUTTON_KIRIM_PENGAJUAN,
                UpdateInactivePhoneEventConstants.LABEL_SUCCESS
        ))
    }

    fun eventFailedClickButtonSubmission(message: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                UpdateInactivePhoneEventConstants.EVENT_CLICK_OTP,
                UpdateInactivePhoneEventConstants.CATEGORY_INACTIVE_PHONE,
                UpdateInactivePhoneEventConstants.CLICK_BUTTON_KIRIM_PENGAJUAN,
                UpdateInactivePhoneEventConstants.LABEL_FAILED + message
        ))
    }

    fun eventClickButtonMainPage() {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                UpdateInactivePhoneEventConstants.EVENT_CLICK_OTP,
                UpdateInactivePhoneEventConstants.CATEGORY_INACTIVE_PHONE,
                UpdateInactivePhoneEventConstants.CLICK_BUTTON_KEMBALI_HALAMAN_UTAMA,
                ""
        ))
    }

    fun screen(screen: String?) {
        if (TextUtils.isEmpty(screen)) {
            return
        }
        TrackApp.getInstance().gtm.sendScreenAuthenticated(screen)
    }

    companion object {
        const val USER_ID = "userId"
    }
}