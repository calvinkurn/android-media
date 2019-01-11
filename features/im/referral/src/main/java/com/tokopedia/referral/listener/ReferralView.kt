package com.tokopedia.referral.listener

import android.app.Activity

import com.tokopedia.referral.data.ReferralCodeEntity
import com.tokopedia.referral.model.ShareApps

interface ReferralView : com.tokopedia.abstraction.base.view.listener.CustomerView {

    val activity: Activity

    val referralCodeFromTextView: String

    fun renderVoucherCodeData(referralData: ReferralCodeEntity)

    fun showToastMessage(message: String)

    fun closeView()

    fun navigateToLoginPage()

    fun showVerificationPhoneNumberPage()

    fun showProcessDialog()

    fun hideProcessDialog()

    fun renderErrorGetVoucherCode(message: String)

    fun renderSharableApps(shareApps: ShareApps, index: Int)
}