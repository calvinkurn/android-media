package com.tokopedia.tkpd.flashsale.presentation.list.child

import com.tokopedia.tkpd.flashsale.domain.entity.FlashSale
import com.tokopedia.tkpd.flashsale.domain.entity.FlashSaleCategory
import com.tokopedia.tkpd.flashsale.domain.entity.FlashSaleData
import com.tokopedia.tkpd.flashsale.domain.entity.enums.FlashSaleStatus
import com.tokopedia.tkpd.flashsale.domain.usecase.GetFlashSaleListForSellerCategoryUseCase
import com.tokopedia.tkpd.flashsale.domain.usecase.GetFlashSaleListForSellerUseCase
import com.tokopedia.tkpd.flashsale.presentation.list.child.adapter.item.FinishedFlashSaleItem
import com.tokopedia.tkpd.flashsale.presentation.list.child.adapter.item.UpcomingFlashSaleItem
import com.tokopedia.tkpd.flashsale.presentation.list.child.uimodel.FlashSaleListUiEvent
import com.tokopedia.tkpd.flashsale.presentation.list.child.uimodel.FlashSaleListUiState
import com.tokopedia.tkpd.flashsale.util.constant.TabConstant
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.util.GregorianCalendar

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
    fun `When fetch category from remote success, should successfully receive the data`() = runBlockingTest {
        val categories = listOf(FlashSaleCategory(1, "Book"))

        val tabName = "upcoming"

        coEvery { getFlashSaleListForSellerCategoryUseCase.execute(tabName) } returns categories

        val emittedValues = arrayListOf<FlashSaleListUiState>()
        val job = launch {
            viewModel.uiState.toList(emittedValues)
        }

        //When
        viewModel.processEvent(FlashSaleListUiEvent.GetFlashSaleCategory(tabName))

        val actualCategories = emittedValues.last().flashSaleCategories
        val actualTabName = emittedValues.last().tabName

        assertEquals(tabName, actualTabName)
        assertEquals(categories, actualCategories)

        job.cancel()
    }

    @Test
    fun `When fetch upcoming flash sale from remote success, should successfully receive the data`() = runBlockingTest {
        //Given
        val tabId = TabConstant.TAB_ID_UPCOMING
        val tabName = "upcoming"
        val offset = 0

        val params = GetFlashSaleListForSellerUseCase.Param(
            tabName,
            offset,
            categoryIds = listOf(),
            statusIds = listOf(),
            sortOrderBy = "DEFAULT_VALUE_PLACEHOLDER",
            sortOrderRule = "ASC",
            requestProductMetaData = false
        )

        val currentDate = GregorianCalendar(2022, 10,1, 0,0,0).time
        val flashSaleStartDate = GregorianCalendar(2022, 10,10, 0,0,0).time
        val flashSaleEndDate = GregorianCalendar(2022, 10,20, 0,0,0).time
        val flashSaleReview = GregorianCalendar(2022, 10,5, 0,0,0).time
        val flashSaleSubmission = GregorianCalendar(2022, 10,1, 7,0,0).time

        val flashSale = FlashSale(
            1,
            "",
            "",
            "",
            flashSaleEndDate,
            1,
            "Flash Sale 1",
            true,
            FlashSale.ProductMeta(5, 5, 10, 5, 5, 0),
            1,
            flashSaleReview,
            flashSaleReview,
            "",
            flashSaleStartDate,
            TabConstant.TAB_ID_UPCOMING,
            "Pendaftaran berakhir",
            flashSaleSubmission,
            flashSaleSubmission,
            false,
            FlashSale.FormattedDate("", ""),
            FlashSaleStatus.UPCOMING
        )
        val expected = listOf(
            UpcomingFlashSaleItem(
                1,
                "Flash Sale 1",
                "",
                1,
                1,
                "",
                "",
                flashSaleEndDate,
                0,
                7,
                flashSaleSubmission
            )

        )
        val response = FlashSaleData(1, listOf(flashSale))

        coEvery { getFlashSaleListForSellerUseCase.execute(params) } returns response

        val emittedValues = arrayListOf<FlashSaleListUiState>()
        val job = launch {
            viewModel.uiState.toList(emittedValues)
        }

        //When
        viewModel.processEvent(FlashSaleListUiEvent.LoadPage(tabId, tabName, offset, currentDate))

        //Then
        val actualTabName = emittedValues.last().tabName
        val actualOffset = emittedValues.last().offset
        val actualTabId = emittedValues.last().tabId
        val actualItems = emittedValues.last().allItems

        assertEquals(tabName, actualTabName)
        assertEquals(offset, actualOffset)
        assertEquals(tabId, actualTabId)
        assertEquals(expected, actualItems)

        job.cancel()
    }

    @Test
    fun `When fetch finished flash sale from remote success, should successfully receive the data`() = runBlockingTest {
        //Given
        val tabId =  TabConstant.TAB_ID_FINISHED
        val tabName = "finished"
        val offset = 0

        val params = GetFlashSaleListForSellerUseCase.Param(
            tabName,
            offset,
            categoryIds = listOf(),
            statusIds = listOf(),
            sortOrderBy = "DEFAULT_VALUE_PLACEHOLDER",
            sortOrderRule = "ASC",
            requestProductMetaData = true
        )

        val productMeta = FlashSale.ProductMeta(5, 5, 10, 5, 5, 0)
        val currentDate = GregorianCalendar(2022, 10,1, 0,0,0).time
        val flashSaleStartDate = GregorianCalendar(2022, 10,10, 0,0,0).time
        val flashSaleEndDate = GregorianCalendar(2022, 10,20, 0,0,0).time
        val flashSaleReview = GregorianCalendar(2022, 10,5, 0,0,0).time
        val flashSaleSubmission = GregorianCalendar(2022, 10,1, 7,0,0).time

        val flashSale = FlashSale(
            1,
            "",
            "",
            "",
            flashSaleEndDate,
            1,
            "Flash Sale 1",
            true,
            productMeta,
            1,
            flashSaleReview,
            flashSaleReview,
            "",
            flashSaleStartDate,
            TabConstant.TAB_ID_FINISHED,
            "Selesai",
            flashSaleSubmission,
            flashSaleSubmission,
            false,
            FlashSale.FormattedDate("", ""),
            FlashSaleStatus.FINISHED
        )
        val expected = listOf(
            FinishedFlashSaleItem(
                1,
                "Flash Sale 1",
                "",
                "",
                "",
                FlashSaleStatus.FINISHED,
                "Selesai",
                productMeta,
                "",
                100
            )

        )
        val response = FlashSaleData(1, listOf(flashSale))

        coEvery { getFlashSaleListForSellerUseCase.execute(params) } returns response

        val emittedValues = arrayListOf<FlashSaleListUiState>()
        val job = launch {
            viewModel.uiState.toList(emittedValues)
        }

        //When
        viewModel.processEvent(FlashSaleListUiEvent.LoadPage(tabId, tabName, offset, currentDate))

        //Then
        val actualTabName = emittedValues.last().tabName
        val actualOffset = emittedValues.last().offset
        val actualTabId = emittedValues.last().tabId
        val actualItems = emittedValues.last().allItems

        assertEquals(tabName, actualTabName)
        assertEquals(offset, actualOffset)
        assertEquals(tabId, actualTabId)
        assertEquals(expected, actualItems)

        job.cancel()
    }
}