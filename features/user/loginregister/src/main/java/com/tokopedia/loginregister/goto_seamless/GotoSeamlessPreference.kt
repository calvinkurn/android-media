package com.tokopedia.loginregister.goto_seamless

import android.content.Context
import android.content.SharedPreferences
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.encryption.security.AeadEncryptor
import com.tokopedia.user.session.UserSession
import javax.inject.Inject

class GotoSeamlessPreference @Inject constructor(@ApplicationContext context: Context, val aeadEncryptor: AeadEncryptor) {

    private val sharedPrefs: SharedPreferences

    companion object {
        const val GOTO_SEAMLESS_PREF = "goto_seamless_pref"
        const val KEY_TEMPORARY = "temporary_key"
    }

    init {
        sharedPrefs = context.getSharedPreferences(
            GOTO_SEAMLESS_PREF,
            Context.MODE_PRIVATE
        )
    }

    fun storeTemporaryToken(token: String) {
        try {
            val encryptedToken = aeadEncryptor.encrypt(token, UserSession.KEY_IV.toByteArray(Charsets.UTF_8))
            sharedPrefs.edit().putString(KEY_TEMPORARY, encryptedToken).apply()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun getTemporaryToken(): String {
        return try {
            val encryptedToken = sharedPrefs.getString(KEY_TEMPORARY, "") ?: ""
            aeadEncryptor.decrypt(
                encryptedToken,
                UserSession.KEY_IV.toByteArray(Charsets.UTF_8)
            )
        } catch (e: Exception) {
            ""
        }
    }
}