package com.tokopedia.media.preview.data.repository

import android.content.Context
import com.tokopedia.picker.common.utils.ImageCompressor
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.File

interface ImageCompressionRepository {
    fun compress(paths: List<String>): Flow<List<String>>
}

class ImageCompressionRepositoryImpl(
    private val context: Context
) : ImageCompressionRepository {

    override fun compress(paths: List<String>): Flow<List<String>> {
        return flow {
            emit(
                paths.filter {
                    File(it).exists()
                }.map {
                    ImageCompressor.compress(
                        context,
                        it
                    ).toString()
                }
            )
        }
    }

}