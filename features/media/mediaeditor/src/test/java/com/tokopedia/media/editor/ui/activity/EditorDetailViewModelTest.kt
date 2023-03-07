package com.tokopedia.media.editor.ui.activity

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.media.editor.data.repository.ColorFilterRepository
import com.tokopedia.media.editor.data.repository.ContrastFilterRepository
import com.tokopedia.media.editor.data.repository.RotateFilterRepository
import com.tokopedia.media.editor.data.repository.SaveImageRepository
import com.tokopedia.media.editor.data.repository.WatermarkFilterRepository
import com.tokopedia.media.editor.data.repository.WatermarkType
import com.tokopedia.media.editor.domain.GetWatermarkUseCase
import com.tokopedia.media.editor.domain.SetRemoveBackgroundUseCase
import com.tokopedia.media.editor.ui.activity.detail.DetailEditorViewModel
import com.tokopedia.media.editor.ui.uimodel.EditorDetailUiModel
import com.tokopedia.media.editor.ui.uimodel.EditorUiModel
import com.tokopedia.media.editor.ui.widget.EditorDetailPreviewWidget
import com.tokopedia.media.editor.utils.ResourceProvider
import com.tokopedia.picker.common.EditorParam
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.shadows.ShadowBitmapFactory
import org.robolectric.shadows.ShadowDrawable
import java.io.File
import java.lang.Exception
import com.tokopedia.media.editor.R as editorR

@ExperimentalCoroutinesApi
@RunWith(RobolectricTestRunner::class)
class EditorDetailViewModelTest {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private val resourceProvider = mockk<ResourceProvider>()
    private val userSession = mockk<UserSessionInterface>()
    private val colorFilterRepository = mockk<ColorFilterRepository>()
    private val removeBackgroundUseCase = mockk<SetRemoveBackgroundUseCase>()
    private val contrastFilterRepository = mockk<ContrastFilterRepository>()
    private val watermarkFilterRepository = mockk<WatermarkFilterRepository>()
    private val rotateFilterRepository = mockk<RotateFilterRepository>()
    private val saveImageRepository = mockk<SaveImageRepository>()
    private val getWatermarkUseCase = mockk<GetWatermarkUseCase>()

    private val viewModel = DetailEditorViewModel(
        resourceProvider,
        userSession,
        colorFilterRepository,
        removeBackgroundUseCase,
        contrastFilterRepository,
        watermarkFilterRepository,
        rotateFilterRepository,
        saveImageRepository,
        getWatermarkUseCase
    )

    @Test
    fun `initialize editor ui model should get ui model`() {
        // When
        viewModel.setIntentUiModel(
            EditorUiModel(
                originalUrl = videoKey
            )
        )

        // Then
        assertEquals(videoKey, viewModel.intentUiModel.value?.getOriginalUrl())
    }

    @Test
    fun `initialize editor detail ui model should get detail ui model`() {
        // When
        viewModel.setIntentDetailUiModel(
            EditorDetailUiModel(
                originalUrl = videoKey
            )
        )

        // Then
        assertEquals(videoKey, viewModel.intentDetailUiModel.value?.originalUrl)
    }

    @Test
    fun `set editor brightness should get brightness matrix color`() {
        // Given
        val colorMatrix = ColorMatrixColorFilter(
            ColorMatrix()
        )

        // When
        every { colorFilterRepository.brightness(any()) } returns colorMatrix
        viewModel.setBrightness(29f)

        // Then
        verify { colorFilterRepository.brightness(any()) }
        assertEquals(colorMatrix, viewModel.brightnessFilter.value)
    }

    @Test
    fun `set editor brightness null should break`() {
        // When
        viewModel.setBrightness(null)

        // Then
        verify(exactly = 0) { colorFilterRepository.brightness(any()) }
    }

    @Test
    fun `set editor contrast should get contrast bitmap`() {
        // Given
        val shopName = "Contrast"
        val bitmap = ShadowBitmapFactory.create(shopName, BitmapFactory.Options())

        // When
        every { contrastFilterRepository.contrast(any(), any()) } returns bitmap
        viewModel.setContrast(null, null)
        viewModel.setContrast(null, bitmap)
        viewModel.setContrast(1f, null)
        viewModel.setContrast(1f, bitmap)

        // Then
        verify { contrastFilterRepository.contrast(any(), any()) }
        assertEquals(bitmap, viewModel.contrastFilter.value)
    }

