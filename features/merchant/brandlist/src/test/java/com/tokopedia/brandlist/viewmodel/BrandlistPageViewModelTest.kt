package com.tokopedia.brandlist.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.brandlist.brandlist_category.data.model.Category
import com.tokopedia.brandlist.brandlist_page.domain.GetBrandlistAllBrandUseCase
import com.tokopedia.brandlist.brandlist_page.domain.GetBrandlistFeaturedBrandUseCase
import com.tokopedia.brandlist.brandlist_page.domain.GetBrandlistPopularBrandUseCase
import com.tokopedia.brandlist.brandlist_page.presentation.viewmodel.BrandlistPageViewModel
import com.tokopedia.usecase.coroutines.Success
import io.mockk.*
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class BrandlistPageViewModelTest {

    @RelaxedMockK
    lateinit var getBrandListFeaturedBrandUseCase: GetBrandlistFeaturedBrandUseCase

    @RelaxedMockK
    lateinit var getBrandListPopularBrandUseCase: GetBrandlistPopularBrandUseCase

    @RelaxedMockK
    lateinit var getBrandListAllBrandUseCase: GetBrandlistAllBrandUseCase

    @get:Rule
    val rule = InstantTaskExecutorRule()


    private val viewModel by lazy {
        BrandlistPageViewModel(getBrandListFeaturedBrandUseCase, getBrandListPopularBrandUseCase, getBrandListAllBrandUseCase, CoroutineTestDispatchersProvider)
    }

    @Before
    fun setup() {
        MockKAnnotations.init(this)
    }



    @Test
    fun `set offset to static int value`() {
        val currentOffset = 100
        viewModel.setOffset()
        Assert.assertEquals(viewModel.getCurrentOffset(), currentOffset)
    }

    @Test
    fun `get offset should return int value`() {
        viewModel.getCurrentOffset()
        Assert.assertTrue(viewModel.getCurrentOffset() is Int)
    }

    @Test
    fun `update current offset should increment current static int value`() {
        val currentOffset = 20
        viewModel.updateCurrentOffset(currentOffset)
        Assert.assertEquals(viewModel.getCurrentOffset(), currentOffset)
    }

    @Test
    fun `reset all request param should change all param to default value`() {
        val firstLetterChanged = false
        val totalBrandSize = 0
        val currentOffset = 0

        viewModel.resetAllBrandRequestParameter()

        Assert.assertEquals(viewModel.getFirstLetterChanged(), firstLetterChanged)
        Assert.assertEquals(viewModel.getTotalBrandSize(), totalBrandSize)
        Assert.assertEquals(viewModel.getCurrentOffset(), currentOffset)
    }

    @Test
    fun `update total brand size should update total brand size using current value`() {
        val totalBrandSize = 80
        viewModel.updateTotalBrandSize(totalBrandSize)
        Assert.assertEquals(viewModel.getTotalBrandSize(), totalBrandSize)
    }


    @Test
    fun `load initial data when category and userid are provided should return success value`() {
        // expected use cases :
        // GetBrandlistFeaturedBrandUseCase, GetBrandlistPopularBrandUseCase, getBrandListAllBrandUseCase
        mockkObject(getBrandListFeaturedBrandUseCase)
        mockkObject(getBrandListPopularBrandUseCase)
        mockkObject(getBrandListAllBrandUseCase)

        coEvery {
            getBrandListFeaturedBrandUseCase.executeOnBackground()
            getBrandListPopularBrandUseCase.executeOnBackground()
            getBrandListAllBrandUseCase.executeOnBackground()
        }

        val mockedCategory = Category()
        viewModel.loadInitialData(mockedCategory, "0")

        coVerify {
            getBrandListFeaturedBrandUseCase.executeOnBackground()
            getBrandListPopularBrandUseCase.executeOnBackground()
            getBrandListAllBrandUseCase.executeOnBackground()
        }

        Assert.assertTrue(viewModel.getFeaturedBrandResult.value is Success)
        Assert.assertTrue(viewModel.getPopularBrandResult.value is Success)
        Assert.assertTrue(viewModel.getNewBrandResult.value is Success)
    }

    @Test
    fun `load more data when category and first letter are provided should return success value`() {
        mockkObject(getBrandListAllBrandUseCase)
        coEvery { getBrandListAllBrandUseCase.executeOnBackground() }

        val mockedCategory = Category()
        val brandFirstLetter = "b"

        viewModel.loadMoreAllBrands(mockedCategory, brandFirstLetter)
        coVerify { getBrandListAllBrandUseCase.executeOnBackground() }
        Assert.assertTrue(viewModel.getAllBrandResult.value is Success)
    }


    @Test
    fun `load brand per-alphabet when category and first letter are provided should return success value`() {
        mockkObject(getBrandListAllBrandUseCase)
        coEvery { getBrandListAllBrandUseCase.executeOnBackground() }

        val mockedCategory = Category()
        val brandFirstLetter = "d"

        viewModel.loadBrandsPerAlphabet(mockedCategory, brandFirstLetter)
        coVerify { getBrandListAllBrandUseCase.executeOnBackground() }
        Assert.assertTrue(viewModel.getAllBrandResult.value is Success)
    }

    @Test
    fun `load all brand when category is provided should return success value of brand header list and brands result`() {
        mockkObject(getBrandListAllBrandUseCase)
        coEvery { getBrandListAllBrandUseCase.executeOnBackground() }

        val mockedCategory = Category()

        viewModel.loadAllBrands(mockedCategory)
        coVerify { getBrandListAllBrandUseCase.executeOnBackground() }
        Assert.assertTrue(viewModel.getAllBrandHeaderResult.value is Success)
        Assert.assertTrue(viewModel.getAllBrandResult.value is Success)
    }
}