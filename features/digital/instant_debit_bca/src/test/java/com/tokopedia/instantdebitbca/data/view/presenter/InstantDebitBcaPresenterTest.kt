package com.tokopedia.instantdebitbca.data.view.presenter

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.instantdebitbca.data.domain.GetAccessTokenBcaUseCase
import com.tokopedia.instantdebitbca.data.domain.NotifyDebitRegisterBcaUseCase
import com.tokopedia.instantdebitbca.data.domain.NotifyDebitRegisterEditLimit
import com.tokopedia.instantdebitbca.data.view.interfaces.InstantDebitBcaContract
import com.tokopedia.instantdebitbca.data.view.model.NotifyDebitRegisterBca
import com.tokopedia.instantdebitbca.data.view.model.TokenInstantDebitBca
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.usecase.RequestParams
import io.mockk.*
import io.mockk.impl.annotations.MockK
import io.mockk.impl.annotations.RelaxedMockK
import org.junit.After
import org.junit.Before

import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import rx.Observable
import rx.Scheduler
import rx.android.schedulers.AndroidSchedulers
import rx.observers.TestSubscriber
import rx.schedulers.Schedulers
import rx.schedulers.TestScheduler

class InstantDebitBcaPresenterTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()
    val getAccessTokenBcaUseCase = mockk<GetAccessTokenBcaUseCase>(relaxed = true)
    val notifyDebitRegisterBcaUseCase = mockk<NotifyDebitRegisterBcaUseCase>(relaxed = true)
    val notifyDebitRegisterEditLimit = mockk<NotifyDebitRegisterEditLimit>(relaxed = true)
    private lateinit var presenter: InstantDebitBcaPresenter

    @RelaxedMockK
    private lateinit var view: InstantDebitBcaContract.View

    @Before
    fun before() {
        MockKAnnotations.init(this)
        presenter = InstantDebitBcaPresenter(
                getAccessTokenBcaUseCase,
                notifyDebitRegisterBcaUseCase,
                notifyDebitRegisterEditLimit,
        )
        presenter.attachView(view)
    }

    @After
    fun cleanup() {
        presenter.detachView()
    }

    private fun provideSchedulers() {
        mockkStatic(Schedulers::class)
        mockkStatic(AndroidSchedulers::class)

        every { Schedulers.io() } returns Schedulers.immediate()
        every { AndroidSchedulers.mainThread() } returns Schedulers.immediate()
    }

    @Test
    fun `getAccessTokenBca success`() {
        val data= mockk<TokenInstantDebitBca>()
        every { data.accessToken } returns "access token"
        provideSchedulers()

        every {
            getAccessTokenBcaUseCase.createObservable(any())
        } returns  Observable.just(data)

        presenter.getAccessTokenBca()

        verify(exactly = 1) {
            view.openWidgetBca("access token")
        }
    }

    @Test
    fun `getAccessTokenBca error`() {
        val message  = "Error"
        val requestParams = RequestParams.EMPTY
        every { getAccessTokenBcaUseCase.createRequestParam()
        } returns requestParams

        provideSchedulers()

        every { getAccessTokenBcaUseCase.createObservable(requestParams)
        } returns Observable.error(MessageErrorException(message))

        presenter.getAccessTokenBca()
        verify { view.showErrorMessage(any()) }
        confirmVerified(view)
    }


    @Test
    fun `notifyDebitRegisterBca success`() {
        val data= mockk<NotifyDebitRegisterBca>()
        val requestParams = RequestParams.EMPTY

        provideSchedulers()

        every {
            notifyDebitRegisterBcaUseCase.createObservable(any())
        } returns  Observable.just(data)
        every { notifyDebitRegisterBcaUseCase.createRequestParam(any(), any())
        } returns requestParams

        presenter.notifyDebitRegisterBca("", "")

        verify(exactly = 1) {
            view.redirectPageAfterRegisterBca()
        }
    }

    @Test
    fun `notifyDebitRegisterBca onError`() {
        val message  = "Error"
        val requestParams = RequestParams.EMPTY

        provideSchedulers()

        every { notifyDebitRegisterBcaUseCase.createObservable(requestParams)
        } returns Observable.error(MessageErrorException(message))
        every { notifyDebitRegisterBcaUseCase.createRequestParam(any(), any())
        } returns requestParams

        presenter.notifyDebitRegisterBca("", "")

        verify { view.redirectPageAfterRegisterBca() }
        confirmVerified(view)
    }

    @Test
    fun `notifyDebitRegisterEditLimit success`() {
        val data= mockk<NotifyDebitRegisterBca>()
        val requestParams = RequestParams.EMPTY

        provideSchedulers()

        every {
            notifyDebitRegisterEditLimit.createObservable(any())
        } returns  Observable.just(data)
        every { notifyDebitRegisterEditLimit.createRequestParam(any(), any())
        } returns requestParams

        presenter.notifyDebitRegisterEditLimit("", "")

        verify(exactly = 1) {
            view.redirectPageAfterRegisterBca()
        }
    }

    @Test
    fun `notifyDebitRegisterEditLimit onError`() {
        val message  = "Error"
        val requestParams = RequestParams.EMPTY

        provideSchedulers()

        every { notifyDebitRegisterEditLimit.createObservable(requestParams)
        } returns Observable.error(MessageErrorException(message))
        every { notifyDebitRegisterEditLimit.createRequestParam(any(), any())
        } returns requestParams

        presenter.notifyDebitRegisterEditLimit("", "")

        verify { view.redirectPageAfterRegisterBca() }
        confirmVerified(view)
    }
}