package com.tokopedia.referral.listener

import android.app.Activity

/**
 * Created by ashwanityagi on 04/12/17.
 */

interface FriendsWelcomeView {

    val activity: Activity

    fun showToastMessage(message: String)

    fun closeView()

}