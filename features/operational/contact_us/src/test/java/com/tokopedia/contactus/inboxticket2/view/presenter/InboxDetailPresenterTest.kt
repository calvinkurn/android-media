package com.tokopedia.contactus.inboxticket2.view.presenter

import android.app.Activity
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.contactus.common.analytics.ContactUsTracking
import com.tokopedia.contactus.common.analytics.InboxTicketTracking
import com.tokopedia.contactus.inboxticket2.data.ImageUpload
import com.tokopedia.contactus.inboxticket2.data.model.ChipGetInboxDetail
import com.tokopedia.contactus.inboxticket2.data.model.Data
import com.tokopedia.contactus.inboxticket2.data.model.TicketReplyResponse
import com.tokopedia.contactus.inboxticket2.data.model.Tickets
import com.tokopedia.contactus.inboxticket2.domain.AttachmentItem
import com.tokopedia.contactus.inboxticket2.domain.CommentsItem
import com.tokopedia.contactus.inboxticket2.domain.StepTwoResponse
import com.tokopedia.contactus.inboxticket2.domain.usecase.*
import com.tokopedia.contactus.inboxticket2.view.activity.ContactUsProvideRatingActivity
import com.tokopedia.contactus.inboxticket2.view.contract.InboxBaseContract
import com.tokopedia.contactus.inboxticket2.view.contract.InboxDetailContract
import com.tokopedia.contactus.inboxticket2.view.presenter.InboxDetailPresenter.Companion.KEY_LIKED
import com.tokopedia.contactus.inboxticket2.view.utils.Utils
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.jvm.Throws
import com.tokopedia.unit.test.rule.CoroutineTestRule

@ExperimentalCoroutinesApi
class InboxDetailPresenterTest {

    @get:Rule
    var rule = InstantTaskExecutorRule()

    @get:Rule
    val testRule = CoroutineTestRule()

    private lateinit var postMessageUseCase: PostMessageUseCase
    private lateinit var postMessageUseCase2: PostMessageUseCase2
    private lateinit var inboxOptionUseCase: InboxOptionUseCase
    private lateinit var postRatingUseCase: PostRatingUseCase
    private lateinit var submitRatingUseCase: SubmitRatingUseCase
    private lateinit var closeTicketByUserUseCase: CloseTicketByUserUseCase
    private lateinit var uploadImageUseCase: ContactUsUploadImageUseCase
    private lateinit var userSession: UserSessionInterface

    private lateinit var presenter: InboxDetailPresenter

    private lateinit var view: InboxDetailContract.InboxDetailView


    @Before
    @Throws(Exception::class)
    fun setUp() {
        MockKAnnotations.init(this)
        postMessageUseCase = mockk(relaxed = true)
        postMessageUseCase2 = mockk(relaxed = true)
        inboxOptionUseCase = mockk(relaxed = true)
        postRatingUseCase = mockk(relaxed = true)
        submitRatingUseCase = mockk(relaxed = true)
        closeTicketByUserUseCase = mockk(relaxed = true)
        uploadImageUseCase = mockk(relaxed = true)
        userSession = mockk(relaxed = true)

        presenter = spyk(InboxDetailPresenter(postMessageUseCase,
                postMessageUseCase2,
                postRatingUseCase,
                inboxOptionUseCase,
                submitRatingUseCase,
                closeTicketByUserUseCase,
                uploadImageUseCase, userSession, testRule.dispatchers))

        view = mockk(relaxed = true)
        presenter.attachView(view)
    }


    @After
    @Throws(Exception::class)
    fun tearDown() {
        Dispatchers.resetMain()
    }


    /*************************************getTicketDetails()***************************************/

    @Test
    fun `check getTicketDetails`() {

        coEvery { inboxOptionUseCase.getChipInboxDetail(any()) } returns mockk(relaxed = true)

        presenter.getTicketDetails("")

        coVerify { view.showProgressBar() }

    }

    @Test
    fun `check invocation of renderMessageList on invocation of getTicketDetails when success`() {

        val response = mockk<ChipGetInboxDetail>(relaxed = true)
        val responseData = mockk<Data>(relaxed = true)
        val utils = mockk<Utils>(relaxed = true)
        val tickets = mockk<Tickets>(relaxed = true)

        coEvery { inboxOptionUseCase.getChipInboxDetail(any()) } returns response

        every { response.data } returns responseData

        every { responseData.isSuccess } returns 1

        every { responseData.tickets } returns tickets

        mockkStatic(Utils::class)
        every { presenter.getUtils() } returns utils

        presenter.getTicketDetails("")

        verify { view.renderMessageList(any()) }

    }

