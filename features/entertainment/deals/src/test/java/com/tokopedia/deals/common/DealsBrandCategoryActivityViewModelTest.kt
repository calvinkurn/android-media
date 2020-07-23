package com.tokopedia.deals.common

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.deals.common.domain.GetDealsBrandCategoryActivityUseCase
import com.tokopedia.deals.common.ui.viewmodel.DealsBrandCategoryActivityViewModel
import com.tokopedia.deals.common.utils.DealsTestDispatcherProvider
import com.tokopedia.deals.search.model.response.Category
import com.tokopedia.deals.search.model.response.CuratedData
import com.tokopedia.deals.search.model.response.EventChildCategory
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import io.mockk.coEvery
import io.mockk.mockk
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class DealsBrandCategoryActivityViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()
    private val dispatcher = DealsTestDispatcherProvider()

    private val useCase: GetDealsBrandCategoryActivityUseCase = mockk()
    private lateinit var viewModel: DealsBrandCategoryActivityViewModel

    @Before
    fun setup() {
        viewModel = DealsBrandCategoryActivityViewModel(useCase, dispatcher)
    }

    @Test
    fun getCategoryCombindedData_fetchFailed_shouldShowErrorMessage() {
        // given
        coEvery { useCase.getChildCategoryResult() } returns Fail(Throwable("Error failed"))

        // when
        viewModel.getCategoryCombindedData()

        // then
        val errorMessage = viewModel.errorMessage.value as Throwable
        assert(errorMessage.message == "Error failed")
    }

    @Test
    fun getCategoryCombindedData_fetchSuccess_shouldShowCuratedData() {
        // given
        coEvery { useCase.getChildCategoryResult() } returns Success(
            CuratedData(
                eventChildCategory = EventChildCategory(
                    categories = listOf(
                        Category(), Category(), Category()
                    )
                )
            )
        )

        // when
        viewModel.getCategoryCombindedData()

        // then
        val curatedData = viewModel.curatedData.value as CuratedData
        assert(curatedData.eventChildCategory.categories.size == 3)
    }
}