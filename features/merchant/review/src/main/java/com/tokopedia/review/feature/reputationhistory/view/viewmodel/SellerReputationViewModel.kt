package com.tokopedia.review.feature.reputationhistory.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.review.feature.reputationhistory.data.model.response.ReputationPenaltyRewardResponse
import com.tokopedia.review.feature.reputationhistory.data.model.response.ReputationPenaltyRewardWrapper
import com.tokopedia.review.feature.reputationhistory.domain.interactor.GetReputationAndPenaltyRewardUseCase
import com.tokopedia.review.feature.reputationhistory.domain.interactor.GetReputationPenaltyRewardUseCase
import com.tokopedia.review.feature.reputationhistory.util.ReputationPenaltyDateUtils
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SellerReputationViewModel @Inject constructor(
    private val dispatchers: CoroutineDispatchers,
    private val userSession: UserSessionInterface,
    private val getReputationAndPenaltyRewardUseCase: GetReputationAndPenaltyRewardUseCase,
    private val getReputationPenaltyRewardUseCase: GetReputationPenaltyRewardUseCase
) : BaseViewModel(dispatchers.main) {

    companion object {
        const val PAST_PENALTY_DAYS = 7
        const val FIRST_PAGE = 1L
        const val PATTERN_PENALTY_DATE_PARAM = "yyyy-MM-dd"
    }

    private val _reputationAndPenaltyMerge =
        MutableLiveData<Result<ReputationPenaltyRewardWrapper>>()
    val reputationAndPenaltyMerge: LiveData<Result<ReputationPenaltyRewardWrapper>>
        get() = _reputationAndPenaltyMerge

    val reputationPenaltyRewardMediator =
        MediatorLiveData<Result<ReputationPenaltyRewardResponse>>()
    val reputationAndPenaltyReward: LiveData<Result<ReputationPenaltyRewardResponse>>
        get() = reputationPenaltyRewardMediator

    private var startDate = ReputationPenaltyDateUtils.format(
        ReputationPenaltyDateUtils.getPastDaysReputationPenaltyDate(PAST_PENALTY_DAYS).time,
        PATTERN_PENALTY_DATE_PARAM
    )
    private var endDate =
        ReputationPenaltyDateUtils.format(System.currentTimeMillis(), PATTERN_PENALTY_DATE_PARAM)

    private val _dateFilterReputationPenalty = MutableLiveData<Pair<String, String>>()

    init {
        initReputationAndPenalty()
    }

    private fun initReputationAndPenalty() {
        reputationPenaltyRewardMediator.addSource(_dateFilterReputationPenalty) {
            startDate = it.first
            endDate = it.second
            getReputationPenaltyList()
        }
    }

    fun setDateFilterReputationPenalty(dateFilter: Pair<String, String>) {
        _dateFilterReputationPenalty.value = Pair(dateFilter.first, dateFilter.second)
    }

    fun getReputationPenaltyRewardMerge() {
        launch(block = {
            val getReputationPenaltyRewardMergeResponse = withContext(dispatchers.io) {
                getReputationAndPenaltyRewardUseCase.execute(
                    userSession.shopId,
                    FIRST_PAGE,
                    startDate,
                    endDate
                )
            }
            _reputationAndPenaltyMerge.postValue(getReputationPenaltyRewardMergeResponse)
        })
    }

    fun getReputationPenaltyList(page: Long = 1) {
        launchCatchError(block = {
            val getReputationPenaltyAndRewardResponse = withContext(dispatchers.io) {
                getReputationPenaltyRewardUseCase.setParams(userSession.shopId, page, startDate, endDate)
                getReputationPenaltyRewardUseCase.executeOnBackground()
            }
            reputationPenaltyRewardMediator.postValue(Success(getReputationPenaltyAndRewardResponse))
        }, onError = {
            reputationPenaltyRewardMediator.postValue(Fail(it))
        })
    }
}