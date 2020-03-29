package com.tokopedia.dynamicfeatures.utils

import com.tokopedia.dynamicfeatures.constant.CommonConstant

object Utils {

    fun getDownloadDuration(startDownloadTime:Long, endDownloadTime:Long): String {
        val diffTime = if (startDownloadTime > 0) {
            (endDownloadTime - startDownloadTime).toFloat() / 1000
        } else {
            0f
        }
        return String.format("%.2f", diffTime)
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