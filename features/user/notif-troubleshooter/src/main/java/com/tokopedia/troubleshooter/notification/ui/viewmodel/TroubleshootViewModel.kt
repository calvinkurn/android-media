package com.tokopedia.troubleshooter.notification.ui.viewmodel

import android.media.RingtoneManager.TYPE_NOTIFICATION
import android.net.Uri
import androidx.lifecycle.*
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.config.GlobalConfig
import com.tokopedia.fcmcommon.FirebaseMessagingManager
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.settingnotif.usersetting.data.pojo.UserNotificationResponse
import com.tokopedia.settingnotif.usersetting.domain.GetUserSettingUseCase
import com.tokopedia.troubleshooter.notification.data.domain.TroubleshootStatusUseCase
import com.tokopedia.troubleshooter.notification.data.entity.NotificationSendTroubleshoot
import com.tokopedia.troubleshooter.notification.data.service.fcm.FirebaseInstanceManager
import com.tokopedia.troubleshooter.notification.data.service.notification.NotificationChannelManager
import com.tokopedia.troubleshooter.notification.data.service.notification.NotificationCompatManager
import com.tokopedia.troubleshooter.notification.data.service.ringtone.RingtoneModeService
import com.tokopedia.troubleshooter.notification.di.module.TroubleshootModule.Companion.KEY_SELLER_SETTING
import com.tokopedia.troubleshooter.notification.di.module.TroubleshootModule.Companion.KEY_USER_SETTING
import com.tokopedia.troubleshooter.notification.ui.state.DeviceSettingState
import com.tokopedia.troubleshooter.notification.ui.state.RingtoneState
import com.tokopedia.troubleshooter.notification.ui.state.StatusState
import com.tokopedia.troubleshooter.notification.ui.uiview.TickerItemUIView
import com.tokopedia.troubleshooter.notification.ui.uiview.UserSettingUIView
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Named
import android.media.RingtoneManager.getDefaultUri as getRingtoneUri
import com.tokopedia.troubleshooter.notification.ui.uiview.UserSettingUIView.Companion.merge as mergeSetting

interface TroubleshootContract {
    fun removeTickers()
    fun tickers(element: TickerItemUIView, status: StatusState)
    fun userSetting()
    fun deviceSetting()
    fun soundNotification()
    fun troubleshoot()
    fun getNewToken()
    fun updateToken(newToken: String)
    fun isDndModeEnabled()
    fun isNotificationEnabled()
}

