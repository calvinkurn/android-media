package com.tokopedia.mediauploader.video

import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.mediauploader.UploaderManager
import com.tokopedia.mediauploader.common.data.consts.*
import com.tokopedia.mediauploader.common.internal.SourcePolicyManager
import com.tokopedia.mediauploader.common.internal.VideoCompressionCacheManager
import com.tokopedia.mediauploader.common.state.ProgressUploader
import com.tokopedia.mediauploader.common.state.UploadResult
import com.tokopedia.mediauploader.common.util.isMaxFileSize
import com.tokopedia.mediauploader.common.util.mbToBytes
import com.tokopedia.mediauploader.video.data.entity.VideoPolicy
import com.tokopedia.mediauploader.video.data.params.VideoCompressionParam
import com.tokopedia.mediauploader.video.domain.GetVideoPolicyUseCase
import com.tokopedia.mediauploader.video.domain.SetVideoCompressionUseCase
import timber.log.Timber
import java.io.File
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class VideoUploaderManager @Inject constructor(
    private val policyManager: SourcePolicyManager,
    private val policyUseCase: GetVideoPolicyUseCase,
    private val videoCompression: SetVideoCompressionUseCase,
    private val simpleUploaderManager: SimpleUploaderManager,
    private val largeUploaderManager: LargeUploaderManager,
    private val compressionCacheManager: VideoCompressionCacheManager
) : UploaderManager {

    private var isSimpleUpload = true

    suspend operator fun invoke(
        file: File,
        sourceId: String,
        loader: ProgressUploader?,
        withTranscode: Boolean,
        shouldCompress: Boolean
    ): UploadResult {
        if (sourceId.isEmpty()) return UploadResult.Error(SOURCE_NOT_FOUND)

        // hit uploader policy
        val policy = policyUseCase(sourceId)
        policyManager.set(policy)

        // compress video if needed
        val filePath = onVideoCompression(
            originalFile = file,
            sourceId = sourceId,
            shouldCompress = shouldCompress,
            policy = policy.videoPolicy,
            loader = loader
        )

        // return the upload result
        return policy.videoPolicy?.let { videoPolicy ->
            val maxSizeOfSimpleUpload = videoPolicy.thresholdSizeOfVideo()
            val maxFileSize = videoPolicy.maxFileSize

            when {
                !filePath.exists() -> {
                    UploadResult.Error(FILE_NOT_FOUND)
                }
                filePath.isMaxFileSize(maxFileSize) -> {
                    UploadResult.Error(maxFileSizeMessage(maxFileSize))
                }
                !allowedExt(filePath.path, videoPolicy.extension) -> {
                    UploadResult.Error(formatNotAllowedMessage(videoPolicy.extension))
                }
                else -> {
                    val startUploadTime = System.currentTimeMillis()
                    val cacheResult = compressionCacheManager.get(sourceId, filePath.path)

                    isSimpleUpload = filePath.length() <= maxSizeOfSimpleUpload.mbToBytes()
                    setProgressUploader(loader)

                    if (!isSimpleUpload) {
                        largeUploaderManager(filePath, sourceId, withTranscode).also {
                            val endUploadTime = System.currentTimeMillis()
                            Timber.d("[MEDIA-UPLOADER] [${file.name}] Compress file-name: ${filePath.name} | upload time: ${TimeUnit.SECONDS.toSeconds(endUploadTime - startUploadTime)}")
                            Timber.d("[MEDIA-UPLOADER] [${file.name}] Compress data: $cacheResult | compress time in sec: ${TimeUnit.SECONDS.toSeconds(cacheResult?.compressedTime ?: 0)}")
                        }
                    } else {
                        simpleUploaderManager(filePath, sourceId, withTranscode).also {
                            val endUploadTime = System.currentTimeMillis()
                            Timber.d("[MEDIA-UPLOADER] [${file.name}] Compress file-name: ${filePath.name} | upload time: ${TimeUnit.SECONDS.toSeconds(endUploadTime - startUploadTime)}")
                            Timber.d("[MEDIA-UPLOADER] [${file.name}] Compress data: $cacheResult | compress time in sec: ${TimeUnit.SECONDS.toSeconds(cacheResult?.compressedTime ?: 0)}")
                        }
                    }
                }
            }
        } ?: UploadResult.Error(UNKNOWN_ERROR)
    }

    suspend fun abortUpload(sourceId: String, fileName: String, abort: suspend () -> Unit) {
        if (isSimpleUpload) error("the upload abort behavior only supported on large video file")

        largeUploaderManager.abortUpload(sourceId, fileName) {
            abort()
        }
    }

    override fun setProgressUploader(progress: ProgressUploader?) {
        if (isSimpleUpload) {
            simpleUploaderManager.setProgressCallback(progress)
        } else {
            largeUploaderManager.setProgressCallback(progress)
        }
    }

    private suspend fun onVideoCompression(
        sourceId: String,
        originalFile: File,
        shouldCompress: Boolean,
        policy: VideoPolicy?,
        loader: ProgressUploader?
    ): File {
        // check if the policy exist, otherwise return the original file path
        val compressionPolicy = policy?.videoCompression ?: return originalFile

        // the toggle comes from policy
        val isCompressionEnabled = compressionPolicy.isCompressionEnabled

        // remote config
        val isCompressionRemoteConfigEnabled = true

        return if (shouldCompress && isCompressionEnabled && isCompressionRemoteConfigEnabled) {
            val originalFileSizeInMb = originalFile.length() / (1024 * 1024)

            if (originalFileSizeInMb < compressionPolicy.thresholdInMb.orZero()) {
                return originalFile
            }

            val param = VideoCompressionParam(
                sourceId = sourceId,
                videoPath = originalFile.path,
                bitrate = compressionPolicy.maxBitrateBps,
                resolution = compressionPolicy.maxResolution
            )

            videoCompression.progressUploader = loader
            File(videoCompression(param))
        } else {
            originalFile
        }
    }

}
