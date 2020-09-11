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
import com.tokopedia.talk.feature.reply.data.model.report.TalkReportCommentResponseWrapper
import com.tokopedia.talk.feature.reply.data.model.report.TalkReportTalkResponseWrapper
import com.tokopedia.talk.feature.reply.data.model.unmask.TalkMarkCommentNotFraudResponseWrapper
import com.tokopedia.talk.feature.reply.data.model.unmask.TalkMarkCommentNotFraudSuccess
import com.tokopedia.talk.feature.reply.data.model.unmask.TalkMarkNotFraudResponseWrapper
import com.tokopedia.talk.feature.reply.domain.usecase.*
import com.tokopedia.talk_old.reporttalk.domain.ReportTalkUseCase
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
        private val talkMarkNotFraudUseCase: TalkMarkNotFraudUseCase,
        private val talkMarkCommentNotFraudUseCase: TalkMarkCommentNotFraudUseCase,
        private val talkReportTalkUseCase: TalkReportTalkUseCase,
        private val talkReportCommentUseCase: TalkReportCommentUseCase,
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

    private val _markCommentNotFraudResult = MutableLiveData<Result<TalkMarkCommentNotFraudSuccess>>()
    val markCommentNotFraudResult: LiveData<Result<TalkMarkCommentNotFraudSuccess>>
        get() = _markCommentNotFraudResult

    private val _markNotFraudResult = MutableLiveData<Result<TalkMarkNotFraudResponseWrapper>>()
    val markNotFraudResult: LiveData<Result<TalkMarkNotFraudResponseWrapper>>
        get() = _markNotFraudResult

    private val _reportTalkResult = MutableLiveData<Result<TalkReportTalkResponseWrapper>>()
    val reportTalkResult: LiveData<Result<TalkReportTalkResponseWrapper>>
        get() = _reportTalkResult

    private val _reportCommentResult = MutableLiveData<Result<TalkReportCommentResponseWrapper>>()
    val reportCommentResult: LiveData<Result<TalkReportCommentResponseWrapper>>
        get() = _reportCommentResult

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

    fun markCommentNotFraud(questionId: String, commentId: String){
        launchCatchError(block = {
            val response = withContext(dispatchers.io) {
                talkMarkCommentNotFraudUseCase.setParams(questionId.toIntOrZero(), commentId.toIntOrZero())
                talkMarkCommentNotFraudUseCase.executeOnBackground()
            }
            if(response.talkMarkCommentNotFraud.data.isSuccess == MUTATION_SUCCESS) {
                _markCommentNotFraudResult.postValue(Success(TalkMarkCommentNotFraudSuccess(commentId)))
            } else {
                _markCommentNotFraudResult.postValue(Fail(Throwable(response.talkMarkCommentNotFraud.messageError.first())))
            }
        }) {
            _markCommentNotFraudResult.postValue(Fail(it))
        }
    }

    fun markQuestionNotFraud(questionId: String) {
        launchCatchError(block = {
            val response = withContext(dispatchers.io) {
                talkMarkNotFraudUseCase.setParams(questionId.toIntOrZero())
                talkMarkNotFraudUseCase.executeOnBackground()
            }
            if(response.talkMarkNotFraud.data.isSuccess == MUTATION_SUCCESS) {
                _markNotFraudResult.postValue(Success(response))
            } else {
                _markNotFraudResult.postValue(Fail(Throwable(response.talkMarkNotFraud.messageError.first())))
            }
        }) {
            _markNotFraudResult.postValue(Fail(it))
        }
    }

    fun reportTalk(questionId: String) {
        launchCatchError(block = {
            talkReportTalkUseCase.setParams(questionId.toIntOrZero())
            val response = talkReportTalkUseCase.executeOnBackground()
            if(response.talkReportTalk.data.isSuccess == MUTATION_SUCCESS) {
                _reportTalkResult.postValue(Success(response))
            } else {
                _reportTalkResult.postValue(Fail(Throwable(response.talkReportTalk.messageError.first())))
            }
        }) {
            _reportTalkResult.postValue(Fail(it))
        }
    }

    fun reportComment(commentId: String) {
        launchCatchError(block = {
            talkReportCommentUseCase.setParams(commentId.toIntOrZero())
            val response = talkReportCommentUseCase.executeOnBackground()
            if(response.talkReportComment.data.isSuccess == MUTATION_SUCCESS) {
                _reportCommentResult.postValue(Success(response))
            } else {
                _reportCommentResult.postValue(Fail(Throwable(response.talkReportComment.messageError.first())))
            }
        }) {
            _reportCommentResult.postValue(Fail(it))
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