package com.tokopedia.product_ar.viewmodel

import android.graphics.Bitmap
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.product_ar.model.ModifaceUiModel
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.RelaxedMockK
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class ProductArSharedViewModelTest {

    @RelaxedMockK
    private lateinit var mockBitmap: Bitmap

    private val viewModel by lazy {
        ProductArSharedViewModel()
    }

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Before
    fun setup() {
        MockKAnnotations.init(this)
    }

    @Test
    fun `success add data`() {
        viewModel.setArListData(
                listOf(ModifaceUiModel()),
                mockBitmap,
                mockBitmap
        )

        Assert.assertNotNull(viewModel.arListData.value)
        Assert.assertEquals(viewModel.arListData.value?.modifaceUiModel?.size, 1)
    }
}