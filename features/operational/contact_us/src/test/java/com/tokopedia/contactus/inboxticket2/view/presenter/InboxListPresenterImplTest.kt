package com.tokopedia.contactus.inboxticket2.view.presenter

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.contactus.createticket.widget.LinearLayoutManager
import com.tokopedia.contactus.common.analytics.ContactUsTracking
import com.tokopedia.contactus.inboxticket2.domain.TicketListResponse
import com.tokopedia.contactus.inboxticket2.domain.TicketsItem
import com.tokopedia.contactus.inboxticket2.domain.usecase.GetTicketListUseCase
import com.tokopedia.contactus.inboxticket2.view.contract.InboxListContract
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class InboxListPresenterImplTest {

    @get:Rule
    var rule = InstantTaskExecutorRule()

    private lateinit var getTicketListUseCase:GetTicketListUseCase

    private lateinit var presenter:InboxListPresenterImpl
    private lateinit var view:InboxListContract.InboxListView


    @Before
    @Throws(Exception::class)
    fun setUp() {
        MockKAnnotations.init(this)
        Dispatchers.setMain(TestCoroutineDispatcher())
        getTicketListUseCase = mockk(relaxed = true)
        presenter = spyk(InboxListPresenterImpl(getTicketListUseCase))
        view = mockk(relaxed = true)
        presenter.attachView(view)
    }


    @After
    @Throws(Exception::class)
    fun tearDown() {
        Dispatchers.resetMain()
    }

    /***************************************ticketList*********************************************/

    @Test
    fun `check invocation of showProgressBar on invocation of ticketList`() {
        coEvery { getTicketListUseCase.getTicketListResponse(any(), any()) } returns mockk(relaxed = true)

        presenter.ticketList

        verify { view.showProgressBar() }
    }

    @Test
    fun `check invocation of toggleEmptyLayout on invocation of ticketList`() {

        val response = mockk<TicketListResponse>(relaxed = true)
        val tickets = listOf(TicketsItem())

        coEvery { getTicketListUseCase.getTicketListResponse(any(), any()) } returns response

        every { response.tickets } returns tickets

        presenter.ticketList

        verify { view.toggleEmptyLayout(any()) }
    }

    @Test
    fun `check invocation of renderTicketList on invocation of ticketList`() {

        val response = mockk<TicketListResponse>(relaxed = true)
        val tickets = listOf(TicketsItem())

        coEvery { getTicketListUseCase.getTicketListResponse(any(), any()) } returns response

        every { response.tickets } returns tickets

        presenter.ticketList

        verify { view.renderTicketList(any()) }
    }

    @Test
    fun `check invocation of showFilter on invocation of ticketList`() {

        val response = mockk<TicketListResponse>(relaxed = true)
        val tickets = listOf(TicketsItem())

        coEvery { getTicketListUseCase.getTicketListResponse(any(), any()) } returns response

        every { response.tickets } returns tickets

        presenter.ticketList

        verify { view.showFilter() }
    }

    @Test
    fun `check invocation of toggleEmptyLayout and showFilter on invocation of ticketList when showFilter is true`() {

        val response = mockk<TicketListResponse>(relaxed = true)
        val tickets = listOf<TicketsItem>()

        coEvery { getTicketListUseCase.getTicketListResponse(any(), any()) } returns response

        every { response.tickets } returns tickets

        presenter.fromFilter = true
        presenter.ticketList

        verify { view.toggleEmptyLayout(any()) }

        every { view.showFilter() }
    }

    @Test
    fun `check invocation of toggleEmptyLayout and showFilter on invocation of ticketList for default case`() {

        val response = mockk<TicketListResponse>(relaxed = true)

        coEvery { getTicketListUseCase.getTicketListResponse(any(), any()) } returns response

        presenter.ticketList

        verify { view.toggleEmptyLayout(any()) }

        every { view.hideFilter() }

    }

    /***************************************ticketList*********************************************/


    /******************************************setFilter()*****************************************/

    @Test
    fun `check fromFilter value on invocation of setFilter`() {

        every {
            presenter.ticketList
        } just runs

        mockkStatic(ContactUsTracking::class)
        every {
            ContactUsTracking.sendGTMInboxTicket(any(), any(), any(), any())
        } just runs

        presenter.setFilter(ALL)

        assertTrue(presenter.fromFilter)
    }

    @Test
    fun `check invocation ticketList on invocation of setFilter when selection is UNREAD`() {

        every { presenter.ticketList } just runs

        mockkStatic(ContactUsTracking::class)
        every { ContactUsTracking.sendGTMInboxTicket(any(), any(), any(), any()) } just runs

        presenter.setFilter(UNREAD)

        verify { presenter.ticketList }
    }

    @Test
    fun `check invocation ticketList on invocation of setFilter when selection is NEEDRATING`() {

        every { presenter.ticketList } just runs

        mockkStatic(ContactUsTracking::class)
        every { ContactUsTracking.sendGTMInboxTicket(any(), any(), any(), any()) } just runs

        presenter.setFilter(NEEDRATING)

        verify { presenter.ticketList }
    }

    @Test
    fun `check invocation ticketList on invocation of setFilter when selection is INPROGRESS`() {

        every { presenter.ticketList } just runs

        mockkStatic(ContactUsTracking::class)
        every { ContactUsTracking.sendGTMInboxTicket(any(), any(), any(), any()) } just runs

        presenter.setFilter(INPROGRESS)

        verify { presenter.ticketList }
    }

    @Test
    fun `check invocation ticketList on invocation of setFilter when selection is READ`() {

        every { presenter.ticketList } just runs

        mockkStatic(ContactUsTracking::class)
        every { ContactUsTracking.sendGTMInboxTicket(any(), any(), any(), any()) } just runs

        presenter.setFilter(READ)

        verify { presenter.ticketList }
    }

    @Test
    fun `check invocation ticketList on invocation of setFilter when selection is CLOSED`() {

        every { presenter.ticketList } just runs

        mockkStatic(ContactUsTracking::class)
        every { ContactUsTracking.sendGTMInboxTicket(any(), any(), any(), any()) } just runs

        presenter.setFilter(CLOSED)

        verify { presenter.ticketList }
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

        verify { presenter.loadMoreItems() }
    }

    @Test
    fun `check invocation of addFooter on invocation of checkIfToLoad`() {

        val layoutManager = mockk<LinearLayoutManager>()

        every { layoutManager.childCount } returns 8
        every { layoutManager.itemCount } returns 10
        every { layoutManager.findFirstVisibleItemPosition() } returns 1

        presenter.isLoading = false
        presenter.isLastPage = false

        presenter.checkIfToLoad(layoutManager)

        verify { view.addFooter() }
    }

    @Test
    fun `check invocation of removeFooter on invocation of checkIfToLoad`() {

        val layoutManager = mockk<LinearLayoutManager>(relaxed = true)

        presenter.isLoading = true
        presenter.isLastPage = false

        presenter.checkIfToLoad(layoutManager)

        verify { view.removeFooter() }
    }

    /**************************************checkIfToLoad()*****************************************/


    /******************************************loadMoreItems()*************************************/

    @Test
    fun `check value of isLastPage on invocation of loadMoreItems when next page returns nonEmpty value`() {

        val response = mockk<TicketListResponse>()

        coEvery { getTicketListUseCase.getTicketListResponse(any(), any()) } returns response

        every { response.tickets } returns listOf(TicketsItem())

        every { response.nextPage } returns "dummy url"


        presenter.loadMoreItems()

        assertFalse(presenter.isLastPage)

    }

    @Test
    fun `check value of isLastPage on invocation of loadMoreItems when next page returns Empty value`() {

        val response = mockk<TicketListResponse>()

        coEvery { getTicketListUseCase.getTicketListResponse(any(), any()) } returns response

        every { response.tickets } returns listOf(TicketsItem())

        every { response.nextPage } returns ""

        presenter.loadMoreItems()

        assertTrue(presenter.isLastPage)

    }

    @Test
    fun `check invocation of updateDataSet on invocation of loadMoreItems`() {

        val response = mockk<TicketListResponse>(relaxed = true)

        coEvery { getTicketListUseCase.getTicketListResponse(any(), any()) } returns response

        every { response.tickets } returns listOf(TicketsItem())

        presenter.loadMoreItems()

        verify { view.updateDataSet() }

    }

    @Test
    fun `check value of isLoading on invocation of loadMoreItems`() {

        val response = mockk<TicketListResponse>(relaxed = true)

        coEvery { getTicketListUseCase.getTicketListResponse(any(), any()) } returns response

        every { response.tickets } returns listOf(TicketsItem())

        presenter.loadMoreItems()

        assertFalse(presenter.isLoading)

    }

    @Test
    fun `check invocation of removeFooter on invocation of loadMoreItems`() {

        val response = mockk<TicketListResponse>(relaxed = true)

        coEvery { getTicketListUseCase.getTicketListResponse(any(), any()) } returns response

        every { response.tickets } returns listOf(TicketsItem())

        presenter.loadMoreItems()

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

        every { presenter.ticketList } just runs

        presenter.reAttachView()

        verify { presenter.ticketList }
    }

    /**************************************reAttachView()******************************************/


}