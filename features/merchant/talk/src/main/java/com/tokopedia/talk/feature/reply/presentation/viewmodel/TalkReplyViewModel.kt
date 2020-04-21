package com.tokopedia.talk.feature.reply.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.talk.common.coroutine.CoroutineDispatchers
import com.tokopedia.talk.feature.reply.data.model.createcomment.TalkCreateNewCommentResponseWrapper
import com.tokopedia.talk.feature.reply.data.model.delete.comment.TalkDeleteCommentResponseWrapper
import com.tokopedia.talk.feature.reply.data.model.delete.talk.TalkDeleteTalkResponseWrapper
import com.tokopedia.talk.feature.reply.data.model.discussion.AttachedProduct
import com.tokopedia.talk.feature.reply.data.model.discussion.DiscussionDataByQuestionIDResponseWrapper
import com.tokopedia.talk.feature.reply.data.model.follow.TalkFollowUnfollowTalkResponseWrapper
import com.tokopedia.talk.feature.reply.domain.usecase.*
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.withContext
import com.tokopedia.usecase.coroutines.Result
import javax.inject.Inject

class TalkReplyViewModel @Inject constructor(
        private val discussionDataByQuestionIDUseCase: DiscussionDataByQuestionIDUseCase,
        private val talkFollowUnfollowTalkUseCase: TalkFollowUnfollowTalkUseCase,
        private val talkDeleteTalkUseCase: TalkDeleteTalkUseCase,
        private val talkDeleteCommentUseCase: TalkDeleteCommentUseCase,
        private val talkCreateNewCommentUseCase: TalkCreateNewCommentUseCase,
        private val dispatchers: CoroutineDispatchers
): BaseViewModel(dispatchers.main) {

    companion object {
        const val MUTATION_SUCCESS = 1
    }

    private val _followUnfollowResult = MutableLiveData<Result<TalkFollowUnfollowTalkResponseWrapper>>()
    val followUnfollowResult: LiveData<Result<TalkFollowUnfollowTalkResponseWrapper>>
    get() = _followUnfollowResult

    private val _discussionData = MutableLiveData<Result<DiscussionDataByQuestionIDResponseWrapper>>()
    val discussionData: LiveData<Result<DiscussionDataByQuestionIDResponseWrapper>>
        get() = _discussionData

    private val _deleteTalkResult = MutableLiveData<Result<TalkDeleteTalkResponseWrapper>>()
    val deleteTalkResult: LiveData<Result<TalkDeleteTalkResponseWrapper>>
        get() = _deleteTalkResult

    private val _deleteCommentResult = MutableLiveData<Result<TalkDeleteCommentResponseWrapper>>()
    val deleteCommentResult: LiveData<Result<TalkDeleteCommentResponseWrapper>>
        get() = _deleteCommentResult

    private val _createNewCommentResult = MutableLiveData<Result<TalkCreateNewCommentResponseWrapper>>()
    val createNewCommentResult: LiveData<Result<TalkCreateNewCommentResponseWrapper>>
        get() = _createNewCommentResult

    private var isFollowing: Boolean = false

    fun followUnfollowTalk(talkId: Int) {
        launchCatchError(block = {
            val response = withContext(dispatchers.io) {
                talkFollowUnfollowTalkUseCase.setParams(talkId)
                talkFollowUnfollowTalkUseCase.executeOnBackground()
            }
            if(response.talkFollowUnfollowTalkResponse.data.isSuccess == MUTATION_SUCCESS) {
                _followUnfollowResult.postValue(Success(response))
            } else {
                _followUnfollowResult.postValue(Fail(Throwable(message = response.talkFollowUnfollowTalkResponse.messageError.first())))
            }
        }) {
            _followUnfollowResult.postValue(Fail(it))
        }
    }

    fun getDiscussionDataByQuestionID(questionId: String, shopId: String){
        launchCatchError(block = {
            val response = withContext(dispatchers.io) {
                discussionDataByQuestionIDUseCase.setParams(questionId, shopId)
                discussionDataByQuestionIDUseCase.executeOnBackground()
            }
            _discussionData.postValue(Success(response))
        }) {
            _discussionData.postValue(Fail(it))
        }
    }

    fun deleteTalk(questionId: String){
        launchCatchError(block = {
            val response = withContext(dispatchers.io) {
                talkDeleteTalkUseCase.setParams(questionId.toIntOrZero())
                talkDeleteTalkUseCase.executeOnBackground()
            }
            if(response.talkDeleteTalk.data.isSuccess == MUTATION_SUCCESS
                    && response.talkDeleteTalk.data.talkId == questionId.toIntOrZero()) {
                _deleteTalkResult.postValue(Success(response))
            } else {
                _deleteTalkResult.postValue(Fail(Throwable(response.talkDeleteTalk.messageError.first())))
            }
        }) {
            _deleteTalkResult.postValue(Fail(it))
        }
    }

    fun deleteComment(questionId: String, commentId: String){
        launchCatchError(block = {
            val response = withContext(dispatchers.io) {
                talkDeleteCommentUseCase.setParams(questionId.toIntOrZero(), commentId.toIntOrZero())
                talkDeleteCommentUseCase.executeOnBackground()
            }
            if(response.talkDeleteComment.data.isSuccess == MUTATION_SUCCESS
                    && commentId.toIntOrZero() == response.talkDeleteComment.data.commentId) {
                _deleteCommentResult.postValue(Success(response))
            } else {
                _deleteCommentResult.postValue(Fail(Throwable(response.talkDeleteComment.messageError.first())))
            }
        }) {
            _deleteCommentResult.postValue(Fail(it))
        }
    }

    fun createNewComment(comment: String, questionId: String, attachedProducts: List<AttachedProduct>) {

    }

    fun setIsFollowing(isFollowing: Boolean) {
        this.isFollowing = isFollowing
    }

    fun getIsFollowing(): Boolean {
        return isFollowing
    }
}