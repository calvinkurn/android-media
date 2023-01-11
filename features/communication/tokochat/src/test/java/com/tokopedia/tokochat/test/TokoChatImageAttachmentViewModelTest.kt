package com.tokopedia.tokochat.test

import android.widget.ImageView
import com.tokopedia.tokochat.base.TokoChatViewModelTestFixture
import com.tokopedia.tokochat.domain.response.extension.TokoChatImageData
import com.tokopedia.tokochat.domain.response.extension.TokoChatImageError
import com.tokopedia.tokochat.domain.response.extension.TokoChatImageResult
import com.tokopedia.tokochat.util.TokoChatViewUtil
import com.tokopedia.tokochat.utils.observeAwaitValue
import io.mockk.coEvery
import io.mockk.invoke
import io.mockk.mockk
import io.mockk.mockkObject
import kotlinx.coroutines.runBlocking
import okhttp3.ResponseBody
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
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

    @Test
    fun `when getImageWithId with new Image, should save image and call onImageReady without image view`() {
        runBlocking {
            // Given
            val imageIdDummy = "test123"
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
                onDirectLoad = {}
            )
            Thread.sleep(2000)

            // Then
            assertEquals(fileDummy, result)
        }
    }

    @Test
    fun `when getImageWithId with new Image but data is empty, should do nothing`() {
        runBlocking {
            // Given
            val imageIdDummy = "test123"
            val fileDummy = mockk<File>(relaxed = true)
            val imageResultDummy = TokoChatImageResult(success = true, data = null)
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
                onDirectLoad = {}
            )
            Thread.sleep(2000)

            // Then
            assertNotEquals(fileDummy, result)
            assertEquals(null, result)
        }
    }

    @Test
    fun `when getImageWithId with new Image but url is empty, should do nothing`() {
        runBlocking {
            // Given
            val imageIdDummy = "test123"
            val fileDummy = mockk<File>(relaxed = true)
            val imageResultDummy = TokoChatImageResult(
                success = true,
                data = TokoChatImageData(url = null)
            )
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
                onDirectLoad = {}
            )
            Thread.sleep(2000)

            // Then
            assertNotEquals(fileDummy, result)
            assertEquals(null, result)
        }
    }

    @Test
    fun `when getImageWithId with cached image, should directly call onImageReady and not retry`() {
        runBlocking {
            // Given
            val imageIdDummy = "test123"
            val fileDummy = mockk<File>(relaxed = true)

            mockkObject(TokoChatViewUtil.Companion)

            coEvery {
                TokoChatViewUtil.getTokoChatPhotoPath(any())
            } returns fileDummy

            coEvery {
                fileDummy.exists()
            } returns true

            // When
            var result: File? = null
            viewModel.getImageWithId(
                imageId = imageIdDummy,
                channelId = CHANNEL_ID_DUMMY,
                onImageReady = {
                    result = it
                },
                onError = {},
                onDirectLoad = {}
            )

            // Then
            assertEquals(fileDummy, result)
        }
    }

    @Test
    fun `when getImageWithId with cached image but from retry, should re-download the image`() {
        runBlocking {
            // Given
            val imageIdDummy = "test123"
            val fileDummy = mockk<File>(relaxed = true)
            val imageResultDummy = TokoChatImageResult(success = true)
            val responseBodyDummy = mockk<ResponseBody>(relaxed = true)

            coEvery {
                getImageUrlUseCase(any())
            } returns imageResultDummy

            coEvery {
                getImageUrlUseCase.getImage(any())
            } returns responseBodyDummy

            mockkObject(TokoChatViewUtil.Companion)

            coEvery {
                TokoChatViewUtil.getTokoChatPhotoPath(any())
            } returns fileDummy

            coEvery {
                fileDummy.exists()
            } returns true

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
                isFromRetry = true
            )
            Thread.sleep(2000)

            // Then
            assertEquals(fileDummy, result)
        }
    }

    @Test
    fun `when getImageWithId but error, should give throwable on error livedata`() {
        runBlocking {
            // Given
            val imageIdDummy = "test123"

            mockkObject(TokoChatViewUtil.Companion)

            coEvery {
                TokoChatViewUtil.getTokoChatPhotoPath(any())
            } throws throwableDummy

            // When
            viewModel.getImageWithId(
                imageId = imageIdDummy,
                channelId = CHANNEL_ID_DUMMY,
                onImageReady = {},
                onError = {},
                onDirectLoad = {},
                isFromRetry = true
            )
            Thread.sleep(2000)

            // Then
            assertEquals(
                throwableDummy,
                viewModel.error.observeAwaitValue()?.first
            )
        }
    }

    @Test
    fun `when getImageWithId but error when get image result`() {
        runBlocking {
            // Given
            val imageIdDummy = "test123"
            val imageResultDummy = TokoChatImageResult(
                success = false,
                error = listOf(
                    TokoChatImageError(message = "Oops!")
                )
            )
            val responseBodyDummy = mockk<ResponseBody>(relaxed = true)

            coEvery {
                getImageUrlUseCase(any())
            } returns imageResultDummy

            coEvery {
                getImageUrlUseCase.getImage(any())
            } returns responseBodyDummy

            // When
            viewModel.getImageWithId(
                imageId = imageIdDummy,
                channelId = CHANNEL_ID_DUMMY,
                onImageReady = {},
                onError = {},
                onDirectLoad = {},
                isFromRetry = true
            )
            Thread.sleep(2000)

            // Then
            assertEquals(
                imageResultDummy.error?.firstOrNull()?.message,
                viewModel.error.observeAwaitValue()?.first?.message
            )
        }
    }

    @Test
    fun `when getImageWithId but error when get image result but empty error list`() {
        runBlocking {
            // Given
            val imageIdDummy = "test123"
            val imageResultDummy = TokoChatImageResult(success = false, error = listOf())
            val responseBodyDummy = mockk<ResponseBody>(relaxed = true)

            coEvery {
                getImageUrlUseCase(any())
            } returns imageResultDummy

            coEvery {
                getImageUrlUseCase.getImage(any())
            } returns responseBodyDummy

            // When
            viewModel.getImageWithId(
                imageId = imageIdDummy,
                channelId = CHANNEL_ID_DUMMY,
                onImageReady = {},
                onError = {},
                onDirectLoad = {},
                isFromRetry = true
            )
            Thread.sleep(2000)

            // Then
            assertEquals(
                imageResultDummy.error?.firstOrNull()?.message,
                viewModel.error.observeAwaitValue()?.first?.message
            )
        }
    }

    @Test
    fun `when getImageWithId but error when get image result but no error`() {
        runBlocking {
            // Given
            val imageIdDummy = "test123"
            val imageResultDummy = TokoChatImageResult(success = false, error = null)
            val responseBodyDummy = mockk<ResponseBody>(relaxed = true)

            coEvery {
                getImageUrlUseCase(any())
            } returns imageResultDummy

            coEvery {
                getImageUrlUseCase.getImage(any())
            } returns responseBodyDummy

            // When
            viewModel.getImageWithId(
                imageId = imageIdDummy,
                channelId = CHANNEL_ID_DUMMY,
                onImageReady = {},
                onError = {},
                onDirectLoad = {},
                isFromRetry = true
            )
            Thread.sleep(2000)

            // Then
            assertEquals(
                imageResultDummy.error?.firstOrNull()?.message,
                viewModel.error.observeAwaitValue()?.first?.message
            )
        }
    }

    @Test
    fun `when getImageWithId but error when get image result and null error message`() {
        runBlocking {
            // Given
            val imageIdDummy = "test123"
            val imageResultDummy = TokoChatImageResult(
                success = false,
                error = listOf(
                    TokoChatImageError(message = null)
                )
            )
            val responseBodyDummy = mockk<ResponseBody>(relaxed = true)

            coEvery {
                getImageUrlUseCase(any())
            } returns imageResultDummy

            coEvery {
                getImageUrlUseCase.getImage(any())
            } returns responseBodyDummy

            // When
            viewModel.getImageWithId(
                imageId = imageIdDummy,
                channelId = CHANNEL_ID_DUMMY,
                onImageReady = {},
                onError = {},
                onDirectLoad = {},
                isFromRetry = true
            )
            Thread.sleep(2000)

            // Then
            assertEquals(
                imageResultDummy.error?.firstOrNull()?.message,
                viewModel.error.observeAwaitValue()?.first?.message
            )
        }
    }
}
