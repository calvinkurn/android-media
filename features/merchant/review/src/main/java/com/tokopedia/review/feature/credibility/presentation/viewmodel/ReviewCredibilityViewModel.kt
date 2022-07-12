package com.tokopedia.review.feature.credibility.presentation.viewmodel

import android.os.Bundle
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.applink.review.ReviewApplinkConst
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.review.common.extension.combine
import com.tokopedia.review.feature.createreputation.domain.RequestState
import com.tokopedia.review.feature.credibility.data.ReviewerCredibilityStatsWrapper
import com.tokopedia.review.feature.credibility.domain.GetReviewerCredibilityUseCase
import com.tokopedia.review.feature.credibility.presentation.mapper.ReviewCredibilityMapper
import com.tokopedia.review.feature.credibility.presentation.uistate.ReviewCredibilityAchievementBoxUiState
import com.tokopedia.review.feature.credibility.presentation.uistate.ReviewCredibilityFooterUiState
import com.tokopedia.review.feature.credibility.presentation.uistate.ReviewCredibilityGlobalErrorUiState
import com.tokopedia.review.feature.credibility.presentation.uistate.ReviewCredibilityHeaderUiState
import com.tokopedia.review.feature.credibility.presentation.uistate.ReviewCredibilityStatisticBoxUiState
import com.tokopedia.reviewcommon.extension.getSavedState
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

typealias GetReviewCredibilityRequestState = RequestState<ReviewerCredibilityStatsWrapper, Nothing>

