package com.tokopedia.dynamicfeatures.utils

import android.content.Context
import com.tokopedia.dynamicfeatures.constant.CommonConstant
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import timber.log.Timber

/**
 * Created by hendry on 2019-10-03.
 */
object DFInstallerLogUtil {
    const val DFM_TAG = "DFM"

    internal fun logStatus(context: Context,
                           message: String = "",
                           modulesName: String,
                           freeInternalStorageBeforeDownload: Long = 0,
                           moduleSize: Long = 0,
                           errorList: List<String> = emptyList(),
                           downloadTimes: Int = 1,
                           isSuccess: Boolean = false,
                           tag: String = DFM_TAG,
                           duration: Long = 0L,
                           progressPercentFirstTime:String = "") {

        GlobalScope.launch(Dispatchers.IO + CoroutineExceptionHandler { _, _ -> }) {
            val messageBuilder = StringBuilder()
            messageBuilder.append(message)

            messageBuilder.append(";mod_name=$modulesName")

            messageBuilder.append(";success=$isSuccess")

            messageBuilder.append(";dl_times=$downloadTimes")

            messageBuilder.append(";err='${getError(errorList)}'")

            messageBuilder.append(";mod_size=")
            if (moduleSize > 0) {
                messageBuilder.append(getSizeInMB(moduleSize))
            } else {
                messageBuilder.append(-1)
            }

            messageBuilder.append(";phone_size=")
            val phoneSize = StorageUtils.getTotalInternalSpaceBytes(context)
            if (phoneSize > 0) {
                messageBuilder.append(getSizeInMB(phoneSize))
            } else {
                messageBuilder.append(-1)
            }

            messageBuilder.append(";free_bef=")
            if (freeInternalStorageBeforeDownload > 0) {
                messageBuilder.append(getSizeInMB(freeInternalStorageBeforeDownload))
            } else {
                messageBuilder.append(-1)
            }

            messageBuilder.append(";free_aft=")
            try {
                messageBuilder.append(getSizeInMB(StorageUtils.getFreeSpaceBytes(context)))
            } catch (ignored: Exception) {
                messageBuilder.append(-1)
            }
            messageBuilder.append(";cache_size='${getSizeInMB(StorageUtils.getInternalCacheSize(context))}'")
            messageBuilder.append(";duration=${duration}")

            messageBuilder.append(";firstProgress=")
            if (progressPercentFirstTime.isEmpty()) {
                messageBuilder.append("0")
            } else {
                messageBuilder.append(progressPercentFirstTime)
            }

            messageBuilder.append(";play_str='${Utils.getPlayStoreVersionName(context)}'")
            messageBuilder.append(";play_str_l=${Utils.getPlayStoreLongVersionCode(context)}")
            messageBuilder.append(";play_srv=${Utils.getPlayServiceLongVersionCode(context)}")
            messageBuilder.append(";installer_pkg=${Utils.getInstallerPackageName(context)}")

            Timber.w("P1#$tag#$messageBuilder")
        }
    }

    private fun getSizeInMB(size: Long): String {
        return String.format("%.2f", size.toDouble() / CommonConstant.MEGA_BYTE)
    }

    private fun getError(errorList: List<String>): String {
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