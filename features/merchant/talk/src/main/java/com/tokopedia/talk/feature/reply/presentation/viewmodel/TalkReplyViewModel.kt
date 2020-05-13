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
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

class TalkReplyViewModel @Inject constructor(
        private val discussionDataByQuestionIDUseCase: DiscussionDataByQuestionIDUseCase,
        private val talkFollowUnfollowTalkUseCase: TalkFollowUnfollowTalkUseCase,
        private val talkDeleteTalkUseCase: TalkDeleteTalkUseCase,
        private val talkDeleteCommentUseCase: TalkDeleteCommentUseCase,
        private val talkCreateNewCommentUseCase: TalkCreateNewCommentUseCase,
        private val userSession: UserSessionInterface,
        private val dispatchers: CoroutineDispatchers
): BaseViewModel(dispatchers.main) {

    companion object {
        const val MUTATION_SUCCESS = 1
    }

    val userId: String = userSession.userId
    val shopAvatar: String = userSession.shopAvatar
    val profilePicture: String = userSession.profilePicture
    var isMyShop: Boolean = false

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

    private val _attachedProducts = MutableLiveData<MutableList<AttachedProduct>>()
    val attachedProducts: LiveData<MutableList<AttachedProduct>>
        get() = _attachedProducts

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
            if(response.talkDeleteTalk.data.isSuccess == MUTATION_SUCCESS) {
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
            if(response.talkDeleteComment.data.isSuccess == MUTATION_SUCCESS) {
                _deleteCommentResult.postValue(Success(response))
            } else {
                _deleteCommentResult.postValue(Fail(Throwable(response.talkDeleteComment.messageError.first())))
            }
        }) {
            _deleteCommentResult.postValue(Fail(it))
        }
    }

    fun createNewComment(comment: String, questionId: String) {
        launchCatchError(block = {
            val response = withContext(dispatchers.io) {
                val attachedProductIds = mutableListOf<String>()
                attachedProducts.value?.forEach {
                    attachedProductIds.add(it.productId)
                }
                talkCreateNewCommentUseCase.setParams(comment, questionId.toIntOrZero(), attachedProductIds.joinToString(prefix = "{", postfix = "}"))
                talkCreateNewCommentUseCase.executeOnBackground()
            }
            if(response.talkCreateNewComment.data.isSuccess == MUTATION_SUCCESS) {
                _createNewCommentResult.postValue(Success(response))
            } else {
                _createNewCommentResult.postValue(Fail(Throwable(response.talkCreateNewComment.messageError.first())))
            }
        }) {
            _createNewCommentResult.postValue(Fail(it))
        }
    }

    fun setAttachedProducts(attachedProducts: MutableList<AttachedProduct>) {
        _attachedProducts.postValue(attachedProducts)
    }

    fun removeAttachedProduct(productId: String) {
        val attachedProducts = _attachedProducts.value?.toMutableList()
        val updatedAttachedProducts = attachedProducts?.filter { it.productId != productId }?.toMutableList()
        _attachedProducts.postValue(updatedAttachedProducts)
    }

    fun setIsFollowing(isFollowing: Boolean) {
        this.isFollowing = isFollowing
    }

    fun getIsFollowing(): Boolean {
        return isFollowing
    }

    fun setIsMyShop(shopId: String) {
        isMyShop = shopId == userSession.shopId
    }
}