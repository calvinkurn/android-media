package com.tokopedia.localizationchooseaddress.ui.preference

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel

class ChooseAddressSharePref(context: Context?) {

    val PREFERENCE_NAME = "LocalCacheAddress"
    val EXTRA_IS_CHOSEN_ADDRESS = "EXTRA_IS_CHOSEN_ADDRESS"
    val EXTRA_IS_COACHMARK = "EXTRA_IS_COACHMARK"

    private val sharedPref: SharedPreferences? by lazy {
        context?.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)
    }

    val editor = sharedPref?.edit()

    fun getLocalCacheData(): LocalCacheModel? {
        val data = sharedPref?.getString(EXTRA_IS_CHOSEN_ADDRESS, null)
        return Gson().fromJson(data, LocalCacheModel::class.java)
    }

    fun checkLocalCache(): String? {
        return sharedPref?.getString(EXTRA_IS_CHOSEN_ADDRESS, null)
    }

    fun setLocalCache(data: LocalCacheModel) {
        val jsonString = Gson().toJson(data)
        editor?.putString(EXTRA_IS_CHOSEN_ADDRESS, jsonString)
        editor?.apply()
    }

    fun setCoachMarkState(state: Boolean) {
        editor?.putBoolean(EXTRA_IS_COACHMARK, state)
        editor?.apply()
    }

    fun getCoachMarkState(): Boolean? {
        return sharedPref?.getBoolean(EXTRA_IS_COACHMARK, true)
    }

}
