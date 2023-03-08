package com.tokopedia.kyc_centralized.ui.gotoKyc.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.kyc_centralized.ui.gotoKyc.domain.GetChallengeResult
import com.tokopedia.kyc_centralized.ui.gotoKyc.domain.GetChallengeUseCase
import javax.inject.Inject

class DobChallengeViewModel @Inject constructor(
    private val getChallengeUseCase: GetChallengeUseCase,
    dispatcher: CoroutineDispatchers
) : BaseViewModel(dispatcher.main)  {

    private val _getChallenge = MutableLiveData<GetChallengeResult>()
    val getChallenge : LiveData<GetChallengeResult> get() = _getChallenge

    fun getChallenge(challengeId: String) {
        _getChallenge.value = GetChallengeResult.Loading()
        launchCatchError(block = {
            _getChallenge.value = getChallengeUseCase(challengeId)
        }, onError = {
            _getChallenge.value = GetChallengeResult.Failed(it)
        })
    }

    fun submitChallenge(questionId: String) {
        // TODO submit challenge GQL
    }

}
