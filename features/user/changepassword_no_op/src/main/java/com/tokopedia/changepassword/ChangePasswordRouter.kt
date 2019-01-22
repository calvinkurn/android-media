package com.tokopedia.changepassword

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.support.v4.app.FragmentActivity

/**
 * @author by nisie on 7/25/18.
 */
interface ChangePasswordRouter {

    fun getForgotPasswordIntent(context: Context, userEmail : String): Intent

    fun logoutToHome(activity: Activity)
}