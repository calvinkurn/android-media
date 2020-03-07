package com.tokopedia.dynamicfeatures.utils

import android.content.Context
import com.google.android.play.core.splitinstall.model.SplitInstallErrorCode
import com.tokopedia.dynamicfeatures.constant.CommonConstant
import com.tokopedia.dynamicfeatures.constant.ErrorConstant

object ErrorUtils {

    private const val MIN_INVALID_INSUFFICIENT_STORAGE = 2 * 1024L

    fun getValidatedErrorCode(context: Context, errCode: String, freeSpace: Long): String {
        var errorCodeTemp = errCode
        if (!Utils.isPlayServiceConnected(context)) {
            errorCodeTemp = ErrorConstant.ERROR_PLAY_SERVICE_NOT_CONNECTED
        } else if (!Utils.isPlayStoreAvailable(context)) {
            errorCodeTemp = ErrorConstant.ERROR_PLAY_STORE_NOT_AVAILABLE
        } else if (SplitInstallErrorCode.INSUFFICIENT_STORAGE.toString() == errorCodeTemp &&
                freeSpace > MIN_INVALID_INSUFFICIENT_STORAGE) {
            errorCodeTemp = ErrorConstant.ERROR_INVALID_INSUFFICIENT_STORAGE
        }
        return errorCodeTemp
    }

    fun getValidatedErrorCode(context: Context, errorCodeList: List<String>, freeSpace: Long): List<String> {
        if (errorCodeList.isEmpty()) {
            return errorCodeList
        }
        val errorCodeListTemp = mutableListOf<String>()
        for(errorCode in errorCodeList){
            errorCodeListTemp.add(getValidatedErrorCode(context, errorCode, freeSpace))
        }
        return errorCodeListTemp
    }
}