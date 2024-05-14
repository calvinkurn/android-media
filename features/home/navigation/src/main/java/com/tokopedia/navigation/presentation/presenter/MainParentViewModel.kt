package com.tokopedia.navigation.presentation.presenter

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tokopedia.navigation.domain.GetHomeBottomNavigationUseCase
import com.tokopedia.navigation.domain.GetNewBottomNavNotificationUseCase
import com.tokopedia.navigation.domain.model.Notification
import com.tokopedia.navigation.util.CompletableTask
import com.tokopedia.navigation_common.ui.BottomNavBarItemType
import com.tokopedia.navigation_common.ui.BottomNavBarUiModel
import com.tokopedia.navigation_common.ui.BottomNavItemId
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.launch
import javax.inject.Inject

internal class MainParentViewModel @Inject constructor(
    private val getNotificationUseCase: GetNewBottomNavNotificationUseCase,
    private val getHomeBottomNavigationUseCase: GetHomeBottomNavigationUseCase,
    private val userSession: UserSessionInterface
) : ViewModel() {

    private val _notification = MutableLiveData<Notification?>(null)
    val notification: LiveData<Notification?> by this::_notification

    private val _dynamicBottomNav = MutableLiveData<List<BottomNavBarUiModel>?>(null)
    val dynamicBottomNav: LiveData<List<BottomNavBarUiModel>?> by this::_dynamicBottomNav

    private val _nextDynamicBottomNav = MutableLiveData<CompletableTask<List<BottomNavBarUiModel>>?>(null)
    val nextDynamicBottomNav: LiveData<CompletableTask<List<BottomNavBarUiModel>>?> by this::_nextDynamicBottomNav

    var isRecurringAppLink = false

    fun hasTabType(type: BottomNavBarItemType): Boolean {
        val bottomNav = dynamicBottomNav.value ?: return false
        return bottomNav.any { it.type == type }
    }

    fun getModelById(id: BottomNavItemId): BottomNavBarUiModel? {
        val bottomNav = dynamicBottomNav.value ?: return null
        return bottomNav.firstOrNull { it.uniqueId == id }
    }

    fun fetchNotificationData() {
        if (!userSession.isLoggedIn) return
        viewModelScope.launch {
            runCatching {
                getNotificationUseCase(userSession.shopId)
            }.onSuccess {
                _notification.value = it
            }
        }
    }

    fun fetchDynamicBottomNavBar() {
        fetchBottomNavBarFromCache()
        fetchBottomNavBarFromNetwork()
    }

    private fun fetchBottomNavBarFromCache() {
        viewModelScope.launch {
            runCatching {
                getHomeBottomNavigationUseCase(
                    GetHomeBottomNavigationUseCase.FromCache(true)
                )
            }.onSuccess {
                _dynamicBottomNav.value = it.items
            }.onFailure {
                Log.e("MainParentViewModel", "Fetch NavBar From Cache Failed", it)
            }
        }
    }

    private fun fetchBottomNavBarFromNetwork() {
        viewModelScope.launch {
            runCatching {
                getHomeBottomNavigationUseCase(
                    GetHomeBottomNavigationUseCase.FromCache(false)
                )
            }.onSuccess {
                _nextDynamicBottomNav.value = it
            }.onFailure {
                Log.e("MainParentViewModel", "Fetch NavBar From Network Failed", it)
            }
        }
    }
}
