package com.tokopedia.vouchercreation.common.utils

import android.app.Activity
import android.content.Context
import java.util.*

object SharedPreferencesUtil {

    private const val KEY_MVC_VOUCHER_LIST_FIRST_TIME_VISIT = "KEY_MVC_VOUCHER_LIST_FIRST_TIME_VISIT"
    private const val KEY_MVC_IS_VOUCHER_LIST_FIRST_TIME_VISIT = "KEY_MVC_IS_VOUCHER_LIST_FIRST_TIME_VISIT"
    private const val KEY_MVC_IS_BC_TICKER_CLOSED = "KEY_MVC_IS_BC_TICKER_CLOSED"

    fun getVoucherListFirstTimeVisit(activity: Activity): Long {
        val sharedPref = activity.getPreferences(Context.MODE_PRIVATE)
        return sharedPref.getLong(KEY_MVC_VOUCHER_LIST_FIRST_TIME_VISIT, Date().time)
    }

    fun setVoucherListFirstTimeVisit(activity: Activity, firstTimeVisit: Date) {
        val sharedPref = activity.getPreferences(Context.MODE_PRIVATE)
        with(sharedPref.edit()) {
            putLong(KEY_MVC_VOUCHER_LIST_FIRST_TIME_VISIT, firstTimeVisit.time)
            commit()
        }
    }

    fun isVoucherListFirstTimeVisit(activity: Activity): Boolean {
        val sharedPref = activity.getPreferences(Context.MODE_PRIVATE)
        return sharedPref.getBoolean(KEY_MVC_IS_VOUCHER_LIST_FIRST_TIME_VISIT, true)
    }

    fun setIsVoucherListFirstTimeVisit(activity: Activity, isFirstTimeVisit: Boolean) {
        val sharedPref = activity.getPreferences(Context.MODE_PRIVATE)
        with(sharedPref.edit()) {
            putBoolean(KEY_MVC_IS_VOUCHER_LIST_FIRST_TIME_VISIT, isFirstTimeVisit)
            commit()
        }
    }

    fun isBcTickerClosed(activity: Activity): Boolean {
        val sharedPref = activity.getPreferences(Context.MODE_PRIVATE)
        return sharedPref.getBoolean(KEY_MVC_IS_BC_TICKER_CLOSED, false)
    }

    fun setIsBcTickerClosed(activity: Activity, isClosed: Boolean) {
        val sharedPref = activity.getPreferences(Context.MODE_PRIVATE)
        with(sharedPref.edit()) {
            putBoolean(KEY_MVC_IS_BC_TICKER_CLOSED, isClosed)
            commit()
        }
    }
}