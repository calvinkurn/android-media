package com.tokopedia.tokochat.test.chatroom

import android.net.Uri
import androidx.lifecycle.LifecycleOwner
import com.tokopedia.picker.common.utils.ImageCompressor
import com.tokopedia.tokochat.base.TokoChatViewModelTestFixture
import com.tokopedia.tokochat.domain.response.upload_image.TokoChatUploadImageResponse
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.mockkStatic
import io.mockk.verify
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.runBlocking
import org.junit.Test

class TokoChatUploadImageViewModelTest : TokoChatViewModelTestFixture() {

    override fun setup() {
        super.setup()
        viewModel.imageAttachmentMap.clear()
        mockkObject(ImageCompressor)
    }

    @Test
    fun should_not_error_when_assign_image_attachment_map() {
        runBlocking {
            // Given
            val imageAttachmentMapDummy = mutableMapOf(Pair("test", "test"))

            // When
            viewModel.imageAttachmentMap = imageAttachmentMapDummy

            // Then
            assertEquals(imageAttachmentMapDummy, viewModel.imageAttachmentMap)
        }
    }

    @Test
    fun should_not_error_when_on_create() {
        runBlocking {
            // Given
            val dummyLifeCycleOwner = mockk<LifecycleOwner>(relaxed = true)

            // When
            viewModel.onCreate(dummyLifeCycleOwner)

            // Then
            verify(exactly = 1) {
                getChannelUseCase.registerExtensionProvider(any())
            }
        }
    }

    @Test
    fun should_not_error_when_on_destroy() {
        runBlocking {
            // Given
            val dummyLifeCycleOwner = mockk<LifecycleOwner>(relaxed = true)

            // When
            viewModel.onDestroy(dummyLifeCycleOwner)

            // Then
            verify(exactly = 1) {
                getChannelUseCase.unRegisterExtensionProvider(any())
            }
        }
    }

    @Test
    fun should_save_cache_when_image_attachment_map_is_not_empty() {
        runBlocking {
            // Given
            val dummyLifeCycleOwner = mockk<LifecycleOwner>(relaxed = true)
            viewModel.imageAttachmentMap["testKey"] = "testValue"

            // When
            viewModel.onPause(dummyLifeCycleOwner)

            // Then
            verify(exactly = 1) {
                cacheManager.saveCache(any(), any())
            }
        }
    }

    @Test
    fun should_not_save_cache_when_image_attachment_map_is_empty() {
        runBlocking {
            // Given
            val dummyLifeCycleOwner = mockk<LifecycleOwner>(relaxed = true)
            viewModel.imageAttachmentMap.clear()

            // When
            viewModel.onPause(dummyLifeCycleOwner)

            // Then
            verify(exactly = 0) {
                cacheManager.saveCache(any(), any())
            }
        }
    }

    @Test
    fun should_not_error_when_on_resume_and_empty_cache() {
        runBlocking {
            // Given
            val dummyLifeCycleOwner = mockk<LifecycleOwner>(relaxed = true)

            // When
            viewModel.onResume(dummyLifeCycleOwner)

            // Then
            verify(exactly = 1) {
                cacheManager.loadCache(any(), Map::class.java)
            }
        }
    }

    @Test
    fun should_not_error_when_on_resume_and_not_empty_cache() {
        runBlocking {
            // Given
            val dummyLifeCycleOwner = mockk<LifecycleOwner>(relaxed = true)
            val dummyImageAttachmentMap = mutableMapOf<String, String>().apply {
                this["testKey"] = "testValue"
            }
            every {
                cacheManager.loadCache(any(), Map::class.java)
            } returns dummyImageAttachmentMap

            // When
            viewModel.onResume(dummyLifeCycleOwner)

            // Then
            verify(exactly = 1) {
                cacheManager.loadCache(any(), Map::class.java)
            }
        }
    }

    @Test
    fun should_give_nothing_to_live_data_when_success_upload_image() {
        runBlocking {
            // Given
            val dummyImagePath = "testImagePath"
            val uriMock = mockk<Uri>()
            val dummyImageUploadResult = TokoChatUploadImageResponse().apply {
                this.data?.imageId = "testImageId"
            }
            mockImageUploadUseCases(dummyImagePath, uriMock, dummyImageUploadResult)

            // When
            viewModel.uploadImage(dummyImagePath) {}

            // Then
            assertEquals(null, viewModel.imageUploadError.value)
        }
    }

    @Test
    fun should_give_error_to_live_data_when_success_upload_image_but_data_is_null() {
        runBlocking {
            // Given
            val dummyImagePath = "testImagePath"
            val uriMock = mockk<Uri>()
            val dummyImageUploadResult = TokoChatUploadImageResponse().apply {
                this.data = null
            }
            mockImageUploadUseCases(dummyImagePath, uriMock, dummyImageUploadResult)

            // When
            viewModel.uploadImage(dummyImagePath) {}

            // Then
            assertEquals(
                "Error payload not expected",
                viewModel.imageUploadError.value?.second?.message
            )
        }
    }

    @Test
    fun should_give_error_to_live_data_when_success_upload_image_but_image_id_is_null() {
        runBlocking {
            // Given
            val dummyImagePath = "testImagePath"
            val uriMock = mockk<Uri>()
            val dummyImageUploadResult = TokoChatUploadImageResponse().apply {
                this.data?.imageId = null
            }
            mockImageUploadUseCases(dummyImagePath, uriMock, dummyImageUploadResult)

            // When
            viewModel.uploadImage(dummyImagePath) {}

            // Then
            assertEquals(
                "java.lang.NullPointerException",
                viewModel.imageUploadError.value?.second?.toString()
            )
        }
    }