    @Test
    fun `set editor watermark should get watermark bitmap`() {
        // Given
        val shopName = "Watermark"
        val bitmap = ShadowBitmapFactory.create(shopName, BitmapFactory.Options())
        val drawable = ShadowDrawable.createFromResourceId(editorR.drawable.watermark_tokopedia)

        // When
        every { watermarkFilterRepository.isAssetInitialize() } returns false
        every { watermarkFilterRepository.setAsset(any(), any(), any()) } just Runs
        every { userSession.shopName } returns shopName
        every { resourceProvider.getWatermarkLogoDrawable() } returns drawable
        every { resourceProvider.getWatermarkTextColor() } returns Pair(null, null)
        every {
            hint(Bitmap::class)
            getWatermarkUseCase(any())
        } returns bitmap
        viewModel.setWatermark(
            bitmap,
            WatermarkType.Diagonal,
            detailUiModel = EditorDetailUiModel(),
            useStorageColor = false
        )

        // Then
        verify { getWatermarkUseCase(any()) }
        assertEquals(bitmap, viewModel.watermarkFilter.value)
    }

    @Test
    fun `set editor watermark thumbnail without asset init should get thumbnail bitmap`() {
        // Given
        val shopName = "Watermark"
        val bitmap = ShadowBitmapFactory.create(shopName, BitmapFactory.Options())

        // When
        every { watermarkFilterRepository.isAssetInitialize() } returns true
        every { watermarkFilterRepository.watermarkDrawerItem(any(), any()) } returns Pair(
            bitmap,
            bitmap
        )
        every { userSession.shopName } returns shopName

        viewModel.setWatermarkFilterThumbnail(
            bitmap
        )

        // Then
        verify { watermarkFilterRepository.watermarkDrawerItem(any(), any()) }
    }

    @Test
    fun `set editor watermark thumbnail with asset init should get thumbnail bitmap`() {
        // Given
        val shopName = "Watermark"
        val bitmap = ShadowBitmapFactory.create(shopName, BitmapFactory.Options())
        val drawable = ShadowDrawable.createFromResourceId(editorR.drawable.watermark_tokopedia)

        // When

        every { watermarkFilterRepository.isAssetInitialize() } returns false
        every { watermarkFilterRepository.setAsset(any(), any(), any()) } just Runs
        every { watermarkFilterRepository.watermarkDrawerItem(any(), any()) } returns Pair(
            bitmap,
            bitmap
        )
        every { userSession.shopName } returns shopName
        every { resourceProvider.getWatermarkLogoDrawable() } returns drawable
        every { resourceProvider.getWatermarkTextColor() } returns Pair(0, 0)

        viewModel.setWatermarkFilterThumbnail(
            bitmap
        )

        // Then
        verify { watermarkFilterRepository.watermarkDrawerItem(any(), any()) }
    }

    @Test
    fun `set editor watermark null should break`() {
        // Given
        val shopName = "Watermark"
        val bitmap = ShadowBitmapFactory.create(shopName, BitmapFactory.Options())

        // When
        every { watermarkFilterRepository.isAssetInitialize() } returns false
        every { watermarkFilterRepository.setAsset(any(), any(), any()) } just Runs
        every { userSession.shopName } returns shopName
        every { resourceProvider.getWatermarkLogoDrawable() } returns null
        every {
            hint(Bitmap::class)
            getWatermarkUseCase(any())
        } returns bitmap
        viewModel.setWatermark(
            bitmap,
            WatermarkType.Diagonal,
            detailUiModel = EditorDetailUiModel(),
            useStorageColor = false
        )

        // Then
        verify { getWatermarkUseCase(any()) }
        assertEquals(bitmap, viewModel.watermarkFilter.value)
    }

    @Test
    fun `set editor param should get editor param`() {
        // Given
        val editorParam = EditorParam().apply {
            withWatermark()
        }

        // When
        viewModel.setEditorParam(
            editorParam
        )

        // Then
        assertEquals(
            editorParam.editorToolsList().size,
            viewModel.editorParam.value?.editorToolsList()?.size
        )
    }

    @Test
    fun `set editor rotate should implement value on view`() {
        // Given
        val previewWidget = mockk<EditorDetailPreviewWidget>()

        // When
        every { rotateFilterRepository.rotate(any(), any(), any(), any(), any()) } just Runs
        viewModel.setRotate(
            previewWidget,
            1f,
            false
        )

        // Then
        verify { rotateFilterRepository.rotate(any(), any(), any(), any(), any()) }
    }

    @Test
    fun `set editor rotate null should break`() {
        // When
        viewModel.setRotate(
            null,
            1f,
            false
        )

        // Then
        verify(exactly = 0) { rotateFilterRepository.rotate(any(), any(), any(), any(), any()) }
    }

