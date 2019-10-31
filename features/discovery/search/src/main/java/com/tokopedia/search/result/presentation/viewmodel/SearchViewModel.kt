package com.tokopedia.search.result.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.discovery.common.DispatcherProvider
import com.tokopedia.search.result.common.Event
import com.tokopedia.search.result.presentation.model.ChildViewVisibilityChangedModel

internal class SearchViewModel(
        coroutineDispatcher: DispatcherProvider
): BaseViewModel(coroutineDispatcher.ui()) {

    private val showAutoCompleteEventLiveData = MutableLiveData<Event<Boolean>>()
    private val hideLoadingEventLiveData = MutableLiveData<Event<Boolean>>()
    private val childViewVisibleEventLiveData = MutableLiveData<Event<ChildViewVisibilityChangedModel>>()

    fun showAutoCompleteView() {
        showAutoCompleteEventLiveData.postValue(Event(true))
    }

    fun hideSearchPageLoading() {
        hideLoadingEventLiveData.postValue(Event(true))
    }

    fun onChildViewVisibilityChanged(childViewVisibilityChangedModel: ChildViewVisibilityChangedModel) {
        if (childViewVisibilityChangedModel.isChildViewVisibleToUser
                && childViewVisibilityChangedModel.isChildViewReady) {

            childViewVisibleEventLiveData.postValue(Event(childViewVisibilityChangedModel))
        }
    }

    fun getShowAutoCompleteViewEventLiveData(): LiveData<Event<Boolean>> {
        return showAutoCompleteEventLiveData
    }

    fun getHideLoadingEventLiveData(): LiveData<Event<Boolean>> {
        return hideLoadingEventLiveData
    }

    fun getChildViewVisibleEventLiveData(): LiveData<Event<ChildViewVisibilityChangedModel>> {
        return childViewVisibleEventLiveData
    }
}