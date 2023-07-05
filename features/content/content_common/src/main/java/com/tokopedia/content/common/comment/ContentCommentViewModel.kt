package com.tokopedia.content.common.comment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.content.common.comment.repository.ContentCommentRepository
import com.tokopedia.content.common.comment.uimodel.*
import com.tokopedia.content.common.types.ResultState
import com.tokopedia.content.common.usecase.FeedComplaintSubmitReportUseCase
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.kotlin.extensions.view.isMoreThanZero
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
    private val repo: ContentCommentRepository,
    private val userSession: UserSessionInterface,
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
                    cursor = param.lastParentCursor
                )
                _comments.update {
                    val contentSame = it.list.zip(result.list).any { item -> item.first == item.second }
                    it.copy(
                        cursor = result.cursor,
                        state = result.state,
                        list = if (contentSame) it.list else it.list + result.list,
                        commenterType = result.commenterType
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
                        needToRefresh = false
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
                    // get selected expandable index
                    val selected = curr.list.indexOfFirst { item -> item is CommentUiModel.Expandable && item.commentType == param.commentType }
                    // get parent comment
                    val parent = curr.list.find { item -> item is CommentUiModel.Item && item.id == param.commentType.parentId } as? CommentUiModel.Item
                    // build new list
                    val newList = buildList {
                        val transformExpand = curr.list.mapIndexed { index, model ->
                            if (index == selected && model is CommentUiModel.Expandable) {
                                model.copy(
                                    isExpanded = if (result.hasNextPage) model.isExpanded else !model.isExpanded,
                                    repliesCount = if (result.hasNextPage) result.nextRepliesCount else parent?.childCount.orEmpty()
                                )
                            } else {
                                model
                            }
                        }
                        addAll(transformExpand)
                        addAll(selected, result.list)
                    }
                    curr.copy(
                        cursor = result.cursor,
                        list = newList
                    )
                }
                _query.update {
                    it.copy(
                        lastChildCursor = result.cursor,
                        needToRefresh = false
                    )
                }
            }) { error ->
                _comments.update {
                    it.copy(state = ResultState.Fail(error))
                }
                _query.update {
                    it.copy(
                        needToRefresh = false
                    )
                }
            }
        }

        if (param.commentType.isParent) {
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
            is CommentAction.SelectComment -> {
                val item = _comments.value.list.filterIsInstance<CommentUiModel.Item>().first { comment -> comment.id == action.id }
                _selectedComment.value = Pair(first = item, second = _comments.value.list.indexOf(item))
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
            _comments.update {
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
            updateQuery(comment.commentType) // //if comment type current in query is different with from handle expand reset
        } else {
            dropChild()
        }
    }

    private fun updateQuery(commentType: CommentType) {
        _query.update {
            it.copy(
                needToRefresh = true,
                commentType = commentType,
                lastChildCursor = if (commentType != it.commentType) "" else it.lastChildCursor
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
                CommentEvent.ShowErrorToaster(message = CommentException.createDeleteFailed()) {
                    deleteComment(isFromToaster = false)
                }
            )
        }
    }

    private fun reportComment(
        param: FeedComplaintSubmitReportUseCase.Param
    ) {
        viewModelScope.launchCatchError(block = {
            val result = repo.reportComment(param)
            if (result)
                _event.emit(CommentEvent.ReportSuccess)
            else
                throw MessageErrorException()
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
        _comments.getAndUpdate {
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
            val regex = LINK_REGEX.toRegex()
            viewModelScope.launchCatchError(block = {
                _event.emit(CommentEvent.HideKeyboard)
                if (regex.findAll(comment).count().isMoreThanZero() && !comment.contains(TOKPED_ESCAPE)) {
                    throw CommentException.createLinkNotAllowed()
                }
                val result = repo.replyComment(source, commentType, comment, _comments.value.commenterType)
                var index = 0
                _comments.getAndUpdate {
                    index = if (result.commentType.isChild) {
                        val selected = it.list
                            .indexOfFirst { item -> item is CommentUiModel.Item && item.id == result.commentType.parentId } + 1
                        selected
                    } else {
                        0
                    }
                    val newList = it.list.toMutableList().apply {
                        add(index, result)
                    }
                    it.copy(list = newList)
                }
                _event.emit(CommentEvent.ReplySuccess(index))
            }) {
                _event.emit(
                    CommentEvent.ShowErrorToaster(
                        message = if (it.message?.isBlank() == true) CommentException.createSendCommentFailed() else it,
                        onClick = { sendReply(comment, commentType) }
                    )
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

    companion object {
        private val LINK_REGEX = """((www|http)(\W+\S+[^).,:;?\]\} \r\n${'$'}]+))"""
        private const val TOKPED_ESCAPE = "tokopedia"
    }
}
