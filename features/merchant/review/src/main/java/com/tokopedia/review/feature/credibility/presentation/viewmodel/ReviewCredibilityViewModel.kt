package com.tokopedia.review.feature.credibility.presentation.viewmodel

import android.os.Bundle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tokopedia.applink.review.ReviewApplinkConst
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.review.common.extension.combine
import com.tokopedia.review.feature.createreputation.domain.RequestState
import com.tokopedia.review.feature.credibility.data.ReviewerCredibilityStatsWrapper
import com.tokopedia.review.feature.credibility.domain.GetReviewerCredibilityUseCase
import com.tokopedia.review.feature.credibility.presentation.mapper.ReviewCredibilityResponseMapper
import com.tokopedia.review.feature.credibility.presentation.uimodel.ReviewCredibilityGlobalErrorUiModel
import com.tokopedia.review.feature.credibility.presentation.uistate.ReviewCredibilityAchievementBoxUiState
import com.tokopedia.review.feature.credibility.presentation.uistate.ReviewCredibilityFooterUiState
import com.tokopedia.review.feature.credibility.presentation.uistate.ReviewCredibilityGlobalErrorUiState
import com.tokopedia.review.feature.credibility.presentation.uistate.ReviewCredibilityHeaderUiState
import com.tokopedia.review.feature.credibility.presentation.uistate.ReviewCredibilityStatisticBoxUiState
import com.tokopedia.reviewcommon.extension.getSavedState
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

typealias GetReviewCredibilityRequestState = RequestState<ReviewerCredibilityStatsWrapper, Nothing>

