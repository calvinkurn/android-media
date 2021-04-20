package com.tokopedia.contactus.inboxticket2.view.presenter

import android.app.Activity
import android.view.View
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.contactus.common.analytics.ContactUsTracking
import com.tokopedia.contactus.createticket.widget.LinearLayoutManager
import com.tokopedia.contactus.inboxticket2.data.model.ChipTopBotStatusResponse
import com.tokopedia.contactus.inboxticket2.data.model.InboxTicketListResponse
import com.tokopedia.contactus.inboxticket2.domain.usecase.ChipTopBotStatusUseCase
import com.tokopedia.contactus.inboxticket2.domain.usecase.GetTicketListUseCase
import com.tokopedia.contactus.inboxticket2.view.contract.InboxBaseContract
import com.tokopedia.contactus.inboxticket2.view.contract.InboxListContract
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class InboxListPresenterTest {

    @get:Rule
    var rule = InstantTaskExecutorRule()

    private lateinit var getTicketListUseCase: GetTicketListUseCase
    private lateinit var topBotStatusUseCase: ChipTopBotStatusUseCase

    private lateinit var presenter: InboxListPresenter
    private lateinit var view: InboxListContract.InboxListView


    @Before
    @Throws(Exception::class)
    fun setUp() {
        MockKAnnotations.init(this)
        Dispatchers.setMain(TestCoroutineDispatcher())
        getTicketListUseCase = mockk(relaxed = true)
        topBotStatusUseCase = mockk(relaxed = true)
        presenter = spyk(InboxListPresenter(getTicketListUseCase, topBotStatusUseCase, mockk(relaxed = true)))
        view = mockk(relaxed = true)
        presenter.attachView(view)
    }


    @After
    @Throws(Exception::class)
    fun tearDown() {
        Dispatchers.resetMain()
    }

    /***********************************getTicketList()*********************************************/

    @Test
    fun `check invocation of showProgressBar on invocation of ticketList`() {
        coEvery { getTicketListUseCase.getTicketListResponse(any()) } returns mockk(relaxed = true)

        presenter.getTicketList(mockk(relaxed = true))

        verify { view.showProgressBar() }
    }

    @Test
    fun `check invocation of toggleEmptyLayout on invocation of ticketList`() {

        val response = mockk<InboxTicketListResponse>(relaxed = true)
        val tickets = listOf(InboxTicketListResponse.Ticket.Data.TicketItem(caseNumber = "caseNumber"))

        coEvery { getTicketListUseCase.getTicketListResponse(any()) } returns response

        every { response.ticket?.TicketData?.ticketItems } returns tickets

        presenter.getTicketList(mockk(relaxed = true))

        verify { view.toggleEmptyLayout(8) }
    }

    @Test
    fun `check invocation of renderTicketList on invocation of ticketList`() {

        val response = mockk<InboxTicketListResponse>(relaxed = true)
        val tickets = listOf(InboxTicketListResponse.Ticket.Data.TicketItem(caseNumber = "caseNumber"))


        coEvery { getTicketListUseCase.getTicketListResponse(any()) } returns response

        every { response.ticket?.TicketData?.ticketItems } returns tickets

        presenter.getTicketList(mockk(relaxed = true))

        verify { view.renderTicketList(any()) }
    }

    @Test
    fun `check invocation of showFilter on invocation of ticketList`() {

        val response = mockk<InboxTicketListResponse>(relaxed = true)
        val tickets = listOf(InboxTicketListResponse.Ticket.Data.TicketItem(caseNumber = "caseNumber"))

        coEvery { getTicketListUseCase.getTicketListResponse(any()) } returns response

        every { response.ticket?.TicketData?.ticketItems } returns tickets

        presenter.getTicketList(mockk(relaxed = true))

        verify { view.showFilter() }
    }

    @Test
    fun `check invocation of toggleEmptyLayout and showFilter on invocation of ticketList when fromFilter is true`() {

        val response = mockk<InboxTicketListResponse>(relaxed = true)
        val tickets = listOf<InboxTicketListResponse.Ticket.Data.TicketItem>()


        coEvery { getTicketListUseCase.getTicketListResponse(any()) } returns response

        every { response.ticket?.TicketData?.ticketItems } returns tickets

        presenter.fromFilter = true
        presenter.getTicketList(mockk(relaxed = true))

        verify { view.toggleEmptyLayout(View.VISIBLE) }

        verify { view.showFilter() }
    }

    @Test
    fun `check invocation of toggleNoTicketLayout and showFilter on invocation of ticketList for default case`() {

        val response = mockk<InboxTicketListResponse>(relaxed = true)
        val tickets = listOf<InboxTicketListResponse.Ticket.Data.TicketItem>()


        coEvery { getTicketListUseCase.getTicketListResponse(any()) } returns response
        every { response.ticket?.TicketData?.ticketItems } returns tickets

        presenter.getTicketList(mockk(relaxed = true))

        verify { view.toggleNoTicketLayout(View.VISIBLE, any()) }

        verify { view.hideFilter() }

    }

    /***************************************getTicketList()*****************************************/


    @Test
    fun `check getChatbotApplink`(){
        val response = ChipTopBotStatusResponse(ChipTopBotStatusResponse.ChipTopBotStatusInbox(ChipTopBotStatusResponse.ChipTopBotStatusInbox.ChipTopBotStatusData(messageId = "123", welcomeMessage = ""), messageError = null, status = ""))
        coEvery { view.getActivity().getString(any()) }  returns "applink prefix/%s"
        coEvery {  topBotStatusUseCase.getChipTopBotStatus()} returns response
        presenter.getTopBotStatus()
        val actual = presenter.getChatbotApplink()
        assertEquals("applink prefix/123", actual)

    }

    @Test
    fun `check getWelcomeMessage`(){
        mockkStatic(MethodChecker::class)
        every { MethodChecker.fromHtmlWithoutExtraSpace(any()) } returns "formatedHtml"
        val actual = presenter.getWelcomeMessage()
        assertEquals("formatedHtml", actual)

    }

    @Test
    fun `check getNotifiactionIndiactor`() {
        val response = ChipTopBotStatusResponse(ChipTopBotStatusResponse.ChipTopBotStatusInbox(ChipTopBotStatusResponse.ChipTopBotStatusInbox.ChipTopBotStatusData(messageId = "123", welcomeMessage = "", unreadNotif = true), messageError = null, status = ""))
        coEvery {  topBotStatusUseCase.getChipTopBotStatus()} returns response
        presenter.getTopBotStatus()
        val actual = presenter.getNotifiactionIndiactor()
        assertTrue(actual)

    }


    /******************************************setFilter()*****************************************/

    @Test
    fun `check fromFilter value on invocation of setFilter`() {

        every {
            presenter.getTicketList(mockk(relaxed = true))
        } just runs

        mockkStatic(ContactUsTracking::class)
        every {
            ContactUsTracking.sendGTMInboxTicket(any(), any(), any(), any(), any())
        } just runs

        presenter.setFilter(ALL)

        assertTrue(presenter.fromFilter)
    }

    @Test
    fun `check invocation getTicketList on invocation of setFilter when selection is IN_PROGRESS`() {

        every { presenter.getTicketList(mockk(relaxed = true)) } just runs

        mockkStatic(ContactUsTracking::class)
        every { ContactUsTracking.sendGTMInboxTicket(any(), any(), any(), any(), any()) } just runs

        presenter.setFilter(IN_PROGRESS)

        verify { presenter.getTicketList(any()) }
    }

    @Test
    fun `check invocation getTicketList on invocation of setFilter when selection is NEED_RATING`() {

        every { presenter.getTicketList(any()) } just runs

        mockkStatic(ContactUsTracking::class)
        every { ContactUsTracking.sendGTMInboxTicket(any(), any(), any(), any(), any()) } just runs

        presenter.setFilter(NEED_RATING)

        verify { presenter.getTicketList(any()) }
    }

    @Test
    fun `check invocation getTicketList on invocation of setFilter when selection is CLOSED`() {

        every { presenter.getTicketList(any()) } just runs

        mockkStatic(ContactUsTracking::class)
        every { ContactUsTracking.sendGTMInboxTicket(any(), any(), any(), any(), any()) } just runs

        presenter.setFilter(CLOSED)

        verify { presenter.getTicketList(any()) }
    }

    /******************************************setFilter()*****************************************/


    /*******************************************onClickFilter()************************************/

    @Test
    fun `check invocation of showBottomFragment on invocation of onClickFilter`() {
        presenter.onClickFilter()

        verify { view.showBottomFragment() }
    }

    /*******************************************onClickFilter()************************************/


    /***************************************scrollList()*******************************************/

    @Test
    fun `check invocation of scrollRv on invocation of scrollList`() {
        presenter.scrollList()

        verify { view.scrollRv() }
    }

    /***************************************scrollList()*******************************************/


    /***************************************onRecyclerViewScrolled()*******************************/

    @Test
    fun `check invocation of checkIfToLoad on invocation of onRecyclerViewScrolled`() {

        every { presenter.checkIfToLoad(any()) } just runs

        presenter.onRecyclerViewScrolled(mockk())

        verify { presenter.checkIfToLoad(any()) }
    }

    /***************************************onRecyclerViewScrolled()*******************************/


    /**************************************checkIfToLoad()*****************************************/

    @Test
    fun `check invocation of loadMoreItems on invocation of checkIfToLoad`() {

        val layoutManager = mockk<LinearLayoutManager>()

        every { layoutManager.childCount } returns 10
        every { layoutManager.itemCount } returns 10
        every { layoutManager.findFirstVisibleItemPosition() } returns 1

        presenter.isLoading = false
        presenter.isLastPage = false

        presenter.checkIfToLoad(layoutManager)

        verify { presenter.loadMoreItems(any()) }
    }

    /**************************************checkIfToLoad()*****************************************/


    /******************************************loadMoreItems()*************************************/

    @Test
    fun `check value of isLastPage on invocation of loadMoreItems when next page returns nonEmpty value`() {

        val response = mockk<InboxTicketListResponse>(relaxed = true)
        val tickets = listOf(InboxTicketListResponse.Ticket.Data.TicketItem())


        coEvery { getTicketListUseCase.getTicketListResponse(any()) } returns response

        every { response.ticket?.TicketData?.nextPage } returns "dummy url"
        every { response.ticket?.TicketData?.ticketItems } returns tickets

        presenter.loadMoreItems(1)

        assertFalse(presenter.isLastPage)

    }

    @Test
    fun `check value of isLastPage on invocation of loadMoreItems when next page returns Empty value`() {

        val response = mockk<InboxTicketListResponse>(relaxed = true)
        val tickets = listOf(InboxTicketListResponse.Ticket.Data.TicketItem())


        coEvery { getTicketListUseCase.getTicketListResponse(any()) } returns response

        every { response.ticket?.TicketData?.nextPage } returns ""
        every { response.ticket?.TicketData?.ticketItems } returns tickets

        presenter.loadMoreItems(1)

        assertTrue(presenter.isLastPage)

    }

    @Test
    fun `check invocation of updateDataSet on invocation of loadMoreItems`() {

        val response = mockk<InboxTicketListResponse>(relaxed = true)
        val tickets = listOf(InboxTicketListResponse.Ticket.Data.TicketItem())


        coEvery { getTicketListUseCase.getTicketListResponse(any()) } returns response

        every { response.ticket?.TicketData?.ticketItems } returns tickets

        presenter.loadMoreItems(1)

        verify { view.updateDataSet() }

    }

    @Test
    fun `check value of isLoading on invocation of loadMoreItems`() {

        val response = mockk<InboxTicketListResponse>(relaxed = true)
        val tickets = listOf(InboxTicketListResponse.Ticket.Data.TicketItem())


        coEvery { getTicketListUseCase.getTicketListResponse(any()) } returns response

        every { response.ticket?.TicketData?.ticketItems } returns tickets

        presenter.loadMoreItems(1)

        assertFalse(presenter.isLoading)

    }

    @Test
    fun `check invocation of removeFooter on invocation of loadMoreItems`() {

        val response = mockk<InboxTicketListResponse>(relaxed = true)
        val tickets = listOf(InboxTicketListResponse.Ticket.Data.TicketItem())


        coEvery { getTicketListUseCase.getTicketListResponse(any()) } returns response

        every { response.ticket?.TicketData?.ticketItems } returns tickets

        presenter.loadMoreItems(1)

        verify { view.removeFooter() }

    }

    /******************************************loadMoreItems()*************************************/


    /************************************toggleSearch()********************************************/

    @Test
    fun `check invocation toggleSearch on invocation of onSearchTextChanged`() {
        presenter.onSearchTextChanged("dummy")

        verify { view.toggleSearch(any()) }
    }

    /************************************toggleSearch()********************************************/


    /***********************************clickCloseSearch()*****************************************/

    @Test
    fun `check invocation toggleSearch on invocation of clickCloseSearch when is searchMode returns true`() {

        every { view.isSearchMode() } returns true

        presenter.clickCloseSearch()

        verify { view.toggleSearch(any()) }
    }

    @Test
    fun `check invocation clearSearch on invocation of clickCloseSearch when is searchMode returns false`() {

        every { view.isSearchMode() } returns false

        presenter.clickCloseSearch()

        verify { view.clearSearch() }
    }

    /***********************************clickCloseSearch()*****************************************/


    /**************************************reAttachView()******************************************/

    @Test
    fun `check reAttachView`() {

        every { presenter.getTicketList(any()) } just runs

        presenter.reAttachView()

        verify { presenter.getTicketList(any()) }
    }

    /**************************************reAttachView()******************************************/


}