package com.tokopedia.play.broadcaster.util.asset

/**
 * Created By : Jonathan Darwin on March 09, 2023
 */
interface AssetChecker {

    suspend fun isLicenseAvailable(): Boolean
}
