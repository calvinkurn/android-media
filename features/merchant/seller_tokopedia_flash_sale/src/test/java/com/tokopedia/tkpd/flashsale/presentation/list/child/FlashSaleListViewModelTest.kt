package com.tokopedia.tkpd.flashsale.presentation.list.child

import com.tokopedia.tkpd.flashsale.domain.entity.FlashSaleCategory
import com.tokopedia.tkpd.flashsale.domain.usecase.GetFlashSaleListForSellerCategoryUseCase
import com.tokopedia.tkpd.flashsale.domain.usecase.GetFlashSaleListForSellerUseCase
import com.tokopedia.tkpd.flashsale.presentation.list.child.uimodel.FlashSaleListUiEvent
import com.tokopedia.tkpd.flashsale.presentation.list.child.uimodel.FlashSaleListUiState
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class FlashSaleListViewModelTest {

    @RelaxedMockK
    lateinit var  getFlashSaleListForSellerUseCase: GetFlashSaleListForSellerUseCase

    @RelaxedMockK
    lateinit var getFlashSaleListForSellerCategoryUseCase: GetFlashSaleListForSellerCategoryUseCase

    private lateinit var viewModel: FlashSaleListViewModel

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        viewModel =  FlashSaleListViewModel(
            CoroutineTestDispatchersProvider,
            getFlashSaleListForSellerUseCase,
            getFlashSaleListForSellerCategoryUseCase
        )
    }

    @Test
    fun `When init page, should fetch flash sale category`() = runBlockingTest {
        val categories = listOf(FlashSaleCategory(1, "Book"))

        val tabId = 1
        val tabName = "upcoming"

        coEvery { getFlashSaleListForSellerCategoryUseCase.execute(tabName) } returns categories

        //When
        viewModel.processEvent(FlashSaleListUiEvent.Init(tabName, tabId))

        //Then
        coVerify { getFlashSaleListForSellerCategoryUseCase.execute(tabName) }
    }

    @Test
    fun `When init page, should store tabId and tabName successfully`() = runBlockingTest {
        val categories = listOf(FlashSaleCategory(1, "Book"))

        val tabId = 1
        val tabName = "upcoming"

        coEvery { getFlashSaleListForSellerCategoryUseCase.execute(tabName) } returns categories

        val emittedValues = arrayListOf<FlashSaleListUiState>()
        val job = launch {
            viewModel.uiState.toList(emittedValues)
        }

        //When
        viewModel.processEvent(FlashSaleListUiEvent.Init(tabName, tabId))

        val actualTabId = emittedValues.last().tabId
        val actualTabName = emittedValues.last().tabName
        val actualCategories = emittedValues.last().flashSaleCategories

        assertEquals(tabId, actualTabId)
        assertEquals(tabName, actualTabName)
        assertEquals(actualCategories, categories)


        job.cancel()
    }
}