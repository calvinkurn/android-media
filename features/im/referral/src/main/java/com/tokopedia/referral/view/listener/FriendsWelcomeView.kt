package com.tokopedia.referral.view.listener

import android.app.Activity

/**
 * Created by ashwanityagi on 04/12/17.
 */

interface FriendsWelcomeView : com.tokopedia.abstraction.base.view.listener.CustomerView{

    val activity: Activity

    fun showToastMessage(message: String)

    fun closeView()

}