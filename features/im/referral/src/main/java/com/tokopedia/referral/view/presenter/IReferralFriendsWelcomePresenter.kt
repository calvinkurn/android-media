package com.tokopedia.referral.view.presenter

/**
 * Created by ashwanityagi on 04/12/17.
 */

interface IReferralFriendsWelcomePresenter {
    fun getSubHeaderFromFirebase(owner: String) : String
    fun initialize(code: String)
    fun copyVoucherCode(code: String)
}