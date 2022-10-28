package com.tokopedia.navigation.presentation.presenter

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.navigation.data.entity.NotificationEntity
import com.tokopedia.navigation.domain.GetBottomNavNotificationUseCase
import com.tokopedia.navigation.domain.subscriber.NotificationSubscriber
import com.tokopedia.navigation.presentation.view.MainParentView
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.ArgumentMatchers.anyBoolean

@ExperimentalCoroutinesApi
class MainParentPresenterTest {

    @RelaxedMockK
    lateinit var getBottomNavNotificationUseCase: GetBottomNavNotificationUseCase

    @RelaxedMockK
    lateinit var userSession: UserSessionInterface

    @RelaxedMockK
    lateinit var mainParentView: MainParentView

    private val mainParenPresenter by lazy {
        MainParentPresenter(getBottomNavNotificationUseCase, userSession)
    }

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        mainParenPresenter.setView(mainParentView)
    }

    @Test
    fun `get notification`() {
        val notificationEntity = NotificationEntity()

        every {
            userSession.isLoggedIn
        } returns true

        every {
            getBottomNavNotificationUseCase.execute(any(), any())
        } answers {
            secondArg<NotificationSubscriber>().onStart()
            secondArg<NotificationSubscriber>().onCompleted()
            secondArg<NotificationSubscriber>().onError(Throwable())
            secondArg<NotificationSubscriber>().onNext(notificationEntity)
        }

        mainParenPresenter.getNotificationData()

        verify {
            mainParentView.onStartLoading()
            getBottomNavNotificationUseCase.execute(any(), any())
        }
    }

    @Test
    fun `given failed start loading when get notification data then not get data notification`() {
        val notificationEntity = NotificationEntity()

        every {
            userSession.isLoggedIn
        } returns true

        every {
            mainParentView.onStartLoading()
        } throws NullPointerException()

        every {
            getBottomNavNotificationUseCase.execute(any(), any())
        } answers {
            secondArg<NotificationSubscriber>().onStart()
            secondArg<NotificationSubscriber>().onCompleted()
            secondArg<NotificationSubscriber>().onError(Throwable())
            secondArg<NotificationSubscriber>().onNext(notificationEntity)
        }

        mainParenPresenter.getNotificationData()

        verify(exactly = 0) { getBottomNavNotificationUseCase.execute(any(), any()) }
    }

    @Test
    fun `on resume`() {
        val notificationEntity = NotificationEntity()

        every {
            userSession.isLoggedIn
        } returns true

        every {
            getBottomNavNotificationUseCase.execute(any(), any())
        } answers {
            secondArg<NotificationSubscriber>().onStart()
            secondArg<NotificationSubscriber>().onCompleted()
            secondArg<NotificationSubscriber>().onError(Throwable())
            secondArg<NotificationSubscriber>().onNext(notificationEntity)
        }

        mainParenPresenter.onResume()

        verify {
            mainParentView.onStartLoading()
            getBottomNavNotificationUseCase.execute(any(), any())
        }
    }

    @Test
    fun `on destroy`() {
        mainParenPresenter.onDestroy()

        every {
            getBottomNavNotificationUseCase.unsubscribe()
        }
    }

    @Test
    fun `is first time`() {
        mainParenPresenter.isFirstTimeUser

        verify {
            mainParentView.isFirstTimeUser
        }
    }

    @Test
    fun `is recurring applink`() {
        mainParenPresenter.isRecurringApplink
    }

    @Test
    fun `set is recurring applink`() {
        mainParenPresenter.setIsRecurringApplink(anyBoolean())

        Assert.assertNotNull(mainParenPresenter.isRecurringApplink)
    }

    @Test
    fun `is logged in`() {
        mainParenPresenter.isUserLogin

        verify {
            userSession.isLoggedIn
        }
    }
}