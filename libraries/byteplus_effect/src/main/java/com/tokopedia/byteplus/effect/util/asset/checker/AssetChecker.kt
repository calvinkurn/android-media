package com.tokopedia.byteplus.effect.util.asset.checker

/**
 * Created By : Jonathan Darwin on March 09, 2023
 */
interface AssetChecker {

    fun isLicenseAvailable(licenseName: String): Boolean

    fun isModelAvailable(): Boolean

    fun isCustomFaceAvailable(): Boolean

    fun isPresetFileAvailable(fileName: String): Boolean
}
