package com.tokopedia.mediauploader.domain

import com.tokopedia.mediauploader.data.FileUploadServices
import com.tokopedia.mediauploader.data.entity.MediaUploader
import com.tokopedia.mediauploader.data.params.MediaUploaderParam
import com.tokopedia.mediauploader.stubUploadFileServices
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class MediaUploaderUseCaseTest {

    private val services = mockk<FileUploadServices>()
    private val useCase = MediaUploaderUseCase(services)
    private var expectedValue = MediaUploader()

    @Test fun `It should be failed to upload image without params`() {
        runBlocking {
            // Given
            val params = MediaUploaderParam()

            // Then
            assertFailsWith<RuntimeException> {
                useCase(params)
            }
        }
    }

    @Test fun `It should success upload image and received uploadId correctly`() {
        runBlocking {
            // Given
            val params = MediaUploaderParam(
                uploadUrl = "/",
                filePath = "image.jpg",
                timeOut = "60",
            )

            // When
            services.stubUploadFileServices(expectedValue)
            val result = useCase(params)

            // Then
            assertEquals(expectedValue.data?.uploadId, result.data?.uploadId)
        }
    }

}