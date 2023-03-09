package com.tokopedia.play.broadcaster.util.asset

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import java.io.File
import javax.inject.Inject

/**
 * Created By : Jonathan Darwin on March 09, 2023
 */
class AssetCheckerImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val assetPathHelper: AssetPathHelper,
) : AssetChecker {

    override suspend fun isPresetFileAvailable(presetName: String): Boolean {
        val file = File(assetPathHelper.getPresetFileDir(presetName))
        return file.exists()
    }
}
