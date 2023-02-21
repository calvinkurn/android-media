package com.tokopedia.topchat.chatroom.viewmodel

import com.tokopedia.chat_common.data.ImageUploadUiModel
import com.tokopedia.device.info.DeviceInfo
import com.tokopedia.topchat.chatroom.service.UploadImageChatService
import com.tokopedia.topchat.chatroom.view.viewmodel.TopChatViewModel
import com.tokopedia.topchat.chatroom.viewmodel.base.BaseTopChatViewModelTest
import com.tokopedia.topchat.common.mapper.ImageUploadMapper
import io.mockk.every
import io.mockk.invoke
import io.mockk.mockkStatic
import io.mockk.verify
import org.junit.Assert.assertEquals
import org.junit.Test
import java.lang.IllegalStateException

class UploadImageViewModelTest : BaseTopChatViewModelTest() {

    val imageUpload = ImageUploadUiModel.Builder().build()

    private fun disableUploadByService() {
        every {
            remoteConfig.getBoolean(TopChatViewModel.ENABLE_UPLOAD_IMAGE_SERVICE)
        } returns false
    }

    private fun enableUploadByService() {
        every {
            remoteConfig.getBoolean(TopChatViewModel.ENABLE_UPLOAD_IMAGE_SERVICE, any())
        } returns true
        mockkStatic(DeviceInfo::getModelName)
    }

    private fun disableUploadByServiceByError() {
        every {
            remoteConfig.getBoolean(TopChatViewModel.ENABLE_UPLOAD_IMAGE_SERVICE)
        } throws expectedThrowable
    }

    @Test
    fun should_upload_image_through_ws_when_success_uploadpedia() {
        // Given
        val wsPayload = "image"
        disableUploadByService()
        every { uploadImageUseCase.upload(imageUpload, captureLambda(), any(), any()) } answers {
            val onSuccess = lambda<(String, ImageUploadUiModel, Boolean) -> Unit>()
            onSuccess.invoke("123", imageUpload, true)
        }
        every { payloadGenerator.generateImageWsPayload(any(), any(), any(), any()) } returns wsPayload

        // When
        viewModel.startUploadImages(imageUpload, true)

        // Then
        assertEquals(viewModel.previewMsg.value, imageUpload)
        verify {
            chatWebSocket.sendPayload(wsPayload)
        }
    }

    @Test
    fun should_update_snackbar_value_if_error_uploadpedia_through_ws() {
        // Given
        val error = IllegalStateException()
        disableUploadByServiceByError()
        every { uploadImageUseCase.upload(imageUpload, any(), captureLambda(), any()) } answers {
            val onError = lambda<(Throwable, ImageUploadUiModel) -> Unit>()
            onError.invoke(error, imageUpload)
        }

        // When
        viewModel.startUploadImages(imageUpload, true)

        // Then
        assertEquals(viewModel.errorSnackbar.value, error)
        assertEquals(viewModel.failUploadImage.value, imageUpload)
    }

    @Test
    fun should_upload_image_through_service() {
        // Given
        enableUploadByService()
        every { DeviceInfo.getModelName() } returns ""

        // When
        viewModel.startUploadImages(imageUpload, true)
        viewModel.startUploadImages(imageUpload, true)

        // Then
        assertEquals(UploadImageChatService.dummyMap.size, 1)
        assertEquals(viewModel.previewMsg.value, imageUpload)
        assertEquals(
            viewModel.uploadImageService.value,
            ImageUploadMapper.mapToImageUploadServer(imageUpload)
        )
    }

    @Test
    fun should_return_true_when_uploading() {
        // Given
        every { uploadImageUseCase.isUploading } returns true

        // When
        val isUploading = viewModel.isUploading()

        // Then
        assertEquals(isUploading, true)
    }
}
