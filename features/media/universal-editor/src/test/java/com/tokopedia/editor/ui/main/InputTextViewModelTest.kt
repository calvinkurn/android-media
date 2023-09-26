package com.tokopedia.editor.ui.main

import android.graphics.Color
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.editor.ui.model.InputTextModel
import com.tokopedia.editor.ui.text.InputTextViewModel
import com.tokopedia.editor.util.FontAlignment
import com.tokopedia.editor.util.FontDetail
import com.tokopedia.editor.util.provider.ColorProvider
import com.tokopedia.unit.test.ext.getOrAwaitValue
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class InputTextViewModelTest {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private val colorProvider = mockk<ColorProvider>()

    private lateinit var viewModel: InputTextViewModel

    @Before
    fun setUp() {
        viewModel = InputTextViewModel(
            colorProvider
        )
    }

    @Test
    fun `should update selected color state`() {
        // Given
        val newSelectedColor = Color.parseColor("#FF7F17")

        // When
        viewModel.updateSelectedColor(newSelectedColor)

        // Then
        assertEquals(newSelectedColor, viewModel.selectedTextColor.getOrAwaitValue())
        assertEquals(newSelectedColor, viewModel.getCurrentSelectedColor())
    }

    @Test
    fun `should update text alignment to left`() {
        // When
        viewModel.increaseAlignment()

        // Then
        assertEquals(FontAlignment.LEFT, viewModel.selectedAlignment.getOrAwaitValue())
    }

    @Test
    fun `should update text alignment to right`() {
        // Given
        val rightAlignment = FontAlignment.RIGHT

        // When
        viewModel.updateAlignment(rightAlignment)

        // Then
        assertEquals(rightAlignment, viewModel.selectedAlignment.getOrAwaitValue())
    }

    @Test
    fun `should set background color`() {
        // Given
        val backgroundColor = Pair(Color.BLACK, Color.WHITE)

        // When
        viewModel.updateBackgroundState(backgroundColor)

        // Then
        assertEquals(backgroundColor, viewModel.backgroundColorSet.getOrAwaitValue())
    }

    @Test
    fun `should set font italic`() {
        // Given
        val italicStyle = FontDetail.OPEN_SAUCE_ONE_ITALIC

        // When
        viewModel.updateFontStyle(italicStyle)

        // Then
        assertEquals(italicStyle, viewModel.selectedFontStyle.getOrAwaitValue())
    }

    @Test
    fun `should update text content`() {
        // Given
        val targetText = "New Text Content"

        // When
        viewModel.updateText(targetText)

        // Then
        assertEquals(targetText, viewModel.textValue.getOrAwaitValue())
    }

    @Test
    fun `should return all text detail`() {
        // Given
        var inputDetail: InputTextModel? = null

        // When
        inputDetail = viewModel.getTextDetail()

        // Then
        assertNotNull(inputDetail)
    }
}
