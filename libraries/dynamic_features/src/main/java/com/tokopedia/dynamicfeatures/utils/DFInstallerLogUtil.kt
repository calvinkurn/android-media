package com.tokopedia.dynamicfeatures.utils

import android.content.Context
import com.tokopedia.dynamicfeatures.constant.CommonConstant
import com.tokopedia.logger.ServerLogger
import com.tokopedia.logger.utils.Priority
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

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
            val messageMap = mutableMapOf<String, String>()
            var successText = success.toString()
            if (!success && errorList.isEmpty()) {
                successText = "NA"
            }

            //Success or error information
            messageMap["type"] = message
            messageMap["mod_name"] = modulesName
            messageMap["success"] = successText
            messageMap["dl_times"] = downloadTimes.toString()
            messageMap["err"] = Utils.getError(success, errorList)

            //Size information
            if (moduleSize >= 0) {
                messageMap["mod_size"] = Utils.getSizeInMB(moduleSize)
            } else {
                messageMap["mod_size"] = "-1"
            }
            val phoneSize = StorageUtils.getTotalInternalSpaceBytes(context)
            if (phoneSize >= 0) {
                messageMap["phone_size"] = Utils.getSizeInMB(phoneSize)
            } else {
                messageMap["phone_size"] = "-1"
            }
            if (freeInternalStorageBeforeDownload >= 0) {
                messageMap["free_bef"] = Utils.getSizeInMB(freeInternalStorageBeforeDownload)
            } else {
                messageMap["free_bef"] = "-1"
            }

            try {
                messageMap["free_aft"] = Utils.getSizeInMB(StorageUtils.getFreeSpaceBytes(context))
            } catch (ignored: Exception) {
                messageMap["free_aft"] = "-1"
            }

            messageMap["cache_size"] = Utils.getSizeInMB(StorageUtils.getInternalCacheSize(context))

            // Additional download information
            messageMap["dl_duration"] = Utils.getDownloadDuration(startDownloadTime, endDownloadTime)
            if (startDownloadPercentage <= 0) {
                messageMap["start_progress"] = "0"
            } else {
                messageMap["start_progress"] = Utils.getFormattedNumber(startDownloadPercentage)
            }
            messageMap["dl_service"] = singletonService.toString()
            messageMap["deeplink"] = deeplink
            messageMap["fallback_url"] = fallbackUrl

            //Play service information
            messageMap["play_str"] = PlayServiceUtils.getPlayStoreVersionName(context)
            messageMap["play_str_l"] = PlayServiceUtils.getPlayStoreLongVersionCode(context).toString()
            messageMap["play_srv"] = PlayServiceUtils.getPlayServiceLongVersionCode(context).toString()
            messageMap["installer_pkg"] = PlayServiceUtils.getInstallerPackageName(context)

            ServerLogger.log(Priority.P1, tag, messageMap)
        }
    }
}