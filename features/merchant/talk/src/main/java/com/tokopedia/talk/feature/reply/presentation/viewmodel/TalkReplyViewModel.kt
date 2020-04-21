package com.tokopedia.talk.feature.reply.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.talk.common.coroutine.CoroutineDispatchers
import com.tokopedia.talk.feature.reply.data.model.DiscussionDataByQuestionIDResponseWrapper
import com.tokopedia.talk.feature.reply.data.model.TalkFollowUnfollowTalkResponseWrapper
import com.tokopedia.talk.feature.reply.domain.usecase.DiscussionDataByQuestionIDUseCase
import com.tokopedia.talk.feature.reply.domain.usecase.TalkFollowUnfollowTalkUseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.withContext
import com.tokopedia.usecase.coroutines.Result
import javax.inject.Inject

class TalkReplyViewModel @Inject constructor(
        private val discussionDataByQuestionIDUseCase: DiscussionDataByQuestionIDUseCase,
        private val talkFollowUnfollowTalkUseCase: TalkFollowUnfollowTalkUseCase,
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

    fun setIsFollowing(isFollowing: Boolean) {
        this.isFollowing = isFollowing
    }

    fun getIsFollowing(): Boolean {
        return isFollowing
    }
}