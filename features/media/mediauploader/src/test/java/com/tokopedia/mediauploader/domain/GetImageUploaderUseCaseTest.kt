package com.tokopedia.mediauploader.domain

import com.tokopedia.mediauploader.data.ImageUploadServices
import com.tokopedia.mediauploader.data.entity.MediaUploader
import com.tokopedia.mediauploader.data.params.ImageUploaderParam
import com.tokopedia.mediauploader.stubUploadFileServices
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class GetImageUploaderUseCaseTest {

    private val services = mockk<ImageUploadServices>()
    private val useCase = GetImageUploaderUseCase(services)
    private var expectedValue = MediaUploader()

    @Test fun `It should be failed to upload image without params`() {
        runBlocking {
            // Given
            val params = ImageUploaderParam()

            // Then
            assertFailsWith<RuntimeException> {
                useCase(params)
            }
        }
    }

    @Test fun `It should success upload image and received uploadId correctly`() {
        runBlocking {
            // Given
            val params = ImageUploaderParam(
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