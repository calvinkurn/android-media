package com.tokopedia.navigation_common.model

import android.content.Context
import android.content.SharedPreferences
import android.text.TextUtils
import com.google.gson.Gson

class WalletPref(context: Context, gson: Gson) {

    private val preferences: SharedPreferences = context.getSharedPreferences(WALLET_PREF, Context.MODE_PRIVATE)

    private val gson: Gson = gson

    fun saveWallet(wallet: WalletModel?) {
        val editor = preferences.edit()
        val jsonWallet = gson.toJson(wallet)
        editor.putString(WALLET_PREF, jsonWallet).apply()
    }

    fun saveDebitInstantUrl(url: String?) {
        val editor = preferences.edit()
        editor.putString(DEBIT_INSTANT_URL, url).apply()
    }

    fun retrieveDebitInstantUrl(): String? {
        return preferences.getString(DEBIT_INSTANT_URL, "")
    }

    fun retrieveWallet(): WalletModel? {
        return try {
            val jsonWallet = preferences.getString(WALLET_PREF, null)
            if (TextUtils.isEmpty(jsonWallet)) {
                null
            } else {
                gson.fromJson(jsonWallet, WalletModel::class.java)
            }
        } catch (e: Exception) {
            null
        }
    }

    fun clear() {
        preferences.edit().clear().apply()
    }

    companion object {
        private const val WALLET_PREF = "wallet.pref"
        private const val DEBIT_INSTANT_URL = "debit_instant_url"
    }

}