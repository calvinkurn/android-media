package com.tokopedia.play.broadcaster.util.asset.checker

/**
 * Created By : Jonathan Darwin on March 09, 2023
 */
interface AssetChecker {

    suspend fun isLicenseAvailable(licenseName: String): Boolean

    suspend fun isPresetFileAvailable(fileName: String): Boolean
}
