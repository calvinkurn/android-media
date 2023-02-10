package com.tokopedia.developer_options.session

import android.content.Context
import android.content.SharedPreferences
import com.tokopedia.developer_options.config.DevOptConfig
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfigKey
import com.tokopedia.user.session.util.EncoderDecoder
import java.util.concurrent.TimeUnit

class DevOptLoginSession(private val context: Context) {

    companion object {
        private const val LOGIN_SESSION_SHARED_PREF = "dev_opt_login_session"
        private const val KEY_PASSWORD = "password_hash"
        private const val KEY_LAST_UPDATED = "last_updated"

        private const val SESSION_EXPIRED_DAYS = 7 // 1 week
        private const val DEV_OPT_IV = "developeropt1234"
    }

    private val sharedPref by lazy { createSharedPreference() }
    private val sharedPrefEditor by lazy { sharedPref.edit() }
    private val remoteConfig by lazy { FirebaseRemoteConfigImpl(context) }

    fun isLoggedIn(): Boolean {
        return isPasswordValid() && !isSessionExpired()
    }

    fun setPassword(password: String) {
        sharedPrefEditor.putString(KEY_PASSWORD, EncoderDecoder.Encrypt(password, DEV_OPT_IV)).apply()
        sharedPrefEditor.putLong(KEY_LAST_UPDATED, System.currentTimeMillis()).apply()
    }

    fun clear() {
        sharedPrefEditor.clear().apply()
        DevOptConfig.setChuckNotifEnabled(context, false)
    }

    private fun getPassword(): String? {
        return EncoderDecoder.Decrypt(sharedPref.getString(KEY_PASSWORD, ""), DEV_OPT_IV)
    }

    private fun getLastUpdated(): Long {
        return sharedPref.getLong(KEY_LAST_UPDATED, 0)
    }

    private fun isPasswordValid(): Boolean {
        val currentPassword = getPassword()
        val serverPassword = getServerPassword()
        return currentPassword == serverPassword
    }

    private fun isSessionExpired(): Boolean {
        val lastUpdated = TimeUnit.MILLISECONDS.toDays(getLastUpdated())
        val currentTime = TimeUnit.MILLISECONDS.toDays(System.currentTimeMillis())
        val diff = currentTime - lastUpdated
        return diff >= SESSION_EXPIRED_DAYS
    }

    private fun getServerPassword(): String? {
        return remoteConfig.getString(RemoteConfigKey.DEV_OPTS_AUTHORIZATION, "")
    }

    private fun createSharedPreference(): SharedPreferences {
        return context.getSharedPreferences(LOGIN_SESSION_SHARED_PREF, Context.MODE_PRIVATE)
    }
}
