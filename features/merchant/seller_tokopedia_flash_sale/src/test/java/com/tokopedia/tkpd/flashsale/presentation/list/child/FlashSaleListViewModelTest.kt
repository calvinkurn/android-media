package com.tokopedia.tkpd.flashsale.presentation.list.child

import com.tokopedia.campaign.components.bottomsheet.selection.entity.MultipleSelectionItem
import com.tokopedia.campaign.components.bottomsheet.selection.entity.SingleSelectionItem
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.tkpd.flashsale.domain.entity.FlashSale
import com.tokopedia.tkpd.flashsale.domain.entity.FlashSaleCategory
import com.tokopedia.tkpd.flashsale.domain.entity.FlashSaleData
import com.tokopedia.tkpd.flashsale.domain.entity.enums.FlashSaleListPageTab
import com.tokopedia.tkpd.flashsale.domain.entity.enums.FlashSaleStatus
import com.tokopedia.tkpd.flashsale.domain.usecase.GetFlashSaleListForSellerCategoryUseCase
import com.tokopedia.tkpd.flashsale.domain.usecase.GetFlashSaleListForSellerUseCase
import com.tokopedia.tkpd.flashsale.presentation.list.child.adapter.item.FinishedFlashSaleItem
import com.tokopedia.tkpd.flashsale.presentation.list.child.adapter.item.OngoingFlashSaleItem
import com.tokopedia.tkpd.flashsale.presentation.list.child.adapter.item.RegisteredFlashSaleItem
import com.tokopedia.tkpd.flashsale.presentation.list.child.adapter.item.UpcomingFlashSaleItem
import com.tokopedia.tkpd.flashsale.presentation.list.child.uimodel.FlashSaleListUiEffect
import com.tokopedia.tkpd.flashsale.presentation.list.child.uimodel.FlashSaleListUiEvent
import com.tokopedia.tkpd.flashsale.presentation.list.child.uimodel.FlashSaleListUiState
import com.tokopedia.tkpd.flashsale.util.constant.TabConstant
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.util.Date
import java.util.GregorianCalendar