    @Test
    fun `set editor mirror should implement value on view`() {
        // Given
        val previewWidget = mockk<EditorDetailPreviewWidget>()

        // When
        every { rotateFilterRepository.mirror(any()) } just Runs
        viewModel.setMirror(
            previewWidget
        )

        // Then
        verify { rotateFilterRepository.mirror(any()) }
    }

    @Test
    fun `set editor mirror null should break`() {
        // When
        viewModel.setMirror(
            null
        )

        // Then
        verify(exactly = 0) { rotateFilterRepository.mirror(any()) }
    }

    @Test
    fun `set editor rotate number should get value`() {
        // Given
        val rotateNumber = 1
        val propertyName = "rotateNumber"

        // When
        every { rotateFilterRepository setProperty propertyName value less(10) } just Runs
        every { rotateFilterRepository getProperty propertyName } returns rotateNumber

        viewModel.rotateNumber = rotateNumber

        // Then
        verify { viewModel.rotateNumber = rotateNumber }
        assertEquals(rotateNumber, viewModel.rotateNumber)
        verify { viewModel getProperty propertyName }
    }

    @Test
    fun `set editor rotate slider should get value`() {
        // Given
        val rotateSliderValue = 1f
        val propertyName = "sliderValue"

        // When
        every { rotateFilterRepository setProperty propertyName value less(10f) } just Runs
        every { rotateFilterRepository getProperty propertyName } returns rotateSliderValue

        viewModel.rotateSliderValue = rotateSliderValue

        // Then
        verify { viewModel.rotateSliderValue = rotateSliderValue }
        assertEquals(rotateSliderValue, viewModel.rotateSliderValue)
        verify { viewModel getProperty "rotateSliderValue" }
    }

    @Test
    fun `set editor rotate scale should get value`() {
        // Given
        val rotateInitialScale = 1f
        val propertyName = "initialScale"

        // When
        every { rotateFilterRepository setProperty propertyName value less(10f) } just Runs
        every { rotateFilterRepository getProperty propertyName } returns rotateInitialScale

        viewModel.rotateInitialScale = rotateInitialScale

        // Then
        verify { viewModel.rotateInitialScale = rotateInitialScale }
        assertEquals(rotateInitialScale, viewModel.rotateInitialScale)
        verify { viewModel getProperty "rotateInitialScale" }
    }

    @Test
    fun `set editor rotate previous degree should get value`() {
        // Given
        val rotatePreviousDegree = 1f
        val propertyName = "previousDegree"

        // When
        every { rotateFilterRepository setProperty propertyName value less(10f) } just Runs
        every { rotateFilterRepository getProperty propertyName } returns rotatePreviousDegree

        viewModel.rotatePreviousDegree = rotatePreviousDegree

        // Then
        verify { viewModel.rotatePreviousDegree = rotatePreviousDegree }
        assertEquals(rotatePreviousDegree, viewModel.rotatePreviousDegree)
        verify { viewModel getProperty "rotatePreviousDegree" }
    }

    @Test
    fun `get editor rotate final degree should get value`() {
        // Given
        val degreeResult = 10f

        // When
        every { rotateFilterRepository.getFinalRotationDegree() } answers { degreeResult }


        // Then
        assertEquals(degreeResult, viewModel.rotateRotationFinalDegree)
    }

    @Test
    fun `save image should store bitmap`() {
        // Given
        val bitmap = ShadowBitmapFactory.create("", BitmapFactory.Options())

        // When
        every { saveImageRepository.saveToCache(any(), any(), any()) } returns null
        viewModel.saveImageCache(
            bitmap,
            sourcePath = ""
        )

        // Then
        verify { saveImageRepository.saveToCache(any(), any(), any()) }
    }

    @Test
    fun `set editor remove background should get bitmap`() {
        // Given
        var isFinishLoading = false

        // When
        coEvery { removeBackgroundUseCase(any()) } returns flow {
            emit(File(""))
        }
        viewModel.isLoading.observeForever {
            if (it) isFinishLoading = true
        }
        viewModel.setRemoveBackground("") {}
        viewModel.setRemoveBackground("") {}

        // Then
        assertTrue(isFinishLoading)
        assertTrue(viewModel.removeBackground.value != null)
    }

    @Test
    fun `set editor remove background error should break`() {
        // Given
        var isError = false

        // When
        coEvery { removeBackgroundUseCase(any()) } returns flow {
            throw Exception()
        }
        viewModel.setRemoveBackground("") {
            isError = true
        }

        // Then
        assertTrue(isError)
    }

    companion object {
        private const val videoKey = "/storage/sdcard/Pictures/Video1.mp4"
    }
}
