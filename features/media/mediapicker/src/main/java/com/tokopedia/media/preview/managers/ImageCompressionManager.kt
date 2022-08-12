package com.tokopedia.media.preview.managers

import android.content.Context
import com.tokopedia.picker.common.utils.ImageCompressor
import com.tokopedia.picker.common.utils.wrapper.PickerFile.Companion.asPickerFile
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.File

interface ImageCompressionManager {
    fun compress(paths: List<String>): Flow<List<String>>
}

class ImageCompressionManagerImpl(
    private val context: Context
) : ImageCompressionManager {

    override fun compress(paths: List<String>): Flow<List<String>> {
        return flow {
            emit(
                paths.filter {
                    val isExist = File(it).exists()
                    val isImage = it.asPickerFile().isImage()

                    isExist && isImage
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