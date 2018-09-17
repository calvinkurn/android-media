package com.tokopedia.profile

import android.content.Context
import android.content.Intent

/**
 * @author by milhamj on 9/17/18.
 */
interface ProfileModuleRouter {
    fun getLoginIntent(context: Context): Intent
}