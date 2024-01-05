package com.tokopedia.product.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.product.detail.common.data.model.variant.Variant
import com.tokopedia.product.detail.common.data.model.variant.Variant.Companion.isVariantEmpty
import com.tokopedia.product.detail.data.model.datamodel.ProductSingleVariantDataModel
import com.tokopedia.product.detail.view.viewmodel.product_detail.mediator.GetProductDetailDataMediator
import com.tokopedia.product.detail.view.viewmodel.product_detail.sub_viewmodel.ThumbnailVariantSubViewModel
import com.tokopedia.product.util.ProductDetailTestUtil
import com.tokopedia.product.util.getOrAwaitValue
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.unmockkAll
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.util.concurrent.TimeoutException

/**
 * Created by yovi.putra on 06/09/23"
 * Project name: tokopedia-app-wg
 **/

@ExperimentalCoroutinesApi
class ThumbnailVariantSubViewModelTest {
    @RelaxedMockK
    lateinit var mockMediator: GetProductDetailDataMediator

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private lateinit var viewModel: ThumbnailVariantSubViewModel

    @Before
    fun beforeTest() {
        MockKAnnotations.init(this)

        viewModel = ThumbnailVariantSubViewModel().apply {
            registerScope(viewModelScope = CoroutineScope(CoroutineTestDispatchersProvider.main))
            registerMediator(mediator = mockMediator)
        }
    }

    @After
    fun afterTest() {
        unmockkAll()
    }

    private fun everyVariant() {
        val dataP1 = ProductDetailTestUtil.getMockPdpLayoutMiniVariant()

        every {
            viewModel.pdpMediator.getP1()
        } returns dataP1.layoutData

        every {
            viewModel.pdpMediator.getVariant()
        } returns dataP1.variantData
    }

    @Test
    fun `select variant thumbnail is successful when first open pdp`() {
        val expectLvl1Category = "28323838"
        val expectLvl1Selected = "94748050"
        val expectLvl2Category = "28323839"
        val expectLvl2Selected = "94748052"
        val singleVariant = ProductSingleVariantDataModel()

        everyVariant()

        viewModel.onThumbnailVariantSelected(
            uiData = singleVariant,
            variantId = expectLvl1Selected,
            categoryKey = expectLvl1Category
        )

        val result = viewModel.onThumbnailVariantSelectedData.getOrAwaitValue()
        val variantLvl1Selected = result?.mapOfSelectedVariant.orEmpty()[expectLvl1Category]
        val variantLvl2Selected = result?.mapOfSelectedVariant.orEmpty()[expectLvl2Category]
        Assert.assertTrue(variantLvl1Selected == expectLvl1Selected)
        Assert.assertTrue(variantLvl2Selected == expectLvl2Selected)
    }

    @Test
    fun `change select variant thumbnail is successful when previously selected`() {
        val expectSelected = "94748050"
        val expectCategory = "28323838"
        val singleVariant = ProductSingleVariantDataModel(
            mapOfSelectedVariant = mutableMapOf(
                expectCategory to "94748049",
                "28323839" to "94748052"
            )
        )

        everyVariant()

        viewModel.onThumbnailVariantSelected(
            uiData = singleVariant,
            variantId = expectSelected,
            categoryKey = expectCategory
        )

        val result = viewModel.onThumbnailVariantSelectedData.getOrAwaitValue()
        val variantLevelOneSelected = result?.mapOfSelectedVariant.orEmpty()[expectCategory]
        Assert.assertTrue(variantLevelOneSelected == expectSelected)
    }

    @Test
    fun `thumbnail variant selected is success when variant have lvl 1 only`() {
        val expectSelected = "94748050"
        val expectCategory = "28323838"
        val singleVariant = ProductSingleVariantDataModel(
            mapOfSelectedVariant = mutableMapOf(
                expectCategory to "94748049",
                "28323839" to "94748052"
            )
        )
        val pdpData = ProductDetailTestUtil.getMockPdpLayoutMiniVariant()
        every {
            viewModel.pdpMediator.getVariant()
        } returns run {
            // give variant one only to variant data
            val variantLvl1 = pdpData.variantData?.variants?.firstOrNull() ?: Variant()
            pdpData.variantData?.copy(
                variants = listOf(variantLvl1)
            )
        }

        viewModel.onThumbnailVariantSelected(
            uiData = singleVariant,
            variantId = expectSelected,
            categoryKey = expectCategory
        )

        val result = viewModel.onThumbnailVariantSelectedData.getOrAwaitValue()
        val variantLevelOneSelected = result?.mapOfSelectedVariant.orEmpty()[expectCategory]
        Assert.assertTrue(variantLevelOneSelected == expectSelected)
    }

    @Test
    fun `variant lvl 2 is empty on thumbnail variant selected when variant lvl 2 options is empty`() {
        val variantLvl1 = "94748050"
        val categoryLvl1 = "28323838"
        val categoryLvl2 = "28323839"
        val singleVariant = ProductSingleVariantDataModel(
            mapOfSelectedVariant = mutableMapOf(
                categoryLvl1 to "94748049"
            )
        )
        val pdpData = ProductDetailTestUtil.getMockPdpLayoutMiniVariant()

        // give variant two with variant options is empty
        every {
            viewModel.pdpMediator.getVariant()
        } returns run {
            // give variant one only to variant data
            pdpData.variantData?.variants.orEmpty().toMutableList().map {
                if (it.pv == categoryLvl2) {
                    it.copy(options = emptyList())
                } else {
                    it
                }
            }.let {
                pdpData.variantData?.copy(variants = it)
            }
        }

        viewModel.onThumbnailVariantSelected(
            uiData = singleVariant,
            variantId = variantLvl1,
            categoryKey = categoryLvl1
        )

        val result = viewModel.onThumbnailVariantSelectedData.getOrAwaitValue()
        val variantLvl2Selected = result?.mapOfSelectedVariant.orEmpty()[categoryLvl2]
        Assert.assertTrue(variantLvl2Selected?.isEmpty() == true)
    }

