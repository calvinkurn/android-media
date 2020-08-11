package com.tokopedia.troubleshooter.notification.ui.viewmodel

import android.content.Context
import android.net.Uri
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.tokopedia.fcmcommon.FirebaseMessagingManager
import com.tokopedia.troubleshooter.notification.data.domain.TroubleshootStatusUseCase
import com.tokopedia.troubleshooter.notification.data.entity.NotificationSendTroubleshoot
import com.tokopedia.troubleshooter.notification.data.entity.NotificationTroubleshoot
import com.tokopedia.troubleshooter.notification.data.service.channel.NotificationChannelManager
import com.tokopedia.troubleshooter.notification.data.service.fcm.FirebaseInstanceManager
import com.tokopedia.troubleshooter.notification.data.service.notification.NotificationCompatManager
import com.tokopedia.troubleshooter.notification.util.dispatchers.TestDispatcherProvider
import com.tokopedia.troubleshooter.notification.util.isEqualsTo
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class TroubleshootViewModelTest {

    @get:Rule val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val troubleshootUseCase: TroubleshootStatusUseCase = mockk(relaxed = true)
    private val messagingManager: FirebaseMessagingManager = mockk(relaxed = true)
    private val instanceManager: FirebaseInstanceManager = mockk(relaxed = true)
    private val channelManager: NotificationChannelManager = mockk(relaxed = true)
    private val compatManager: NotificationCompatManager = mockk(relaxed = true)

    private lateinit var viewModel: TroubleshootViewModel
    private val context: Context = mockk(relaxed = true)

    private val troubleshootObservable: Observer<NotificationSendTroubleshoot> = mockk(relaxed = true)
    private val notificationObservable: Observer<Boolean> = mockk(relaxed = true)
    private val errorObservable: Observer<Throwable> = mockk(relaxed = true)
    private val ringtoneObservable: Observer<Uri?> = mockk(relaxed = true)
    private val tokenObservable: Observer<String> = mockk(relaxed = true)
    private val channelObservable: Observer<Int> = mockk(relaxed = true)

    private val dispatcherProvider = TestDispatcherProvider()

    @Before fun setUp() {
        viewModel = TroubleshootViewModel(
                troubleshootUseCase,
                channelManager,
                compatManager,
                messagingManager,
                instanceManager,
                dispatcherProvider
        )

        viewModel.notificationSetting.observeForever(notificationObservable)
        viewModel.notificationRingtoneUri.observeForever(ringtoneObservable)
        viewModel.notificationImportance.observeForever(channelObservable)
        viewModel.troubleshoot.observeForever(troubleshootObservable)
        viewModel.token.observeForever(tokenObservable)
        viewModel.error.observeForever(errorObservable)
    }

    @Test fun `it should troubleshoot push notification properly`() = runBlockingTest {
        val expectedReturn = NotificationTroubleshoot()
        val expectedValue = expectedReturn.notificationSendTroubleshoot

        coEvery {
            troubleshootUseCase(any())
        } returns expectedReturn

        viewModel.troubleshoot()

        verify { troubleshootObservable.onChanged(expectedValue) }
        viewModel.troubleshoot isEqualsTo expectedValue
    }

    @Test fun `it should cannot troubleshoot`() = runBlockingTest {
        val expectedValue = Throwable()
        coEvery {
            troubleshootUseCase(any())
        } throws expectedValue

        viewModel.troubleshoot()

        verify { errorObservable.onChanged(expectedValue) }
    }

    @Test fun `it should return new token`() {
        val token = "123"

        every { instanceManager.getNewToken(any()) } answers {
            firstArg<(String) -> Unit>().invoke(token)
        }

        viewModel.getNewToken()

        verify { instanceManager.getNewToken(any()) }
        verify { tokenObservable.onChanged(token) }

        viewModel.token isEqualsTo token
    }

    @Test fun `it should update new token`() {
        val token = "123"
        viewModel.updateToken(token)
        verify { messagingManager.onNewToken(token) }
    }

    @Test fun `it should get importance notification channel correctly`() {
        val expectedValue = -1000
        val isAndroid26 = true
        every { channelManager.hasNotificationChannel() } returns isAndroid26
        every { channelManager.getNotificationChannel() } returns expectedValue

        viewModel.getImportanceNotification()

        verify { channelObservable.onChanged(expectedValue) }
        viewModel.notificationImportance isEqualsTo expectedValue
    }

    @Test fun `it should cannot get importance notification channel`() {
        val expectedValue = Int.MAX_VALUE
        val isAndroid26 = false
        every { channelManager.hasNotificationChannel() } returns isAndroid26

        viewModel.getImportanceNotification()

        verify { channelObservable.onChanged(expectedValue) }
        viewModel.notificationImportance isEqualsTo expectedValue
    }

    @Test fun `it should return notification settings enable state`() {
        val expectedValue = true
        every { compatManager.isNotificationEnabled() } returns expectedValue

        viewModel.isNotificationEnabled()

        verify { notificationObservable.onChanged(expectedValue) }
        viewModel.notificationSetting isEqualsTo expectedValue
    }

    @Test fun `it should return notification settings disable state`() {
        val expectedValue = false
        every { compatManager.isNotificationEnabled() } returns expectedValue

        viewModel.isNotificationEnabled()

        verify { notificationObservable.onChanged(expectedValue) }
        viewModel.notificationSetting isEqualsTo expectedValue
    }

    @Test fun `it should return notification ringtone uri`() {
        viewModel.getSoundNotification()
        verify { ringtoneObservable.onChanged(any()) }
    }

}