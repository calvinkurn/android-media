package com.tokopedia.play.broadcaster.util.asset.manager

/**
 * Created By : Jonathan Darwin on March 09, 2023
 */
interface AssetManager {

    suspend fun downloadUnzip(
        url: String,
        fileName: String,
        filePath: String,
        folderPath: String,
    ): Boolean
}