    @Test
    fun should_give_error_to_live_data_when_fail_preprocessing_compress() {
        runBlocking {
            // Given
            val dummyImagePath = "testImagePath"
            val uriMock = mockk<Uri>()
            val dummyImageUploadResult = TokoChatUploadImageResponse().apply {
                this.data?.imageId = "testImageId"
            }
            mockImageUploadUseCases(dummyImagePath, uriMock, dummyImageUploadResult)
            every {
                viewUtil.compressImageToTokoChatPath(any())
            } returns null

            // When
            viewModel.uploadImage(dummyImagePath) {}

            // Then
            assertEquals(
                "Compressed image null",
                viewModel.imageUploadError.value?.second?.message
            )
        }
    }

    @Test
    fun should_give_error_to_live_data_when_fail_preprocessing_rename() {
        runBlocking {
            // Given
            val dummyImagePath = "testImagePath"
            val uriMock = mockk<Uri>()
            val dummyImageUploadResult = TokoChatUploadImageResponse().apply {
                this.data?.imageId = "testImageId"
            }
            mockImageUploadUseCases(dummyImagePath, uriMock, dummyImageUploadResult)
            every {
                viewUtil.renameAndMoveFileToTokoChatDir(any(), any())
            } returns null

            // When
            viewModel.uploadImage(dummyImagePath) {}

            // Then
            assertEquals(
                "Renamed image null",
                viewModel.imageUploadError.value?.second?.message
            )
        }
    }

    @Test
    fun should_give_error_to_live_data_when_fail_upload_image() {
        runBlocking {
            // Given
            val dummyImagePath = "testImagePath"
            val uriMock = mockk<Uri>()
            val dummyErrorMessage = "testError"
            val dummyImageUploadResult = TokoChatUploadImageResponse().apply {
                this.error = listOf(dummyErrorMessage)
            }
            mockImageUploadUseCases(dummyImagePath, uriMock, dummyImageUploadResult)

            // When
            viewModel.uploadImage(dummyImagePath) {}

            // Then
            assertEquals(dummyErrorMessage, viewModel.imageUploadError.value?.second?.message)
        }
    }

    @Test
    fun should_give_nothing_to_live_data_when_success_upload_image_and_error_message_null() {
        runBlocking {
            // Given
            val dummyImagePath = "testImagePath"
            val uriMock = mockk<Uri>()
            val dummyImageUploadResult = TokoChatUploadImageResponse().apply {
                this.error = null
            }
            mockImageUploadUseCases(dummyImagePath, uriMock, dummyImageUploadResult)

            // When
            viewModel.uploadImage(dummyImagePath) {}

            // Then
            assertEquals(null, viewModel.imageUploadError.value)
        }
    }

    @Test
    fun should_give_nothing_to_live_data_when_success_resend_image() {
        runBlocking {
            // Given
            val dummyImagePath = "testImagePath"
            val dummyImageId = "testImageId"
            val uriMock = mockk<Uri>()
            val dummyImageUploadResult = TokoChatUploadImageResponse().apply {
                this.data?.imageId = dummyImageId
            }
            viewModel.imageAttachmentMap[dummyImageId] = dummyImageId
            mockImageUploadUseCases(dummyImagePath, uriMock, dummyImageUploadResult)

            // When
            viewModel.resendImage(dummyImageId)

            // Then
            assertEquals(null, viewModel.imageUploadError.value)
        }
    }

    @Test
    fun should_give_error_to_live_data_when_fail_resend_image_because_image_attachment_empty() {
        runBlocking {
            // Given
            val dummyImagePath = "testImagePath"
            val dummyImageId = "testImageId"
            val uriMock = mockk<Uri>()
            val dummyImageUploadResult = TokoChatUploadImageResponse().apply {
                this.data?.imageId = dummyImageId
            }
            viewModel.imageAttachmentMap.clear()
            mockImageUploadUseCases(dummyImagePath, uriMock, dummyImageUploadResult)

            // When
            viewModel.resendImage(dummyImageId)

            // Then
            assertEquals(
                "java.lang.NullPointerException",
                viewModel.imageUploadError.value?.second?.toString()
            )
        }
    }

    @Test
    fun should_give_error_to_live_data_when_fail_resend_image_because_other_reason() {
        runBlocking {
            // Given
            val dummyImagePath = "testImagePath"
            val dummyImageId = "testImageId"
            val uriMock = mockk<Uri>()
            val dummyImageUploadResult = TokoChatUploadImageResponse().apply {
                this.data?.imageId = dummyImageId
            }
            viewModel.imageAttachmentMap[dummyImageId] = dummyImageId
            mockImageUploadUseCases(dummyImagePath, uriMock, dummyImageUploadResult)
            coEvery {
                uploadImageUseCase(any())
            } throws throwableDummy

            // When
            viewModel.resendImage(dummyImageId)

            // Then
            assertEquals(throwableDummy, viewModel.imageUploadError.value?.second)
        }
    }

    private fun mockImageUploadUseCases(
        dummyImagePath: String,
        uriMock: Uri,
        dummyImageUploadResult: TokoChatUploadImageResponse
    ) {
        mockkStatic(Uri::class)
        every {
            Uri.parse(any())
        } returns uriMock

        every {
            viewUtil.compressImageToTokoChatPath(any())
        } returns uriMock

        every {
            viewUtil.renameAndMoveFileToTokoChatDir(any(), any())
        } returns dummyImagePath

        every {
            sendMessageUseCase.addTransientMessage(any(), any())
        } returns Unit

        every {
            sendMessageUseCase.sendTransientMessage(any(), any())
        } returns Unit

        coEvery {
            uploadImageUseCase(any())
        } returns dummyImageUploadResult
    }
}
