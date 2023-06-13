package com.tokopedia.product_ar.util

import android.graphics.Bitmap
import com.tokopedia.product_ar.model.state.ImageMapMode
import com.tokopedia.product_ar.viewmodel.ProductArComparissonViewModel
import org.junit.Assert

class ProductArComparissonAssertAssistant(private val viewModel: ProductArComparissonViewModel) {

    fun appendOneImage(expectedColor: Int,
                       expectedMode: ImageMapMode,
                       expectedSpanSize: Int,
                       expectedBitmapSize: Int,
                       expectedSelectedValue: List<String>,
                       mockBitmap: Bitmap) {
        val modifaceData = viewModel.generateMakeUpBackground.value?.lipLayers?.firstOrNull()?.product
        Assert.assertEquals(modifaceData?.color ?: 0, expectedColor)

        viewModel.addGridImages(mockBitmap, viewModel.addRemoveImageGrid.value.imagesBitmap)

        val comparissonImageList = viewModel.addRemoveImageGrid.value
        Assert.assertEquals(comparissonImageList.mode, expectedMode)
        Assert.assertEquals(comparissonImageList.spanSize, expectedSpanSize)
        Assert.assertEquals(comparissonImageList.removePosition, -1)

        Assert.assertEquals(comparissonImageList.imagesBitmap.size, expectedBitmapSize)
        expectedSelectedValue.forEachIndexed { index, s ->
            Assert.assertEquals(comparissonImageList.imagesBitmap[index].productName, s)
        }

        val variants = viewModel.processedVariantData.value
        val countIsSelected = variants?.count {
            it.isSelected
        } ?: 0
        Assert.assertEquals(countIsSelected, expectedBitmapSize)
        expectedSelectedValue.forEachIndexed { index, s ->
            val data = variants?.firstOrNull { it.productName == s }
            Assert.assertNotNull(data)
            Assert.assertEquals(data!!.isSelected, true)
            Assert.assertEquals(data.counter, index + 1)
        }
    }

    fun removeOneImage(expectedMode: ImageMapMode,
                       expectedSpanSize: Int,
                       expectedBitmapSize: Int,
                       expectedSelectedValue: List<String>,
                       expectedRemovedPosition: Int) {
        val comparissonImageList = viewModel.addRemoveImageGrid.value
        Assert.assertEquals(comparissonImageList.mode, expectedMode)
        Assert.assertEquals(comparissonImageList.spanSize, expectedSpanSize)
        Assert.assertEquals(comparissonImageList.removePosition, expectedRemovedPosition)

        Assert.assertEquals(comparissonImageList.imagesBitmap.size, expectedBitmapSize)
        expectedSelectedValue.forEachIndexed { index, s ->
            Assert.assertEquals(comparissonImageList.imagesBitmap[index].productName, s)
        }

        val variants = viewModel.processedVariantData.value
        val countIsSelected = variants?.count {
            it.isSelected
        } ?: 0
        Assert.assertEquals(countIsSelected, expectedBitmapSize)
        expectedSelectedValue.forEachIndexed { index, s ->
            val data = variants?.firstOrNull { it.productName == s }
            Assert.assertNotNull(data)
            Assert.assertEquals(data!!.isSelected, true)
            Assert.assertEquals(data.counter, index + 1)
        }
    }

    fun onVariantClicked(selectedProductId: String,
                         selectedProductName: String,
                         isSelected: Boolean) {
        viewModel.onVariantClicked(
                data = viewModel.processedVariantData.value ?: listOf(),
                selectedProductId = selectedProductId,
                selectedProductName = selectedProductName,
                isSelected = isSelected
        )
    }
}