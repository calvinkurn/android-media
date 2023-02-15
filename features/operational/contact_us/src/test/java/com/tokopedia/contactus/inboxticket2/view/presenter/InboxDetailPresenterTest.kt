package com.tokopedia.contactus.inboxticket2.view.presenter

import android.app.Activity
import android.content.Intent
import android.text.TextWatcher
import android.view.MenuItem
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.contactus.R
import com.tokopedia.contactus.common.analytics.ContactUsTracking
import com.tokopedia.contactus.inboxticket2.data.ImageUpload
import com.tokopedia.contactus.inboxticket2.data.model.*
import com.tokopedia.contactus.inboxticket2.domain.AttachmentItem
import com.tokopedia.contactus.inboxticket2.domain.CommentsItem
import com.tokopedia.contactus.inboxticket2.domain.CreatedBy
import com.tokopedia.contactus.inboxticket2.domain.StepTwoResponse
import com.tokopedia.contactus.inboxticket2.domain.usecase.*
import com.tokopedia.contactus.inboxticket2.view.activity.InboxDetailActivity
import com.tokopedia.contactus.inboxticket2.view.contract.InboxBaseContract
import com.tokopedia.contactus.inboxticket2.view.contract.InboxDetailContract
import com.tokopedia.contactus.inboxticket2.view.utils.NEW
import com.tokopedia.contactus.inboxticket2.view.utils.OPEN
import com.tokopedia.contactus.inboxticket2.view.utils.SOLVED
import com.tokopedia.contactus.inboxticket2.view.utils.Utils
import com.tokopedia.csat_rating.data.BadCsatReasonListItem
import com.tokopedia.graphql.data.model.GraphqlError
import com.tokopedia.unit.test.rule.CoroutineTestRule
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import org.junit.*
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue


@ExperimentalCoroutinesApi
class InboxDetailPresenterTest {

