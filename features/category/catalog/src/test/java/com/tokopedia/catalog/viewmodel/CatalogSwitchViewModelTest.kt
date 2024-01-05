package com.tokopedia.catalog.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.catalog.domain.CatalogComparisonProductUseCase
import com.tokopedia.catalog.ui.model.CatalogComparisonProductsUiModel
import com.tokopedia.catalog.ui.viewmodel.CatalogSwitchComparisonViewModel
import com.tokopedia.catalogcommon.uimodel.ComparisonUiModel
import com.tokopedia.oldcatalog.model.raw.CatalogComparisonProductsResponse
import com.tokopedia.oldcatalog.usecase.detail.CatalogDetailUseCase
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.unit.test.ext.getOrAwaitValue
import io.mockk.MockKAnnotations
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.spyk
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.jupiter.api.AfterEach

class CatalogSwitchViewModelTest {

    // Initialization scope
    @get:Rule
    val instantTaskExcecutorRule = InstantTaskExecutorRule()

    @RelaxedMockK
    lateinit var catalogDetailUseCase: CatalogDetailUseCase

    @RelaxedMockK
    lateinit var catalogComparisonProductUseCase: CatalogComparisonProductUseCase

    private val viewModel by lazy {
        spyk(
            CatalogSwitchComparisonViewModel(
                CoroutineTestDispatchersProvider,
                catalogDetailUseCase,
                catalogComparisonProductUseCase
            )
        )
    }

    @Before
    @Throws(Exception::class)
    fun setup() {
        MockKAnnotations.init(this)
    }

    @AfterEach
    fun tearDown() {
        clearAllMocks()
    }

    @Test
    fun `When getComparisonProducts is Success, Then should invoke CatalogComparisonProductsUiModel`() {
        val mockResponse = CatalogComparisonProductsResponse(
            CatalogComparisonProductsResponse.CatalogComparisonList(
                header = CatalogComparisonProductsResponse.CatalogComparisonList.Header(
                    0,
                    ""
                )
            )
        )
        coEvery {
            catalogComparisonProductUseCase.getCatalogComparisonProducts(
                "",
                "",
                "",
                "",
                "",
                ""
            )
        } returns mockResponse

        viewModel.getComparisonProducts(
            "",
            "",
            "",
            10,
            0,
            ""
        )
        val result = viewModel.catalogListingUiModel.getOrAwaitValue()
        assert(result is CatalogComparisonProductsUiModel)
    }

    @Test
    fun `When getComparisonProducts is Error, Then should invoke Throwanle`() {
        coEvery {
            catalogComparisonProductUseCase.getCatalogComparisonProducts(
                any(),
                any(),
                any(),
                any(),
                any(),
                any()
            )
        } throws Throwable("Error")

        viewModel.getComparisonProducts(
            "",
            "",
            "",
            10,
            0,
            ""
        )
        assert(viewModel.errorsToasterGetCatalogListing.getOrAwaitValue().message == "Error")
    }

    @Test
    fun `When getInitComparisonSwitch is Success, Then should invoke data catalog item selection and catalog listing`() {
        val mockResponseCatalogListing = CatalogComparisonProductsResponse(
            CatalogComparisonProductsResponse.CatalogComparisonList(
                header = CatalogComparisonProductsResponse.CatalogComparisonList.Header(
                    0,
                    ""
                )
            )
        )
        coEvery {
            catalogComparisonProductUseCase.getCatalogComparisonProducts(
                "",
                "",
                "",
                "",
                "",
                ""
            )
        } returns mockResponseCatalogListing

        val mockResponseCatalogDetail = ComparisonUiModel()

        coEvery {
            catalogDetailUseCase.getCatalogDetailV4Comparison(
                any(),
                any()
            )
        } returns mockResponseCatalogDetail

        viewModel.loadAllDataInOnePage(
            "",
            "",
            "",
            10,
            listOf(),
            ""
        )
        val resultCatalogListing = viewModel.catalogListingUiModel.getOrAwaitValue()
        val resultCatalogSelection = viewModel.comparisonUiModel.getOrAwaitValue()
        assert(resultCatalogListing is CatalogComparisonProductsUiModel)
        assert(resultCatalogSelection is CatalogComparisonProductsUiModel)
    }

