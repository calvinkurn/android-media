package com.tkpd.atc_variant

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tkpd.atc_variant.views.AtcVariantSharedViewModel
import com.tokopedia.product.detail.common.data.model.aggregator.ProductVariantBottomSheetParams
import com.tokopedia.product.detail.common.data.model.aggregator.ProductVariantResult
import io.mockk.MockKAnnotations
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * Created by Yehezkiel on 31/05/21
 */
class AtcVariantSharedViewModelTest {
    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Before
    fun setup() {
        MockKAnnotations.init(this)
    }

    private val sharedViewModel: AtcVariantSharedViewModel by lazy {
        AtcVariantSharedViewModel()
    }

    @Test
    fun `update aggregator params`() {
        sharedViewModel.setAtcBottomSheetParams(ProductVariantBottomSheetParams())

        Assert.assertNotNull(sharedViewModel.aggregatorParams.value)
    }

    @Test
    fun `update activity result`() {
        sharedViewModel.setActivityResult(ProductVariantResult())

        Assert.assertNotNull(sharedViewModel.activityResult.value)
    }

}