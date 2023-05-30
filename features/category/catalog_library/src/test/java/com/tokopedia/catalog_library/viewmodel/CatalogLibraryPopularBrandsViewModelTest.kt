package com.tokopedia.catalog_library.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.catalog_library.model.raw.CatalogBrandsPopularResponse
import com.tokopedia.catalog_library.usecase.CatalogBrandsPopularWithCatalogsUseCase
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
class CatalogLibraryPopularBrandsViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val catalogBrandsPopularWithCatalogsUseCase = mockk<CatalogBrandsPopularWithCatalogsUseCase>(relaxed = true)

    private lateinit var viewModel: CatalogPopularBrandsViewModel

    private val fetchFailedErrorMessage = "Fetch Failed"
    private val mockThrowable = Throwable(message = fetchFailedErrorMessage)

    @Before
    fun setUp() {
        viewModel = CatalogPopularBrandsViewModel(
            catalogBrandsPopularWithCatalogsUseCase
        )
    }

    @Test
    fun `Get Brands with Catalog Response Success`() {
        val response = CatalogBrandsPopularResponse(
            CatalogBrandsPopularResponse.CatalogGetBrandPopular(
                CatalogBrandsPopularResponse.CatalogGetBrandPopular.CatalogBrandsPopularHeader(),
                arrayListOf(mockk(relaxed = true))
            )
        )
        coEvery {
            catalogBrandsPopularWithCatalogsUseCase.getBrandPopularWithCatalogs(any(), any())
        } coAnswers {
            firstArg<(CatalogBrandsPopularResponse) -> Unit>().invoke(response)
        }
        viewModel.getBrandsWithCatalogs()
        assert(viewModel.brandsWithCatalogsLiveData.value is Success)
    }

    @Test
    fun `Get Brands with Catalog success but condition fail`() {
        val response = CatalogBrandsPopularResponse(mockk(relaxed = true))
        coEvery {
            catalogBrandsPopularWithCatalogsUseCase.getBrandPopularWithCatalogs(any(), any())
        } coAnswers {
            firstArg<(CatalogBrandsPopularResponse) -> Unit>().invoke(response)
        }
        viewModel.getBrandsWithCatalogs()
        Assert.assertEquals(
            (viewModel.brandsWithCatalogsLiveData.value as Fail).throwable.localizedMessage,
            "No Brands Response Data"
        )
    }

    @Test
    fun `Get Brands with Catalog fetch failed throws exception`() {
        coEvery {
            catalogBrandsPopularWithCatalogsUseCase.getBrandPopularWithCatalogs(any(), any())
        } coAnswers {
            secondArg<(Throwable) -> Unit>().invoke(mockThrowable)
        }

        viewModel.getBrandsWithCatalogs()
        Assert.assertEquals(
            (viewModel.brandsWithCatalogsLiveData.value as Fail).throwable,
            mockThrowable
        )
    }
}
