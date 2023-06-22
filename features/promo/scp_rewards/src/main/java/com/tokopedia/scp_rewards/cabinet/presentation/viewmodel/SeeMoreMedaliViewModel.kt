package com.tokopedia.scp_rewards.cabinet.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.kotlin.extensions.orFalse
import com.tokopedia.scp_rewards.cabinet.domain.GetUserMedaliUseCase
import com.tokopedia.scp_rewards.cabinet.domain.model.ScpRewardsGetUserMedalisResponse
import com.tokopedia.scp_rewards.common.data.InfiniteLoading
import com.tokopedia.scp_rewards.common.data.Loading
import com.tokopedia.scp_rewards.common.data.ScpResult
import com.tokopedia.scp_rewards.common.data.Success
import com.tokopedia.scp_rewards.common.utils.PAGESIZE_PARAM
import com.tokopedia.scp_rewards.common.utils.PAGE_PARAM
import com.tokopedia.scp_rewards.common.utils.TYPE_PARAM
import com.tokopedia.scp_rewards.common.utils.launchCatchError
import com.tokopedia.scp_rewards_common.EARNED_BADGE
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject

class SeeMoreMedaliViewModel @Inject constructor(private val userMedaliUseCase: GetUserMedaliUseCase) :
    ViewModel() {

    companion object {
        private const val PAGE_SIZE = 20
    }

    val visitableList: MutableList<Visitable<*>> = mutableListOf()
    var pageCount = 0

    private val _medalLiveData: MutableLiveData<ScpResult> = MutableLiveData()
    val medalLiveData: LiveData<ScpResult> = _medalLiveData

    private val _hasNextLiveData = MutableLiveData(true)
    val hasNextLiveData: LiveData<Boolean> = _hasNextLiveData

    fun getUserMedalis(page: Int = 1, badgeType: String = EARNED_BADGE) {
        viewModelScope.launchCatchError(block = {
            if (page == 1) {
                _medalLiveData.postValue(Loading)
            } else {
                _medalLiveData.postValue(InfiniteLoading)
            }
            val response = userMedaliUseCase.getUserMedalis(getRequestParams(badgeType))
            pageCount++
            _medalLiveData.postValue(Success(response))
            checkForNextPage(response)
        }, onError = {})
    }

    private fun checkForNextPage(res: ScpRewardsGetUserMedalisResponse?) {
        _hasNextLiveData.postValue(res?.scpRewardsGetUserMedalisByType?.paging?.hasNext.orFalse())
    }

    private fun getRequestParams(badgeType: String): RequestParams {
        return RequestParams().apply {
            putString(TYPE_PARAM, badgeType)
            putInt(PAGE_PARAM, 1)
            putInt(PAGESIZE_PARAM, PAGE_SIZE)
        }
    }
}
