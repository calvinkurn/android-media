package com.tokopedia.deals.common

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.gson.Gson
import com.tokopedia.deals.category.domain.GetChipsCategoryUseCase
import com.tokopedia.deals.common.ui.viewmodel.DealsBrandCategoryActivityViewModel
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.deals.search.model.response.CuratedData
import io.mockk.coEvery
import io.mockk.mockk
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import com.tokopedia.deals.DealsJsonMapper
import org.junit.Assert.assertEquals

class DealsBrandCategoryActivityViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()
    private val dispatcher = CoroutineTestDispatchersProvider

    private val useCase: GetChipsCategoryUseCase = mockk()
    private lateinit var viewModel: DealsBrandCategoryActivityViewModel

    @Before
    fun setup() {
        viewModel = DealsBrandCategoryActivityViewModel(useCase, dispatcher)
    }

    @Test
    fun getCategoryCombindedData_fetchFailed_shouldShowErrorMessage() {
        // given
        val mockThrowable = Throwable("Error failed")
        coEvery {
            useCase.execute(any(), any())
        } coAnswers {
            secondArg<(Throwable) -> Unit>().invoke(mockThrowable)
        }

        // when
        viewModel.getCategoryCombindedData()

        // then
        val errorMessage = viewModel.errorMessage.value as Throwable
        assert(errorMessage.message == "Error failed")
    }

    @Test
    fun getCategoryCombindedData_fetchSuccess_shouldShowCuratedData() {
        // given
        val mockCuratedData = Gson().fromJson(DealsJsonMapper.getJson("curateddata.json"), CuratedData::class.java)
        coEvery {
            useCase.execute(any(), any())
        } coAnswers {
            firstArg<(CuratedData) -> Unit>().invoke(mockCuratedData)
        }

        // when
        viewModel.getCategoryCombindedData()

        // then
        val curatedData = viewModel.curatedData.value as CuratedData
        assertEquals(curatedData, mockCuratedData)
    }
}