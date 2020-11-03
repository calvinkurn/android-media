package com.tokopedia.home_account

import android.text.TextUtils
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import com.tokopedia.graphql.data.model.GraphqlResponse

object Utils {
    const val M_RESULT = "mResults"
    const val M_ERRORS = "mErrors"

    fun normalizePhoneNumber(phoneNum: String): String? {
        return if (phoneNum.isNotEmpty()) phoneNum.replaceFirst("^0(?!$)".toRegex(), "62") else ""
    }

    fun convertResponseToJson(gqlResponse: GraphqlResponse): Map<String, Any> {
        return try {
            val gson = GsonBuilder().serializeNulls().setPrettyPrinting().create()
            val json = gson.toJson(gqlResponse)
            Gson().fromJson(json, object : TypeToken<Map<Any, Any>>() {}.type)
        } catch (e: Exception) {
            e.printStackTrace()
            HashMap<String, Any>().apply {
                this[M_ERRORS] = "$e"
            }
        }
    }
}