package com.tokopedia.effect.util.asset.checker

import com.tokopedia.effect.util.asset.checker.AssetHelper
import java.io.File
import javax.inject.Inject

/**
 * Created By : Jonathan Darwin on March 09, 2023
 */
class AssetCheckerImpl @Inject constructor(
    private val assetHelper: AssetHelper,
) : AssetChecker {

    override suspend fun isLicenseAvailable(licenseName: String): Boolean {
        return File(assetHelper.licenseDir).listFiles()?.any { it.name == licenseName } ?: false
    }

    override suspend fun isModelAvailable(): Boolean {
        return File(assetHelper.modelDir).exists()
    }

    override suspend fun isCustomFaceAvailable(): Boolean {
        return File(assetHelper.customFaceDir).exists()
    }

    override suspend fun isPresetFileAvailable(presetName: String): Boolean {
        val file = File(assetHelper.getPresetFilePath(presetName))
        return file.exists()
    }
}
