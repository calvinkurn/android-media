package com.tokopedia.mediauploader.video.internal

import android.content.Context
import android.net.Uri
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.mediauploader.common.compressor.CompressionProgressListener
import com.tokopedia.mediauploader.common.compressor.Compressor
import com.tokopedia.mediauploader.common.compressor.data.Configuration
import com.tokopedia.mediauploader.common.compressor.data.Result
import com.tokopedia.mediauploader.common.state.ProgressType
import com.tokopedia.mediauploader.common.state.ProgressUploader
import com.tokopedia.utils.file.FileUtil
import com.tokopedia.utils.image.ImageProcessingUtil
import java.io.File
import javax.inject.Inject

interface VideoCompressor {
    suspend fun compress(
        path: String,
        configuration: Configuration,
        progressUploader: ProgressUploader?
    ): Result
}

class VideoCompressorImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : VideoCompressor {

    override suspend fun compress(
        path: String,
        configuration: Configuration,
        progressUploader: ProgressUploader?
    ): Result {
        val compressedFile = compressedVideoPath(path)

        return Compressor.compressVideo(
            context = context,
            srcUri = Uri.parse(path),
            destination = compressedFile.path,
            configuration = configuration,
            listener = object : CompressionProgressListener {
                override fun onProgressChanged(percent: Float) {
                    progressUploader?.onProgress(percent.toInt(), ProgressType.Compression)
                }
            }
        )
    }

    private fun compressedVideoPath(originalPath: String): File {
        val currentTime = System.currentTimeMillis()

        // internal app storage
        val internalAppCacheDir = FileUtil
            .getTokopediaInternalDirectory(ImageProcessingUtil.DEFAULT_DIRECTORY)
            .absolutePath

        // get the file name from original path
        val fileName = File(originalPath).nameWithoutExtension

        return File(
            internalAppCacheDir,
            "compressed_${fileName}_${currentTime}.mp4"
        )
    }
}
