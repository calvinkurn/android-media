package com.tokopedia.contactus.inboxticket2.view.presenter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.text.Editable
import android.text.TextWatcher
import android.view.MenuItem
import android.view.View
import com.google.gson.reflect.TypeToken
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.common.network.data.model.RestResponse
import com.tokopedia.contactus.R
import com.tokopedia.contactus.common.analytics.ContactUsTracking
import com.tokopedia.contactus.common.analytics.InboxTicketTracking
import com.tokopedia.contactus.inboxticket2.data.ImageUpload
import com.tokopedia.contactus.inboxticket2.data.model.Tickets
import com.tokopedia.contactus.inboxticket2.domain.*
import com.tokopedia.contactus.inboxticket2.domain.usecase.*
import com.tokopedia.contactus.inboxticket2.view.activity.*
import com.tokopedia.contactus.inboxticket2.view.contract.InboxBaseContract.InboxBaseView
import com.tokopedia.contactus.inboxticket2.view.contract.InboxDetailContract
import com.tokopedia.contactus.inboxticket2.view.contract.InboxDetailContract.InboxDetailView
import com.tokopedia.contactus.inboxticket2.view.customview.CustomEditText
import com.tokopedia.contactus.inboxticket2.view.fragment.InboxBottomSheetFragment
import com.tokopedia.contactus.inboxticket2.view.utils.CLOSED
import com.tokopedia.contactus.inboxticket2.view.utils.NEW
import com.tokopedia.contactus.inboxticket2.view.utils.OPEN
import com.tokopedia.contactus.inboxticket2.view.utils.SOLVED
import com.tokopedia.contactus.inboxticket2.view.utils.Utils
import com.tokopedia.csat_rating.presenter.BaseProvideRatingFragmentPresenter.Companion.EMOJI_STATE
import com.tokopedia.csat_rating.presenter.BaseProvideRatingFragmentPresenter.Companion.SELECTED_ITEM
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.launch_cache_error.launchCatchError
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.*
import rx.Subscriber
import java.lang.reflect.Type
import java.util.*
import kotlin.collections.ArrayList
import kotlin.coroutines.CoroutineContext

private const val AGENT = "agent"
private const val REPLY_TICKET_RESPONSE_STATUS = "OK"
private const val ROLE_TYPE_CUSTOMER = "customer"

