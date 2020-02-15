package tkpd.tokopedia.com.brandlist.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.brandlist.brandlist_page.domain.GetBrandlistAllBrandUseCase
import com.tokopedia.brandlist.brandlist_page.domain.GetBrandlistPopularBrandUseCase
import com.tokopedia.brandlist.brandlist_search.presentation.viewmodel.BrandlistSearchViewModel
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.RelaxedMockK
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




}