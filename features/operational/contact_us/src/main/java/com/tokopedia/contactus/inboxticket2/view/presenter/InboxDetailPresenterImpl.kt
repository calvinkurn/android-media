package com.tokopedia.contactus.inboxticket2.view.presenter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.text.Editable
import android.text.TextWatcher
import android.view.MenuItem
import android.view.View
import com.google.gson.reflect.TypeToken
import com.tokopedia.common.network.data.model.RestResponse
import com.tokopedia.contactus.R
import com.tokopedia.contactus.common.analytics.ContactUsTracking
import com.tokopedia.contactus.common.analytics.InboxTicketTracking
import com.tokopedia.contactus.inboxticket2.data.ImageUpload
import com.tokopedia.contactus.inboxticket2.data.model.Tickets
import com.tokopedia.contactus.inboxticket2.domain.*
import com.tokopedia.contactus.inboxticket2.domain.usecase.*
import com.tokopedia.contactus.inboxticket2.view.activity.ContactUsProvideRatingActivity
import com.tokopedia.contactus.inboxticket2.view.activity.InboxDetailActivity
import com.tokopedia.contactus.inboxticket2.view.contract.InboxBaseContract.InboxBaseView
import com.tokopedia.contactus.inboxticket2.view.contract.InboxDetailContract.InboxDetailPresenter
import com.tokopedia.contactus.inboxticket2.view.contract.InboxDetailContract.InboxDetailView
import com.tokopedia.contactus.inboxticket2.view.customview.CustomEditText
import com.tokopedia.contactus.inboxticket2.view.fragment.InboxBottomSheetFragment
import com.tokopedia.contactus.inboxticket2.view.utils.CLOSED
import com.tokopedia.contactus.inboxticket2.view.utils.OPEN
import com.tokopedia.contactus.inboxticket2.view.utils.SOLVED
import com.tokopedia.contactus.inboxticket2.view.utils.Utils
import com.tokopedia.csat_rating.presenter.BaseProvideRatingFragmentPresenter.Companion.EMOJI_STATE
import com.tokopedia.csat_rating.presenter.BaseProvideRatingFragmentPresenter.Companion.SELECTED_ITEM
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

