package com.tokopedia.loginregister.redefineregisteremail.view.inputphone.data.local

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.loginregister.login.const.LoginConstants
import kotlinx.coroutines.withContext
import javax.inject.Inject

class RegisterPreferences @Inject constructor(
    @ApplicationContext private val context: Context,
    private val dispatcher: CoroutineDispatchers
) {
    suspend fun saveFirstInstallTime() {
        withContext(dispatcher.io) {
            val sharedPrefs = context.getSharedPreferences(
                LoginConstants.PrefKey.KEY_FIRST_INSTALL_SEARCH, Context.MODE_PRIVATE
            )
            sharedPrefs?.edit()?.putLong(
                LoginConstants.PrefKey.KEY_FIRST_INSTALL_TIME_SEARCH, 0
            )?.apply()
        }
    }
}
