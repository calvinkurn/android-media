package com.tokopedia.topchat.chatroom.viewmodel

import com.tokopedia.chat_common.data.ImageUploadUiModel
import com.tokopedia.topchat.R
import com.tokopedia.topchat.chatroom.viewmodel.base.BaseTopChatViewModelTest
import com.tokopedia.topchat.common.util.ImageUtil
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockkObject
import io.mockk.verify
import org.junit.Assert
import org.junit.Test

class CompressImageViewModelTest: BaseTopChatViewModelTest() {

    private val testImageUrl = "testImageUrl"
    private val testCompressedImageUrl = "testCompressedImageUrl"

    override fun before() {
        super.before()
        mockkObject(ImageUtil)
    }

    @Test
    fun should_upload_image_when_success_compress_image() {
        //Given
        val imageUploadUiModel = ImageUploadUiModel.Builder()
            .withImageUrl(testImageUrl)
            .build()

        every {
            ImageUtil.validateImageAttachment(any())
        } returns Pair(true, ImageUtil.IMAGE_VALID)

        coEvery {
            compressImageUseCase.compressImage(any())
        } returns testCompressedImageUrl

        //When
        viewModel.startCompressImages(imageUploadUiModel)

        //Then
        verify (exactly = 1) {
            viewModel.startUploadImages(imageUploadUiModel)
        }
    }

    @Test
    fun should_not_upload_image_when_url_null() {
        //Given
        val imageUploadUiModel = ImageUploadUiModel.Builder().build()

        every {
            ImageUtil.validateImageAttachment(any())
        } returns Pair(true, ImageUtil.IMAGE_VALID)

        coEvery {
            compressImageUseCase.compressImage(any())
        } returns testCompressedImageUrl

        //When
        viewModel.startCompressImages(imageUploadUiModel)

        //Then
        verify (exactly = 0) {
            viewModel.startUploadImages(imageUploadUiModel)
        }
    }

    @Test
    fun should_get_error_message_when_image_undersized() {
        //Given
        val imageUploadUiModel = ImageUploadUiModel.Builder().build()

        every {
            ImageUtil.validateImageAttachment(any())
        } returns Pair(false, ImageUtil.IMAGE_UNDERSIZE)

        coEvery {
            compressImageUseCase.compressImage(any())
        } returns testCompressedImageUrl

        //When
        viewModel.startCompressImages(imageUploadUiModel)

        //Then
        Assert.assertEquals(
            R.string.undersize_image,
            viewModel.errorSnackbarStringRes.value
        )
    }

    @Test
    fun should_get_error_message_when_image_exceed_size_limit() {
        //Given
        val imageUploadUiModel = ImageUploadUiModel.Builder().build()

        every {
            ImageUtil.validateImageAttachment(any())
        } returns Pair(false, ImageUtil.IMAGE_EXCEED_SIZE_LIMIT)

        coEvery {
            compressImageUseCase.compressImage(any())
        } returns testCompressedImageUrl

        //When
        viewModel.startCompressImages(imageUploadUiModel)

        //Then
        Assert.assertEquals(
            R.string.oversize_image,
            viewModel.errorSnackbarStringRes.value
        )
    }

    @Test
    fun should_get_nothing_when_image_error_no_uri() {
        //Given
        val imageUploadUiModel = ImageUploadUiModel.Builder().build()

        every {
            ImageUtil.validateImageAttachment(any())
        } returns Pair(false, ImageUtil.IMAGE_NO_URI)

        coEvery {
            compressImageUseCase.compressImage(any())
        } returns testCompressedImageUrl

        //When
        viewModel.startCompressImages(imageUploadUiModel)

        //Then
        Assert.assertEquals(
            null,
            viewModel.errorSnackbarStringRes.value
        )
    }

    @Test
    fun should_get_error_message_when_fail_to_compress_image() {
        //Given
        val imageUploadUiModel = ImageUploadUiModel.Builder()
            .withImageUrl(testImageUrl)
            .build()

        every {
            ImageUtil.validateImageAttachment(any())
        } returns Pair(true, ImageUtil.IMAGE_VALID)

        coEvery {
            compressImageUseCase.compressImage(any())
        } throws expectedThrowable

        //When
        viewModel.startCompressImages(imageUploadUiModel)

        //Then
        Assert.assertEquals(
            R.string.error_compress_image,
            viewModel.errorSnackbarStringRes.value
        )
    }
}