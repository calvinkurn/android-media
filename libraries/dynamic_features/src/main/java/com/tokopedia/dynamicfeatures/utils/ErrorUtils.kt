package com.tokopedia.dynamicfeatures.utils

import android.content.Context
import com.google.android.play.core.splitinstall.model.SplitInstallErrorCode
import com.tokopedia.dynamicfeatures.config.DFRemoteConfig
import com.tokopedia.dynamicfeatures.constant.CommonConstant
import com.tokopedia.dynamicfeatures.constant.ErrorConstant

object ErrorUtils {

    fun getValidatedErrorCode(context: Context, errCode: String, freeInternalStorage: Long): String {
        var errorCodeTemp = errCode
        if ( errorCodeTemp == SplitInstallErrorCode.INSUFFICIENT_STORAGE.toString()) {
            val maxThresholdInsufficientStorage = DFRemoteConfig.getConfig(context).maxThresholdInsufficientStorage
            if (maxThresholdInsufficientStorage > 0 &&
                    freeInternalStorage > (maxThresholdInsufficientStorage * CommonConstant.MEGA_BYTE)) {
                errorCodeTemp = ErrorConstant.ERROR_INVALID_INSUFFICIENT_STORAGE
            }
        }
        if (errorCodeTemp.isEmpty() || errorCodeTemp == SplitInstallErrorCode.NO_ERROR.toString()) {
            if (!PlayServiceUtils.isPlayServiceConnected(context)) {
                errorCodeTemp = ErrorConstant.ERROR_PLAY_SERVICE_NOT_CONNECTED
            } else if (!PlayServiceUtils.isPlayStoreAvailable(context)) {
                errorCodeTemp = ErrorConstant.ERROR_PLAY_STORE_NOT_AVAILABLE
            }
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