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
    }

    private val sharedPref: SharedPreferences by lazy {
        context.getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE)
    }


}