package com.tokopedia.inbox.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.inbox.common.InboxCoroutineDispatcher
import com.tokopedia.inbox.domain.cache.InboxCacheManager
import com.tokopedia.inbox.domain.data.notification.Notifications
import com.tokopedia.inbox.domain.usecase.InboxNotificationUseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

class InboxViewModel @Inject constructor(
        private val notificationUseCase: InboxNotificationUseCase,
        private val userSession: UserSessionInterface,
        private val cacheManager: InboxCacheManager,
        dispatchers: InboxCoroutineDispatcher
) : BaseViewModel(dispatchers.IO) {

    private val _notifications = MutableLiveData<Result<Notifications>>()
    val notifications: LiveData<Result<Notifications>>
        get() = _notifications

    fun getNotifications() {
        notificationUseCase.getNotification(
                {
                    _notifications.value = Success(it)
                },
                {
                    _notifications.value = Fail(it)
                }
        )
    }

    fun hasShowOnBoarding(): Boolean {
        val isBuyerOnly = !userSession.hasShop()
        val hasShownSeller = cacheManager.loadCacheBoolean(KEY_ONBOARDING_SELLER) ?: false
        val hasShownBuyer = cacheManager.loadCacheBoolean(KEY_ONBOARDING_BUYER) ?: false
        return if (isBuyerOnly && !hasShownSeller) {
            hasShownBuyer
        } else {
            hasShownSeller
        }
    }

    fun markFinishedBuyerOnBoarding() {
        cacheManager.saveCacheBoolean(KEY_ONBOARDING_BUYER, true)
    }

    fun markFinishedSellerOnBoarding() {
        cacheManager.saveCacheBoolean(KEY_ONBOARDING_SELLER, true)
    }

    companion object {
        private const val KEY_ONBOARDING_SELLER = "key_onboarding_seller"
        private const val KEY_ONBOARDING_BUYER = "key_onboarding_buyer"
    }
}