    @Test
    fun `check invocation of getCommentsWithTopItem on invocation of getTicketDetails when success and comments empty`() {

        val response = mockk<ChipGetInboxDetail>(relaxed = true)
        val responseData = mockk<Data>(relaxed = true)
        val utils = mockk<Utils>(relaxed = true)
        val tickets = Tickets()
        tickets.message = "top item"

        val slot = slot<Tickets>()
        var mTicketDetail: Tickets? = null

        tickets.comments = mutableListOf()

        coEvery { inboxOptionUseCase.getChipInboxDetail(any()) } returns response

        every { response.data } returns responseData

        every { responseData.isSuccess } returns 1

        every { responseData.tickets } returns tickets

        mockkStatic(Utils::class)
        coEvery { presenter.getUtils() } returns utils

        every { view.renderMessageList(capture(slot)) } answers { mTicketDetail = slot.captured }

        presenter.getTicketDetails("")

        assertEquals(mTicketDetail?.message, "top item")

    }

    @Test
    fun `check invocation of getShortTime on invocation of getTicketDetails when success and comments create time not empty`() {

        val response = mockk<ChipGetInboxDetail>(relaxed = true)
        val responseData = mockk<Data>(relaxed = true)
        val utils = mockk<Utils>(relaxed = true)
        val tickets = Tickets()
        tickets.comments = mutableListOf(CommentsItem(createTime = ""))

        val slot = slot<Tickets>()
        var mTicketDetail: Tickets? = null

        coEvery { inboxOptionUseCase.getChipInboxDetail(any()) } returns response

        every { response.data } returns responseData

        every { responseData.isSuccess } returns 1

        every { responseData.tickets } returns tickets

        mockkStatic(Utils::class)
        every { presenter.getUtils() } returns utils

        every { utils.getDateTime(any()) } returns "2 jan 1995"

        every { view.renderMessageList(capture(slot)) } answers { mTicketDetail = slot.captured }

        presenter.getTicketDetails("")

        assertEquals(mTicketDetail?.comments?.get(2)?.shortTime, "2 jan")

    }

    @Test
    fun `check invocation of hideProgressBar on invocation of getTicketDetails when success`() {

        val response = mockk<ChipGetInboxDetail>()
        val responseData = mockk<Data>()
        val utils = mockk<Utils>(relaxed = true)

        coEvery { inboxOptionUseCase.getChipInboxDetail(any()) } returns response

        every { response.data } returns responseData

        every { responseData.isSuccess } returns 1

        every { responseData.tickets } returns mockk(relaxed = true)

        mockkStatic(Utils::class)
        every { presenter.getUtils() } returns utils

        presenter.getTicketDetails("")

        verify { view.hideProgressBar() }

    }

    @Test
    fun `check invocation of showNoTicketView on invocation of getTicketDetails when not success`() {

        val response = mockk<ChipGetInboxDetail>(relaxed = true)
        val responseData = mockk<Data>()

        coEvery { inboxOptionUseCase.getChipInboxDetail(any()) } returns response

        every { response.data } returns responseData

        every { responseData.isSuccess } returns 0

        presenter.getTicketDetails("")

        verify { view.showNoTicketView(any()) }

    }

    /*************************************getTicketDetails()***************************************/


    /**************************************onSearchSubmitted()*************************************/

    @Test
    fun `check onSearchSubmitted`() {

        mockkStatic(ContactUsTracking::class)
        every { ContactUsTracking.sendGTMInboxTicket(any(), any(), any(), any(), any()) } just runs

        presenter.onSearchSubmitted("dummy")

        verify { view.showProgressBar() }

    }

    @Test
    fun `check invocation of initializeIndicesList on invocation onSearchSubmitted`() {

        val ticketDetail = Tickets()
        val commentsItem = CommentsItem()
        commentsItem.messagePlaintext = "a string with dummy text"
        ticketDetail.comments = mutableListOf(commentsItem)

        val utils = mockk<Utils>(relaxed = true)

        mockkStatic(ContactUsTracking::class)
        every { ContactUsTracking.sendGTMInboxTicket(any(), any(), any(), any(), any()) } just runs

        every { presenter.mTicketDetail } returns ticketDetail

        every { presenter.getUtils() } returns utils

        every { utils.containsIgnoreCase(any(), any()) } returns true

        presenter.onSearchSubmitted("dummy")

        assertEquals(presenter.searchIndices.size, 1)

    }

    @Test
    fun `check invocation of hideProgressBar on invocation onSearchSubmitted`() {

        mockkStatic(ContactUsTracking::class)
        every { ContactUsTracking.sendGTMInboxTicket(any(), any(), any(), any(), any()) } just runs

        presenter.onSearchSubmitted("dummy")

        verify { view.hideProgressBar() }
    }

    @Test
    fun `check invocation of enterSearchMode on invocation onSearchSubmitted`() {

        mockkStatic(ContactUsTracking::class)
        every { ContactUsTracking.sendGTMInboxTicket(any(), any(), any(), any(), any()) } just runs

        presenter.onSearchSubmitted("dummy")

        verify { view.enterSearchMode(any(), any()) }
    }

