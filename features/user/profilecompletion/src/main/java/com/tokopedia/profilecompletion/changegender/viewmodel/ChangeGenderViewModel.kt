package com.tokopedia.profilecompletion.changegender.viewmodel

import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.profilecompletion.changegender.data.ChangeGenderResult
import com.tokopedia.profilecompletion.data.ProfileCompletionQueryConstant.PARAM_GENDER
import com.tokopedia.profilecompletion.domain.ChangeGenderUseCase
import com.tokopedia.sessioncommon.ErrorHandlerSession
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

class ChangeGenderViewModel @Inject constructor(
    private val changeGenderUseCase: ChangeGenderUseCase,
    val dispatcher: CoroutineDispatchers
) : BaseViewModel(dispatcher.main) {

    val mutateChangeGenderResponse = MutableLiveData<Result<ChangeGenderResult>>()

    fun mutateChangeGender(gender: Int) {
        val params = mapOf(PARAM_GENDER to gender)

        launchCatchError(block = {
            val response = changeGenderUseCase(params)

            val errorMessage = response.data.errorMessage
            val isSuccess = response.data.isSuccess

            if (errorMessage.isBlank() && isSuccess) {
                mutateChangeGenderResponse.value = Success(ChangeGenderResult(response.data, gender))
            } else if (errorMessage.isNotBlank()) {
                mutateChangeGenderResponse.value = Fail(
                    MessageErrorException(
                        errorMessage,
                        ErrorHandlerSession.ErrorCode.WS_ERROR.toString()
                    )
                )
            } else {
                mutateChangeGenderResponse.value = Fail(RuntimeException())
            }
        }, onError = {
            mutateChangeGenderResponse.value = Fail(it)
        })
    }
}
