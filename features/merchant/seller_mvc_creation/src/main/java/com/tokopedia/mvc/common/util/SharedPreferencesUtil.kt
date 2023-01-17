package com.tokopedia.mvc.common.util

import android.content.Context
import android.content.SharedPreferences

object SharedPreferencesUtil {
    private const val SA_MVC_CREATION_SHARED_PRED = "mvc_creation_shared_pred"
    private const val SA_MVC_UPLOAD_RESULT = "mvc_upload_result"

    private fun initiateSharedPref(context: Context): SharedPreferences {
        return context.getSharedPreferences(SA_MVC_CREATION_SHARED_PRED, Context.MODE_PRIVATE)
    }

    fun getUploadResult(context: Context): String {
        val sharedPref = initiateSharedPref(context)
        return sharedPref.getString(SA_MVC_UPLOAD_RESULT, "").orEmpty()
    }

    fun clearUploadResult(context: Context) {
        setUploadResult(context, "")
    }

    fun setUploadResult(context: Context, value: String) {
        val sharedPref = initiateSharedPref(context)
        with(sharedPref.edit()) {
            putString(SA_MVC_UPLOAD_RESULT, value)
            commit()
        }
    }
}