@ExperimentalCoroutinesApi
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
    fun `When fetch category from remote error, should emit correct error event`() = runBlockingTest {
        //Given
        val tabName = "upcoming"
        val error = MessageErrorException("Server Error")
        val expectedEvent = FlashSaleListUiEffect.FetchCategoryError(error)

        coEvery { getFlashSaleListForSellerCategoryUseCase.execute(tabName) } throws error

        val emittedValues = arrayListOf<FlashSaleListUiEffect>()
        val job = launch {
            viewModel.uiEffect.toList(emittedValues)
        }

        //When
        viewModel.processEvent(FlashSaleListUiEvent.GetFlashSaleCategory(tabName))

        val actualEvent = emittedValues.last()

        assertEquals(expectedEvent, actualEvent)

        job.cancel()
    }

    @Test
    fun `When fetch flash sale from remote error, should emit correct error event`() = runBlockingTest {
        //Given
        val tabId = TabConstant.TAB_ID_UPCOMING
        val tabName = "upcoming"
        val offset = 0
        val error = MessageErrorException("Server Error")
        val expectedEvent = FlashSaleListUiEffect.FetchFlashSaleError(error)
        val currentDate = Date()

        val params = GetFlashSaleListForSellerUseCase.Param(
            tabName,
            offset,
            categoryIds = listOf(),
            statusIds = listOf(),
            sortOrderBy = "DEFAULT_VALUE_PLACEHOLDER",
            sortOrderRule = "ASC",
            requestProductMetaData = false
        )

        coEvery { getFlashSaleListForSellerUseCase.execute(params) }  throws error

        val emittedValues = arrayListOf<FlashSaleListUiEffect>()
        val job = launch {
            viewModel.uiEffect.toList(emittedValues)
        }

        //When
        viewModel.processEvent(FlashSaleListUiEvent.LoadPage(tabId, tabName, offset, currentDate))

        //Then
        val actualEvent = emittedValues.last()

        assertEquals(expectedEvent, actualEvent)

        job.cancel()
    }

    @Test
    fun `When fetch flash sale from remote error, should update loading state to false`() = runBlockingTest {
        //Given
        val tabId = TabConstant.TAB_ID_UPCOMING
        val tabName = "upcoming"
        val offset = 0
        val error = MessageErrorException("Server Error")
        val currentDate = Date()

        val params = GetFlashSaleListForSellerUseCase.Param(
            tabName,
            offset,
            categoryIds = listOf(),
            statusIds = listOf(),
            sortOrderBy = "DEFAULT_VALUE_PLACEHOLDER",
            sortOrderRule = "ASC",
            requestProductMetaData = false
        )

        coEvery { getFlashSaleListForSellerUseCase.execute(params) }  throws error

        val emittedValues = arrayListOf<FlashSaleListUiState>()

        val job = launch {
            viewModel.uiState.toList(emittedValues)
        }

        //When
        viewModel.processEvent(FlashSaleListUiEvent.LoadPage(tabId, tabName, offset, currentDate))

        //Then
        val actualValues = emittedValues.last()

        assertEquals(false, actualValues.isLoading)

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
            FlashSale.ProductMeta(5, 5, 10, 5, 5, 0, 0.0),
            1,
            flashSaleReview,
            flashSaleReview,
            "",
            flashSaleStartDate,
            tabId,
            "Pendaftaran berakhir",
            flashSaleSubmission,
            flashSaleSubmission,
            false,
            FlashSale.FormattedDate("", ""),
            FlashSaleStatus.UPCOMING,
            emptyList(),
            FlashSaleListPageTab.UPCOMING
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
                flashSaleSubmission,
                useMultiLocation = false
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

        val productMeta = FlashSale.ProductMeta(5, 5, 10, 5, 5, 0, 0.0)
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
            tabId,
            "Selesai",
            flashSaleSubmission,
            flashSaleSubmission,
            false,
            FlashSale.FormattedDate("", ""),
            FlashSaleStatus.FINISHED,
            emptyList(),
            FlashSaleListPageTab.FINISHED
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
                100,
                useMultiLocation = false
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
    fun `When fetch registered flash sale from remote success, should successfully receive the data`() = runBlockingTest {
        //Given
        val tabId =  TabConstant.TAB_ID_REGISTERED
        val tabName = "registered"
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

        val productMeta = FlashSale.ProductMeta(5, 5, 10, 5, 5, 0, 0.0)
        val currentDate = GregorianCalendar(2022, 10,10, 0,0,0).time
        val flashSaleStartDate = GregorianCalendar(2022, 10,10, 7,0,0).time
        val flashSaleEndDate = GregorianCalendar(2022, 10,20, 0,0,0).time
        val flashSaleReview = GregorianCalendar(2022, 10,10, 5,0,0).time
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
            tabId,
            "Proses Seleksi",
            flashSaleSubmission,
            flashSaleSubmission,
            false,
            FlashSale.FormattedDate("", ""),
            FlashSaleStatus.WAITING_FOR_SELECTION,
            emptyList(),
            FlashSaleListPageTab.REGISTERED
        )

        val expected = listOf(
            RegisteredFlashSaleItem(
                1,
                "Flash Sale 1",
                "",
                flashSaleStartDate,
                flashSaleEndDate,
                "",
                "",
                flashSaleReview,
                flashSaleReview,
                FlashSaleStatus.WAITING_FOR_SELECTION,
                "Proses Seleksi",
                useMultiLocation = false
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
    fun `When fetch ongoing flash sale from remote success, should successfully receive the data`() = runBlockingTest {
        //Given
        val tabId =  TabConstant.TAB_ID_ONGOING
        val tabName = "ongoing"
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

        val productMeta = FlashSale.ProductMeta(5, 5, 10, 5, 5, 0, 0.0)
        val currentDate = GregorianCalendar(2022, 10,10, 0,0,0).time
        val flashSaleStartDate = GregorianCalendar(2022, 10,10, 10,0,0).time
        val flashSaleEndDate = GregorianCalendar(2022, 10,10, 12,0,0).time
        val flashSaleReview = GregorianCalendar(2022, 10,5, 5,0,0).time
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
            tabId,
            "Berlangsung",
            flashSaleSubmission,
            flashSaleSubmission,
            false,
            FlashSale.FormattedDate("", ""),
            FlashSaleStatus.ONGOING,
            emptyList(),
            FlashSaleListPageTab.ONGOING
        )
        val expected = listOf(
            OngoingFlashSaleItem(
                1,
                "Flash Sale 1",
                "",
                "",
                "",
                flashSaleEndDate,
                5,
                5,
                FlashSaleStatus.ONGOING,
                "Berlangsung",
                useMultiLocation = false
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
    fun `When fetch flash sale list with unknown tab id, should treat the received flash sale as upcoming campaign`() = runBlockingTest {
        //Given
        val tabId = 9
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
            FlashSale.ProductMeta(5, 5, 10, 5, 5, 0, 0.0),
            1,
            flashSaleReview,
            flashSaleReview,
            "",
            flashSaleStartDate,
            tabId,
            "Pendaftaran berakhir",
            flashSaleSubmission,
            flashSaleSubmission,
            false,
            FlashSale.FormattedDate("", ""),
            FlashSaleStatus.UPCOMING,
            emptyList(),
            FlashSaleListPageTab.UPCOMING
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
                flashSaleSubmission,
                useMultiLocation = false
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
    fun `When change sort, should emit correct event with selected sort id`() = runBlockingTest{
        //Given
        val selectedSortId = "DEFAULT_VALUE_PLACEHOLDER"
        val expected = FlashSaleListUiEffect.ShowSortBottomSheet(selectedSortId)
        val emittedValues = arrayListOf<FlashSaleListUiEffect>()
        val job = launch {
            viewModel.uiEffect.toList(emittedValues)
        }

        //When
        viewModel.processEvent(FlashSaleListUiEvent.ChangeSort)

        //Then
        val actual = emittedValues.last()


        assertEquals(expected, actual)

        job.cancel()
    }

    @Test
    fun `When change category, should emit correct event with selected sort id`() = runBlockingTest{
        //Given
        val selectedCategoryId = listOf<Long>()
        val categories = listOf<FlashSaleCategory>()
        val expected = FlashSaleListUiEffect.ShowCategoryBottomSheet(selectedCategoryId, categories)

        val emittedValues = arrayListOf<FlashSaleListUiEffect>()
        val job = launch {
            viewModel.uiEffect.toList(emittedValues)
        }

        //When
        viewModel.processEvent(FlashSaleListUiEvent.ChangeCategory)

        //Then
        val actual = emittedValues.last()


        assertEquals(expected, actual)

        job.cancel()
    }

    @Test
    fun `When change status, should emit correct event with selected sort id`() = runBlockingTest{
        //Given
        val selectedStatusIds = listOf<String>()
        val expected = FlashSaleListUiEffect.ShowStatusBottomSheet(selectedStatusIds)

        val emittedValues = arrayListOf<FlashSaleListUiEffect>()
        val job = launch {
            viewModel.uiEffect.toList(emittedValues)
        }

        //When
        viewModel.processEvent(FlashSaleListUiEvent.ChangeStatus)

        //Then
        val actual = emittedValues.last()


        assertEquals(expected, actual)

        job.cancel()
    }


    @Test
    fun `When apply sort date by desc order, isFilterActive should be true`() = runBlockingTest {
        //Given
        val selectedSort = SingleSelectionItem("DATE", name = "Terbaru", isSelected = true, direction = "DESC")

        val emittedValues = arrayListOf<FlashSaleListUiState>()
        val job = launch {
            viewModel.uiState.toList(emittedValues)
        }

        //When
        viewModel.processEvent(FlashSaleListUiEvent.ApplySort(selectedSort))

        //Then
        val actual = emittedValues.last()


        assertEquals(selectedSort, actual.selectedSort)
        assertEquals(0, actual.offset)
        assertEquals(true, actual.isFilterActive)

        job.cancel()
    }

    @Test
    fun `When apply sort date to default sort, isFilterActive should be false`() = runBlockingTest {
        //Given
        val selectedSort = SingleSelectionItem("DEFAULT_VALUE_PLACEHOLDER", name = "", isSelected = false, direction = "ASC")

        val emittedValues = arrayListOf<FlashSaleListUiState>()
        val job = launch {
            viewModel.uiState.toList(emittedValues)
        }

        //When
        viewModel.processEvent(FlashSaleListUiEvent.ApplySort(selectedSort))

        //Then
        val actual = emittedValues.last()


        assertEquals(selectedSort, actual.selectedSort)
        assertEquals(0, actual.offset)
        assertEquals(false, actual.isFilterActive)

        job.cancel()
    }

    @Test
    fun `When apply category filter, isFilterActive should be true`() = runBlockingTest {
        //Given
        val selectedCategories = listOf(MultipleSelectionItem("1", "Buku"), MultipleSelectionItem("2", "Permainan"))

        val emittedValues = arrayListOf<FlashSaleListUiState>()
        val job = launch {
            viewModel.uiState.toList(emittedValues)
        }

        //When
        viewModel.processEvent(FlashSaleListUiEvent.ApplyCategoryFilter(selectedCategories))

        //Then
        val actual = emittedValues.last()


        assertEquals(listOf<Long>(1,2), actual.selectedCategoryIds)
        assertEquals(0, actual.offset)
        assertEquals(true, actual.isFilterActive)

        job.cancel()
    }

    @Test
    fun `When apply category filter and selected categories is empty, isFilterActive should be false`() = runBlockingTest {
        //Given
        val selectedCategories = listOf<MultipleSelectionItem>()

        val emittedValues = arrayListOf<FlashSaleListUiState>()
        val job = launch {
            viewModel.uiState.toList(emittedValues)
        }

        //When
        viewModel.processEvent(FlashSaleListUiEvent.ApplyCategoryFilter(selectedCategories))

        //Then
        val actual = emittedValues.last()


        assertEquals(listOf<Long>(), actual.selectedCategoryIds)
        assertEquals(0, actual.offset)
        assertEquals(false, actual.isFilterActive)

        job.cancel()
    }


    @Test
    fun `When apply status filter, isFilterActive should be true`() = runBlockingTest {
        //Given
        val selectedStatus = listOf(MultipleSelectionItem("1", "Terlewat"), MultipleSelectionItem("2", "Selesai"))

        val emittedValues = arrayListOf<FlashSaleListUiState>()
        val job = launch {
            viewModel.uiState.toList(emittedValues)
        }

        //When
        viewModel.processEvent(FlashSaleListUiEvent.ApplyStatusFilter(selectedStatus))

        //Then
        val actual = emittedValues.last()


        assertEquals(listOf("1","2"), actual.selectedStatusIds)
        assertEquals(0, actual.offset)
        assertEquals(true, actual.isFilterActive)

        job.cancel()
    }

    @Test
    fun `When apply status filter and selected status is empty, isFilterActive should be false`() = runBlockingTest {
        //Given
        val selectedStatus = listOf<MultipleSelectionItem>()

        val emittedValues = arrayListOf<FlashSaleListUiState>()
        val job = launch {
            viewModel.uiState.toList(emittedValues)
        }

        //When
        viewModel.processEvent(FlashSaleListUiEvent.ApplyStatusFilter(selectedStatus))

        //Then
        val actual = emittedValues.last()


        assertEquals(listOf<Long>(), actual.selectedStatusIds)
        assertEquals(0, actual.offset)
        assertEquals(false, actual.isFilterActive)

        job.cancel()
    }

    @Test
    fun `When clear filter, related data should be successfully cleared`() = runBlockingTest {
        //Given
        val emittedValues = arrayListOf<FlashSaleListUiState>()
        val job = launch {
            viewModel.uiState.toList(emittedValues)
        }

        //When
        viewModel.processEvent(FlashSaleListUiEvent.ClearFilter)

        //Then
        val actual = emittedValues.last()


        assertEquals(listOf<Long>(), actual.selectedCategoryIds)
        assertEquals(SingleSelectionItem("DEFAULT_VALUE_PLACEHOLDER", name = "", isSelected = false, direction = "ASC"), actual.selectedSort)
        assertEquals(listOf<String>(), actual.selectedStatusIds)
        assertEquals(0, actual.offset)
        assertEquals(false, actual.isFilterActive)
        assertEquals(false, actual.isLoading)

        job.cancel()
    }
}
