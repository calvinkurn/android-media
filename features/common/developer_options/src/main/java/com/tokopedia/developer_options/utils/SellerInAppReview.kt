package com.tokopedia.developer_options.utils

import android.content.Context
import android.content.SharedPreferences

/**
 * Created By @ilhamsuaib on 04/02/21
 */

class SellerInAppReview {

    companion object {
        private const val PREFERENCE_NAME = "CACHE_SELLER_IN_APP_REVIEW"
        private const val KEY_IS_ALLOW_APP_REVIEW_DEBUGGING = "KEY_SIR_IS_ALLOW_APP_REVIEW_DEBUGGING"

        @JvmStatic
        fun setSellerAppReviewDebuggingEnabled(context: Context, boolean: Boolean) {
            val spe = getSharedPrefEditor(context)
            spe.putBoolean(KEY_IS_ALLOW_APP_REVIEW_DEBUGGING, boolean)
            spe.apply()
        }

        @JvmStatic
        fun getSellerAppReviewDebuggingEnabled(context: Context): Boolean {
            val sp = getSharedPref(context)
            return sp.getBoolean(KEY_IS_ALLOW_APP_REVIEW_DEBUGGING, false)
        }

        private fun getSharedPref(context: Context): SharedPreferences {
            return context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)
        }

        private fun getSharedPrefEditor(context: Context): SharedPreferences.Editor {
            val pref = getSharedPref(context)
            return pref.edit()
        }
    }
}