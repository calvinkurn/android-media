package com.tokopedia.product.addedit.common.util

import android.app.Activity
import android.content.Context

object SharedPreferencesUtil {

    private const val MA_SA_ADDEDITPRODUCT_FIRST_TIME_SPESIFICATION = "FirstTimeSpecification"

    fun getFirstTimeSpecification(activity: Activity): Boolean {
        val sharedPref = activity.getPreferences(Context.MODE_PRIVATE)
        return sharedPref.getBoolean(MA_SA_ADDEDITPRODUCT_FIRST_TIME_SPESIFICATION, false)
    }

    fun setFirstTimeSpecification(activity: Activity, value: Boolean) {
        val sharedPref = activity.getPreferences(Context.MODE_PRIVATE)
        with(sharedPref.edit()) {
            putBoolean(MA_SA_ADDEDITPRODUCT_FIRST_TIME_SPESIFICATION, value)
            commit()
        }
    }
}