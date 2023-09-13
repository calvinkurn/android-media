package com.tokopedia.mediauploader.data.repository

import android.content.Context
import android.content.Intent
import androidx.core.content.FileProvider
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.kotlin.extensions.view.formattedToMB
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.mediauploader.video.internal.VideoMetaDataExtractor
import com.tokopedia.mediauploader.common.state.UploadResult
import com.tokopedia.mediauploader.data.entity.Logs
import com.tokopedia.mediauploader.common.data.store.datastore.AnalyticsCacheDataStore
import com.tokopedia.mediauploader.common.di.UploaderQualifier
import com.tokopedia.picker.common.utils.wrapper.PickerFile.Companion.asPickerFile
import java.io.File
import java.util.concurrent.TimeUnit
import javax.inject.Inject

interface LogRepository {
    fun fileInfo(path: String): List<Logs>

    suspend fun uploadResult(result: UploadResult, sourceId: String, path: String): List<Logs>
    suspend fun compressionInfo(sourceId: String, path: String): List<Logs>
}

class LogRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    @UploaderQualifier private val videoMetaDataExtractor: VideoMetaDataExtractor,
    @UploaderQualifier private val trackerCacheStore: AnalyticsCacheDataStore,
) : LogRepository {

    /**
     * @sample
     * Upload ID: f00-b4r-lor3n-1psum
     * Video Url: https://vod.tokopedia.net/tests.m3u8
     * Image Url: https://images.tokopedia.net/test.webp
     */
    override suspend fun uploadResult(result: UploadResult, sourceId: String, path: String): List<Logs> {
        return when (result) {
            is UploadResult.Success -> {
                listOf(Logs("Upload ID", result.uploadId))
                    .toMutableList()
                    .also {
                        if (result.videoUrl.isNotEmpty()) {
                            val key = trackerCacheStore.key(sourceId, path)
                            val cached = trackerCacheStore.getData(key)

                            it.add(Logs("Video Url", result.videoUrl))

                            if (cached != null) {
                                val uploadTime = TimeUnit.MILLISECONDS.toSeconds(
                                    cached.endUploadTime - cached.startUploadTime
                                )

                                it.add(Logs("Upload Time", "$uploadTime sec"))
                            }
                        }

                        if (result.fileUrl.isNotEmpty()) {
                            it.add(Logs("Image Url", result.fileUrl))
                        }
                    }
            }
            is UploadResult.Error -> {
                listOf(Logs("Gagal", result.message))
            }
        }
    }

    /**
     * @sample
     * the [compressionInfo] have exact data from [fileInfo], with addition:
     * Compression Time: 12 sec
     */
    override suspend fun compressionInfo(sourceId: String, path: String): List<Logs> {
        val key = trackerCacheStore.key(sourceId, path)
        val cached = trackerCacheStore.getData(key) ?: return emptyList()

        return fileInfo(cached.compressedVideoPath)
            .toMutableList()
            .also { log ->
                val compressedTime = TimeUnit.MILLISECONDS.toSeconds(
                    cached.endCompressedTime - cached.startCompressedTime
                )

                log.add(Logs("Compression Time", "$compressedTime sec"))
            }
    }

    /**
     * @sample
     * Path: /file/path/sample/test.mp4
     * Size: 123 MB
     *
     * additional if the file is video:
     * Bitrate: 1000000 bps
     * Resolution: 100 x 100 px
     */
    override fun fileInfo(path: String): List<Logs> {
        val isVideo = path.asPickerFile().isVideo()
        val fileSize = File(path).length().formattedToMB()

        val file = FileProvider.getUriForFile(
            context,
            context.packageName + ".provider",
            File(path)
        )

        val ctaButton = Pair(
            "Browse",
            Intent(Intent.ACTION_VIEW, file).also {
                it.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }
        )

        return mutableListOf(
            Logs("Path", path, ctaButton),
            Logs("Size", "$fileSize MB"),
        ).also {
            if (isVideo) {
                val metadata = videoMetaDataExtractor.extract(path)

                val width = metadata?.width.orZero()
                val height = metadata?.height.orZero()

                val bitrate = metadata?.bitrate.orZero() / 1024 // bps -> kbps

                it.add(Logs("Bitrate", "$bitrate kbps"))
                it.add(Logs("Resolution", "$width x $height px"))
            }
        }
    }
}
