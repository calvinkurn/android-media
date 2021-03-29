package com.tokopedia.product.addedit.common.util

import android.app.Activity
import android.content.Context
import com.tokopedia.kotlin.extensions.view.orZero
import java.math.BigInteger

object SharedPreferencesUtil {

    private const val MA_SA_ADDEDITPRODUCT_FIRST_TIME_SPECIFICATION = "FirstTimeSpecification"
    private const val MA_SA_ADDEDITPRODUCT_PRICE_WHEN_LOADED = "PriceWhenLoaded"

    fun getFirstTimeSpecification(activity: Activity): Boolean {
        val sharedPref = activity.getPreferences(Context.MODE_PRIVATE)
        return sharedPref.getBoolean(MA_SA_ADDEDITPRODUCT_FIRST_TIME_SPECIFICATION, false)
    }

    fun setFirstTimeSpecification(activity: Activity, value: Boolean) {
        val sharedPref = activity.getPreferences(Context.MODE_PRIVATE)
        with(sharedPref.edit()) {
            putBoolean(MA_SA_ADDEDITPRODUCT_FIRST_TIME_SPECIFICATION, value)
            commit()
        }
    }

    fun getPriceWhenLoaded(activity: Activity): BigInteger {
        val sharedPref = activity.getPreferences(Context.MODE_PRIVATE)
        val value = sharedPref.getString(MA_SA_ADDEDITPRODUCT_PRICE_WHEN_LOADED, "0")
        return value?.toBigIntegerOrNull().orZero()
    }

    fun setPriceWhenLoaded(activity: Activity, value: BigInteger) {
        val sharedPref = activity.getPreferences(Context.MODE_PRIVATE)
        with(sharedPref.edit()) {
            putString(MA_SA_ADDEDITPRODUCT_PRICE_WHEN_LOADED, value.toString())
            commit()
        }
    }
}