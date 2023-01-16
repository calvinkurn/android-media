package com.tokopedia.product_ar.viewmodel

import android.graphics.Bitmap
import android.graphics.Color
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.gson.Gson
import com.tokopedia.product_ar.model.state.ImageMapMode
import com.tokopedia.product_ar.util.ProductArComparissonAssertAssistant
import com.tokopedia.product_ar.util.ProductArUseCaseMapperTest
import com.tokopedia.product_ar.util.TestUtil
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.spyk
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class ProductArComparissonViewModelTest {

    @RelaxedMockK
    private lateinit var mockBitmap: Bitmap

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private val viewModel by lazy {
        ProductArComparissonViewModel()
    }

    private val spykViewModel by lazy {
        spyk(ProductArComparissonViewModel())
    }

    private val gson = Gson()
    private val mockData = TestUtil.getProductArDataMock(gson)
    private val mockOfUiData = ProductArUseCaseMapperTest.mapToModifaceUiModel("2684167590", mockData)
    private var assertAssistant = ProductArComparissonAssertAssistant(viewModel)

    @Before
    fun setup() {
        MockKAnnotations.init(this)
    }

    @Test
    fun `success render initial data`() {
        viewModel.renderInitialData(mockBitmap, mockOfUiData)

        val processedList = viewModel.processedVariantData.value
        val selectedId = processedList?.firstOrNull { it.isSelected }?.productId
        Assert.assertEquals(selectedId, "2684167590")

        val appendLiveData = viewModel.addRemoveImageGrid.value

        Assert.assertEquals(appendLiveData.mode, ImageMapMode.APPEND)
        Assert.assertEquals(appendLiveData.spanSize, 1)
        Assert.assertEquals(appendLiveData.removePosition, -1)
        Assert.assertEquals(appendLiveData.imagesBitmap.size, 1)
        Assert.assertEquals(appendLiveData.imagesBitmap.firstOrNull()?.productName ?: "", "Merah")
    }

    @Test
    fun `success add four image when click variant`() {
        `success render initial data`()
        assertAssistant.onVariantClicked(
                "2684217811",
                "Ungu",
                false
        )
        assertAssistant.appendOneImage(
                expectedColor = Color.argb(255, 191, 0, 255),
                expectedMode = ImageMapMode.APPEND,
                expectedSpanSize = 2,
                expectedBitmapSize = 2,
                expectedSelectedValue = listOf("Merah", "Ungu"),
                mockBitmap = mockBitmap
        )

        assertAssistant.onVariantClicked(
                "2684217809",
                "Orange",
                false
        )
        assertAssistant.appendOneImage(
                expectedColor = Color.argb(255, 255, 165, 0),
                expectedMode = ImageMapMode.APPEND,
                expectedSpanSize = 2,
                expectedBitmapSize = 3,
                expectedSelectedValue = listOf("Merah", "Ungu", "Orange"),
                mockBitmap = mockBitmap
        )

        assertAssistant.onVariantClicked(
                "2684217810",
                "Cokelat",
                false
        )
        assertAssistant.appendOneImage(
                expectedColor = Color.argb(255, 194, 114, 107),
                expectedMode = ImageMapMode.APPEND,
                expectedSpanSize = 2,
                expectedBitmapSize = 4,
                expectedSelectedValue = listOf("Merah", "Ungu", "Orange", "Cokelat"),
                mockBitmap = mockBitmap
        )
    }

    @Test
    fun `trying to add five image when click variant`() {
        `success add four image when click variant`()

        assertAssistant.onVariantClicked(
                "2684167589",
                "Merah Muda",
                false
        )
        assertAssistant.appendOneImage(
                expectedColor = Color.argb(255, 194, 114, 107),
                expectedMode = ImageMapMode.APPEND,
                expectedSpanSize = 2,
                expectedBitmapSize = 4,
                expectedSelectedValue = listOf("Merah", "Ungu", "Orange", "Cokelat"),
                mockBitmap = mockBitmap
        )
    }

    @Test
    fun `success remove three image when click variant`() {
        `success add four image when click variant`()

        assertAssistant.onVariantClicked(
                "2684217810",
                "Cokelat",
                true
        )
        assertAssistant.removeOneImage(
                expectedMode = ImageMapMode.REMOVE,
                expectedSpanSize = 2,
                expectedBitmapSize = 3,
                expectedSelectedValue = listOf("Merah", "Ungu", "Orange"),
                expectedRemovedPosition = 3
        )

        assertAssistant.onVariantClicked(
                "2684217809",
                "Orange",
                true
        )
        assertAssistant.removeOneImage(
                expectedMode = ImageMapMode.REMOVE,
                expectedSpanSize = 2,
                expectedBitmapSize = 2,
                expectedSelectedValue = listOf("Merah", "Ungu"),
                expectedRemovedPosition = 2
        )


        assertAssistant.onVariantClicked(
                "2684217811",
                "Ungu",
                true
        )
        assertAssistant.removeOneImage(
                expectedMode = ImageMapMode.REMOVE,
                expectedSpanSize = 1,
                expectedBitmapSize = 1,
                expectedSelectedValue = listOf("Merah"),
                expectedRemovedPosition = 1
        )
    }

    @Test
    fun `trying to remove one image with only one image left`() {
        `success render initial data`()
        //Only merah and want to remove merah from selection
        assertAssistant.onVariantClicked(
                selectedProductId = "2684167590",
                selectedProductName = "Merah",
                isSelected = true
        )
        assertAssistant.removeOneImage(
                expectedMode = ImageMapMode.APPEND,
                expectedSpanSize = 1, // still append because we dont edit this livedata
                expectedBitmapSize = 1,
                expectedSelectedValue = listOf("Merah"),
                expectedRemovedPosition = -1
        )
    }

    @Test
    fun `render initial data return throw`() {
        every {
            spykViewModel.addGridImages(mockBitmap, listOf())
        } throws Throwable()

        spykViewModel.renderInitialData(mockBitmap, listOf())
    }
}