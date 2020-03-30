package com.tokopedia.updateinactivephone.common.analytics

import android.text.TextUtils
import com.tokopedia.track.TrackApp
import com.tokopedia.track.TrackAppUtils
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

class UpdateInactivePhoneAnalytics @Inject constructor(private val userSessionInterface: UserSessionInterface) {

    fun eventInactivePhoneClick() {
        val gtmData = TrackAppUtils.gtmData(
                UpdateInactivePhoneEventConstants.Event.INACTIVE_PHONE_CLICK,
                UpdateInactivePhoneEventConstants.Category.INACTIVE_PHONE_PAGE,
                UpdateInactivePhoneEventConstants.Action.CLICK_SELUNJANTYA,
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
                UpdateInactivePhoneEventConstants.Event.INACTIVE_PHONE_CLICK,
                UpdateInactivePhoneEventConstants.Category.CHANGE_NOT_ACTIVE_PHONE,
                UpdateInactivePhoneEventConstants.Action.CLICK_MASUK,
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
                UpdateInactivePhoneEventConstants.Event.INACTIVE_PHONE_CLICK,
                UpdateInactivePhoneEventConstants.Category.INACTIVE_PHONE_PAGE,
                UpdateInactivePhoneEventConstants.Action.CLICK_BATAL,
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
                UpdateInactivePhoneEventConstants.Event.INACTIVE_PHONE_VIEW,
                UpdateInactivePhoneEventConstants.Category.WAITING_CONFIRMATION_PAGE,
                UpdateInactivePhoneEventConstants.Action.VIEW_ON_WAITING_CONFIRMATION_APGE,
                UpdateInactivePhoneEventConstants.EventLabel.CHANGE_NOT_ACTIVE_PHONE
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
                UpdateInactivePhoneEventConstants.Event.INACTIVE_PHONE_VIEW,
                UpdateInactivePhoneEventConstants.Category.SUBMIT_SUCCESS_PAGE,
                UpdateInactivePhoneEventConstants.Action.VIEW_ON_SUBMIT_SUCCESS_PAGE,
                UpdateInactivePhoneEventConstants.EventLabel.CHANGE_NOT_ACTIVE_PHONE
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
                UpdateInactivePhoneEventConstants.Event.INACTIVE_PHONE_CLICK,
                UpdateInactivePhoneEventConstants.Category.CHANGE_NOT_ACTIVE_PHONE,
                UpdateInactivePhoneEventConstants.Action.CLICK_KIRIM_PENGAJUAN,
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
                UpdateInactivePhoneEventConstants.Event.INACTIVE_PHONE_VIEW,
                UpdateInactivePhoneEventConstants.Category.INPUT_ID_AND_BANK_ACCOUNT,
                UpdateInactivePhoneEventConstants.Action.VIEW_KIRIM_PENGAJUAN,
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
                UpdateInactivePhoneEventConstants.Event.INACTIVE_PHONE_VIEW,
                UpdateInactivePhoneEventConstants.Category.INPUT_ID_AND_BANK_ACCOUNT,
                UpdateInactivePhoneEventConstants.Action.VIEW_ON_LANJUTKAN,
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
                UpdateInactivePhoneEventConstants.Event.INACTIVE_PHONE_CLICK,
                UpdateInactivePhoneEventConstants.Category.INPUT_ID_AND_BANK_ACCOUNT,
                UpdateInactivePhoneEventConstants.Action.CLICK_ON_LANJUTKAN,
                ""
        )

        if(TextUtils.isEmpty(userSessionInterface.userId)) {
            gtmData[USER_ID] = "0"
        } else {
            gtmData[USER_ID] = userSessionInterface.userId
        }

        TrackApp.getInstance().gtm.sendGeneralEvent(gtmData)
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