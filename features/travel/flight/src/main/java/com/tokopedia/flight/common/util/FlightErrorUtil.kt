package com.tokopedia.flight.common.util

import android.content.Context
import android.text.TextUtils
import com.google.gson.Gson
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.google.gson.reflect.TypeToken
import com.tokopedia.kotlin.extensions.view.toIntSafely
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.network.utils.ErrorHandler

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
                    flightError[KEY_ID].toString().toIntSafely()
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
            flightError.id.toIntSafely()
        } catch (e: Exception) {
            -1
        }
    }
}

/**
 * Created by User on 11/28/2017.
 */
data class FlightOrderError(
    @SerializedName("id")
    @Expose
    var id: String = "",

    @SerializedName("status")
    @Expose
    val status: String = "",

    @SerializedName("title")
    @Expose
    val title: String = ""
) {

    override fun equals(other: Any?): Boolean {
        return other is FlightOrderError && other.id.equals(id, ignoreCase = true)
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }

    override fun toString(): String {
        return title
    }
}

class FlightOrderException(
    message: String?,
    val errorList: List<FlightOrderError>
) : MessageErrorException(message) {
    override val message: String
        get() = super.message ?: ""

}