    @Test
    fun `check invocation of sendGTMInboxTicket on invocation onSearchSubmitted when searchIndices size greater than 0`() {

        val ticketDetail = Tickets()
        val commentsItem = CommentsItem()
        commentsItem.messagePlaintext = "a string with dummy text"
        ticketDetail.comments = mutableListOf(commentsItem)

        val utils = mockk<Utils>(relaxed = true)

        mockkStatic(ContactUsTracking::class)
        every { ContactUsTracking.sendGTMInboxTicket(any(), any(), any(), any(), any()) } just runs

        every { presenter.mTicketDetail } returns ticketDetail

        every { presenter.getUtils() } returns utils

        every { utils.containsIgnoreCase(any(), any()) } returns true

        presenter.onSearchSubmitted("dummy")

        verify { ContactUsTracking.sendGTMInboxTicket(any(), any(), any(), any(), any()) }

    }

    @Test
    fun `check invocation of setSnackBarErrorMessage on invocation onSearchSubmitted when searchIndices size is 0`() {

        mockkStatic(ContactUsTracking::class)
        every { ContactUsTracking.sendGTMInboxTicket(any(), any(), any(), any(), any()) } just runs

        presenter.onSearchSubmitted("dummy")

        verify { view.setSnackBarErrorMessage(any(), false) }

    }

    @Test
    fun `check invocation of sendGTMInboxTicket on invocation onSearchSubmitted when searchIndices size is 0`() {

        mockkStatic(ContactUsTracking::class)
        every { ContactUsTracking.sendGTMInboxTicket(any(), any(), any(), any(), any()) } just runs

        presenter.onSearchSubmitted("dummy")

        verify {
            ContactUsTracking.sendGTMInboxTicket(any(), any(), any(), any(), InboxTicketTracking.Label.NoResult)
        }

    }

    @Test
    fun `check invocation of hideProgressBar on invocation onSearchSubmitted when exception in search method`() {

        runBlockingTest {
            mockkStatic(ContactUsTracking::class)
            every { ContactUsTracking.sendGTMInboxTicket(any(), any(), any(), any(), any()) } just runs

            every { view.enterSearchMode(any(), any()) } throws Exception("getting exception")

            presenter.onSearchSubmitted("dummy")

            verify { view.hideProgressBar() }

        }
    }

    @Test
    fun `check search is not invoked  on invocation onSearchSubmitted text is empty`() {

        presenter.onSearchSubmitted("")

        verify(exactly = 0) { view.showProgressBar() }
    }

    /**************************************onSearchSubmitted()*************************************/


    /**************************************onSearchTextChanged()***********************************/

    @Test
    fun `check invocation of onSearchSubmitted on invocation of onSearchTextChanged`() {

        presenter.onSearchTextChanged("")

        verify { presenter.onSearchSubmitted(any()) }

    }

    /**************************************onSearchTextChanged()***********************************/


    /*****************************************refreshLayout()**************************************/

    @Test
    fun `check invocation of getTicketDetails on invocation of refreshLayout`() {

        presenter.refreshLayout()

        verify { presenter.getTicketDetails(any()) }

    }

    /*****************************************refreshLayout()**************************************/


    /*************************************onActivityResult()***************************************/

    @Test
    fun `check invocation of submitCsatRating on invocation of onActivityResult`() {

        every { submitRatingUseCase.createRequestParams(any(), any(), any()) } returns mockk(relaxed = true)

        coEvery { submitRatingUseCase.getChipInboxDetail(any()) } returns mockk(relaxed = true)

        presenter.onActivityResult(InboxBaseContract.InboxBaseView.REQUEST_SUBMIT_FEEDBACK, Activity.RESULT_OK, mockk(relaxed = true))

        verify { view.showProgressBar() }

    }


    @Test
    fun `check invocation of showErrorMessage on invocation of onActivityResult`() {
        val chipGetInboxDetail = mockk<ChipGetInboxDetail>(relaxed = true)

        every { submitRatingUseCase.createRequestParams(any(), any(), any()) } returns mockk(relaxed = true)

        coEvery { submitRatingUseCase.getChipInboxDetail(any()) } returns chipGetInboxDetail

        every { chipGetInboxDetail.messageError } returns listOf("error")

        presenter.onActivityResult(InboxBaseContract.InboxBaseView.REQUEST_SUBMIT_FEEDBACK, Activity.RESULT_OK, mockk(relaxed = true))

        verify { view.showErrorMessage(any()) }

    }

    @Test
    fun `check invocation of showMessage on invocation of onActivityResult`() {
        val chipGetInboxDetail = mockk<ChipGetInboxDetail>(relaxed = true)

        every { submitRatingUseCase.createRequestParams(any(), any(), any()) } returns mockk(relaxed = true)

        coEvery { submitRatingUseCase.getChipInboxDetail(any()) } returns chipGetInboxDetail

        every { chipGetInboxDetail.messageError } returns listOf()

        presenter.onActivityResult(InboxBaseContract.InboxBaseView.REQUEST_SUBMIT_FEEDBACK, Activity.RESULT_OK, mockk(relaxed = true))

        verify { view.showMessage(any()) }

    }

