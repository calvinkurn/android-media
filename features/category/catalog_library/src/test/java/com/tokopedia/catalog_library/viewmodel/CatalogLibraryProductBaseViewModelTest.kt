package com.tokopedia.catalog_library.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.catalog_library.model.raw.CatalogListResponse
import com.tokopedia.catalog_library.model.raw.CatalogSpecialResponse
import com.tokopedia.catalog_library.usecase.CatalogProductsUseCase
import com.tokopedia.catalog_library.util.CatalogLibraryConstant
import com.tokopedia.catalog_library.util.CatalogLibraryConstant.SORT_TYPE_CATALOG
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
class CatalogLibraryProductBaseViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val catalogLibraryProductUseCase = mockk<CatalogProductsUseCase>(relaxed = true)

    private lateinit var viewModel: CatalogProductsBaseViewModel

    private val fetchFailedErrorMessage = "Fetch Failed"
    private val mockThrowable = Throwable(message = fetchFailedErrorMessage)

    @Before
    fun setUp() {
        viewModel = CatalogProductsBaseViewModel(
            catalogLibraryProductUseCase
        )
    }

    @Test
    fun `Get Product Success`() {
        val response = CatalogListResponse(
            CatalogListResponse.CatalogGetList(
                CatalogSpecialResponse.CatalogCategorySpecial.CatalogSpecialHeader(),
                arrayListOf(mockk(relaxed = true)),
                ""
            )
        )
        coEvery {
            catalogLibraryProductUseCase.getCatalogProductsData(any(), any(), any(), any(), any(), any(), any())
        } coAnswers {
            firstArg<(CatalogListResponse, Int) -> Unit>().invoke(response, 1)
        }
        viewModel.getCatalogListData(
            CatalogLibraryConstant.SOURCE_CATEGORY_BRAND_LANDING_PAGE,
            "",
            SORT_TYPE_CATALOG,
            10,
            1,
            ""
        )
        assert(viewModel.catalogProductsLiveDataResponse.value is Success)
    }

    @Test
    fun `Get product success but condition fail`() {
        val response = CatalogListResponse(CatalogListResponse.CatalogGetList())
        coEvery {
            catalogLibraryProductUseCase.getCatalogProductsData(any(), any(), any(), any(), any(), any(), any())
        } coAnswers {
            firstArg<(CatalogListResponse, Int) -> Unit>().invoke(response, 1)
        }
        viewModel.getCatalogListData(
            CatalogLibraryConstant.SOURCE_CATEGORY_BRAND_LANDING_PAGE,
            "",
            SORT_TYPE_CATALOG,
            10,
            1,
            ""
        )
        Assert.assertEquals(
            (viewModel.catalogProductsLiveDataResponse.value as Fail).throwable.localizedMessage,
            "No Catalog List Response Data"
        )
    }

    @Test
    fun `Get product fetch failed throws exception`() {
        coEvery {
            catalogLibraryProductUseCase.getCatalogProductsData(any(), any(), any(), any(), any(), any(), any())
        } coAnswers {
            secondArg<(Throwable) -> Unit>().invoke(mockThrowable)
        }

        viewModel.getCatalogListData(
            CatalogLibraryConstant.SOURCE_CATEGORY_BRAND_LANDING_PAGE,
            "",
            SORT_TYPE_CATALOG,
            10,
            1,
            ""
        )
        Assert.assertEquals(
            (viewModel.catalogProductsLiveDataResponse.value as Fail).throwable,
            mockThrowable
        )
    }
}
