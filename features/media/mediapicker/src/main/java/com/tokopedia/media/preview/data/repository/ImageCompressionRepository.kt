package com.tokopedia.media.preview.data.repository

import android.content.Context
import com.tokopedia.picker.common.utils.ImageCompressor
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.File

interface ImageCompressionRepository {
    suspend fun compress(paths: List<String>): List<String>
}

class ImageCompressionRepositoryImpl(
    private val context: Context
) : ImageCompressionRepository {

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

}