    @Test
    fun `check invocation of showIssueClosed on invocation of onActivityResult`() {
        val chipGetInboxDetail = mockk<ChipGetInboxDetail>(relaxed = true)

        every { submitRatingUseCase.createRequestParams(any(), any(), any()) } returns mockk(relaxed = true)

        coEvery { submitRatingUseCase.getChipInboxDetail(any()) } returns chipGetInboxDetail

        every { chipGetInboxDetail.messageError } returns listOf()

        presenter.onActivityResult(InboxBaseContract.InboxBaseView.REQUEST_SUBMIT_FEEDBACK, Activity.RESULT_OK, mockk(relaxed = true))

        verify { view.showIssueClosed() }

    }

//    @Test
//    fun `check invocation of getTicketDetails on invocation of onActivityResult`() {
//        runBlockingTest {
//            val chipGetInboxDetail = mockk<ChipGetInboxDetail>(relaxed = true)
//
//            every { submitRatingUseCase.createRequestParams(any(), any(), any()) } returns mockk(relaxed = true)
//
//            coEvery { submitRatingUseCase.getChipInboxDetail(any()) } returns chipGetInboxDetail
//
//            every { chipGetInboxDetail.messageError } returns listOf()
//
//            every { presenter.getTicketDetails(any()) } just runs
//
//            presenter.onActivityResult(InboxBaseContract.InboxBaseView.REQUEST_SUBMIT_FEEDBACK, Activity.RESULT_OK, mockk(relaxed = true))
//
//            verify { presenter.getTicketDetails(any()) }
//        }
//
//    }

    @Test
    fun `check invocation of hideProgressBar on invocation of onActivityResult with exception`() {

        every { submitRatingUseCase.createRequestParams(any(), any(), any()) } returns mockk(relaxed = true)

        coEvery { submitRatingUseCase.getChipInboxDetail(any()) } throws Exception("my exception")

        presenter.onActivityResult(InboxBaseContract.InboxBaseView.REQUEST_SUBMIT_FEEDBACK, Activity.RESULT_OK, mockk(relaxed = true))

        verify { view.hideProgressBar() }

    }

    /*************************************onActivityResult()***************************************/


    /****************************************onImageSelect()***************************************/

    @Test
    fun `check invocation of setSnackBarErrorMessage on invocation onImageSelect and fileSize is not valid`() {
        val utils = mockk<Utils>()
        every { presenter.getUtils() } returns utils

        every { utils.fileSizeValid(any()) } returns false

        mockkStatic(ContactUsTracking::class)
        every { ContactUsTracking.sendGTMInboxTicket(any(), any(), any(), any(), any()) } just runs

        presenter.onImageSelect(mockk(relaxed = true))

        verify { view.setSnackBarErrorMessage(any(), true) }
    }

    @Test
    fun `check invocation of setSnackBarErrorMessage on invocation onImageSelect and bitmapDimen is not valid`() {

        val utils = mockk<Utils>()
        every { presenter.getUtils() } returns utils

        every { utils.fileSizeValid(any()) } returns true

        every { utils.isBitmapDimenValid(any()) } returns false

        mockkStatic(ContactUsTracking::class)
        every { ContactUsTracking.sendGTMInboxTicket(any(), any(), any(), any(), any()) } just runs

        presenter.onImageSelect(mockk(relaxed = true))

        verify { view.setSnackBarErrorMessage(any(), true) }
    }

    @Test
    fun `check invocation of addImage on invocation onImageSelect and bitmapDimen and fileSize is valid`() {

        val utils = mockk<Utils>()
        every { presenter.getUtils() } returns utils

        every { utils.fileSizeValid(any()) } returns true

        every { utils.isBitmapDimenValid(any()) } returns true

        mockkStatic(ContactUsTracking::class)
        every { ContactUsTracking.sendGTMInboxTicket(any(), any(), any(), any(), any()) } just runs

        presenter.onImageSelect(mockk(relaxed = true))

        verify { view.addImage(any()) }
    }

    /****************************************onImageSelect()***************************************/


    /**************************************isUploadImageValid**************************************/

    @Test
    fun `check invocation of setSnackBarErrorMessage on invocation of isUploadImageValid when imageList is empty and attachment required`() {

        val mTicketDetail = mockk<Tickets>(relaxed = true)

        every { presenter.mTicketDetail } returns mTicketDetail

        every { mTicketDetail.isNeedAttachment } returns true

        every { view.imageList } returns listOf()

        mockkStatic(ContactUsTracking::class)
        every { ContactUsTracking.sendGTMInboxTicket(any(), any(), any(), any(), any()) } just runs

        presenter.isUploadImageValid

        verify { view.setSnackBarErrorMessage(any(), true) }

    }

