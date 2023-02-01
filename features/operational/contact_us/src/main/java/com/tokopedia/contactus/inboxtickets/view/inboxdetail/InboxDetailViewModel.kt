package com.tokopedia.contactus.inboxtickets.view.inboxdetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.contactus.inboxtickets.data.ImageUpload
import com.tokopedia.contactus.inboxtickets.data.model.ChipUploadHostConfig
import com.tokopedia.contactus.inboxtickets.data.model.SecureImageParameter
import com.tokopedia.contactus.inboxtickets.data.model.TicketReplyResponse
import com.tokopedia.contactus.inboxtickets.data.model.Tickets
import com.tokopedia.contactus.inboxtickets.domain.AttachmentItem
import com.tokopedia.contactus.inboxtickets.domain.CommentsItem
import com.tokopedia.contactus.inboxtickets.domain.CreatedBy
import com.tokopedia.contactus.inboxtickets.view.inboxdetail.InboxDetailConstanta.KEY_DISLIKED
import com.tokopedia.contactus.inboxtickets.view.inboxdetail.InboxDetailConstanta.KEY_LIKED
import com.tokopedia.contactus.inboxtickets.view.inboxdetail.InboxDetailConstanta.SUCCESS_HIT_API
import com.tokopedia.contactus.inboxtickets.view.inboxdetail.InboxDetailConstanta.SUCCESS_KEY_SECURE_IMAGE_PARAMETER
import com.tokopedia.contactus.inboxtickets.view.inboxdetail.InboxDetailConstanta.TICKET_STATUS_CLOSED
import com.tokopedia.contactus.inboxtickets.view.inboxdetail.InboxDetailConstanta.TICKET_STATUS_IN_PROCESS
import com.tokopedia.contactus.inboxtickets.view.inboxdetail.InboxDetailConstanta.TICKET_STATUS_NEED_RATING
import com.tokopedia.contactus.inboxtickets.view.inboxdetail.uimodel.OnFindKeywordAtTicket
import com.tokopedia.contactus.inboxtickets.view.inboxdetail.uimodel.InboxDetailUiEffect
import com.tokopedia.contactus.inboxtickets.view.inboxdetail.uimodel.InboxDetailUiState
import com.tokopedia.contactus.inboxtickets.view.utils.*
import com.tokopedia.contactus.inboxtickets.view.utils.CLOSED
import com.tokopedia.contactus.inboxtickets.domain.usecase.ChipUploadHostConfigUseCase
import com.tokopedia.contactus.inboxtickets.domain.usecase.CloseTicketByUserUseCase
import com.tokopedia.contactus.inboxtickets.domain.usecase.ContactUsUploadImageUseCase
import com.tokopedia.contactus.inboxtickets.domain.usecase.InboxOptionUseCase
import com.tokopedia.contactus.inboxtickets.domain.usecase.PostMessageUseCase
import com.tokopedia.contactus.inboxtickets.domain.usecase.PostMessageUseCase2
import com.tokopedia.contactus.inboxtickets.domain.usecase.SecureUploadUseCase
import com.tokopedia.contactus.inboxtickets.domain.usecase.SubmitRatingUseCase
import com.tokopedia.contactus.inboxtickets.view.utils.Utils
import com.tokopedia.csat_rating.data.BadCsatReasonListItem
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.launch_cache_error.launchCatchError
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import javax.inject.Inject

