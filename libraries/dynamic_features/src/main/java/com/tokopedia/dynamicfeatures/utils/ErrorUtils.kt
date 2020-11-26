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
        return errorCodeTemp
    }
}