    @Test
    fun `check invocation of hideSendProgress on invocation of isUploadImageValid when imageList is empty and attachment required`() {

        val mTicketDetail = mockk<Tickets>(relaxed = true)

        every { presenter.mTicketDetail } returns mTicketDetail

        every { mTicketDetail.isNeedAttachment } returns true

        every { view.imageList } returns listOf()

        mockkStatic(ContactUsTracking::class)
        every { ContactUsTracking.sendGTMInboxTicket(any(), any(), any(), any(), any()) } just runs

        presenter.isUploadImageValid

        verify { view.hideSendProgress() }

    }

    @Test
    fun `check value of isUploadImageValid when imageList is empty and attachment required`() {

        val mTicketDetail = mockk<Tickets>(relaxed = true)

        every { presenter.mTicketDetail } returns mTicketDetail

        every { mTicketDetail.isNeedAttachment } returns true

        every { view.imageList } returns listOf()

        mockkStatic(ContactUsTracking::class)
        every { ContactUsTracking.sendGTMInboxTicket(any(), any(), any(), any(), any()) } just runs

        presenter.isUploadImageValid

        assertEquals(presenter.isUploadImageValid, -2)

    }

    @Test
    fun `check value of isUploadImageValid when imageList is not empty`() {

        val utils = mockk<Utils>(relaxed = true)

        every { view.imageList } returns listOf(ImageUpload(), ImageUpload())

        every { presenter.getUtils() } returns utils

        every { utils.verifyAllImages(any()) } returns 2

        presenter.isUploadImageValid

        assertEquals(presenter.isUploadImageValid, 2)

    }

    @Test
    fun `check value of isUploadImageValid when imageList is not empty and image is not valid`() {

        val utils = mockk<Utils>(relaxed = true)

        every { view.imageList } returns listOf(ImageUpload(), ImageUpload())

        every { presenter.getUtils() } returns utils

        every { utils.verifyAllImages(any()) } returns 1

        val result = presenter.isUploadImageValid

        assertEquals(result, -1)

    }

    @Test
    fun `check value of isUploadImageValid when imageList is empty`() {

        every { view.imageList } returns listOf()

        presenter.isUploadImageValid

        assertEquals(presenter.isUploadImageValid, 0)

    }

    /**************************************isUploadImageValid**************************************/


    /****************************************sendMessage()*****************************************/

    @Test
    fun `check invocation of hideSendProgress and setSnackBarErrorMessage on invocation of sendMessage when isUploadImageValid equals to 0`() {

        val response = mockk<TicketReplyResponse>(relaxed = true)

        every { presenter.isUploadImageValid } returns 0

        coEvery { postMessageUseCase.getCreateTicketResult(any()) } returns response

        every { response.ticketReply?.ticketReplyData?.status } returns ""

        presenter.sendMessage()

        verify { view.hideSendProgress() }

        verify { view.setSnackBarErrorMessage(any(), true) }

    }

    @Test
    fun `check updation of comment in Ticket on invocation of sendMessage when isUploadImageValid equals to 0`() {

        val response = mockk<TicketReplyResponse>(relaxed = true)
        val tickets = Tickets()
        tickets.comments = mutableListOf()

        val slot = slot<CommentsItem>()
        var actual = ""

        every { presenter.isUploadImageValid } returns 0

        coEvery { postMessageUseCase.getCreateTicketResult(any()) } returns response

        every { response.ticketReply?.ticketReplyData?.status } returns "OK"

        every { presenter.mTicketDetail } returns tickets

        every { view.imageList } returns listOf()

        every { view.userMessage } returns "message"

        every { view.updateAddComment(capture(slot)) } answers { actual = slot.captured.message?:""}

        presenter.sendMessage()

        assertEquals("message", actual)
        verify { view.updateAddComment(any()) }

    }

    @Test
    fun `check invocation of hideSendProgress invocation of sendMessage when isUploadImageValid equals to 0 throws exception`() {

        val tickets = Tickets()
        tickets.comments = mutableListOf()

        every { presenter.isUploadImageValid } returns 0

        coEvery { postMessageUseCase.getCreateTicketResult(any()) } throws Exception("my exception")

        presenter.sendMessage()

        verify { view.hideSendProgress() }

    }

    @Test
    fun `check invocation of setSnackBarErrorMessage invocation of sendMessage when isUploadImageValid returns -1`() {

        every { presenter.isUploadImageValid } returns -1

        presenter.sendMessage()

        verify { view.setSnackBarErrorMessage(any(), true) }

    }

