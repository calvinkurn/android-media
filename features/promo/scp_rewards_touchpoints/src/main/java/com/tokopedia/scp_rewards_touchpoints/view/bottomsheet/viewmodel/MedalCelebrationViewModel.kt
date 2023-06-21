package com.tokopedia.scp_rewards_touchpoints.view.bottomsheet.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import javax.inject.Inject
import androidx.lifecycle.viewModelScope
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.scp_rewards_touchpoints.view.bottomsheet.domain.Error
import com.tokopedia.scp_rewards_touchpoints.view.bottomsheet.domain.Loading
import com.tokopedia.scp_rewards_touchpoints.view.bottomsheet.domain.RewardsGetMedaliCelebrationPageUseCase
import com.tokopedia.scp_rewards_touchpoints.view.bottomsheet.domain.ScpResult
import com.tokopedia.scp_rewards_touchpoints.view.bottomsheet.domain.Success

class MedalCelebrationViewModel @Inject constructor(
    private val rewardsGetMedaliCelebrationPageUseCase: RewardsGetMedaliCelebrationPageUseCase
) : ViewModel() {

    private val _badgeLiveData:MutableLiveData<ScpResult> = MutableLiveData(Loading)
    val badgeLiveData: LiveData<ScpResult> = _badgeLiveData

    fun getRewards(medaliSlug:String = "",pageName:String = ""){
        viewModelScope.launchCatchError(
            block = {
                    val response = rewardsGetMedaliCelebrationPageUseCase.getRewards(
                        medaliSlug = medaliSlug,
                        pageName = pageName
                    )
                    if(response.scpRewardsCelebrationPage?.resultStatus?.code == "200"){
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
