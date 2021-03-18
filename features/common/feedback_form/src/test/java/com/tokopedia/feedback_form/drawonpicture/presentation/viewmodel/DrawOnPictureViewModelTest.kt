package com.tokopedia.feedback_form.drawonpicture.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * @author by furqan on 01/10/2020
 */
class DrawOnPictureViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private val testDispatcherProvider = CoroutineTestDispatchersProvider

    private lateinit var viewModel: DrawOnPictureViewModel

    @Before
    fun setUp() {
        viewModel = DrawOnPictureViewModel(testDispatcherProvider)
    }

    @Test
    fun startDrawing_shouldHidePencilOptions() {
        // given

        // when
        viewModel.startDrawing()

        // then
        Thread.sleep(1500)
        assert(viewModel.showPencilOptions.value == false)
    }

    @Test
    fun stopDrawing_shouldShowPencilOptions() {
        // given

        // when
        viewModel.stopDrawing()

        // then
        assert(viewModel.showPencilOptions.value == true)
    }

    @Test
    fun startDrawingThenStop_shouldShowPencilOptions() {
        // given

        // when
        viewModel.startDrawing()
        viewModel.stopDrawing()

        // then
        Thread.sleep(1500)
        assert(viewModel.showPencilOptions.value == true)
    }
}