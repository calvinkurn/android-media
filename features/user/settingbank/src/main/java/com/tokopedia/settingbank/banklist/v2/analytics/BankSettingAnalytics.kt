package com.tokopedia.settingbank.banklist.v2.analytics

import com.tokopedia.track.TrackApp

class BankSettingAnalytics {

    fun eventOnAddBankClick() {
        TrackApp.getInstance().gtm.sendGeneralEvent(
                Events.E_NAME_CLICK_BANK_ACCOUNT,
                Events.E_CATEGORY_CLICK_BANK_ACCOUNT,
                Events.E_ACTION_CLICK_ANOTHER_BANK_ACCOUNT,
                "")
    }

    fun eventOnAddAnotherBankClick() {
        TrackApp.getInstance().gtm.sendGeneralEvent(
                Events.E_NAME_CLICK_BANK_ACCOUNT,
                Events.E_ACTION_ADD_ACCOUNT_CONFIRM,
                Events.E_ACTION_CLICK_BANK_ACCOUNT,
                "")
    }

    fun eventOnToolbarTNCClick() {
        TrackApp.getInstance().gtm.sendGeneralEvent(
                Events.E_NAME_CLICK_BANK_ACCOUNT,
                Events.E_CATEGORY_CLICK_BANK_ACCOUNT,
                Events.E_ACTION_TOOLBAR_TNC,
                "")
    }

    fun eventOnPericsaButtonClick() {
        TrackApp.getInstance().gtm.sendGeneralEvent(
                Events.E_NAME_CLICK_BANK_ACCOUNT,
                Events.E_CATEGORY_CLICK_BANK_ACCOUNT,
                Events.E_ACTION_PERIKSA_CLICK,
                "")
    }

    fun eventOnTermsAndConditionClick() {
        TrackApp.getInstance().gtm.sendGeneralEvent(
                Events.E_NAME_CLICK_BANK_ACCOUNT,
                Events.E_CATEGORY_CLICK_BANK_ACCOUNT,
                Events.E_ACTION_TNC_CLICK,
                "")
    }

    fun eventOnManualNameSimpanClick() {
        TrackApp.getInstance().gtm.sendGeneralEvent(
                Events.E_NAME_CLICK_BANK_ACCOUNT,
                Events.E_CATEGORY_CLICK_BANK_ACCOUNT,
                Events.E_ACTION_MANUAL_SIMPAN,
                "")
    }


    fun eventOnAutoNameSimpanClick() {
        TrackApp.getInstance().gtm.sendGeneralEvent(
                Events.E_NAME_CLICK_BANK_ACCOUNT,
                Events.E_CATEGORY_CLICK_BANK_ACCOUNT,
                Events.E_ACTION_AUTO_SIMPAN,
                "")
    }


    fun eventDialogConfirmAddAccountClick() {
        TrackApp.getInstance().gtm.sendGeneralEvent(
                Events.E_NAME_CLICK_BANK_ACCOUNT,
                Events.E_CATEGORY_CLICK_BANK_ACCOUNT,
                Events.E_ACTION_ADD_ACCOUNT_CONFIRM,
                "")
    }

    fun eventMakeAccountPrimaryClick() {
        TrackApp.getInstance().gtm.sendGeneralEvent(
                Events.E_NAME_CLICK_BANK_ACCOUNT,
                Events.E_CATEGORY_CLICK_BANK_ACCOUNT,
                Events.E_ACTION_MAKE_ACCOUNT_PRIMARY,
                "")
    }

    fun eventDeleteAccountClick() {
        TrackApp.getInstance().gtm.sendGeneralEvent(
                Events.E_NAME_CLICK_BANK_ACCOUNT,
                Events.E_CATEGORY_CLICK_BANK_ACCOUNT,
                Events.E_ACTION_DELETE_ACCOUNT,
                "")
    }

    fun eventDialogConfirmDeleteAccountClick() {
        TrackApp.getInstance().gtm.sendGeneralEvent(
                Events.E_NAME_CLICK_BANK_ACCOUNT,
                Events.E_CATEGORY_CLICK_BANK_ACCOUNT,
                Events.E_ACTION_CONFIRM_DELETE_ACCOUNT,
                "")
    }


