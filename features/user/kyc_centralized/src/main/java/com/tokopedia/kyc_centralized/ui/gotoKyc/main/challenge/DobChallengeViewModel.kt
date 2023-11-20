package com.tokopedia.kyc_centralized.ui.gotoKyc.main.challenge

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.kyc_centralized.ui.gotoKyc.domain.GetChallengeResult
import com.tokopedia.kyc_centralized.ui.gotoKyc.domain.GetChallengeUseCase
import com.tokopedia.kyc_centralized.ui.gotoKyc.domain.KycSubmitGoToChallengeAnswer
import com.tokopedia.kyc_centralized.ui.gotoKyc.domain.SubmitChallengeData
import com.tokopedia.kyc_centralized.ui.gotoKyc.domain.SubmitChallengeParam
import com.tokopedia.kyc_centralized.ui.gotoKyc.domain.SubmitChallengeResult
import com.tokopedia.kyc_centralized.ui.gotoKyc.domain.SubmitChallengeUseCase
import com.tokopedia.utils.lifecycle.SingleLiveEvent
import javax.inject.Inject

class DobChallengeViewModel @Inject constructor(
    private val getChallengeUseCase: GetChallengeUseCase,
    private val submitChallengeUseCase: SubmitChallengeUseCase,
    dispatcher: CoroutineDispatchers
) : BaseViewModel(dispatcher.main)  {

    var questionId = ""
        private set

    private val _getChallenge = MutableLiveData<GetChallengeResult>()
    val getChallenge : LiveData<GetChallengeResult> get() = _getChallenge

    private val _submitChallenge = SingleLiveEvent<SubmitChallengeResult>()
    val submitChallenge : SingleLiveEvent<SubmitChallengeResult> get() = _submitChallenge

    fun getChallenge(challengeId: String) {
        _getChallenge.value = GetChallengeResult.Loading
        launchCatchError(block = {
            val response = getChallengeUseCase(challengeId)

            _getChallenge.value = response

            if (response is GetChallengeResult.Success) {
                questionId = response.questionId
            }
        }, onError = {
            _getChallenge.value = GetChallengeResult.Failed(it)
        })
    }

    fun submitChallenge(challengeId: String, questionId: String, selectedDate: String) {
        val parameter = SubmitChallengeParam(
            param = SubmitChallengeData(
                challengeID = challengeId,
                answers = listOf(
                    KycSubmitGoToChallengeAnswer(
                        questionId = questionId,
                        answer = selectedDate
                    )
                )
            )
        )

        _submitChallenge.value = SubmitChallengeResult.Loading
        launchCatchError(block = {
            _submitChallenge.value = submitChallengeUseCase(parameter)
        }, onError = {
            _submitChallenge.value = SubmitChallengeResult.Failed(it)
        })
    }

}
