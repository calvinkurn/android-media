package com.tokopedia.editshipping.data.preference

import android.content.Context
import android.content.SharedPreferences

class GocarInstanCoachMarkSharePref(context: Context) {

    private val PREFERENCE_NAME = "coachmark_gocar_instan"

    private val EXTRA_IS_COACHMARK = "EXTRA_IS_COACHMARK"

    private val sharedPref: SharedPreferences? by lazy {
        context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)
    }

    private val editor = sharedPref?.edit()

    fun setCoachMarkState(state: Boolean) {
        editor?.putBoolean(EXTRA_IS_COACHMARK, state)
        editor?.apply()
    }

    fun getCoachMarkState(): Boolean? {
        return sharedPref?.getBoolean(EXTRA_IS_COACHMARK, true)
    }
}