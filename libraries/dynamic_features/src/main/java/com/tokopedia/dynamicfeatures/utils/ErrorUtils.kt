package com.tokopedia.dynamicfeatures.utils

import android.content.Context
import com.google.android.play.core.splitinstall.model.SplitInstallErrorCode
import com.tokopedia.dynamicfeatures.constant.CommonConstant
import com.tokopedia.dynamicfeatures.constant.ErrorConstant

object ErrorUtils {

    fun getValidatedErrorCode(context: Context, errCode: String, freeSpace: Long): String {
        var errorCodeTemp = errCode
        if (!Utils.isPlayServiceConnected(context)) {
            errorCodeTemp = ErrorConstant.ERROR_PLAY_SERVICE_NOT_CONNECTED
        } else if (!Utils.isPlayStoreAvailable(context)) {
            errorCodeTemp = ErrorConstant.ERROR_PLAY_STORE_NOT_AVAILABLE
        } else if (SplitInstallErrorCode.INSUFFICIENT_STORAGE.toString() == errorCodeTemp &&
                freeSpace > (2 * CommonConstant.ONE_KB)) {
            errorCodeTemp = ErrorConstant.ERROR_INVALID_INSUFFICIENT_STORAGE
        }
        return errorCodeTemp
    }
}