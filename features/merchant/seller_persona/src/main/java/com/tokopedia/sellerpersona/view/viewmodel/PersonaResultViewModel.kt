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
import com.tokopedia.sellerpersona.view.model.PersonaUiModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import dagger.Lazy
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by @ilhamsuaib on 31/01/23.
 */

class PersonaResultViewModel @Inject constructor(
    private val getPersonaDataUseCase: Lazy<GetPersonaDataUseCase>,
    private val togglePersonaUseCase: Lazy<TogglePersonaUseCase>,
    private val userSession: Lazy<UserSessionInterface>,
    private val dispatchers: CoroutineDispatchers
) : BaseViewModel(dispatchers.io) {

    val personaData: LiveData<Result<PersonaDataUiModel>>
        get() = _personaData
    val togglePersonaStatus: LiveData<Result<TogglePersonaModel>>
        get() = _togglePersonaStatus

    private val _personaData = MutableLiveData<Result<PersonaDataUiModel>>()
    private val _togglePersonaStatus = MutableLiveData<Result<TogglePersonaModel>>()

    fun fetchPersonaData() {
        launchCatchError(block = {
            val result = getPersonaDataUseCase.get().execute(
                userSession.get().shopId,
                Constants.PERSONA_PAGE_PARAM
            )
            _personaData.postValue(Success(result))
        }, onError = {
            _personaData.postValue(Fail(it))
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

    fun setPersona(persona: PersonaUiModel) {
        launch(context = dispatchers.io) {
            val personaData = PersonaDataUiModel(
                persona = persona.name,
                personaStatus = PersonaStatus.ACTIVE,
                personaData = persona
            )
            _personaData.postValue(Success(personaData))
        }
    }
}