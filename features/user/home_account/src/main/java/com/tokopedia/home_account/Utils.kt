package com.tokopedia.home_account

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import com.tokopedia.graphql.data.model.GraphqlResponse
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.*
import kotlin.collections.HashMap

object Utils {
    const val M_RESULT = "mResults"
    const val M_ERRORS = "mErrors"
    private const val LANGUAGE_ID = "id"
    private const val COUNTRY_ID = "ID"
    private const val PATTERN_CURRENCY = "Rp#,##0.##"
    private const val DECIMAL_SEPARATOR = ','
    private const val GROUPING_SEPARATOR = '.'

    fun normalizePhoneNumber(phoneNum: String): String? {
        return if (phoneNum.isNotEmpty()) phoneNum.replaceFirst("^0(?!$)".toRegex(), "62") else ""
    }

    fun formatPhoneNumber(phoneNum: String): String? {
        if (phoneNum.isNotEmpty()) {
            return when {
                phoneNum.startsWith("62") -> phoneNum.replaceFirst("62", "0")
                phoneNum.startsWith("+62") -> phoneNum.replaceFirst("+62", "0")
                else -> phoneNum
            }
        }
        return ""
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

    fun formatIdrCurrency(balance: Long): String {
        val unusualSymbols = DecimalFormatSymbols(Locale(LANGUAGE_ID, COUNTRY_ID))
        unusualSymbols.decimalSeparator = DECIMAL_SEPARATOR
        unusualSymbols.groupingSeparator = GROUPING_SEPARATOR

        val pattern = PATTERN_CURRENCY
        val formatter = DecimalFormat(pattern, unusualSymbols)
        formatter.groupingSize = 3

        return formatter.format(balance)
    }
}