package com.tokopedia.profilecompletion.addbod.viewmodel

import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.profilecompletion.addbod.data.AddBodData
import com.tokopedia.profilecompletion.domain.AddBodUseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

/**
 * Created by Ade Fulki on 2019-07-16.
 * ade.hadian@tokopedia.com
 */

class AddBodViewModel @Inject constructor(
    private val addBodUseCase: AddBodUseCase,
    dispatcher: CoroutineDispatchers
) : BaseViewModel(dispatcher.main) {

    val editBodUserProfileResponse = MutableLiveData<Result<AddBodData>>()

    fun editBodUserProfile(selectedDate: String) {
        launchCatchError(block = {
            val response = addBodUseCase(selectedDate).addBodData

            if (response.isSuccess) {
                editBodUserProfileResponse.value = Success(response)
            } else if (response.birthDateMessage.isNotBlank()) {
                editBodUserProfileResponse.value =
                    Fail(MessageErrorException(response.birthDateMessage))
            } else {
                editBodUserProfileResponse.value = Fail(RuntimeException())
            }
        }, onError = {
            editBodUserProfileResponse.value = Fail(it)
        })
    }
}
