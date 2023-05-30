package com.tokopedia.catalog_library.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.catalog_library.model.raw.CatalogBrandsPopularResponse
import com.tokopedia.catalog_library.model.raw.CatalogRelevantResponse
import com.tokopedia.catalog_library.model.raw.CatalogSpecialResponse
import com.tokopedia.catalog_library.usecase.CatalogBrandsPopularUseCase
import com.tokopedia.catalog_library.usecase.CatalogRelevantUseCase
import com.tokopedia.catalog_library.usecase.CatalogSpecialUseCase
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
class CatalogLibraryHomePageViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val catalogSpecialUseCase = mockk<CatalogSpecialUseCase>(relaxed = true)
    private val catalogRelevantUseCase = mockk<CatalogRelevantUseCase>(relaxed = true)
    private val catalogBrandsPopularUseCase = mockk<CatalogBrandsPopularUseCase>(relaxed = true)

    private lateinit var viewModel: CatalogHomepageViewModel
    private val fetchFailedErrorMessage = "Fetch Failed"
    private val mockThrowable = Throwable(message = fetchFailedErrorMessage)

    @Before
    fun setUp() {
        viewModel = CatalogHomepageViewModel(
            catalogSpecialUseCase,
            catalogRelevantUseCase,
            catalogBrandsPopularUseCase
        )
    }

    @Test
    fun `Get Special List Response Success`() {
        val response = CatalogSpecialResponse(
            CatalogSpecialResponse.CatalogCategorySpecial(
                CatalogSpecialResponse.CatalogCategorySpecial.CatalogSpecialHeader(),
                arrayListOf(mockk(relaxed = true))
            )
        )
        coEvery {
            catalogSpecialUseCase.getSpecialData(any(), any())
        } coAnswers {
            firstArg<(CatalogSpecialResponse) -> Unit>().invoke(response)
        }
        viewModel.getSpecialData()
        assert(viewModel.catalogLibraryLiveDataResponse.value is Success)
    }

    @Test
    fun `Get Special List success but condition fail`() {
        val response = CatalogSpecialResponse()
        coEvery {
            catalogSpecialUseCase.getSpecialData(any(), any())
        } coAnswers {
            firstArg<(CatalogSpecialResponse) -> Unit>().invoke(response)
        }
        viewModel.getSpecialData()
        Assert.assertEquals(
            (viewModel.catalogLibraryLiveDataResponse.value as Fail).throwable.localizedMessage,
            "No Special Response Data"
        )
    }

    @Test
    fun `Get Special List fetch failed throws exception`() {
        coEvery {
            catalogSpecialUseCase.getSpecialData(any(), any())
        } coAnswers {
            secondArg<(Throwable) -> Unit>().invoke(mockThrowable)
        }
        viewModel.getSpecialData()
        Assert.assertEquals(
            (viewModel.catalogLibraryLiveDataResponse.value as Fail).throwable,
            mockThrowable
        )
    }

    @Test
    fun `Get Relevant List Response Success`() {
        val response = CatalogRelevantResponse(
            CatalogRelevantResponse.CatalogGetRelevant(arrayListOf(mockk(relaxed = true)))
        )
        coEvery {
            catalogRelevantUseCase.getRelevantData(any(), any())
        } coAnswers {
            firstArg<(CatalogRelevantResponse) -> Unit>().invoke(response)
        }
        viewModel.getRelevantData()
        assert(viewModel.catalogLibraryLiveDataResponse.value is Success)
    }

    @Test
    fun `Get Relevant List success but condition fail`() {
        val response = CatalogRelevantResponse()
        coEvery {
            catalogRelevantUseCase.getRelevantData(any(), any())
        } coAnswers {
            firstArg<(CatalogRelevantResponse) -> Unit>().invoke(response)
        }
        viewModel.getRelevantData()
        Assert.assertEquals(
            (viewModel.catalogLibraryLiveDataResponse.value as Fail).throwable.localizedMessage,
            "No Relevant Response Data"
        )
    }

    @Test
    fun `Get Relevant List fetch failed throws exception`() {
        coEvery {
            catalogRelevantUseCase.getRelevantData(any(), any())
        } coAnswers {
            secondArg<(Throwable) -> Unit>().invoke(mockThrowable)
        }
        viewModel.getRelevantData()
        Assert.assertEquals(
            (viewModel.catalogLibraryLiveDataResponse.value as Fail).throwable,
            mockThrowable
        )
    }

    @Test
    fun `Get Popular Brands List Response Success`() {
        val response = CatalogBrandsPopularResponse(
            CatalogBrandsPopularResponse.CatalogGetBrandPopular(
                CatalogBrandsPopularResponse.CatalogGetBrandPopular.CatalogBrandsPopularHeader(),
                arrayListOf(mockk(relaxed = true))
            )
        )
        coEvery {
            catalogBrandsPopularUseCase.getBrandPopular(any(), any())
        } coAnswers {
            firstArg<(CatalogBrandsPopularResponse) -> Unit>().invoke(response)
        }
        viewModel.getPopularBrands()
        assert(viewModel.catalogLibraryLiveDataResponse.value is Success)
    }

    @Test
    fun `Get Popular Brands List success but condition fail`() {
        val response = CatalogBrandsPopularResponse()
        coEvery {
            catalogBrandsPopularUseCase.getBrandPopular(any(), any())
        } coAnswers {
            firstArg<(CatalogBrandsPopularResponse) -> Unit>().invoke(response)
        }
        viewModel.getPopularBrands()
        Assert.assertEquals(
            (viewModel.catalogLibraryLiveDataResponse.value as Fail).throwable.localizedMessage,
            "No Brands Response Data"
        )
    }

    @Test
    fun `Get Popular Brands fetch failed and throws exception`() {
        coEvery {
            catalogBrandsPopularUseCase.getBrandPopular(any(), any())
        } coAnswers {
            secondArg<(Throwable) -> Unit>().invoke(mockThrowable)
        }
        viewModel.getPopularBrands()
        Assert.assertEquals(
            (viewModel.catalogLibraryLiveDataResponse.value as Fail).throwable,
            mockThrowable
        )
    }
}
