package tkpd.tokopedia.com.brandlist.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.brandlist.brandlist_category.data.model.BrandlistCategories
import com.tokopedia.brandlist.brandlist_category.domain.GetBrandlistCategoriesUseCase
import com.tokopedia.brandlist.brandlist_category.presentation.viewmodel.BrandlistCategoryViewModel
import com.tokopedia.usecase.coroutines.Success
import io.mockk.*
import io.mockk.impl.annotations.RelaxedMockK
//import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import tkpd.tokopedia.com.brandlist.TestDispatcherProvider

@ExperimentalCoroutinesApi
class BrandlistCategoryTabViewModelTest {

    @RelaxedMockK
    lateinit var getBrandlistCategoriesUseCase: GetBrandlistCategoriesUseCase

    @get:Rule
    val rule = InstantTaskExecutorRule()

//    private val dispatchers by lazy {
//        Dispatchers.Unconfined
//    }

    private val viewModel by lazy {
        BrandlistCategoryViewModel(getBrandlistCategoriesUseCase, TestDispatcherProvider())
    }

    @Before
    fun setup() {
        MockKAnnotations.init(this)
    }

    @Test
    fun `given category list when request is executed`() {
        mockkObject(GetBrandlistCategoriesUseCase)
        coEvery {
            getBrandlistCategoriesUseCase.executeOnBackground()
        } returns BrandlistCategories()
        viewModel.getBrandlistCategories()
        coVerify {
            getBrandlistCategoriesUseCase.executeOnBackground()
        }
        Assert.assertTrue(viewModel.brandlistCategoriesResponse.value is Success)
    }

}