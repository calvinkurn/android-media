package com.tokopedia.play.broadcaster.util.asset

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import java.io.File
import javax.inject.Inject

/**
 * Created By : Jonathan Darwin on March 14, 2023
 */
class AssetHelper @Inject constructor(
    @ApplicationContext private val context: Context
){
    val effectRootDir = context.getExternalFilesDir(ASSET_DIR)?.absolutePath + File.separator + EFFECT_DIR

    val licenseDir = effectRootDir
        .appendPath(LICENSE_BUNDLE)

    val modelDir = effectRootDir
        .appendPath(MODEL_BUNDLE)

    val composeMakeupDir = effectRootDir
        .appendPath(COMPOSE_BUNDLE)
        .appendPath(COMPOSE_DIR)

    val customFaceDir = composeMakeupDir
        .appendPath(COMPOSE_CUSTOM_FACE_MAKEUP_DIR)

    val presetDir: String = composeMakeupDir
        .appendPath(COMPOSE_PRESET_MAKEUP_DIR)

    fun getPresetFilePath(presetName: String) = presetDir.appendPath(presetName)

    private fun String.appendPath(path: String): String {
        return this + File.separator + path
    }

    fun getFileNameFromLink(link: String): String {
        return link.substring(link.lastIndexOf(PATH_SEPARATOR) + 1)
    }

    fun getFileNameFromLinkWithoutExtension(link: String): String {
        val fileName = getFileNameFromLink(link)
        return fileName.substring(0, fileName.lastIndexOf(EXTENSION_SEPARATOR))
    }

    companion object {
        private const val ASSET_DIR = "assets"
        private const val EFFECT_DIR = "effect"

        private const val LICENSE_BUNDLE = "LicenseBag.bundle"
        private const val MODEL_BUNDLE = "ModelResource.bundle"
        private const val COMPOSE_BUNDLE = "ComposeMakeup.bundle"

        private const val COMPOSE_DIR = "ComposeMakeup"
        private const val COMPOSE_CUSTOM_FACE_MAKEUP_DIR = "beauty_Android_standard"
        private const val COMPOSE_PRESET_MAKEUP_DIR = "style_makeup"

        private const val PATH_SEPARATOR = "/"
        private const val EXTENSION_SEPARATOR = "."
    }
}
