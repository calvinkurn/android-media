package com.tokopedia.profilecompletion.common

import android.content.Context
import android.content.SharedPreferences
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import javax.inject.Inject

class PinPreference @Inject constructor(@ApplicationContext val context: Context) {
    private val PREF_NAME = "temp_pin_pref"

    private val KEY_TEMP_PIN = "temp_pin_flow"
    private val KEY_TEMP_CONFIRM_PIN = "temp_confirm_pin_flow"
    private val KEY_TEMP_HASH = "temp_hash_pin_flow"

    val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("PREF_NAME", Context.MODE_PRIVATE)

    fun setTempPin(pin: String) {
        sharedPreferences.edit().putString(KEY_TEMP_PIN, pin).apply()
    }

    fun clearTempPin() {
        sharedPreferences.edit().remove(KEY_TEMP_PIN).apply()
    }

    fun setTempConfirmationPin(confirmationPin: String) {
        sharedPreferences.edit().putString(KEY_TEMP_CONFIRM_PIN, confirmationPin).apply()
    }

    fun setTempHash(hash: String) {
        sharedPreferences.edit().putString(KEY_TEMP_HASH, hash).apply()
    }

    fun getTempPin(): String =
        sharedPreferences.getString(KEY_TEMP_PIN, "") ?: ""

    fun getTempConfirmPin(): String =
        sharedPreferences.getString(KEY_TEMP_CONFIRM_PIN, "") ?: ""

    fun getHash(): String =
        sharedPreferences.getString(KEY_TEMP_HASH, "") ?: ""

}
