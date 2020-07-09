package com.tokopedia.troubleshooter.notification.ui.viewmodel

import android.app.NotificationManager
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import androidx.core.app.NotificationManagerCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.fcmcommon.FirebaseMessagingManager
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.troubleshooter.notification.data.FirebaseInstanceManager
import com.tokopedia.troubleshooter.notification.data.domain.TroubleshootStatusUseCase
import com.tokopedia.troubleshooter.notification.data.entity.NotificationSendTroubleshoot
import com.tokopedia.troubleshooter.notification.di.TroubleshootContext
import com.tokopedia.troubleshooter.notification.util.DispatcherProvider
import com.tokopedia.usecase.RequestParams
import kotlinx.coroutines.withContext
import javax.inject.Inject

interface TroubleshootContract {
    fun getNewToken()
    fun updateToken(newToken: String)
    fun troubleshoot()
    fun getSetting()
    fun getImportanceNotification()
    fun getSoundNotification()
}

class TroubleshootViewModel @Inject constructor(
        private val troubleshootUseCase: TroubleshootStatusUseCase,
        private val messagingManager: FirebaseMessagingManager,
        private val instanceManager: FirebaseInstanceManager,
        @TroubleshootContext private val context: Context,
        private val dispatcher: DispatcherProvider
) : BaseViewModel(dispatcher.io()), TroubleshootContract {

    private val _notificationSetting = MutableLiveData<Boolean>()
    val notificationSetting: LiveData<Boolean> get() = _notificationSetting

    private val _notificationImportance = MutableLiveData<Int>()
    val notificationImportance: LiveData<Int> get() = _notificationImportance

    private val _notificationSoundUri = MutableLiveData<Uri?>()
    val notificationSoundUri: LiveData<Uri?> get() = _notificationSoundUri

    private val _troubleshoot = MutableLiveData<NotificationSendTroubleshoot>()
    val troubleshoot: LiveData<NotificationSendTroubleshoot> get() = _troubleshoot

    private val _token = MutableLiveData<String>()
    val token: LiveData<String> get() = _token

    private val _error = MutableLiveData<Throwable>()
    val error: LiveData<Throwable> get() = _error

    init {
        getSetting()
        getImportanceNotification()
        getSoundNotification()
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

    override fun getNewToken() {
        instanceManager.getNewToken { token ->
            _token.value = token
        }
    }

    override fun updateToken(newToken: String) {
        messagingManager.onNewToken(newToken)
    }

    override fun getImportanceNotification() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val manager = context.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            val channel = manager.getNotificationChannel(CHANNEL_ID)
            _notificationImportance.value = channel.importance
        } else {
            _notificationImportance.value = Int.MAX_VALUE
        }
    }

    override fun getSetting() {
        _notificationSetting.value = NotificationManagerCompat
                .from(context)
                .areNotificationsEnabled()
    }

    override fun getSoundNotification() {
        _notificationSoundUri.value = RingtoneManager.getDefaultUri(
                RingtoneManager.TYPE_NOTIFICATION
        )
    }

    companion object {
        private const val CHANNEL_ID = "ANDROID_GENERAL_CHANNEL"
    }

}