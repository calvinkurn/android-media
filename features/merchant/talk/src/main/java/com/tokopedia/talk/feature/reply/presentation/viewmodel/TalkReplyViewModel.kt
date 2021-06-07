package com.tokopedia.talk.feature.reply.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.talk.feature.reply.data.model.createcomment.TalkCreateNewCommentResponseWrapper
import com.tokopedia.talk.feature.reply.data.model.delete.comment.TalkDeleteCommentResponseWrapper
import com.tokopedia.talk.feature.reply.data.model.delete.talk.TalkDeleteTalkResponseWrapper
import com.tokopedia.talk.feature.reply.data.model.discussion.AttachedProduct
import com.tokopedia.talk.feature.reply.data.model.discussion.DiscussionDataByQuestionIDResponseWrapper
import com.tokopedia.talk.feature.reply.data.model.follow.TalkFollowUnfollowTalkResponseWrapper
import com.tokopedia.talk.feature.reply.data.model.report.TalkReportCommentResponseWrapper
import com.tokopedia.talk.feature.reply.data.model.report.TalkReportTalkResponseWrapper
import com.tokopedia.talk.feature.reply.data.model.unmask.TalkMarkCommentNotFraudSuccess
import com.tokopedia.talk.feature.reply.data.model.unmask.TalkMarkNotFraudResponseWrapper
import com.tokopedia.talk.feature.reply.domain.usecase.*
import com.tokopedia.talk.feature.sellersettings.template.data.ChatTemplatesAll
import com.tokopedia.talk.feature.sellersettings.template.domain.usecase.GetAllTemplatesUseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
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
        private val getAllTemplatesUseCase: GetAllTemplatesUseCase,
        private val userSession: UserSessionInterface,
        private val dispatchers: CoroutineDispatchers
) : BaseViewModel(dispatchers.io) {

    companion object {
        const val MUTATION_SUCCESS = 1
    }

    val userId: String
        get() = userSession.userId
    val shopAvatar: String
        get() = userSession.shopAvatar
    val profilePicture: String
        get() = userSession.profilePicture
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

    private val _templateList = MutableLiveData<Result<ChatTemplatesAll>>()
    val templateList: LiveData<Result<ChatTemplatesAll>>
        get() = _templateList

    private var isFollowing: Boolean = false

    fun followUnfollowTalk(talkId: Int) {
        launchCatchError(block = {
            talkFollowUnfollowTalkUseCase.setParams(talkId)
            val response = talkFollowUnfollowTalkUseCase.executeOnBackground()
            if (response.talkFollowUnfollowTalkResponse.data.isSuccess == MUTATION_SUCCESS) {
                _followUnfollowResult.postValue(Success(response))
            } else {
                _followUnfollowResult.postValue(Fail(MessageErrorException(response.talkFollowUnfollowTalkResponse.messageError.firstOrNull())))
            }
        }) {
            _followUnfollowResult.postValue(Fail(it))
        }
    }

    fun getDiscussionDataByQuestionID(questionId: String, shopId: String) {
        launchCatchError(block = {
            discussionDataByQuestionIDUseCase.setParams(questionId, shopId)
            val response = discussionDataByQuestionIDUseCase.executeOnBackground()
            isMyShop = response.discussionDataByQuestionID.shopID == userSession.shopId
            _discussionData.postValue(Success(response))
        }) {
            _discussionData.postValue(Fail(it))
        }
    }

    fun deleteTalk(questionId: String) {
        launchCatchError(block = {
            talkDeleteTalkUseCase.setParams(questionId.toIntOrZero())
            val response = talkDeleteTalkUseCase.executeOnBackground()
            if (response.talkDeleteTalk.data.isSuccess == MUTATION_SUCCESS) {
                _deleteTalkResult.postValue(Success(response))
            } else {
                _deleteTalkResult.postValue(Fail(MessageErrorException(response.talkDeleteTalk.messageError.firstOrNull())))
            }
        }) {
            _deleteTalkResult.postValue(Fail(it))
        }
    }

    fun deleteComment(questionId: String, commentId: String) {
        launchCatchError(block = {
            talkDeleteCommentUseCase.setParams(questionId.toIntOrZero(), commentId.toIntOrZero())
            val response = talkDeleteCommentUseCase.executeOnBackground()
            if (response.talkDeleteComment.data.isSuccess == MUTATION_SUCCESS) {
                _deleteCommentResult.postValue(Success(response))
            } else {
                _deleteCommentResult.postValue(Fail(MessageErrorException(response.talkDeleteComment.messageError.firstOrNull())))
            }
        }) {
            _deleteCommentResult.postValue(Fail(it))
        }
    }

    fun createNewComment(comment: String, questionId: String) {
        launchCatchError(block = {
            val attachedProductIds = mutableListOf<String>()
            attachedProducts.value?.forEach {
                attachedProductIds.add(it.productId)
            }
            talkCreateNewCommentUseCase.setParams(comment, questionId.toIntOrZero(), attachedProductIds.joinToString(prefix = "{", postfix = "}"))
            val response = talkCreateNewCommentUseCase.executeOnBackground()
            if (response.talkCreateNewComment.data.isSuccess == MUTATION_SUCCESS) {
                _createNewCommentResult.postValue(Success(response))
            } else {
                _createNewCommentResult.postValue(Fail(MessageErrorException(response.talkCreateNewComment.messageError.firstOrNull())))
            }
        }) {
            _createNewCommentResult.postValue(Fail(it))
        }
    }

    fun markCommentNotFraud(questionId: String, commentId: String) {
        launchCatchError(block = {
            talkMarkCommentNotFraudUseCase.setParams(questionId.toIntOrZero(), commentId.toIntOrZero())
            val response = talkMarkCommentNotFraudUseCase.executeOnBackground()
            if (response.talkMarkCommentNotFraud.data.isSuccess == MUTATION_SUCCESS) {
                _markCommentNotFraudResult.postValue(Success(TalkMarkCommentNotFraudSuccess(commentId)))
            } else {
                _markCommentNotFraudResult.postValue(Fail(MessageErrorException(response.talkMarkCommentNotFraud.messageError.firstOrNull())))
            }
        }) {
            _markCommentNotFraudResult.postValue(Fail(it))
        }
    }

    fun markQuestionNotFraud(questionId: String) {
        launchCatchError(block = {

            talkMarkNotFraudUseCase.setParams(questionId.toIntOrZero())
            val response = talkMarkNotFraudUseCase.executeOnBackground()
            if (response.talkMarkNotFraud.data.isSuccess == MUTATION_SUCCESS) {
                _markNotFraudResult.postValue(Success(response))
            } else {
                _markNotFraudResult.postValue(Fail(MessageErrorException(response.talkMarkNotFraud.messageError.firstOrNull())))
            }
        }) {
            _markNotFraudResult.postValue(Fail(it))
        }
    }

    fun reportTalk(questionId: String) {
        launchCatchError(block = {
            talkReportTalkUseCase.setParams(questionId.toIntOrZero(), TalkReportTalkUseCase.SELLER_REPORT_REASON)
            val response = talkReportTalkUseCase.executeOnBackground()
            if (response.talkReportTalk.data.isSuccess == MUTATION_SUCCESS) {
                _reportTalkResult.postValue(Success(response))
            } else {
                _reportTalkResult.postValue(Fail(MessageErrorException(response.talkReportTalk.messageError.firstOrNull())))
            }
        }) {
            _reportTalkResult.postValue(Fail(it))
        }
    }

    fun reportComment(commentId: String) {
        launchCatchError(block = {
            talkReportCommentUseCase.setParams(commentId.toIntOrZero(), TalkReportTalkUseCase.SELLER_REPORT_REASON)
            val response = talkReportCommentUseCase.executeOnBackground()
            if (response.talkReportComment.data.isSuccess == MUTATION_SUCCESS) {
                _reportCommentResult.postValue(Success(response))
            } else {
                _reportCommentResult.postValue(Fail(MessageErrorException(response.talkReportComment.messageError.firstOrNull())))
            }
        }) {
            _reportCommentResult.postValue(Fail(it))
        }
    }

    fun getAllTemplates(isSeller: Boolean) {
        launchCatchError(block = {
            getAllTemplatesUseCase.setParams(isSeller)
            val response = getAllTemplatesUseCase.executeOnBackground()
            _templateList.postValue(Success(response.chatTemplatesAll))
        }) {
            _templateList.postValue(Fail(it))
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

    fun getShopName(): String {
        return userSession.shopName
    }
}