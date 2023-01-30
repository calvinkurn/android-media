package com.tokopedia.sellerpersona.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.sellerpersona.data.remote.usecase.GetPersonaDataUseCase
import com.tokopedia.sellerpersona.view.model.PersonaDataUiModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import dagger.Lazy
import javax.inject.Inject

/**
 * Created by @ilhamsuaib on 27/01/23.
 */

class PersonaSharedViewModel @Inject constructor(
    private val getPersonaData: Lazy<GetPersonaDataUseCase>,
    dispatchers: CoroutineDispatchers
) : BaseViewModel(dispatchers.io) {

    val personaData: LiveData<Result<PersonaDataUiModel>>
        get() = _personaData
    private val _personaData: MutableLiveData<Result<PersonaDataUiModel>> = MutableLiveData()

    fun fetchPersonaData() {
        launchCatchError(block = {
            val data = getPersonaData.get().execute()
            _personaData.postValue(Success(data))
        }, onError = {
            _personaData.postValue(Fail(it))
        })
    }
}