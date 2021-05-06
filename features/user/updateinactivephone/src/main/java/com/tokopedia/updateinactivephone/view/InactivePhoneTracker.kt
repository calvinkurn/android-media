package com.tokopedia.updateinactivephone.view

import com.tokopedia.track.TrackApp

class InactivePhoneTracker {

    private val tracker = TrackApp.getInstance().gtm

    /**
     * Onboarding Page
     */
    fun clickOnNextButtonOnboarding() {
        tracker.sendGeneralEvent(
                EVENT.CLICK_UBAH_NOMOR_HP,
                CATEGORY.UBAH_NOMOR_HP,
                ACTION.CLICK_ON_BUTTON_LANJUT_UBAH,
                LABEL.EMPTY
        )
    }

    fun clickOnBackButtonOnbaording() {
        tracker.sendGeneralEvent(
                EVENT.CLICK_UBAH_NOMOR_HP,
                CATEGORY.UBAH_NOMOR_HP,
                ACTION.CLICK_ON_BUTTON_BACK,
                LABEL.LANJUT_UBAH
        )
    }

    /**
     * Onboarding ID Card
     */
    fun clickOnNextButtonIdCardOnboarding() {
        tracker.sendGeneralEvent(
                EVENT.CLICK_UBAH_NOMOR_HP,
                CATEGORY.UBAH_NOMOR_HP,
                ACTION.CLICK_ON_BUTTON_FOTO_KARTU_IDENTITAS,
                LABEL.EMPTY
        )
    }

    fun clickOnBackButtonIdCardOnboarding() {
        tracker.sendGeneralEvent(
                EVENT.CLICK_UBAH_NOMOR_HP,
                CATEGORY.UBAH_NOMOR_HP,
                ACTION.CLICK_ON_BUTTON_BACK,
                LABEL.FOTO_KARTU_IDENTITAS
        )
    }

    /**
     * Onboarding Selfie
     */
    fun clickOnNextButtonSelfiewOnboarding() {
        tracker.sendGeneralEvent(
                EVENT.CLICK_UBAH_NOMOR_HP,
                CATEGORY.UBAH_NOMOR_HP,
                ACTION.CLICK_ON_BUTTON_MULAI_FOTO,
                LABEL.EMPTY
        )
    }

    fun clickOnBackButtonSelfiewOnboarding() {
        tracker.sendGeneralEvent(
                EVENT.CLICK_UBAH_NOMOR_HP,
                CATEGORY.UBAH_NOMOR_HP,
                ACTION.CLICK_ON_BUTTON_BACK,
                LABEL.MULAI_FOTO
        )
    }

    /**
     * Camera view
     */
    fun clickOnCaptureButtonCameraViewIdCard() {
        tracker.sendGeneralEvent(
                EVENT.CLICK_UBAH_NOMOR_HP,
                CATEGORY.UBAH_NOMOR_HP,
                ACTION.CLICK_ON_BUTTON_CAPTURE_FOTO_KARTU_IDENTITAS,
                LABEL.EMPTY
        )
    }

    fun clickOnBackButtonCameraViewIdCard() {
        tracker.sendGeneralEvent(
                EVENT.CLICK_UBAH_NOMOR_HP,
                CATEGORY.UBAH_NOMOR_HP,
                ACTION.CLICK_ON_BUTTON_BACK,
                LABEL.CAPTURE_FOTO_KARTU_IDENTITAS
        )
    }

    fun clickOnNextButtonCameraViewIdCardConfirmation() {
        tracker.sendGeneralEvent(
                EVENT.CLICK_UBAH_NOMOR_HP,
                CATEGORY.UBAH_NOMOR_HP,
                ACTION.CLICK_ON_BUTTON_LANJUT_FOTO_KARTU_IDENTITAS_CONFIRMATION,
                LABEL.EMPTY
        )
    }

    fun clickOnBackButtonCameraViewIdCardConfirmation() {
        tracker.sendGeneralEvent(
                EVENT.CLICK_UBAH_NOMOR_HP,
                CATEGORY.UBAH_NOMOR_HP,
                ACTION.CLICK_ON_BUTTON_BACK,
                LABEL.FOTO_KARTU_IDENTITAS_CONFIRMATION
        )
    }

    fun clickOnCaptureButtonCameraViewSelfie() {
        tracker.sendGeneralEvent(
                EVENT.CLICK_UBAH_NOMOR_HP,
                CATEGORY.UBAH_NOMOR_HP,
                ACTION.CLICK_ON_BUTTON_CAPTURE_WAJAH_DAN_KARTU_IDENTITAS,
                LABEL.EMPTY
        )
    }

    fun clickOnBackButtonCameraViewSelfiew() {
        tracker.sendGeneralEvent(
                EVENT.CLICK_UBAH_NOMOR_HP,
                CATEGORY.UBAH_NOMOR_HP,
                ACTION.CLICK_ON_BUTTON_BACK,
                LABEL.CATURE_FOTO_WAJAH_DAN_KARTU_IDENTITAS
        )
    }

