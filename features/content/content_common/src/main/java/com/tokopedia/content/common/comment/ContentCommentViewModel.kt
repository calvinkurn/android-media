package com.tokopedia.content.common.comment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.content.common.comment.repository.ContentCommentRepository
import com.tokopedia.content.common.comment.uimodel.*
import com.tokopedia.content.common.report_content.model.FeedReportRequestParamModel
import com.tokopedia.content.common.types.ResultState
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.user.session.UserSessionInterface
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

/**
 * @author by astidhiyaa on 09/02/23
 */
class ContentCommentViewModel @AssistedInject constructor(
    @Assisted private val source: PageSource,
    private val dispatchers: CoroutineDispatchers,
    private val repo: ContentCommentRepository,
    private val userSession: UserSessionInterface
) : ViewModel() {

    val comments: Flow<CommentWidgetUiModel>
        get() = _comments

    val event: Flow<CommentEvent>
        get() = _event

    val userInfo: UserSessionInterface
        get() = userSession

    val isCreator: Boolean
        get() = _comments.value.commenterType == UserType.Shop

    @AssistedFactory
    interface Factory {
        fun create(@Assisted source: PageSource): ContentCommentViewModel
    }

    private val _query = MutableStateFlow(CommentParam())
    private val _comments = MutableStateFlow(CommentWidgetUiModel.Empty)

    private val _event = MutableSharedFlow<CommentEvent>(extraBufferCapacity = 2)

    private val _selectedComment = MutableStateFlow(Pair(CommentUiModel.Item.Empty, 0))

    init {
        viewModelScope.launch {
            _query.distinctUntilChanged { old, new ->
                old == new
            }.collectLatest {
                if (it.needToRefresh) getComment(it)
            }
        }
    }

    private fun getComment(param: CommentParam) {
        fun handleParent() {
            viewModelScope.launchCatchError(block = {
                val result = repo.getComments(
                    source,
                    commentType = param.commentType,
                    cursor = param.lastParentCursor,
                )
                _comments.update {
                    val contentSame =
                        it.list.zip(result.list).any { item -> item.first == item.second }
                    it.copy(
                        cursor = result.cursor,
                        state = result.state,
                        list = if (contentSame) it.list else it.list + result.list,
                        commenterType = result.commenterType,
                    )
                }
                _query.update {
                    it.copy(
                        lastParentCursor = result.cursor,
                        needToRefresh = false
                    )
                }
            }) { error ->
                _comments.update {
                    it.copy(state = ResultState.Fail(error))
                }
                _query.update {
                    it.copy(
                        needToRefresh = false,
                    )
                }
            }
        }

        fun handleChild() {
            viewModelScope.launchCatchError(block = {
                val result = repo.getComments(
                    source,
                    commentType = param.commentType,
                    cursor = param.lastChildCursor
                )
                _comments.update { curr ->
                    val selected = curr.list
                        .indexOfFirst { item -> item is CommentUiModel.Expandable && item.commentType == param.commentType }
                    val parent =
                        curr.list.find { item -> item is CommentUiModel.Item && item.id == param.commentType.parentId } as? CommentUiModel.Item
                    val newChild = mutableListOf<CommentUiModel>().apply {
                        addAll(
                            curr.list.mapIndexed { index, model ->
                                if (index == selected && model is CommentUiModel.Expandable) {
                                    model.copy(
                                        isExpanded = if (result.hasNextPage) model.isExpanded else !model.isExpanded,
                                        repliesCount = if (result.hasNextPage) result.nextRepliesCount else parent?.childCount.orEmpty()
                                    )
                                } else {
                                    model
                                }
                            }
                        )
                    }
                    newChild.addAll(selected, result.list.filterNot { item -> curr.list.contains(item) })
                    curr.copy(
                        cursor = result.cursor,
                        list = newChild,
                    )
                }
                _query.update {
                    it.copy(
                        lastChildCursor = result.cursor,
                        needToRefresh = false,
                    )
                }
            }) { error ->
                _comments.update {
                    it.copy(state = ResultState.Fail(error))
                }
                _query.update {
                    it.copy(
                        needToRefresh = false,
                    )
                }
            }
        }

        if (param.commentType is CommentType.Parent) {
            handleParent()
        } else {
            handleChild()
        }
    }

    fun submitAction(action: CommentAction) {
        when (action) {
            is CommentAction.ExpandComment -> handleExpand(action.comment)
            is CommentAction.LoadNextPage -> updateQuery(action.commentType)
            CommentAction.RefreshComment -> resetQuery(needToRefresh = true)
            CommentAction.DismissComment -> resetQuery(needToRefresh = false)
            is CommentAction.DeleteComment -> deleteComment(isFromToaster = action.isFromToaster)
            is CommentAction.PermanentRemoveComment -> deleteComment()
            is CommentAction.ReportComment -> reportComment(action.param)
            CommentAction.RequestReportAction -> handleOpenReport()
            is CommentAction.SelectComment -> _selectedComment.update {
                val item = action.comment
                it.copy(first = item, second = _comments.value.list.indexOf(item))
            }
            is CommentAction.EditTextClicked -> handleEditTextClicked(action.item)
            is CommentAction.ReplyComment -> sendReply(action.comment, action.commentType)
            is CommentAction.OpenAppLinkAction -> {
                viewModelScope.launch {
                    _event.emit(CommentEvent.OpenAppLink(action.appLink))
                }
            }
            else -> {}
        }
    }

    private fun handleExpand(comment: CommentUiModel.Expandable) {
        fun dropChild() {
            _comments.getAndUpdate {
                val newList = it.list.map { item ->
                    if (item is CommentUiModel.Expandable && item == comment) {
                        item
                            .copy(isExpanded = !item.isExpanded)
                    } else {
                        item
                    }
                }
                    .toMutableList().apply {
                        removeAll { item -> item is CommentUiModel.Item && item.commentType == comment.commentType }
                    }
                it.copy(list = newList)
            }
        }

        if (!comment.isExpanded) {
            updateQuery(comment.commentType)
        } else {
            dropChild()
        }
    }

    private fun updateQuery(commentType: CommentType) {
        _query.update {
            it.copy(
                needToRefresh = true,
                commentType = commentType
            )
        }
    }

    private fun resetQuery(needToRefresh: Boolean) {
        _query.update {
            CommentParam(needToRefresh = needToRefresh)
        }
    }

    private fun deleteComment(isFromToaster: Boolean) {
        fun removeComment() {
            _comments.update {
                it.copy(list = it.list.filterNot { item -> (item is CommentUiModel.Item && (item.id == _selectedComment.value.first.id || item.commentType.parentId == _selectedComment.value.first.id)) || item is CommentUiModel.Expandable && item.commentType.parentId == _selectedComment.value.first.id })
            }
        }

        if (!isFromToaster) {
            removeComment()
            viewModelScope.launch {
                _event.emit(
                    CommentEvent.ShowSuccessToaster()
                )
            }
        } else {
            undoComment()
        }
    }

    private fun deleteComment() {
        viewModelScope.launchCatchError(block = {
            repo.deleteComment(_selectedComment.value.first.id)
        }) {
            undoComment()
            _event.emit(
                CommentEvent.ShowErrorToaster(message = MessageErrorException(CommentException.FailedDelete.message)) {
                    deleteComment(isFromToaster = false)
                }
            )
        }
    }

    private fun reportComment(
        param: FeedReportRequestParamModel
    ) {
        viewModelScope.launchCatchError(block = {
            val result = repo.reportComment(param)
            if (result) _event.emit(CommentEvent.ReportSuccess)
        }) {
            _event.emit(
                CommentEvent.ShowErrorToaster(
                    message = it,
                    onClick = { reportComment(param) }
                )
            )
        }
    }

    private fun undoComment() {
        _comments.update {
            val newList = it.list.toMutableList()
            newList.add(_selectedComment.value.second, _selectedComment.value.first)
            it.copy(list = newList)
        }
    }

    private fun handleEditTextClicked(item: CommentUiModel.Item) {
        requireLogin {
            viewModelScope.launch {
                _event.emit(CommentEvent.AutoType(item))
            }
        }
    }

    private fun sendReply(comment: String, commentType: CommentType) {
        requireLogin {
            val regex = """((www|http)(\W+\S+[^).,:;?\]\} \r\n${'$'}]+))""".toRegex()
            viewModelScope.launchCatchError(block = {
                _event.emit(CommentEvent.HideKeyboard)
                if (regex.findAll(comment)
                        .count() > 0 && !comment.contains("tokopedia")
                ) throw MessageErrorException(CommentException.LinkNotAllowed.message)
                val result =
                    repo.replyComment(source, commentType, comment, _comments.value.commenterType)
                _comments.getAndUpdate {
                    val index = if (result.commentType.isChild) {
                        val selected = it.list
                            .indexOfFirst { item -> item is CommentUiModel.Item && item.id == result.commentType.parentId } + 1
                        selected
                    } else 0
                    val newList = it.list.toMutableList().apply {
                        add(index, result)
                    }
                    it.copy(list = newList)
                }
                _event.emit(CommentEvent.ReplySuccess)

            }) {
                _event.emit(
                    CommentEvent.ShowErrorToaster(
                        message = MessageErrorException(CommentException.SendCommentFailed.message),
                        onClick = { sendReply(comment, commentType) })
                )
            }
        }
    }

    private fun handleOpenReport() {
        requireLogin {
            viewModelScope.launch {
                _event.emit(CommentEvent.OpenReportEvent)
            }
        }
    }

    private fun requireLogin(action: (isLoggedIn: Boolean) -> Unit) {
        if (!userSession.isLoggedIn) {
            viewModelScope.launch {
                _event.emit(
                    CommentEvent.OpenAppLink(
                        appLink = ApplinkConst.LOGIN
                    )
                )
            }
        } else {
            action(true)
        }
    }
}
