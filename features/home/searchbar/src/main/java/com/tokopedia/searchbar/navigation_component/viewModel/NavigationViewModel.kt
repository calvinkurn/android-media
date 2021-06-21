package com.tokopedia.searchbar.navigation_component.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.searchbar.navigation_component.datamodel.TopNavNotificationModel
import com.tokopedia.searchbar.navigation_component.domain.GetNotificationUseCase
import com.tokopedia.searchbar.navigation_component.icons.IconConfig
import com.tokopedia.searchbar.navigation_component.icons.IconList
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.Job
import javax.inject.Inject

class NavigationViewModel @Inject constructor(
        dispatcher: CoroutineDispatchers,
        val userSession: UserSessionInterface,
        val getNotificationUseCase: GetNotificationUseCase
) : BaseViewModel(dispatcher.io) {
    private var getNotificationJob: Job? = null

    private val _navNotificationModel = MutableLiveData<TopNavNotificationModel>(TopNavNotificationModel())
    val navNotificationLiveData: LiveData<TopNavNotificationModel>
        get() = _navNotificationModel

    private var topNavNotificationModel = TopNavNotificationModel()
    private var registeredIconList = mutableSetOf<Int>()

    private val supportedNotificationIcon = listOf(
            IconList.ID_MESSAGE,
            IconList.ID_INBOX,
            IconList.ID_NOTIFICATION,
            IconList.ID_CART,
            IconList.ID_NAV_GLOBAL
    )

    fun getNotification() {
        getAllNotification()
    }

    fun applyNotification() {
        _navNotificationModel.applyLiveDataValue()
    }

    internal fun setRegisteredIconList(iconConfig: IconConfig) {
        registeredIconList.clear()
        iconConfig.iconList.forEach {
            registeredIconList.add(it.id)
        }
    }

    private fun getAllNotification() {
        if (jobIsValid() && iconNeedNotificationCounter()) {
            getNotificationJob = viewModelScope.launchCatchError(coroutineContext, {
                topNavNotificationModel = getNotificationUseCase.executeOnBackground()
                _navNotificationModel.postValue(topNavNotificationModel)
            }) {

            }
        }
    }

    private fun jobIsValid() = getNotificationJob == null || getNotificationJob?.isActive == false

    private fun iconNeedNotificationCounter(): Boolean = registeredIconList.any { it in supportedNotificationIcon }

    //triggering observer without heavy process
    fun <T> MutableLiveData<T>.applyLiveDataValue() {
        this.value = this.value
    }
}