package com.tokopedia.troubleshooter.notification.ui.viewmodel

import android.media.RingtoneManager.TYPE_NOTIFICATION
import android.net.Uri
import androidx.lifecycle.*
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.fcmcommon.FirebaseMessagingManager
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.troubleshooter.notification.data.domain.TroubleshootStatusUseCase
import com.tokopedia.troubleshooter.notification.data.entity.NotificationSendTroubleshoot
import com.tokopedia.troubleshooter.notification.data.service.channel.NotificationChannelManager
import com.tokopedia.troubleshooter.notification.data.service.fcm.FirebaseInstanceManager
import com.tokopedia.troubleshooter.notification.data.service.notification.NotificationCompatManager
import com.tokopedia.troubleshooter.notification.util.dispatchers.DispatcherProvider
import com.tokopedia.usecase.RequestParams
import kotlinx.coroutines.withContext
import javax.inject.Inject
import android.media.RingtoneManager.getDefaultUri as getRingtoneUri

interface TroubleshootContract {
    fun getNewToken()
    fun updateToken(newToken: String)
    fun troubleshoot()
    fun isNotificationEnabled()
    fun getImportanceNotification()
    fun getSoundNotification()
}

class TroubleshootViewModel @Inject constructor(
        private val troubleshootUseCase: TroubleshootStatusUseCase,
        private val notificationChannel: NotificationChannelManager,
        private val notificationCompat: NotificationCompatManager,
        private val messagingManager: FirebaseMessagingManager,
        private val instanceManager: FirebaseInstanceManager,
        private val dispatcher: DispatcherProvider
) : BaseViewModel(dispatcher.io()), TroubleshootContract, LifecycleObserver {

    private val _notificationSetting = MutableLiveData<Boolean>()
    val notificationSetting: LiveData<Boolean> get() = _notificationSetting

    private val _notificationImportance = MutableLiveData<Int>()
    val notificationImportance: LiveData<Int> get() = _notificationImportance

    private val _notificationSoundUri = MutableLiveData<Uri?>()
    val notificationSoundUri: LiveData<Uri?> get() = _notificationSoundUri

    private val _troubleshoot = MutableLiveData<NotificationSendTroubleshoot>()
    val troubleshoot: LiveData<NotificationSendTroubleshoot> get() = _troubleshoot

    private val _token = MediatorLiveData<String>()
    val token: LiveData<String> get() = _token

    private val _error = MutableLiveData<Throwable>()
    val error: LiveData<Throwable> get() = _error

    init {
        _token.addSource(_troubleshoot) {
            getNewToken()
        }
    }

    override fun troubleshoot() {
        launchCatchError(block = {
            val result = troubleshootUseCase(RequestParams.EMPTY)
            withContext(dispatcher.main()) {
                _troubleshoot.value = result.notificationSendTroubleshoot
            }
        }, onError = {
            _error.value = it
        })
    }

    override fun updateToken(newToken: String) {
        messagingManager.onNewToken(newToken)
    }

    override fun getNewToken() {
        instanceManager.getNewToken { token ->
            _token.value = token
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    override fun getImportanceNotification() {
        if (notificationChannel.hasNotificationChannel()) {
            val channel = notificationChannel.getNotificationChannel()
            _notificationImportance.value = channel
        } else {
            _notificationImportance.value = Int.MAX_VALUE
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    override fun isNotificationEnabled() {
        _notificationSetting.value = notificationCompat.isNotificationEnabled()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    override fun getSoundNotification() {
        val ringtone = getRingtoneUri(TYPE_NOTIFICATION)
        _notificationSoundUri.value = ringtone
    }

}