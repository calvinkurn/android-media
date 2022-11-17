package com.tokopedia.tokochat.test

import android.widget.ImageView
import com.tokopedia.tokochat.base.TokoChatViewModelTestFixture
import com.tokopedia.tokochat.domain.response.extension.TokoChatImageResult
import io.mockk.coEvery
import io.mockk.invoke
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import okhttp3.ResponseBody
import org.junit.Assert.assertEquals
import org.junit.Test
import java.io.File

class TokoChatImageAttachmentViewModelTest : TokoChatViewModelTestFixture() {

    @Test
    fun `when getImageWithId with new Image, should save image and call onImageReady`() {
        runBlocking {
            // Given
            val imageIdDummy = "test123"
            val imageViewDummy = mockk<ImageView>(relaxed = true)
            val fileDummy = mockk<File>(relaxed = true)
            val imageResultDummy = TokoChatImageResult(success = true)
            val responseBodyDummy = mockk<ResponseBody>(relaxed = true)

            coEvery {
                getImageUrlUseCase(any())
            } returns imageResultDummy

            coEvery {
                getImageUrlUseCase.getImage(any())
            } returns responseBodyDummy

            coEvery {
                viewUtil.downloadAndSaveByteArrayImage(any(), any(), captureLambda(), any(), any(), any())
            } answers {
                val onSuccessDummy = lambda<(File?) -> Unit>()
                onSuccessDummy.invoke(fileDummy)
            }

            // When
            var result: File? = null
            viewModel.getImageWithId(
                imageId = imageIdDummy,
                channelId = CHANNEL_ID_DUMMY,
                onImageReady = {
                    result = it
                },
                onError = {},
                onDirectLoad = {},
                imageView = imageViewDummy,
                isFromRetry = false
            )
            Thread.sleep(2000)

            // Then
            assertEquals(fileDummy, result)
        }
    }
}
