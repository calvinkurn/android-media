package com.tokopedia.catalog_library.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.catalog_library.model.raw.CatalogLibraryResponse
import com.tokopedia.catalog_library.usecase.CatalogLibraryBrandCategoryUseCase
import com.tokopedia.catalog_library.usecase.CatalogLibraryUseCase
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
class CatalogLibraryLihatSemuaViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val catalogLibraryUseCase = mockk<CatalogLibraryUseCase>(relaxed = true)
    private val catalogLibraryBrandCategoryUseCase = mockk<CatalogLibraryBrandCategoryUseCase>(relaxed = true)

    private lateinit var viewModel: CatalogLihatSemuaPageViewModel

    private val fetchFailedErrorMessage = "Fetch Failed"
    private val mockThrowable = Throwable(message = fetchFailedErrorMessage)

    @Before
    fun setUp() {
        viewModel = CatalogLihatSemuaPageViewModel(
            catalogLibraryUseCase,
            catalogLibraryBrandCategoryUseCase
        )
    }

    @Test
    fun `Get Category List Response Success`() {
        val response = CatalogLibraryResponse(
            CatalogLibraryResponse.CategoryListLibraryPage(
                "",
                "",
                arrayListOf(mockk(relaxed = true))
            )
        )
        coEvery {
            catalogLibraryUseCase.getLibraryData(any(), any(), "")
        } coAnswers {
            firstArg<(CatalogLibraryResponse) -> Unit>().invoke(response)
        }
        viewModel.getLihatSemuaPageData("")
        assert(viewModel.catalogLihatLiveDataResponse.value is Success)
    }

    @Test
    fun `Get Category List success but condition fail`() {
        val response = CatalogLibraryResponse()
        coEvery {
            catalogLibraryUseCase.getLibraryData(any(), any(), "")
        } coAnswers {
            firstArg<(CatalogLibraryResponse) -> Unit>().invoke(response)
        }
        viewModel.getLihatSemuaPageData("")
        Assert.assertEquals(
            (viewModel.catalogLihatLiveDataResponse.value as Fail).throwable.localizedMessage,
            "No Lihat Semua Data"
        )
    }

    @Test
    fun `Get Category List fetch failed throws exception`() {
        coEvery {
            catalogLibraryUseCase.getLibraryData(any(), any(), "")
        } coAnswers {
            secondArg<(Throwable) -> Unit>().invoke(mockThrowable)
        }
        viewModel.getLihatSemuaPageData("")
        Assert.assertEquals(
            (viewModel.catalogLihatLiveDataResponse.value as Fail).throwable,
            mockThrowable
        )
    }

    @Test
    fun `Get Brand List Response Success`() {
        val response = CatalogLibraryResponse(
            CatalogLibraryResponse.CategoryListLibraryPage(
                "BrandName",
                "",
                arrayListOf(mockk(relaxed = true))
            )
        )
        coEvery {
            catalogLibraryBrandCategoryUseCase.getBrandCategories(any(), any(), "")
        } coAnswers {
            firstArg<(CatalogLibraryResponse) -> Unit>().invoke(response)
        }
        viewModel.getLihatSemuaByBrandData("", "")
        assert(viewModel.catalogLihatLiveDataResponse.value is Success)
        viewModel.brandNameLiveData.value?.isNotBlank()?.let { assert(it) }
    }

    @Test
    fun `Get Brand List Tab Response Success`() {
        val response = CatalogLibraryResponse(
            CatalogLibraryResponse.CategoryListLibraryPage(
                "BrandName",
                "",
                arrayListOf(mockk(relaxed = true))
            )
        )
        coEvery {
            catalogLibraryBrandCategoryUseCase.getBrandCategories(any(), any(), "")
        } coAnswers {
            firstArg<(CatalogLibraryResponse) -> Unit>().invoke(response)
        }
        viewModel.getLihatSemuaByBrandData("", "", false)
        assert(viewModel.catalogLihatLiveDataResponse.value is Success)
        viewModel.brandNameLiveData.value?.isNotBlank()?.let { assert(it) }
    }

    @Test
    fun `Get Brand List success but condition fail`() {
        val response = CatalogLibraryResponse()
        coEvery {
            catalogLibraryBrandCategoryUseCase.getBrandCategories(any(), any(), "")
        } coAnswers {
            firstArg<(CatalogLibraryResponse) -> Unit>().invoke(response)
        }
        viewModel.getLihatSemuaByBrandData("", "")
        Assert.assertEquals(
            (viewModel.catalogLihatLiveDataResponse.value as Fail).throwable.localizedMessage,
            "No Lihat Semua Data"
        )
    }

    @Test
    fun `Get Brand List fetch failed throws exception`() {
        coEvery {
            catalogLibraryBrandCategoryUseCase.getBrandCategories(any(), any(), "")
        } coAnswers {
            secondArg<(Throwable) -> Unit>().invoke(mockThrowable)
        }
        viewModel.getLihatSemuaByBrandData("", "")
        Assert.assertEquals(
            (viewModel.catalogLihatLiveDataResponse.value as Fail).throwable,
            mockThrowable
        )
    }
}
