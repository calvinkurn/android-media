package com.tokopedia.contactus.inboxticket2.view.presenter

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tkpd.library.ui.view.LinearLayoutManager
import com.tokopedia.contactus.common.analytics.ContactUsTracking
import com.tokopedia.contactus.inboxticket2.domain.TicketListResponse
import com.tokopedia.contactus.inboxticket2.domain.TicketsItem
import com.tokopedia.contactus.inboxticket2.domain.usecase.GetTicketListUseCase
import com.tokopedia.contactus.inboxticket2.view.activity.InboxDetailActivity
import com.tokopedia.contactus.inboxticket2.view.adapter.InboxFilterAdapter
import com.tokopedia.contactus.inboxticket2.view.contract.InboxListContract
import com.tokopedia.contactus.inboxticket2.view.fragment.InboxBottomSheetFragment
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

    private val getTicketListUseCase = mockk<GetTicketListUseCase>(relaxed = true)

    private val presenter = spyk(InboxListPresenterImpl(getTicketListUseCase))
    private val view = mockk<InboxListContract.InboxListView>(relaxed = true)


    @Before
    @Throws(Exception::class)
    fun setUp() {
        MockKAnnotations.init(this)
        Dispatchers.setMain(TestCoroutineDispatcher())
        presenter.attachView(view)
    }


    @After
    @Throws(Exception::class)
    fun tearDown() {
        Dispatchers.resetMain()
    }


    @Test
    fun `check invocation of showProgressBar on invocation of ticketList`() {
        coEvery {
            getTicketListUseCase.getTicketListResponse(any(), any())
        } returns mockk(relaxed = true)

        presenter.ticketList

        coVerify {
            view.showProgressBar()
        }
    }

    @Test
    fun `check invocation of toggleEmptyLayout on invocation of ticketList`() {

        val response = mockk<TicketListResponse>(relaxed = true)
        val tickets = listOf(TicketsItem())

        coEvery {
            getTicketListUseCase.getTicketListResponse(any(), any())
        } returns response

        coEvery {
            response.tickets
        } returns tickets

        presenter.ticketList

        coVerify {
            view.toggleEmptyLayout(any())
        }
    }

    @Test
    fun `check invocation of renderTicketList on invocation of ticketList`() {

        val response = mockk<TicketListResponse>(relaxed = true)
        val tickets = listOf(TicketsItem())

        coEvery {
            getTicketListUseCase.getTicketListResponse(any(), any())
        } returns response

        coEvery {
            response.tickets
        } returns tickets

        presenter.ticketList

        coVerify {
            view.renderTicketList(any())
        }
    }

    @Test
    fun `check invocation of showFilter on invocation of ticketList`() {

        val response = mockk<TicketListResponse>(relaxed = true)
        val tickets = listOf(TicketsItem())

        coEvery {
            getTicketListUseCase.getTicketListResponse(any(), any())
        } returns response

        coEvery {
            response.tickets
        } returns tickets

        presenter.ticketList

        coVerify {
            view.showFilter()
        }
    }

    @Test
    fun `check invocation of toggleEmptyLayout and showFilter on invocation of ticketList when showfilter is true`() {

        val response = mockk<TicketListResponse>(relaxed = true)
        val tickets = listOf<TicketsItem>()

        coEvery {
            getTicketListUseCase.getTicketListResponse(any(), any())
        } returns response

        coEvery {
            response.tickets
        } returns tickets

        presenter.fromFilter = true
        presenter.ticketList

        coVerify {
            view.toggleEmptyLayout(any())
        }

        coEvery {
            view.showFilter()
        }
    }

    @Test
    fun `check invocation of toggleEmptyLayout and showFilter on invocation of ticketList for defult case`() {

        val response = mockk<TicketListResponse>(relaxed = true)
        val tickets = listOf<TicketsItem>()

        coEvery {
            getTicketListUseCase.getTicketListResponse(any(), any())
        } returns response

        presenter.ticketList

        coVerify {
            view.toggleEmptyLayout(any())
        }

        coEvery {
            view.hideFilter()
        }
    }

    /**********************************************************************************************/
    @Test
    fun `check fromFilter value on invocation of setFilter`() {

        coEvery {
            presenter.ticketList
        } just runs

        mockkStatic(ContactUsTracking::class)
        coEvery {
            ContactUsTracking.sendGTMInboxTicket(any(), any(), any(), any())
        } just runs

        presenter.setFilter(ALL)

        assertTrue(presenter.fromFilter)
    }

    @Test
    fun `check invocation of setSelected on Invocation of setFilter when position is ALL`() {

        val filterAdapter = spyk(InboxFilterAdapter(listOf(), presenter))

        coEvery {
            presenter.ticketList
        } just runs

        mockkStatic(ContactUsTracking::class)
        coEvery {
            ContactUsTracking.sendGTMInboxTicket(any(), any(), any(), any())
        } just runs

        coEvery {
            filterAdapter.setSelected(any())
        } just runs

        presenter.setFilter(ALL)

        coVerify {
            filterAdapter.setSelected(any())
        }
    }


    @Test
    fun `check invocation ticketList on invocation of setFilter when selection is UNREAD`() {

        coEvery {
            presenter.ticketList
        } just runs

        mockkStatic(ContactUsTracking::class)
        coEvery {
            ContactUsTracking.sendGTMInboxTicket(any(), any(), any(), any())
        } just runs

        presenter.setFilter(UNREAD)

        coVerify {
            presenter.ticketList
        }
    }

    @Test
    fun `check invocation ticketList on invocation of setFilter when selection is NEEDRATING`() {

        coEvery {
            presenter.ticketList
        } just runs

        mockkStatic(ContactUsTracking::class)
        coEvery {
            ContactUsTracking.sendGTMInboxTicket(any(), any(), any(), any())
        } just runs

        presenter.setFilter(NEEDRATING)

        coVerify {
            presenter.ticketList
        }
    }

    @Test
    fun `check invocation ticketList on invocation of setFilter when selection is INPROGRESS`() {

        coEvery {
            presenter.ticketList
        } just runs

        mockkStatic(ContactUsTracking::class)
        coEvery {
            ContactUsTracking.sendGTMInboxTicket(any(), any(), any(), any())
        } just runs

        presenter.setFilter(INPROGRESS)

        coVerify {
            presenter.ticketList
        }
    }

    @Test
    fun `check invocation ticketList on invocation of setFilter when selection is READ`() {

        coEvery {
            presenter.ticketList
        } just runs

        mockkStatic(ContactUsTracking::class)
        coEvery {
            ContactUsTracking.sendGTMInboxTicket(any(), any(), any(), any())
        } just runs

        presenter.setFilter(READ)

        coVerify {
            presenter.ticketList
        }
    }

    @Test
    fun `check invocation ticketList on invocation of setFilter when selection is CLOSED`() {

        coEvery {
            presenter.ticketList
        } just runs

        mockkStatic(ContactUsTracking::class)
        coEvery {
            ContactUsTracking.sendGTMInboxTicket(any(), any(), any(), any())
        } just runs

        presenter.setFilter(CLOSED)

        coVerify {
            presenter.ticketList
        }
    }

    /**********************************************************************************************/

    @Test
    fun `check invocation of showBottomFragment on invocation of onClickFilter`() {
        presenter.onClickFilter()

        coVerify {
            view.showBottomFragment()
        }
    }

    /**********************************************************************************************/

    @Test
    fun `check invocation of scrollRv on invocation of scrollList`() {
        presenter.scrollList()

        coVerify {
            view.scrollRv()
        }
    }

    /**********************************************************************************************/

    @Test
    fun `check invocation of checkIfToLoad on invocation of onRecyclerViewScrolled`() {


        coEvery {
            presenter.checkIfToLoad(any())
        } just runs

        presenter.onRecyclerViewScrolled(mockk())

        coVerify {
            presenter.checkIfToLoad(any())
        }
    }

    /**********************************************************************************************/

    @Test
    fun `check invocation of loadMoreItems on invocation of checkIfToLoad`() {

        val layoutManager = mockk<LinearLayoutManager>()

        coEvery {
            layoutManager.childCount
        } returns 10
        coEvery {
            layoutManager.itemCount
        } returns 10
        coEvery {
            layoutManager.findFirstVisibleItemPosition()
        } returns 1

        presenter.isLoading = false
        presenter.isLastPage = false

        presenter.checkIfToLoad(layoutManager)

        coVerify {
            presenter.loadMoreItems()
        }
    }

    @Test
    fun `check invocation of addFooter on invocation of checkIfToLoad`() {

        val layoutManager = mockk<LinearLayoutManager>()

        coEvery {
            layoutManager.childCount
        } returns 8
        coEvery {
            layoutManager.itemCount
        } returns 10
        coEvery {
            layoutManager.findFirstVisibleItemPosition()
        } returns 1

        presenter.isLoading = false
        presenter.isLastPage = false

        presenter.checkIfToLoad(layoutManager)

        coVerify {
            view.addFooter()
        }
    }

    @Test
    fun `check invocation of removeFooter on invocation of checkIfToLoad`() {

        val layoutManager = mockk<LinearLayoutManager>(relaxed = true)

        presenter.isLoading = true
        presenter.isLastPage = false

        presenter.checkIfToLoad(layoutManager)

        coVerify {
            view.removeFooter()
        }
    }

    /**********************************************************************************************/

    @Test
    fun `check value of isLastPage on invocation of loadMoreItems when next page returns nonEmpty value`() {

        val response = mockk<TicketListResponse>()

        coEvery {
            getTicketListUseCase.getTicketListResponse(any(), any())
        } returns response

        coEvery {
            response.tickets
        } returns listOf(TicketsItem())

        coEvery {
            response.nextPage
        } returns "dummy url"


        presenter.loadMoreItems()

        assertFalse(presenter.isLastPage)

    }

    @Test
    fun `check value of isLastPage on invocation of loadMoreItems when next page returns Empty value`() {

        val response = mockk<TicketListResponse>()

        coEvery {
            getTicketListUseCase.getTicketListResponse(any(), any())
        } returns response

        coEvery {
            response.tickets
        } returns listOf(TicketsItem())

        coEvery {
            response.nextPage
        } returns ""


        presenter.loadMoreItems()

        assertTrue(presenter.isLastPage)

    }

    @Test
    fun `check invocation of updateDataSet on invocation of loadMoreItems`() {

        val response = mockk<TicketListResponse>(relaxed = true)

        coEvery {
            getTicketListUseCase.getTicketListResponse(any(), any())
        } returns response

        coEvery {
            response.tickets
        } returns listOf(TicketsItem())


        presenter.loadMoreItems()

        coVerify {
            view.updateDataSet()
        }

    }

    @Test
    fun `check value of isLoading on invocation of loadMoreItems`() {

        val response = mockk<TicketListResponse>(relaxed = true)

        coEvery {
            getTicketListUseCase.getTicketListResponse(any(), any())
        } returns response

        coEvery {
            response.tickets
        } returns listOf(TicketsItem())


        presenter.loadMoreItems()

        assertFalse(presenter.isLoading)

    }

    @Test
    fun `check invocation of removeFooter on invocation of loadMoreItems`() {

        val response = mockk<TicketListResponse>(relaxed = true)

        coEvery {
            getTicketListUseCase.getTicketListResponse(any(), any())
        } returns response

        coEvery {
            response.tickets
        } returns listOf(TicketsItem())


        presenter.loadMoreItems()

        coVerify {
            view.removeFooter()
        }

    }

    /**********************************************************************************************/

    @Test
    fun `check invocation toggleSearch on invocation of onSearchTextChanged`() {
        presenter.onSearchTextChanged("dummy")

        coVerify {
            view.toggleSearch(any())
        }
    }

    /**********************************************************************************************/

    @Test
    fun `check invocation toggleSearch on invocation of clickCloseSearch when is searchMode returns true`() {

        coEvery {
            view.isSearchMode()
        } returns true

        presenter.clickCloseSearch()

        coVerify {
            view.toggleSearch(any())
        }
    }

    @Test
    fun `check invocation clearSearch on invocation of clickCloseSearch when is searchMode returns false`() {

        coEvery {
            view.isSearchMode()
        } returns false

        presenter.clickCloseSearch()

        coVerify {
            view.clearSearch()
        }
    }

    /**********************************************************************************************/

    @Test
    fun `check reAttachView`() {

        presenter.reAttachView()

        coEvery {
            presenter.ticketList
        } returns mockk()

        coVerify {
            presenter.ticketList
        }
    }

    /**********************************************************************************************/

    @Test
    fun `check getBottomFragment`() {

        val fragment = mockk<InboxBottomSheetFragment>()

        mockkStatic(InboxBottomSheetFragment::class)
        coEvery {
            InboxBottomSheetFragment.getBottomSheetFragment(any())
        } returns fragment

        coEvery {
            fragment.setAdapter(any())
        } returns mockk()

        presenter.getBottomFragment(mockk())

        coVerify {
            fragment.setAdapter(any())
        }
    }

    /**********************************************************************************************/

    @Test
    fun `check onClickTicket`() {

        mockkStatic(InboxDetailActivity::class)
        coEvery {
            InboxDetailActivity.getIntent(mockk(), "", false)
        } returns mockk()

        mockkStatic(ContactUsTracking::class)
        coEvery {
            ContactUsTracking.sendGTMInboxTicket(any(), any(), any(), any())
        }returns  mockk()

        presenter.onClickTicket(1, false)

        coVerify {
            view.navigateToActivityRequest(any(), any())
        }
    }

}