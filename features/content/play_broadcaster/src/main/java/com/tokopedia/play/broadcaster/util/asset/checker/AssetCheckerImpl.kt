package com.tokopedia.play.broadcaster.util.asset.checker

import com.tokopedia.play.broadcaster.util.asset.AssetPathHelper
import java.io.File
import javax.inject.Inject

/**
 * Created By : Jonathan Darwin on March 09, 2023
 */
class AssetCheckerImpl @Inject constructor(
    private val assetPathHelper: AssetPathHelper,
) : AssetChecker {

    override suspend fun isLicenseAvailable(licenseName: String): Boolean {
        return File(assetPathHelper.licenseDir).listFiles()?.any { it.name == licenseName } ?: false
    }

    override suspend fun isPresetFileAvailable(presetName: String): Boolean {
        val file = File(assetPathHelper.getPresetFilePath(presetName))
        return file.exists()
    }
}
