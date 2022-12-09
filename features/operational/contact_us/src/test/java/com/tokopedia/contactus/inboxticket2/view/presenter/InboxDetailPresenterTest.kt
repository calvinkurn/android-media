package com.tokopedia.contactus.inboxticket2.view.presenter

import android.app.Activity
import android.content.Intent
import android.text.TextWatcher
import android.view.MenuItem
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.contactus.R
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
import com.tokopedia.contactus.inboxticket2.view.contract.InboxBaseContract
import com.tokopedia.contactus.inboxticket2.view.contract.InboxDetailContract
import com.tokopedia.contactus.inboxticket2.view.presenter.InboxDetailPresenter.Companion.KEY_LIKED
import com.tokopedia.contactus.inboxticket2.view.utils.Utils
import com.tokopedia.unit.test.rule.CoroutineTestRule
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test


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
    private lateinit var chipUploadHostConfigUseCase: ChipUploadHostConfigUseCase
    private lateinit var secureUploadUseCase: SecureUploadUseCase
    private lateinit var userSession: UserSessionInterface

    private lateinit var presenter: InboxDetailPresenter

    private lateinit var view: InboxDetailContract.InboxDetailView
    private var viewNullable: InboxDetailContract.InboxDetailView? = null


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
        chipUploadHostConfigUseCase = mockk(relaxed = true)
        secureUploadUseCase = mockk(relaxed = true)
        userSession = mockk(relaxed = true)

        presenter = spyk(
            InboxDetailPresenter(
                postMessageUseCase,
                postMessageUseCase2,
                postRatingUseCase,
                inboxOptionUseCase,
                submitRatingUseCase,
                closeTicketByUserUseCase,
                uploadImageUseCase,
                chipUploadHostConfigUseCase,
                secureUploadUseCase,
                userSession, testRule.dispatchers
            )
        )

        view = mockk(relaxed = true)
        viewNullable = mockk(relaxed = true)
    }


    @After
    @Throws(Exception::class)
    fun tearDown() {
        Dispatchers.resetMain()
    }


    /*************************************getTicketDetails()***************************************/

    @Test
    fun `check getTicketDetails`() {

        presenter.attachView(view)

        coEvery { inboxOptionUseCase.getChipInboxDetail(any()) } returns mockk(relaxed = true)

        presenter.getTicketDetails("")

        coVerify { view.showProgressBar() }

    }

    @Test
    fun `check invocation of renderMessageList on invocation of getTicketDetails when success`() {
        presenter.attachView(view)
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
        presenter.attachView(view)
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
        presenter.attachView(view)
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
        presenter.attachView(view)
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
        presenter.attachView(view)
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
        presenter.attachView(view)
        mockkStatic(ContactUsTracking::class)
        every { ContactUsTracking.sendGTMInboxTicket(any(), any(), any(), any(), any()) } just runs

        presenter.onSearchSubmitted("dummy")

        verify { view.showProgressBar() }

    }

    @Test
    fun `check invocation of initializeIndicesList on invocation onSearchSubmitted`() {
        presenter.attachView(view)
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
        presenter.attachView(view)
        mockkStatic(ContactUsTracking::class)
        every { ContactUsTracking.sendGTMInboxTicket(any(), any(), any(), any(), any()) } just runs

        presenter.onSearchSubmitted("dummy")

        verify { view.hideProgressBar() }
    }

    @Test
    fun `check invocation of enterSearchMode on invocation onSearchSubmitted`() {
        presenter.attachView(view)
        mockkStatic(ContactUsTracking::class)
        every { ContactUsTracking.sendGTMInboxTicket(any(), any(), any(), any(), any()) } just runs

        presenter.onSearchSubmitted("dummy")

        verify { view.enterSearchMode(any(), any()) }
    }

    @Test
    fun `check invocation of sendGTMInboxTicket on invocation onSearchSubmitted when searchIndices size greater than 0`() {
        presenter.attachView(view)
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
        presenter.attachView(view)
        mockkStatic(ContactUsTracking::class)
        every { ContactUsTracking.sendGTMInboxTicket(any(), any(), any(), any(), any()) } just runs

        presenter.onSearchSubmitted("dummy")

        verify { view.setSnackBarErrorMessage(any(), false) }

    }

    @Test
    fun `check invocation of sendGTMInboxTicket on invocation onSearchSubmitted when searchIndices size is 0`() {
        presenter.attachView(view)
        mockkStatic(ContactUsTracking::class)
        every { ContactUsTracking.sendGTMInboxTicket(any(), any(), any(), any(), any()) } just runs

        presenter.onSearchSubmitted("dummy")

        verify {
            ContactUsTracking.sendGTMInboxTicket(
                any(),
                any(),
                any(),
                any(),
                InboxTicketTracking.Label.NoResult
            )
        }

    }

    @Test
    fun `check invocation of hideProgressBar on invocation onSearchSubmitted when exception in search method`() {
        presenter.attachView(view)
        runBlockingTest {
            mockkStatic(ContactUsTracking::class)
            every {
                ContactUsTracking.sendGTMInboxTicket(
                    any(),
                    any(),
                    any(),
                    any(),
                    any()
                )
            } just runs

            every { view.enterSearchMode(any(), any()) } throws Exception("getting exception")

            presenter.onSearchSubmitted("dummy")

            verify { view.hideProgressBar() }

        }
    }

    @Test
    fun `check search is not invoked  on invocation onSearchSubmitted text is empty`() {
        presenter.attachView(view)
        presenter.onSearchSubmitted("")

        verify(exactly = 0) { view.showProgressBar() }
    }

    /**************************************onSearchSubmitted()*************************************/


    /**************************************onSearchTextChanged()***********************************/

    @Test
    fun `check invocation of onSearchSubmitted on invocation of onSearchTextChanged`() {
        presenter.attachView(view)
        presenter.onSearchTextChanged("")

        verify { presenter.onSearchSubmitted(any()) }

    }

    /**************************************onSearchTextChanged()***********************************/


    /*****************************************refreshLayout()**************************************/

    @Test
    fun `check invocation of getTicketDetails on invocation of refreshLayout`() {
        presenter.attachView(view)
        presenter.refreshLayout()

        verify { presenter.getTicketDetails(any()) }

    }

    /*****************************************refreshLayout()**************************************/


    /*************************************onActivityResult()***************************************/

    @Test
    fun `check invocation of submitCsatRating on invocation of onActivityResult`() {
        presenter.attachView(view)
        every {
            submitRatingUseCase.createRequestParams(
                any(),
                any(),
                any()
            )
        } returns mockk(relaxed = true)

        coEvery { submitRatingUseCase.getChipInboxDetail(any()) } returns mockk(relaxed = true)

        presenter.onActivityResult(
            InboxBaseContract.InboxBaseView.REQUEST_SUBMIT_FEEDBACK,
            Activity.RESULT_OK,
            mockk(relaxed = true)
        )

        verify { view.showProgressBar() }

    }

    @Test
    fun `check invocation of submitCsatRating on invocation of onActivityResult but data is Null`() {
        presenter.attachView(view)
        every {
            submitRatingUseCase.createRequestParams(
                any(),
                any(),
                any()
            )
        } returns mockk(relaxed = true)

        coEvery { submitRatingUseCase.getChipInboxDetail(any()) } returns mockk(relaxed = true)

        presenter.onActivityResult(
            InboxBaseContract.InboxBaseView.REQUEST_SUBMIT_FEEDBACK,
            Activity.RESULT_OK,
            null
        )

        verify { view.showProgressBar() }

    }

    @Test
    fun `check invocation of submitCsatRating on invocation of onActivityResult but data is Null and view is null`() {
        every {
            submitRatingUseCase.createRequestParams(
                any(),
                any(),
                any()
            )
        } returns mockk(relaxed = true)

        coEvery { submitRatingUseCase.getChipInboxDetail(any()) } returns mockk(relaxed = true)

        presenter.onActivityResult(
            InboxBaseContract.InboxBaseView.REQUEST_SUBMIT_FEEDBACK,
            Activity.RESULT_OK,
            null
        )

        verify(exactly = 0) { view.showProgressBar() }
    }

    @Test
    fun `check invocation of submitCsatRating on invocation getString return emptyString`() {
        presenter.attachView(viewNullable)
        every {
            submitRatingUseCase.createRequestParams(
                any(),
                any(),
                any()
            )
        } returns mockk(relaxed = true)

        coEvery { submitRatingUseCase.getChipInboxDetail(any()) } returns mockk(relaxed = true)

        every { viewNullable?.getActivity()?.getString(any()) } returns null

        mockkStatic(ContactUsTracking::class)
        every {
            ContactUsTracking.sendGTMInboxTicket(any(), any(), any(), any(), any())
        } just runs

        presenter.onActivityResult(
            InboxBaseContract.InboxBaseView.REQUEST_SUBMIT_FEEDBACK,
            Activity.RESULT_OK,
            null
        )

        verify { viewNullable?.showMessage("") }
    }

    @Test
    fun `check invocation of showErrorMessage on invocation of onActivityResult`() {
        presenter.attachView(view)
        val chipGetInboxDetail = mockk<ChipGetInboxDetail>(relaxed = true)

        every {
            submitRatingUseCase.createRequestParams(
                any(),
                any(),
                any()
            )
        } returns mockk(relaxed = true)

        coEvery { submitRatingUseCase.getChipInboxDetail(any()) } returns chipGetInboxDetail

        every { chipGetInboxDetail.messageError } returns listOf("error")

        presenter.onActivityResult(
            InboxBaseContract.InboxBaseView.REQUEST_SUBMIT_FEEDBACK,
            Activity.RESULT_OK,
            mockk(relaxed = true)
        )

        verify { view.showErrorMessage(any()) }

    }

    @Test
    fun `check invocation of showMessage on invocation of onActivityResult`() {
        presenter.attachView(view)
        val chipGetInboxDetail = mockk<ChipGetInboxDetail>(relaxed = true)

        every {
            submitRatingUseCase.createRequestParams(
                any(),
                any(),
                any()
            )
        } returns mockk(relaxed = true)

        coEvery { submitRatingUseCase.getChipInboxDetail(any()) } returns chipGetInboxDetail

        every { chipGetInboxDetail.messageError } returns listOf()

        presenter.onActivityResult(
            InboxBaseContract.InboxBaseView.REQUEST_SUBMIT_FEEDBACK,
            Activity.RESULT_OK,
            mockk(relaxed = true)
        )

        verify { view.showMessage(any()) }

    }

    @Test
    fun `check invocation of showIssueClosed on invocation of onActivityResult`() {
        presenter.attachView(view)
        val chipGetInboxDetail = mockk<ChipGetInboxDetail>(relaxed = true)

        every {
            submitRatingUseCase.createRequestParams(
                any(),
                any(),
                any()
            )
        } returns mockk(relaxed = true)

        coEvery { submitRatingUseCase.getChipInboxDetail(any()) } returns chipGetInboxDetail

        every { chipGetInboxDetail.messageError } returns listOf()

        presenter.onActivityResult(
            InboxBaseContract.InboxBaseView.REQUEST_SUBMIT_FEEDBACK,
            Activity.RESULT_OK,
            mockk(relaxed = true)
        )

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
        presenter.attachView(view)
        every {
            submitRatingUseCase.createRequestParams(
                any(),
                any(),
                any()
            )
        } returns mockk(relaxed = true)

        coEvery { submitRatingUseCase.getChipInboxDetail(any()) } throws Exception("my exception")

        presenter.onActivityResult(
            InboxBaseContract.InboxBaseView.REQUEST_SUBMIT_FEEDBACK,
            Activity.RESULT_OK,
            mockk(relaxed = true)
        )

        verify { view.hideProgressBar() }

    }

    /*************************************onActivityResult()***************************************/


    /****************************************onImageSelect()***************************************/

    @Test
    fun `check invocation of setSnackBarErrorMessage on invocation onImageSelect and fileSize is not valid`() {
        presenter.attachView(view)
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
        presenter.attachView(view)
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
        presenter.attachView(view)
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
        presenter.attachView(view)
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
        presenter.attachView(view)
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
        presenter.attachView(view)
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
        presenter.attachView(view)
        val utils = mockk<Utils>(relaxed = true)

        every { view.imageList } returns listOf(ImageUpload(), ImageUpload())

        every { presenter.getUtils() } returns utils

        every { utils.verifyAllImages(any()) } returns 2

        presenter.isUploadImageValid

        assertEquals(presenter.isUploadImageValid, 2)

    }

    @Test
    fun `check value of isUploadImageValid when imageList is not empty and image is not valid`() {
        presenter.attachView(view)
        val utils = mockk<Utils>(relaxed = true)

        every { view.imageList } returns listOf(ImageUpload(), ImageUpload())

        every { presenter.getUtils() } returns utils

        every { utils.verifyAllImages(any()) } returns 1

        val result = presenter.isUploadImageValid

        assertEquals(result, -1)

    }

    @Test
    fun `check value of isUploadImageValid when imageList is empty`() {
        presenter.attachView(view)
        every { view.imageList } returns listOf()

        presenter.isUploadImageValid

        assertEquals(presenter.isUploadImageValid, 0)

    }

    /**************************************isUploadImageValid**************************************/


    /****************************************sendMessage()*****************************************/

    @Test
    fun `check invocation of hideSendProgress and setSnackBarErrorMessage on invocation of sendMessage when isUploadImageValid equals to 0`() {
        presenter.attachView(view)
        val response = mockk<TicketReplyResponse>(relaxed = true)

        every { presenter.isUploadImageValid } returns 0

        coEvery {
            postMessageUseCase.getCreateTicketResult(any())
                .getData<TicketReplyResponse>(TicketReplyResponse::class.java)
        } returns response

        every { response.ticketReply?.ticketReplyData?.status } returns ""

        presenter.sendMessage()

        verify { view.hideSendProgress() }

        verify { view.setSnackBarErrorMessage(any(), true) }

    }

    @Test
    fun `check updation of comment in Ticket on invocation of sendMessage when isUploadImageValid equals to 0`() {
        presenter.attachView(view)
        val response = mockk<TicketReplyResponse>(relaxed = true)
        val tickets = Tickets()
        tickets.comments = mutableListOf()

        val slot = slot<CommentsItem>()
        var actual = ""

        every { presenter.isUploadImageValid } returns 0

        coEvery {
            postMessageUseCase.getCreateTicketResult(any())
                .getData<TicketReplyResponse>(TicketReplyResponse::class.java)
        } returns response

        every { response.ticketReply?.ticketReplyData?.status } returns "OK"

        every { presenter.mTicketDetail } returns tickets

        every { view.imageList } returns listOf()

        every { view.userMessage } returns "message"

        every { view.updateAddComment(capture(slot)) } answers {
            actual = slot.captured.message ?: ""
        }

        presenter.sendMessage()

        assertEquals("message", actual)
        verify { view.updateAddComment(any()) }

    }

    @Test
    fun `check invocation of hideSendProgress invocation of sendMessage when isUploadImageValid equals to 0 throws exception`() {
        presenter.attachView(view)
        val tickets = Tickets()
        tickets.comments = mutableListOf()

        every { presenter.isUploadImageValid } returns 0

        coEvery { postMessageUseCase.getCreateTicketResult(any()) } throws Exception("my exception")

        presenter.sendMessage()

        verify { view.hideSendProgress() }

    }

    @Test
    fun `check invocation of setSnackBarErrorMessage invocation of sendMessage when isUploadImageValid returns -1`() {
        presenter.attachView(view)
        every { presenter.isUploadImageValid } returns -1

        presenter.sendMessage()

        verify { view.setSnackBarErrorMessage(any(), true) }

    }

    @Test
    fun `check invocation of sendMessageWithImages on invocation of sendMessage when isUploadImageValid returns 1 or more than 1 and response return status OK`() {
        presenter.attachView(view)
        val response = TicketReplyResponse(
            TicketReplyResponse.TicketReply(
                TicketReplyResponse.TicketReply.Data(
                    status = "OK",
                    postKey = "key"
                )
            )
        )

        val utils = mockk<Utils>(relaxed = true)

        every { presenter.isUploadImageValid } returns 2


        every { uploadImageUseCase.getFile(any()) } returns mockk()

        coEvery { uploadImageUseCase.uploadFile(any(), any(), any(), any()) } returns listOf()

        every { presenter.getUtils().getAttachmentAsString(any()) } returns ""

        every {
            postMessageUseCase.createRequestParams(
                any(),
                any(),
                any(),
                any(),
                any(),
                any()
            )
        } returns mockk()

        coEvery { postMessageUseCase2.getInboxDataResponse(any()) } returns null

        coEvery {
            postMessageUseCase.getCreateTicketResult(any())
                .getData<TicketReplyResponse>(TicketReplyResponse::class.java)
        } returns response

        every { presenter.getUtils() } returns utils

        every { utils.getFileUploaded(any()) } returns ""

        presenter.sendMessage()

        verify { view.hideSendProgress() }

        verify { view.setSnackBarErrorMessage(any(), true) }

    }

    @Test
    fun `check invocation of sendMessageWithImages on invocation of sendMessage when isUploadImageValid returns 1 or more than 1 and response return status OK and postKey empty`() {
        presenter.attachView(view)
        val response = TicketReplyResponse(
            TicketReplyResponse.TicketReply(
                TicketReplyResponse.TicketReply.Data(
                    status = "OK",
                    postKey = ""
                )
            )
        )

        val utils = mockk<Utils>(relaxed = true)

        every { presenter.isUploadImageValid } returns 2


        every { uploadImageUseCase.getFile(any()) } returns mockk()

        coEvery { uploadImageUseCase.uploadFile(any(), any(), any(), any()) } returns listOf()

        every { presenter.getUtils().getAttachmentAsString(any()) } returns ""

        every {
            postMessageUseCase.createRequestParams(
                any(),
                any(),
                any(),
                any(),
                any(),
                any()
            )
        } returns mockk()

        coEvery { postMessageUseCase2.getInboxDataResponse(any()) } returns null

        coEvery {
            postMessageUseCase.getCreateTicketResult(any())
                .getData<TicketReplyResponse>(TicketReplyResponse::class.java)
        } returns response

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
        presenter.attachView(view)
        val response = TicketReplyResponse(
            TicketReplyResponse.TicketReply(
                TicketReplyResponse.TicketReply.Data(
                    status = "OK",
                    postKey = "key"
                )
            )
        )
        val response2 = StepTwoResponse(
            StepTwoResponse.TicketReplyAttach(
                StepTwoResponse.TicketReplyAttach.TicketReplyAttachData(isSuccess = 0)
            )
        )

        val utils = mockk<Utils>(relaxed = true)

        every { presenter.isUploadImageValid } returns 2


        every { uploadImageUseCase.getFile(any()) } returns mockk()

        coEvery { uploadImageUseCase.uploadFile(any(), any(), any(), any()) } returns listOf()

        every { presenter.getUtils().getAttachmentAsString(any()) } returns ""

        every {
            postMessageUseCase.createRequestParams(
                any(),
                any(),
                any(),
                any(),
                any(),
                any()
            )
        } returns mockk()

        coEvery { postMessageUseCase2.getInboxDataResponse(any()) } returns response2

        coEvery {
            postMessageUseCase.getCreateTicketResult(any())
                .getData<TicketReplyResponse>(TicketReplyResponse::class.java)
        } returns response

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
        presenter.attachView(view)
        val response = TicketReplyResponse(
            TicketReplyResponse.TicketReply(
                TicketReplyResponse.TicketReply.Data(
                    status = "OK",
                    postKey = "key"
                )
            )
        )
        val utils = mockk<Utils>(relaxed = true)

        every { presenter.isUploadImageValid } returns 2


        every { uploadImageUseCase.getFile(any()) } returns mockk()

        coEvery { uploadImageUseCase.uploadFile(any(), any(), any(), any()) } returns listOf()

        every { presenter.getUtils().getAttachmentAsString(any()) } returns ""

        every {
            postMessageUseCase.createRequestParams(
                any(),
                any(),
                any(),
                any(),
                any(),
                any()
            )
        } returns mockk()

        coEvery { postMessageUseCase2.getInboxDataResponse(any()) } throws java.lang.Exception("my exception")

        coEvery {
            postMessageUseCase.getCreateTicketResult(any())
                .getData<TicketReplyResponse>(TicketReplyResponse::class.java)
        } returns response

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
        presenter.attachView(view)
        val response = TicketReplyResponse(
            TicketReplyResponse.TicketReply(
                TicketReplyResponse.TicketReply.Data(
                    status = "OK",
                    postKey = "key"
                )
            )
        )
        val response2 = StepTwoResponse(
            StepTwoResponse.TicketReplyAttach(
                StepTwoResponse.TicketReplyAttach.TicketReplyAttachData(isSuccess = 1)
            )
        )

        val utils = mockk<Utils>(relaxed = true)
        val slot = slot<CommentsItem>()
        var actual = ""

        every { presenter.isUploadImageValid } returns 2


        every { uploadImageUseCase.getFile(any()) } returns listOf("")

        coEvery { uploadImageUseCase.uploadFile(any(), any(), any(), any()) } returns listOf()

        coEvery { chipUploadHostConfigUseCase.getChipUploadHostConfig() } returns mockk(relaxed = true)

        every { presenter.getUtils().getAttachmentAsString(any()) } returns ""

        every {
            postMessageUseCase.createRequestParams(
                any(),
                any(),
                any(),
                any(),
                any(),
                any()
            )
        } returns mockk()
        coEvery {
            postMessageUseCase.getCreateTicketResult(any())
                .getData<TicketReplyResponse>(TicketReplyResponse::class.java)
        } returns response

        coEvery { postMessageUseCase2.getInboxDataResponse(any()) } returns response2

        every { presenter.getUtils() } returns utils

        every { utils.getFileUploaded(any()) } returns ""

        every { view.imageList } returns listOf(ImageUpload(fileLoc = "fileLoc"))

        every { view.userMessage } returns "message"

        every { view.updateAddComment(capture(slot)) } answers {
            actual = slot.captured.attachment?.getOrNull(0)?.thumbnail ?: ""
        }

        coEvery { presenter.getSecurelyUploadedImages(any(), any()) } returns arrayListOf(
            ImageUpload()
        )

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
        presenter.attachView(view)

        val utils = mockk<Utils>(relaxed = true)

        every { presenter.isUploadImageValid } returns 2


        every { uploadImageUseCase.getFile(any()) } returns mockk()

        coEvery { uploadImageUseCase.uploadFile(any(), any(), any(), any()) } returns listOf()

        every { presenter.getUtils().getAttachmentAsString(any()) } returns ""

        every {
            postMessageUseCase.createRequestParams(
                any(),
                any(),
                any(),
                any(),
                any(),
                any()
            )
        } returns mockk()

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
        presenter.attachView(view)
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
        presenter.attachView(view)
        every { presenter.searchIndices } returns arrayListOf(1, 2)

        val result = presenter.getNextResult()

        assertEquals(result, 2)
        assertEquals(presenter.searchIndices[1], 2)
        verify { view.setCurrentRes(2) }

    }

    @Test
    fun `check invocation of showMessage on invocation of getNextResult when searchIndices size is 1 `() {
        presenter.attachView(view)
        every { presenter.searchIndices } returns arrayListOf(1)
        every { presenter.next } returns 0

        val result = presenter.getNextResult()

        verify { view.showMessage(any()) }
        assertEquals(result, -1)

    }

    @Test
    fun `check invocation of showMessage on invocation of getNextResult when searchIndices size is 0`() {
        presenter.attachView(view)
        every { presenter.searchIndices } returns arrayListOf()

        val result = presenter.getNextResult()

        verify { view.showMessage(any()) }
        assertEquals(result, -1)

    }

    /****************************************getNextResult()***************************************/


    /*************************************getPreviousResult()**************************************/

    @Test
    fun `check value of searchIndices on invocation of getPreviousResult`() {
        presenter.attachView(view)
        every { presenter.searchIndices } returns arrayListOf(1, 2)
        every { presenter.next } returns 1

        val result = presenter.getPreviousResult()

        assertEquals(result, 2)
        assertEquals(presenter.searchIndices[0], 1)
        verify { view.setCurrentRes(2) }

    }

    @Test
    fun `check invocation of showMessage on invocation of getPreviousResult when searchIndices size is 1 `() {
        presenter.attachView(view)
        every { presenter.searchIndices } returns arrayListOf(1)
        every { presenter.next } returns 1

        val result = presenter.getPreviousResult()

        verify { view.showMessage(any()) }
        assertEquals(result, -1)

    }

    @Test
    fun `check invocation of showMessage on invocation of getPreviousResult when searchIndices size is 0`() {
        presenter.attachView(view)
        every { presenter.searchIndices } returns arrayListOf()

        val result = presenter.getPreviousResult()

        verify { view.showMessage(any()) }
        assertEquals(result, -1)

    }

    /*************************************getPreviousResult()**************************************/


    /*****************************************showImagePreview()***********************************/

