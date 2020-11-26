package com.tokopedia.deals.common

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.gson.Gson
import com.tokopedia.deals.category.domain.GetChipsCategoryUseCase
import com.tokopedia.deals.common.ui.viewmodel.DealsBrandCategoryActivityViewModel
import com.tokopedia.deals.common.utils.DealsTestDispatcherProvider
import com.tokopedia.deals.search.model.response.Category
import com.tokopedia.deals.search.model.response.CuratedData
import com.tokopedia.deals.search.model.response.EventChildCategory
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.lang.Exception
import com.tokopedia.deals.DealsJsonMapper
import junit.framework.Assert.assertEquals

class DealsBrandCategoryActivityViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()
    private val dispatcher = DealsTestDispatcherProvider()

    private val useCase: GetChipsCategoryUseCase = mockk()
    private lateinit var viewModel: DealsBrandCategoryActivityViewModel

    @Before
    fun setup() {
        viewModel = DealsBrandCategoryActivityViewModel(useCase, dispatcher)
    }

    @Test
    fun getCategoryCombindedData_fetchFailed_shouldShowErrorMessage() {
        // given
        coEvery { useCase.executeOnBackground() } coAnswers {throw  Throwable("Error failed") }

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
        coEvery { useCase.executeOnBackground() } returns mockCuratedData

        // when
        viewModel.getCategoryCombindedData()

        // then
        val curatedData = viewModel.curatedData.value as CuratedData
        assertEquals(curatedData, mockCuratedData)
    }
}