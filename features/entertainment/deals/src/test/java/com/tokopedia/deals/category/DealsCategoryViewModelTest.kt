package com.tokopedia.deals.category

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.gson.Gson
import com.tokopedia.deals.category.domain.GetChipsCategoryUseCase
import com.tokopedia.deals.category.ui.dataview.ProductListDataView
import com.tokopedia.deals.category.ui.viewmodel.DealCategoryViewModel
import com.tokopedia.deals.category.utils.MapperCategoryLayout
import com.tokopedia.deals.common.model.response.SearchData
import com.tokopedia.deals.common.ui.dataview.DealsBaseItemDataView
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.deals.search.model.response.CuratedData
import io.mockk.coEvery
import io.mockk.mockk
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import com.tokopedia.deals.DealsJsonMapper
import com.tokopedia.deals.common.domain.DealsSearchUseCase
import com.tokopedia.deals.common.ui.dataview.DealsBrandsDataView
import com.tokopedia.deals.location_picker.model.response.Location
import junit.framework.Assert.assertEquals

@RunWith(JUnit4::class)
class DealsCategoryViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()
    private val dispatcher = CoroutineTestDispatchersProvider

    private val getChipsCategoryUseCase: GetChipsCategoryUseCase = mockk()
    private val dealsSearchUseCase: DealsSearchUseCase = mockk()
    private lateinit var viewModel: DealCategoryViewModel
    val mContextMock = mockk<Context>(relaxed = true)

    lateinit var mapper : MapperCategoryLayout

    @Before
    fun setup() {
        mapper  = MapperCategoryLayout(mContextMock)
        viewModel = DealCategoryViewModel(
                mapper,
                getChipsCategoryUseCase,
                dealsSearchUseCase,
                dispatcher
        )
    }

    @Test
    fun getChipFilter_fetchFailed_shouldShowErrorMessage(){
        //given
        val mockThrowable = Throwable("Fetch chip failed")
        coEvery {
            getChipsCategoryUseCase.execute(any(), any())
        } coAnswers {
            secondArg<(Throwable) -> Unit>().invoke(mockThrowable)
        }
        //when
        viewModel.getChipsData()
        //then
        assert(viewModel.errorMessage.value?.message == "Fetch chip failed")
    }

    @Test
    fun getChipFilter_fetchSuccess_shouldShowSuccessMessage(){
        //given
        val mockCuratedData = Gson().fromJson(DealsJsonMapper.getJson("curateddata.json"), CuratedData::class.java)
        val mockFilterChip = mapper.mapCategoryToChips(mockCuratedData.eventChildCategory.categories)
        coEvery {
            getChipsCategoryUseCase.execute(any(), any())
        } coAnswers {
            firstArg<(CuratedData) -> Unit>().invoke(mockCuratedData)
        }
        //when
        viewModel.getChipsData()
        //then
        val curatedData = viewModel.observableChips.value
        assertEquals(curatedData, mockFilterChip)
    }

    @Test
    fun getCategoryBrandData_fetchChipFailed_shouldShowErrorMessage() {
        // given
        val mockThrowable = Throwable("Fetch chip failed")
        coEvery {
            dealsSearchUseCase.getDealsSearchResult(
                    any(), any(), any(), any(), any(), any(), any(), any(), any()
            )
        } coAnswers {
            secondArg<(Throwable) -> Unit>().invoke(mockThrowable)
        }

        // when
        viewModel.getCategoryBrandData("", "", "", 1, true)

        // then
        assert(viewModel.errorMessage.value?.message == "Fetch chip failed")
    }

    @Test
    fun getCategoryBrandData_fetchBrandProductCategoryFailed_shouldShowErrorMessage() {
        // given
        val mockThrowable = Throwable("Fetch brand product category failed")
        coEvery {
            dealsSearchUseCase.getDealsSearchResult(
                    any(), any(), any(), any(), any(), any(), any(), any(), any()
            )
        } coAnswers {
            secondArg<(Throwable) -> Unit>().invoke(mockThrowable)
        }

        // when
        viewModel.getCategoryBrandData("", "", "", 1, true)

        // then
        assert(viewModel.errorMessage.value?.message == "Fetch brand product category failed")
    }

    @Test
    fun getCategoryBrandData_fetchSuccessOnPageOne_dealsCategoryShouldContainsData() {
        val mockEvent = Gson().fromJson(DealsJsonMapper.getJson("brandproduct.json"), SearchData::class.java)
        val mockResult = mapper.mapCategoryLayout(mockEvent, 1, "")
        // given
        coEvery {
            dealsSearchUseCase.getDealsSearchResult(
                    any(), any(), any(), any(), any(), any(), any(), any(), any()
            )
        } coAnswers {
            firstArg<(SearchData) -> Unit>().invoke(mockEvent)
        }
        // when
        viewModel.getCategoryBrandData("", "", "", 1, true)

        // then
        assertEquals(viewModel.observableDealsCategoryLayout.value, mockResult)
    }

    @Test
    fun getCategoryBrandData_fetchSuccessOnPageGreaterThanOne_productsShouldContainsData() {
        val mockEvent = Gson().fromJson(DealsJsonMapper.getJson("brandproduct.json"), SearchData::class.java)
        val mockResult = mapper.mapProducttoLayout(mockEvent, 2)
        // given
        coEvery {
            dealsSearchUseCase.getDealsSearchResult(
                    any(), any(), any(), any(), any(), any(), any(), any(), any()
            )
        } coAnswers {
            firstArg<(SearchData) -> Unit>().invoke(mockEvent)
        }

        // when
        viewModel.getCategoryBrandData("", "", "", 2, true)

        // then
        assertEquals(viewModel.observableProducts.value, mockResult)
    }

    @Test
    fun getShimmeringData_fetchSuccess_shimmeringShouldShimmering(){
        //given
        val layouts = mutableListOf<DealsBaseItemDataView>()
        layouts.add(DealCategoryViewModel.BRAND_POPULAR, DealsBrandsDataView(oneRow = true))
        layouts.add(DealCategoryViewModel.PRODUCT_LIST, ProductListDataView())
        //when
        viewModel.shimmeringCategory()
        //then
        assertEquals(viewModel.observableDealsCategoryLayout.value,layouts)
    }

    @Test
    fun getCategoryBrandData_fetchSuccessUpdateChips_dealsCategoryShouldContainsData() {
        val mockEvent = Gson().fromJson(DealsJsonMapper.getJson("brandproduct.json"), SearchData::class.java)
        val mockResult = mapper.mapCategoryLayout(mockEvent, 1, "")
        // given
        coEvery {
            dealsSearchUseCase.getDealsSearchResult(
                    any(), any(), any(), any(), any(), any(), any(), any(), any()
            )
        } coAnswers {
            firstArg<(SearchData) -> Unit>().invoke(mockEvent)
        }
        // when
        viewModel.updateChips(Location(), "12", true)

        // then
        assertEquals(viewModel.observableDealsCategoryLayout.value, mockResult)
    }
}