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

    private val ASSET_DIR = "assets"
    private val EFFECT_DIR = "effect"

    private val COMPOSE_BUNDLE = "ComposeMakeup.bundle"

    private val COMPOSE_DIR = "ComposeMakeup"
    private val COMPOSE_PRESET_MAKEUP_DIR = "style_makeup"

    private val mResourcePath = context.getExternalFilesDir(ASSET_DIR)?.absolutePath + File.separator + EFFECT_DIR

    val presetPathFileDir: String = mResourcePath.apply {
        appendPath(COMPOSE_BUNDLE)
        appendPath(COMPOSE_DIR)
        appendPath(COMPOSE_PRESET_MAKEUP_DIR)
    }

    fun getPresetFileDir(presetName: String) = presetPathFileDir.appendPath(presetName)

    private fun String.appendPath(path: String): String {
        return this + File.separator + path
    }
}
