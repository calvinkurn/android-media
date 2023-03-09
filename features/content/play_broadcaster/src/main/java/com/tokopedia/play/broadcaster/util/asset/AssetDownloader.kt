package com.tokopedia.play.broadcaster.util.asset

/**
 * Created By : Jonathan Darwin on March 09, 2023
 */
interface AssetDownloader {

    suspend fun downloadUnzip(
        fileUrl: String,
        fileName: String,
        filePath: String,
    ): Boolean
}
