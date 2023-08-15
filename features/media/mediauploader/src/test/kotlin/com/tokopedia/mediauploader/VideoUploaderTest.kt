package com.tokopedia.mediauploader

import com.tokopedia.mediauploader.common.state.UploadResult
import com.tokopedia.mediauploader.image.ImageUploaderManager
import com.tokopedia.mediauploader.util.BaseUploaderTest
import com.tokopedia.mediauploader.video.VideoUploaderManager
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class VideoUploaderTest : BaseUploaderTest() {

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
    fun upload_video_successful() {
        runBlocking {
            // Given
            val sourceId = "abc-xyz"
            val uploadId = "foo-bar"
            val videoUrl = "https://vod.tokopedia.net/sample.m3u8"

            val file = generateMockVideoFile()
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

    @Test
    fun upload_video_fail_when_file_not_found() {
        runBlocking {
            // Given
            val sourceId = "abc-xyz"

            val file = mockNotExistVideoFile()
            val param = useCase.createParams(
                sourceId = sourceId,
                filePath = file
            )

            // When
            coEvery {
                videoUploaderManager.invoke(any(), any(), any(), any(), any(), any())
            } returns UploadResult.Error("")

            every {
                videoUploaderManager.setError(any(), any(), any())
            } returns UploadResult.Error("")

            // Then
            val execute = useCase(param)
            assert(execute is UploadResult.Error)
        }
    }

}
