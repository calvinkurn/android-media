package com.tokopedia.navigation.presentation.presenter

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.navigation.data.entity.NotificationEntity
import com.tokopedia.navigation.domain.GetDrawerNotificationUseCase
import com.tokopedia.navigation.domain.subscriber.DrawerNotificationSubscriber
import com.tokopedia.navigation.presentation.view.NotificationView
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class NotificationPresenterTest {

    @RelaxedMockK
    lateinit var getDrawerNotificationUseCase: GetDrawerNotificationUseCase

    @RelaxedMockK
    lateinit var notificationView: NotificationView

    private val notificationPresenter by lazy {
        NotificationPresenter(getDrawerNotificationUseCase)
    }

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        notificationPresenter.setView(notificationView)
    }

    @Test
    fun `get drawer notification`() {
        val notificationEntity = NotificationEntity()

        every {
            getDrawerNotificationUseCase.execute(any(), any())
        } answers {
            secondArg<DrawerNotificationSubscriber>().onStart()
            secondArg<DrawerNotificationSubscriber>().onCompleted()
            secondArg<DrawerNotificationSubscriber>().onError(Throwable())
            secondArg<DrawerNotificationSubscriber>().onNext(notificationEntity)
        }

        notificationPresenter.getDrawerNotification()

        verify {
            notificationView.onStartLoading()

            getDrawerNotificationUseCase.execute(any(), any())
        }
    }

    @Test
    fun `on resume`() {
        val notificationEntity = NotificationEntity()

        every {
            getDrawerNotificationUseCase.execute(any(), any())
        } answers {
            secondArg<DrawerNotificationSubscriber>().onStart()
            secondArg<DrawerNotificationSubscriber>().onCompleted()
            secondArg<DrawerNotificationSubscriber>().onError(Throwable())
            secondArg<DrawerNotificationSubscriber>().onNext(notificationEntity)
        }

        notificationPresenter.onResume()

        verify {
            notificationView.onStartLoading()

            getDrawerNotificationUseCase.execute(any(), any())
        }
    }

    @Test
    fun `on destroy`() {
        notificationPresenter.onDestroy()

        verify {
            getDrawerNotificationUseCase.unsubscribe()
        }
    }
}