package com.tokopedia.changepassword

import android.content.Context
import android.content.Intent

/**
 * @author by nisie on 7/25/18.
 */
interface ChangePasswordRouter {

    fun getHomeIntent(context: Context): Intent
}