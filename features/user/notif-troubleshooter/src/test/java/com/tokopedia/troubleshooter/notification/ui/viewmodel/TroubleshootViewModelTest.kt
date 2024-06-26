package com.tokopedia.troubleshooter.notification.ui.viewmodel

import android.net.Uri
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.tokopedia.config.GlobalConfig
import com.tokopedia.fcmcommon.FirebaseMessagingManager
import com.tokopedia.settingnotif.usersetting.data.pojo.SettingSections
import com.tokopedia.settingnotif.usersetting.data.pojo.UserNotificationResponse
import com.tokopedia.settingnotif.usersetting.data.pojo.UserSetting
import com.tokopedia.settingnotif.usersetting.domain.GetUserSettingUseCase
import com.tokopedia.troubleshooter.notification.data.domain.GetTroubleshootStatusUseCase
import com.tokopedia.troubleshooter.notification.data.entity.NotificationSendTroubleshoot
import com.tokopedia.troubleshooter.notification.data.entity.NotificationTroubleshoot
import com.tokopedia.troubleshooter.notification.data.service.fcm.FirebaseInstanceManager
import com.tokopedia.troubleshooter.notification.data.service.googleplay.PlayServicesManager
import com.tokopedia.troubleshooter.notification.data.service.notification.NotificationChannelManager
import com.tokopedia.troubleshooter.notification.data.service.notification.NotificationCompatManager
import com.tokopedia.troubleshooter.notification.data.service.ringtone.RingtoneModeService
import com.tokopedia.troubleshooter.notification.ui.state.DeviceSettingState
import com.tokopedia.troubleshooter.notification.ui.state.RingtoneState
import com.tokopedia.troubleshooter.notification.ui.state.StatusState
import com.tokopedia.troubleshooter.notification.ui.uiview.TickerItemUIView
import com.tokopedia.troubleshooter.notification.ui.uiview.UserSettingUIView
import com.tokopedia.troubleshooter.notification.util.FileUtils
import com.tokopedia.troubleshooter.notification.util.isEqualsTo
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.*
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class TroubleshootViewModelTest {

    @get:Rule val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val troubleshootUseCase: GetTroubleshootStatusUseCase = mockk(relaxed = true)
    private val notificationChannel: NotificationChannelManager = mockk(relaxed = true)
    private val notificationCompat: NotificationCompatManager = mockk(relaxed = true)
    private val messagingManager: FirebaseMessagingManager = mockk(relaxed = true)
    private val userSettingUseCase: GetUserSettingUseCase = mockk(relaxed = true)
    private val instanceManager: FirebaseInstanceManager = mockk(relaxed = true)
    private val ringtoneMode: RingtoneModeService = mockk(relaxed = true)
    private val userSession: UserSessionInterface = mockk(relaxed = true)
    private val playServicesManager: PlayServicesManager = mockk(relaxed = true)

    private val notificationStatus: Observer<Boolean> = mockk(relaxed = true)
    private val notificationSetting: Observer<Result<UserSettingUIView>> = mockk(relaxed = true)
    private val deviceSetting: Observer<Result<DeviceSettingState>> = mockk(relaxed = true)
    private val notificationRingtoneUri: Observer<Pair<Uri?, RingtoneState>> = mockk(relaxed = true)
    private val troubleshootSuccess: Observer<NotificationSendTroubleshoot> = mockk(relaxed = true)
    private val troubleshootError: Observer<Throwable> = mockk(relaxed = true)
    private val tokenObserver: Observer<Result<String>> = mockk(relaxed = true)
    private val dndMode: Observer<Boolean> = mockk(relaxed = true)
    private val playServicesObserver: Observer<Result<Boolean>> = mockk(relaxed = true)

    private val dispatcherProvider = CoroutineTestDispatchersProvider

    private lateinit var viewModel: TroubleshootViewModel

    @Before fun setUp() {
        viewModel = TroubleshootViewModel(
                userSettingUseCase,
                userSettingUseCase,
                troubleshootUseCase,
                notificationChannel,
                notificationCompat,
                messagingManager,
                instanceManager,
                ringtoneMode,
                userSession,
                dispatcherProvider,
                playServicesManager
        )

        viewModel.notificationStatus.observeForever(notificationStatus)
        viewModel.notificationSetting.observeForever(notificationSetting)
        viewModel.deviceSetting.observeForever(deviceSetting)
        viewModel.notificationRingtoneUri.observeForever(notificationRingtoneUri)
        viewModel.troubleshootSuccess.observeForever(troubleshootSuccess)
        viewModel.troubleshootError.observeForever(troubleshootError)
        viewModel.token.observeForever(tokenObserver)
        viewModel.dndMode.observeForever(dndMode)
        viewModel.playServicesSetting.observeForever(playServicesObserver)
    }

    @Test fun `it should return notification status as true`() {
        val expectedValue = true
        every { notificationCompat.isNotificationEnabled() } returns expectedValue

        viewModel.isNotificationEnabled()

        verify { notificationStatus.onChanged(expectedValue) }
    }

    @Test fun `it should return notification status as false`() {
        val expectedValue = false
        every { notificationCompat.isNotificationEnabled() } returns expectedValue

        viewModel.isNotificationEnabled()

        verify { notificationStatus.onChanged(expectedValue) }
    }

    @Test fun `it should troubleshoot push notification properly`() = runTest {
        val expectedValue = NotificationTroubleshoot().notificationSendTroubleshoot

        coEvery {
            troubleshootUseCase(Unit)
        } returns NotificationTroubleshoot()

        viewModel.troubleshoot()

        verify { troubleshootSuccess.onChanged(expectedValue) }
        viewModel.troubleshootSuccess isEqualsTo expectedValue
    }

    @Test fun `it should cannot troubleshoot`() = runTest {
        val expectedValue = Throwable()

        coEvery { troubleshootUseCase(Unit) } throws expectedValue

        viewModel.troubleshoot()

        verify { troubleshootError.onChanged(expectedValue) }
        viewModel.troubleshootError isEqualsTo expectedValue
    }

    @Test fun `it should return new token`() {
        val token = "123"
        val expectedValue = Success(token)

        every { instanceManager.getNewToken(any(), any()) } answers {
            firstArg<(String) -> Unit>().invoke(token)
        }

        viewModel.getNewToken()

        verify { instanceManager.getNewToken(any(), any()) }
        verify { tokenObserver.onChanged(expectedValue) }

        viewModel.token isEqualsTo expectedValue
    }

    @Test fun `it should throw if request a new token`() {
        val throwable = Throwable("")
        val expectedValue = Fail(throwable)

        every { instanceManager.getNewToken(any(), any()) } answers {
            secondArg<(Throwable) -> Unit>().invoke(throwable)
        }

        viewModel.getNewToken()

        verify { instanceManager.getNewToken(any(), any()) }
        verify { tokenObserver.onChanged(expectedValue) }

        viewModel.token isEqualsTo expectedValue
    }

    @Test fun `it should update new token`() {
        val token = "123"
        viewModel.updateToken(token)
        verify { messagingManager.onNewToken(token) }
    }

    @Test fun `it should get high importance notification channel correctly`() {
        val expectedValue = Success(DeviceSettingState.High)

        every { notificationCompat.isNotificationEnabled() } returns true
        every { notificationChannel.hasNotificationChannel() } returns true
        every { notificationChannel.isImportanceChannel() } returns true

        viewModel.deviceSetting()

        verify { deviceSetting.onChanged(expectedValue) }
        viewModel.deviceSetting isEqualsTo expectedValue
    }

    @Test fun `it should get low importance notification channel correctly`() {
        val expectedValue = Success(DeviceSettingState.Low)

        every { notificationCompat.isNotificationEnabled() } returns true
        every { notificationChannel.hasNotificationChannel() } returns true
        every { notificationChannel.isImportanceChannel() } returns false

        viewModel.deviceSetting()

        verify { deviceSetting.onChanged(expectedValue) }
        viewModel.deviceSetting isEqualsTo expectedValue
    }

    @Test fun `it should get none of notification channel`() {
        val expectedValue = Success(DeviceSettingState.None)

        every { notificationCompat.isNotificationEnabled() } returns true
        every { notificationChannel.hasNotificationChannel() } returns false

        viewModel.deviceSetting()

        verify { deviceSetting.onChanged(expectedValue) }
        viewModel.deviceSetting isEqualsTo expectedValue
    }

    @Test fun `it should get fail of device setting if notification disabled`() {
        val expectedValue = Fail(Throwable(""))
        every { notificationCompat.isNotificationEnabled() } returns false

        viewModel.deviceSetting()

        viewModel.deviceSetting.value isEqualsTo expectedValue
    }

    @Test fun `it should get fail of device setting if notification channel disabled`() {
        val expectedValue = Fail(Throwable(""))

        every { notificationCompat.isNotificationEnabled() } returns true
        every { notificationChannel.hasNotificationChannel() } returns true
        every { notificationChannel.isNotificationChannelEnabled() } returns true

        viewModel.deviceSetting()

        viewModel.deviceSetting.value isEqualsTo expectedValue
    }

    @Test fun `it should return seller settings properly`() {
        runTest {
            mockkStatic(GlobalConfig::class)

            val response = FileUtils.parse<UserNotificationResponse>(
                fileName = "/success_seller_notif_setting.json",
                typeOfT = UserNotificationResponse::class.java
            )

            val resultUiModel = viewModel.notificationSetting(response)
            val expectedValue = Success(resultUiModel)

            every { GlobalConfig.isSellerApp() } returns true
            coEvery { userSettingUseCase.executeOnBackground() } returns response

            viewModel.userSetting()

            verify { notificationSetting.onChanged(expectedValue) }
        }
    }

    @Test fun `it should return user settings as buyer and have shop properly`() {
        runTest {
            mockkStatic(GlobalConfig::class)

            val response = FileUtils.parse<UserNotificationResponse>(
                fileName = "/success_notif_setting.json",
                typeOfT = UserNotificationResponse::class.java
            )

            every { GlobalConfig.isSellerApp() } returns false
            every { userSession.hasShop() } returns true

            coEvery { userSettingUseCase.executeOnBackground() } returns response

            viewModel.userSetting()

            verify(atLeast = 1) { notificationSetting.onChanged(any()) }
        }
    }

    @Test fun `it should return user settings as buyer and did not have shop properly`() {
        mockkStatic(GlobalConfig::class)

        val response = FileUtils.parse<UserNotificationResponse>(
            fileName = "/success_notif_setting.json",
            typeOfT = UserNotificationResponse::class.java
        )

        val resultUiModel = viewModel.notificationSetting(response)
        val expectedValue = Success(resultUiModel)

        every { GlobalConfig.isSellerApp() } returns false
        every { userSession.hasShop() } returns false

        coEvery { userSettingUseCase.executeOnBackground() } returns response

        viewModel.userSetting()

        verify { notificationSetting.onChanged(expectedValue) }
    }

    @Test fun `it should cannot get user settings`() = runTest {
        val expectedValue = Throwable()

        coEvery {
            userSettingUseCase.executeOnBackground()
        } throws expectedValue

        viewModel.userSetting()

        verify { notificationSetting.onChanged(Fail(expectedValue)) }
    }

    @Test fun `it should return notification ringtone normal state`() {
        every { ringtoneMode.isRing() } returns RingtoneState.Normal
        viewModel.soundNotification()
        verify { notificationRingtoneUri.onChanged(any()) }
    }

    @Test fun `it should return enable state if google play services is present`() {
        every { playServicesManager.isPlayServiceExist() } returns true
        viewModel.playServicesSetting()
        verify { playServicesObserver.onChanged(any()) }
    }

    @Test fun `it should return disable state if google play services is absent`() {
        val expectedValue = Fail(Throwable(""))
        every { playServicesManager.isPlayServiceExist() } returns false
        viewModel.playServicesSetting()
        viewModel.playServicesSetting.value isEqualsTo expectedValue
    }

    @Test fun `it should return enabled state of do not disturb mode`() {
        every { notificationCompat.isDndModeEnabled() } returns true
        viewModel.isDndModeEnabled()
        verify { dndMode.onChanged(true) }
    }

    @Test fun `it should return disabled state of do not disturb mode`() {
        every { notificationCompat.isDndModeEnabled() } returns false
        viewModel.isDndModeEnabled()
        verify { dndMode.onChanged(false) }
    }

    @Test fun `it should return disabled state if there's no DnD mode`() {
        every { notificationCompat.isDndModeEnabled() } throws Throwable("")
        viewModel.isDndModeEnabled()
        verify { dndMode.onChanged(false) }
    }

    @Test fun `it should added a ticker`() {
        viewModel.removeTickers()
        viewModel.tickers(TickerItemUIView())
        assert(viewModel.tickerItems.isNotEmpty())
    }

    @Test fun `it should did not add a ticker`() {
        viewModel.tickers(TickerItemUIView())
        viewModel.tickers(TickerItemUIView())
    }

}
