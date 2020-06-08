package com.tokopedia.home.account.presentation.presenter

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.home.account.data.pojo.NotifCenterSendNotifData
import com.tokopedia.home.account.domain.SendNotifUseCase
import com.tokopedia.home.account.presentation.listener.LogoutView
import com.tokopedia.logout.domain.model.LogoutDomain
import com.tokopedia.logout.domain.usecase.LogoutUseCase
import com.tokopedia.navigation_common.model.WalletPref
import com.tokopedia.user.session.UserSession
import io.mockk.*
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import rx.Subscriber

@ExperimentalCoroutinesApi
class LogoutPresenterTest {

    @RelaxedMockK
    lateinit var logoutUseCase: LogoutUseCase

    @RelaxedMockK
    lateinit var sendNotifUseCase: SendNotifUseCase

    @RelaxedMockK
    lateinit var userSession: UserSession

    @RelaxedMockK
    lateinit var walletPref: WalletPref

    @RelaxedMockK
    lateinit var logoutView: LogoutView

    private val logoutPresenter by lazy {
        LogoutPresenter(logoutUseCase, sendNotifUseCase, userSession, walletPref)
    }

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        logoutPresenter.attachView(logoutView)
    }

    /**
     * logout
     * */
    @Test
    fun `do logout | on success`() {
        val logoutDomain = LogoutDomain(true)

        every {
            logoutUseCase.execute(any(), any())
        } answers {
            secondArg<Subscriber<LogoutDomain>>().onStart()
            secondArg<Subscriber<LogoutDomain>>().onNext(logoutDomain)
        }

        logoutPresenter.doLogout()

        verify {
            logoutUseCase.execute(any(), any())
        }

        verify {
            walletPref.clear()
            logoutView.onSuccessLogout()
        }
    }

    @Test
    fun `do logout | on error`() {
        val throwable = Throwable("Ops!")

        every {
            logoutUseCase.execute(any(), any())
        } answers {
            secondArg<Subscriber<LogoutDomain>>().onStart()
            secondArg<Subscriber<LogoutDomain>>().onError(throwable)
        }

        logoutPresenter.doLogout()

        verify {
            logoutUseCase.execute(any(), any())
        }

        verify {
            logoutView.onErrorLogout(throwable)
        }
    }

    @Test
    fun `do logout | on completed`() {
        every {
            logoutUseCase.execute(any(), any())
        } answers {
            secondArg<Subscriber<LogoutDomain>>().onStart()
            secondArg<Subscriber<LogoutDomain>>().onCompleted()
        }

        logoutPresenter.doLogout()

        verify {
            logoutUseCase.execute(any(), any())
        }

        verify {
            logoutView.logoutFacebook()
        }
    }

    /**
     * notification
     * */
    @Test
    fun `send notification`() {
        val notificaSendNotifData = NotifCenterSendNotifData()
        val onError = mockk<(Throwable) -> Unit>()

        every {
            sendNotifUseCase.executeCoroutines(captureLambda(), any())
        } answers {
            val onSuccess = lambda<(NotifCenterSendNotifData) -> Unit>()
            onSuccess.invoke(notificaSendNotifData)
        }

        logoutPresenter.sendNotif({
            Assert.assertEquals(it, notificaSendNotifData)
        }, onError)

        verify {
            sendNotifUseCase.executeCoroutines(any(), any())
        }
    }

    /**
     * on detach
     * */
    @Test
    fun `on detach view`() {
        logoutPresenter.detachView()

        verify {
            logoutUseCase.unsubscribe()
        }
    }
}