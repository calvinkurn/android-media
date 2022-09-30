package com.tokopedia.loginregister.redefineregisteremail.view.inputphone.data.local

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.loginregister.login.const.LoginConstants
import javax.inject.Inject

class RegisterPreferences @Inject constructor(
    @ApplicationContext private val context: Context)
{
    fun saveFirstInstallTime() {
        val sharedPrefs = context.getSharedPreferences(
            LoginConstants.PrefKey.KEY_FIRST_INSTALL_SEARCH, Context.MODE_PRIVATE
        )
        sharedPrefs?.edit()?.putLong(
            LoginConstants.PrefKey.KEY_FIRST_INSTALL_TIME_SEARCH, 0
        )?.apply()
    }
}