//    @Test
//    fun `check invocation of onClickEmoji on invocation of showImagePreview`() {
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
        presenter.attachView(view)
        val response = mockk<ChipGetInboxDetail>(relaxed = true)

        coEvery { submitRatingUseCase.getChipInboxDetail(any()) } returns response
        coEvery { response.messageError } returns listOf("error")

        presenter.onClick(true, 0, "dummy id")

        verify { view.hideProgressBar() }
        verify { view.showErrorMessage("error") }

    }

    @Test
    fun `check invocation of onSuccessSubmitOfRating on invocation of onClick`() {
        presenter.attachView(view)
        val response = mockk<ChipGetInboxDetail>(relaxed = true)

        coEvery { submitRatingUseCase.getChipInboxDetail(any()) } returns response

        presenter.onClick(true, 0, "dummy id")

        verify { view.hideProgressBar() }
        verify { view.onSuccessSubmitOfRating(KEY_LIKED, 0) }

    }

    @Test
    fun `check invocation of hideProgressBar on invocation of onClick when exception`() {
        presenter.attachView(view)
        coEvery { submitRatingUseCase.getChipInboxDetail(any()) } throws Exception("my exception")

        presenter.onClick(true, 0, "dummy id")

        verify { view.hideProgressBar() }
    }

    /****************************************onClick()*********************************************/


    /***************************************closeTicket()******************************************/

    @Test
    fun `check invocation of showErrorMessage on invocation of closeTicket`() {
        presenter.attachView(view)
        val response = mockk<ChipGetInboxDetail>(relaxed = true)

        coEvery { closeTicketByUserUseCase.getChipInboxDetail(any()) } returns response
        every { response.messageError } returns listOf("error")

        presenter.closeTicket()

        verify { view.showErrorMessage("error") }

    }

    @Test
    fun `check invocation of OnSucessfullTicketClose on invocation of closeTicket`() {
        presenter.attachView(view)
        val response = mockk<ChipGetInboxDetail>(relaxed = true)

        coEvery { closeTicketByUserUseCase.getChipInboxDetail(any()) } returns response
        every { response.messageError } returns listOf()

        presenter.closeTicket()

        verify { view.OnSucessfullTicketClose() }

    }

    @Test
    fun `check invocation of hideProgressBar on invocation of closeTicket`() {
        presenter.attachView(view)
        coEvery { closeTicketByUserUseCase.getChipInboxDetail(any()) } throws Exception("my exception")

        presenter.closeTicket()

        verify { view.hideProgressBar() }

    }

    /***************************************closeTicket()******************************************/


    /******************************************getTicketStatus()***********************************/

    @Test
    fun `check value of getTicketStatus`() {
        presenter.attachView(view)
        val tickets = Tickets()
        tickets.status = "status"

        every { presenter.mTicketDetail } returns tickets

        val status = presenter.getTicketStatus()

        assertEquals(status, "status")
    }

    /******************************************getTicketStatus()***********************************/

    @Test
    fun `check getUserId`() {
        every { userSession.userId } returns "123"

        val actual = presenter.getUserId()

        assertEquals("123", actual)
    }

    @Test
    fun `is detachView running`() {
        presenter.attachView(view)
        presenter.detachView()
    }

    @Test
    fun `is onDestroy running`() {
        presenter.attachView(view)
        presenter.onDestroy()
    }

    @Test
    fun `is reAttachView running`() {
        presenter.attachView(view)
        presenter.reAttachView()
    }

    @Test
    fun `is clickCloseSearch running`() {
        presenter.attachView(view)
        presenter.clickCloseSearch()
    }

    @Test
    fun `check invocation of submitCsatRating on invocation of onActivityResult but intent extra is null`() {
        presenter.attachView(view)
        val responseIntent = mockk<Intent>(relaxed = true)
        val chipGetInboxDetail = mockk<ChipGetInboxDetail>(relaxed = true)

        every { responseIntent.data } returns null

        every {
            submitRatingUseCase.createRequestParams(
                any(),
                any(),
                any()
            )
        } returns mockk(relaxed = true)

        coEvery { submitRatingUseCase.getChipInboxDetail(any()) } returns chipGetInboxDetail

        every { chipGetInboxDetail.messageError } returns arrayListOf("ads", "bisa")

        presenter.onActivityResult(
            InboxBaseContract.InboxBaseView.REQUEST_SUBMIT_FEEDBACK,
            Activity.RESULT_OK,
            responseIntent
        )

        verify { view.showErrorMessage(any()) }
    }

    @Test
    fun `check invocation of submitCsatRating on invocation of onActivityResult but intent extra and view is null`() {
        val responseIntent = mockk<Intent>(relaxed = true)
        val chipGetInboxDetail = mockk<ChipGetInboxDetail>(relaxed = true)

        every { responseIntent.data } returns null

        every {
            submitRatingUseCase.createRequestParams(
                any(),
                any(),
                any()
            )
        } returns mockk(relaxed = true)

        coEvery { submitRatingUseCase.getChipInboxDetail(any()) } returns chipGetInboxDetail

        every { chipGetInboxDetail.messageError } returns arrayListOf("ads", "bisa")

        presenter.onActivityResult(
            InboxBaseContract.InboxBaseView.REQUEST_SUBMIT_FEEDBACK,
            Activity.RESULT_OK,
            responseIntent
        )

        verify(exactly = 0) { view.showErrorMessage(any()) }
    }

    @Test
    fun `check getTicketDetails isShowingRating False`() {
        val responseIntent = mockk<Intent>(relaxed = true)
        val chipGetInboxDetail = mockk<ChipGetInboxDetail>(relaxed = true)

        every { responseIntent.data } returns null

        mockkStatic(ContactUsTracking::class)
        every {
            ContactUsTracking.sendGTMInboxTicket(any(), any(), any(), any(), any())
        } just runs

        every {
            submitRatingUseCase.createRequestParams(
                any(),
                any(),
                any()
            )
        } returns mockk(relaxed = true)

        coEvery { submitRatingUseCase.getChipInboxDetail(any()) } returns chipGetInboxDetail

        every { chipGetInboxDetail.messageError } returns arrayListOf()

        presenter.onActivityResult(
            InboxBaseContract.InboxBaseView.REQUEST_SUBMIT_FEEDBACK,
            Activity.RESULT_OK,
            responseIntent
        )

        val response = mockk<ChipGetInboxDetail>(relaxed = true)

        coEvery { inboxOptionUseCase.getChipInboxDetail(any()) } returns response

        every { response.data?.isSuccess } returns 1

        presenter.getTicketDetails("")

        assertEquals(false, presenter.mTicketDetail?.isShowRating)
    }

    @Test
    fun `run get search listener`() {
        presenter.attachView(view)
        val actualResult = presenter.getSearchListener()
        assertEquals(true, actualResult is InboxDetailPresenter)
    }

    /*@Test
    fun `run on Options Item Selected is action search`() {
        presenter.attachView(view)
        val menu = mockk<MenuItem>(relaxed = true)
        every { menu.itemId } returns R.id.action_search

        val actualResult = presenter.onOptionsItemSelected(menu)
        assertTrue(actualResult)
        verify { view.toggleSearch(any()) }
    }*/

    /*@Test
    fun `run on Options Item Selected is action search but view empty`() {
        val menu = mockk<MenuItem>(relaxed = true)
        every { menu.itemId } returns R.id.action_search

        val actualResult = presenter.onOptionsItemSelected(menu)
        assertTrue(actualResult)
        verify(exactly = 0) { view.toggleSearch(any()) }
    }*/

    @Test
    fun `run on Options Item Selected not action search or home`() {
        presenter.attachView(view)
        val menu = mockk<MenuItem>(relaxed = true)
        every { menu.itemId } returns 1

        val actualResult = presenter.onOptionsItemSelected(menu)
        assertFalse(actualResult)
        verify(exactly = 0) { view.toggleSearch(any()) }
    }

    @Test
    fun `run on Options Item Selected is home and view mode is search mode`() {
        presenter.attachView(view)
        val menu = mockk<MenuItem>(relaxed = true)
        every { menu.itemId } returns 16908332
        every { view.isSearchMode() } returns true

        presenter.onOptionsItemSelected(menu)
        verify { view.toggleSearch(any()) }
    }

    @Test
    fun `run on Options Item Selected is home and view mode is search mode and show ratting true`() {
        presenter.attachView(view)
        val menu = mockk<MenuItem>(relaxed = true)
        every { menu.itemId } returns 16908332
        every { view.isSearchMode() } returns true
        every { presenter.mTicketDetail?.isShowRating } returns true

        presenter.onOptionsItemSelected(menu)
        verify { view.toggleTextToolbar(any()) }
    }

    @Test
    fun `run on Options Item Selected is home and view mode is search mode and show ratting false plus status is CLOSED`() {
        presenter.attachView(view)

        mockkStatic(Utils::class)
        val menu = mockk<MenuItem>(relaxed = true)
        every { menu.itemId } returns 16908332
        every { view.isSearchMode() } returns true
        every { presenter.mTicketDetail?.isShowRating } returns false
        every { presenter.mTicketDetail?.status } returns "closed"

        presenter.onOptionsItemSelected(menu)
        verify { view.showIssueClosed() }
    }

    @Test
    fun `run on Options Item Selected is home and view mode is search mode and show ratting false plus status is CLOSED but view null`() {

        mockkStatic(Utils::class)
        val menu = mockk<MenuItem>(relaxed = true)
        every { menu.itemId } returns 16908332
        every { view.isSearchMode() } returns true
        every { presenter.mTicketDetail?.isShowRating } returns false
        every { presenter.mTicketDetail?.status } returns "closed"

        presenter.onOptionsItemSelected(menu)
        verify(exactly = 0) { view.showIssueClosed() }
    }

    @Test
    fun `run on Options Item Selected is home and view mode is search mode and show ratting false plus status is CLOSED but view is null`() {
        val menu = mockk<MenuItem>(relaxed = true)
        every { menu.itemId } returns 16908332
        every { view.isSearchMode() } returns true
        every { presenter.mTicketDetail?.isShowRating } returns false
        every { presenter.mTicketDetail?.status } returns CLOSED.toString()

        presenter.onOptionsItemSelected(menu)
        verify(exactly = 0) { view.showIssueClosed() }
    }

    @Test
    fun `run on Options Item Selected is home but view mode is not search mode`() {
        presenter.attachView(view)
        val menu = mockk<MenuItem>(relaxed = true)
        every { menu.itemId } returns 16908332
        every { view.isSearchMode() } returns false

        val actional = presenter.onOptionsItemSelected(menu)
        assertFalse(actional)
        verify(exactly = 0) { view.toggleSearch(any()) }
    }

    @Test
    fun `run on Options Item Selected when view is null`() {
        val menu = mockk<MenuItem>(relaxed = true)
        every { menu.itemId } returns 1

        presenter.onOptionsItemSelected(menu)
        verify(exactly = 0) { view.toggleSearch(any()) }
    }

    @Test
    fun `run and verify of textWatcher`() {
        presenter.attachView(view)
        val actualCase = presenter.watcher()
        assertEquals(true, actualCase is TextWatcher)
    }

    @Test
    fun `run and verify of textWatcher and is running on onTextChanged for size text minimum`() {
        presenter.attachView(view)
        val actualCase = presenter.watcher()
        actualCase.onTextChanged("just only testing", mockk(relaxed = true), mockk(relaxed = true), mockk(relaxed = true))
        verify { view.setSubmitButtonEnabled(true) }
    }

    @Test
    fun `run and verify of textWatcher and is running on onTextChanged for size text below minimum`() {
        presenter.attachView(view)
        val actualCase = presenter.watcher()
        actualCase.onTextChanged("just only", mockk(relaxed = true), mockk(relaxed = true), mockk(relaxed = true))
        verify { view.setSubmitButtonEnabled(false) }
    }

    @Test
    fun `run and verify of textWatcher and is running on afterTextChanged`(){
        presenter.attachView(view)
        val actualCase = presenter.watcher()
        actualCase.afterTextChanged(mockk(relaxed = true))
    }

    @Test
    fun `run and verify of textWatcher and is running on beforeTextChanged`(){
        presenter.attachView(view)
        val actualCase = presenter.watcher()
        actualCase.beforeTextChanged(mockk(relaxed = true),mockk(relaxed = true),mockk(relaxed = true),mockk(relaxed = true))
    }
}