class ReviewCredibilityViewModel @Inject constructor(
    private val getReviewerCredibilityUseCase: GetReviewerCredibilityUseCase,
    private val userSession: UserSessionInterface,
    coroutineDispatchers: CoroutineDispatchers
) : BaseViewModel(coroutineDispatchers.io) {

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
    val reviewCredibilityHeaderUiState = combine(
        shouldLoadReviewCredibilityData,
        getReviewCredibilityResult,
        ::mapReviewCredibilityHeaderUiState
    ).stateIn(
        scope = this,
        started = SharingStarted.WhileSubscribed(STATE_FLOW_STOP_TIMEOUT_MILLIS),
        initialValue = ReviewCredibilityHeaderUiState.Hidden
    )
    val reviewCredibilityAchievementBoxUiState = combine(
        shouldLoadReviewCredibilityData,
        getReviewCredibilityResult,
        ::mapReviewCredibilityAchievementBoxUiState
    ).stateIn(
        scope = this,
        started = SharingStarted.WhileSubscribed(STATE_FLOW_STOP_TIMEOUT_MILLIS),
        initialValue = ReviewCredibilityAchievementBoxUiState.Hidden
    )
    val reviewCredibilityStatisticBoxUiState = combine(
        shouldLoadReviewCredibilityData,
        getReviewCredibilityResult,
        ::mapReviewCredibilityStatisticBoxUiState
    ).stateIn(
        scope = this,
        started = SharingStarted.WhileSubscribed(STATE_FLOW_STOP_TIMEOUT_MILLIS),
        initialValue = ReviewCredibilityStatisticBoxUiState.Hidden
    )
    val reviewCredibilityFooterUiState = combine(
        shouldLoadReviewCredibilityData,
        getReviewCredibilityResult,
        ::mapReviewCredibilityFooterUiState
    ).stateIn(
        scope = this,
        started = SharingStarted.WhileSubscribed(STATE_FLOW_STOP_TIMEOUT_MILLIS),
        initialValue = ReviewCredibilityFooterUiState.Hidden
    )
    val reviewCredibilityGlobalErrorUiState = combine(
        shouldLoadReviewCredibilityData,
        getReviewCredibilityResult,
        ::mapReviewCredibilityGlobalErrorUiState
    ).stateIn(
        scope = this,
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
        launch {
            combine(
                shouldLoadReviewCredibilityData,
                reviewCredibilityHeaderUiState,
                reviewCredibilityStatisticBoxUiState,
                reviewCredibilityFooterUiState,
                reviewCredibilityGlobalErrorUiState,
                reviewCredibilityHeaderTransitioning,
                reviewCredibilityAchievementBoxTransitioning,
                reviewCredibilityStatisticBoxTransitioning,
                reviewCredibilityFooterTransitioning
            ) { loadCredibilityData, headerUiState, statisticBoxUiState,
                footerUiState, globalErrorUiState, headerTransitioning, achievementBoxTransitioning,
                statisticBoxTransitioning, footerTransitioning ->
                loadCredibilityData && headerUiState is ReviewCredibilityHeaderUiState.Loading &&
                        statisticBoxUiState is ReviewCredibilityStatisticBoxUiState.Loading &&
                        footerUiState is ReviewCredibilityFooterUiState.Loading &&
                        globalErrorUiState is ReviewCredibilityGlobalErrorUiState.Hidden &&
                        !headerTransitioning && !achievementBoxTransitioning && !statisticBoxTransitioning &&
                        !footerTransitioning
            }.collectLatest { if (it) getReviewCredibility() }
        }
    }

    private fun mapReviewCredibilityHeaderUiState(
        shouldLoadCredibilityData: Boolean,
        requestState: GetReviewCredibilityRequestState
    ): ReviewCredibilityHeaderUiState {
        val currentUiState = reviewCredibilityHeaderUiState.value
        return if (shouldLoadCredibilityData) {
            if (currentUiState !is ReviewCredibilityHeaderUiState.Loading) {
                reviewCredibilityHeaderTransitioning.value = true
            }
            ReviewCredibilityHeaderUiState.Loading
        } else {
            when (requestState) {
                is RequestState.Success -> {
                    if (currentUiState !is ReviewCredibilityHeaderUiState.Showed) {
                        reviewCredibilityHeaderTransitioning.value = true
                    }
                    ReviewCredibilityHeaderUiState.Showed(
                        ReviewCredibilityMapper.mapReviewCredibilityResponseToReviewCredibilityHeaderUiModel(requestState.result)
                    )
                }
                else -> {
                    if (currentUiState !is ReviewCredibilityHeaderUiState.Hidden) {
                        reviewCredibilityHeaderTransitioning.value = true
                    }
                    ReviewCredibilityHeaderUiState.Hidden
                }
            }
        }
    }

    private fun mapReviewCredibilityAchievementBoxUiState(
        shouldLoadCredibilityData: Boolean,
        requestState: GetReviewCredibilityRequestState
    ): ReviewCredibilityAchievementBoxUiState {
        val currentUiState = reviewCredibilityAchievementBoxUiState.value
        return if (shouldLoadCredibilityData) {
            if (currentUiState !is ReviewCredibilityAchievementBoxUiState.Hidden) {
                reviewCredibilityAchievementBoxTransitioning.value = true
            }
            ReviewCredibilityAchievementBoxUiState.Hidden
        } else {
            when (requestState) {
                is RequestState.Success -> {
                    if (requestState.result.label.achievements.isNullOrEmpty()) {
                        if (currentUiState !is ReviewCredibilityAchievementBoxUiState.Hidden) {
                            reviewCredibilityAchievementBoxTransitioning.value = true
                        }
                        ReviewCredibilityAchievementBoxUiState.Hidden
                    } else {
                        if (currentUiState !is ReviewCredibilityAchievementBoxUiState.Showed) {
                            reviewCredibilityAchievementBoxTransitioning.value = true
                        }
                        ReviewCredibilityAchievementBoxUiState.Showed(
                            ReviewCredibilityMapper.mapReviewCredibilityResponseToReviewCredibilityAchievementBoxUiModel(requestState.result)
                        )
                    }
                }
                else -> {
                    if (currentUiState !is ReviewCredibilityAchievementBoxUiState.Hidden) {
                        reviewCredibilityAchievementBoxTransitioning.value = true
                    }
                    ReviewCredibilityAchievementBoxUiState.Hidden
                }
            }
        }
    }

    private fun mapReviewCredibilityStatisticBoxUiState(
        shouldLoadCredibilityData: Boolean,
        requestState: GetReviewCredibilityRequestState
    ): ReviewCredibilityStatisticBoxUiState {
        val currentUiState = reviewCredibilityStatisticBoxUiState.value
        return if (shouldLoadCredibilityData) {
            if (currentUiState !is ReviewCredibilityStatisticBoxUiState.Loading) {
                reviewCredibilityStatisticBoxTransitioning.value = true
            }
            ReviewCredibilityStatisticBoxUiState.Loading
        } else {
            when (requestState) {
                is RequestState.Success -> {
                    ReviewCredibilityMapper.mapReviewCredibilityResponseToReviewCredibilityStatisticBoxUiModel(requestState.result).let {
                        if (it.statistics.isEmpty()) {
                            if (currentUiState !is ReviewCredibilityStatisticBoxUiState.Hidden) {
                                reviewCredibilityStatisticBoxTransitioning.value = true
                            }
                            ReviewCredibilityStatisticBoxUiState.Hidden
                        } else {
                            if (currentUiState !is ReviewCredibilityStatisticBoxUiState.Showed) {
                                reviewCredibilityStatisticBoxTransitioning.value = true
                            }
                            ReviewCredibilityStatisticBoxUiState.Showed(it)
                        }
                    }
                }
                else -> {
                    if (currentUiState !is ReviewCredibilityStatisticBoxUiState.Hidden) {
                        reviewCredibilityStatisticBoxTransitioning.value = true
                    }
                    ReviewCredibilityStatisticBoxUiState.Hidden
                }
            }
        }
    }

    private fun mapReviewCredibilityFooterUiState(
        shouldLoadCredibilityData: Boolean,
        requestState: GetReviewCredibilityRequestState
    ): ReviewCredibilityFooterUiState {
        val currentUiState = reviewCredibilityFooterUiState.value
        return if (shouldLoadCredibilityData) {
            if (currentUiState !is ReviewCredibilityFooterUiState.Loading) {
                reviewCredibilityStatisticBoxTransitioning.value = true
            }
            ReviewCredibilityFooterUiState.Loading
        } else {
            when (requestState) {
                is RequestState.Success -> {
                    if (currentUiState !is ReviewCredibilityFooterUiState.Showed) {
                        reviewCredibilityStatisticBoxTransitioning.value = true
                    }
                    ReviewCredibilityFooterUiState.Showed(
                        ReviewCredibilityMapper.mapReviewCredibilityResponseToReviewCredibilityFooterUiModel(requestState.result)
                    )
                }
                else -> {
                    if (currentUiState !is ReviewCredibilityFooterUiState.Hidden) {
                        reviewCredibilityStatisticBoxTransitioning.value = true
                    }
                    ReviewCredibilityFooterUiState.Hidden
                }
            }
        }
    }

    private fun mapReviewCredibilityGlobalErrorUiState(
        shouldLoadCredibilityData: Boolean,
        requestState: GetReviewCredibilityRequestState
    ): ReviewCredibilityGlobalErrorUiState {
        return if (shouldLoadCredibilityData) {
            ReviewCredibilityGlobalErrorUiState.Hidden
        } else {
            when (requestState) {
                is RequestState.Error -> {
                    ReviewCredibilityGlobalErrorUiState.Showed
                }
                else -> {
                    ReviewCredibilityGlobalErrorUiState.Hidden
                }
            }
        }
    }

    private fun getReviewCredibility() {
        launchCatchError(block = {
            getReviewCredibilityResult.value = RequestState.Requesting()
            getReviewerCredibilityUseCase.setParams(reviewerUserID, source)
            getReviewCredibilityResult.value = RequestState.Success(
                getReviewerCredibilityUseCase.executeOnBackground().response
            )
            shouldLoadReviewCredibilityData.value = false
        }) {
            getReviewCredibilityResult.value = RequestState.Error(it)
            shouldLoadReviewCredibilityData.value = false
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

    fun saveUiState(outState: Bundle) {
        outState.putSerializable(SAVED_STATE_KEY_GET_REVIEW_CREDIBILITY_RESULT, getReviewCredibilityResult.value)
        outState.putString(SAVED_STATE_KEY_PRODUCT_ID, productID)
        outState.putString(SAVED_STATE_KEY_REVIEWER_USER_ID, reviewerUserID)
        outState.putString(SAVED_STATE_KEY_SOURCE, source)
        outState.putString(SAVED_STATE_KEY_PENDING_APP_LINK, pendingAppLink)
        outState.putBoolean(SAVED_STATE_KEY_SHOULD_LOAD_REVIEW_CREDIBILITY_DATA, shouldLoadReviewCredibilityData.value)
    }

    fun restoreUiState(savedInstanceState: Bundle) {
        getReviewCredibilityResult.value = savedInstanceState.getSavedState(
            SAVED_STATE_KEY_GET_REVIEW_CREDIBILITY_RESULT,
            getReviewCredibilityResult.value
        )!!
        setProductID(savedInstanceState.getSavedState(SAVED_STATE_KEY_PRODUCT_ID, productID)!!)
        setReviewerUserID(savedInstanceState.getSavedState(SAVED_STATE_KEY_REVIEWER_USER_ID, reviewerUserID)!!)
        setSource(savedInstanceState.getSavedState(SAVED_STATE_KEY_SOURCE, source)!!)
        setPendingAppLink(savedInstanceState.getSavedState(SAVED_STATE_KEY_PENDING_APP_LINK, pendingAppLink)!!)
        shouldLoadReviewCredibilityData.value = savedInstanceState.getSavedState(
            SAVED_STATE_KEY_SHOULD_LOAD_REVIEW_CREDIBILITY_DATA,
            shouldLoadReviewCredibilityData.value
        )!!
    }
}