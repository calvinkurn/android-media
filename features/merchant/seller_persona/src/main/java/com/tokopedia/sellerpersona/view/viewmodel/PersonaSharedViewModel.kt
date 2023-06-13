package com.tokopedia.sellerpersona.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.sellerpersona.common.Constants
import com.tokopedia.sellerpersona.data.remote.model.PersonaStatusModel
import com.tokopedia.sellerpersona.data.remote.usecase.GetPersonaStatusUseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import dagger.Lazy
import javax.inject.Inject

/**
 * Created by @ilhamsuaib on 27/01/23.
 */

class PersonaSharedViewModel @Inject constructor(
    private val getPersonaStatus: Lazy<GetPersonaStatusUseCase>,
    private val userSession: Lazy<UserSessionInterface>,
    dispatchers: CoroutineDispatchers
) : BaseViewModel(dispatchers.io) {

    val personaStatus: LiveData<Result<PersonaStatusModel>>
        get() = _personaStatus
    private val _personaStatus = MutableLiveData<Result<PersonaStatusModel>>()

    fun fetchPersonaStatus() {
        launchCatchError(block = {
            val data = getPersonaStatus.get()
                .execute(userSession.get().shopId, Constants.PERSONA_PAGE_PARAM)
            _personaStatus.postValue(Success(data))
        }, onError = {
            _personaStatus.postValue(Fail(it))
        })
    }
}