    @Test
    fun `check invocation of sendMessageWithImages on invocation of sendMessage when isUploadImageValid returns 1 or more than 1 and response return status OK`() {

        val response = TicketReplyResponse(TicketReplyResponse.TicketReply(TicketReplyResponse.TicketReply.Data(status = "OK", postKey = "key")))

        val utils = mockk<Utils>(relaxed = true)

        every { presenter.isUploadImageValid } returns 2


        every { uploadImageUseCase.getFile(any()) } returns mockk()

        coEvery { uploadImageUseCase.uploadFile(any(), any(), any()) } returns listOf()

        every { presenter.getUtils().getAttachmentAsString(any()) } returns ""

        every { postMessageUseCase.createRequestParams(any(), any(), any(), any(), any(), any()) } returns mockk()

        coEvery { postMessageUseCase2.getInboxDataResponse(any()) } returns null

        coEvery { postMessageUseCase.getCreateTicketResult(any()) } returns response

        every { presenter.getUtils() } returns utils

        every { utils.getFileUploaded(any()) } returns ""

        presenter.sendMessage()

        verify { view.hideSendProgress() }

        verify { view.setSnackBarErrorMessage(any(), true) }

    }

    @Test
    fun `check invocation of sendMessageWithImages on invocation of sendMessage when isUploadImageValid returns 1 or more than 1 and response return status OK and postKey empty`() {

        val response = TicketReplyResponse(TicketReplyResponse.TicketReply(TicketReplyResponse.TicketReply.Data(status = "OK", postKey = "")))

        val utils = mockk<Utils>(relaxed = true)

        every { presenter.isUploadImageValid } returns 2


        every { uploadImageUseCase.getFile(any()) } returns mockk()

        coEvery { uploadImageUseCase.uploadFile(any(), any(), any()) } returns listOf()

        every { presenter.getUtils().getAttachmentAsString(any()) } returns ""

        every { postMessageUseCase.createRequestParams(any(), any(), any(), any(), any(), any()) } returns mockk()

        coEvery { postMessageUseCase2.getInboxDataResponse(any()) } returns null

        coEvery { postMessageUseCase.getCreateTicketResult(any()) } returns response

        every { presenter.getUtils() } returns utils

        every { utils.getFileUploaded(any()) } returns ""

        presenter.sendMessage()

        verify { view.hideSendProgress() }

        verify { view.setSnackBarErrorMessage(any(), true) }

    }

    /*
        check invocation of sendMessageWithImages on invocation of
        sendMessage when isUploadImageValid
        returns 1 or more than 1 and response return status OK and
        postKey notEmpty and responsetwo return success value 0
    */
    @Test
    fun `invocation of sendImages`() {

        val response = TicketReplyResponse(TicketReplyResponse.TicketReply(TicketReplyResponse.TicketReply.Data(status = "OK", postKey = "key")))
        val response2 = StepTwoResponse(StepTwoResponse.TicketReplyAttach(StepTwoResponse.TicketReplyAttach.TicketReplyAttachData(isSuccess = 0)))

        val utils = mockk<Utils>(relaxed = true)

        every { presenter.isUploadImageValid } returns 2


        every { uploadImageUseCase.getFile(any()) } returns mockk()

        coEvery { uploadImageUseCase.uploadFile(any(), any(), any()) } returns listOf()

        every { presenter.getUtils().getAttachmentAsString(any()) } returns ""

        every { postMessageUseCase.createRequestParams(any(), any(), any(), any(), any(), any()) } returns mockk()

        coEvery { postMessageUseCase2.getInboxDataResponse(any()) } returns response2

        coEvery { postMessageUseCase.getCreateTicketResult(any()) } returns response

        every { presenter.getUtils() } returns utils

        every { utils.getFileUploaded(any()) } returns ""

        presenter.sendMessage()

        verify { view.hideSendProgress() }

        verify { view.setSnackBarErrorMessage(any(), true) }

    }

    /*
       check invocation of sendMessageWithImages on invocation of
       sendMessage when isUploadImageValid
       returns 1 or more than 1 and response return status OK and
       postKey notEmpty and responsetwo throws Exception
       */

    @Test
    fun `invocation of sendImages with exception`() {

        val response = TicketReplyResponse(TicketReplyResponse.TicketReply(TicketReplyResponse.TicketReply.Data(status = "OK", postKey = "key")))
        val utils = mockk<Utils>(relaxed = true)

        every { presenter.isUploadImageValid } returns 2


        every { uploadImageUseCase.getFile(any()) } returns mockk()

        coEvery { uploadImageUseCase.uploadFile(any(), any(), any()) } returns listOf()

        every { presenter.getUtils().getAttachmentAsString(any()) } returns ""

        every { postMessageUseCase.createRequestParams(any(), any(), any(), any(), any(), any()) } returns mockk()

        coEvery { postMessageUseCase2.getInboxDataResponse(any()) } throws java.lang.Exception("my exception")

        coEvery { postMessageUseCase.getCreateTicketResult(any()) } returns response

        every { presenter.getUtils() } returns utils

        every { utils.getFileUploaded(any()) } returns ""

        presenter.sendMessage()

        verify { view.hideSendProgress() }

        verify { view.setSnackBarErrorMessage(any(), true) }

    }

