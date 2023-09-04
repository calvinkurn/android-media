package com.tokopedia.scp_rewards.cabinet.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.kotlin.extensions.orFalse
import com.tokopedia.scp_rewards.cabinet.domain.GetUserMedaliUseCase
import com.tokopedia.scp_rewards.cabinet.domain.model.ScpRewardsGetUserMedalisResponse
import com.tokopedia.scp_rewards.cabinet.domain.model.getData
import com.tokopedia.scp_rewards.common.data.Error
import com.tokopedia.scp_rewards.common.data.Loading
import com.tokopedia.scp_rewards.common.data.ScpResult
import com.tokopedia.scp_rewards.common.data.Success
import com.tokopedia.scp_rewards.common.utils.PAGESIZE_PARAM
import com.tokopedia.scp_rewards.common.utils.PAGE_NAME_PARAM
import com.tokopedia.scp_rewards.common.utils.PAGE_PARAM
import com.tokopedia.scp_rewards.common.utils.TYPE_PARAM
import com.tokopedia.scp_rewards.common.utils.launchCatchError
import com.tokopedia.scp_rewards_common.EARNED_BADGE
import com.tokopedia.scp_rewards_common.SUCCESS_CODE
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject

class SeeMoreMedaliViewModel @Inject constructor(private val userMedaliUseCase: GetUserMedaliUseCase) :
    ViewModel() {

    companion object {
        private const val PAGE_SIZE = 20
    }

    val visitableList: MutableList<Visitable<*>> = mutableListOf()
    var pageCount = 0
        private set

    private val _medalLiveData: MutableLiveData<ScpResult> = MutableLiveData()
    val medalLiveData: LiveData<ScpResult> = _medalLiveData

    private val _hasNextLiveData = MutableLiveData(true)
    val hasNextLiveData: LiveData<Boolean> = _hasNextLiveData

    fun getUserMedalis(page: Int = 1, badgeType: String = EARNED_BADGE) {
        viewModelScope.launchCatchError(block = {
            _medalLiveData.postValue(Loading)
            val response = userMedaliUseCase.getUserMedalis(getRequestParams(badgeType, page))?.getData(SUCCESS_CODE)
            if (response == null) {
                throw Throwable()
            } else {
                pageCount++
                _medalLiveData.postValue(Success(response))
                checkForNextPage(response)
            }
        }, onError = {
            _medalLiveData.postValue(Error(it))
        })
    }

    private fun checkForNextPage(res: ScpRewardsGetUserMedalisResponse?) {
        _hasNextLiveData.postValue(res?.scpRewardsGetUserMedalisByType?.paging?.hasNext.orFalse())
    }

    private fun getRequestParams(badgeType: String, page: Int): RequestParams {
        return RequestParams().apply {
            putString(TYPE_PARAM, badgeType)
            putString(PAGE_NAME_PARAM, "medali_list_page")
            putInt(PAGE_PARAM, page)
            putInt(PAGESIZE_PARAM, PAGE_SIZE)
        }
    }
}
