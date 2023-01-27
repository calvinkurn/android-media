package com.tokopedia.sellerpersona.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import javax.inject.Inject

/**
 * Created by @ilhamsuaib on 27/01/23.
 */

class PersonaSharedViewModel @Inject constructor(
    dispatchers: CoroutineDispatchers
) : BaseViewModel(dispatchers.main) {

    val personaData: LiveData<Boolean>
        get() = _personaData
    private val _personaData: MutableLiveData<Boolean> = MutableLiveData()

    fun fetchPersonaData() {
        _personaData.value = true
    }
}