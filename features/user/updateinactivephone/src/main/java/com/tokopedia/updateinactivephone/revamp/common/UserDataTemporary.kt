package com.tokopedia.updateinactivephone.revamp.common

import android.content.Context
import android.content.SharedPreferences

class UserDataTemporary constructor(
        private val context: Context
) {
    private fun getSharedPReference() : SharedPreferences {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }

    fun getUserId(): String {
        return getSharedPReference().getString(KEY_USER_ID, "") ?: ""
    }

    fun setUserId(value: String) {
        getSharedPReference().edit().putString(KEY_USER_ID, value).apply()
    }

    fun getEmail(): String {
        return getSharedPReference().getString(KEY_EMAIL, "") ?: ""
    }

    fun setEmail(value: String) {
        getSharedPReference().edit().putString(KEY_EMAIL, value).apply()
    }

    fun getOldPhone(): String {
        return getSharedPReference().getString(KEY_OLD_PHONE, "") ?: ""
    }

    fun setOldPhone(value: String) {
        getSharedPReference().edit().putString(KEY_OLD_PHONE, value).apply()
    }

    fun getIndex(): Int {
        return getSharedPReference().getInt(KEY_USER_INDEX, 0)
    }

    fun setIndex(value: Int) {
        getSharedPReference().edit().putInt(KEY_USER_INDEX, value).apply()
    }

    fun delete() {
        getSharedPReference()
                .edit()
                .clear()
                .apply()
    }

    companion object {
        private const val PREF_NAME = "userDataInactivePhone.pref"
        private const val KEY_USER_ID = "userId"
        private const val KEY_EMAIL = "email"
        private const val KEY_OLD_PHONE = "oldPhone"
        private const val KEY_USER_INDEX = "userIndex"
    }
}