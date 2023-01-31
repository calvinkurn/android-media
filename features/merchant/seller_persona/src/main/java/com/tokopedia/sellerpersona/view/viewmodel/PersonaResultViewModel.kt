package com.tokopedia.sellerpersona.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.sellerpersona.common.Constants
import com.tokopedia.sellerpersona.data.remote.usecase.GetPersonaDataUseCase
import com.tokopedia.sellerpersona.view.model.PersonaDataUiModel
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
    private val userSession: Lazy<UserSessionInterface>,
    dispatchers: CoroutineDispatchers
) : BaseViewModel(dispatchers.io) {

    val personaList: LiveData<Result<PersonaDataUiModel>>
        get() = _personaList

    private val _personaList = MutableLiveData<Result<PersonaDataUiModel>>()

    fun fetchPersonaList() {
        launchCatchError(block = {
            val result = getPersonaDataUseCase.get().execute(
                userSession.get().shopId,
                Constants.PERSONA_PAGE_PARAM
            )
            delay(1000)
            _personaList.postValue(Success(result))
        }, onError = {
            _personaList.postValue(Fail(it))
        })
    }
}