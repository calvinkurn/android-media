package com.tokopedia.search.result.presentation.viewmodel

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.discovery.common.DispatcherProvider
import com.tokopedia.search.result.common.Event

internal class RedirectionViewModel(
        coroutineDispatcher: DispatcherProvider
): BaseViewModel(coroutineDispatcher.ui()) {

    private val showAutoCompleteEventLiveData = MutableLiveData<Event<Boolean>>()

    fun showAutoCompleteView() {
        showAutoCompleteEventLiveData.postValue(Event(true))
    }

    fun getShowAutoCompleteViewEventLiveData(): LiveData<Event<Boolean>> {
        return showAutoCompleteEventLiveData
    }
}