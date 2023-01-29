package com.tokopedia.sellerpersona.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.sellerpersona.data.remote.usecase.FetchPersonaListUseCase
import com.tokopedia.sellerpersona.view.model.PersonaUiModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import dagger.Lazy
import javax.inject.Inject

/**
 * Created by @ilhamsuaib on 29/01/23.
 */

class SelectPersonaTypeViewModel @Inject constructor(
    private val fetchPersonaListUseCase: Lazy<FetchPersonaListUseCase>,
    dispatchers: CoroutineDispatchers
) : BaseViewModel(dispatchers.main) {

    val personaList: LiveData<Result<List<PersonaUiModel>>>
        get() = _personaList

    private val _personaList: MutableLiveData<Result<List<PersonaUiModel>>> = MutableLiveData()

    fun fetchPersonaList() {
        launchCatchError(block = {
            val data = fetchPersonaListUseCase.get().execute()
            _personaList.value = Success(data)
        }, onError = {
            _personaList.value = Fail(it)
        })
    }
}