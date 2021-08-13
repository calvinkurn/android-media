package com.tokopedia.brandlist.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.brandlist.brandlist_page.data.model.OfficialStoreAllBrands
import com.tokopedia.brandlist.brandlist_page.data.model.OfficialStoreBrandsRecommendation
import com.tokopedia.brandlist.brandlist_page.domain.GetBrandlistAllBrandUseCase
import com.tokopedia.brandlist.brandlist_page.domain.GetBrandlistPopularBrandUseCase
import com.tokopedia.brandlist.brandlist_search.presentation.viewmodel.BrandlistSearchViewModel
import com.tokopedia.usecase.coroutines.Success
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.mockkObject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class BrandlistSearchViewModelTest {

    @RelaxedMockK
    lateinit var getBrandlistPopularBrandUseCase: GetBrandlistPopularBrandUseCase

    @RelaxedMockK
    lateinit var getBrandlistAllBrandUseCase: GetBrandlistAllBrandUseCase

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private val viewModel by lazy {
        BrandlistSearchViewModel(
                getBrandlistPopularBrandUseCase,
                getBrandlistAllBrandUseCase,
                CoroutineTestDispatchersProvider
        )
    }

    @Before
    fun setup() {
        MockKAnnotations.init(this)
    }

    @Test
    fun `load initial data should execute expected usecase`() {
        mockkObject(getBrandlistAllBrandUseCase)
        coEvery {
            getBrandlistAllBrandUseCase.executeOnBackground()
        } returns OfficialStoreAllBrands()
        viewModel.loadInitialBrands()
        coVerify {
            getBrandlistAllBrandUseCase.executeOnBackground()
        }
        Assert.assertTrue(viewModel.brandlistAllBrandsSearchResponse.value is Success)
    }

    @Test
    fun `load more brand without first brand letter should execute expected usecase`() {
        mockkObject(getBrandlistAllBrandUseCase)
        coEvery {
            getBrandlistAllBrandUseCase.executeOnBackground()
        } returns OfficialStoreAllBrands()
        viewModel.loadMoreBrands()
        coVerify {
            getBrandlistAllBrandUseCase.executeOnBackground()
        }
        Assert.assertTrue(viewModel.brandlistAllBrandsSearchResponse.value is Success)
    }

    @Test
    fun `load more brand with first brand letter should execute expected usecase`() {
        mockkObject(getBrandlistAllBrandUseCase)
        coEvery {
            getBrandlistAllBrandUseCase.executeOnBackground()
        } returns OfficialStoreAllBrands()
        val brandFirstLetter = "A"
        viewModel.loadMoreBrands(brandFirstLetter)
        coVerify {
            getBrandlistAllBrandUseCase.executeOnBackground()
        }
        Assert.assertTrue(viewModel.brandlistAllBrandsSearchResponse.value is Success)
    }

    @Test
    fun `ask total brand size should return int total brand size`() {
        viewModel.getTotalBrandSize()
        Assert.assertTrue(viewModel.getTotalBrandSize() is Int)
    }

    @Test
    fun `update total brand size when provided total brand size`() {
        val totalBrandSize: Int = 12
        viewModel.updateTotalBrandSize(totalBrandSize)
        Assert.assertEquals(viewModel.getTotalBrandSize(), totalBrandSize)
    }

    @Test
    fun `ask total brand size for chip header should return int total brand size on the chip header`() {
        viewModel.getTotalBrandSizeForChipHeader()
        Assert.assertTrue(viewModel.getTotalBrandSizeForChipHeader() is Int)
    }

    @Test
    fun `update total brand size for chip header when provided total brand size`() {
        val totalBrandSize: Int = 16
        viewModel.updateTotalBrandSizeForChipHeader(totalBrandSize)
        Assert.assertEquals(viewModel.getTotalBrandSizeForChipHeader(), totalBrandSize)
    }

    @Test
    fun `update current offset when provided total rendered brands`() {
        val renderedBrands: Int = 20
        viewModel.updateCurrentOffset(renderedBrands)
        Assert.assertEquals(viewModel.currentOffset, renderedBrands)
    }

    @Test
    fun `update current letter when provided current letter`() {
        val currentLetter: Char = 'a'
        viewModel.currentLetter = currentLetter
        Assert.assertEquals(viewModel.currentLetter, currentLetter)
    }

    @Test
    fun `update current offset when provided current offset`() {
        val currentOffset: Int = 0
        viewModel.currentOffset = currentOffset
        Assert.assertEquals(viewModel.currentOffset, currentOffset)
    }

    @Test
    fun `ask total current offset should return int total current offset`() {
        Assert.assertTrue(viewModel.currentOffset is Int)
    }

    @Test
    fun `should reset all params`() {
        val INITIAL_OFFSET: Int = 0
        viewModel.resetParams()

        Assert.assertEquals(viewModel.currentOffset, INITIAL_OFFSET)
        Assert.assertTrue(viewModel.getTotalBrandSize() is Int)
        Assert.assertTrue(viewModel.getFirstLetterChanged() is Boolean)
    }

    @Test
    fun `search brand should execute expected usecase`() {
        mockkObject(getBrandlistAllBrandUseCase)
        coEvery {
            getBrandlistAllBrandUseCase.executeOnBackground()
        } returns OfficialStoreAllBrands()
        val offset = 0
        val query = "Samsung"
        val firstLetter = ""
        val brandSize = 10

        viewModel.searchBrand(offset, query, brandSize, firstLetter)
        coVerify {
            getBrandlistAllBrandUseCase.executeOnBackground()
        }
        Assert.assertTrue(viewModel.brandlistSearchResponse.value is Success)
    }

    @Test
    fun `search recommendation should execute expected usecase`() {
        mockkObject(getBrandlistAllBrandUseCase)
        coEvery {
            getBrandlistPopularBrandUseCase.executeOnBackground()
        } returns OfficialStoreBrandsRecommendation()
        val userId = "640"
        val categoryIds: ArrayList<Int> = arrayListOf(1, 2, 3)
        viewModel.searchRecommendation(userId, categoryIds)
        coVerify {
            getBrandlistPopularBrandUseCase.executeOnBackground()
        }
        Assert.assertTrue(viewModel.brandlistSearchRecommendationResponse.value is Success)
    }

    @Test
    fun `search all brands should execute expected usecase`() {
        mockkObject(getBrandlistAllBrandUseCase)
        coEvery {
            getBrandlistAllBrandUseCase.executeOnBackground()
        } returns OfficialStoreAllBrands()

        val offset = 0
        val query = ""
        val sortType = 1
        val firstLetter = "A"
        val brandSize = 10

        viewModel.searchAllBrands(offset, query, brandSize, sortType, firstLetter)
        coVerify {
            getBrandlistAllBrandUseCase.executeOnBackground()
        }
        Assert.assertTrue(viewModel.brandlistAllBrandsSearchResponse.value is Success)
    }

    @Test
    fun `get Total brands should execute expected usecase`() {
        mockkObject(getBrandlistAllBrandUseCase)
        coEvery {
            getBrandlistAllBrandUseCase.executeOnBackground()
        } returns OfficialStoreAllBrands()
        viewModel.getTotalBrands()
        coVerify {
            getBrandlistAllBrandUseCase.executeOnBackground()
        }
        Assert.assertTrue(viewModel.brandlistAllBrandTotal.value is Success)
    }

}