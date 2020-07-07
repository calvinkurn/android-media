package com.tokopedia.troubleshooter.notification.ui.viewmodel

import android.app.NotificationManager
import android.content.Context
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import androidx.core.app.NotificationManagerCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.troubleshooter.notification.util.DispatcherProvider
import javax.inject.Inject

interface LocalNotificationSettingContract {
    fun getSetting(context: Context)
}

class LocalNotificationSettingViewModel @Inject constructor(
        private val dispatcher: DispatcherProvider
) : BaseViewModel(dispatcher.io()), LocalNotificationSettingContract {

    private val _notificationSetting = MutableLiveData<Boolean>()
    val notificationSetting: LiveData<Boolean> get() = _notificationSetting

    private val _notificationImportance = MutableLiveData<Int>()
    val notificationImportance: LiveData<Int> get() = _notificationImportance

    private val _notificationSoundUri = MutableLiveData<Uri?>()
    val notificationSoundUri: LiveData<Uri?> get() = _notificationSoundUri

    override fun getSetting(context: Context) {
        _notificationSetting.value = NotificationManagerCompat.from(context).areNotificationsEnabled()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            val channel = manager.getNotificationChannel("ANDROID_GENERAL_CHANNEL")
            _notificationImportance.value = channel.importance
        }
        _notificationSoundUri.value = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
    }

}