    companion object{
        private const val SUCCESS_KEY_SECURE_IMAGE_PARAMETER = 1
    }

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
    private lateinit var intent: Intent


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
        intent = mockk(relaxed = true)
        viewNullable = mockk(relaxed = true)
    }


    @After
    @Throws(Exception::class)
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `is detachView running`() {
        presenter.attachView(view)
        presenter.detachView()
    }

    @Test
    fun `run get search listener`() {
        presenter.attachView(view)
        val actualResult = presenter.getSearchListener()
        assertEquals(true, actualResult is InboxDetailPresenter)
    }

    @Test
    fun `onActivityResult not running submitCsatRating because all condition if is not compatible`() {
        presenter.onActivityResult(
            InboxBaseContract.InboxBaseView.REQUEST_IMAGE_PICKER,
            Activity.RESULT_CANCELED,
            mockk(relaxed = true)
        )

        verify(exactly = 0) { viewNullable?.showProgressBar() }
    }

    @Test
    fun `onActivityResult not running submitCsatRating because requestCode if is not compatible`() {
        presenter.onActivityResult(
            InboxBaseContract.InboxBaseView.REQUEST_IMAGE_PICKER,
            Activity.RESULT_OK,
            mockk(relaxed = true)
        )

        verify(exactly = 0) { viewNullable?.showProgressBar() }
    }

    @Test
    fun `onActivityResult not running submitCsatRating because resultCode if is not compatible`() {
        presenter.onActivityResult(
            InboxBaseContract.InboxBaseView.REQUEST_SUBMIT_FEEDBACK,
            Activity.RESULT_CANCELED,
            mockk(relaxed = true)
        )

        verify(exactly = 0) { viewNullable?.showProgressBar() }
    }

    @Test
    fun `onActivityResult running submitCsatRating but intent null, view not null`() {
        runBlockingTest {
            presenter.attachView(view)
            presenter.onActivityResult(
                InboxBaseContract.InboxBaseView.REQUEST_SUBMIT_FEEDBACK,
                Activity.RESULT_OK,
                null
            )
            verify { view.showProgressBar() }
        }
    }

    @Test
    fun `onActivityResult running submitCsatRating but intent null, view null`() {
        runBlockingTest {
            presenter.onActivityResult(
                InboxBaseContract.InboxBaseView.REQUEST_SUBMIT_FEEDBACK,
                Activity.RESULT_OK,
                null
            )
            verify(exactly = 0) { viewNullable?.getCommentID() }
        }
    }

    @Test
    fun `onActivityResult running submitCsatRating, intent not null but false data, view not null`() {
        runBlockingTest {
            presenter.attachView(view)
            coEvery { view.getCommentID() } returns "1"
            presenter.onActivityResult(
                InboxBaseContract.InboxBaseView.REQUEST_SUBMIT_FEEDBACK,
                Activity.RESULT_OK,
                Intent()
            )
            assertTrue(view.getCommentID() == "1")
            verify { view.showProgressBar() }

        }
    }

    @Test
    fun `check submitCsatRating and it is error`() {
        presenter.attachView(view)
        val chipGetInboxDetail = mockk<ChipGetInboxDetail>(relaxed = true)
        createRequestParamsSubmitRatting()
        coEvery { submitRatingUseCase.getChipInboxDetail(any()) } returns chipGetInboxDetail
        every { chipGetInboxDetail.messageError } returns listOf("error")
        presenter.onActivityResult(
            InboxBaseContract.InboxBaseView.REQUEST_SUBMIT_FEEDBACK,
            Activity.RESULT_OK,
            mockk(relaxed = true)
        )
        verify { view.showProgressBar() }
        verify { view.showErrorMessage(any()) }
        verify { view.hideProgressBar() }
    }

    @Test
    fun `check submitCsatRating and it is error, also view is null`() {
        val chipGetInboxDetail = mockk<ChipGetInboxDetail>(relaxed = true)
        createRequestParamsSubmitRatting()
        coEvery { submitRatingUseCase.getChipInboxDetail(any()) } returns chipGetInboxDetail
        every { chipGetInboxDetail.messageError } returns listOf("error")
        presenter.onActivityResult(
            InboxBaseContract.InboxBaseView.REQUEST_SUBMIT_FEEDBACK,
            Activity.RESULT_OK,
            mockk(relaxed = true)
        )
        verify(exactly = 0) { viewNullable?.showProgressBar() }
        verify(exactly = 0) { viewNullable?.showErrorMessage(any()) }
        verify(exactly = 0) { viewNullable?.hideProgressBar() }
    }

    @Test
    fun `check submitCsatRating when it is success`() {
        presenter.attachView(view)
        createRequestParamsSubmitRatting()
        val chipGetInboxDetail = mockk<ChipGetInboxDetail>(relaxed = true)
        coEvery { submitRatingUseCase.getChipInboxDetail(any()) } returns chipGetInboxDetail
        every { chipGetInboxDetail.messageError } returns arrayListOf()
        presenter.onActivityResult(
            InboxBaseContract.InboxBaseView.REQUEST_SUBMIT_FEEDBACK,
            Activity.RESULT_OK,
            mockk(relaxed = true)
        )
        verify { view.showProgressBar() }
        verify { view.showMessage(any()) }
        verify { view.hideProgressBar() }
    }

    @Test
    fun `check submitCsatRating when it is success, message null`() {
        presenter.attachView(view)
        createRequestParamsSubmitRatting()
        val chipGetInboxDetail = mockk<ChipGetInboxDetail>(relaxed = true)
        coEvery { submitRatingUseCase.getChipInboxDetail(any()) } returns chipGetInboxDetail
        every { chipGetInboxDetail.messageError } returns null
        presenter.onActivityResult(
            InboxBaseContract.InboxBaseView.REQUEST_SUBMIT_FEEDBACK,
            Activity.RESULT_OK,
            mockk(relaxed = true)
        )
        verify { view.showProgressBar() }
        verify { view.showMessage(any()) }
        verify { view.hideProgressBar() }
    }

    @Test
    fun `check submitCsatRating when it is success, submitRatingUseCase return null`() {
        presenter.attachView(view)
        createRequestParamsSubmitRatting()
        coEvery { submitRatingUseCase.getChipInboxDetail(any()) } returns null
        presenter.onActivityResult(
            InboxBaseContract.InboxBaseView.REQUEST_SUBMIT_FEEDBACK,
            Activity.RESULT_OK,
            mockk(relaxed = true)
        )
        verify { view.showProgressBar() }
        verify { view.showMessage(any()) }
        verify { view.hideProgressBar() }
    }

    @Test
    fun `check submitCsatRating when it is success, but view is null`() {
        createRequestParamsSubmitRatting()
        coEvery { submitRatingUseCase.getChipInboxDetail(any()) } returns null
        coEvery { viewNullable?.getActivity() } returns null
        presenter.onActivityResult(
            InboxBaseContract.InboxBaseView.REQUEST_SUBMIT_FEEDBACK,
            Activity.RESULT_OK,
            mockk(relaxed = true)
        )
        verify(exactly = 0) { viewNullable?.showProgressBar() }
        verify(exactly = 0) { viewNullable?.showMessage(any()) }
        verify(exactly = 0) { viewNullable?.hideProgressBar() }
    }

    @Test
    fun `check submitCsatRating when it is success, but view is null, failed getString`() {
        createRequestParamsSubmitRatting()
        coEvery { submitRatingUseCase.getChipInboxDetail(any()) } returns null
        coEvery { viewNullable?.getActivity()?.getString(any()) } returns null
        presenter.onActivityResult(
            InboxBaseContract.InboxBaseView.REQUEST_SUBMIT_FEEDBACK,
            Activity.RESULT_OK,
            mockk(relaxed = true)
        )
        verify(exactly = 0) { viewNullable?.getActivity()?.getString(any()) }
        verify(exactly = 0) { viewNullable?.showProgressBar() }
        verify(exactly = 0) { viewNullable?.showMessage(any()) }
        verify(exactly = 0) { viewNullable?.hideProgressBar() }
    }

    @Test
    fun `check sendGTMEventClickSubmitCsatRating when the rating is 5`() {
        presenter.attachView(view)
        createRequestParamsSubmitRatting()
        val chipGetInboxDetail = mockk<ChipGetInboxDetail>(relaxed = true)
        coEvery { submitRatingUseCase.getChipInboxDetail(any()) } returns chipGetInboxDetail
        every { chipGetInboxDetail.messageError } returns arrayListOf()
        every { view.getActivity().resources.getStringArray(any()) } returns returnArrayOfCsatCaptions()
        coEvery { intent.extras?.getInt(any()) } returns 5
        coEvery { intent.getStringExtra(any()) } returns "5"
        mockkStatic(ContactUsTracking::class)
        every { ContactUsTracking.sendGTMInboxTicket(any(), any(), any(), any(), any()) } just runs

        presenter.onActivityResult(
            InboxBaseContract.InboxBaseView.REQUEST_SUBMIT_FEEDBACK,
            Activity.RESULT_OK,
            intent
        )
        verify { view.showProgressBar() }
        verify { view.showMessage(any()) }
        verify { view.hideProgressBar() }
    }

    @Test
    fun `check sendGTMEventClickSubmitCsatRating when the rating is 5 and get badCsatReasonList are not same`() {
        presenter.attachView(view)
        createRequestParamsSubmitRatting()
        val chipGetInboxDetail = mockk<ChipGetInboxDetail>(relaxed = true)

        coEvery { presenter.mTicketDetail?.badCsatReasonList } returns arrayListOf(
            BadCsatReasonListItem().apply {
                id = 4
                message = "Sangat Memuaskan"
            }
        )

        coEvery { submitRatingUseCase.getChipInboxDetail(any()) } returns chipGetInboxDetail
        every { chipGetInboxDetail.messageError } returns arrayListOf()
        every { view.getActivity().resources.getStringArray(any()) } returns returnArrayOfCsatCaptions()
        coEvery { intent.extras?.getInt(any()) } returns 5
        coEvery { intent.getStringExtra(any()) } returns "5"


        mockkStatic(ContactUsTracking::class)
        every { ContactUsTracking.sendGTMInboxTicket(any(), any(), any(), any(), any()) } just runs

        presenter.onActivityResult(
            InboxBaseContract.InboxBaseView.REQUEST_SUBMIT_FEEDBACK,
            Activity.RESULT_OK,
            intent
        )
        verify { view.showProgressBar() }
        verify { view.showMessage(any()) }
        verify { view.hideProgressBar() }
    }

    @Test
    fun `check sendGTMEventClickSubmitCsatRating when the rating is 5 and get badCsatReasonList are same`() {
        presenter.attachView(view)
        createRequestParamsSubmitRatting()
        val chipGetInboxDetail = mockk<ChipGetInboxDetail>(relaxed = true)

        coEvery { presenter.mTicketDetail?.badCsatReasonList } returns arrayListOf(
            BadCsatReasonListItem().apply {
                id = 5
                message = "Sangat Memuaskan"
            }
        )

        coEvery { submitRatingUseCase.getChipInboxDetail(any()) } returns chipGetInboxDetail
        every { chipGetInboxDetail.messageError } returns arrayListOf()
        every { view.getActivity().resources.getStringArray(any()) } returns returnArrayOfCsatCaptions()
        coEvery { intent.extras?.getInt(any()) } returns 5
        coEvery { intent.getStringExtra(any()) } returns "5"


        mockkStatic(ContactUsTracking::class)
        every { ContactUsTracking.sendGTMInboxTicket(any(), any(), any(), any(), any()) } just runs

        presenter.onActivityResult(
            InboxBaseContract.InboxBaseView.REQUEST_SUBMIT_FEEDBACK,
            Activity.RESULT_OK,
            intent
        )
        verify { view.showProgressBar() }
        verify { view.showMessage(any()) }
        verify { view.hideProgressBar() }
    }

    @Test
    fun `check sendGTMEventClickSubmitCsatRating when the rating is 5 and get badCsatReasonList are same but message null`() {
        presenter.attachView(view)
        createRequestParamsSubmitRatting()
        val chipGetInboxDetail = mockk<ChipGetInboxDetail>(relaxed = true)

        coEvery { presenter.mTicketDetail?.badCsatReasonList } returns arrayListOf(
            BadCsatReasonListItem().apply {
                id = 5
            }
        )

        coEvery { submitRatingUseCase.getChipInboxDetail(any()) } returns chipGetInboxDetail
        every { chipGetInboxDetail.messageError } returns arrayListOf()
        every { view.getActivity().resources.getStringArray(any()) } returns returnArrayOfCsatCaptions()
        coEvery { intent.extras?.getInt(any()) } returns 5
        coEvery { intent.getStringExtra(any()) } returns "5"


        mockkStatic(ContactUsTracking::class)
        every { ContactUsTracking.sendGTMInboxTicket(any(), any(), any(), any(), any()) } just runs

        presenter.onActivityResult(
            InboxBaseContract.InboxBaseView.REQUEST_SUBMIT_FEEDBACK,
            Activity.RESULT_OK,
            intent
        )
        verify { view.showProgressBar() }
        verify { view.showMessage(any()) }
        verify { view.hideProgressBar() }
    }

    @Test
    fun `check sendGTMEventClickSubmitCsatRating when the ratting is 0`() {
        presenter.attachView(view)
        createRequestParamsSubmitRatting()
        val chipGetInboxDetail = mockk<ChipGetInboxDetail>(relaxed = true)
        coEvery { submitRatingUseCase.getChipInboxDetail(any()) } returns chipGetInboxDetail
        every { chipGetInboxDetail.messageError } returns arrayListOf()
        every { view.getActivity().resources.getStringArray(any()) } returns returnArrayOfCsatCaptions()
        coEvery { intent.extras?.getInt(any()) } returns 0
        coEvery { intent.getStringExtra(any()) } returns "0"
        mockkStatic(ContactUsTracking::class)
        every { ContactUsTracking.sendGTMInboxTicket(any(), any(), any(), any(), any()) } just runs

        presenter.onActivityResult(
            InboxBaseContract.InboxBaseView.REQUEST_SUBMIT_FEEDBACK,
            Activity.RESULT_OK,
            intent
        )


        verify { view.showProgressBar() }
        verify { view.showMessage(any()) }
        verify { view.hideProgressBar() }
    }

    @Test
    fun `check sendGTMEventClickSubmitCsatRating when the ratting is 5 but captions is null`() {
        presenter.attachView(viewNullable)
        createRequestParamsSubmitRatting()
        val chipGetInboxDetail = mockk<ChipGetInboxDetail>(relaxed = true)
        coEvery { submitRatingUseCase.getChipInboxDetail(any()) } returns chipGetInboxDetail
        every { chipGetInboxDetail.messageError } returns arrayListOf()
        every { viewNullable?.getActivity()?.resources?.getStringArray(any()) } returns null
        coEvery { intent.extras?.getInt(any()) } returns 5
        coEvery { intent.getStringExtra(any()) } returns "5"
        mockkStatic(ContactUsTracking::class)
        every { ContactUsTracking.sendGTMInboxTicket(any(), any(), any(), any(), any()) } just runs

        presenter.onActivityResult(
            InboxBaseContract.InboxBaseView.REQUEST_SUBMIT_FEEDBACK,
            Activity.RESULT_OK,
            intent
        )
    }

    private fun returnArrayOfCsatCaptions(): Array<String> {
        return arrayOf(
            "Tidak Memuaskan", "Kurang Memuaskan", "Cukup Memuaskan",
            "Memuaskan", "Sangat Memuaskan"
        )
    }

    private fun createRequestParamsSubmitRatting() {
        every {
            submitRatingUseCase.createRequestParams(
                any(),
                any(),
                any()
            )
        } returns mockk(relaxed = true)
    }

    @Test
    fun `is onDestroy running`() {
        presenter.attachView(view)
        presenter.onDestroy()
    }

    @Test
    fun `run on Options Item Selected is action search`() {
        presenter.attachView(view)
        val menu = mockk<MenuItem>(relaxed = true)
        every { menu.itemId } returns R.id.action_search

        val actualResult = presenter.onOptionsItemSelected(menu)
        assertTrue(actualResult)
        verify { view.toggleSearch(any()) }
    }

    @Test
    fun `run on Options Item Selected is action search but view empty`() {
        val menu = mockk<MenuItem>(relaxed = true)
        every { menu.itemId } returns R.id.action_search

        val actualResult = presenter.onOptionsItemSelected(menu)
        assertTrue(actualResult)
        verify(exactly = 0) { view.toggleSearch(any()) }
    }

    @Test
    fun `run on Options Item Selected not action search or home`() {
        presenter.attachView(view)
        val menu = mockk<MenuItem>(relaxed = true)
        every { menu.itemId } returns 1

        val actualResult = presenter.onOptionsItemSelected(menu)
        Assert.assertFalse(actualResult)
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
    fun `run on Options Item Selected is home and view mode is search mode but view null`() {
        val menu = mockk<MenuItem>(relaxed = true)
        every { menu.itemId } returns 16908332
        every { viewNullable?.isSearchMode() } returns true

        presenter.onOptionsItemSelected(menu)
        verify(exactly = 0) { viewNullable?.toggleSearch(any()) }
        verify(exactly = 0) { viewNullable?.toggleTextToolbar(any()) }
        verify(exactly = 0) { viewNullable?.showIssueClosed() }
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
    fun `run on Options Item Selected is home and view mode is search mode and show ratting null plus status is CLOSED but view null`() {

        mockkStatic(Utils::class)
        val menu = mockk<MenuItem>(relaxed = true)
        every { menu.itemId } returns 16908332
        every { view.isSearchMode() } returns true
        every { presenter.mTicketDetail?.isShowRating } returns null
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
        Assert.assertFalse(actional)
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
    fun `check refreshLayout`() {
        presenter.attachView(view)
        presenter.refreshLayout()
        verify { presenter.getTicketDetails(any()) }
    }

    @Test
    fun `check onSearchSubmitted`() {
        presenter.attachView(view)
        mockkStatic(ContactUsTracking::class)
        every { ContactUsTracking.sendGTMInboxTicket(any(), any(), any(), any(), any()) } just runs

        presenter.onSearchSubmitted("dummy")

        verify { view.showProgressBar() }

    }

    @Test
    fun `check onSearchSubmitted when input is empty`() {
        presenter.attachView(view)
        mockkStatic(ContactUsTracking::class)
        every { ContactUsTracking.sendGTMInboxTicket(any(), any(), any(), any(), any()) } just runs

        presenter.onSearchSubmitted("")

        verify { presenter.searchIndices.clear() }

    }

    @Test
    fun `check search with correct way`() {
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
    fun `check search with correct way but view are null`() {
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
    fun `check invocation of enterSearchMode on invocation onSearchSubmitted`() {
        presenter.attachView(view)
        mockkStatic(ContactUsTracking::class)
        every { ContactUsTracking.sendGTMInboxTicket(any(), any(), any(), any(), any()) } just runs

        presenter.onSearchSubmitted("dummy")

        verify { view.enterSearchMode(any(), any()) }
    }

    @Test
    fun `check search are error and view is not null`() {
        presenter.attachView(view)
        runBlockingTest {

            every { view.enterSearchMode(any(), any()) } throws Exception("getting exception")

            presenter.onSearchSubmitted("dummy")

            verify { view.hideProgressBar() }
            verify { view.setSnackBarErrorMessage(any(), any()) }
        }
    }

    @Test
    fun `check search are error and view is null`() {
        runBlockingTest {
            every {
                viewNullable?.enterSearchMode(
                    any(),
                    any()
                )
            } throws Exception("getting exception")

            presenter.onSearchSubmitted("dummy")

            verify(exactly = 0) { viewNullable?.hideProgressBar() }
            verify(exactly = 0) { viewNullable?.setSnackBarErrorMessage(any(), any()) }
        }
    }

    @Test
    fun `check onSearchTextChanged`() {
        presenter.attachView(view)
        presenter.onSearchTextChanged("")
        verify { presenter.onSearchSubmitted(any()) }
    }

    @Test
    fun `check getTicketDetails is not success`() {
        val response: ChipGetInboxDetail = mockk(relaxed = true)
        presenter.attachView(view)
        coEvery { inboxOptionUseCase.getChipInboxDetail(any()) } returns response
        coEvery { response.data?.isSuccess } returns 0

        presenter.getTicketDetails("")

        coVerify { view.showProgressBar() }
        coVerify { view.showNoTicketView(any()) }
    }

    @Test
    fun `check getTicketDetails is not success and view is null`() {
        val response: ChipGetInboxDetail = mockk(relaxed = true)
        coEvery { inboxOptionUseCase.getChipInboxDetail(any()) } returns response
        coEvery { response.data?.isSuccess } returns 0

        presenter.getTicketDetails("")

        coVerify(exactly = 0) { viewNullable?.showProgressBar() }
        coVerify(exactly = 0) { viewNullable?.showNoTicketView(any()) }
    }

    @Test
    fun `check getTicketDetails is null`() {
        val response: ChipGetInboxDetail = mockk(relaxed = true)
        presenter.attachView(view)
        coEvery { inboxOptionUseCase.getChipInboxDetail(any()) } returns null
        coEvery { response.data } returns null

        presenter.getTicketDetails("")

        coVerify(exactly = 1) { view.showProgressBar() }
        coVerify(exactly = 0) { view.showNoTicketView(any()) }
    }

    @Test
    fun `check getTicketDetails is success`() {
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
    fun `check getTicketDetails is success and the issue is closed`() {
        runBlockingTest {
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
            createRequestParamsSubmitRatting()
            val chipGetInboxDetail = mockk<ChipGetInboxDetail>(relaxed = true)
            coEvery { submitRatingUseCase.getChipInboxDetail(any()) } returns chipGetInboxDetail
            every { chipGetInboxDetail.messageError } returns arrayListOf()
            presenter.onActivityResult(
                InboxBaseContract.InboxBaseView.REQUEST_SUBMIT_FEEDBACK,
                Activity.RESULT_OK,
                mockk(relaxed = true)
            )

            presenter.getTicketDetails("")

            verify { view.renderMessageList(any()) }
            assertTrue(presenter.mTicketDetail?.isShowRating == false)
        }
    }

    @Test
    fun `check getTicketDetails on getTopItem and getCommentHeader where mTicketDetail is not null`() {
        runBlockingTest {
            presenter.attachView(view)
            val response = mockk<ChipGetInboxDetail>(relaxed = true)
            val ticketDetail = Tickets()
            val commentsItem = CommentsItem()
            commentsItem.messagePlaintext = "a string with dummy text"
            ticketDetail.comments = mutableListOf(commentsItem)

            coEvery { inboxOptionUseCase.getChipInboxDetail(any()) } returns response
            coEvery { response.data?.isSuccess } returns 1
            coEvery { response.data?.tickets } returns ticketDetail
            presenter.getTicketDetails("1")

            verify { view.renderMessageList(any()) }
        }
    }

    @Test
    fun `check getTicketDetails on getTopItem and getCommentHeader where mTicketDetail is has full detail`() {
        runBlockingTest {
            presenter.attachView(view)
            val response = mockk<ChipGetInboxDetail>(relaxed = true)
            val ticketDetail = Tickets()
            ticketDetail.status = SOLVED
            val commentsItem = CommentsItem()
            commentsItem.messagePlaintext = "a string with dummy text"
            ticketDetail.comments = mutableListOf(commentsItem)
            ticketDetail.attachment = arrayListOf()
            ticketDetail.message = ""

            coEvery { inboxOptionUseCase.getChipInboxDetail(any()) } returns response
            coEvery { response.data?.isSuccess } returns 1
            coEvery { response.data?.tickets } returns ticketDetail
            presenter.getTicketDetails("1")

            verify { view.renderMessageList(any()) }
        }
    }

    @Test
    fun `check getStatus() when status is OPEN `() {
        runBlockingTest {
            presenter.attachView(view)
            val response = mockk<ChipGetInboxDetail>(relaxed = true)
            val ticketDetail = Tickets()
            ticketDetail.status = OPEN
            val commentsItem = CommentsItem()
            commentsItem.messagePlaintext = "a string with dummy text"
            ticketDetail.comments = mutableListOf(commentsItem)
            ticketDetail.attachment = arrayListOf()
            ticketDetail.message = ""

            coEvery { inboxOptionUseCase.getChipInboxDetail(any()) } returns response
            coEvery { response.data?.isSuccess } returns 1
            coEvery { response.data?.tickets } returns ticketDetail
            presenter.getTicketDetails("1")

            verify { view.renderMessageList(any()) }
        }
    }

    @Test
    fun `check getStatus() when status is NEW `() {
        runBlockingTest {
            presenter.attachView(view)
            val response = mockk<ChipGetInboxDetail>(relaxed = true)
            val ticketDetail = Tickets()
            ticketDetail.status = NEW
            val commentsItem = CommentsItem()
            commentsItem.messagePlaintext = "a string with dummy text"
            ticketDetail.comments = mutableListOf(commentsItem)
            ticketDetail.attachment = arrayListOf()
            ticketDetail.message = ""

            coEvery { inboxOptionUseCase.getChipInboxDetail(any()) } returns response
            coEvery { response.data?.isSuccess } returns 1
            coEvery { response.data?.tickets } returns ticketDetail
            presenter.getTicketDetails("1")

            verify { view.renderMessageList(any()) }
        }
    }

    @Test
    fun `check getStatus() when status is CLOSED `() {
        runBlockingTest {
            presenter.attachView(view)
            val response = mockk<ChipGetInboxDetail>(relaxed = true)
            val ticketDetail = Tickets()
            ticketDetail.status = com.tokopedia.contactus.inboxticket2.view.utils.CLOSED
            val commentsItem = CommentsItem()
            commentsItem.messagePlaintext = "a string with dummy text"
            ticketDetail.comments = mutableListOf(commentsItem)
            ticketDetail.attachment = arrayListOf()
            ticketDetail.message = ""

            coEvery { inboxOptionUseCase.getChipInboxDetail(any()) } returns response
            coEvery { response.data?.isSuccess } returns 1
            coEvery { response.data?.tickets } returns ticketDetail
            presenter.getTicketDetails("1")

            verify { view.renderMessageList(any()) }
        }
    }

    @Test
    fun `check getStatus() when show ratting is true `() {
        runBlockingTest {
            presenter.attachView(view)
            val response = mockk<ChipGetInboxDetail>(relaxed = true)
            val ticketDetail = Tickets()
            ticketDetail.status = com.tokopedia.contactus.inboxticket2.view.utils.CLOSED
            val commentsItem = CommentsItem()
            commentsItem.messagePlaintext = "a string with dummy text"
            ticketDetail.comments = mutableListOf(commentsItem)
            ticketDetail.attachment = arrayListOf()
            ticketDetail.message = ""
            ticketDetail.isShowRating = true

            coEvery { inboxOptionUseCase.getChipInboxDetail(any()) } returns response
            coEvery { response.data?.isSuccess } returns 1
            coEvery { response.data?.tickets } returns ticketDetail
            presenter.getTicketDetails("1")

            verify { view.renderMessageList(any()) }
        }
    }

    @Test
    fun `check getTicketDetails is success but view is null`() {
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

        verify(exactly = 0) { viewNullable?.renderMessageList(any()) }
        verify(exactly = 0) { viewNullable?.hideProgressBar() }
    }

    @Test
    fun `check get ticket id when PARAM_TICKET_T_ID is null and PARAM_TICKET_ID is 2`() {
        presenter.attachView(view)
        every { view.getActivity().intent.getStringExtra(InboxDetailActivity.PARAM_TICKET_T_ID) } returns null
        every { view.getActivity().intent.getStringExtra(InboxDetailActivity.PARAM_TICKET_ID) } returns "2"
        val actualResult = presenter.getTicketId()
        assertTrue(actualResult == "2")
    }

    @Test
    fun `check get ticket id PARAM_TICKET_T_ID is 2`() {
        presenter.attachView(view)
        every { view.getActivity().intent.getStringExtra(InboxDetailActivity.PARAM_TICKET_T_ID) } returns "2"
        val actualResult = presenter.getTicketId()
        assertTrue(actualResult == "2")
    }

    @Test
    fun `check get ticket id PARAM_TICKET_T_ID and PARAM_TICKET_T_ID is null`() {
        presenter.attachView(view)
        every { view.getActivity().intent.getStringExtra(InboxDetailActivity.PARAM_TICKET_T_ID) } returns null
        every { view.getActivity().intent.getStringExtra(InboxDetailActivity.PARAM_TICKET_ID) } returns null
        val actualResult = presenter.getTicketId()
        assertTrue(actualResult == null)
    }

    @Test
    fun `check get ticket id where view is null`() {
        val actualResult = presenter.getTicketId()
        verify(exactly = 0) {
            viewNullable?.getActivity()?.intent?.getStringExtra(
                InboxDetailActivity.PARAM_TICKET_T_ID
            )
        }
        verify(exactly = 0) {
            viewNullable?.getActivity()?.intent?.getStringExtra(
                InboxDetailActivity.PARAM_TICKET_ID
            )
        }
        assertTrue(actualResult == null)
    }


    @Test
    fun `check onImageSelect`() {
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
    fun `check onImageSelect but view is null`() {
        val utils = mockk<Utils>()
        every { presenter.getUtils() } returns utils

        every { utils.fileSizeValid(any()) } returns false

        mockkStatic(ContactUsTracking::class)
        every { ContactUsTracking.sendGTMInboxTicket(any(), any(), any(), any(), any()) } just runs

        presenter.onImageSelect(mockk(relaxed = true))

        verify(exactly = 0) { viewNullable?.setSnackBarErrorMessage(any(), true) }
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

    @Test
    fun `check invocation of addImage on invocation onImageSelect and bitmapDimen and fileSize is valid but view is null`() {
        val utils = mockk<Utils>()
        every { presenter.getUtils() } returns utils

        every { utils.fileSizeValid(any()) } returns true

        every { utils.isBitmapDimenValid(any()) } returns true

        mockkStatic(ContactUsTracking::class)
        every { ContactUsTracking.sendGTMInboxTicket(any(), any(), any(), any(), any()) } just runs

        presenter.onImageSelect(mockk(relaxed = true))

        verify(exactly = 0) { viewNullable?.addImage(any()) }
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
        actualCase.onTextChanged(
            "just only testing",
            mockk(relaxed = true),
            mockk(relaxed = true),
            mockk(relaxed = true)
        )
        verify { view.setSubmitButtonEnabled(true) }
    }

    @Test
    fun `run and verify of textWatcher and is running on onTextChanged for size text below minimum`() {
        presenter.attachView(view)
        val actualCase = presenter.watcher()
        actualCase.onTextChanged(
            "just only",
            mockk(relaxed = true),
            mockk(relaxed = true),
            mockk(relaxed = true)
        )
        verify { view.setSubmitButtonEnabled(false) }
    }

    @Test
    fun `run and verify of textWatcher and is running on onTextChanged for size text with maximum`() {
        presenter.attachView(view)
        val actualCase = presenter.watcher()
        actualCase.onTextChanged(
            "just only just only just only just only just only just only just only just only just " +
                "only just only1 just only just only just only just only just only just only just " +
                "only just only just only just only1 just only just only just only just only just " +
                "only just only just only just only just only just only1 just only just only just " +
                "only just only just only just only just only just only just only just only1 just " +
                "only just only just only just only just only just only just only just only just " +
                "only just only1 just only just only just only just only just only just only just " +
                "only just only just only just only1 just only just only just only just only just " +
                "only just only just only just only just only just only1 just only just only just" +
                " only just only just only just only just only just only just only just only1 just" +
                " only just only just only just only just only just only just only just only just " +
                "only just only1 just only just only just only just only just only just only just " +
                "only just only just only j",
            mockk(relaxed = true),
            mockk(relaxed = true),
            mockk(relaxed = true)
        )
        verify { view.setSubmitButtonEnabled(true) }
    }

    @Test
    fun `run and verify of textWatcher and is running on onTextChanged for size text minimum without view`() {
        val actualCase = presenter.watcher()
        actualCase.onTextChanged(
            "just only testing",
            mockk(relaxed = true),
            mockk(relaxed = true),
            mockk(relaxed = true)
        )
        verify(exactly = 0) { viewNullable?.setSubmitButtonEnabled(true) }
    }

    @Test
    fun `run and verify of textWatcher and is running on onTextChanged for size text below minimum without view`() {
        val actualCase = presenter.watcher()
        actualCase.onTextChanged(
            "just only",
            mockk(relaxed = true),
            mockk(relaxed = true),
            mockk(relaxed = true)
        )
        verify(exactly = 0) { viewNullable?.setSubmitButtonEnabled(false) }
    }

    @Test
    fun `run and verify of textWatcher and is running on onTextChanged for size text with maximum without view`() {
        val actualCase = presenter.watcher()
        actualCase.onTextChanged(
            "just only just only just only just only just only just only just only just only just " +
                "only just only1 just only just only just only just only just only just only just " +
                "only just only just only just only1 just only just only just only just only just " +
                "only just only just only just only just only just only1 just only just only just " +
                "only just only just only just only just only just only just only just only1 just " +
                "only just only just only just only just only just only just only just only just " +
                "only just only1 just only just only just only just only just only just only just " +
                "only just only just only just only1 just only just only just only just only just " +
                "only just only just only just only just only just only1 just only just only just" +
                " only just only just only just only just only just only just only just only1 just" +
                " only just only just only just only just only just only just only just only just " +
                "only just only1 just only just only just only just only just only just only just " +
                "only just only just only j",
            mockk(relaxed = true),
            mockk(relaxed = true),
            mockk(relaxed = true)
        )
        verify(exactly = 0) { viewNullable?.setSubmitButtonEnabled(true) }
    }

    @Test
    fun `run and verify of textWatcher and is running on afterTextChanged`() {
        presenter.attachView(view)
        val actualCase = presenter.watcher()
        actualCase.afterTextChanged(mockk(relaxed = true))
    }

    @Test
    fun `run and verify of textWatcher and is running on beforeTextChanged`() {
        presenter.attachView(view)
        val actualCase = presenter.watcher()
        actualCase.beforeTextChanged(
            mockk(relaxed = true),
            mockk(relaxed = true),
            mockk(relaxed = true),
            mockk(relaxed = true)
        )
    }

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
    fun `check invocation of setSnackBarErrorMessage invocation of sendMessage when isUploadImageValid returns -1 without view`() {
        every { presenter.isUploadImageValid } returns -1

        presenter.sendMessage()

        verify(exactly = 0) { viewNullable?.setSnackBarErrorMessage(any(), true) }
    }

    @Test
    fun `check invocation of setSnackBarErrorMessage invocation of sendMessage when isUploadImageValid returns -1 no found resource`() {
        presenter.attachView(viewNullable)
        every { presenter.isUploadImageValid } returns -1
        every { viewNullable?.getActivity()?.resources } returns null

        presenter.sendMessage()

        verify(exactly = 1) { viewNullable?.setSnackBarErrorMessage("", true) }
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

        every { presenter.isUploadImageValid } returns 1


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

        every { presenter.isUploadImageValid } returns 1


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
    fun `check getLastReplyFromAgent when reversed role check`() {
        presenter.attachView(view)
        val response = mockk<TicketReplyResponse>(relaxed = true)
        val tickets = Tickets()
        tickets.comments = arrayListOf( CommentsItem(createdBy = CreatedBy(role = "agent"), message = "adasd"),CommentsItem(createdBy = CreatedBy(role = "agent"), message = null), CommentsItem(createdBy = CreatedBy(role = "customer")), CommentsItem(createdBy = CreatedBy(role = null)))

        val slot = slot<CommentsItem>()
        var actual = ""

        every { presenter.isUploadImageValid } returns 0
        every { presenter.mTicketDetail?.id } returns "1"

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
    fun `check showImagePreview`() {
        presenter.attachView(view)
        val attachmentItem = AttachmentItem()
        attachmentItem.url = "dummy url"
        val attachmentItem2 = AttachmentItem()
        attachmentItem2.url = null
        attachmentItem2.thumbnail = "dummy url"
        val attachmentItem3 = AttachmentItem()
        attachmentItem3.url = null
        attachmentItem3.thumbnail = null

        mockkStatic(ContactUsTracking::class)
        every {
            ContactUsTracking.sendGTMInboxTicket(any(), any(), any(), any(), any())
        } just runs

        presenter.showImagePreview(0, listOf(attachmentItem, attachmentItem2, attachmentItem3))

        verify { view.showImagePreview(0, any()) }

    }

    @Test
    fun `check showImagePreview but no view`() {
        val attachmentItem = AttachmentItem()
        attachmentItem.url = "dummy url"

        mockkStatic(ContactUsTracking::class)
        every {
            ContactUsTracking.sendGTMInboxTicket(any(), any(), any(), any(), any())
        } just runs

        presenter.showImagePreview(0, listOf(attachmentItem))

        verify(exactly = 0) { viewNullable?.showImagePreview(0, any()) }

    }

    /***************************************showImagePreview()*************************************/

    @Test
    fun `run and verify of onClickEmoji`() {
        presenter.attachView(view)
        mockkStatic(ContactUsTracking::class)
        every { ContactUsTracking.sendGTMInboxTicket(any(), any(), any(), any(), any()) } just runs
        presenter.onClickEmoji(1)
        verify { view.getActivity() }
        verify { view.startActivityForResult(any(), any()) }
    }

    @Test
    fun `run and verify of onClickEmoji but view is not available`() {
        mockkStatic(ContactUsTracking::class)
        every { ContactUsTracking.sendGTMInboxTicket(any(), any(), any(), any(), any()) } just runs
        presenter.onClickEmoji(1)
        verify(exactly = 0) { view.getActivity() }
        verify(exactly = 0) { view.startActivityForResult(any(), any()) }
    }

    @Test
    fun `run and verify of onClickEmoji but no view`() {
        mockkStatic(ContactUsTracking::class)
        every { ContactUsTracking.sendGTMInboxTicket(any(), any(), any(), any(), any()) } just runs
        presenter.onClickEmoji(1)
        verify(exactly = 0) { viewNullable?.getActivity() }
        verify(exactly = 0) { viewNullable?.startActivityForResult(any(), any()) }
    }

    @Test
    fun `run and verify of onClickEmoji but no view for getcommentId and mTicketDetail badCsatReasonList is null`() {
        mockkStatic(ContactUsTracking::class)
        every { ContactUsTracking.sendGTMInboxTicket(any(), any(), any(), any(), any()) } just runs
        every { viewNullable?.getCommentID() } returns null
        every { presenter.mTicketDetail?.comments } returns null
        every { presenter.mTicketDetail?.number } returns "1"
        presenter.onClickEmoji(1)
        verify(exactly = 0) { viewNullable?.getActivity() }
        verify(exactly = 0) { viewNullable?.startActivityForResult(any(), any()) }
    }


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
        verify { view.onSuccessSubmitOfRating(InboxDetailPresenter.KEY_LIKED, 0) }

    }

    @Test
    fun `check invocation of hideProgressBar on invocation of onClick when exception`() {
        presenter.attachView(view)
        coEvery { submitRatingUseCase.getChipInboxDetail(any()) } throws Exception("my exception")

        presenter.onClick(true, 0, "dummy id")

        verify { view.hideProgressBar() }
    }


    @Test
    fun `check showErrorMessage on closeTicket`() {
        presenter.attachView(view)
        val response = mockk<ChipGetInboxDetail>(relaxed = true)

        coEvery { closeTicketByUserUseCase.getChipInboxDetail(any()) } returns response
        every { response.messageError } returns listOf("error")

        presenter.closeTicket()

        verify { view.showErrorMessage("error") }

    }

    @Test
    fun `check OnSucessfullTicketClose on closeTicket`() {
        presenter.attachView(view)
        val response = mockk<ChipGetInboxDetail>(relaxed = true)

        coEvery { closeTicketByUserUseCase.getChipInboxDetail(any()) } returns response
        every { response.messageError } returns listOf()

        presenter.closeTicket()

        verify { view.OnSucessfullTicketClose() }

    }

    @Test
    fun `check closeTicket`() {
        presenter.attachView(view)
        coEvery { closeTicketByUserUseCase.getChipInboxDetail(any()) } throws Exception("my exception")

        presenter.closeTicket()

        verify { view.hideProgressBar() }

    }

    @Test
    fun `check getUserId`() {
        every { userSession.userId } returns "123"

        val actual = presenter.getUserId()

        assertEquals("123", actual)
    }

    @Test
    fun `check value of searchIndices on getNextResult`() {
        presenter.attachView(view)
        every { presenter.searchIndices } returns arrayListOf(1, 2)

        val result = presenter.getNextResult()

        assertEquals(result, 2)
        assertEquals(presenter.searchIndices[1], 2)
        verify { view.setCurrentRes(2) }

    }

    @Test
    fun `check invocation of showMessage on getNextResult when searchIndices size is 1 `() {
        presenter.attachView(view)
        every { presenter.searchIndices } returns arrayListOf(1)
        every { presenter.next } returns 0

        val result = presenter.getNextResult()

        verify { view.showMessage(any()) }
        assertEquals(result, -1)

    }

    @Test
    fun `check showMessage on invocation of getNextResult when searchIndices size is 0`() {
        presenter.attachView(view)
        every { presenter.searchIndices } returns arrayListOf()

        val result = presenter.getNextResult()

        verify { view.showMessage(any()) }
        assertEquals(result, -1)

    }

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

    @Test
    fun `invocation addNewLocalComment`() {
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

    @Test
    fun `run and verify of getSecurelyUploadedImages return is Null`() {
        runBlockingTest {
            val files = mockk<ArrayList<String>>(relaxed = true)
            val chipUploadHostConfig = mockk<ChipUploadHostConfig>(relaxed = true)
            val actiualResult =
                presenter.getSecurelyUploadedImages(files = files, chipUploadHostConfig)
            Assert.assertNull(actiualResult)
        }
    }

    @Test
    fun `run and verify of getSecurelyUploadedImages with SecureUploadUseCase error and method return is Null`() {
        runBlockingTest {
            val files = arrayListOf("", "", "")
            val secureReturnGqlUseCase = SecureImageParameter(
                mockk(relaxed = true), mockk(relaxed = true), "", ""
            )
            val chipUploadHostConfig = mockk<ChipUploadHostConfig>(relaxed = true)

            coEvery {
                secureUploadUseCase.getSecureImageParameter(
                    any(),
                    any()
                )
            } returns secureReturnGqlUseCase
            coEvery { secureReturnGqlUseCase.imageData?.isSuccess } returns 2
            val actiualResult =
                presenter.getSecurelyUploadedImages(files = files, chipUploadHostConfig)
            Assert.assertNull(actiualResult)
        }
    }

    @Test
    fun `run and verify of getSecurelyUploadedImages with SecureUploadUseCase success but contactUsUploadImageUseCase is Empty and method return is Null`() {
        runBlockingTest {
            val files = arrayListOf("", "", "")
            val secureReturnGqlUseCase = SecureImageParameter(
                mockk(relaxed = true), mockk(relaxed = true), "", ""
            )
            val chipUploadHostConfig = mockk<ChipUploadHostConfig>(relaxed = true)

            coEvery {
                secureUploadUseCase.getSecureImageParameter(
                    any(),
                    any()
                )
            } returns secureReturnGqlUseCase
            coEvery { secureReturnGqlUseCase.imageData?.isSuccess } returns SUCCESS_KEY_SECURE_IMAGE_PARAMETER
            coEvery { uploadImageUseCase.uploadFile("", any(), any(), any()) } returns arrayListOf()

            val actiualResult =
                presenter.getSecurelyUploadedImages(files = files, chipUploadHostConfig)
            Assert.assertNull(actiualResult)
        }
    }

    @Test
    fun `run and verify of getSecurelyUploadedImages with SecureUploadUseCase success but contactUsUploadImageUseCase is Not Empty and method return is not null`() {
        runBlockingTest {
            val files = arrayListOf("", "", "")
            val secureReturnGqlUseCase = SecureImageParameter(
                mockk(relaxed = true), mockk(relaxed = true), "", ""
            )
            val chipUploadHostConfig = mockk<ChipUploadHostConfig>(relaxed = true)

            coEvery {
                secureUploadUseCase.getSecureImageParameter(
                    any(),
                    any()
                )
            } returns secureReturnGqlUseCase
            coEvery { secureReturnGqlUseCase.imageData?.isSuccess } returns SUCCESS_KEY_SECURE_IMAGE_PARAMETER
            coEvery { uploadImageUseCase.uploadFile("", any(), any(), any()) } returns arrayListOf(
                mockk(relaxed = true), mockk(relaxed = true)
            )

            val actiualResult =
                presenter.getSecurelyUploadedImages(files = files, chipUploadHostConfig)
            Assert.assertNotNull(actiualResult)
        }
    }

    @Test
    fun `check handleErrorState from sendMessageWithImages when FAILURE_KEY_UPLOAD_HOST_CONFIG, message is empty`(){
        presenter.attachView(view)
        val response : ChipUploadHostConfig = mockk(relaxed = true)
        coEvery { chipUploadHostConfigUseCase.getChipUploadHostConfig() } returns response
        every { response.chipUploadHostConfig?.chipUploadHostConfigData?.generatedHost?.serverId } returns "0"
        every { response.chipUploadHostConfig?.messageError } returns arrayListOf()
        every { presenter.isUploadImageValid } returns 1
        presenter.sendMessage()
        verify { view.setSnackBarErrorMessage(any(), any()) }
    }

    @Test
    fun `check handleErrorState from sendMessageWithImages when FAILURE_KEY_UPLOAD_HOST_CONFIG, message is empty, without view`(){
        val response : ChipUploadHostConfig = mockk(relaxed = true)
        coEvery { chipUploadHostConfigUseCase.getChipUploadHostConfig() } returns response
        every { response.chipUploadHostConfig?.chipUploadHostConfigData?.generatedHost?.serverId } returns "0"
        every { response.chipUploadHostConfig?.messageError } returns arrayListOf()
        every { presenter.isUploadImageValid } returns 1
        presenter.sendMessage()
        verify(exactly = 0) { viewNullable?.setSnackBarErrorMessage(any(), any()) }

    }

    @Test
    fun `check handleErrorState from sendMessageWithImages when FAILURE_KEY_UPLOAD_HOST_CONFIG, message is not empty`(){
        presenter.attachView(view)
        val response : ChipUploadHostConfig = mockk(relaxed = true)
        coEvery { chipUploadHostConfigUseCase.getChipUploadHostConfig() } returns response
        every { response.chipUploadHostConfig?.chipUploadHostConfigData?.generatedHost?.serverId } returns "0"
        every { response.chipUploadHostConfig?.messageError } returns arrayListOf("Error woi")
        every { presenter.isUploadImageValid } returns 1
        presenter.sendMessage()
        verify { view.setSnackBarErrorMessage(any(), any()) }
    }

    @Test
    fun `check handleErrorState from sendMessageWithImages when FAILURE_KEY_UPLOAD_HOST_CONFIG, message is not empty, without view`(){
        val response : ChipUploadHostConfig = mockk(relaxed = true)
        coEvery { chipUploadHostConfigUseCase.getChipUploadHostConfig() } returns response
        every { response.chipUploadHostConfig?.chipUploadHostConfigData?.generatedHost?.serverId } returns "0"
        every { response.chipUploadHostConfig?.messageError } returns arrayListOf("Error woi")
        every { presenter.isUploadImageValid } returns 1
        presenter.sendMessage()
        verify(exactly = 0) { viewNullable?.setSnackBarErrorMessage(any(), any()) }

    }

    @Test
    fun `check handleErrorState from sendMessageWithImages when not FAILURE_KEY_UPLOAD_HOST_CONFIG, and postMessageUseCase is ok`(){
        presenter.attachView(view)
        val response : ChipUploadHostConfig = mockk(relaxed = true)
        val response2 : TicketReplyResponse = mockk(relaxed = true)
        response2.apply {
            TicketReplyResponse.TicketReply(
                TicketReplyResponse.TicketReply.Data()
            )
        }
        coEvery { chipUploadHostConfigUseCase.getChipUploadHostConfig() } returns response
        every { response.chipUploadHostConfig?.chipUploadHostConfigData?.generatedHost?.serverId } returns "1"
        every { presenter.isUploadImageValid } returns 1
        coEvery { presenter.getSecurelyUploadedImages(any(), any()) } returns arrayListOf(
            ImageUpload()
        )
        coEvery {
            postMessageUseCase.getCreateTicketResult(any())
                .getData<TicketReplyResponse>(TicketReplyResponse::class.java)
        } returns response2
        coEvery { response2.ticketReply?.ticketReplyData?.status } returns "OK"
        presenter.sendMessage()
        verify { view.setSnackBarErrorMessage(any(), any()) }

    }

    @Test
    fun `check handleErrorState from sendMessageWithImages when not FAILURE_KEY_UPLOAD_HOST_CONFIG NOT, and postMessageUseCase OK`(){
        presenter.attachView(view)
        val response : ChipUploadHostConfig = mockk(relaxed = true)
        val response2 : TicketReplyResponse = mockk(relaxed = true)

        coEvery { chipUploadHostConfigUseCase.getChipUploadHostConfig() } returns response
        every { response.chipUploadHostConfig?.chipUploadHostConfigData?.generatedHost?.serverId } returns "1"
        every { presenter.isUploadImageValid } returns 1
        coEvery { presenter.getSecurelyUploadedImages(any(), any()) } returns arrayListOf(
            ImageUpload()
        )
        coEvery {
            postMessageUseCase.getCreateTicketResult(any())
                .getData<TicketReplyResponse>(TicketReplyResponse::class.java)
        } returns response2
        coEvery { response2.ticketReply?.ticketReplyData?.status } returns "NOT OK"
        presenter.sendMessage()
        verify { view.setSnackBarErrorMessage(any(), any()) }
    }

    @Test
    fun `check handleErrorState from sendMessageWithImages when not FAILURE_KEY_UPLOAD_HOST_CONFIG, and postMessageUseCase is ok, view is null`(){
        val response : ChipUploadHostConfig = mockk(relaxed = true)
        val response2 : TicketReplyResponse = mockk(relaxed = true)
        response2.apply {
            TicketReplyResponse.TicketReply(
                TicketReplyResponse.TicketReply.Data()
            )
        }
        coEvery { chipUploadHostConfigUseCase.getChipUploadHostConfig() } returns response
        every { response.chipUploadHostConfig?.chipUploadHostConfigData?.generatedHost?.serverId } returns "1"
        every { presenter.isUploadImageValid } returns 1
        coEvery { presenter.getSecurelyUploadedImages(any(), any()) } returns arrayListOf(
            ImageUpload()
        )
        coEvery {
            postMessageUseCase.getCreateTicketResult(any())
                .getData<TicketReplyResponse>(TicketReplyResponse::class.java)
        } returns response2
        coEvery { response2.ticketReply?.ticketReplyData?.status } returns "OK"
        presenter.sendMessage()
        verify(exactly = 0) { viewNullable?.setSnackBarErrorMessage(any(), any()) }

    }

    @Test
    fun `check handleErrorState from sendMessageWithImages when not FAILURE_KEY_UPLOAD_HOST_CONFIG NOT, and postMessageUseCase OK, view is null`(){
        val response : ChipUploadHostConfig = mockk(relaxed = true)
        val response2 : TicketReplyResponse = mockk(relaxed = true)

        coEvery { chipUploadHostConfigUseCase.getChipUploadHostConfig() } returns response
        every { response.chipUploadHostConfig?.chipUploadHostConfigData?.generatedHost?.serverId } returns "1"
        every { presenter.isUploadImageValid } returns 1
        coEvery { presenter.getSecurelyUploadedImages(any(), any()) } returns arrayListOf(
            ImageUpload()
        )
        coEvery {
            postMessageUseCase.getCreateTicketResult(any())
                .getData<TicketReplyResponse>(TicketReplyResponse::class.java)
        } returns response2
        coEvery { response2.ticketReply?.ticketReplyData?.status } returns "NOT OK"
        presenter.sendMessage()
        verify(exactly = 0) { viewNullable?.setSnackBarErrorMessage(any(), any()) }
    }

    @Test
    fun `check handleErrorState from sendMessage when it error`(){
        presenter.attachView(view)
        val graphqlError = GraphqlError()
        graphqlError.message = "Error yakk"
        val response = mockk<TicketReplyResponse>(relaxed = true)
        coEvery {
            postMessageUseCase.getCreateTicketResult(any())
                .getData<TicketReplyResponse>(TicketReplyResponse::class.java)
        } returns response
        coEvery {
            postMessageUseCase.getCreateTicketResult(any()).getError(TicketReplyResponse::class.java)
        } returns arrayListOf(graphqlError)
        coEvery { response.ticketReply?.ticketReplyData?.status } returns "NOT OK"
        every { presenter.isUploadImageValid } returns 0
        presenter.sendMessage()
        verify { view.setSnackBarErrorMessage("Error yakk", any()) }
    }

    @Test
    fun `check handleErrorState from sendMessage when it error but view is null`(){
        val graphqlError = GraphqlError()
        graphqlError.message = "Error yakk"
        val response = mockk<TicketReplyResponse>(relaxed = true)
        coEvery {
            postMessageUseCase.getCreateTicketResult(any())
                .getData<TicketReplyResponse>(TicketReplyResponse::class.java)
        } returns response
        coEvery {
            postMessageUseCase.getCreateTicketResult(any()).getError(TicketReplyResponse::class.java)
        } returns arrayListOf(graphqlError)
        coEvery { response.ticketReply?.ticketReplyData?.status } returns "NOT OK"
        every { presenter.isUploadImageValid } returns 0
        presenter.sendMessage()
        verify(exactly = 0) { viewNullable?.setSnackBarErrorMessage("Error yakk", any()) }
    }

    @Test
    fun `check handleErrorState from sendMessage when error but message is empty`(){
        presenter.attachView(view)
        val response = mockk<TicketReplyResponse>(relaxed = true)
        coEvery {
            postMessageUseCase.getCreateTicketResult(any())
                .getData<TicketReplyResponse>(TicketReplyResponse::class.java)
        } returns response
        coEvery {
            postMessageUseCase.getCreateTicketResult(any()).getError(TicketReplyResponse::class.java)
        } returns null
        coEvery { response.ticketReply?.ticketReplyData?.status } returns "NOT OK"
        every { presenter.isUploadImageValid } returns 0
        every { view.getActivity().getString(any()) } returns "Pesanmu gagal dikirim, coba kirim ulang"
        presenter.sendMessage()
        verify { view.setSnackBarErrorMessage("Pesanmu gagal dikirim, coba kirim ulang", any()) }
    }

    @Test
    fun `check handleErrorState from sendMessage when error but message is empty and view null`(){
        val response = mockk<TicketReplyResponse>(relaxed = true)
        coEvery {
            postMessageUseCase.getCreateTicketResult(any())
                .getData<TicketReplyResponse>(TicketReplyResponse::class.java)
        } returns response
        coEvery {
            postMessageUseCase.getCreateTicketResult(any()).getError(TicketReplyResponse::class.java)
        } returns null
        coEvery { response.ticketReply?.ticketReplyData?.status } returns "NOT OK"
        every { presenter.isUploadImageValid } returns 0
        every { view.getActivity().getString(any()) } returns "Pesanmu gagal dikirim, coba kirim ulang"
        presenter.sendMessage()
        verify(exactly = 0) { viewNullable?.setSnackBarErrorMessage("Pesanmu gagal dikirim, coba kirim ulang", any()) }
    }

}
