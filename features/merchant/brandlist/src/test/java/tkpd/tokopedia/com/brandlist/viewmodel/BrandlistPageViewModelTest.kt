package tkpd.tokopedia.com.brandlist.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.brandlist.brandlist_category.data.model.Category
import com.tokopedia.brandlist.brandlist_page.domain.GetBrandlistAllBrandUseCase
import com.tokopedia.brandlist.brandlist_page.domain.GetBrandlistFeaturedBrandUseCase
import com.tokopedia.brandlist.brandlist_page.domain.GetBrandlistPopularBrandUseCase
import com.tokopedia.brandlist.brandlist_page.presentation.viewmodel.BrandlistPageViewModel
import io.mockk.*
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
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

    private val dispatchers by lazy {
        Dispatchers.Unconfined
    }

    private val viewModel by lazy {
        BrandlistPageViewModel(getBrandListFeaturedBrandUseCase, getBrandListPopularBrandUseCase, getBrandListAllBrandUseCase, dispatchers)
    }

    @Before
    fun setup() {
        MockKAnnotations.init(this)
    }

    @Test
    fun `load initial data should execute expected use cases`() {
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
        val mockedCategory = mockk<Category>()
        viewModel.loadInitialData(mockedCategory, "0")
        coVerify {
            getBrandListFeaturedBrandUseCase.executeOnBackground()
            getBrandListPopularBrandUseCase.executeOnBackground()
            getBrandListAllBrandUseCase.executeOnBackground()
        }
    }

    @Test
    fun `load more data should execute getBrandListAllBrandUseCase`() {
        mockkObject(getBrandListAllBrandUseCase)
        coEvery { getBrandListAllBrandUseCase.executeOnBackground() }
        val mockedCategory = mockk<Category>()
        viewModel.loadMoreAllBrands(mockedCategory)
        coVerify { getBrandListAllBrandUseCase.executeOnBackground() }
    }
}