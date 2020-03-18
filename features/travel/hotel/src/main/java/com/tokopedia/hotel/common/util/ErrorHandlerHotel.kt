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

        fun isPhoneNotVerfiedError(t: Throwable): Boolean {
            return t.message?.isNotEmpty() ?: false && (t.message == ERROR_VERIFIED_PHONE_MESSAGE || t.message == ERROR_VERIFIED_PHONE_CODE)
        }

        fun isGetFailedRoomError(t: Throwable): Boolean {
            return if (t is HotelErrorException) {
                (t.errorCode == ERROR_GET_FAILED_ROOM_CODE || t.errorCode == ERROR_GET_FAILED_ROOM_CODE_2)
            } else false
        }

        fun getErrorMessage(context: Context?, e: Throwable?): String {
            if (context == null || e == null) {
                return "Kami akan bereskan secepatnya. Klik tombol di bawah atau balik lagi nanti."
            } else {
                if (e is UnknownHostException) return context.getString(R.string.hotel_error_no_internet_connection_subtitle)
                else if (e.message.isNullOrEmpty() && e.localizedMessage.length <= 3) return e.message ?: ""
                else return context.getString(R.string.hotel_error_server_error_subtitle)
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
            } else if (e is UnknownHostException) return R.drawable.hotel_ic_no_internet_connection
            else return R.drawable.hotel_ic_server_error
        }
    }
}