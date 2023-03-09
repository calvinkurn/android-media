package com.tokopedia.play.broadcaster.util.asset

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import java.io.File
import javax.inject.Inject

/**
 * Created By : Jonathan Darwin on March 09, 2023
 */
class AssetPathHelper @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val mResourcePath = context.getExternalFilesDir(ASSET_DIR)?.absolutePath + File.separator + EFFECT_DIR

    val presetDir: String = mResourcePath
        .appendPath(COMPOSE_BUNDLE)
        .appendPath(COMPOSE_DIR)
        .appendPath(COMPOSE_PRESET_MAKEUP_DIR)

    val licenseDir = mResourcePath
        .appendPath(LICENSE_BUNDLE)

    fun getPresetFilePath(presetName: String) = presetDir.appendPath(presetName)

    private fun String.appendPath(path: String): String {
        return this + File.separator + path
    }

    companion object {
        private const val ASSET_DIR = "assets"
        private const val EFFECT_DIR = "effect"

        private const val COMPOSE_BUNDLE = "ComposeMakeup.bundle"
        private const val LICENSE_BUNDLE = "LicenseBag.bundle"

        private const val COMPOSE_DIR = "ComposeMakeup"
        private const val COMPOSE_PRESET_MAKEUP_DIR = "style_makeup"
    }
}
