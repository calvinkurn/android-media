package com.tokopedia.scp_rewards.detail.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tokopedia.scp_rewards.common.data.Error
import com.tokopedia.scp_rewards.common.data.Loading
import com.tokopedia.scp_rewards.common.data.ScpResult
import com.tokopedia.scp_rewards.common.data.Success
import com.tokopedia.scp_rewards.common.utils.launchCatchError
import com.tokopedia.scp_rewards.detail.domain.MedalDetailUseCase
import javax.inject.Inject

class MedalDetailViewModel @Inject constructor(
    private val medalDetailUseCase: MedalDetailUseCase
) : ViewModel() {

    private val _badgeLiveData: MutableLiveData<ScpResult> = MutableLiveData(Loading)
    val badgeLiveData: LiveData<ScpResult> = _badgeLiveData

    fun getMedalDetail(medaliSlug: String = "", sourceName: String = "", pageName: String = "") {
        viewModelScope.launchCatchError(
            block = {
                val response = medalDetailUseCase.getMedalDetail(
                    medaliSlug = "INJECT_BADGE_1",
                    sourceName = "homepage",
                    pageName = "medali_detail_page"
                )
                if (response.detail != null) {
                    _badgeLiveData.postValue(Success(response))
                } else {
                    throw Throwable()
                }
            },
            onError = {
                _badgeLiveData.postValue(Error(it))
            }
        )
    }
}
