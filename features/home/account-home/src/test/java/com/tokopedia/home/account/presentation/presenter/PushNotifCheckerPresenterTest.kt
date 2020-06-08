package com.tokopedia.home.account.presentation.presenter

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.home.account.data.model.NotifierSendTroubleshooter
import com.tokopedia.home.account.data.model.PushNotifCheckerResponse
import com.tokopedia.home.account.domain.GetPushNotifCheckerStatusUseCase
import com.tokopedia.home.account.presentation.PushNotifCheckerContract
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.ArgumentMatchers.anyString
import rx.Subscriber

@ExperimentalCoroutinesApi
class PushNotifCheckerPresenterTest {

    @RelaxedMockK
    lateinit var getPushNotifCheckerStatusUseCase: GetPushNotifCheckerStatusUseCase

    @RelaxedMockK
    lateinit var view: PushNotifCheckerContract.View


    private val presenter by lazy {
        PushNotifCheckerPresenter(getPushNotifCheckerStatusUseCase)
    }

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        presenter.attachView(view)
    }

    @Test
    fun `get status push notif checker | on success`() {

        val notifierSendTroubleshooter = NotifierSendTroubleshooter(anyInt(), anyString())
        val response = PushNotifCheckerResponse(notifierSendTroubleshooter)

        every {
            getPushNotifCheckerStatusUseCase.execute(any(), any())
        } answers {
            secondArg<Subscriber<PushNotifCheckerResponse>>().onStart()
            secondArg<Subscriber<PushNotifCheckerResponse>>().onCompleted()
            secondArg<Subscriber<PushNotifCheckerResponse>>().onNext(response)
        }

        presenter.getStatusPushNotifChecker()

        verify {
            getPushNotifCheckerStatusUseCase.execute(any(), any())
        }

        verify {
            view.onSuccessGetStatusPushNotifChecker(response.notifierSendTroubleshooter)
        }
    }

    @Test
    fun `get status push notif checker | on error`() {
        val throwable = Throwable("Ops !")

        every {
            getPushNotifCheckerStatusUseCase.execute(any(), any())
        } answers {
            secondArg<Subscriber<PushNotifCheckerResponse>>().onStart()
            secondArg<Subscriber<PushNotifCheckerResponse>>().onCompleted()
            secondArg<Subscriber<PushNotifCheckerResponse>>().onError(throwable)
        }

        presenter.getStatusPushNotifChecker()

        verify {
            getPushNotifCheckerStatusUseCase.execute(any(), any())
        }

        verify {
            view.onErrorGetPushNotifChecker(throwable.toString())
        }
    }

    @Test
    fun `on detach`() {
        presenter.detachView()

        verify {
            getPushNotifCheckerStatusUseCase.unsubscribe()
        }
    }
}