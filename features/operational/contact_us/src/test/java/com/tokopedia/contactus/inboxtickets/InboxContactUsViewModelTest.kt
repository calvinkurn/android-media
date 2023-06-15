package com.tokopedia.contactus.inboxtickets

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.contactus.inboxtickets.data.model.ChipTopBotStatusResponse
import com.tokopedia.contactus.inboxtickets.data.model.InboxTicketListResponse
import com.tokopedia.contactus.inboxtickets.domain.usecase.ChipTopBotStatusUseCase
import com.tokopedia.contactus.inboxtickets.domain.usecase.GetTicketListUseCase
import com.tokopedia.contactus.inboxtickets.view.inbox.InboxContactUsViewModel
import com.tokopedia.contactus.inboxtickets.view.inbox.InboxContactUsViewModel.Companion.ALL
import com.tokopedia.contactus.inboxtickets.view.inbox.InboxContactUsViewModel.Companion.CLOSED
import com.tokopedia.contactus.inboxtickets.view.inbox.InboxContactUsViewModel.Companion.IN_PROGRESS
import com.tokopedia.contactus.inboxtickets.view.inbox.InboxContactUsViewModel.Companion.NEED_RATING
import com.tokopedia.contactus.inboxtickets.view.inbox.uimodel.InboxFilterSelection
import com.tokopedia.contactus.inboxtickets.view.inbox.uimodel.InboxUiEffect
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.unit.test.rule.CoroutineTestRule
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class InboxContactUsViewModelTest {

    var chipTopUsecase: ChipTopBotStatusUseCase =  mockk(relaxed = true)

    var getTicketUsecase: GetTicketListUseCase = mockk(relaxed = true)

    var userSession: UserSessionInterface = mockk(relaxed = true)

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @get:Rule
    val coroutineTestRule = CoroutineTestRule()

    private lateinit var viewModel: InboxContactUsViewModel

    companion object {
        private const val SUCCESS = 1
        private const val FAILED = 0
    }

    @Before
    fun setUp() {
        MockKAnnotations.init(this)

        viewModel = InboxContactUsViewModel(
            CoroutineTestDispatchersProvider,
            chipTopUsecase,
            getTicketUsecase,
            userSession
        )
    }

    @Test
    fun `test setOptionsFilter`() {
        val inputAndExpectation = listOf(
            InboxFilterSelection(ALL, "All", false),
            InboxFilterSelection(IN_PROGRESS, "In Progress", true),
            InboxFilterSelection(NEED_RATING, "Rating", false)
        )
        viewModel.setOptionsFilter(inputAndExpectation)

        assertEquals(viewModel.getOptionsFilter(), inputAndExpectation)
    }

    @Test
    fun `get mark filter options`() {
        val target = InboxFilterSelection(IN_PROGRESS, "In Progress", true)
        val options = listOf(
            InboxFilterSelection(ALL, "All", false),
            InboxFilterSelection(IN_PROGRESS, "In Progress", true),
            InboxFilterSelection(NEED_RATING, "Rating", false)
        )
        viewModel.setOptionsFilter(options)

        val markOptions = viewModel.findMarkOptions()
        assertEquals(target, markOptions)
    }

    @Test
    fun `apply filter`() {
        val optionsSelected = listOf(
            InboxFilterSelection(ALL, "All", false),
            InboxFilterSelection(IN_PROGRESS, "In Progress", true),
            InboxFilterSelection(NEED_RATING, "Rating", false)
        )
        viewModel.markOptionsFilterWithSelected(optionsSelected)
        val optionsState = viewModel.uiState.value
        assertEquals(1, optionsState.offset)
        assertEquals(0, optionsState.ticketItems.size)
        assertEquals(true, optionsState.isFilteredData)
    }

    @Test
    fun `auto filter to show all without any filter`() {
        val optionsSelected = listOf(
            InboxFilterSelection(ALL, "All", false),
            InboxFilterSelection(IN_PROGRESS, "In Progress", true),
            InboxFilterSelection(NEED_RATING, "Rating", false),
            InboxFilterSelection(CLOSED, "Closed", false)
        )
        viewModel.markOptionsFilterWithSelected(optionsSelected)
        viewModel.autoPickShowAllOptionsFilter()
        val isAllFilter = viewModel.getOptionsFilter()[0].isSelected
        assertEquals(true, isAllFilter)
    }

    @Test
    fun `check getTopBotStatus if error`() {
        runTest {
            coEvery {
                chipTopUsecase.invoke(Unit)
            } throws Exception()
            viewModel.getTopBotStatus()
            val actual = viewModel.uiState.value.showChatBotWidget
            assertEquals(false, actual)
        }
    }

    @Test
    fun `check when getTopBotStatus if success but not active`() {
        coEvery { chipTopUsecase.invoke(Unit) } returns createTopBotResponse(
            SUCCESS,
            false
        )
        viewModel.getTopBotStatus()
        val actual = viewModel.uiState.value.showChatBotWidget
        assertEquals(false, actual)
    }

    @Test
    fun `check when getTopBotStatus if failed but active`() {
        coEvery { chipTopUsecase.invoke(Unit) } returns createTopBotResponse(FAILED, false)
        viewModel.getTopBotStatus()
        val actual = viewModel.uiState.value.showChatBotWidget
        assertEquals(false, actual)
    }

    @Test
    fun `check when getTopBotStatus if failed and not active`() {
        coEvery { chipTopUsecase.invoke(Unit) } returns createTopBotResponse(FAILED, false)
        viewModel.getTopBotStatus()
        val actual = viewModel.uiState.value.showChatBotWidget
        assertEquals(false, actual)
    }

    @Test
    fun `check when getTopBotStatus if success and active also unreadNotif = false`() {
        coEvery { chipTopUsecase.invoke(Unit) } returns createTopBotResponse(
            SUCCESS,
            true,
            "Silahkan Masuk"
        )
        viewModel.getTopBotStatus()
        val actual = viewModel.uiState.value
        assertEquals(true, actual.showChatBotWidget)
        assertEquals("256", actual.idMessage)
        assertEquals(false, actual.unReadNotification)
        assertEquals("Silahkan Masuk", actual.welcomeMessage)
    }

    @Test
    fun `check when getTopBotStatus if success and active also unreadNotif = true`() {
        coEvery { chipTopUsecase.invoke(Unit) } returns createTopBotResponse(
            SUCCESS,
            true,
            "Silahkan Masuk",
            true
        )
        viewModel.getTopBotStatus()
        val actual = viewModel.uiState.value
        assertEquals(true, actual.showChatBotWidget)
        assertEquals("256", actual.idMessage)
        assertEquals(true, actual.unReadNotification)
        assertEquals("Silahkan Masuk", actual.welcomeMessage)
    }

    @Test
    fun `check when getTopBotStatus success hit but error and have message`() {
        val errorMessage = "Error Satu Yakk!!"
        coEvery { chipTopUsecase.invoke(Unit) } returns createTopBotResponse(
            SUCCESS,
            true,
            "Silahkan Masuk",
            true,
            messageError = arrayListOf(errorMessage)
        )
        viewModel.getTopBotStatus()
        val actual = viewModel.uiState.value
        val errorMessageActual = actual.errorMessageChatBotWidget
        val isChatbotWidgetShown = actual.showChatBotWidget
        assertEquals(false, isChatbotWidgetShown)
        assertEquals(errorMessage, errorMessageActual)
    }

    @Test
    fun `check when getTopBotStatus success hit but isChatbot is Not Active`() {
        runTest {
            coEvery { chipTopUsecase.invoke(Unit) } returns createTopBotResponse(
                SUCCESS,
                true,
                "Silahkan Masuk",
                true,
                isChatbotActive = false
            )
            viewModel.getTopBotStatus()
            val actual = viewModel.uiState.value
            val isChatbotOff = actual.isChatbotActive
            assertEquals(false, isChatbotOff)
        }
    }

    @Test
    fun `check when getTopBotStatus success hit but isChatbot is null`() {
        runTest {
            coEvery { chipTopUsecase.invoke(Unit) } returns createTopBotResponse(
                SUCCESS,
                true,
                "Silahkan Masuk",
                true,
                isChatbotActive = null
            )
            viewModel.getTopBotStatus()
            val actual = viewModel.uiState.value
            val isChatbotOff = actual.isChatbotActive
            assertEquals(false, isChatbotOff)
        }
    }

    @Test
    fun `check when getTopBotStatus success hit but isChatbot is active`() {
        runTest {
            coEvery { chipTopUsecase.invoke(Unit) } returns createTopBotResponse(
                SUCCESS,
                true,
                "Silahkan",
                true,
                isChatbotActive = true
            )
            viewModel.getTopBotStatus()
            val actual = viewModel.uiState.value
            val isChatbotOff = actual.isChatbotActive
            assertEquals(true, isChatbotOff)
        }
    }

    @Test
    fun `get Ticket List but it is error`() {
        runBlockingTest {
            val optionsSelected = listOf(
                InboxFilterSelection(ALL, "All", true),
                InboxFilterSelection(IN_PROGRESS, "In Progress", false),
                InboxFilterSelection(NEED_RATING, "Rating", false),
                InboxFilterSelection(CLOSED, "Closed", false)
            )
            viewModel.setOptionsFilter(optionsSelected)
            coEvery { getTicketUsecase.invoke(any()) } throws Error()
            val emittedValues = arrayListOf<InboxUiEffect>()
            val job = launch {
                viewModel.uiEffect.toList(emittedValues)
            }
            viewModel.getTicketItems()
            val actualEvent = emittedValues.last()
            val isEmpty = actualEvent is InboxUiEffect.FetchInboxError
            assertEquals(true, isEmpty)
            job.cancel()
        }
    }

    @Test
    fun `get Ticket List On Inbox when filter is not apply but empty`() {
        runBlockingTest {
            val optionsSelected = listOf(
                InboxFilterSelection(ALL, "All", true),
                InboxFilterSelection(IN_PROGRESS, "In Progress", false),
                InboxFilterSelection(NEED_RATING, "Rating", false),
                InboxFilterSelection(CLOSED, "Closed", false)
            )
            viewModel.setOptionsFilter(optionsSelected)
            coEvery { getTicketUsecase.invoke(any()) } returns createListTicket(true)
            val emittedValues = arrayListOf<InboxUiEffect>()

            val job = launch {
                viewModel.uiEffect.toList(emittedValues)
            }

            viewModel.getTicketItems()
            val actualEvent = emittedValues.last()
            val isEmpty = actualEvent is InboxUiEffect.EmptyTicket
            assertEquals(true, isEmpty)

            job.cancel()
        }
    }

    @Test
    fun `get Ticket List On Inbox when filter is not apply`() {
        runBlockingTest {
            val optionsSelected = listOf(
                InboxFilterSelection(ALL, "All", true),
                InboxFilterSelection(IN_PROGRESS, "In Progress", false),
                InboxFilterSelection(NEED_RATING, "Rating", false),
                InboxFilterSelection(CLOSED, "Closed", false)
            )
            viewModel.setOptionsFilter(optionsSelected)
            coEvery { getTicketUsecase.invoke(any()) } returns createListTicket(
                false,
                statusTicket = ALL
            )
            viewModel.getTicketItems()
            val stateData = viewModel.uiState.value
            assertEquals(5, stateData.ticketItems.size)
        }
    }

    @Test
    fun `get Ticket List On Inbox when filter is In Progress`() {
        runBlockingTest {
            val optionsSelected = listOf(
                InboxFilterSelection(ALL, "All", false),
                InboxFilterSelection(IN_PROGRESS, "In Progress", true),
                InboxFilterSelection(NEED_RATING, "Rating", false),
                InboxFilterSelection(CLOSED, "Closed", false)
            )
            viewModel.setOptionsFilter(optionsSelected)
            coEvery { getTicketUsecase.invoke(any()) } returns createListTicket(
                false,
                statusTicket = IN_PROGRESS
            )
            viewModel.getTicketItems()
            val stateData = viewModel.uiState.value
            assertEquals(4, stateData.ticketItems.size)
        }
    }

    @Test
    fun `get Ticket List On Inbox when filter is NEED RATING`() {
        runBlockingTest {
            val optionsSelected = listOf(
                InboxFilterSelection(ALL, "All", false),
                InboxFilterSelection(IN_PROGRESS, "In Progress", false),
                InboxFilterSelection(NEED_RATING, "Rating", true),
                InboxFilterSelection(CLOSED, "Closed", false)
            )
            viewModel.setOptionsFilter(optionsSelected)
            coEvery { getTicketUsecase.invoke(any()) } returns createListTicket(
                false,
                statusTicket = NEED_RATING
            )
            viewModel.getTicketItems()
            val stateData = viewModel.uiState.value
            assertEquals(3, stateData.ticketItems.size)
        }
    }

    @Test
    fun `get Ticket List On Inbox when filter is Closed`() {
        runBlockingTest {
            val optionsSelected = listOf(
                InboxFilterSelection(ALL, "All", false),
                InboxFilterSelection(IN_PROGRESS, "In Progress", false),
                InboxFilterSelection(NEED_RATING, "Rating", false),
                InboxFilterSelection(CLOSED, "Closed", true)
            )
            viewModel.setOptionsFilter(optionsSelected)
            coEvery { getTicketUsecase.invoke(any()) } returns createListTicket(
                false,
                statusTicket = CLOSED
            )
            viewModel.getTicketItems()
            val stateData = viewModel.uiState.value
            assertEquals(2, stateData.ticketItems.size)
        }
    }

    @Test
    fun `get Ticket List On Inbox when filter is Closed and has next and prev`() {
        runBlockingTest {
            val optionsSelected = listOf(
                InboxFilterSelection(ALL, "All", false),
                InboxFilterSelection(IN_PROGRESS, "In Progress", false),
                InboxFilterSelection(NEED_RATING, "Rating", false),
                InboxFilterSelection(CLOSED, "Closed", true)
            )
            viewModel.setOptionsFilter(optionsSelected)
            coEvery { getTicketUsecase.invoke(any()) } returns createListTicketHasNextAndPref(
                false,
                statusTicket = CLOSED
            )
            viewModel.getTicketItems()
            val stateData = viewModel.uiState.value
            assertEquals(2, stateData.ticketItems.size)
        }
    }

    @Test
    fun `check getItemTicketOnPosition`() {
        runBlockingTest {
            val listResponse = createListTicketHasNextAndPref(false, statusTicket = CLOSED)
            val target = listResponse.getTicketOnInbox().getTicket().getTicketList()[0]
            val optionsSelected = listOf(
                InboxFilterSelection(ALL, "All", false),
                InboxFilterSelection(IN_PROGRESS, "In Progress", false),
                InboxFilterSelection(NEED_RATING, "Rating", false),
                InboxFilterSelection(CLOSED, "Closed", true)
            )
            viewModel.setOptionsFilter(optionsSelected)
            coEvery { getTicketUsecase.invoke(any()) } returns listResponse
            viewModel.getTicketItems()
            val ticketItem = viewModel.getItemTicketOnPosition(0)
            assertEquals(target, ticketItem)
        }
    }

    @Test
    fun `check restart paging`() {
        viewModel.restartPageOfList()
        val stateData = viewModel.uiState.value
        assertEquals(stateData.offset, 1)
    }

    private fun createTopBotResponse(
        isSuccessHit: Int,
        isActive: Boolean,
        welcomeMessage: String = "",
        unreadNotif: Boolean = false,
        messageError: List<String> = arrayListOf(),
        isChatbotActive: Boolean? = true
    ): ChipTopBotStatusResponse {
        return ChipTopBotStatusResponse(
            ChipTopBotStatusResponse.ChipTopBotStatusInbox(
                chipTopBotStatusData =
                ChipTopBotStatusResponse.ChipTopBotStatusInbox.ChipTopBotStatusData(
                    isActive = isActive,
                    isSuccess = isSuccessHit,
                    messageId = "256",
                    unreadNotif = unreadNotif,
                    welcomeMessage = welcomeMessage,
                    isChatbotActive = isChatbotActive
                ),
                    messageError= messageError
            )
        )
    }

    private fun createListTicket(
        isEmpty: Boolean,
        isSuccess: Int = SUCCESS,
        statusTicket: Int = CLOSED
    ): InboxTicketListResponse {
        return InboxTicketListResponse(
            ticket = InboxTicketListResponse.Ticket(
                ticketData = InboxTicketListResponse.Ticket.Data(
                    isSuccess = isSuccess,
                    nextPage = "",
                    previousPage = "",
                    ticketItems = if (isEmpty) {
                        emptyList()
                    } else {
                        createTicketItemsBaseOnStatus(statusTicket)
                    }
                )
            )
        )
    }

    private fun createListTicketHasNextAndPref(
        isEmpty: Boolean,
        isSuccess: Int = SUCCESS,
        statusTicket: Int = CLOSED
    ): InboxTicketListResponse {
        return InboxTicketListResponse(
            ticket = InboxTicketListResponse.Ticket(
                ticketData = InboxTicketListResponse.Ticket.Data(
                    isSuccess = isSuccess,
                    nextPage = "dasdasd",
                    previousPage = "dasdasd",
                    ticketItems = if (isEmpty) {
                        emptyList()
                    } else {
                        createTicketItemsBaseOnStatus(statusTicket)
                    }
                )
            )
        )
    }

    private fun createTicketItemsBaseOnStatus(statusTicket: Int): List<InboxTicketListResponse.Ticket.Data.TicketItem> {
        val list =
            arrayListOf<InboxTicketListResponse.Ticket.Data.TicketItem>()
        when (statusTicket) {
            CLOSED -> {
                for (i in 1..2) {
                    list.add(createTicketItem(statusTicket.toLong(), i.toString()))
                }
            }
            NEED_RATING -> {
                for (i in 1..3) {
                    list.add(createTicketItem(statusTicket.toLong(), i.toString()))
                }
            }
            IN_PROGRESS -> {
                for (i in 1..4) {
                    list.add(createTicketItem(statusTicket.toLong(), i.toString()))
                }
            }
            ALL -> {
                for (i in 1..5) {
                    list.add(createTicketItem(i.toLong(), i.toString()))
                }
            }
        }
        return list
    }

    private fun createTicketItem(
        statusId: Long,
        id: String
    ): InboxTicketListResponse.Ticket.Data.TicketItem {
        return InboxTicketListResponse.Ticket.Data.TicketItem(
            id = id,
            statusId = statusId
        )
    }
}
