package com.tokopedia.searchbar.navigation_component.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.searchbar.navigation_component.datamodel.TopNavNotificationModel
import com.tokopedia.searchbar.navigation_component.domain.GetNotificationUseCase
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import javax.inject.Inject

class NavigationViewModel @Inject constructor(
        dispatcher: CoroutineDispatcher,
        val userSession: UserSessionInterface,
        val getNotificationUseCase: GetNotificationUseCase
) : BaseViewModel(dispatcher) {
    private var getNotificationJob: Job? = null

    private val _navNotificationModel = MutableLiveData<TopNavNotificationModel>(TopNavNotificationModel())
    val navNotificationLiveData: LiveData<TopNavNotificationModel>
        get() = _navNotificationModel

    private var topNavNotificationModel = TopNavNotificationModel()

    fun getNotification() {
        getAllNotification()
    }

    fun applyNotification() {
        _navNotificationModel.applyLiveDataValue()
    }

    private fun getAllNotification() {
        if (getNotificationJob == null || getNotificationJob?.isActive == false) {
            getNotificationJob = viewModelScope.launchCatchError(Dispatchers.IO, {
                topNavNotificationModel = getNotificationUseCase.executeOnBackground()
                _navNotificationModel.postValue(topNavNotificationModel)
            }) {

            }
        }
    }

    //triggering observer without heavy process
    fun <T> MutableLiveData<T>.applyLiveDataValue() {
        this.value = this.value
    }
}