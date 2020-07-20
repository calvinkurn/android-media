package com.tokopedia.deals.category

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.deals.category.domain.GetBrandProductCategoryUseCase
import com.tokopedia.deals.category.domain.GetChipsCategoryUseCase
import com.tokopedia.deals.category.ui.dataview.ProductListDataView
import com.tokopedia.deals.category.ui.viewmodel.DealCategoryViewModel
import com.tokopedia.deals.category.utils.MapperCategoryLayout
import com.tokopedia.deals.common.model.response.SearchData
import com.tokopedia.deals.common.ui.dataview.DealsBaseItemDataView
import com.tokopedia.deals.common.utils.DealsLocationUtils
import com.tokopedia.deals.common.utils.DealsTestDispatcherProvider
import com.tokopedia.deals.location_picker.model.response.Location
import com.tokopedia.deals.search.model.response.CuratedData
import io.mockk.coEvery
import io.mockk.mockk
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class DealsCategoryViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()
    private val dispatcher = DealsTestDispatcherProvider()

    private val getChipsCategoryUseCase: GetChipsCategoryUseCase = mockk()
    private val getBrandProductCategoryUseCase: GetBrandProductCategoryUseCase = mockk()
    private val mapper: MapperCategoryLayout = mockk()
    private lateinit var viewModel: DealCategoryViewModel

    @Before
    fun setup() {
        viewModel = DealCategoryViewModel(
            mapper,
            getChipsCategoryUseCase,
            getBrandProductCategoryUseCase,
            dispatcher
        )
    }

    @Test
    fun getCategoryBrandData_fetchChipFailed_shouldShowErrorMessage() {
        // given
        coEvery { getChipsCategoryUseCase.executeOnBackground() } coAnswers { throw Exception("Fetch chip failed") }
        coEvery { getBrandProductCategoryUseCase.useParams(any()) } returns mockk()
        coEvery { getBrandProductCategoryUseCase.executeOnBackground() } returns SearchData()

        // when
        viewModel.getCategoryBrandData("", "", "", 1, true)

        // then
        assert(viewModel.errorMessage.value?.message == "Fetch chip failed")
    }

    @Test
    fun getCategoryBrandData_fetchBrandProductCategoryFailed_shouldShowErrorMessage() {
        // given
        coEvery { getChipsCategoryUseCase.executeOnBackground() } returns CuratedData()
        coEvery { getBrandProductCategoryUseCase.useParams(any()) } returns mockk()
        coEvery { getBrandProductCategoryUseCase.executeOnBackground() } coAnswers {
            throw Exception("Fetch brand product category failed")
        }

        // when
        viewModel.getCategoryBrandData("", "", "", 1, true)

        // then
        assert(viewModel.errorMessage.value?.message == "Fetch brand product category failed")
    }

    @Test
    fun getCategoryBrandData_fetchSuccessOnPageOne_dealsCategoryShouldContainsData() {
        // given
        coEvery { getChipsCategoryUseCase.executeOnBackground() } returns CuratedData()
        coEvery { getBrandProductCategoryUseCase.useParams(any()) } returns mockk()
        coEvery { getBrandProductCategoryUseCase.executeOnBackground() } returns SearchData()
        coEvery { mapper.mapCategoryLayout(any(), any()) } returns listOf(DealsBaseItemDataView())

        // when
        viewModel.getCategoryBrandData("", "", "", 1, true)

        // then
        assert(viewModel.observableDealsCategoryLayout.value.isNullOrEmpty().not())
    }

    @Test
    fun getCategoryBrandData_fetchSuccessOnPageGreaterThanOne_productsShouldContainsData() {
        // given
        coEvery { getChipsCategoryUseCase.executeOnBackground() } returns CuratedData()
        coEvery { getBrandProductCategoryUseCase.useParams(any()) } returns mockk()
        coEvery { getBrandProductCategoryUseCase.executeOnBackground() } returns SearchData()
        coEvery { mapper.mapProducttoLayout(any()) } returns ProductListDataView()


        // when
        viewModel.getCategoryBrandData("", "", "", 10, true)

        // then
        assert(viewModel.observableProducts.value.isNullOrEmpty().not())
    }
}