package tkpd.tokopedia.com.brandlist.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.brandlist.brandlist_page.domain.GetBrandlistAllBrandUseCase
import com.tokopedia.brandlist.brandlist_page.domain.GetBrandlistPopularBrandUseCase
import com.tokopedia.brandlist.brandlist_search.presentation.viewmodel.BrandlistSearchViewModel
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.mockkObject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
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

    private val dispatchers by lazy {
        Dispatchers.Unconfined
    }

    private val viewModel by lazy {
        BrandlistSearchViewModel(
                getBrandlistPopularBrandUseCase,
                getBrandlistAllBrandUseCase,
                dispatchers
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
        }
        viewModel.loadInitialBrands()
        coVerify {
            getBrandlistAllBrandUseCase.executeOnBackground()
        }
    }

    @Test
    fun `load more brand should execute expected usecase`() {
        mockkObject(getBrandlistAllBrandUseCase)
        coEvery {
            getBrandlistAllBrandUseCase.executeOnBackground()
        }
        viewModel.loadMoreBrands()
        coVerify {
            getBrandlistAllBrandUseCase.executeOnBackground()
        }
    }

    @Test
    fun `search brand should execute expected usecase`() {
        mockkObject(getBrandlistAllBrandUseCase)
        coEvery {
            getBrandlistAllBrandUseCase.executeOnBackground()
        }
        val categoryId = 0
        val offset = 0
        val query = "Samsung"
        val sortType = 1
        val firstLetter = ""
        val brandSize = 10
        viewModel.searchBrand(categoryId, offset, query, brandSize, sortType, firstLetter)
        coVerify {
            getBrandlistAllBrandUseCase.executeOnBackground()
        }
    }

    @Test
    fun `search recommendation should execute expected usecase`() {
        mockkObject(getBrandlistAllBrandUseCase)
        coEvery {
            getBrandlistPopularBrandUseCase.executeOnBackground()
        }
        val userId = 640
        val categoryIds = "0"
        viewModel.searchRecommendation(userId, categoryIds)
        coVerify {
            getBrandlistPopularBrandUseCase.executeOnBackground()
        }
    }

    @Test
    fun `search all brands should execute expected usecase`() {
        mockkObject(getBrandlistAllBrandUseCase)
        coEvery {
            getBrandlistAllBrandUseCase.executeOnBackground()
        }
        val categoryId = 0
        val offset = 0
        val query = ""
        val sortType = 1
        val firstLetter = "A"
        val brandSize = 10
        viewModel.searchAllBrands(categoryId, offset, query, brandSize, sortType, firstLetter)
        coVerify {
            getBrandlistAllBrandUseCase.executeOnBackground()
        }
    }

    @Test
    fun `get Total brands should execute expected usecase`() {
        mockkObject(getBrandlistAllBrandUseCase)
        coEvery {
            getBrandlistAllBrandUseCase.executeOnBackground()
        }
        viewModel.getTotalBrands()
        coVerify {
            getBrandlistAllBrandUseCase.executeOnBackground()
        }
    }

}