class InboxDetailViewModel @Inject constructor(
    private val postMessageUseCase: PostMessageUseCase,
    private val postMessageUseCase2: PostMessageUseCase2,
    private val inboxOptionUseCase: InboxOptionUseCase,
    private val submitRatingUseCase: SubmitRatingUseCase,
    private val closeTicketByUserUseCase: CloseTicketByUserUseCase,
    private val contactUsUploadImageUseCase: ContactUsUploadImageUseCase,
    private val chipUploadHostConfigUseCase: ChipUploadHostConfigUseCase,
    private val secureUploadUseCase: SecureUploadUseCase,
    private val userSession: UserSessionInterface,
    private val coroutineDispatcherProvider: CoroutineDispatchers,
) : BaseViewModel(coroutineDispatcherProvider.main) {

    companion object {

        //status on find keyword
        const val NOT_FIND_ANY_TEXT = 0
        const val OUT_OF_BOND = 1
        const val FIND_KEYWORD = 2

        const val LAST_COMMENT_POSITION = 0
        private const val FORM_DATA_KEY = "file_upload"
        private const val MEDIA_TYPE = "image/*"
        private const val AGENT = "agent"
        private const val REPLY_TICKET_RESPONSE_STATUS = "OK"
        private const val ROLE_TYPE_CUSTOMER = "customer"
        private const val FAILURE_KEY_UPLOAD_HOST_CONFIG = "0"
    }

    private val _uiState = MutableStateFlow(InboxDetailUiState())
    val uiState = _uiState.asStateFlow()

    private val _uiEffect = MutableSharedFlow<InboxDetailUiEffect>(replay = 1)
    val uiEffect = _uiEffect.asSharedFlow()

    private var _onFindKeywordAtTicket: MutableLiveData<OnFindKeywordAtTicket> = MutableLiveData()
    val onFindKeywordAtTicket: LiveData<OnFindKeywordAtTicket>
        get() = _onFindKeywordAtTicket

    private var _isImageInvalid: MutableLiveData<Boolean> = MutableLiveData()
    val isImageInvalid: LiveData<Boolean>
        get() = _isImageInvalid

    private val currentState: InboxDetailUiState
        get() = _uiState.value

    private val utils by lazy {
        Utils()
    }

    private var ticketId: String = ""
    val searchIndices = arrayListOf<Int>()
    private var next = 0

    fun isShowRating() = currentState.ticketDetail.isShowRating

    fun getTicketStatus() = currentState.ticketDetail.getStatusTicket()

    fun isCommentEmpty() : Boolean {
        return currentState.ticketDetail.getTicketComment().isEmpty()
    }

    fun getTicketNumber() = currentState.ticketDetail.getTicketNumber()

    fun getTicketRating() =
        currentState.ticketDetail.getTicketComment()[currentState.ticketDetail.getTicketComment().size - 1].rating

    fun getCommentOnPosition(position: Int): CommentsItem {
        return currentState.ticketDetail.getTicketComment()[position]
    }

    fun isTicketAllowClose() = currentState.ticketDetail.isAllowClose

    fun setLastCommentAsClosed() {
        getCommentOnPosition(LAST_COMMENT_POSITION).ticketStatus =
            TICKET_STATUS_CLOSED
    }

    fun getFirstCommentId() =
        currentState.ticketDetail.getTicketComment()[currentState.ticketDetail.getTicketComment().size - 1].id

    fun isNeedAttachment() = currentState.ticketDetail.isNeedAttachment

    fun setLastCommentAsOfficialStore(isOfficialStore: Boolean) {
        getCommentOnPosition(LAST_COMMENT_POSITION).priorityLabel = isOfficialStore
    }

    fun getUserId(): String = userSession.userId

    fun ticketId(ticketId: String) {
        this.ticketId = ticketId
        getTicketDetails(ticketId)
    }

    fun submitCsatRating(reason: String, rating: Int) {
        launchCatchError(
            block = {
                val requestParams = submitRatingUseCase.createRequestParams(
                    getFirstCommentId(),
                    rating, reason
                )
                val chipGetInboxDetail = submitRatingUseCase.getChipInboxDetail(requestParams)
                if (chipGetInboxDetail.getErrorListMessage().isNotEmpty()) {
                    _uiEffect.emit(InboxDetailUiEffect.SendCSATRatingFailed(messageError = chipGetInboxDetail.getErrorMessage()))
                } else {
                    _uiEffect.emit(
                        InboxDetailUiEffect.SendCSATRatingSuccess(
                            currentState.ticketDetail.getTicketNumber(),
                            reason,
                            rating
                        )
                    )
                    _uiState.update {
                        it.copy(isIssueClose = true)
                    }
                }
            },
            onError = {
                _uiEffect.emit(InboxDetailUiEffect.SendCSATRatingFailed(messageError = "", it))
            }
        )
    }

    fun getReasonListAsString(reasonListAsInt: List<String>): List<String> {
        val list = arrayListOf<String>()
        reasonListAsInt.forEach {
            for (reason in currentState.ticketDetail.getBadCsatReasons()) {
                if (reason.id.toString() == it) {
                    list.add(reason.getMessageCsatReason())
                }
            }
        }
        return list
    }

    fun getCSATBadReasonList(): List<BadCsatReasonListItem> {
        return currentState.csatReasonListBadReview
    }

    fun refreshLayout() {
        getTicketDetails(ticketId)
    }

    private fun getTicketDetails(ticketId: String) {
        launchCatchError(
            block = {
                val requestParams = inboxOptionUseCase.createRequestParams(ticketId)
                val chipGetInboxDetail = inboxOptionUseCase.getChipInboxDetail(requestParams)
                if (chipGetInboxDetail.isSuccess() == SUCCESS_HIT_API) {
                    val commentsItems = chipGetInboxDetail.getDataTicket().getTicketComment()
                    val isCommentsItemsNull = chipGetInboxDetail.getDataTicket().isCommentsNull()
                    addItemAtTopOfComment(
                        commentsItems,
                        getTopItem(chipGetInboxDetail.getDataTicket()),
                        isCommentsItemsNull
                    )
                    addItemAtTopOfComment(
                        commentsItems,
                        getCommentHeader(chipGetInboxDetail.getDataTicket())
                    )
                    for (item in commentsItems) {
                        val isTimeAreNotEmpty = item.getCreateCommentTime().isNotEmpty()
                        val createTime = if(isTimeAreNotEmpty)utils.getDateTime(item.getCreateCommentTime()) else ""
                        item.createTime = createTime
                        item.shortTime = if(isTimeAreNotEmpty) getShortTime(createTime) else ""
                    }
                    if (currentState.isIssueClose) {
                        currentState.ticketDetail.isShowRating = false
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                csatReasonListBadReview = chipGetInboxDetail.getDataTicket().getBadCsatReasons(),
                                ticketDetail = chipGetInboxDetail.getDataTicket().apply {
                                    isShowRating = false
                                }, isIssueClose = false
                            )
                        }
                    } else {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                csatReasonListBadReview = chipGetInboxDetail.getDataTicket().getBadCsatReasons(),
                                ticketDetail = chipGetInboxDetail.getDataTicket()
                            )
                        }
                    }
                } else {
                    _uiEffect.emit(
                        InboxDetailUiEffect.GetDetailInboxDetailFailed(
                            messageError = chipGetInboxDetail.getErrorMessage()
                        )
                    )
                }
            },
            onError = {
                _uiEffect.emit(InboxDetailUiEffect.GetDetailInboxDetailFailed(messageError = "", it))
            }
        )
    }

    private fun addItemAtTopOfComment(
        commentsItems: MutableList<CommentsItem>,
        topItem: CommentsItem,
        isNullableItems: Boolean = false
    ) {
        var mCommentsItem = commentsItems
        if (isNullableItems) {
            mCommentsItem = ArrayList()
            mCommentsItem.add(topItem)
        } else {
            mCommentsItem.add(0, topItem)
        }
    }

    private fun getTopItem(mTicketDetail: Tickets): CommentsItem {
        val topItem = CommentsItem()
        topItem.attachment = mTicketDetail.attachment
        topItem.message = mTicketDetail.getMessageTicket()
        topItem.messagePlaintext = mTicketDetail.getMessageTicket()
        topItem.createdBy = mTicketDetail.getCreatedTicketBy()
        topItem.createTime = mTicketDetail.getCreateTimeTicket()
        return topItem
    }

    private fun getCommentHeader(mTicketDetail: Tickets): CommentsItem {
        return CommentsItem().apply {
            ticketTitle = mTicketDetail.subject
            ticketId = mTicketDetail.number
            this.ticketStatus = getStatus(mTicketDetail)
        }
    }

    private fun getStatus(mTicketDetail: Tickets): String {
        val isNeedRating = mTicketDetail.isShowRating
        val status = mTicketDetail.status
        return when {
            status.equals(SOLVED, ignoreCase = true) ||
                status.equals(OPEN, ignoreCase = true) ||
                status.equals(NEW, ignoreCase = true) -> {
                TICKET_STATUS_IN_PROCESS
            }
            status.equals(CLOSED, ignoreCase = true) && !isNeedRating -> {
                TICKET_STATUS_CLOSED
            }
            isNeedRating -> {
                TICKET_STATUS_NEED_RATING
            }
            else -> {
                ""
            }
        }
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

    fun onSearchSubmitted(text: String) {
        var mText = text
        mText = mText.trim()
        if (mText.isNotEmpty()) {
            search(mText)
        } else {
            searchIndices.clear()
        }
    }

    private fun search(searchText: String) {
        launch {
            withContext(coroutineDispatcherProvider.default) {
                initializeIndicesList(searchText)
                next = 0
                _uiEffect.emit(
                    InboxDetailUiEffect.OnSearchInboxDetailKeyword(
                        isOnProgress = false,
                        searchText,
                        searchIndices.size
                    )
                )
            }
        }
    }

    private fun initializeIndicesList(searchText: String) {
        searchIndices.clear()
        currentState.ticketDetail.getTicketComment().forEachIndexed { index, commentsItem ->
            if (utils.containsIgnoreCase(
                    commentsItem.messagePlaintext, searchText
                )
            ) {
                searchIndices.add(index)
            }
        }

    }

    fun getNextResult() {
        if (searchIndices.size > 0) {
            if (next >= -1 && next < searchIndices.size.minus(1)) {
                next += 1
                _onFindKeywordAtTicket.value = OnFindKeywordAtTicket(searchIndices[next], next + 1)
            } else {
                _onFindKeywordAtTicket.value = OnFindKeywordAtTicket(status = OUT_OF_BOND)
            }
        } else {
            _onFindKeywordAtTicket.value = OnFindKeywordAtTicket(status = NOT_FIND_ANY_TEXT)
        }
    }

    fun getPreviousResult() {
        if (searchIndices.size > 0) {
            if (next > -1 && next < searchIndices.size) {
                _onFindKeywordAtTicket.value =
                    OnFindKeywordAtTicket(searchIndices[next--], next + 1)
            } else {
                _onFindKeywordAtTicket.value = OnFindKeywordAtTicket(status = OUT_OF_BOND)
            }
        } else {
            _onFindKeywordAtTicket.value = OnFindKeywordAtTicket(status = NOT_FIND_ANY_TEXT)
        }
    }

    fun closeTicket() {
        launchCatchError(
            block = {
                val requestParams = closeTicketByUserUseCase.createRequestParams(ticketId, "mobile")
                val chipGetInboxDetail = closeTicketByUserUseCase.getChipInboxDetail(requestParams)
                if (chipGetInboxDetail.getErrorListMessage().isNotEmpty()) {
                    _uiEffect.emit(
                        InboxDetailUiEffect.OnCloseInboxDetailFailed(
                            chipGetInboxDetail.getErrorMessage()
                        )
                    )
                } else {
                    _uiEffect.emit(InboxDetailUiEffect.OnCloseInboxDetailSuccess(ticketNumber = getTicketNumber()))
                }
            },
            onError = {
                _uiEffect.emit(InboxDetailUiEffect.OnCloseInboxDetailFailed("", it))
            }
        )
    }

    fun sendMessage(isUploadImageValid: Int, imageList: List<ImageUpload>, message: String) {
        when {
            isUploadImageValid >= 1 -> sendMessageWithImages(imageList, message)
            isUploadImageValid == 0 -> sendTextMessage(imageList, message)
            else -> {
                _isImageInvalid.value = true
            }
        }
    }

    private fun sendTextMessage(imageList: List<ImageUpload>, message: String) {
        launchCatchError(
            block = {
                val requestParam = postMessageUseCase.createRequestParams(
                    ticketId, message, 0, "", getLastReplyFromAgent(), userSession.userId
                )

                val replyTicketResponse = postMessageUseCase.getCreateTicketResult(requestParam)

                val successResponse =
                    replyTicketResponse.getData<TicketReplyResponse>(TicketReplyResponse::class.java)
                val errorResponse =
                    replyTicketResponse.getError(TicketReplyResponse::class.java)


                if (successResponse.getTicketReplay().getTicketReplayData().status == REPLY_TICKET_RESPONSE_STATUS
                ) {
                    val newItemMessage = addNewLocalComment(imageList, message)
                    _uiEffect.emit(InboxDetailUiEffect.SendTextMessageSuccess(newItemMessage))

                } else if (!errorResponse.isNullOrEmpty()) {
                    val errorMessage = errorResponse[0].message
                    _uiEffect.emit(InboxDetailUiEffect.SendTextMessageFailed(messageError = errorMessage))
                } else {
                    _uiEffect.emit(InboxDetailUiEffect.SendTextMessageFailed())
                }
            },
            onError = {
                it.printStackTrace()
                _uiEffect.emit(InboxDetailUiEffect.SendTextMessageFailed(throwable = it))
            }
        )
    }

    private fun sendMessageWithImages(imageList: List<ImageUpload>, message: String) {
        launchCatchError(
            block = {
                val files = contactUsUploadImageUseCase.getFile(imageList)
                val list = arrayListOf<ImageUpload>()

                val chipUploadHostConfig = chipUploadHostConfigUseCase.getChipUploadHostConfig()

                if (chipUploadHostConfig.getUploadHostConfig().getUploadHostConfigData().getHost().getServerID() != FAILURE_KEY_UPLOAD_HOST_CONFIG) {

                    val securelyUploadedImages =
                        getSecurelyUploadedImages(imageList, files, chipUploadHostConfig)
                    if (securelyUploadedImages.isNotEmpty()) {
                        list.addAll(securelyUploadedImages)
                    }
                } else {
                    val errorMessage =
                        chipUploadHostConfig.getUploadHostConfig().getErrorMessage()

                    _uiEffect.emit(InboxDetailUiEffect.SendTextMessageFailed(errorMessage))
                    return@launchCatchError
                }

                if (list.isNotEmpty()) {
                    val requestParam = postMessageUseCase.createRequestParams(
                        ticketId,
                        message,
                        1,
                        utils.getAttachmentAsString(imageList),
                        getLastReplyFromAgent(),
                        userSession.userId
                    )

                    val createTicketResponse =
                        postMessageUseCase.getCreateTicketResult(requestParam)

                    val successResponse =
                        createTicketResponse.getData<TicketReplyResponse>(TicketReplyResponse::class.java)

                    if (successResponse.getTicketReplay().getTicketReplayData().status == REPLY_TICKET_RESPONSE_STATUS) {
                        val ticketReplyData = successResponse.getTicketReplay().getTicketReplayData()
                        val das = utils.getFileUploaded(list)
                        if (ticketReplyData.postKey.isNotEmpty()) {
                            val requestParams = postMessageUseCase2.createRequestParams(
                                currentState.ticketDetail.getTicketId(),
                                userSession.userId,
                                das,
                                ticketReplyData.postKey
                            )
                            sendImages(requestParams, imageList, message)
                        } else {
                            _uiEffect.emit(InboxDetailUiEffect.SendTextMessageFailed())
                        }

                    } else {
                        _uiEffect.emit(InboxDetailUiEffect.SendTextMessageFailed())
                    }


                } else {
                    _uiEffect.emit(InboxDetailUiEffect.SendTextMessageFailed())
                }

            },
            onError = {
                _uiEffect.emit(InboxDetailUiEffect.SendTextMessageFailed(throwable = it))
            }
        )
    }

    private suspend fun getSecurelyUploadedImages(
        imageList: List<ImageUpload>,
        files: List<String>,
        chipUploadHostConfig: ChipUploadHostConfig
    ): ArrayList<ImageUpload> {
        val listOfSecureImageParmeter = getListOfSecureImageParameter(files, chipUploadHostConfig)
        if (listOfSecureImageParmeter.isEmpty() || files.size != listOfSecureImageParmeter.size) return arrayListOf()

        val uploadedImageList = getUploadedImageList(imageList, files, listOfSecureImageParmeter)
        return if (uploadedImageList.isEmpty()) {
            arrayListOf()
        } else uploadedImageList
    }

    private suspend fun getListOfSecureImageParameter(
        files: List<String>,
        chipUploadHostConfig: ChipUploadHostConfig
    ): ArrayList<SecureImageParameter> {
        val list = arrayListOf<SecureImageParameter>()
        files.forEachIndexed { _, file ->
            val secureImageParmeter = secureUploadUseCase
                .getSecureImageParameter(
                    getMultiPartObject(file),
                    chipUploadHostConfig
                )
            if (secureImageParmeter.getImage().isSuccess() == SUCCESS_KEY_SECURE_IMAGE_PARAMETER) {
                list.add(secureImageParmeter)
            } else {
                return list
            }
        }
        return list
    }

    private fun getMultiPartObject(pathFile: String): MultipartBody.Part {
        val file = File(pathFile)
        val reqFile = file.asRequestBody(MEDIA_TYPE.toMediaTypeOrNull())
        return MultipartBody.Part.createFormData(FORM_DATA_KEY, file.name, reqFile)
    }

    private suspend fun getUploadedImageList(
        imageList: List<ImageUpload>,
        files: List<String>,
        listOfSecureImageParmeter: ArrayList<SecureImageParameter>
    ): ArrayList<ImageUpload> {
        val list = arrayListOf<ImageUpload>()
        withContext(coroutineDispatcherProvider.io) {
            list.addAll(
                contactUsUploadImageUseCase.uploadFile(
                    userSession.userId,
                    imageList,
                    files,
                    listOfSecureImageParmeter
                )
            )
        }
        return list
    }

    private fun getLastReplyFromAgent(): String {
        var reply = ""
        run loop@{
            currentState.ticketDetail.getTicketComment().reversed().forEach { comment ->
                if (comment.createdBy.role == AGENT) {
                    reply = comment.message
                    return@loop
                }
            }
        }
        return reply
    }

    private fun sendImages(
        requestParams: RequestParams,
        imageList: List<ImageUpload>,
        message: String
    ) {
        launchCatchError(
            block = {
                val stepTwoResponse = postMessageUseCase2.getInboxDataResponse(requestParams)
                if (stepTwoResponse.getTicketAttach().getAttachment().isSuccess > 0) {
                    val newItemMessage = addNewLocalComment(imageList, message)
                    _uiEffect.emit(InboxDetailUiEffect.SendTextMessageSuccess(newItemMessage))
                } else {
                    _uiEffect.emit(InboxDetailUiEffect.SendTextMessageFailed())
                }
            },
            onError = {
                _uiEffect.emit(InboxDetailUiEffect.SendTextMessageFailed(throwable = it))
                it.printStackTrace()
            })
    }

    private fun addNewLocalComment(
        imageList: List<ImageUpload>,
        userMessage: String
    ): CommentsItem {
        val newItem = getNewComment(userMessage)
        val uploadImageList = imageList
        val attachmentItems: MutableList<AttachmentItem> = ArrayList()
        if (uploadImageList.isNotEmpty()) {
            for (upload in uploadImageList) {
                val attachment = AttachmentItem()
                attachment.thumbnail = upload.fileLoc
                attachmentItems.add(attachment)
            }
            newItem.attachment = attachmentItems
        }
        _uiState.update {
            it.copy(
                ticketDetail = it.ticketDetail.apply { isNeedAttachment = false }
            )
        }
        return newItem
    }

    private fun getNewComment(userMessage: String): CommentsItem {
        val newItem = CommentsItem()
        newItem.createdBy = getCommentCreator()
        newItem.message = userMessage
        newItem.createTime = utils.dateTimeCurrent
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

    fun sendRating(agreed: Boolean, commentPosition: Int, commentId: String) {
        val rating =
            if (agreed) KEY_LIKED else KEY_DISLIKED
        launchCatchError(
            block = {
                val requestParams = submitRatingUseCase.createRequestParams(commentId, rating, "-")
                val chipGetInboxDetail = submitRatingUseCase.getChipInboxDetail(requestParams)
                if (chipGetInboxDetail.getErrorListMessage().isNotEmpty()) {
                    val errorMessage = chipGetInboxDetail.getErrorMessage()
                    _uiEffect.emit(InboxDetailUiEffect.OnSendRatingFailed(errorMessage))
                } else {
                    _uiEffect.emit(
                        InboxDetailUiEffect.OnSendRatingSuccess(
                            rating,
                            getCommentOnPosition(commentPosition),
                            commentPosition
                        )
                    )
                }
            },
            onError = {
                _uiEffect.emit(InboxDetailUiEffect.OnSendRatingFailed(throwable = it))
            }
        )
    }


}
