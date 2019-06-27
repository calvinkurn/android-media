package com.tokopedia.referral.view.presenter

import android.support.v4.app.FragmentManager

/**
 * Created by ashwanityagi on 18/09/17.
 */

interface IReferralPresenter {

    val referralSubHeader: String

    val howItWorks: String

    val voucherCodeFromCache: String

    val isAppShowReferralButtonActivated: Boolean?

    val referralTitleDesc: String

    fun initialize()

    fun checkLoginAndFetchReferralCode()

    fun shareApp(fragmentManager: FragmentManager)

    fun getReferralVoucherCode()

    fun copyVoucherCode(code: String)
}

