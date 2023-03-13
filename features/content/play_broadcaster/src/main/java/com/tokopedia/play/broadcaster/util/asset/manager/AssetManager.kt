package com.tokopedia.play.broadcaster.util.asset.manager

import okhttp3.ResponseBody

/**
 * Created By : Jonathan Darwin on March 09, 2023
 */
interface AssetManager {

    suspend fun unzipAndSave(
        responseBody: ResponseBody,
        fileName: String,
        filePath: String,
        folderPath: String,
    ): Boolean
}