    fun clickOnNextButtonCameraViewSelfiewConfirmation() {
        tracker.sendGeneralEvent(
                EVENT.CLICK_UBAH_NOMOR_HP,
                CATEGORY.UBAH_NOMOR_HP,
                ACTION.CLICK_ON_BUTTON_LANJUT_FOTO_WAJAH_DAN_KARTU_INDENTITAS_CONFIRMATION,
                LABEL.EMPTY
        )
    }

    fun clickOnBackButtonCameraViewSelfieConfirmation() {
        tracker.sendGeneralEvent(
                EVENT.CLICK_UBAH_NOMOR_HP,
                CATEGORY.UBAH_NOMOR_HP,
                ACTION.CLICK_ON_BUTTON_BACK,
                LABEL.FOTO_WAJAH_DAN_KARTU_IDENTITAS_CONFIRMATION
        )
    }

    /**
     * Upload data
     */
    fun clickOnButtonSubmitUploadData() {
        tracker.sendGeneralEvent(
                EVENT.CLICK_UBAH_NOMOR_HP,
                CATEGORY.UBAH_NOMOR_HP,
                ACTION.CLICK_ON_UPLOAD_DATA,
                LABEL.EMPTY
        )
    }

    fun clickOnBackButtonUploadData() {
        tracker.sendGeneralEvent(
                EVENT.CLICK_UBAH_NOMOR_HP,
                CATEGORY.UBAH_NOMOR_HP,
                ACTION.CLICK_ON_BUTTON_BACK,
                LABEL.UPLOAD_DATA
        )
    }

    fun clickOnTextViewInputNewPhoneNumber() {
        tracker.sendGeneralEvent(
                EVENT.CLICK_UBAH_NOMOR_HP,
                CATEGORY.UBAH_NOMOR_HP,
                ACTION.INPUT_NOMOR_HP,
                LABEL.EMPTY
        )
    }

    fun clickOnExitButtonPopupUploadData() {
        tracker.sendGeneralEvent(
                EVENT.CLICK_UBAH_NOMOR_HP,
                CATEGORY.UBAH_NOMOR_HP,
                ACTION.CLICK_ON_BUTTON_KELUAR_POPUP,
                LABEL.EMPTY
        )
    }

    /**
     * Success page
     */
    fun clickOnButtonGotoHome() {
        tracker.sendGeneralEvent(
                EVENT.CLICK_UBAH_NOMOR_HP,
                CATEGORY.UBAH_NOMOR_HP,
                ACTION.CLICK_BUTTON_GOTO_HOME,
                LABEL.EMPTY
        )
    }

    companion object {
        object EVENT {
            const val CLICK_UBAH_NOMOR_HP = "clickUbahNomorHp"
        }

        object CATEGORY {
            const val UBAH_NOMOR_HP = "ubah nomor hp page"
        }

        object ACTION {
            const val CLICK_ON_BUTTON_LANJUT_UBAH = "click on button lanjut ubah"
            const val CLICK_ON_BUTTON_BACK = "click on button back"
            const val CLICK_ON_BUTTON_FOTO_KARTU_IDENTITAS = "click on button foto kartu identitas"
            const val CLICK_ON_BUTTON_CAPTURE_FOTO_KARTU_IDENTITAS = "click on button capture foto kartu identitas"
            const val CLICK_ON_BUTTON_LANJUT_FOTO_KARTU_IDENTITAS_CONFIRMATION = "click on button lanjut foto kartu identitas confirmation"
            const val CLICK_ON_BUTTON_MULAI_FOTO = "click on button mulai foto"
            const val CLICK_ON_BUTTON_CAPTURE_WAJAH_DAN_KARTU_IDENTITAS = "click on button capture foto wajah dan kartu identitas"
            const val CLICK_ON_BUTTON_LANJUT_FOTO_WAJAH_DAN_KARTU_INDENTITAS_CONFIRMATION = "click on button lanjut foto wajah dan kartu identitas confirmation"
            const val CLICK_ON_UPLOAD_DATA = "click on button upload data"
            const val INPUT_NOMOR_HP = "input nomor hp baru"
            const val CLICK_ON_BUTTON_KELUAR_POPUP = "click on button keluar - pop up keluar halaman"
            const val CLICK_BUTTON_GOTO_HOME = "click on button ke home"
        }

        object  LABEL {
            const val LANJUT_UBAH = "lanjut ubah"
            const val FOTO_KARTU_IDENTITAS = "foto kartu identitas"
            const val CAPTURE_FOTO_KARTU_IDENTITAS = "capture foto kartu identitas"
            const val FOTO_KARTU_IDENTITAS_CONFIRMATION = "foto kartu identitas confirmation"
            const val MULAI_FOTO = "mulai foto"
            const val CATURE_FOTO_WAJAH_DAN_KARTU_IDENTITAS = "capture foto wajah dan kartu identitas"
            const val FOTO_WAJAH_DAN_KARTU_IDENTITAS_CONFIRMATION = "foto wajah dan kartu identitas confirmation"
            const val UPLOAD_DATA = "upload data"
            const val EMPTY = ""
        }
    }
}