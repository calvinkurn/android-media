package com.tokopedia.sellerpersona.data.local

import android.content.Context
import android.content.SharedPreferences
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import javax.inject.Inject

/**
 * Created by @ilhamsuaib on 18/01/23.
 */

class PersonaSharedPreference @Inject constructor(
    @ApplicationContext private val context: Context
) {

    companion object {
        private const val SHARED_PREF = "SellerPersonaSharedPref"
        private const val KEY_IS_FIRST_VISIT = "keyIsFirstVisit"
    }

    private val sharedPref: SharedPreferences by lazy {
        context.getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE)
    }

    fun setHasFirstVisit() {
        val spe = sharedPref.edit()
        spe.putBoolean(KEY_IS_FIRST_VISIT, false)
        spe.apply()
    }

    fun isFirstVisit(): Boolean {
        return sharedPref.getBoolean(KEY_IS_FIRST_VISIT, true)
    }
}