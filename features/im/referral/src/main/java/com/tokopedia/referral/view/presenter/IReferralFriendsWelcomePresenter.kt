package com.tokopedia.referral.view.presenter

/**
 * Created by ashwanityagi on 04/12/17.
 */

interface IReferralFriendsWelcomePresenter {
    val subHeaderFromFirebase: String
    fun initialize()
    fun copyVoucherCode(code: String)
}