class InboxDetailPresenter(private val postMessageUseCase: PostMessageUseCase,
                           private val postMessageUseCase2: PostMessageUseCase2,
                           private val postRatingUseCase: PostRatingUseCase,
                           private val inboxOptionUseCase: InboxOptionUseCase,
                           private val submitRatingUseCase: SubmitRatingUseCase,
                           private val closeTicketByUserUseCase: CloseTicketByUserUseCase,
                           private val contactUsUploadImageUseCase: ContactUsUploadImageUseCase,
                           private val userSession: UserSessionInterface,
                           private val dispatcher: CoroutineDispatchers) : InboxDetailContract.Presenter, CustomEditText.Listener, CoroutineScope {
    private var mView: InboxDetailView? = null
    var mTicketDetail: Tickets? = null
        get() = field
        private set
    val searchIndices: ArrayList<Int> by lazy { ArrayList<Int>() }
    private val no = "NO"
    private val rateCommentID: String? = null
    var next = 0
        get() = field
        private set
    private val reasonList: ArrayList<String> by lazy { ArrayList<String>() }
    private var isIssueClosed = false


    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + SupervisorJob()

    override fun attachView(view: InboxBaseView?) {
        mView = view as InboxDetailView
        reasonList.addAll(Arrays.asList(*mView?.getActivity()?.resources?.getStringArray(R.array.bad_reason_array)))
    }


    override fun detachView() {}
    override fun getSearchListener(): CustomEditText.Listener {
        return this
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == InboxBaseView.REQUEST_SUBMIT_FEEDBACK && resultCode == Activity.RESULT_OK) {
            submitCsatRating(data)
        }
    }

    private fun submitCsatRating(data: Intent?) {

        launchCatchError(
                block = {
                    mView?.showProgressBar()
                    val requestParams = submitRatingUseCase.createRequestParams(mView?.getCommentID()
                            ?: "",
                            data?.extras?.getInt(EMOJI_STATE) ?: 0,
                            data?.getStringExtra(SELECTED_ITEM) ?: "")

                    val chipGetInboxDetail = submitRatingUseCase.getChipInboxDetail(requestParams)

                    if (chipGetInboxDetail?.messageError?.size ?: 0 > 0) {
                        mView?.showErrorMessage(chipGetInboxDetail?.messageError?.get(0))
                        mView?.hideProgressBar()
                    } else {
                        mView?.hideProgressBar()
                        mView?.showMessage(mView?.getActivity()?.getString(R.string.cu_terima_kasih_atas_masukannya)
                                ?: "")
                        mView?.showIssueClosed()
                        isIssueClosed = true
                        mView?.updateClosedStatus()
                    }

                },
                onError = {
                    mView?.hideProgressBar()
                    it.printStackTrace()
                }
        )
    }

    override fun onDestroy() {}

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if (item.itemId == R.id.action_search) {
            mView?.toggleSearch(View.VISIBLE)
            true
        } else if (item.itemId == android.R.id.home) {
            if (mView?.isSearchMode() == true) {
                mView?.toggleSearch(View.GONE)
                if (mTicketDetail?.isShowRating == true) {
                    mView?.toggleTextToolbar(View.GONE)
                } else if (mTicketDetail?.status.equals(CLOSED, ignoreCase = true)
                        && mTicketDetail?.isShowRating != true) {
                    mView?.showIssueClosed()
                } else {
                    mView?.toggleTextToolbar(View.VISIBLE)
                }
                mView?.clearSearch()
                mView?.exitSearchMode()
                true
            } else {
                false
            }
        } else {
            false
        }
    }

    override fun reAttachView() {}
    override fun clickCloseSearch() {}
    override fun refreshLayout() {
        getTicketDetails(getTicketId())
    }

    override fun onSearchSubmitted(text: String) {
        var mText = text
        mText = mText.trim()
        if (mText.isNotEmpty()) {
            search(mText)
        } else {
            searchIndices.clear()
        }
    }

    override fun onSearchTextChanged(text: String) {
        onSearchSubmitted(text)
    }

    override fun getTicketDetails(ticketId: String?) {

        mView?.showProgressBar()
        launchCatchError(
                block = {
                    val requestParams = inboxOptionUseCase.createRequestParams(ticketId)
                    val chipGetInboxDetail = inboxOptionUseCase.getChipInboxDetail(requestParams)
                    if (chipGetInboxDetail?.data?.isSuccess == 1) {
                        mTicketDetail = chipGetInboxDetail.data?.tickets
                        val commentsItems = mTicketDetail?.comments ?: mutableListOf()
                        addItemAtTopOfComment(commentsItems, getTopItem(mTicketDetail))
                        addItemAtTopOfComment(commentsItems, getCommentHeader(mTicketDetail))
                        for (item in commentsItems) {
                            val createTime = item.createTime?.let { getUtils().getDateTime(it) }
                            item.createTime = createTime
                            item.shortTime = createTime?.let { getShortTime(it) }
                        }
                        if (isIssueClosed) {
                            mTicketDetail?.isShowRating = false
                            isIssueClosed = false
                        }
                        mTicketDetail?.let { mView?.renderMessageList(it) }
                        mView?.hideProgressBar()
                    } else if (chipGetInboxDetail?.data?.isSuccess == 0) {
                        mView?.showNoTicketView(chipGetInboxDetail.messageError)
                    }

                },
                onError = {
                    it.printStackTrace()
                }
        )
    }

    private fun getCommentHeader(mTicketDetail: Tickets?): CommentsItem {
        return CommentsItem().apply {
            ticketTitle = mTicketDetail?.subject
            ticketId = mTicketDetail?.number
            this.ticketStatus = getStatus()
        }
    }

    private fun getStatus(): String {
        val status = getTicketStatus()
        return when {
            status.equals(SOLVED, ignoreCase = true) ||
                    status.equals(OPEN, ignoreCase = true) ||
                    status.equals(NEW, ignoreCase = true) -> {
                TICKET_STATUS_IN_PROCESS
            }
            status.equals(CLOSED, ignoreCase = true) &&
                    mTicketDetail?.isShowRating == false -> {
                TICKET_STATUS_CLOSED
            }
            mTicketDetail?.isShowRating == true -> {
                TICKET_STATUS_NEED_RATING
            }
            else -> {
                ""
            }
        }
    }

    override fun getTicketId(): String? {
        var tId = mView?.getActivity()?.intent?.getStringExtra(InboxDetailActivity.PARAM_TICKET_T_ID)
        if (tId == null) {
            tId = mView?.getActivity()?.intent?.getStringExtra(InboxDetailActivity.PARAM_TICKET_ID)
        }
        return tId
    }

    private fun getShortTime(createTime: String): String {
        var count = 0
        var i = 0
        while (i < createTime.length) {
            val c = createTime.get(i)
            if (c == ' ') {
                count++
                if (count == 2) break
            }
            i++
        }
        return if (createTime.isNotEmpty()) createTime.substring(0, i) else ""
    }

    private fun addItemAtTopOfComment(commentsItems: MutableList<CommentsItem>?, topItem: CommentsItem) {
        var mCommentsItem = commentsItems
        if (mCommentsItem == null) {
            mCommentsItem = ArrayList()
            mCommentsItem.add(topItem)
        } else {
            mCommentsItem.add(0, topItem)
        }
    }

    private fun getTopItem(mTicketDetail: Tickets?): CommentsItem {
        val topItem = CommentsItem()
        topItem.attachment = mTicketDetail?.attachment
        topItem.message = mTicketDetail?.message
        topItem.messagePlaintext = mTicketDetail?.message
        topItem.createdBy = mTicketDetail?.createdBy
        topItem.createTime = mTicketDetail?.createTime
        return topItem
    }

    override fun onImageSelect(image: ImageUpload) {
        if (image.fileLoc?.let { getUtils().fileSizeValid(it) } == false) {
            showErrorMessage(MESSAGE_WRONG_FILE_SIZE)
            ContactUsTracking.sendGTMInboxTicket(mView?.getActivity(), "",
                    InboxTicketTracking.Category.EventInboxTicket,
                    InboxTicketTracking.Action.EventClickAttachImage,
                    InboxTicketTracking.Label.ImageError1)
        } else if (image.fileLoc?.let { getUtils().isBitmapDimenValid(it) } == false) {
            showErrorMessage(MESSAGE_WRONG_DIMENSION)
            ContactUsTracking.sendGTMInboxTicket(mView?.getActivity(), "",
                    InboxTicketTracking.Category.EventInboxTicket,
                    InboxTicketTracking.Action.EventClickAttachImage,
                    InboxTicketTracking.Label.ImageError2)
        } else {
            mView?.addImage(image)
        }
    }

    private fun showErrorMessage(messageWrongParam: Int) {
        if (messageWrongParam == MESSAGE_WRONG_FILE_SIZE) {
            mView?.setSnackBarErrorMessage(mView?.getActivity()?.getString(R.string.error_msg_wrong_size)
                    ?: "", true)
        } else if (messageWrongParam == MESSAGE_WRONG_DIMENSION) {
            mView?.setSnackBarErrorMessage(mView?.getActivity()?.getString(R.string.error_msg_wrong_height_width)
                    ?: "", true)
        }
    }

    override fun watcher(): TextWatcher {
        return object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (s.length >= 15) {
                    mView?.setSubmitButtonEnabled(true)
                } else {
                    mView?.setSubmitButtonEnabled(false)
                }
            }

            override fun afterTextChanged(s: Editable) {}
        }
    }

    override fun sendMessage() {
        mView?.showSendProgress()
        val isValid = isUploadImageValid
        when {
            isValid >= 1 -> sendMessageWithImages()
            isValid == 0 -> sendTextMessage()
            isValid == -1 -> mView?.setSnackBarErrorMessage(mView?.getActivity()?.resources?.getString(R.string.invalid_images)
                    ?: "", true)

        }
    }

    private fun sendTextMessage() {
        launchCatchError(
                block = {
                    mView?.hideSendProgress()
                    val requestParam = postMessageUseCase.createRequestParams(mTicketDetail?.id
                            ?: "", mView?.userMessage
                            ?: "", 0, "", getLastReplyFromAgent(), userSession.userId)
                    val replyTicketResponse = postMessageUseCase.getCreateTicketResult(requestParam)
                    if (replyTicketResponse.ticketReply?.ticketReplyData?.status.equals(REPLY_TICKET_RESPONSE_STATUS)) {
                        addNewLocalComment()
                    } else {
                        mView?.setSnackBarErrorMessage(mView?.getActivity()?.getString(R.string.contact_us_sent_error_message)
                                ?: "", true)
                    }

                },
                onError = {
                    mView?.hideSendProgress()
                    it.printStackTrace()
                }
        )
    }

    private fun getLastReplyFromAgent(): String {
        var reply = ""
        run loop@{
            mTicketDetail?.comments?.reversed()?.forEach  { comment ->
                if (comment.createdBy?.role == AGENT) {
                    reply = comment.message ?: ""
                    return@loop
                }
            }
        }
        return reply
    }

    private fun sendMessageWithImages() {
        launchCatchError(
                block = {
                    val files = contactUsUploadImageUseCase.getFile(mView?.imageList)
                    val list = arrayListOf<ImageUpload>()

                    withContext(dispatcher.io) {
                        list.addAll(contactUsUploadImageUseCase.uploadFile(
                                userSession.userId,
                                mView?.imageList,
                                files))
                    }
                    val requestParam = postMessageUseCase.createRequestParams(
                            mTicketDetail?.id ?: "",
                            mView?.userMessage ?: "",
                            1,
                            getUtils().getAttachmentAsString(mView?.imageList?: listOf()),
                            getLastReplyFromAgent(),
                            userSession.userId)

                    val createTicketResponse = postMessageUseCase.getCreateTicketResult(requestParam)
                    val ticketReplyData = createTicketResponse.ticketReply?.ticketReplyData
                    if (ticketReplyData?.status.equals(REPLY_TICKET_RESPONSE_STATUS)) {
                        if (ticketReplyData?.postKey?.isNotEmpty() == true) {
                            val requestParams = postMessageUseCase2.createRequestParams(
                                    mTicketDetail?.id ?: "",
                                    userSession.userId,
                                    getUtils().getFileUploaded(list),
                                    ticketReplyData.postKey)
                            sendImages(requestParams)
                        } else {
                            mView?.hideSendProgress()
                            mView?.setSnackBarErrorMessage(mView?.getActivity()?.getString(R.string.contact_us_sent_error_message)
                                    ?: "", true)
                        }
                    } else {
                        mView?.hideSendProgress()
                        mView?.setSnackBarErrorMessage(mView?.getActivity()?.getString(R.string.contact_us_sent_error_message)
                                ?: "", true)
                    }

                },
                onError = {
                    mView?.hideSendProgress()
                    mView?.setSnackBarErrorMessage(mView?.getActivity()?.getString(R.string.contact_us_sent_error_message)
                            ?: "", true)
                    it.printStackTrace()
                }
        )
    }

    private fun sendImages(requestParams: RequestParams) {
        launchCatchError(
                block = {
                    val stepTwoResponse = postMessageUseCase2.getInboxDataResponse(requestParams)
                    if (stepTwoResponse?.ticketReplyAttach?.ticketReplyAttachData?.isSuccess ?: 0 > 0) {
                        mView?.hideSendProgress()
                        addNewLocalComment()
                    } else {
                        mView?.hideSendProgress()
                        mView?.setSnackBarErrorMessage(mView?.getActivity()?.getString(R.string.contact_us_sent_error_message)?:"", true)
                    }
                },
                onError = {
                    mView?.hideSendProgress()
                    mView?.setSnackBarErrorMessage(mView?.getActivity()?.getString(R.string.contact_us_sent_error_message)
                            ?: "", true)
                    it.printStackTrace()
                })

    }

    override fun getNextResult(): Int {
        return if (searchIndices.size > 0) {
            if (next >= -1 && next < searchIndices.size.minus(1)) {
                next += 1
                mView?.setCurrentRes(next + 1)
                searchIndices[next]
            } else {
                mView?.showMessage(mView?.getActivity()?.resources?.getString(R.string.cu_no_more_results)
                        ?: "")
                -1
            }
        } else {
            mView?.showMessage(mView?.getActivity()?.resources?.getString(R.string.no_search_result)
                    ?: "")
            -1
        }
    }

    override fun getPreviousResult(): Int {
        return if (searchIndices.size > 0) {
            if (next > -1 && next < searchIndices.size) {
                mView?.setCurrentRes(next + 1)
                searchIndices[next--]
            } else {
                mView?.showMessage(mView?.getActivity()?.resources?.getString(R.string.cu_no_more_results)
                        ?: "")
                -1
            }
        } else {
            mView?.showMessage(mView?.getActivity()?.resources?.getString(R.string.no_search_result)
                    ?: "")
            -1
        }
    }

    val isUploadImageValid: Int
        get() {
            val uploadImageList = mView?.imageList
            if (mTicketDetail?.isNeedAttachment == true && (uploadImageList == null || uploadImageList.isEmpty())) {
                showMessageAndSendEvent()
                return -2
            }
            val numOfImages = uploadImageList?.size ?: 0
            if (numOfImages > 0) {
                val count = getUtils().verifyAllImages(uploadImageList ?: listOf())
                return if (numOfImages == count) {
                    numOfImages
                } else {
                    -1
                }
            } else if (numOfImages == 0) {
                return 0
            }
            return -1
        }

    private fun showMessageAndSendEvent() {
        mView?.setSnackBarErrorMessage(mView?.getActivity()?.getString(R.string.attachment_required)
                ?: "", true)
        mView?.hideSendProgress()
        ContactUsTracking.sendGTMInboxTicket(mView?.getActivity(), "",
                InboxTicketTracking.Category.EventInboxTicket,
                InboxTicketTracking.Action.EventNotAttachImageRequired,
                "")
    }

    private fun search(searchText: String) {
        mView?.showProgressBar()
        launchCatchError(
                block = {
                    withContext(dispatcher.default) {
                        initializeIndicesList(searchText)
                        next = 0
                        withContext(Dispatchers.Main) {
                            mView?.hideProgressBar()
                            mView?.enterSearchMode(searchText, searchIndices.size)
                        }
                    }
                    if (searchIndices.size > 0) {
                        ContactUsTracking.sendGTMInboxTicket(mView?.getActivity(), "",
                                InboxTicketTracking.Category.EventInboxTicket,
                                InboxTicketTracking.Action.EventClickSearchDetails,
                                InboxTicketTracking.Label.GetResult)
                    } else {
                        withContext(Dispatchers.Main) {
                            mView?.setSnackBarErrorMessage(mView?.getActivity()?.getString(R.string.no_search_result)
                                    ?: "", false)
                        }
                        ContactUsTracking.sendGTMInboxTicket(mView?.getActivity(), "",
                                InboxTicketTracking.Category.EventInboxTicket,
                                InboxTicketTracking.Action.EventClickSearchDetails,
                                InboxTicketTracking.Label.NoResult)
                    }
                },
                onError = {
                    mView?.hideProgressBar()
                    mView?.setSnackBarErrorMessage("", false)
                    it.printStackTrace()
                }
        )
    }

    private fun initializeIndicesList(searchText: String) {
        searchIndices.clear()
        var count = -1
        mTicketDetail?.comments?.forEach {
            if (getUtils().containsIgnoreCase(it.messagePlaintext
                            ?: "", searchText)) {
                count++
                searchIndices.add(count)
            }
        }

    }

    override fun getUtils() = Utils()

    override fun showImagePreview(position: Int, imagesURL: List<AttachmentItem>) {
        ContactUsTracking.sendGTMInboxTicket(mView?.getActivity(), "",
                InboxTicketTracking.Category.EventInboxTicket,
                InboxTicketTracking.Action.EventClickAttachImage,
                InboxTicketTracking.Label.ImageAttached)
        val imagesUrl = ArrayList<String>()
        for (item in imagesURL) {
            if (item.url?.isNotEmpty() == true)
                imagesUrl.add(item.url ?: "")
            else imagesUrl.add(item.thumbnail ?: "")
        }
        mView?.showImagePreview(position, imagesUrl)
    }

    override fun onClickEmoji(number: Int) {
        sendGTMEventView()
        mView?.startActivityForResult(ContactUsProvideRatingActivity.getInstance(mView?.getActivity() as Context,
                number,
                mView?.getCommentID() ?: "",
                mTicketDetail?.badCsatReasonList
                        ?: arrayListOf()), InboxBaseView.REQUEST_SUBMIT_FEEDBACK)
    }


    override fun onClick(agreed: Boolean, commentPosition: Int, commentId: String) {
        val rating = if (agreed) KEY_LIKED else KEY_DISLIKED
        mView?.showProgressBar()
        launchCatchError(
                block = {
                    val requestParams = submitRatingUseCase.createRequestParams(commentId, rating, "-")
                    val chipGetInboxDetail = submitRatingUseCase.getChipInboxDetail(requestParams)
                    mView?.hideProgressBar()
                    if (chipGetInboxDetail?.messageError?.size ?: 0 > 0) {
                        mView?.showErrorMessage(chipGetInboxDetail?.messageError?.get(0))
                    } else {
                        mView?.onSuccessSubmitOfRating(rating, commentPosition)
                    }
                },
                onError = {
                    mView?.hideProgressBar()
                    it.printStackTrace()
                }
        )
    }

    override fun closeTicket() {
        mView?.showProgressBar()
        launchCatchError(
                block = {
                    mView?.hideProgressBar()
                    val requestParams = closeTicketByUserUseCase.createRequestParams(mView?.ticketID, "mobile")
                    val chipGetInboxDetail = closeTicketByUserUseCase.getChipInboxDetail(requestParams)
                    if (chipGetInboxDetail?.messageError?.size ?: 0 > 0) {
                        mView?.showErrorMessage(chipGetInboxDetail?.messageError?.get(0))
                    } else {
                        mView?.OnSucessfullTicketClose()
                    }
                },
                onError = {
                    mView?.hideProgressBar()
                    it.printStackTrace()
                }
        )
    }

    override fun getTicketStatus(): String {
        return mTicketDetail?.status ?: ""
    }

    private fun addNewLocalComment() {
        val newItem = getNewComment()
        val uploadImageList = mView?.imageList
        val attachmentItems: MutableList<AttachmentItem> = ArrayList()
        if (uploadImageList != null && uploadImageList.isNotEmpty()) {
            for (upload in uploadImageList) {
                val attachment = AttachmentItem()
                attachment.thumbnail = upload.fileLoc
                attachmentItems.add(attachment)
            }
            newItem.attachment = attachmentItems
        }
        mTicketDetail?.isNeedAttachment = false
        mView?.updateAddComment(newItem)
    }

    private fun getNewComment(): CommentsItem {
        val newItem = CommentsItem()
        newItem.createdBy = getCommentCreator()
        newItem.message = mView?.userMessage
        newItem.createTime = getUtils().dateTimeCurrent
        return newItem
    }

    private fun getCommentCreator(): CreatedBy {
        val creator by lazy { CreatedBy() }
        return creator.apply {
            name = userSession.name
            role = ROLE_TYPE_CUSTOMER
            picture = userSession.profilePicture
        }
    }

    private fun sendGTMEventView() {
        ContactUsTracking.sendGTMInboxTicket(mView?.getActivity(), InboxTicketTracking.Event.EventView,
                InboxTicketTracking.Category.EventHelpMessageInbox,
                InboxTicketTracking.Action.EventImpressionOnCsatRating,
                mView?.ticketID)
    }

    override fun getUserId(): String = userSession.userId

    companion object {
        const val KEY_LIKED = 101
        const val KEY_DISLIKED = 102
        const val DELAY_FOUR_MILLIS = 4000
        private const val MESSAGE_WRONG_DIMENSION = 0
        private const val MESSAGE_WRONG_FILE_SIZE = 1
        const val PARAM_IMAGE_ID = "id"
        const val PARAM_WEB_SERVICE = "web_service"
    }

}