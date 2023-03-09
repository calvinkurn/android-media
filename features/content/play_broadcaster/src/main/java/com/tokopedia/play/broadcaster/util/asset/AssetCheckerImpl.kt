package com.tokopedia.play.broadcaster.util.asset

import android.content.Context
import java.io.File
import javax.inject.Inject

/**
 * Created By : Jonathan Darwin on March 09, 2023
 */
class AssetCheckerImpl @Inject constructor(
    private val context: Context,
) : AssetChecker {

    private val LICENSE_BUNDLE = "tokopedia_20221231_20231231_com.tokopedia.tkpd_4.2.3.licbag"
    private val mResourcePath = context.getExternalFilesDir("assets")?.absolutePath + File.separator + "effect"

    override suspend fun isLicenseAvailable(): Boolean {
        val file = File(mResourcePath + File.separator + LICENSE_BUNDLE)
        return file.exists()
    }
}