    /*
      check invocation of sendMessageWithImages on invocation of
      sendMessage when isUploadImageValid
      returns 1 or more than 1 and response return status OK and
      postKey notEmpty and responsetwo return success value more than 0
     */

    @Test
    fun `invocation of addNewLocalComment`() {

        val response = TicketReplyResponse(TicketReplyResponse.TicketReply(TicketReplyResponse.TicketReply.Data(status = "OK", postKey = "key")))
        val response2 = StepTwoResponse(StepTwoResponse.TicketReplyAttach(StepTwoResponse.TicketReplyAttach.TicketReplyAttachData(isSuccess = 1)))

        val utils = mockk<Utils>(relaxed = true)
        val slot = slot<CommentsItem>()
        var actual = ""

        every { presenter.isUploadImageValid } returns 2


        every { uploadImageUseCase.getFile(any()) } returns mockk()

        coEvery { uploadImageUseCase.uploadFile(any(), any(), any()) } returns listOf()

        every { presenter.getUtils().getAttachmentAsString(any()) } returns ""

        every { postMessageUseCase.createRequestParams(any(), any(), any(), any(), any(), any()) } returns mockk()

        coEvery { postMessageUseCase2.getInboxDataResponse(any()) } returns response2

        coEvery { postMessageUseCase.getCreateTicketResult(any()) } returns response

        every { presenter.getUtils() } returns utils

        every { utils.getFileUploaded(any()) } returns ""

        every { view.imageList } returns listOf(ImageUpload(fileLoc = "fileLoc"))

        every { view.userMessage } returns "message"

        every { view.updateAddComment(capture(slot)) } answers { actual = slot.captured.attachment?.getOrNull(0)?.thumbnail?:""}

        presenter.sendMessage()

        presenter.sendMessage()

        assertEquals("fileLoc", actual)
        verify { view.updateAddComment(any()) }
        verify { view.hideSendProgress() }

    }
/*
      check invocation of sendMessageWithImages on invocation of
      sendMessage when isUploadImageValid
      returns 1 or more than 1 and response throws exception
     */

    @Test
    fun `invocation of sendMessageWithImages with exception`() {


        val utils = mockk<Utils>(relaxed = true)

        every { presenter.isUploadImageValid } returns 2


        every { uploadImageUseCase.getFile(any()) } returns mockk()

        coEvery { uploadImageUseCase.uploadFile(any(), any(), any()) } returns listOf()

        every { presenter.getUtils().getAttachmentAsString(any()) } returns ""

        every { postMessageUseCase.createRequestParams(any(), any(), any(), any(), any(), any()) } returns mockk()

        coEvery { postMessageUseCase.getCreateTicketResult(any()) } throws java.lang.Exception("my exception")

        every { presenter.getUtils() } returns utils

        every { utils.getFileUploaded(any()) } returns ""

        presenter.sendMessage()

        verify { view.hideSendProgress() }

        verify { view.setSnackBarErrorMessage(any(), true) }

    }

    /****************************************sendMessage()*****************************************/


    /***************************************showImagePreview()*************************************/

    @Test
    fun `check invocation of showImagePreview on invocation of showImagePreview`() {

        val attachmentItem = AttachmentItem()
        attachmentItem.url = "dummy url"

        mockkStatic(ContactUsTracking::class)
        every {
            ContactUsTracking.sendGTMInboxTicket(any(), any(), any(), any(), any())
        } just runs

        presenter.showImagePreview(0, listOf(attachmentItem))

        verify { view.showImagePreview(0, any()) }

    }

    /***************************************showImagePreview()*************************************/


    /****************************************getNextResult()***************************************/

    @Test
    fun `check value of searchIndices on invocation of getNextResult`() {

        every { presenter.searchIndices } returns arrayListOf(1, 2)

        val result = presenter.getNextResult()

        assertEquals(result, 2)
        assertEquals(presenter.searchIndices[1], 2)
        verify { view.setCurrentRes(2) }

    }

    @Test
    fun `check invocation of showMessage on invocation of getNextResult when searchIndices size is 1 `() {

        every { presenter.searchIndices } returns arrayListOf(1)
        every { presenter.next } returns 0

        val result = presenter.getNextResult()

        verify { view.showMessage(any()) }
        assertEquals(result, -1)

    }

    @Test
    fun `check invocation of showMessage on invocation of getNextResult when searchIndices size is 0`() {

        every { presenter.searchIndices } returns arrayListOf()

        val result = presenter.getNextResult()

        verify { view.showMessage(any()) }
        assertEquals(result, -1)

    }

    /****************************************getNextResult()***************************************/


    /*************************************getPreviousResult()**************************************/

