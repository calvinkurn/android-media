package com.tokopedia.mediauploader

import com.tokopedia.mediauploader.common.state.UploadResult
import com.tokopedia.mediauploader.common.util.isVideoFormat
import com.tokopedia.mediauploader.image.ImageUploaderManager
import com.tokopedia.mediauploader.video.VideoUploaderManager
import com.tokopedia.picker.common.utils.fileExtension
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.mockkStatic
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class LargeVideoUploaderTest : CompressionTest() {

    private val imageUploaderManager = mockk<ImageUploaderManager>()
    private val videoUploaderManager = mockk<VideoUploaderManager>()

    private lateinit var useCase: UploaderUseCase

    @Before
    fun setUp() {
        useCase = UploaderUseCase(
            imageUploaderManager,
            videoUploaderManager
        )
    }

    @Test
    fun upload_large_video_successful() {
        runBlocking {
            // Given
            val sourceId = "abc-xyz"
            val uploadId = "foo-bar"
            val videoUrl = "https://vod.tokopedia.net/sample.m3u8"

            val file = generateMockVideoFile(
                length = 1_000_000_000
            )
            val param = useCase.createParams(
                sourceId = sourceId,
                filePath = file
            )

            // When
            coEvery {
                videoUploaderManager.invoke(any(), any(), any(), any(), any(), any())
            } returns UploadResult.Success(
                uploadId = uploadId,
                videoUrl = videoUrl
            )

            // Then
            val execute = useCase(param) as UploadResult.Success

            assert(execute.uploadId == uploadId)
            assert(execute.videoUrl == videoUrl)
        }
    }

}
