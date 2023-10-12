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

class ImageUploaderTest : BaseUploaderTest() {

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
    fun upload_image_successful() {
        runBlocking {
            // Given
            val sourceId = "abc-xyz"
            val uploadId = "foo-bar"

            val file = generateMockImageFile()
            val param = useCase.createParams(
                sourceId = sourceId,
                filePath = file
            )

            // When
            coEvery {
                imageUploaderManager.invoke(any(), any(), any(), any(), any(), any())
            } returns UploadResult.Success(uploadId)

            // Then
            val execute = useCase(param) as UploadResult.Success
            assert(execute.uploadId == uploadId)
        }
    }

    @Test
    fun upload_image_fail_when_file_not_found() {
        runBlocking {
            // Given
            val sourceId = "abc-xyz"

            val file = mockNotExistImageFile()
            val param = useCase.createParams(
                sourceId = sourceId,
                filePath = file
            )

            // When
            coEvery {
                imageUploaderManager.invoke(any(), any(), any(), any(), any(), any())
            } returns UploadResult.Error("")

            every {
                imageUploaderManager.setError(any(), any(), any())
            } returns UploadResult.Error("")

            // Then
            val execute = useCase(param)
            assert(execute is UploadResult.Error)
        }
    }
}
