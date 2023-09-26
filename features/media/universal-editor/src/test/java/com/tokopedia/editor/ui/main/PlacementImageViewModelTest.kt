package com.tokopedia.editor.ui.main

import android.graphics.BitmapFactory
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.editor.data.repository.ImageSaveRepository
import com.tokopedia.editor.ui.model.ImagePlacementModel
import com.tokopedia.editor.ui.placement.PlacementImageViewModel
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.unit.test.ext.getOrAwaitValue
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.shadows.ShadowBitmapFactory

@RunWith(RobolectricTestRunner::class)
class PlacementImageViewModelTest {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private val imageSaveRepository = mockk<ImageSaveRepository>()
    private val dispatchers = CoroutineTestDispatchersProvider

    private lateinit var viewModel: PlacementImageViewModel

    @Before
    fun setUp() {
        viewModel = PlacementImageViewModel(
            imageSaveRepository,
            dispatchers
        )
    }

    @Test
    fun `should update loading state`() {
        // Given
        val expectedState = true

        // When
        viewModel.updateLoadingState(expectedState)

        // Then
        assertEquals(expectedState, viewModel.isLoadingShow.getOrAwaitValue())
    }

    @Test
    fun `should update placement model`() {
        // Given
        val placementModel = ImagePlacementModel(
            "dummy_path/file_name.png",
            scale = 1f,
            angle = 0f,
            translateX = 100f,
            translateY = 100f
        )

        // When
        viewModel.setPlacementModel(placementModel)

        // Then
        viewModel.placementModel.getOrAwaitValue()?.let {
            assertEquals(placementModel.path, it.path)
            assertEquals(placementModel.translateY, it.translateY)
        }
    }

    @Test
    fun `should set image path url`() {
        // Given
        val expectedImageUrl = "dummy/image/image_dummy.png"

        // When
        viewModel.setImagePath(expectedImageUrl)

        // Then
        assertEquals(expectedImageUrl, viewModel.imagePath.getOrAwaitValue())
    }

    @Test
    fun `should show exit confirmation on back`() {
        // Given
        val initialMatrixValue = FloatArray(3) { 1f }
        val currentMatrixValue = FloatArray(3) { 2f }

        // When
        viewModel.initialImageMatrix = initialMatrixValue

        // Then
        viewModel.isShowExitConfirmation(currentMatrixValue).let {
            assertEquals(true, it)
        }
    }

    @Test
    fun `shouldn't show exit confirmation on back`() {
        // Given
        val initialMatrixValue = FloatArray(3) { 1f }
        val currentMatrixValue = FloatArray(3) { 1f }

        // When
        viewModel.initialImageMatrix = initialMatrixValue

        // Then
        viewModel.isShowExitConfirmation(currentMatrixValue).let {
            assertEquals(false, it)
        }
    }

    @Test
    fun `should update placement model result on page save`() {
        // Given
        val bitmap = ShadowBitmapFactory.create("", BitmapFactory.Options())
        val resultPath = "dummy_path/result_file.png"
        val placementModel = ImagePlacementModel(
            "dummy_path/file_name.png",
            scale = 1f,
            angle = 0f,
            translateX = 100f,
            translateY = 100f
        )

        // When
        every { imageSaveRepository.saveBitmap(any(), any()) } returns resultPath
        viewModel.savePlacementBitmap(
            resultPath,
            bitmap,
            placementModel.translateX,
            placementModel.translateY,
            placementModel.scale,
            placementModel.angle
        )

        // Then
        viewModel.placementModelResult.getOrAwaitValue().let {
            assertEquals(resultPath, it.path)
            assertEquals(placementModel.translateY, it.translateY)
        }
    }
}
