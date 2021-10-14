package com.tokopedia.mediauploader.domain

import com.tokopedia.mediauploader.image.data.ImageUploadServices
import com.tokopedia.mediauploader.image.data.entity.ImageUploader
import com.tokopedia.mediauploader.image.data.params.ImageUploadParam
import com.tokopedia.mediauploader.image.domain.GetImageUploaderUseCase
import com.tokopedia.mediauploader.stubUploadFileServices
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class GetImageUploaderUseCaseTest {

    private val services = mockk<ImageUploadServices>()
    private val useCase = GetImageUploaderUseCase(services)
    private var expectedValue = ImageUploader()

    @Test fun `It should be failed to upload image without params`() {
        runBlocking {
            // Given
            val params = ImageUploadParam()

            // Then
            assertFailsWith<RuntimeException> {
                useCase(params)
            }
        }
    }

    @Test fun `It should success upload image and received uploadId correctly`() {
        runBlocking {
            // Given
            val params = ImageUploadParam(
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