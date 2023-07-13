package com.tokopedia.sellerpersona.view.compose.viewmodel

import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.sellerpersona.common.Constants
import com.tokopedia.sellerpersona.data.remote.usecase.GetPersonaDataUseCase
import com.tokopedia.sellerpersona.data.remote.usecase.TogglePersonaUseCase
import com.tokopedia.sellerpersona.view.compose.model.UiState
import com.tokopedia.sellerpersona.view.model.PersonaDataUiModel
import com.tokopedia.sellerpersona.view.model.PersonaStatus
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import dagger.Lazy
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

/**
 * Created by @ilhamsuaib on 31/01/23.
 */

class ComposePersonaResultViewModel @Inject constructor(
    private val getPersonaDataUseCase: Lazy<GetPersonaDataUseCase>,
    private val togglePersonaUseCase: Lazy<TogglePersonaUseCase>,
    private val userSession: Lazy<UserSessionInterface>,
    private val dispatchers: CoroutineDispatchers
) : BaseViewModel(dispatchers.io) {

    private val _personaData = MutableStateFlow<UiState<PersonaDataUiModel>>(UiState.Default)
    val personaData: StateFlow<UiState<PersonaDataUiModel>>
        get() = _personaData.asStateFlow()

    private val _togglePersonaStatus = MutableStateFlow<UiState<PersonaStatus>>(UiState.Default)
    val togglePersonaStatus: StateFlow<UiState<PersonaStatus>>
        get() = _togglePersonaStatus.asStateFlow()

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
}