    fun eventIsiDataClick() {
        TrackApp.getInstance().gtm.sendGeneralEvent(
                Events.E_NAME_CLICK_BANK_ACCOUNT,
                Events.E_CATEGORY_CLICK_BANK_ACCOUNT,
                Events.E_ACTION_ISI_DATA_CLICK,
                "")
    }

    fun eventSelanjutnyaClick() {
        TrackApp.getInstance().gtm.sendGeneralEvent(
                Events.E_NAME_CLICK_BANK_ACCOUNT,
                Events.E_CATEGORY_CLICK_BANK_ACCOUNT,
                Events.E_ACTION_ISI_DATA_CLICK,
                "")
    }

    fun eventRekeningConfirmationClick() {
        TrackApp.getInstance().gtm.sendGeneralEvent(
                Events.E_NAME_CLICK_BANK_ACCOUNT,
                Events.E_CATEGORY_CLICK_BANK_ACCOUNT,
                Events.E_ACTION_ON_CONFIRM_ACCOUNT_OPTION_SELECTED,
                "")
    }

    fun eventFamilyDocumentSubmitClick() {
        TrackApp.getInstance().gtm.sendGeneralEvent(
                Events.E_NAME_CLICK_BANK_ACCOUNT,
                Events.E_CATEGORY_CLICK_BANK_ACCOUNT,
                Events.E_ACTION_SELANJUTNYA_CLICK,
                Events.E_LABEL_SELANJUTNYA_FAMILY)

    }

    fun eventCompanyDocumentSubmitClick() {
        TrackApp.getInstance().gtm.sendGeneralEvent(
                Events.E_NAME_CLICK_BANK_ACCOUNT,
                Events.E_CATEGORY_CLICK_BANK_ACCOUNT,
                Events.E_ACTION_SELANJUTNYA_CLICK,
                Events.E_LABEL_SELANJUTNYA_COMPANY)

    }

    fun eventConfirmAccountNameClick() {
        TrackApp.getInstance().gtm.sendGeneralEvent(
                Events.E_NAME_CLICK_BANK_ACCOUNT,
                Events.E_CATEGORY_CLICK_BANK_ACCOUNT,
                Events.E_ACTION_CONFIRM_MY_ACCOUNT,
                "")
    }


    object Events {
        const val E_NAME_CLICK_BANK_ACCOUNT = "clickBankAccount"

        /*On BANK LISTING PAGE*/
        const val E_CATEGORY_CLICK_BANK_ACCOUNT = "bank account page"
        const val E_ACTION_CLICK_BANK_ACCOUNT = "click tambah rekening masih kosong"
        const val E_ACTION_CLICK_ANOTHER_BANK_ACCOUNT = "click tambah rekening lain"
        const val E_ACTION_TOOLBAR_TNC = "click i"
        const val E_ACTION_PERIKSA_CLICK = "click periksa"
        const val E_ACTION_TNC_CLICK = "click syarat ketentuan"
        const val E_ACTION_MANUAL_SIMPAN = "click simpan manual"
        const val E_ACTION_AUTO_SIMPAN = "click simpan automatic"
        const val E_ACTION_ADD_ACCOUNT_CONFIRM = "click ya tambah"
        const val E_ACTION_MAKE_ACCOUNT_PRIMARY = "click jadikan utama"
        const val E_ACTION_DELETE_ACCOUNT = "click hapus"
        const val E_ACTION_CONFIRM_DELETE_ACCOUNT = "click ya hapus"
        const val E_ACTION_ISI_DATA_CLICK = "click isi data"

        const val E_ACTION_SELANJUTNYA_CLICK = "click selanjutnya"
        const val E_LABEL_SELANJUTNYA_FAMILY = "Family"
        const val E_LABEL_SELANJUTNYA_COMPANY = "Company"
        const val E_LABEL_SELANJUTNYA_OTHER = "Others"


        const val E_ACTION_ON_CONFIRM_ACCOUNT_OPTION_SELECTED = "click rekening perusahaan"
        const val E_ACTION_CONFIRM_MY_ACCOUNT = "click ya rekening saya"


    }
}