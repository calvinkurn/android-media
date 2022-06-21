package com.tokopedia.media.preview.managers

import android.content.Context
import com.tokopedia.picker.common.utils.ImageCompressor
import com.tokopedia.picker.common.utils.isVideoFormat
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

interface ImageCompressionManager {
    fun compress(paths: List<String>): Flow<List<String>>
}

class ImageCompressionManagerImpl(
    private val context: Context
) : ImageCompressionManager {

    override fun compress(paths: List<String>): Flow<List<String>> {
        return flow {
            emit(
                paths
                    .filterNot { isVideoFormat(it) }
                    .map {
                        ImageCompressor.compress(
                            context,
                            it
                        ).toString()
                    }
            )
        }
    }

}