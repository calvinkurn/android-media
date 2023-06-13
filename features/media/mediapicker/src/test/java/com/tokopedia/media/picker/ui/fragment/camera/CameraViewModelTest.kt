package com.tokopedia.media.picker.ui.fragment.camera

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.otaliastudios.cameraview.size.Size
import com.tokopedia.media.picker.data.repository.CreateMediaRepository
import com.tokopedia.media.util.test
import com.tokopedia.unit.test.rule.CoroutineTestRule
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestCoroutineScope
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.io.File

@ExperimentalCoroutinesApi
class CameraViewModelTest {
    @get:Rule val instantTaskExecutorRule = InstantTaskExecutorRule()
    @get:Rule val coroutineScopeRule = CoroutineTestRule()

    private val createMediaRepository = mockk<CreateMediaRepository>()

    private lateinit var viewModel: CameraViewModel

    @Before
    fun setup() {
        viewModel = CameraViewModel(
            createMediaRepository,
            coroutineScopeRule.dispatchers
        )
    }

    @Test
    fun `it should be able to taken a picture`() = runBlocking {
        viewModel.pictureTaken.test {
            // Given
            val expectedValue = `provide mock file`()
            coEvery { createMediaRepository.image(any(), any()) } returns flow { emit(expectedValue) }

            // When
            viewModel.onPictureTaken(
                Size(0, 0),
                ByteArray(0)
            )

            // Then
            assertEquals(awaitItem(), expectedValue)
        }
    }

    @Test
    fun `it should be cannot taken a picture`() = runBlocking {
        viewModel.pictureTaken.test {
            // Given
            coEvery { createMediaRepository.image(any(), any()) } returns flow { emit(null) }

            // When
            viewModel.onPictureTaken(
                Size(0, 0),
                ByteArray(0)
            )

            // Then
            assert(awaitItem() == null)
        }
    }

    @Test
    fun `it should be able to taken a video`() = runBlocking {
        viewModel.videoTaken.test {
            // Given
            val expectedValue = `provide mock file`()
            coEvery { createMediaRepository.video() } returns expectedValue

            // When
            viewModel.onVideoTaken()

            // Then
            assertEquals(awaitItem(), expectedValue)
        }
    }

    @Test
    fun `it should be cannot taken a video`() = runBlocking {
        viewModel.videoTaken.test {
            // Given
            coEvery { createMediaRepository.video() } returns null

            // When
            viewModel.onVideoTaken()

            // Then
            assert(awaitItem() == null)
        }
    }

    @Test
    fun `it should be state of loading is invoked`() {
        // Given
        val expectedValue = `provide mock file`()
        coEvery { createMediaRepository.image(any(), any()) } returns flow { emit(expectedValue) }

        // When
        viewModel.onPictureTaken(
            Size(0, 0),
            ByteArray(0)
        )

        // Then
        assert(viewModel.isLoading.value != null)
    }


    private fun `provide mock file`(): File {
        val file = mockk<File>(relaxed = true)
        every { file.exists() } returns true
        every { file.path } returns "image.jpg"

        return file
    }

}
