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
    private const val KEY_ID = "id"

    @JvmStatic
    fun getMessageFromException(context: Context?, e: Throwable?): String {
        return if (e is FlightOrderException) {
            TextUtils.join(",", e.errorList)
        } else {
            ErrorHandler.getErrorMessage(context, e)
        }
    }

    fun getErrorIdAndTitleFromFlightError(context: Context, t: Throwable): Pair<Int, String> {
        var errorId = 0
        return try {
            val type = object : TypeToken<Map<String, Any>>() {}.type
            val flightError = Gson().fromJson<Map<String, Any>>(t.message, type)
            if (flightError.containsKey(KEY_ID)) {
                errorId = try {
                    flightError[KEY_ID].toString().toInt()
                } catch (t: Throwable) {
                    t.printStackTrace()
                    0
                }
            }

            if (flightError.containsKey(KEY_TITLE)) {
                Pair(errorId, flightError[KEY_TITLE].toString())
            } else {
                Pair(errorId, ErrorHandler.getErrorMessage(context, t))
            }
        } catch (e: Exception) {
            Pair(errorId, ErrorHandler.getErrorMessage(context, t))
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