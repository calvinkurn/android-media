package com.tokopedia.sellerpersona.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.sellerpersona.data.remote.usecase.GetPersonaListUseCase
import com.tokopedia.sellerpersona.data.remote.usecase.SetPersonaUseCase
import com.tokopedia.sellerpersona.view.model.PersonaUiModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import dagger.Lazy
import javax.inject.Inject

/**
 * Created by @ilhamsuaib on 29/01/23.
 */

class SelectPersonaTypeViewModel @Inject constructor(
    private val getPersonaListUseCase: Lazy<GetPersonaListUseCase>,
    private val setPersonaUseCase: Lazy<SetPersonaUseCase>,
    private val userSession: Lazy<UserSessionInterface>,
    dispatchers: CoroutineDispatchers
) : BaseViewModel(dispatchers.main) {

    val personaList: LiveData<Result<List<PersonaUiModel>>>
        get() = _personaList
    val setPersonaResult: LiveData<Result<String>>
        get() = _setPersonaResult

    private val _personaList = MutableLiveData<Result<List<PersonaUiModel>>>()
    private val _setPersonaResult = MutableLiveData<Result<String>>()

    fun fetchPersonaList() {
        launchCatchError(block = {
            val data = getPersonaListUseCase.get().execute()
            _personaList.value = Success(data)
        }, onError = {
            _personaList.value = Fail(it)
        })
    }

    fun setPersona(persona: String) {
        launchCatchError(block = {
            val shopId = userSession.get().shopId
            val result = setPersonaUseCase.get().execute(
                shopId = shopId,
                persona = persona,
                answers = emptyList()
            )
            _setPersonaResult.postValue(Success(result))
        }, onError = {
            _setPersonaResult.postValue(Fail(it))
        })
    }
}