package com.tokopedia.flight.orderlist.util

import android.content.Context
import android.text.TextUtils
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.tokopedia.abstraction.common.utils.network.ErrorHandler
import com.tokopedia.flight.orderlist.network.model.FlightOrderError
import com.tokopedia.flight.orderlist.network.model.FlightOrderException

/**
 * Created by User on 11/28/2017.
 */
object FlightErrorUtil {

    private const val KEY_TITLE = "title"

    @JvmStatic
    fun getMessageFromException(context: Context?, e: Throwable?): String {
        return if (e is FlightOrderException) {
            TextUtils.join(",", e.errorList)
        } else {
            ErrorHandler.getErrorMessage(context, e)
        }
    }

    fun getTitleFromFlightError(context: Context, t: Throwable): String {
        val type = object : TypeToken<List<Map<String, Any>>>() {}.type
        val flightErrorList = Gson().fromJson<List<Map<String, Any>>>(t.message, type)
        return if (flightErrorList.isNotEmpty()) {
            val flightError = flightErrorList[0]
            if (flightError.containsKey(KEY_TITLE)) {
                flightError[KEY_TITLE].toString()
            } else {
                "Terjadi kesalahan. Ulangi beberapa saat lagi"
            }
        } else {
            "Terjadi kesalahan. Ulangi beberapa saat lagi"
        }
    }

    fun getErrorCode(flightError: FlightOrderError): Int {
        return try {
            flightError.id.toInt()
        } catch (e: Exception) {
            -1
        }
    }
}