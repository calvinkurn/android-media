package com.tokopedia.content.common.comment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.content.common.comment.repository.ContentCommentRepository
import com.tokopedia.content.common.comment.uimodel.CommentParam
import com.tokopedia.content.common.comment.uimodel.CommentType
import com.tokopedia.content.common.comment.uimodel.CommentUiModel
import com.tokopedia.content.common.comment.uimodel.CommentWidgetUiModel
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
            _query.distinctUntilChanged { old, new -> old != new && new.needToRefresh }
                .collectLatest {
                    getComment(it)
                }
        }
    }

    private fun getComment(param: CommentParam) {
        viewModelScope.launchCatchError(block = {
            val cursor =
                if (param.commentType is CommentType.Parent) param.lastParentCursor else param.lastChildCursor
            val result = repo.getComments(
                PageSource.Play(source.id),
                commentType = param.commentType,
                cursor = cursor
            )
            _comments.update { result }
            _query.update {
                it.copy(
                    lastChildCursor = if (result.commentType is CommentType.Child) result.cursor else it.lastChildCursor,
                    lastParentCursor = if (result.commentType is CommentType.Parent) result.cursor else it.lastParentCursor,
                    needToRefresh = false,
                )
            }
        }) { error ->
            _comments.update {
                it.copy(state = ResultState.Fail(error))
            }
        }
    }

    //from action - LoadNextPage(commenterType)
    private fun updateQuery(commentType: CommentType, cursor: String, needToRefresh: Boolean) {
        _query.update {
            it.copy(
                needToRefresh = needToRefresh,
                commentType = commentType,
            )
        }
    }
}
