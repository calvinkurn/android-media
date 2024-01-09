package com.tokopedia.deals.common

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.gson.Gson
import com.tokopedia.deals.DealsJsonMapper
import com.tokopedia.deals.common.ui.viewmodel.DealsBrandCategoryActivityViewModel
import com.tokopedia.deals.data.entity.CuratedData
import com.tokopedia.deals.domain.GetChipsCategoryUseCase
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import io.mockk.coEvery
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

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
        val mockThrowable = Exception("Error failed")
        coEvery {
            useCase.invoke(Unit)
        } throws mockThrowable

        // when
        viewModel.getCategoryCombindedData()

        // then
        val errorMessage = viewModel.errorMessage.value as Throwable
        assert(errorMessage.message == "Error failed")
    }

    @Test
    fun getCategoryCombindedData_fetchSuccess_shouldShowCuratedData() {
        // given
        val mockCuratedData =
            Gson().fromJson(DealsJsonMapper.getJson("curateddata.json"), CuratedData::class.java)
        coEvery {
            useCase.invoke(Unit)
        } returns mockCuratedData

        // when
        viewModel.getCategoryCombindedData()

        // then
        val curatedData = viewModel.curatedData.value as CuratedData
        assertEquals(curatedData, mockCuratedData)
    }
}
