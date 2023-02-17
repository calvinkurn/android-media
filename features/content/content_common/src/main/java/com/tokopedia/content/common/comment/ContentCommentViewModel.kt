package com.tokopedia.content.common.comment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.content.common.comment.repository.ContentCommentRepository
import com.tokopedia.content.common.comment.uimodel.*
import com.tokopedia.content.common.types.ResultState
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
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
    private val repo: ContentCommentRepository
) : ViewModel() {

    val comments: Flow<CommentWidgetUiModel>
        get() = _comments

    @AssistedFactory
    interface Factory {
        fun create(@Assisted source: PageSource): ContentCommentViewModel
    }

    private val _query = MutableStateFlow(CommentParam())
    private val _comments = MutableStateFlow(CommentWidgetUiModel.Empty)

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
                    PageSource.Play(source.id),
                    commentType = param.commentType,
                    cursor = param.lastParentCursor,
                )
                _comments.update {
                    it.copy(
                        cursor = result.cursor,
                        state = result.state,
                        list = if (it.list.isEmpty()) result.list else it.list + result.list
                    )
                }
                _query.update {
                    it.copy(
                        lastParentCursor = result.cursor,
                        needToRefresh = false,
                    )
                }
            }) { error ->
                _comments.update {
                    it.copy(state = ResultState.Fail(error))
                }
            }
        }

        fun handleChild() {
            viewModelScope.launchCatchError(block = {
                val result = repo.getComments(
                    PageSource.Play(source.id),
                    commentType = param.commentType,
                    cursor = param.lastChildCursor,
                )
                _comments.getAndUpdate {
                    val selected = it.list
                        .indexOfFirst { item -> item is CommentUiModel.Expandable && item.commentType == param.commentType }
                    val parent =
                        it.list.find { item -> item is CommentUiModel.Item && item.id == param.commentType.parentId } as? CommentUiModel.Item
                    val newChild = mutableListOf<CommentUiModel>().apply {
                        addAll(it.list.mapIndexed { index, model ->
                            if (index == selected && model is CommentUiModel.Expandable) model.copy(
                                isExpanded = if (result.hasNextPage) model.isExpanded else !model.isExpanded,
                                repliesCount = if (result.hasNextPage) model.repliesCount else parent?.childCount.orEmpty()
                            )
                            else model
                        })
                    }
                    newChild.addAll(selected, result.list)
                    it.copy(
                        cursor = result.cursor, list = newChild
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
            }
        }

        if (param.commentType is CommentType.Parent) handleParent()
        else handleChild()
    }

    fun submitAction(action: CommentAction) {
        when (action) {
            is CommentAction.ExpandComment -> handleExpand(action.comment)
            is CommentAction.LoadNextPage -> updateQuery(action.commentType)
            CommentAction.RefreshComment -> resetQuery(needToRefresh = true)
            CommentAction.DismissComment -> resetQuery(needToRefresh = false)
        }
    }

    private fun handleExpand(comment: CommentUiModel.Expandable) {
        fun dropChild() {
            _comments.getAndUpdate {
                val newList = it.list.map { item ->
                    if (item is CommentUiModel.Expandable && item == comment) item
                        .copy(isExpanded = !item.isExpanded) else item
                }
                    .toMutableList().apply {
                        removeIf { item -> item is CommentUiModel.Item && item.commentType == comment.commentType }
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
                commentType = commentType,
            )
        }
    }

    private fun resetQuery(needToRefresh: Boolean) {
        _query.update {
            CommentParam(needToRefresh = needToRefresh)
        }
    }
}
