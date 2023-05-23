package com.tokopedia.media.editor.ui.activity

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.tokopedia.media.editor.data.repository.SaveImageRepository
import com.tokopedia.media.editor.ui.activity.main.EditorViewModel
import com.tokopedia.media.editor.ui.uimodel.EditorDetailUiModel
import com.tokopedia.picker.common.EditorParam
import io.mockk.mockk
import org.junit.Assert.*
import org.junit.Test
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.media.editor.data.repository.AddLogoFilterRepository
import com.tokopedia.media.editor.data.repository.AddTextFilterRepository
import com.tokopedia.media.editor.data.repository.BitmapCreationRepository
import com.tokopedia.media.editor.ui.activity.addtext.AddTextViewModel
import com.tokopedia.media.editor.ui.uimodel.EditorAddLogoUiModel
import com.tokopedia.media.editor.ui.uimodel.EditorAddTextUiModel
import com.tokopedia.media.editor.ui.uimodel.EditorUiModel
import com.tokopedia.media.editor.utils.getImageSize
import com.tokopedia.media.editor.utils.getTokopediaCacheDir
import com.tokopedia.picker.common.PICKER_URL_FILE_CODE
import com.tokopedia.picker.common.types.EditorToolType
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.utils.file.FileUtil
import org.junit.Rule
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockkStatic
import io.mockk.verify
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.shadows.ShadowBitmapFactory

@RunWith(RobolectricTestRunner::class)
class EditorAddTextViewModel {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private val addTextFilterRepository = mockk<AddTextFilterRepository>()

    private val viewModel = AddTextViewModel(
        addTextFilterRepository
    )

    @Test
    fun `should update img url value`() {
        // Given
        val expectedUrl = "https://Test.com"

        // When
        viewModel.setImageUrl(expectedUrl)

        // Then
        assertEquals(expectedUrl, viewModel.imgUrl.value)
    }

    @Test
    fun `should update text value by set input`() {
        // Given
        val expectedText = "Test Text"

        // When
        viewModel.setTextInput(expectedText)

        // Then
        assertEquals(expectedText, viewModel.textInput.value)
    }

    @Test
    fun `should update text value by set ui model data`() {
        // Given
        val expectedText = "Test Text"

        // When
        viewModel.textData = EditorAddTextUiModel(
            textValue = expectedText
        )

        // Then
        assertEquals(expectedText, viewModel.textInput.value)
    }

    @Test
    fun `should update page mode value`() {
        // Given
        val expectedMode = 3

        // When
        viewModel.setPageMode(expectedMode)

        // Then
        assertEquals(expectedMode, viewModel.pageMode.value)
    }

    @Test
    fun `should update text data`() {
        // Given
        val textSample = "Text Sample"
        val textData = EditorAddTextUiModel(
            textValue = textSample
        )

        // When
        viewModel.textData = textData

        // Then
        assertEquals(textData.textValue, viewModel.textData.textValue)
    }

    @Test
    fun `generate add text overlay without base image url`() {
        // Given
        val expectedOverlay: Bitmap = ShadowBitmapFactory.create("bitmapName", BitmapFactory.Options())
        var overlayText: Bitmap? = null

        // When
        every { addTextFilterRepository.generateTextOverlay(any(), any()) } returns expectedOverlay
        overlayText = viewModel.getAddTextFilterOverlay()

        // Then
        assertEquals(expectedOverlay, overlayText)
    }

    @Test
    fun `generate add text overlay with base image url`() {
        // Given
        val expectedOverlay: Bitmap = ShadowBitmapFactory.create("bitmapName", BitmapFactory.Options())
        var overlayText: Bitmap? = null

        // When
        every { addTextFilterRepository.generateTextOverlay(any(), any()) } returns expectedOverlay
        viewModel.setImageUrl("url")
        overlayText = viewModel.getAddTextFilterOverlay()

        // Then
        assertEquals(expectedOverlay, overlayText)
    }
}
