package com.tokopedia.searchbar.navigation_component.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.searchbar.navigation_component.datamodel.TopNavNotificationModel
import com.tokopedia.searchbar.navigation_component.domain.GetNotificationUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import javax.inject.Inject

class NavigationViewModel @Inject constructor(
        dispatcher: CoroutineDispatcher,
        val getNotificationUseCase: GetNotificationUseCase
) : BaseViewModel(dispatcher) {
    private var getNotificationJob: Job? = null

    private val _navNotificationModel = MutableLiveData<TopNavNotificationModel>()
    val navNotificationLiveData: LiveData<TopNavNotificationModel>
        get() = _navNotificationModel

    fun getNotification() {
        if (getNotificationJob == null || getNotificationJob?.isActive == false) {
            getNotificationJob = viewModelScope.launchCatchError(Dispatchers.IO, {
                val topNavNotificationModel = getNotificationUseCase.executeOnBackground()
                _navNotificationModel.postValue(topNavNotificationModel)
            }) {

            }
        }
    }
}