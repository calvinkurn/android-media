package com.tokopedia.scp_rewards.celebration.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tokopedia.scp_rewards.celebration.domain.RewardsGetMedaliCelebrationPageUseCase
import javax.inject.Inject
import androidx.lifecycle.viewModelScope
import com.tokopedia.scp_rewards.celebration.domain.model.ScpRewardsCelebrationModel
import com.tokopedia.scp_rewards.common.constants.ALL_MEDALI_CELEBRATED_ERROR_CODE
import com.tokopedia.scp_rewards.common.constants.SUCCESS_CODE
import com.tokopedia.scp_rewards.common.utils.launchCatchError

class MedalCelebrationViewModel @Inject constructor(
    private val rewardsGetMedaliCelebrationPageUseCase: RewardsGetMedaliCelebrationPageUseCase
) : ViewModel() {

    private val _badgeLiveData: MutableLiveData<ScpResult> = MutableLiveData(ScpResult.Loading)
    val badgeLiveData: LiveData<ScpResult> = _badgeLiveData

    fun getRewards(medaliSlug: String = "", sourceName: String = "") {
        viewModelScope.launchCatchError(
            block = {
                val response = rewardsGetMedaliCelebrationPageUseCase.getRewards(
                    medaliSlug = medaliSlug,
                    sourceName = sourceName
                )

                when (val responseCode = response.scpRewardsCelebrationPage?.resultStatus?.code) {
                    SUCCESS_CODE -> {
                        _badgeLiveData.postValue(ScpResult.Success(response))
                    }
                    ALL_MEDALI_CELEBRATED_ERROR_CODE -> {
                        _badgeLiveData.postValue(ScpResult.AllMedaliCelebratedError(response))
                    }
                    else -> {
                        _badgeLiveData.postValue(ScpResult.Error(Throwable(), responseCode.orEmpty()))
                    }
                }
            },
            onError = {
                _badgeLiveData.postValue(ScpResult.Error(it))
            }
        )
    }

    sealed class ScpResult {
        class Success<T>(val data: T) : ScpResult()
        class AllMedaliCelebratedError(val data: ScpRewardsCelebrationModel) : ScpResult()
        class Error(val error: Throwable, val errorCode: String = "") : ScpResult()
        object Loading : ScpResult()
    }
}
