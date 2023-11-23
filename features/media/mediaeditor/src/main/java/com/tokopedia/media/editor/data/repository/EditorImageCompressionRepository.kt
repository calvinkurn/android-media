package com.tokopedia.media.editor.data.repository

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.media.editor.utils.getEditorSaveFolderSubPath
import com.tokopedia.picker.common.utils.ImageCompressor
import java.io.File
import javax.inject.Inject

interface EditorImageCompressionRepository {
    suspend fun compress(paths: List<String>): List<String>
    suspend fun compress(path: String, maxWidth: Float, maxHeight: Float, quality: Int, shouldSkipProcess: Boolean): String
}

class EditorImageCompressionRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : EditorImageCompressionRepository {

    override suspend fun compress(paths: List<String>): List<String> {
        return paths.filter {
            File(it).exists()
        }.map {
            ImageCompressor.compress(
                context,
                it
            ).toString()
        }
    }

    override suspend fun compress(
        path: String,
        maxWidth: Float,
        maxHeight: Float,
        quality: Int,
        shouldSkipProcess: Boolean
    ): String {
        return if (File(path).exists()) {
            ImageCompressor.compress(
                context,
                path,
                maxWidth = maxWidth,
                maxHeight = maxHeight,
                quality = quality,
                subDirectory = getEditorSaveFolderSubPath(),
                shouldSkipProcess = shouldSkipProcess
            )?.path ?: path
        } else {
            path
        }
    }

}
