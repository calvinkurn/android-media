package com.tokopedia.sellerpersona.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.sellerpersona.common.Constants
import com.tokopedia.sellerpersona.data.remote.model.TogglePersonaModel
import com.tokopedia.sellerpersona.data.remote.usecase.GetPersonaDataUseCase
import com.tokopedia.sellerpersona.data.remote.usecase.TogglePersonaUseCase
import com.tokopedia.sellerpersona.view.model.PersonaDataUiModel
import com.tokopedia.sellerpersona.view.model.PersonaStatus
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import dagger.Lazy
import kotlinx.coroutines.delay
import javax.inject.Inject

/**
 * Created by @ilhamsuaib on 31/01/23.
 */

class PersonaResultViewModel @Inject constructor(
    private val getPersonaDataUseCase: Lazy<GetPersonaDataUseCase>,
    private val togglePersonaUseCase: Lazy<TogglePersonaUseCase>,
    private val userSession: Lazy<UserSessionInterface>,
    dispatchers: CoroutineDispatchers
) : BaseViewModel(dispatchers.io) {

    val personaList: LiveData<Result<PersonaDataUiModel>>
        get() = _personaList
    val togglePersonaStatus: LiveData<Result<TogglePersonaModel>>
        get() = _togglePersonaStatus

    private val _personaList = MutableLiveData<Result<PersonaDataUiModel>>()
    private val _togglePersonaStatus = MutableLiveData<Result<TogglePersonaModel>>()

    fun fetchPersonaList() {
        launchCatchError(block = {
            val result = getPersonaDataUseCase.get().execute(
                userSession.get().shopId,
                Constants.PERSONA_PAGE_PARAM
            )
            _personaList.postValue(Success(result))
        }, onError = {
            _personaList.postValue(Fail(it))
        })
    }

    fun toggleUserPersona(status: PersonaStatus) {
        launchCatchError(block = {
            val toggleStatus = togglePersonaUseCase.get().execute(
                userSession.get().shopId, status
            )
            _togglePersonaStatus.postValue(Success(toggleStatus))
        }, onError = {
            _togglePersonaStatus.postValue(Fail(it))
        })
    }
}