    @Test
    fun `check value of searchIndices on invocation of getPreviousResult`() {

        every { presenter.searchIndices } returns arrayListOf(1, 2)
        every { presenter.next } returns 1

        val result = presenter.getPreviousResult()

        assertEquals(result, 2)
        assertEquals(presenter.searchIndices[0], 1)
        verify { view.setCurrentRes(2) }

    }

    @Test
    fun `check invocation of showMessage on invocation of getPreviousResult when searchIndices size is 1 `() {

        every { presenter.searchIndices } returns arrayListOf(1)
        every { presenter.next } returns 1

        val result = presenter.getPreviousResult()

        verify { view.showMessage(any()) }
        assertEquals(result, -1)

    }

    @Test
    fun `check invocation of showMessage on invocation of getPreviousResult when searchIndices size is 0`() {

        every { presenter.searchIndices } returns arrayListOf()

        val result = presenter.getPreviousResult()

        verify { view.showMessage(any()) }
        assertEquals(result, -1)

    }

    /*************************************getPreviousResult()**************************************/


    /*****************************************showImagePreview()***********************************/

//    @Test
//    fun `check invocation of onClickEmoji on invocation of showImagePreview`() {
//
//        mockkStatic(ContactUsTracking::class)
//        every {
//            ContactUsTracking.sendGTMInboxTicket(any(), any(), any(), any())
//        } just runs
//
//        mockkStatic(ContactUsProvideRatingActivity::class)
//        every {
//            ContactUsProvideRatingActivity.getInstance(any(), 0, any(), any())
//        } returns mockk()
//
//        every { view.startActivityForResult(any(), InboxBaseContract.InboxBaseView.REQUEST_SUBMIT_FEEDBACK) } just Runs
//
//        presenter.onClickEmoji(0)
//
//        verify { view.startActivityForResult(any(), InboxBaseContract.InboxBaseView.REQUEST_SUBMIT_FEEDBACK) }
//
//    }

    /*****************************************showImagePreview()***********************************/


    /****************************************onClick()*********************************************/

    @Test
    fun `check invocation of showErrorMessage on invocation of onClick`() {

        val response = mockk<ChipGetInboxDetail>(relaxed = true)

        coEvery { submitRatingUseCase.getChipInboxDetail(any()) } returns response
        coEvery { response.messageError } returns listOf("error")

        presenter.onClick(true, 0, "dummy id")

        verify { view.hideProgressBar() }
        verify { view.showErrorMessage("error") }

    }

    @Test
    fun `check invocation of onSuccessSubmitOfRating on invocation of onClick`() {

        val response = mockk<ChipGetInboxDetail>(relaxed = true)

        coEvery { submitRatingUseCase.getChipInboxDetail(any()) } returns response

        presenter.onClick(true, 0, "dummy id")

        verify { view.hideProgressBar() }
        verify { view.onSuccessSubmitOfRating(KEY_LIKED, 0) }

    }

    @Test
    fun `check invocation of hideProgressBar on invocation of onClick when exception`() {

        coEvery { submitRatingUseCase.getChipInboxDetail(any()) } throws Exception("my exception")

        presenter.onClick(true, 0, "dummy id")

        verify { view.hideProgressBar() }
    }

    /****************************************onClick()*********************************************/


    /***************************************closeTicket()******************************************/

    @Test
    fun `check invocation of showErrorMessage on invocation of closeTicket`() {

        val response = mockk<ChipGetInboxDetail>(relaxed = true)

        coEvery { closeTicketByUserUseCase.getChipInboxDetail(any()) } returns response
        every { response.messageError } returns listOf("error")

        presenter.closeTicket()

        verify { view.showErrorMessage("error") }

    }

    @Test
    fun `check invocation of OnSucessfullTicketClose on invocation of closeTicket`() {

        val response = mockk<ChipGetInboxDetail>(relaxed = true)

        coEvery { closeTicketByUserUseCase.getChipInboxDetail(any()) } returns response
        every { response.messageError } returns listOf()

        presenter.closeTicket()

        verify { view.OnSucessfullTicketClose() }

    }

    @Test
    fun `check invocation of hideProgressBar on invocation of closeTicket`() {

        coEvery { closeTicketByUserUseCase.getChipInboxDetail(any()) } throws Exception("my exception")

        presenter.closeTicket()

        verify { view.hideProgressBar() }

    }

    /***************************************closeTicket()******************************************/


    /******************************************getTicketStatus()***********************************/

    @Test
    fun `check value of getTicketStatus`() {

        val tickets = Tickets()
        tickets.status = "status"

        every { presenter.mTicketDetail } returns tickets

        val status = presenter.getTicketStatus()

        assertEquals(status, "status")
    }

    /******************************************getTicketStatus()***********************************/

    @Test
    fun `check getUserId`(){
        every { userSession.userId } returns "123"

        val actual = presenter.getUserId()

        assertEquals("123", actual)
    }

}