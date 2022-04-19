package com.tokopedia.review.feature.credibility.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.review.feature.credibility.data.ReviewerCredibilityStatsWrapper
import com.tokopedia.review.feature.credibility.domain.GetReviewerCredibilityUseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

class ReviewCredibilityViewModel @Inject constructor(
    private val getReviewerCredibilityUseCase: GetReviewerCredibilityUseCase,
    private val userSession: UserSessionInterface,
    coroutineDispatchers: CoroutineDispatchers
) : BaseViewModel(coroutineDispatchers.io) {

    private val _reviewerCredibility = MutableLiveData<Result<ReviewerCredibilityStatsWrapper>>()
    val reviewerCredibility: LiveData<Result<ReviewerCredibilityStatsWrapper>>
        get() = _reviewerCredibility
    
    val userId: String = userSession.userId

    fun getReviewCredibility(source: String, userId: String) {
        launchCatchError(block = {
            getReviewerCredibilityUseCase.setParams(userId, source)
            val response = getReviewerCredibilityUseCase.executeOnBackground()
            _reviewerCredibility.postValue(Success(response.response))
        }) {
            _reviewerCredibility.postValue(Fail(it))
        }
    }

    fun isLoggedIn(): Boolean {
        return userSession.isLoggedIn
    }

    fun isUsersOwnCredibility(userId: String): Boolean {
        return userId == userSession.userId
    }
}