class InboxDetailPresenterImpl(private val postMessageUseCase: PostMessageUseCase,
                               private val postMessageUseCase2: PostMessageUseCase2,
                               private val postRatingUseCase: PostRatingUseCase,
                               private val inboxOptionUseCase: InboxOptionUseCase,
                               private val submitRatingUseCase: SubmitRatingUseCase,
                               private val closeTicketByUserUseCase: CloseTicketByUserUseCase,
                               private val uploadImageUseCase: UploadImageUseCase,
                               private val userSession: UserSessionInterface,
                               private val defaultDispatcher: CoroutineDispatcher) : InboxDetailPresenter, CustomEditText.Listener, CoroutineScope {
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
    private var userData: CreatedBy? = null
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
                        delay(DELAY_FOUR_MILLIS.toLong())
                        getTicketDetails(getTicketId())
                    }

                },
                onError = {
                    mView?.hideProgressBar()
                    it.printStackTrace()
                }
        )
    }

    override fun onDestroy() {}

    override fun getBottomFragment(resID: Int): InboxBottomSheetFragment? {
        val bottomFragment = InboxBottomSheetFragment.getBottomSheetFragment(resID)
        bottomFragment?.setPresenter(this)
        return bottomFragment
    }

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
                        val commentsItems =
                                getCommentsWithTopItem(mTicketDetail?.comments, getTopItem(mTicketDetail))
                        for (item in commentsItems) {
                            if (userData == null) {
                                if (item.createdBy?.role == "customer") {
                                    userData = item.createdBy
                                }
                            }
                            val createTime = getUtils().getDateTime(item.createTime)
                            item.createTime = createTime
                            item.shortTime = getShortTime(createTime)
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

    override fun getTicketId(): String? {
        var tId = mView?.getActivity()?.intent?.getStringExtra(InboxDetailActivity.PARAM_TICKET_T_ID)
        if (tId == null) {
            tId = mView?.getActivity()?.intent?.getStringExtra(InboxDetailActivity.PARAM_TICKET_ID)
        }
        return tId
    }

    private fun getShortTime(createTime: String?): String? {
        var count = 0
        var i = 0
        while (i < createTime?.length ?: 0) {
            val c = createTime?.get(i)
            if (c == ' ') {
                count++
                if (count == 2) break
            }
            i++
        }
        return createTime?.substring(0, i)

    }

    private fun getCommentsWithTopItem(commentsItems: MutableList<CommentsItem>?, topItem: CommentsItem): MutableList<CommentsItem> {
        var mCommentsItem = commentsItems
        if (mCommentsItem == null) {
            mCommentsItem = ArrayList()
            mCommentsItem.add(topItem)
        } else {
            mCommentsItem.add(0, topItem)
        }
        return mCommentsItem
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
            ContactUsTracking.sendGTMInboxTicket("",
                    InboxTicketTracking.Category.EventInboxTicket,
                    InboxTicketTracking.Action.EventClickAttachImage,
                    InboxTicketTracking.Label.ImageError1)
        } else if (image.fileLoc?.let { getUtils().isBitmapDimenValid(it) } == false) {
            showErrorMessage(MESSAGE_WRONG_DIMENSION)
            ContactUsTracking.sendGTMInboxTicket("",
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
                        mView?.setSnackBarErrorMessage(mView?.getActivity()?.getString(R.string.contact_us_something_went_wrong)
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
                    val networkCalculatorList =
                            uploadImageUseCase.getNetworkCalculatorList(
                                    mView?.imageList)
                    val files = uploadImageUseCase.getFile(mView?.imageList)
                    val list = uploadImageUseCase.uploadFile(
                            mView?.imageList,
                            networkCalculatorList,
                            files,
                    userSession.isLoggedIn)
                    val requestParam = postMessageUseCase.createRequestParams(
                            mTicketDetail?.id ?: "",
                            mView?.userMessage ?: "",
                            1,
                            getUtils().getAttachmentAsString(list),
                            getLastReplyFromAgent(),
                            userSession.userId)

                    val createTicketResponse = postMessageUseCase.getCreateTicketResult(requestParam)
                    val ticketReplyData = createTicketResponse.ticketReply?.ticketReplyData
                    if (ticketReplyData?.status.equals(REPLY_TICKET_RESPONSE_STATUS)) {
                        if (ticketReplyData?.postKey?.isNotEmpty() == true) {
                            val queryMap2 = postMessageUseCase2.setQueryMap(
                                    getUtils().getFileUploaded(list),
                                    ticketReplyData.postKey)
                            sendImages(queryMap2)
                        } else {
                            mView?.hideSendProgress()
                            mView?.setSnackBarErrorMessage(mView?.getActivity()?.getString(R.string.contact_us_something_went_wrong)
                                    ?: "", true)
                        }
                    } else {
                        mView?.hideSendProgress()
                        mView?.setSnackBarErrorMessage(mView?.getActivity()?.getString(R.string.contact_us_something_went_wrong)
                                ?: "", true)
                    }

                },
                onError = {
                    mView?.hideSendProgress()
                    mView?.setSnackBarErrorMessage(it.message.toString(), true)
                    it.printStackTrace()
                }
        )
    }

    private fun sendImages(queryMap: MutableMap<String, Any>) {
        launchCatchError(
                block = {
                    val response = postMessageUseCase2.getInboxDataResponse(queryMap)
                    val stepTwoResponse = response?.data as StepTwoResponse
                    if (stepTwoResponse.isSuccess > 0) {
                        mView?.hideSendProgress()
                        addNewLocalComment()
                    } else {
                        mView?.hideSendProgress()
                        mView?.setSnackBarErrorMessage(response.errorMessage?.get(0) ?: "", true)
                    }
                },
                onError = {
                    mView?.hideSendProgress()
                    mView?.setSnackBarErrorMessage(it.message.toString(), true)
                    it.printStackTrace()
                })

    }

    override fun setBadRating(position: Int) {
        if (position in 1..6) {
            postRatingUseCase.setQueryMap(rateCommentID, no, 1, position, "")
            mView?.showProgressBar()
            mView?.toggleTextToolbar(View.VISIBLE)
            sendRating()
            ContactUsTracking.sendGTMInboxTicket("",
                    InboxTicketTracking.Category.EventInboxTicket,
                    InboxTicketTracking.Action.EventClickReason,
                    reasonList[position - 1])
        }
    }

    override fun sendCustomReason(customReason: String) {
        mView?.showSendProgress()
        postRatingUseCase.setQueryMap(rateCommentID, no, 1, 7, customReason)
        sendRating()
        ContactUsTracking.sendGTMInboxTicket("",
                InboxTicketTracking.Category.EventInboxTicket,
                InboxTicketTracking.Action.EventClickReason,
                customReason)
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
        ContactUsTracking.sendGTMInboxTicket("",
                InboxTicketTracking.Category.EventInboxTicket,
                InboxTicketTracking.Action.EventNotAttachImageRequired,
                "")
    }


    private fun sendRating() {
        postRatingUseCase.execute(object : Subscriber<Map<Type, RestResponse>>() {
            override fun onCompleted() {}
            override fun onError(e: Throwable) {
                e.printStackTrace()
            }

            override fun onNext(typeRestResponseMap: Map<Type, RestResponse>) {
                mView?.hideProgressBar()
                mView?.hideSendProgress()
                val token = object : TypeToken<InboxDataResponse<RatingResponse?>?>() {}.type
                val res1 = typeRestResponseMap[token]
                val ticketListResponse: InboxDataResponse<*> = res1?.getData() as InboxDataResponse<*>
                val ratingResponse = ticketListResponse.data as RatingResponse
                if (ratingResponse.isSuccess > 0) {
                    mView?.hideBottomFragment()
                    mView?.showMessage(mView?.getActivity()?.getString(R.string.thanks_input) ?: "")
                    if (mTicketDetail?.status == OPEN || mTicketDetail?.status == SOLVED) {
                        mView?.toggleTextToolbar(View.VISIBLE)
                    } else {
                        mView?.showIssueClosed()
                        mView?.updateClosedStatus(mTicketDetail?.subject)
                    }
                } else {
                    mView?.setSnackBarErrorMessage(ticketListResponse.errorMessage?.get(0)
                            ?: "", true)
                    mView?.toggleTextToolbar(View.GONE)
                }
            }
        })
    }

    private fun search(searchText: String) {
        mView?.showProgressBar()
        launchCatchError(
                block = {
                    withContext(defaultDispatcher) {
                        initializeIndicesList(searchText)
                        next = 0
                        withContext(Dispatchers.Main) {
                            mView?.hideProgressBar()
                            mView?.enterSearchMode(searchText, searchIndices.size)
                        }
                    }
                    if (searchIndices.size > 0) {
                        ContactUsTracking.sendGTMInboxTicket("",
                                InboxTicketTracking.Category.EventInboxTicket,
                                InboxTicketTracking.Action.EventClickSearchDetails,
                                InboxTicketTracking.Label.GetResult)
                    } else {
                        withContext(Dispatchers.Main) {
                            mView?.setSnackBarErrorMessage(mView?.getActivity()?.getString(R.string.no_search_result)
                                    ?: "", false)
                        }
                        ContactUsTracking.sendGTMInboxTicket("",
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
        ContactUsTracking.sendGTMInboxTicket("",
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
        mTicketDetail?.comments?.add(newItem)
        mTicketDetail?.isNeedAttachment = false
        mView?.updateAddComment()
    }

    private fun getNewComment(): CommentsItem {
        val newItem = CommentsItem()
        newItem.createdBy = userData
        newItem.message = mView?.userMessage
        newItem.createTime = getUtils().dateTimeCurrent
        return newItem
    }

    private fun sendGTMEventView() {
        ContactUsTracking.sendGTMInboxTicket(InboxTicketTracking.Event.EventView,
                InboxTicketTracking.Category.EventHelpMessageInbox,
                InboxTicketTracking.Action.EventImpressionOnCsatRating,
                mView?.ticketID)
    }

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