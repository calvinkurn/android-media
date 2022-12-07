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
    private var viewNullable: InboxListContract.InboxListView?= null


    @Before
    @Throws(Exception::class)
    fun setUp() {
        MockKAnnotations.init(this)
        Dispatchers.setMain(TestCoroutineDispatcher())
        getTicketListUseCase = mockk(relaxed = true)
        topBotStatusUseCase = mockk(relaxed = true)
        presenter = spyk(InboxListPresenter(getTicketListUseCase, topBotStatusUseCase, mockk(relaxed = true)))
        view = mockk(relaxed = true)
        viewNullable = mockk(relaxed = true)
    }


    @After
    @Throws(Exception::class)
    fun tearDown() {
        Dispatchers.resetMain()
    }

    /***********************************getTicketList()*********************************************/

    @Test
    fun `check invocation of showProgressBar on invocation of ticketList`() {
        presenter.attachView(view)
        coEvery { getTicketListUseCase.getTicketListResponse(any()) } returns mockk(relaxed = true)

        presenter.getTicketList(mockk(relaxed = true))

        verify { view.showProgressBar() }
    }

    @Test
    fun `check invocation of toggleEmptyLayout on invocation of ticketList`() {
        presenter.attachView(view)
        val response = mockk<InboxTicketListResponse>(relaxed = true)
        val tickets = listOf(InboxTicketListResponse.Ticket.Data.TicketItem(caseNumber = "caseNumber"))

        coEvery { getTicketListUseCase.getTicketListResponse(any()) } returns response

        every { response.ticket?.TicketData?.ticketItems } returns tickets

        presenter.getTicketList(mockk(relaxed = true))

        verify { view.toggleEmptyLayout(8) }
    }

    @Test
    fun `check invocation of renderTicketList on invocation of ticketList`() {
        presenter.attachView(view)
        val response = mockk<InboxTicketListResponse>(relaxed = true)
        val tickets = listOf(InboxTicketListResponse.Ticket.Data.TicketItem(caseNumber = "caseNumber"))


        coEvery { getTicketListUseCase.getTicketListResponse(any()) } returns response

        every { response.ticket?.TicketData?.ticketItems } returns tickets

        presenter.getTicketList(mockk(relaxed = true))

        verify { view.renderTicketList(any()) }
    }

    @Test
    fun `check invocation of showFilter on invocation of ticketList`() {
        presenter.attachView(view)
        val response = mockk<InboxTicketListResponse>(relaxed = true)
        val tickets = listOf(InboxTicketListResponse.Ticket.Data.TicketItem(caseNumber = "caseNumber"))

        coEvery { getTicketListUseCase.getTicketListResponse(any()) } returns response

        every { response.ticket?.TicketData?.ticketItems } returns tickets

        presenter.getTicketList(mockk(relaxed = true))

        verify { view.showFilter() }
    }

    @Test
    fun `check invocation of toggleEmptyLayout and showFilter on invocation of ticketList when fromFilter is true`() {
        presenter.attachView(view)
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
    fun `check invocation of toggleEmptyLayout and showFilter on invocation of ticketList when fromFilter is true but view is null`() {
        val response = mockk<InboxTicketListResponse>(relaxed = true)
        val tickets = listOf<InboxTicketListResponse.Ticket.Data.TicketItem>()

        coEvery { getTicketListUseCase.getTicketListResponse(any()) } returns response

        every { response.ticket?.TicketData?.ticketItems } returns tickets

        presenter.fromFilter = true
        presenter.getTicketList(mockk(relaxed = true))

        verify(exactly = 0) { view.showProgressBar() }
        verify(exactly = 0) { view.toggleEmptyLayout(View.VISIBLE) }
        verify(exactly = 0) { viewNullable?.showFilter() }

    }

    @Test
    fun `check invocation of toggleNoTicketLayout and showFilter on invocation of ticketList for default case`() {
        presenter.attachView(view)
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
        presenter.attachView(view)
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
        presenter.attachView(view)
        val response = ChipTopBotStatusResponse(ChipTopBotStatusResponse.ChipTopBotStatusInbox(ChipTopBotStatusResponse.ChipTopBotStatusInbox.ChipTopBotStatusData(messageId = "123", welcomeMessage = "", unreadNotif = true), messageError = null, status = ""))
        coEvery {  topBotStatusUseCase.getChipTopBotStatus()} returns response
        presenter.getTopBotStatus()
        val actual = presenter.getNotifiactionIndiactor()
        assertTrue(actual)

    }


    /******************************************setFilter()*****************************************/

    @Test
    fun `check fromFilter value on invocation of setFilter`() {
        presenter.attachView(view)

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
        presenter.attachView(view)

        every { presenter.getTicketList(mockk(relaxed = true)) } just runs

        mockkStatic(ContactUsTracking::class)
        every { ContactUsTracking.sendGTMInboxTicket(any(), any(), any(), any(), any()) } just runs

        presenter.setFilter(IN_PROGRESS)

        verify { presenter.getTicketList(any()) }
    }

    @Test
    fun `check invocation getTicketList on invocation of setFilter when selection is NEED_RATING`() {
        presenter.attachView(view)

        every { presenter.getTicketList(any()) } just runs

        mockkStatic(ContactUsTracking::class)
        every { ContactUsTracking.sendGTMInboxTicket(any(), any(), any(), any(), any()) } just runs

        presenter.setFilter(NEED_RATING)

        verify { presenter.getTicketList(any()) }
    }

    @Test
    fun `check invocation getTicketList on invocation of setFilter when selection is CLOSED`() {
        presenter.attachView(view)

        every { presenter.getTicketList(any()) } just runs

        mockkStatic(ContactUsTracking::class)
        every { ContactUsTracking.sendGTMInboxTicket(any(), any(), any(), any(), any()) } just runs

        presenter.setFilter(CLOSED)

        verify { presenter.getTicketList(any()) }
    }

    @Test
    fun `check invocation getTicketList on invocation of setFilter when selection is CLOSED but view is null`() {

        every { presenter.getTicketList(any()) } just runs

        mockkStatic(ContactUsTracking::class)
        every { ContactUsTracking.sendGTMInboxTicket(any(), any(), any(), any(), any()) } just runs

        presenter.setFilter(CLOSED)

        verify { presenter.getTicketList(any()) }
        verify(exactly = 0) { viewNullable?.hideBottomFragment() }
    }

    /******************************************setFilter()*****************************************/


    /*******************************************onClickFilter()************************************/

    @Test
    fun `check invocation of showBottomFragment on invocation of onClickFilter`() {
        presenter.attachView(view)
        presenter.onClickFilter()

        verify { view.showBottomFragment() }
    }

    @Test
    fun `check invocation of showBottomFragment on invocation of onClickFilter but view is null`() {
        presenter.onClickFilter()

        verify(exactly = 0) { view.showBottomFragment() }
    }

    /*******************************************onClickFilter()************************************/


    /***************************************scrollList()*******************************************/

    @Test
    fun `check invocation of scrollRv on invocation of scrollList`() {
        presenter.attachView(view)
        presenter.scrollList()

        verify { view.scrollRv() }
    }

    @Test
    fun `check invocation of scrollRv when view is Null`() {
        presenter.scrollList()

        verify(exactly = 0) { viewNullable?.scrollRv() }
    }

    /***************************************scrollList()*******************************************/


    /***************************************onRecyclerViewScrolled()*******************************/

    @Test
    fun `check invocation of checkIfToLoad on invocation of onRecyclerViewScrolled`() {
        presenter.attachView(view)

        every { presenter.checkIfToLoad(any()) } just runs

        presenter.onRecyclerViewScrolled(mockk())

        verify { presenter.checkIfToLoad(any()) }
    }

    /***************************************onRecyclerViewScrolled()*******************************/


    /**************************************checkIfToLoad()*****************************************/

    @Test
    fun `check invocation of loadMoreItems on invocation of checkIfToLoad`() {
        presenter.attachView(view)

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
        presenter.attachView(view)

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
        presenter.attachView(view)

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
        presenter.attachView(view)

        val response = mockk<InboxTicketListResponse>(relaxed = true)
        val tickets = listOf(InboxTicketListResponse.Ticket.Data.TicketItem())


        coEvery { getTicketListUseCase.getTicketListResponse(any()) } returns response

        every { response.ticket?.TicketData?.ticketItems } returns tickets

        presenter.loadMoreItems(1)

        verify { view.updateDataSet() }

    }

    @Test
    fun `check value of isLoading on invocation of loadMoreItems`() {
        presenter.attachView(view)

        val response = mockk<InboxTicketListResponse>(relaxed = true)
        val tickets = listOf(InboxTicketListResponse.Ticket.Data.TicketItem())


        coEvery { getTicketListUseCase.getTicketListResponse(any()) } returns response

        every { response.ticket?.TicketData?.ticketItems } returns tickets

        presenter.loadMoreItems(1)

        assertFalse(presenter.isLoading)

    }

    @Test
    fun `check invocation of removeFooter on invocation of loadMoreItems`() {
        presenter.attachView(view)

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
        presenter.attachView(view)
        presenter.onSearchTextChanged("dummy")

        verify { view.toggleSearch(any()) }
    }

    @Test
    fun `check invocation toggleSearch on invocation of onSearchTextChanged but view is Null`() {
        presenter.onSearchTextChanged("dummy")

        verify(exactly = 0) { viewNullable?.toggleSearch(any()) }
    }

    @Test
    fun `check invocation empty String in toggleSearch on invocation of onSearchTextChanged but view is Null`() {
        presenter.onSearchTextChanged("")

        verify(exactly = 0) { viewNullable?.toggleSearch(any()) }
    }

    @Test
    fun `check invocation empty String in toggleSearch on invocation of onSearchTextChanged`() {
        presenter.attachView(view)
        presenter.onSearchTextChanged("")

        verify(exactly = 0) { view.toggleSearch(any()) }
    }

    /************************************toggleSearch()********************************************/


    /***********************************clickCloseSearch()*****************************************/

    @Test
    fun `check invocation toggleSearch on invocation of clickCloseSearch when is searchMode returns true`() {
        presenter.attachView(view)

        every { view.isSearchMode() } returns true

        presenter.clickCloseSearch()

        verify { view.toggleSearch(any()) }
    }

    @Test
    fun `check invocation clearSearch on invocation of clickCloseSearch when is searchMode returns false`() {
        presenter.attachView(view)

        every { view.isSearchMode() } returns false

        presenter.clickCloseSearch()

        verify { view.clearSearch() }
    }

     @Test
     fun `check when view is Null on clickCloseSearch and method view clearSearch not running`() {

         every { viewNullable?.isSearchMode() } returns null

         presenter.clickCloseSearch()

         verify(exactly = 0) { viewNullable?.toggleSearch(any()) }
         verify(exactly = 0) { viewNullable?.clearSearch() }
     }

    /***********************************clickCloseSearch()*****************************************/


    /**************************************reAttachView()******************************************/

    @Test
    fun `check reAttachView`() {
        presenter.attachView(view)

        every { presenter.getTicketList(any()) } just runs

        presenter.reAttachView()

        verify { presenter.getTicketList(any()) }
    }

    /**************************************reAttachView()******************************************/

    @Test
    fun `check get Ticket when view is Null`(){
        presenter.getTicketList(null)

        verify(exactly = 0) { viewNullable?.showProgressBar() }
    }

    @Test
    fun `check get Ticket when view is Not Null`(){
        presenter.attachView(view)
        presenter.getTicketList(null)

        verify{ view.showProgressBar() }
    }

    @Test
    fun `check get Ticket List when requestParams is null and all variable not null`(){
        presenter.attachView(view)
        val response = mockk<InboxTicketListResponse>(relaxed = true)
        val tickets = listOf(InboxTicketListResponse.Ticket.Data.TicketItem(caseNumber = "caseNumber"))

        coEvery { getTicketListUseCase.getTicketListResponse(any()) } returns response

        every { response.ticket?.TicketData?.ticketItems } returns tickets

        presenter.getTicketList(null)

        verify { view.showProgressBar() }
        verify { view.toggleEmptyLayout(View.GONE) }
        verify { view.renderTicketList(any()) }
        verify { view.showFilter() }
    }

    @Test
    fun `check get Ticket List when requestParams is null and view is Null but ticketItems is not null or Empty`(){
        val response = mockk<InboxTicketListResponse>(relaxed = true)
        val tickets = listOf(InboxTicketListResponse.Ticket.Data.TicketItem(caseNumber = "caseNumber"))

        coEvery { getTicketListUseCase.getTicketListResponse(any()) } returns response

        every { response.ticket?.TicketData?.ticketItems } returns tickets

        presenter.getTicketList(null)

        verify(exactly = 0) { viewNullable?.showProgressBar() }
        verify(exactly = 0) { viewNullable?.toggleEmptyLayout(View.GONE) }
        verify(exactly = 0) { viewNullable?.renderTicketList(any()) }
        verify(exactly = 0) { viewNullable?.showFilter() }
    }

    @Test
    fun `check get Ticket List when requestParams is null and TicketItem on Ticket Response is Null`(){
        presenter.attachView(view)
        val response = mockk<InboxTicketListResponse>(relaxed = true)

        coEvery { getTicketListUseCase.getTicketListResponse(any()) } returns response

        every { response.ticket } returns null

        presenter.getTicketList(null)

        verify { view.toggleNoTicketLayout(any(), any()) }
        verify { view.hideFilter() }
    }

    @Test
    fun `check get Ticket List when requestParams is null and TicketItem on Ticket Response is Null and view is null`(){
        val response = mockk<InboxTicketListResponse>(relaxed = true)

        coEvery { getTicketListUseCase.getTicketListResponse(any()) } returns response

        every { response.ticket } returns null

        presenter.getTicketList(null)

        verify(exactly = 0) { view.toggleNoTicketLayout(any(), any()) }
        verify(exactly = 0) { view.hideFilter() }
    }

    @Test
    fun `get Top Bot Status when chip Top Bot Status Data is true`(){
        presenter.attachView(view)
        val response = ChipTopBotStatusResponse(ChipTopBotStatusResponse.ChipTopBotStatusInbox(ChipTopBotStatusResponse.ChipTopBotStatusInbox.ChipTopBotStatusData(messageId = "123", welcomeMessage = "", isActive = true, isSuccess = 1), messageError = null, status = ""))
        coEvery {  topBotStatusUseCase.getChipTopBotStatus()} returns response
        presenter.getTopBotStatus()
        verify { view.showChatBotWidget() }
    }

    @Test
    fun `check when view is Null`(){
        val actual = presenter.getChatbotApplink()
        assertEquals("", actual)
    }

    @Test
    fun `check when view getActivity is Null`(){
        presenter.attachView(viewNullable)
        coEvery { viewNullable?.getActivity() } returns null
        val actual = presenter.getChatbotApplink()
        assertEquals("", actual)
    }

    @Test
    fun `check when view getString is Null`(){
        presenter.attachView(viewNullable)
        coEvery { viewNullable?.getActivity()?.getString(any()) } returns null
        val actual = presenter.getChatbotApplink()
        assertEquals("", actual)
    }

    @Test
    fun `check getChatbotApplink when ChipTopBotStatusResponse is Null `(){
        presenter.attachView(view)
        val response = ChipTopBotStatusResponse(null)
        coEvery { view.getActivity().getString(any()) }  returns "applink prefix/%s"
        coEvery {  topBotStatusUseCase.getChipTopBotStatus()} returns response
        presenter.getTopBotStatus()
        val actual = presenter.getChatbotApplink()
        assertEquals("applink prefix/null", actual)
    }

    @Test
    fun `check getChatbotApplink when ChipTopBotStatusInbox on ChipTopBotStatusResponse is Null `(){
        presenter.attachView(view)
        val response = ChipTopBotStatusResponse(ChipTopBotStatusResponse.ChipTopBotStatusInbox(null, messageError = null, status = ""))
        coEvery { view.getActivity().getString(any()) }  returns "applink prefix/%s"
        coEvery {  topBotStatusUseCase.getChipTopBotStatus()} returns response
        presenter.getTopBotStatus()
        val actual = presenter.getChatbotApplink()
        assertEquals("applink prefix/null", actual)
    }

    @Test
    fun `check Notification Indicator when not set Response is null`() {
        presenter.attachView(view)
        val actual = presenter.getNotifiactionIndiactor()
        assertFalse(actual)
    }

    @Test
    fun `check Notification Indicator when ChipTopBotStatusResponse is null`() {
        presenter.attachView(view)
        val response = ChipTopBotStatusResponse(null)
        coEvery {  topBotStatusUseCase.getChipTopBotStatus() } returns response
        presenter.getTopBotStatus()
        val actual = presenter.getNotifiactionIndiactor()
        assertFalse(actual)
    }

    @Test
    fun `check Notification Indicator when ChipTopBotStatusResponse contains null ChipTopBotStatusInbox`() {
        presenter.attachView(view)
        val response = ChipTopBotStatusResponse(ChipTopBotStatusResponse.ChipTopBotStatusInbox(null, messageError = null, status = ""))
        coEvery {  topBotStatusUseCase.getChipTopBotStatus()} returns response
        presenter.getTopBotStatus()
        val actual = presenter.getNotifiactionIndiactor()
        assertFalse(actual)
    }

    @Test
    fun `check Notification Indicator when ChipTopBotStatusResponse contains ChipTopBotStatusData,unreadNotif is false `() {
        presenter.attachView(view)
        val response = ChipTopBotStatusResponse(ChipTopBotStatusResponse.ChipTopBotStatusInbox(
            ChipTopBotStatusResponse.ChipTopBotStatusInbox.ChipTopBotStatusData(unreadNotif = false,messageId="", welcomeMessage =""), messageError = null, status = ""))
        coEvery {  topBotStatusUseCase.getChipTopBotStatus()} returns response
        presenter.getTopBotStatus()
        val actual = presenter.getNotifiactionIndiactor()
        assertFalse(actual)
    }

    @Test
    fun `check Notification Indicator when ChipTopBotStatusResponse contains ChipTopBotStatusData,unreadNotif is true `() {
        presenter.attachView(view)
        val response = ChipTopBotStatusResponse(ChipTopBotStatusResponse.ChipTopBotStatusInbox(
            ChipTopBotStatusResponse.ChipTopBotStatusInbox.ChipTopBotStatusData(unreadNotif = true,messageId="", welcomeMessage =""), messageError = null, status = ""))
        coEvery {  topBotStatusUseCase.getChipTopBotStatus()} returns response
        presenter.getTopBotStatus()
        val actual = presenter.getNotifiactionIndiactor()
        assertTrue(actual)
    }

    @Test
    fun `is refreshLayout running`(){
        presenter.attachView(view)
        presenter.refreshLayout()
    }

    @Test
    fun `is onSearchSubmitted running`(){
        presenter.attachView(view)
        presenter.onSearchSubmitted("")
    }

    @Test
    fun `is detachView running`(){
        presenter.attachView(view)
        presenter.detachView()
    }

    @Test
    fun `is onDestroy running`(){
        presenter.attachView(view)
        presenter.onDestroy()
    }

    @Test
    fun `is onActivityResult running but view is null`(){
        presenter.onActivityResult(InboxBaseContract.InboxBaseView.REQUEST_DETAILS, InboxBaseContract.InboxBaseView.RESULT_FINISH, mockk(relaxed = true))
        verify(exactly = 0) { view.getActivity().finish() }
    }

    @Test
    fun `is onActivityResult running in good mode when data is false`(){
        presenter.attachView(view)
        presenter.onActivityResult(InboxBaseContract.InboxBaseView.REQUEST_DETAILS,
            Activity.RESULT_CANCELED, mockk(relaxed = true))
    }

    @Test
    fun `is onActivityResult running in good mode when data is false2`(){
        presenter.attachView(view)
        presenter.onActivityResult(InboxBaseContract.InboxBaseView.RESULT_FINISH,
            Activity.RESULT_CANCELED, mockk(relaxed = true))
    }


    @Test
    fun `is showSerVicePriorityBottomSheet showing`(){
        presenter.attachView(view)
        presenter.showSerVicePriorityBottomSheet()
        verify { view.showSerVicePriorityBottomSheet() }
    }

    @Test
    fun `check showSerVicePriorityBottomSheet not show because view is Null`(){
        presenter.showSerVicePriorityBottomSheet()
        verify(exactly = 0) { view.showSerVicePriorityBottomSheet() }
    }

    @Test
    fun `getTopBotStatus running with view is null`() {
        val response = ChipTopBotStatusResponse(ChipTopBotStatusResponse.ChipTopBotStatusInbox(null, messageError = null, status = ""))
        coEvery {  topBotStatusUseCase.getChipTopBotStatus()} returns response
        presenter.getTopBotStatus()
        verify(exactly = 0) { view.hideChatBotWidget() }
    }

    @Test
    fun `getTopBotStatus when ChipTopBotStatusResponse is null`() {
        val response = ChipTopBotStatusResponse(null)
        coEvery {  topBotStatusUseCase.getChipTopBotStatus()} returns response
        presenter.getTopBotStatus()
        verify(exactly = 0) { view.hideChatBotWidget() }
    }

    @Test
    fun `getTopBotStatus when resposnse is Not Null but chipTopBotStatusData is null`() {
        val response = ChipTopBotStatusResponse(ChipTopBotStatusResponse.ChipTopBotStatusInbox(null, messageError = null, status = ""))
        coEvery {  topBotStatusUseCase.getChipTopBotStatus()} returns response
        presenter.getTopBotStatus()
        verify(exactly = 0) { view.hideChatBotWidget() }
    }

    @Test
    fun `getTopBotStatus when resposnse is Not Null but isSuccess on ChipTopBotStatusInbox is 0 and isActive is true`() {
        presenter.attachView(view)
        val response = ChipTopBotStatusResponse(ChipTopBotStatusResponse.ChipTopBotStatusInbox(
            ChipTopBotStatusResponse.ChipTopBotStatusInbox.ChipTopBotStatusData(messageId = "123", welcomeMessage = "", isActive = true, isSuccess = 0), messageError = null, status = ""))
        coEvery {  topBotStatusUseCase.getChipTopBotStatus()} returns response
        presenter.getTopBotStatus()
        verify{ view.hideChatBotWidget() }
    }

    @Test
    fun `getTopBotStatus when resposnse is null`() {
        presenter.attachView(view)
        presenter.getTopBotStatus()
        verify{ view.hideChatBotWidget() }
    }

    @Test
    fun `getTopBotStatus when resposnse is Not Null but isSuccess on ChipTopBotStatusInbox is 0 and isActive is true but view is null`() {
        val response = ChipTopBotStatusResponse(ChipTopBotStatusResponse.ChipTopBotStatusInbox(
            ChipTopBotStatusResponse.ChipTopBotStatusInbox.ChipTopBotStatusData(messageId = "123", welcomeMessage = "", isActive = true, isSuccess = 0), messageError = null, status = ""))
        coEvery {  topBotStatusUseCase.getChipTopBotStatus()} returns response
        presenter.getTopBotStatus()
        verify(exactly = 0){ view.hideChatBotWidget() }
    }

    @Test
    fun `getTopBotStatus when resposnse is Not Null but isSuccess on ChipTopBotStatusInbox is 1 and isActive is true but view is null`() {
        val response = ChipTopBotStatusResponse(ChipTopBotStatusResponse.ChipTopBotStatusInbox(
            ChipTopBotStatusResponse.ChipTopBotStatusInbox.ChipTopBotStatusData(messageId = "123", welcomeMessage = "", isActive = true, isSuccess = 1), messageError = null, status = ""))
        coEvery {  topBotStatusUseCase.getChipTopBotStatus()} returns response
        presenter.getTopBotStatus()
        verify(exactly = 0){ view.hideChatBotWidget() }
    }

}