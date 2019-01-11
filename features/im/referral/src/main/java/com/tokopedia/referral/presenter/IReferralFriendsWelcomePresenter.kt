package com.tokopedia.referral.presenter

/**
 * Created by ashwanityagi on 04/12/17.
 */

interface IReferralFriendsWelcomePresenter {
    val subHeaderFromFirebase: String
    fun initialize()
    fun copyVoucherCode(code: String)
}