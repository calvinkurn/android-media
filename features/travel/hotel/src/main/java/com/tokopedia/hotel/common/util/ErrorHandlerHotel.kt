package com.tokopedia.hotel.common.util

import android.content.Context
import com.tokopedia.hotel.R
import com.tokopedia.hotel.common.data.HotelErrorException
import java.net.UnknownHostException

/**
 * @author by jessica on 11/06/19
 */

class ErrorHandlerHotel {

    companion object {
        private const val ERROR_VERIFIED_PHONE_MESSAGE = "PhoneNotVerified"
        private const val ERROR_VERIFIED_PHONE_CODE = "68"
        private const val ERROR_GET_FAILED_ROOM_CODE_2 = 362
        private const val ERROR_GET_FAILED_ROOM_CODE = 206
        private const val ERROR_EMAIL_NOT_VERIFIED = 34
        private const val ERROR_ORDER_NOT_FOUND = 77
        private const val ERROR_ORDER_HAS_BEEN_CANCELLED = 143

        fun isOrderNotFound(t: Throwable): Boolean = if (t is HotelErrorException) t.errorCode == ERROR_ORDER_NOT_FOUND else false

        fun isOrderHasBeenCancelled(t: Throwable) = if (t is HotelErrorException) t.errorCode == ERROR_ORDER_HAS_BEEN_CANCELLED else false

        fun isPhoneNotVerfiedError(t: Throwable): Boolean {
            return t.message?.isNotEmpty() ?: false && (t.message == ERROR_VERIFIED_PHONE_MESSAGE || t.message == ERROR_VERIFIED_PHONE_CODE)
        }

        fun isGetFailedRoomError(t: Throwable): Boolean {
            return if (t is HotelErrorException) {
                (t.errorCode == ERROR_GET_FAILED_ROOM_CODE || t.errorCode == ERROR_GET_FAILED_ROOM_CODE_2)
            } else false
        }

        fun isEmailNotVerifiedError(t: Throwable): Boolean {
            return if (t is HotelErrorException) {
                (t.errorCode == ERROR_EMAIL_NOT_VERIFIED)
            } else false
        }

        fun getErrorMessage(context: Context?, e: Throwable?): String {
            return if (context == null || e == null) {
                "Kami akan bereskan secepatnya. Klik tombol di bawah atau balik lagi nanti."
            } else {
                if (e is UnknownHostException) context.getString(R.string.hotel_error_no_internet_connection_subtitle)
                else if (!e.message.isNullOrEmpty()) e.message ?: ""
                else if (!e.localizedMessage.isNullOrEmpty()) e.localizedMessage
                else context.getString(R.string.hotel_error_server_error_subtitle)
            }
        }

        fun getErrorTitle(context: Context?, e: Throwable?): String {
            if (context == null || e == null) {
                return "Ada gangguan di rumah Toped"
            } else if (e is UnknownHostException) return context.getString(R.string.hotel_error_no_internet_connection_title)
            else return context.getString(R.string.hotel_error_server_error_title)
        }

        fun getErrorImage(e: Throwable?): Int {
            if (e == null) {
                return R.drawable.hotel_ic_server_error
            } else if (e is UnknownHostException) return com.tokopedia.globalerror.R.drawable.unify_globalerrors_connection
            else return R.drawable.hotel_ic_server_error
        }
    }
}