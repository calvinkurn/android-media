package com.tokopedia.sellerpersona.data.local

import android.content.Context
import android.content.SharedPreferences
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import javax.inject.Inject

/**
 * Created by @ilhamsuaib on 20/02/23.
 */

class PersonaSharedPref @Inject constructor(
    @ApplicationContext private val context: Context
) : PersonaSharedPrefInterface {

    companion object {
        private const val PERSONA_SP = "persona_shared_pref"
        private const val KEY_IS_FIRST_VISIT = "is_first_visit"
    }

    private val sp: SharedPreferences by lazy {
        context.getSharedPreferences(PERSONA_SP, Context.MODE_PRIVATE)
    }

    override fun isFirstVisit(): Boolean = sp.getBoolean(KEY_IS_FIRST_VISIT, false)

    override fun setIsFirstVisit(isFirstVisit: Boolean) {
        sp.edit().putBoolean(KEY_IS_FIRST_VISIT, isFirstVisit).apply()
    }
}