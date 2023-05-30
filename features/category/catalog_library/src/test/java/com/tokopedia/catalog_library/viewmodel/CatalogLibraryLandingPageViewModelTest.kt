package com.tokopedia.catalog_library.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.catalog_library.model.raw.CatalogListResponse
import com.tokopedia.catalog_library.model.raw.CatalogSpecialResponse
import com.tokopedia.catalog_library.usecase.CatalogProductsUseCase
import com.tokopedia.catalog_library.util.CatalogLibraryConstant
import com.tokopedia.catalog_library.util.CatalogLibraryConstant.CATALOG_CONTAINER_TYPE_TOP_FIVE
import com.tokopedia.catalog_library.util.CatalogLibraryConstant.SORT_TYPE_TOP_FIVE
import com.tokopedia.catalog_library.util.CatalogLibraryResponseException
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class CatalogLibraryLandingPageViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val catalogTopFiveUseCase = mockk<CatalogProductsUseCase>(relaxed = true)
    private val catalogMostViralUseCase = mockk<CatalogProductsUseCase>(relaxed = true)

    private lateinit var viewModel: CatalogLandingPageViewModel

    private val fetchFailedErrorMessage = "Fetch Failed"
    private val mockThrowable = CatalogLibraryResponseException(fetchFailedErrorMessage, CATALOG_CONTAINER_TYPE_TOP_FIVE)

    @Before
    fun setUp() {
        viewModel = CatalogLandingPageViewModel(
            catalogTopFiveUseCase,
            catalogMostViralUseCase
        )
    }

    @Test
    fun `Get Top Five Success`() {
        val response = CatalogListResponse(
            CatalogListResponse.CatalogGetList(
                CatalogSpecialResponse.CatalogCategorySpecial.CatalogSpecialHeader(),
                arrayListOf(mockk(relaxed = true)),
                "Audio"
            )
        )
        coEvery {
            catalogTopFiveUseCase.getCatalogProductsData(any(), any(), any(), any(), any(), any(), any())
        } coAnswers {
            firstArg<(CatalogListResponse, Int) -> Unit>().invoke(response, 1)
        }
        viewModel.getCatalogTopFiveData(
            CatalogLibraryConstant.SOURCE_CATEGORY_BRAND_LANDING_PAGE,
            SORT_TYPE_TOP_FIVE,
            10
        )
        assert(viewModel.catalogLandingPageLiveDataResponse.value is Success)
        assert(viewModel.categoryName.value == "Audio")
    }

    @Test
    fun `Get Top Five success but condition fail`() {
        val response = CatalogListResponse(CatalogListResponse.CatalogGetList())
        coEvery {
            catalogTopFiveUseCase.getCatalogProductsData(any(), any(), any(), any(), any(), any(), any())
        } coAnswers {
            firstArg<(CatalogListResponse, Int) -> Unit>().invoke(response, 1)
        }
        viewModel.getCatalogTopFiveData(
            CatalogLibraryConstant.SOURCE_CATEGORY_BRAND_LANDING_PAGE,
            SORT_TYPE_TOP_FIVE,
            10
        )
        Assert.assertEquals(
            (viewModel.catalogLandingPageLiveDataResponse.value as Fail).throwable.localizedMessage,
            "No Catalog Landing Page Data"
        )
    }

    @Test
    fun `Get Top Five fetch failed throws exception`() {
        coEvery {
            catalogTopFiveUseCase.getCatalogProductsData(any(), any(), any(), any(), any(), any(), any())
        } coAnswers {
            secondArg<(Throwable) -> Unit>().invoke(mockThrowable)
        }

        viewModel.getCatalogTopFiveData(
            CatalogLibraryConstant.SOURCE_CATEGORY_BRAND_LANDING_PAGE,
            SORT_TYPE_TOP_FIVE,
            10
        )
        assert(
            (viewModel.catalogLandingPageLiveDataResponse.value is Fail)
        )
    }

    @Test
    fun `Get Most Viral Success`() {
        val response = CatalogListResponse(
            CatalogListResponse.CatalogGetList(
                CatalogSpecialResponse.CatalogCategorySpecial.CatalogSpecialHeader(),
                arrayListOf(mockk(relaxed = true)),
                ""
            )
        )
        coEvery {
            catalogMostViralUseCase.getCatalogProductsData(any(), any(), any(), any(), any(), any(), any())
        } coAnswers {
            firstArg<(CatalogListResponse, Int) -> Unit>().invoke(response, 1)
        }
        viewModel.getCatalogMostViralData(
            CatalogLibraryConstant.SOURCE_CATEGORY_BRAND_LANDING_PAGE,
            SORT_TYPE_TOP_FIVE,
            10
        )
        assert(viewModel.catalogLandingPageLiveDataResponse.value is Success)
    }

    @Test
    fun `Get Most Viral success but condition fail`() {
        val response = CatalogListResponse(CatalogListResponse.CatalogGetList())
        coEvery {
            catalogMostViralUseCase.getCatalogProductsData(any(), any(), any(), any(), any(), any(), any())
        } coAnswers {
            firstArg<(CatalogListResponse, Int) -> Unit>().invoke(response, 1)
        }
        viewModel.getCatalogMostViralData(
            CatalogLibraryConstant.SOURCE_CATEGORY_BRAND_LANDING_PAGE,
            SORT_TYPE_TOP_FIVE,
            10
        )
        Assert.assertEquals(
            (viewModel.catalogLandingPageLiveDataResponse.value as Fail).throwable.localizedMessage,
            "No Catalog Landing Page Data"
        )
    }

    @Test
    fun `Get Most Viral fetch failed throws exception`() {
        coEvery {
            catalogMostViralUseCase.getCatalogProductsData(any(), any(), any(), any(), any(), any(), any())
        } coAnswers {
            secondArg<(Throwable) -> Unit>().invoke(mockThrowable)
        }

        viewModel.getCatalogMostViralData(
            CatalogLibraryConstant.SOURCE_CATEGORY_BRAND_LANDING_PAGE,
            SORT_TYPE_TOP_FIVE,
            10
        )
        assert(
            (viewModel.catalogLandingPageLiveDataResponse.value is Fail)
        )
    }
}
