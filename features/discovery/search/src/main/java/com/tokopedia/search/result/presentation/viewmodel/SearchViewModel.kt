package com.tokopedia.search.result.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.discovery.common.Event

internal class SearchViewModel(
        coroutineDispatcher: CoroutineDispatchers
): BaseViewModel(coroutineDispatcher.main) {

    private val showAutoCompleteEventLiveData = MutableLiveData<Event<Boolean>>()
    private val hideLoadingEventLiveData = MutableLiveData<Event<Boolean>>()
    private val bottomNavigationVisibilityLiveData = MutableLiveData<Boolean>()

    fun showAutoCompleteView() {
        showAutoCompleteEventLiveData.postValue(Event(true))
    }

    fun hideSearchPageLoading() {
        hideLoadingEventLiveData.postValue(Event(true))
    }

    fun changeBottomNavigationVisibility(isVisible: Boolean) {
        bottomNavigationVisibilityLiveData.postValue(isVisible)
    }

    fun getShowAutoCompleteViewEventLiveData(): LiveData<Event<Boolean>> {
        return showAutoCompleteEventLiveData
    }

    fun getHideLoadingEventLiveData(): LiveData<Event<Boolean>> {
        return hideLoadingEventLiveData
    }

    fun getBottomNavigationVisibilityLiveData(): LiveData<Boolean> {
        return bottomNavigationVisibilityLiveData
    }
}