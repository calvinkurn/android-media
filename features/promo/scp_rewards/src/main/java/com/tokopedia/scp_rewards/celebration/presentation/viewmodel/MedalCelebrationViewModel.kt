package com.tokopedia.scp_rewards.celebration.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tokopedia.scp_rewards.celebration.domain.RewardsGetMedaliCelebrationPageUseCase
import javax.inject.Inject
import androidx.lifecycle.viewModelScope
import com.tokopedia.scp_rewards.common.data.Error
import com.tokopedia.scp_rewards.common.data.Loading
import com.tokopedia.scp_rewards.common.data.ScpResult
import com.tokopedia.scp_rewards.common.data.Success
import kotlinx.coroutines.launch
import com.tokopedia.scp_rewards.common.utils.launchCatchError

class MedalCelebrationViewModel @Inject constructor(
    private val rewardsGetMedaliCelebrationPageUseCase: RewardsGetMedaliCelebrationPageUseCase
) : ViewModel() {

    private val _badgeLiveData:MutableLiveData<ScpResult> = MutableLiveData(Loading)
    val badgeLiveData: LiveData<ScpResult> = _badgeLiveData

    fun getRewards(medaliSlug:String = "",sourceName:String = ""){
        viewModelScope.launchCatchError(
            block = {
                    val response = rewardsGetMedaliCelebrationPageUseCase.getRewards(
                        medaliSlug = "INJECT_BADGE_1",
                        sourceName = "homepage"
                    )
                    if(response.scpRewardsCelebrationPage?.celebrationPage!=null){
                        _badgeLiveData.postValue(Success(response))
                    }
                    else throw Throwable()
            },
            onError = {
                  _badgeLiveData.postValue(Error(it))
            }
        )
    }
}
