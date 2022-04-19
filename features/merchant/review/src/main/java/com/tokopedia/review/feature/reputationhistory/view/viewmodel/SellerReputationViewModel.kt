package com.tokopedia.review.feature.reputationhistory.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.review.feature.reputationhistory.domain.mapper.SellerReputationPenaltyMapper
import com.tokopedia.review.feature.reputationhistory.domain.usecase.GetReputationShopAndPenaltyRewardUseCase
import com.tokopedia.review.feature.reputationhistory.domain.usecase.GetReputationPenaltyRewardUseCase
import com.tokopedia.review.feature.reputationhistory.util.ReputationPenaltyDateUtils
import com.tokopedia.review.feature.reputationhistory.view.model.SellerReputationPenaltyMergeUiModel
import com.tokopedia.review.feature.reputationhistory.view.model.SellerReputationPenaltyUiModel
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
    private val getReputationShopAndPenaltyRewardUseCase: GetReputationShopAndPenaltyRewardUseCase,
    private val getReputationPenaltyRewardUseCase: GetReputationPenaltyRewardUseCase,
    private val sellerReputationPenaltyMapper: SellerReputationPenaltyMapper
) : BaseViewModel(dispatchers.main) {

    companion object {
        const val PAST_PENALTY_DAYS = 7
        const val FIRST_PAGE = 1
        const val PATTERN_PENALTY_DATE_PARAM = "yyyy-MM-dd"
    }

    private val _reputationAndPenaltyMerge =
        MutableLiveData<Result<SellerReputationPenaltyMergeUiModel>>()
    val reputationAndPenaltyMerge: LiveData<Result<SellerReputationPenaltyMergeUiModel>>
        get() = _reputationAndPenaltyMerge

    val reputationPenaltyRewardMediator =
        MediatorLiveData<Result<SellerReputationPenaltyUiModel>>()
    val reputationAndPenaltyReward: LiveData<Result<SellerReputationPenaltyUiModel>>
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

    fun setDateFilterReputationPenalty(dateFilter: Pair<Long, Long>) {
        val startDate = ReputationPenaltyDateUtils.format(dateFilter.first, PATTERN_PENALTY_DATE_PARAM)
        val endDate = ReputationPenaltyDateUtils.format(dateFilter.second, PATTERN_PENALTY_DATE_PARAM)
        _dateFilterReputationPenalty.value = Pair(startDate, endDate)
    }

    fun getReputationPenaltyRewardMerge() {
        launch(block = {
            val getReputationPenaltyRewardMergeResponse = withContext(dispatchers.io) {
                getReputationShopAndPenaltyRewardUseCase.execute(
                    userSession.shopId.toLongOrZero(),
                    FIRST_PAGE,
                    startDate,
                    endDate
                )
            }
            _reputationAndPenaltyMerge.postValue(getReputationPenaltyRewardMergeResponse)
        })
    }

    fun getReputationPenaltyList(page: Int = FIRST_PAGE) {
        launchCatchError(block = {
            val reputationPenaltyAndRewardResponse = withContext(dispatchers.io) {
                getReputationPenaltyRewardUseCase.setParams(userSession.shopId.toLongOrZero(), page, startDate, endDate)
                sellerReputationPenaltyMapper.mapToPenaltyReputationList(getReputationPenaltyRewardUseCase.executeOnBackground())
            }
            reputationPenaltyRewardMediator.postValue(Success(reputationPenaltyAndRewardResponse))
        }, onError = {
            reputationPenaltyRewardMediator.postValue(Fail(it))
        })
    }
}