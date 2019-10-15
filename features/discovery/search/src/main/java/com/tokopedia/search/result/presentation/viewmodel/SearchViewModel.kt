package com.tokopedia.search.result.presentation.viewmodel

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.discovery.common.DispatcherProvider
import com.tokopedia.search.result.common.Event
import com.tokopedia.search.result.presentation.model.ChildViewVisibilityModel

internal class SearchViewModel(
        coroutineDispatcher: DispatcherProvider
): BaseViewModel(coroutineDispatcher.ui()) {

    private val showAutoCompleteEventLiveData = MutableLiveData<Event<Boolean>>()
    private val hideLoadingEventLiveData = MutableLiveData<Event<Boolean>>()
    private val childViewVisibleEventLiveData = MutableLiveData<Event<ChildViewVisibilityModel>>()

    fun showAutoCompleteView() {
        showAutoCompleteEventLiveData.postValue(Event(true))
    }

    fun hideSearchPageLoading() {
        hideLoadingEventLiveData.postValue(Event(true))
    }

    fun onChildViewVisibilityChanged(childViewVisibilityModel: ChildViewVisibilityModel) {
        if (childViewVisibilityModel.isChildViewVisibleToUser
                && childViewVisibilityModel.isChildViewReady) {

            childViewVisibleEventLiveData.postValue(Event(childViewVisibilityModel))
        }
    }

    fun getShowAutoCompleteViewEventLiveData(): LiveData<Event<Boolean>> {
        return showAutoCompleteEventLiveData
    }

    fun getHideLoadingEventLiveData(): LiveData<Event<Boolean>> {
        return hideLoadingEventLiveData
    }

    fun getChildViewVisibleEventLiveData(): LiveData<Event<ChildViewVisibilityModel>> {
        return childViewVisibleEventLiveData
    }
}