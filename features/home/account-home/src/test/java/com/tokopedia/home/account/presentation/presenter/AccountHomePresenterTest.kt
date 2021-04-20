package com.tokopedia.home.account.presentation.presenter

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.home.account.analytics.AccountAnalytics
import com.tokopedia.home.account.analytics.data.model.UserAttributeData
import com.tokopedia.home.account.analytics.domain.GetUserAttributesUseCase
import com.tokopedia.home.account.presentation.AccountHome
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import rx.Subscriber

@ExperimentalCoroutinesApi
class AccountHomePresenterTest {

    @RelaxedMockK
    lateinit var getUserAttributesUseCase: GetUserAttributesUseCase

    @RelaxedMockK
    lateinit var accountAnalytics: AccountAnalytics

    @RelaxedMockK
    lateinit var view: AccountHome.View

    private val presenter by lazy {
        AccountHomePresenter(getUserAttributesUseCase, accountAnalytics)
    }

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        presenter.attachView(view)
    }

    /**
     *
     * */
    @Test
    fun `send user attribute tracker`() {
        val userAttributeData = UserAttributeData()

        every {
            getUserAttributesUseCase.execute(any())
        } answers {
            firstArg<Subscriber<UserAttributeData>>().onCompleted()
            firstArg<Subscriber<UserAttributeData>>().onError(Throwable())
            firstArg<Subscriber<UserAttributeData>>().onNext(userAttributeData)
        }

        presenter.sendUserAttributeTracker()

        verify {
            getUserAttributesUseCase.setSaldoQuery(any())
            getUserAttributesUseCase.execute(any())

            accountAnalytics.setUserAttributes(any())
        }
    }

    @Test
    fun `detach view`() {
        presenter.detachView()

        verify {
            getUserAttributesUseCase.unsubscribe()
        }
    }
}