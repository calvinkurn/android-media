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

    internal fun logStatus(context: Context,
                           tag: String = CommonConstant.DFM_TAG,
                           message: String = "",
                           modulesName: String,
                           freeInternalStorageBeforeDownload: Long = 0,
                           moduleSize: Long = 0,
                           errorList: List<String> = emptyList(),
                           downloadTimes: Int = 1,
                           success: Boolean = false,
                           startDownloadTime: Long = 0L,
                           endDownloadTime: Long = 0L,
                           startDownloadPercentage: Float = -1f,
                           singletonService: Boolean = true,
                           deeplink: String = "",
                           fallbackUrl: String = "") {

        GlobalScope.launch(Dispatchers.IO + CoroutineExceptionHandler { _, _ -> }) {
            val messageBuilder = StringBuilder()
            var successText = success.toString()
            if (!success && errorList.isEmpty()) {
                successText = "NA"
            }

            //Success or error information
            messageBuilder.append(message)
            messageBuilder.append(";mod_name=$modulesName")
            messageBuilder.append(";success=$successText")
            messageBuilder.append(";dl_times=$downloadTimes")
            messageBuilder.append(";err='${Utils.getError(success, errorList)}'")

            //Size information
            messageBuilder.append(";mod_size=")
            if (moduleSize >= 0) {
                messageBuilder.append(Utils.getSizeInMB(moduleSize))
            } else {
                messageBuilder.append(-1)
            }
            messageBuilder.append(";phone_size=")
            val phoneSize = StorageUtils.getTotalInternalSpaceBytes(context)
            if (phoneSize >= 0) {
                messageBuilder.append(Utils.getSizeInMB(phoneSize))
            } else {
                messageBuilder.append(-1)
            }
            messageBuilder.append(";free_bef=")
            if (freeInternalStorageBeforeDownload >= 0) {
                messageBuilder.append(Utils.getSizeInMB(freeInternalStorageBeforeDownload))
            } else {
                messageBuilder.append(-1)
            }
            messageBuilder.append(";free_aft=")
            try {
                messageBuilder.append(Utils.getSizeInMB(StorageUtils.getFreeSpaceBytes(context)))
            } catch (ignored: Exception) {
                messageBuilder.append(-1)
            }
            messageBuilder.append(";cache_size='${Utils.getSizeInMB(StorageUtils.getInternalCacheSize(context))}'")

            // Additional download information
            messageBuilder.append(";dl_duration=${Utils.getDownloadDuration(startDownloadTime, endDownloadTime)}")
            messageBuilder.append(";start_progress=")
            if (startDownloadPercentage <= 0) {
                messageBuilder.append("0")
            } else {
                messageBuilder.append(Utils.getFormattedNumber(startDownloadPercentage))
            }
            messageBuilder.append(";dl_service=$singletonService")
            messageBuilder.append(";deeplink='$deeplink'")
            messageBuilder.append(";fallback_url='$fallbackUrl'")

            //Play service information
            messageBuilder.append(";play_str='${PlayServiceUtils.getPlayStoreVersionName(context)}'")
            messageBuilder.append(";play_str_l=${PlayServiceUtils.getPlayStoreLongVersionCode(context)}")
            messageBuilder.append(";play_srv=${PlayServiceUtils.getPlayServiceLongVersionCode(context)}")
            messageBuilder.append(";installer_pkg=${PlayServiceUtils.getInstallerPackageName(context)}")

            Timber.w("P1#$tag#$messageBuilder")
        }
    }
}