class ReviewCredibilityViewModel @Inject constructor(
    private val getReviewerCredibilityUseCase: GetReviewerCredibilityUseCase,
    private val userSession: UserSessionInterface
) : ViewModel() {

    companion object {
        private const val STATE_FLOW_STOP_TIMEOUT_MILLIS = 5000L
        private const val SAVED_STATE_KEY_GET_REVIEW_CREDIBILITY_RESULT = "savedStateKeyGetReviewCredibilityResult"
        private const val SAVED_STATE_KEY_PRODUCT_ID = "savedStateKeyProductID"
        private const val SAVED_STATE_KEY_REVIEWER_USER_ID = "savedStateKeyReviewerUserID"
        private const val SAVED_STATE_KEY_SOURCE = "savedStateKeySource"
        private const val SAVED_STATE_KEY_PENDING_APP_LINK = "savedStateKeyPendingAppLink"
        private const val SAVED_STATE_KEY_SHOULD_LOAD_REVIEW_CREDIBILITY_DATA = "savedStateKeyShouldLoadReviewCredibilityData"
    }

    private val getReviewCredibilityResult = MutableStateFlow<GetReviewCredibilityRequestState>(
        RequestState.Idle
    )
    private val shouldLoadReviewCredibilityData = MutableStateFlow(false)
    private val reviewCredibilityHeaderTransitioning = MutableStateFlow(true)
    private val reviewCredibilityAchievementBoxTransitioning = MutableStateFlow(true)
    private val reviewCredibilityStatisticBoxTransitioning = MutableStateFlow(true)
    private val reviewCredibilityFooterTransitioning = MutableStateFlow(true)
    private val reviewCredibilityGlobalErrorTransitioning = MutableStateFlow(true)
    val reviewCredibilityHeaderUiState = combine(
        shouldLoadReviewCredibilityData,
        getReviewCredibilityResult,
        ::mapReviewCredibilityHeaderUiState
    ).stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(STATE_FLOW_STOP_TIMEOUT_MILLIS),
        initialValue = ReviewCredibilityHeaderUiState.Hidden
    )
    val reviewCredibilityAchievementBoxUiState = combine(
        shouldLoadReviewCredibilityData,
        getReviewCredibilityResult,
        ::mapReviewCredibilityAchievementBoxUiState
    ).stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(STATE_FLOW_STOP_TIMEOUT_MILLIS),
        initialValue = ReviewCredibilityAchievementBoxUiState.Hidden
    )
    val reviewCredibilityStatisticBoxUiState = combine(
        shouldLoadReviewCredibilityData,
        getReviewCredibilityResult,
        ::mapReviewCredibilityStatisticBoxUiState
    ).stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(STATE_FLOW_STOP_TIMEOUT_MILLIS),
        initialValue = ReviewCredibilityStatisticBoxUiState.Hidden
    )
    val reviewCredibilityFooterUiState = combine(
        shouldLoadReviewCredibilityData,
        getReviewCredibilityResult,
        ::mapReviewCredibilityFooterUiState
    ).stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(STATE_FLOW_STOP_TIMEOUT_MILLIS),
        initialValue = ReviewCredibilityFooterUiState.Hidden
    )
    val reviewCredibilityGlobalErrorUiState = combine(
        shouldLoadReviewCredibilityData,
        getReviewCredibilityResult,
        ::mapReviewCredibilityGlobalErrorUiState
    ).stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(STATE_FLOW_STOP_TIMEOUT_MILLIS),
        initialValue = ReviewCredibilityGlobalErrorUiState.Hidden
    )

    private var productID = ""
    private var reviewerUserID = ""
    private var source = ""
    private var pendingAppLink = ""

    init {
        handleLoadReviewCredibilityData()
    }

    private fun handleLoadReviewCredibilityData() {
        viewModelScope.launch {
            combine(
                shouldLoadReviewCredibilityData,
                reviewCredibilityHeaderTransitioning,
                reviewCredibilityAchievementBoxTransitioning,
                reviewCredibilityStatisticBoxTransitioning,
                reviewCredibilityFooterTransitioning,
                reviewCredibilityGlobalErrorTransitioning,
                reviewCredibilityHeaderUiState,
                reviewCredibilityAchievementBoxUiState,
                reviewCredibilityStatisticBoxUiState,
                reviewCredibilityFooterUiState,
                reviewCredibilityGlobalErrorUiState
            ) { loadCredibilityData, headerTransitioning, achievementBoxTransitioning,
                statisticBoxTransitioning, footerTransitioning, globalErrorTransitioning,
                headerUiState, achievementBoxUiState, statisticBoxUiState, footerUiState, globalErrorUiState ->
                loadCredibilityData && headerUiState is ReviewCredibilityHeaderUiState.Loading &&
                        achievementBoxUiState is ReviewCredibilityAchievementBoxUiState.Hidden &&
                        statisticBoxUiState is ReviewCredibilityStatisticBoxUiState.Loading &&
                        footerUiState is ReviewCredibilityFooterUiState.Loading &&
                        globalErrorUiState is ReviewCredibilityGlobalErrorUiState.Hidden &&
                        !headerTransitioning && !achievementBoxTransitioning &&
                        !statisticBoxTransitioning && !footerTransitioning && !globalErrorTransitioning
            }.collect { if (it) getReviewCredibility() }
        }
    }

    private fun mapReviewCredibilityHeaderUiState(
        shouldLoadCredibilityData: Boolean, requestState: GetReviewCredibilityRequestState
    ): ReviewCredibilityHeaderUiState {
        val currentUiState = reviewCredibilityHeaderUiState.value
        val newState = if (shouldLoadCredibilityData) {
            ReviewCredibilityHeaderUiState.Loading
        } else {
            when (requestState) {
                is RequestState.Success -> {
                    ReviewCredibilityResponseMapper.toReviewCredibilityHeaderUiModel(
                        requestState.result,
                        reviewerUserID,
                        userSession.userId,
                        productID,
                        source
                    ).let { ReviewCredibilityHeaderUiState.Showed(it) }
                }
                is RequestState.Requesting -> {
                    ReviewCredibilityHeaderUiState.Loading
                }
                else -> {
                    ReviewCredibilityHeaderUiState.Hidden
                }
            }
        }
        if (currentUiState != newState) {
            reviewCredibilityHeaderTransitioning.value = true
        }
        return newState
    }

    private fun mapReviewCredibilityAchievementBoxUiState(
        shouldLoadCredibilityData: Boolean, requestState: GetReviewCredibilityRequestState
    ): ReviewCredibilityAchievementBoxUiState {
        val currentUiState = reviewCredibilityAchievementBoxUiState.value
        val newState = if (shouldLoadCredibilityData) {
            ReviewCredibilityAchievementBoxUiState.Hidden
        } else {
            when (requestState) {
                is RequestState.Success -> {
                    if (requestState.result.label.achievements.isNullOrEmpty()) {
                        ReviewCredibilityAchievementBoxUiState.Hidden
                    } else {
                        ReviewCredibilityResponseMapper.toReviewCredibilityAchievementBoxUiModel(
                            requestState.result
                        ).let { ReviewCredibilityAchievementBoxUiState.Showed(it) }
                    }
                }
                else -> {
                    ReviewCredibilityAchievementBoxUiState.Hidden
                }
            }
        }
        if (currentUiState != newState) {
            reviewCredibilityAchievementBoxTransitioning.value = true
        }
        return newState
    }

    private fun mapReviewCredibilityStatisticBoxUiState(
        shouldLoadCredibilityData: Boolean, requestState: GetReviewCredibilityRequestState
    ): ReviewCredibilityStatisticBoxUiState {
        val currentUiState = reviewCredibilityStatisticBoxUiState.value
        val newState = if (shouldLoadCredibilityData) {
            ReviewCredibilityStatisticBoxUiState.Loading
        } else {
            when (requestState) {
                is RequestState.Success -> {
                    ReviewCredibilityResponseMapper.toReviewCredibilityStatisticBoxUiModel(
                        requestState.result
                    ).let {
                        if (it.statistics.isEmpty()) {
                            ReviewCredibilityStatisticBoxUiState.Hidden
                        } else {
                            ReviewCredibilityStatisticBoxUiState.Showed(it)
                        }
                    }
                }
                is RequestState.Requesting -> {
                    ReviewCredibilityStatisticBoxUiState.Loading
                }
                else -> {
                    ReviewCredibilityStatisticBoxUiState.Hidden
                }
            }
        }
        if (currentUiState != newState) {
            reviewCredibilityStatisticBoxTransitioning.value = true
        }
        return newState
    }

    private fun mapReviewCredibilityFooterUiState(
        shouldLoadCredibilityData: Boolean, requestState: GetReviewCredibilityRequestState
    ): ReviewCredibilityFooterUiState {
        val currentUiState = reviewCredibilityFooterUiState.value
        val newState = if (shouldLoadCredibilityData) {
            ReviewCredibilityFooterUiState.Loading
        } else {
            when (requestState) {
                is RequestState.Success -> {
                    ReviewCredibilityResponseMapper.toReviewCredibilityFooterUiModel(
                        requestState.result
                    ).let { ReviewCredibilityFooterUiState.Showed(it) }
                }
                is RequestState.Requesting -> {
                    ReviewCredibilityFooterUiState.Loading
                }
                else -> {
                    ReviewCredibilityFooterUiState.Hidden
                }
            }
        }
        if (currentUiState != newState) {
            reviewCredibilityFooterTransitioning.value = true
        }
        return newState
    }

    private fun mapReviewCredibilityGlobalErrorUiState(
        shouldLoadCredibilityData: Boolean, requestState: GetReviewCredibilityRequestState
    ): ReviewCredibilityGlobalErrorUiState {
        val currentUiState = reviewCredibilityGlobalErrorUiState.value
        val newUiState = if (shouldLoadCredibilityData) {
            ReviewCredibilityGlobalErrorUiState.Hidden
        } else {
            when (requestState) {
                is RequestState.Error -> {
                    ReviewCredibilityGlobalErrorUiState.Showed(
                        ReviewCredibilityGlobalErrorUiModel(requestState.throwable)
                    )
                }
                else -> {
                    ReviewCredibilityGlobalErrorUiState.Hidden
                }
            }
        }
        if (currentUiState != newUiState) {
            reviewCredibilityGlobalErrorTransitioning.value = true
        }
        return newUiState
    }

    private fun getReviewCredibility() {
        viewModelScope.launchCatchError(block = {
            getReviewCredibilityResult.value = RequestState.Requesting()
            shouldLoadReviewCredibilityData.value = false
            getReviewCredibilityResult.value = RequestState.Success(
                getReviewerCredibilityUseCase.apply {
                    setParams(reviewerUserID, source)
                }.executeOnBackground().response
            )
        }) {
            getReviewCredibilityResult.value = RequestState.Error(it)
        }
    }

    fun loadReviewCredibilityData() {
        shouldLoadReviewCredibilityData.value = true
    }

    fun isLoggedIn() = userSession.isLoggedIn

    fun isUsersOwnCredibility() = reviewerUserID == userSession.userId

    fun getUserID() = userSession.userId.orEmpty()

    fun getProductID() = productID

    fun getReviewerUserID() = reviewerUserID

    fun getSource() = source

    fun setProductID(productID: String) {
        this.productID = productID
    }

    fun setReviewerUserID(reviewerUserID: String) {
        this.reviewerUserID = reviewerUserID
    }

    fun setSource(source: String) {
        this.source = source
    }

    fun setPendingAppLink(appLink: String) {
        pendingAppLink = appLink
    }

    fun getPendingAppLink(): String {
        return pendingAppLink
    }

    fun isFromInbox(): Boolean {
        return source == ReviewApplinkConst.REVIEW_CREDIBILITY_SOURCE_REVIEW_INBOX
    }

    fun onHeaderStopTransitioning() {
        reviewCredibilityHeaderTransitioning.value = false
    }

    fun onAchievementBoxStopTransitioning() {
        reviewCredibilityAchievementBoxTransitioning.value = false
    }

    fun onStatisticBoxStopTransitioning() {
        reviewCredibilityStatisticBoxTransitioning.value = false
    }

    fun onFooterStopTransitioning() {
        reviewCredibilityFooterTransitioning.value = false
    }

    fun onGlobalErrorStopTransitioning() {
        reviewCredibilityGlobalErrorTransitioning.value = false
    }

    fun saveUiState(outState: Bundle) {
        outState.putSerializable(
            SAVED_STATE_KEY_GET_REVIEW_CREDIBILITY_RESULT, getReviewCredibilityResult.value
        )
        outState.putString(SAVED_STATE_KEY_PRODUCT_ID, productID)
        outState.putString(SAVED_STATE_KEY_REVIEWER_USER_ID, reviewerUserID)
        outState.putString(SAVED_STATE_KEY_SOURCE, source)
        outState.putString(SAVED_STATE_KEY_PENDING_APP_LINK, pendingAppLink)
        outState.putBoolean(
            SAVED_STATE_KEY_SHOULD_LOAD_REVIEW_CREDIBILITY_DATA,
            shouldLoadReviewCredibilityData.value
        )
    }

    fun restoreUiState(savedInstanceState: Bundle) {
        getReviewCredibilityResult.value = savedInstanceState.getSavedState(
            SAVED_STATE_KEY_GET_REVIEW_CREDIBILITY_RESULT, getReviewCredibilityResult.value
        )!!
        setProductID(savedInstanceState.getSavedState(SAVED_STATE_KEY_PRODUCT_ID, productID)!!)
        setReviewerUserID(
            savedInstanceState.getSavedState(
                SAVED_STATE_KEY_REVIEWER_USER_ID, reviewerUserID
            )!!
        )
        setSource(savedInstanceState.getSavedState(SAVED_STATE_KEY_SOURCE, source)!!)
        setPendingAppLink(
            savedInstanceState.getSavedState(
                SAVED_STATE_KEY_PENDING_APP_LINK, pendingAppLink
            )!!
        )
        shouldLoadReviewCredibilityData.value = savedInstanceState.getSavedState(
            SAVED_STATE_KEY_SHOULD_LOAD_REVIEW_CREDIBILITY_DATA,
            shouldLoadReviewCredibilityData.value
        )!!
    }
}