    @Test
    fun `When getInitComparisonSwitch is Success, Then should invoke data null catalog item selection and catalog listing`() {
        val mockResponseCatalogListing = CatalogComparisonProductsResponse(
            CatalogComparisonProductsResponse.CatalogComparisonList(
                header = CatalogComparisonProductsResponse.CatalogComparisonList.Header(
                    0,
                    ""
                )
            )
        )
        coEvery {
            catalogComparisonProductUseCase.getCatalogComparisonProducts(
                "",
                "",
                "",
                "",
                "",
                ""
            )
        } returns mockResponseCatalogListing

//        val mockResponseCatalogDetail = ComparisonUiModel()

        coEvery {
            catalogDetailUseCase.getCatalogDetailV4Comparison(
                any(),
                any()
            )
        } returns null

        viewModel.loadAllDataInOnePage(
            "",
            "",
            "",
            10,
            listOf(),
            ""
        )
        val resultCatalogListing = viewModel.catalogListingUiModel.getOrAwaitValue()
        val resultCatalogSelection = viewModel.comparisonUiModel.getOrAwaitValue()
        assert(resultCatalogListing is CatalogComparisonProductsUiModel)
        assert(resultCatalogSelection == null)
    }

    @Test
    fun `When getInitComparisonSwitch is Two usecase Error, Then should invoke Throwable`() {
        coEvery {
            catalogComparisonProductUseCase.getCatalogComparisonProducts(
                any(),
                any(),
                any(),
                any(),
                any(),
                any()
            )
        } throws Throwable("Error")

        coEvery {
            catalogDetailUseCase.getCatalogDetailV4Comparison(
                any(),
                any()
            )
        } throws Throwable("Error")

        viewModel.loadAllDataInOnePage(
            "",
            "",
            "",
            10,
            listOf(),
            ""
        )
        assert(viewModel.errorsToasterGetInitComparison.getOrAwaitValue().message == "Error")
    }

    @Test
    fun `When getCatalogComparisonProducts Error, Then should invoke Throwable`() {
        coEvery {
            catalogComparisonProductUseCase.getCatalogComparisonProducts(
                any(),
                any(),
                any(),
                any(),
                any(),
                any()
            )
        } throws Throwable("Error")

        val mockResponseCatalogDetail = ComparisonUiModel()

        coEvery {
            catalogDetailUseCase.getCatalogDetailV4Comparison(
                any(),
                any()
            )
        } returns mockResponseCatalogDetail

        viewModel.loadAllDataInOnePage(
            "",
            "",
            "",
            10,
            listOf(),
            ""
        )
        assert(viewModel.errorsToasterGetInitComparison.getOrAwaitValue().message == "Error")
    }

    @Test
    fun `When getCatalogDetailV4Comparison Error, Then should invoke Throwable`() {
        val mockResponseCatalogListing = CatalogComparisonProductsResponse(
            CatalogComparisonProductsResponse.CatalogComparisonList(
                header = CatalogComparisonProductsResponse.CatalogComparisonList.Header(
                    0,
                    ""
                )
            )
        )

        coEvery {
            catalogComparisonProductUseCase.getCatalogComparisonProducts(
                any(),
                any(),
                any(),
                any(),
                any(),
                any()
            )
        } returns mockResponseCatalogListing

        coEvery {
            catalogDetailUseCase.getCatalogDetailV4Comparison(
                any(),
                any()
            )
        } throws Throwable("Error")

        viewModel.loadAllDataInOnePage(
            "",
            "",
            "",
            10,
            listOf(),
            ""
        )
        assert(viewModel.errorsToasterGetInitComparison.getOrAwaitValue().message == "Error")
    }
}
