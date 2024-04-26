package com.tokopedia.navigation.presentation.presenter

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.tokopedia.navigation.domain.GetNewBottomNavNotificationUseCase
import com.tokopedia.navigation.domain.model.Notification
import com.tokopedia.navigation.presentation.type.TabType
import com.tokopedia.navigation_common.ui.BottomNavBarUiModel
import com.tokopedia.navigation_common.usecase.GetHomeBottomNavigationUseCase
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

internal class MainParentViewModel @Inject constructor(
    private val getNotificationUseCase: GetNewBottomNavNotificationUseCase,
    private val getHomeBottomNavigationUseCase: GetHomeBottomNavigationUseCase,
    private val userSession: UserSessionInterface,
): ViewModel() {

    private val _notification = MutableStateFlow<Notification?>(null)
    val notification get() = _notification.asLiveData()

    private val _dynamicBottomNav = MutableLiveData<List<BottomNavBarUiModel>>(emptyList())
    val dynamicBottomNav: LiveData<List<BottomNavBarUiModel>> by this::_dynamicBottomNav

    var isRecurringAppLink = false

    fun hasTabType(type: TabType): Boolean {
//        TODO()
        return false
    }

    fun fetchNotificationData() {
        if (!userSession.isLoggedIn) return
        viewModelScope.launch {
            runCatching {
                getNotificationUseCase(userSession.shopId)
            }.onSuccess {
                _notification.update { it }
            }
        }
    }

    fun fetchDynamicBottomNavBar() {
        viewModelScope.launch {
            runCatching {
                getHomeBottomNavigationUseCase(Unit)
            }.onSuccess {
                _dynamicBottomNav.value = it
            }
        }
    }
}
