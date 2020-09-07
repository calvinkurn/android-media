package com.tokopedia.troubleshooter.notification.ui.viewmodel

import android.net.Uri
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.tokopedia.fcmcommon.FirebaseMessagingManager
import com.tokopedia.settingnotif.usersetting.data.pojo.UserNotificationResponse
import com.tokopedia.settingnotif.usersetting.domain.GetUserSettingUseCase
import com.tokopedia.troubleshooter.notification.data.domain.TroubleshootStatusUseCase
import com.tokopedia.troubleshooter.notification.data.entity.NotificationSendTroubleshoot
import com.tokopedia.troubleshooter.notification.data.entity.NotificationTroubleshoot
import com.tokopedia.troubleshooter.notification.data.service.channel.NotificationChannelManager
import com.tokopedia.troubleshooter.notification.data.service.fcm.FirebaseInstanceManager
import com.tokopedia.troubleshooter.notification.data.service.notification.NotificationCompatManager
import com.tokopedia.troubleshooter.notification.data.service.ringtone.RingtoneModeService
import com.tokopedia.troubleshooter.notification.ui.state.DeviceSettingState
import com.tokopedia.troubleshooter.notification.ui.state.RingtoneState
import com.tokopedia.troubleshooter.notification.ui.state.StatusState
import com.tokopedia.troubleshooter.notification.ui.uiview.TickerItemUIView
import com.tokopedia.troubleshooter.notification.ui.uiview.UserSettingUIView
import com.tokopedia.troubleshooter.notification.util.dispatchers.TestDispatcherProvider
import com.tokopedia.troubleshooter.notification.util.isEqualsTo
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.core.IsInstanceOf.instanceOf
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class TroubleshootViewModelTest {

    @get:Rule val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val troubleshootUseCase: TroubleshootStatusUseCase = mockk(relaxed = true)
    private val notificationChannel: NotificationChannelManager = mockk(relaxed = true)
    private val notificationCompat: NotificationCompatManager = mockk(relaxed = true)
    private val messagingManager: FirebaseMessagingManager = mockk(relaxed = true)
    private val userSettingUseCase: GetUserSettingUseCase = mockk(relaxed = true)
    private val instanceManager: FirebaseInstanceManager = mockk(relaxed = true)
    private val ringtoneMode: RingtoneModeService = mockk(relaxed = true)

    private val notificationSetting: Observer<Result<UserSettingUIView>> = mockk(relaxed = true)
    private val deviceSetting: Observer<Result<DeviceSettingState>> = mockk(relaxed = true)
    private val notificationRingtoneUri: Observer<Pair<Uri?, RingtoneState>> = mockk(relaxed = true)
    private val troubleshoot: Observer<Result<NotificationSendTroubleshoot>> = mockk(relaxed = true)
    private val tokenObserver: Observer<String> = mockk(relaxed = true)
    private val dndMode: Observer<Boolean> = mockk(relaxed = true)

    private val dispatcherProvider = TestDispatcherProvider()

    private lateinit var viewModel: TroubleshootViewModel

    @Before fun setUp() {
        viewModel = TroubleshootViewModel(
                troubleshootUseCase,
                notificationChannel,
                notificationCompat,
                messagingManager,
                userSettingUseCase,
                instanceManager,
                ringtoneMode,
                dispatcherProvider
        )

        viewModel.notificationSetting.observeForever(notificationSetting)
        viewModel.deviceSetting.observeForever(deviceSetting)
        viewModel.notificationRingtoneUri.observeForever(notificationRingtoneUri)
        viewModel.troubleshoot.observeForever(troubleshoot)
        viewModel.token.observeForever(tokenObserver)
        viewModel.dndMode.observeForever(dndMode)
    }

    @Test fun `it should troubleshoot push notification properly`() = runBlockingTest {
        val expectedReturn = NotificationTroubleshoot()
        val expectedValue = Success(expectedReturn.notificationSendTroubleshoot)

        coEvery {
            troubleshootUseCase(any())
        } returns expectedReturn

        viewModel.troubleshoot()

        verify { troubleshoot.onChanged(expectedValue) }
        viewModel.troubleshoot isEqualsTo expectedValue
    }

    @Test fun `it should cannot troubleshoot`() = runBlockingTest {
        val expectedValue = Throwable()
        coEvery {
            troubleshootUseCase(any())
        } throws expectedValue

        viewModel.troubleshoot()

        verify { troubleshoot.onChanged(Fail(expectedValue)) }
    }

    @Test fun `it should return new token`() {
        val token = "123"

        every { instanceManager.getNewToken(any()) } answers {
            firstArg<(String) -> Unit>().invoke(token)
        }

        viewModel.getNewToken()

        verify { instanceManager.getNewToken(any()) }
        verify { tokenObserver.onChanged(token) }

        viewModel.token isEqualsTo token
    }

    @Test fun `it should update new token`() {
        val token = "123"
        viewModel.updateToken(token)
        verify { messagingManager.onNewToken(token) }
    }

    @Test fun `it should push notification of device is disabled`() {
        every { notificationCompat.isNotificationEnabled() } returns false

        viewModel.deviceSetting()

        verify { deviceSetting.onChanged(any()) }
        assertThat(viewModel.deviceSetting.value, instanceOf(Fail::class.java))
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

    @Test fun `it should get normal importance notification channel`() {
        val expectedValue = Success(DeviceSettingState.Normal)

        every { notificationCompat.isNotificationEnabled() } returns true
        every { notificationChannel.hasNotificationChannel() } returns false

        viewModel.deviceSetting()

        verify { deviceSetting.onChanged(expectedValue) }
        viewModel.deviceSetting isEqualsTo expectedValue
    }

    @Test fun `it should return user settings properly`() {
        val userSettingMock = UserSettingUIView()
        val expectedValue = Success(userSettingMock)

        coEvery { userSettingUseCase.executeOnBackground() } returns UserNotificationResponse()

        viewModel.userSetting()

        verify { notificationSetting.onChanged(expectedValue) }
    }

    @Test fun `it should cannot get user settings`() = runBlockingTest {
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

    @Test fun `it should added tickers`() {
        viewModel.removeTickers()
        viewModel.tickers(TickerItemUIView(), StatusState.Success)
        assert(viewModel.tickerItems.isNotEmpty())
    }

}