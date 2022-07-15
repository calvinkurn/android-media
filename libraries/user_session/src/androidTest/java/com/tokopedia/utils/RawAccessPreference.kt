package com.tokopedia.utils

import android.content.Context

class RawAccessPreference(context: Context, name: String) {

    private val sharedPref = context.getSharedPreferences(name, Context.MODE_PRIVATE)

    fun getRawValue(key: String): Any? {
        return sharedPref.all[key]
    }

}