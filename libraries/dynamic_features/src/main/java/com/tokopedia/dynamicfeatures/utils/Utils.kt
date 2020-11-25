package com.tokopedia.dynamicfeatures.utils

import com.tokopedia.dynamicfeatures.constant.CommonConstant
import com.tokopedia.dynamicfeatures.constant.ErrorConstant
import java.util.*

object Utils {

    fun getFormattedNumber(number: Float, format: String = "%.2f", locale: Locale = Locale.ENGLISH) : String {
        return String.format(locale, format, number)
    }

    fun getSizeInMB(size: Long): String {
        return getFormattedNumber(size.toFloat() / CommonConstant.MEGA_BYTE)
    }

    fun getDownloadDuration(startDownloadTime:Long, endDownloadTime:Long): String {
        var diffTime = 0f
        if (startDownloadTime in 1 until endDownloadTime) {
            diffTime = (endDownloadTime - startDownloadTime).toFloat() / 1000
        }
        return getFormattedNumber(diffTime)
    }

    fun getError(success:Boolean, errorList: List<String>): String {
        if (success) {
            return ErrorConstant.ERROR_SUCCESS
        }
        if (errorList.isEmpty()) {
            return ErrorConstant.ERROR_NOT_AVAILABLE
        }
        var errorText = errorList.first()
        for (error in errorList) if (error != errorText) {
            errorText = errorList.joinToString("|")
            break
        }
        return errorText
    }
}