package com.tokopedia.home_account.privacy_account

import com.tokopedia.home_account.privacy_account.common.isDisplayed
import com.tokopedia.home_account.privacy_account.common.isChecked
import com.tokopedia.home_account.privacy_account.common.isTextDisplayed
import com.tokopedia.home_account.privacy_account.common.isEnable
import com.tokopedia.home_account.privacy_account.common.isNotDisplayed
import com.tokopedia.home_account.privacy_account.common.clickOnSpannable
import com.tokopedia.home_account.privacy_account.common.clickOnView
import com.tokopedia.home_account.test.R

fun isConsentSocialNetworkDisplayed(isActive: Boolean) {
    isDisplayed(
        R.id.txt_header_consent_social_network,
        R.id.txt_desc_consent_social_network,
        R.id.switch_permission_data_usage
    )
    isChecked(isActive, R.id.switch_permission_data_usage)
}

fun clarificationDataUsageDisplayed() {
    clickOnSpannable("Cek Data yang Dipakai", R.id.txt_desc_consent_social_network)
    isTextDisplayed("Data yang dipakai")
    isDisplayed(
        R.id.txt_desc_clarification_data_usage,
        R.id.carousel_clarification_data_usage
    )
}

fun verificationEnabledDataUsageAction() {
    clickOnView(R.id.switch_permission_data_usage)
    isDisplayed(
        R.id.img_header,
        R.id.txt_title,
        R.id.txt_sub_title,
        R.id.cb_verification,
        R.id.txt_desc_checkbox,
        R.id.btn_verification
    )
    isEnable(false, R.id.btn_verification)
}

fun submitEnabledDataUsageSuccessAction() {
    clickOnView(R.id.switch_permission_data_usage)
    clickOnView(R.id.cb_verification)
    isEnable(true, R.id.btn_verification)
    clickOnView(R.id.btn_verification)
    isConsentSocialNetworkDisplayed(true)
}

fun submitEnabledDataUsageFailedAction() {
    clickOnView(R.id.switch_permission_data_usage)
    clickOnView(R.id.cb_verification)
    isEnable(true, R.id.btn_verification)
    clickOnView(R.id.btn_verification)
    isConsentSocialNetworkDisplayed(false)
    isTextDisplayed("Gagal mengaktifkan penggunaan data")
}

fun verificationDisabledDataUsageAction() {
    clickOnView(R.id.switch_permission_data_usage)
    isDisplayed(
        R.id.dialog_title,
        R.id.dialog_description,
        R.id.dialog_btn_primary,
        R.id.dialog_btn_secondary
    )
}

fun verificationDismissDisabledDataUsageAction() {
    clickOnView(R.id.switch_permission_data_usage)
    clickOnView(R.id.dialog_btn_secondary)
    isNotDisplayed(
        R.id.dialog_title,
        R.id.dialog_description,
        R.id.dialog_btn_primary,
        R.id.dialog_btn_secondary
    )
}

fun verificationSubmitDisabledDataUsageSuccessAction() {
    clickOnView(R.id.switch_permission_data_usage)
    clickOnView(R.id.dialog_btn_primary)
    isNotDisplayed(
        R.id.dialog_title,
        R.id.dialog_description,
        R.id.dialog_btn_primary,
        R.id.dialog_btn_secondary
    )
    isConsentSocialNetworkDisplayed(false)
}

fun verificationSubmitDisabledDataUsageFailedAction() {
    clickOnView(R.id.switch_permission_data_usage)
    clickOnView(R.id.dialog_btn_primary)
    isNotDisplayed(
        R.id.dialog_title,
        R.id.dialog_description,
        R.id.dialog_btn_primary,
        R.id.dialog_btn_secondary
    )
    isConsentSocialNetworkDisplayed(true)
    isTextDisplayed("Gagal menonaktifkan penggunaan data")
}