class TroubleshootViewModel @Inject constructor(
        @Named(KEY_SELLER_SETTING) private val sellerSettingUseCase: GetUserSettingUseCase,
        @Named(KEY_USER_SETTING) private val userSettingUseCase: GetUserSettingUseCase,
        private val troubleshootUseCase: TroubleshootStatusUseCase,
        private val notificationChannel: NotificationChannelManager,
        private val notificationCompat: NotificationCompatManager,
        private val messagingManager: FirebaseMessagingManager,
        private val instanceManager: FirebaseInstanceManager,
        private val ringtoneMode: RingtoneModeService,
        private val userSession: UserSessionInterface,
        private val dispatcher: CoroutineDispatchers
) : BaseViewModel(dispatcher.io), TroubleshootContract, LifecycleObserver {

    private val _notificationStatus = MutableLiveData<Boolean>()
    val notificationStatus: LiveData<Boolean> get() = _notificationStatus

    private val _notificationSetting = MutableLiveData<Result<UserSettingUIView>>()
    val notificationSetting: LiveData<Result<UserSettingUIView>> get() = _notificationSetting

    private val _deviceSetting = MutableLiveData<Result<DeviceSettingState>>()
    val deviceSetting: LiveData<Result<DeviceSettingState>> get() = _deviceSetting

    private val _notificationRingtoneUri = MutableLiveData<Pair<Uri?, RingtoneState>>()
    val notificationRingtoneUri: LiveData<Pair<Uri?, RingtoneState>> get() = _notificationRingtoneUri

    private val _troubleshoot = MutableLiveData<Result<NotificationSendTroubleshoot>>()
    val troubleshoot: LiveData<Result<NotificationSendTroubleshoot>> get() = _troubleshoot

    private val _token = MediatorLiveData<Result<String>>()
    val token: LiveData<Result<String>> get() = _token

    private val _tickerItems = mutableListOf<TickerItemUIView>()
    val tickerItems: List<TickerItemUIView> get() = _tickerItems

    private val _dndMode = MutableLiveData<Boolean>()
    val dndMode: LiveData<Boolean> get() = _dndMode

    init {
        _token.addSource(_troubleshoot) {
            getNewToken()
        }
    }

    override fun tickers(element: TickerItemUIView, status: StatusState) {
        if (_tickerItems.contains(element)) return
        _tickerItems.add(element)
    }

    override fun removeTickers() {
        _tickerItems.clear()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    override fun isNotificationEnabled() {
        _notificationStatus.value = notificationCompat.isNotificationEnabled()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    override fun isDndModeEnabled() {
        val isDndEnabled = try {
            notificationCompat.isDndModeEnabled()
        } catch (e: Throwable) {
            false // device hasn't DnD mode
        }

        _dndMode.value = isDndEnabled
    }

    override fun troubleshoot() {
        launchCatchError(block = {
            val result = troubleshootUseCase(RequestParams.EMPTY)
            withContext(dispatcher.main) {
                _troubleshoot.value = Success(result.notificationSendTroubleshoot)
            }
        }, onError = {
            _troubleshoot.postValue(Fail(it))
        })
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    override fun userSetting() {
        launchCatchError(block = {
            val notification = userSettingUseCase.executeOnBackground()
            val seller = sellerSettingUseCase.executeOnBackground()
            withContext(dispatcher.main) {
                val userNotification = notificationSetting(notification)
                val sellerNotification = notificationSetting(seller)

                _notificationSetting.value = if (GlobalConfig.isSellerApp()) {
                    Success(sellerNotification)
                } else {
                    if (userSession.hasShop()) {
                        Success(mergeSetting(userNotification, sellerNotification))
                    } else {
                        Success(userNotification)
                    }
                }
            }
        }, onError = {
            _notificationSetting.postValue(Fail(it))
        })
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    override fun deviceSetting() {
        if (!notificationCompat.isNotificationEnabled()) {
            _deviceSetting.value = Fail(Throwable(""))
            return
        }

        if (notificationChannel.hasNotificationChannel()) {
            // check status channel
            if (notificationChannel.isNotificationChannelEnabled()) {
                _deviceSetting.value = Fail(Throwable(""))
                return
            }

            // check importance
            if (notificationChannel.isImportanceChannel()) {
                _deviceSetting.value = Success(DeviceSettingState.High)
            } else {
                _deviceSetting.value = Success(DeviceSettingState.Low)
            }
        } else {
            _deviceSetting.value = Success(DeviceSettingState.None)
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    override fun soundNotification() {
        val ringtoneUri = getRingtoneUri(TYPE_NOTIFICATION)
        val isRinging = ringtoneMode.isRing()
        _notificationRingtoneUri.value = Pair(ringtoneUri, isRinging)
    }

    override fun updateToken(newToken: String) {
        messagingManager.onNewToken(newToken)
    }

    override fun getNewToken() {
        instanceManager.getNewToken({ token ->
            _token.value = Success(token)
        }, {
            _token.value = Fail(it)
        })
    }

    private fun notificationSetting(result: UserNotificationResponse): UserSettingUIView {
        val userNotification = UserSettingUIView()
        result.userSetting.settingSections.forEach { section ->
            section?.listSettings?.forEach {
                userNotification.notifications++
                if (it?.status == true) {
                    userNotification.totalOn++
                }
            }
        }
        return userNotification
    }

}