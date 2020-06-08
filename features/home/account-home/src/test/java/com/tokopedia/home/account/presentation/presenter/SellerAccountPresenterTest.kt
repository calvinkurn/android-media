package com.tokopedia.home.account.presentation.presenter

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.home.account.domain.GetSellerAccountUseCase
import com.tokopedia.home.account.presentation.SellerAccount
import com.tokopedia.home.account.presentation.subscriber.GetSellerAccountSubscriber
import com.tokopedia.home.account.presentation.viewmodel.base.SellerViewModel
import com.tokopedia.user.session.UserSession
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.ArgumentMatchers.anyString

@ExperimentalCoroutinesApi
class SellerAccountPresenterTest {

    @RelaxedMockK
    lateinit var getSellerAccountUseCase: GetSellerAccountUseCase

    @RelaxedMockK
    lateinit var userSession: UserSession

    @RelaxedMockK
    lateinit var view: SellerAccount.View

    private val presenter by lazy {
        SellerAccountPresenter(getSellerAccountUseCase, userSession)
    }

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        presenter.attachView(view)
    }

    @Test
    fun `get seller data`() {
        val model = SellerViewModel()

        every {
            userSession.shopId
        } returns "123"

        every {
            getSellerAccountUseCase.execute(any(), any())
        } answers {
            secondArg<GetSellerAccountSubscriber>().onStart()
            secondArg<GetSellerAccountSubscriber>().onCompleted()
            secondArg<GetSellerAccountSubscriber>().onNext(model)
        }

        presenter.getSellerData(anyString(), anyString(), anyString(), anyString())

        verify {
            getSellerAccountUseCase.execute(any(), any())
        }

        verify {
            view.loadSellerData(model)
            view.hideLoading()
        }
    }

    @Test
    fun `in detach view`() {
        presenter.detachView()

        verify {
            getSellerAccountUseCase.unsubscribe()
        }
    }
}