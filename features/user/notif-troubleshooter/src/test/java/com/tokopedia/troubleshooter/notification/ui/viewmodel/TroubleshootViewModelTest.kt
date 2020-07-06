package com.tokopedia.troubleshooter.notification.ui.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.tokopedia.fcmcommon.FirebaseMessagingManager
import com.tokopedia.troubleshooter.notification.data.FirebaseInstanceManager
import com.tokopedia.troubleshooter.notification.data.domain.TroubleshootStatusUseCase
import com.tokopedia.troubleshooter.notification.data.entity.NotificationSendTroubleshoot
import com.tokopedia.troubleshooter.notification.data.entity.NotificationTroubleshoot
import com.tokopedia.troubleshooter.notification.util.TestDispatcherProvider
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

    private lateinit var viewModel: TroubleshootViewModel

    private val troubleshootObservable: Observer<NotificationSendTroubleshoot> = mockk(relaxed = true)
    private val errorObservable: Observer<Throwable> = mockk(relaxed = true)
    private val dispatcherProvider = TestDispatcherProvider()

    @Before fun setUp() {
        viewModel = TroubleshootViewModel(
                troubleshootUseCase,
                messagingManager,
                instanceManager,
                dispatcherProvider
        )

        viewModel.troubleshoot.observeForever(troubleshootObservable)
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

        viewModel.updateToken()

        verify { instanceManager.getNewToken(any()) }
        verify { messagingManager.onNewToken(token) }
    }

}