    @Test
    fun `don't select variant thumbnail when select variant with variant id is empty`() {
        val categoryKeyLvl1 = "28323838"
        val categoryKeyLvl2 = "28323839"
        val singleVariant = ProductSingleVariantDataModel(
            mapOfSelectedVariant = mutableMapOf(
                categoryKeyLvl1 to "94748049",
                categoryKeyLvl2 to "94748052"
            )
        )

        everyVariant()

        viewModel.onThumbnailVariantSelected(
            uiData = singleVariant,
            variantId = "",
            categoryKey = ""
        )

        val result = viewModel.onThumbnailVariantSelectedData.getOrAwaitValue()
        val variantLvl1Selected = result?.mapOfSelectedVariant.orEmpty()[categoryKeyLvl1]
        val variantLvl2Selected = result?.mapOfSelectedVariant.orEmpty()[categoryKeyLvl2]
        Assert.assertTrue(variantLvl1Selected.orEmpty().isVariantEmpty())
        Assert.assertFalse(variantLvl2Selected.orEmpty().isVariantEmpty())
    }

    @Test
    fun `thumbnail variant not changes when variants is empty`() {
        val categoryKeyLvl1 = "28323838"
        val categoryKeyLvl2 = "28323839"
        val singleVariant = ProductSingleVariantDataModel(
            mapOfSelectedVariant = mutableMapOf(
                categoryKeyLvl1 to "94748049",
                categoryKeyLvl2 to "94748052"
            )
        )
        val pdpData = ProductDetailTestUtil.getMockPdpLayoutMiniVariant()

        // give empty variant data to be expected
        every {
            viewModel.pdpMediator.getVariant()
        } returns run {
            pdpData.variantData?.copy(variants = listOf(Variant(pv = null)))
        }

        viewModel.onThumbnailVariantSelected(
            uiData = singleVariant,
            variantId = "",
            categoryKey = ""
        )

        try {
            viewModel.onThumbnailVariantSelectedData.getOrAwaitValue()
        } catch (e: Throwable) {
            Assert.assertTrue(e is TimeoutException)
        }
    }

    @Test
    fun `thumbnail variant change is success when variant lv2 have pv is null`() {
        val selectVariant1 = "94748050"
        val categoryKeyLvl1 = "28323838"
        val categoryKeyLvl2 = "28323839"
        val singleVariant = ProductSingleVariantDataModel(
            mapOfSelectedVariant = mutableMapOf(
                categoryKeyLvl1 to "94748049"
            )
        )
        val pdpData = ProductDetailTestUtil.getMockPdpLayoutMiniVariant()

        every {
            viewModel.pdpMediator.getVariant()
        } returns run {
            pdpData.variantData?.variants.orEmpty()
                .map {
                    if (it.pv == categoryKeyLvl2) {
                        it.copy(pv = null)
                    } else {
                        it
                    }
                }.let {
                    pdpData.variantData?.copy(variants = it)
                }
        }

        viewModel.onThumbnailVariantSelected(
            uiData = singleVariant,
            variantId = selectVariant1,
            categoryKey = categoryKeyLvl1
        )

        val result = viewModel.onThumbnailVariantSelectedData.getOrAwaitValue()
        val variantLvl1Selected = result?.mapOfSelectedVariant.orEmpty()[categoryKeyLvl1]
        val variantLvl2Selected = result?.mapOfSelectedVariant.orEmpty()[categoryKeyLvl2]
        val variantLvl2Actual = result?.mapOfSelectedVariant.orEmpty()[""]
        Assert.assertTrue(variantLvl1Selected == selectVariant1)
        Assert.assertTrue(variantLvl2Selected == null)
        Assert.assertFalse(variantLvl2Actual.isNullOrEmpty())
    }

    @Test
    fun `no impact when select variant thumbnail with variant data is null`() {
        val categoryKeyLvl1 = "28323838"
        val categoryKeyLvl2 = "28323839"
        val singleVariant = ProductSingleVariantDataModel(
            mapOfSelectedVariant = mutableMapOf(
                categoryKeyLvl1 to "94748049",
                categoryKeyLvl2 to "94748052"
            )
        )

        everyVariant()

        every {
            viewModel.pdpMediator.getVariant()
        } returns null

        viewModel.onThumbnailVariantSelected(
            uiData = singleVariant,
            variantId = "",
            categoryKey = ""
        )

        try {
            viewModel.onThumbnailVariantSelectedData.getOrAwaitValue()
        } catch (e: Throwable) {
            Assert.assertTrue(e is TimeoutException)
        }
    }

    @Test
    fun `no impact when select variant thumbnail with ui-data is null `() {
        val expectSelected = "94748050"
        val expectCategory = "28323838"
        everyVariant()

        viewModel.onThumbnailVariantSelected(
            uiData = null,
            variantId = expectSelected,
            categoryKey = expectCategory
        )

        try {
            viewModel.onThumbnailVariantSelectedData.getOrAwaitValue()
        } catch (e: Throwable) {
            Assert.assertTrue(e is TimeoutException)
        }
    }
}
