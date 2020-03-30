package com.tokopedia.dynamicfeatures.utils

import com.tokopedia.dynamicfeatures.constant.CommonConstant

object Utils {

    fun getFormattedNumber(number: Float, format: String = "%.2f") : String {
        return String.format(format, number)
    }

    fun getDownloadDuration(startDownloadTime:Long, endDownloadTime:Long): String {
        val diffTime = 0f
        if (startDownloadTime in 1 until endDownloadTime) {
            (endDownloadTime - startDownloadTime).toFloat() / 1000
        }
        return getFormattedNumber(diffTime)
    }

    fun getSizeInMB(size: Long): String {
        return String.format("%.2f", size.toDouble() / CommonConstant.MEGA_BYTE)
    }

    fun getError(errorList: List<String>): String {
        if (errorList.isEmpty()) {
            return "0"
        }
        var errorText = errorList.first()
        for (error in errorList) if (error != errorText) {
            errorText = errorList.joinToString("|")
            break
        }
        return errorText
    }
}