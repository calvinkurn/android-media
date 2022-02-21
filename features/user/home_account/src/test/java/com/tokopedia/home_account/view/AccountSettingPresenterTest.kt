package com.tokopedia.home_account.view

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.home_account.account_settings.data.model.AccountSettingConfig
import com.tokopedia.home_account.account_settings.domain.GetAccountSettingConfigUseCase
import com.tokopedia.home_account.account_settings.presentation.AccountSetting
import com.tokopedia.home_account.account_settings.presentation.presenter.AccountSettingPresenter
import com.tokopedia.home_account.account_settings.presentation.subscriber.GetAccountSettingConfigSubscriber
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class AccountSettingPresenterTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val getAccountSettingConfigUseCase =
        mockk<GetAccountSettingConfigUseCase>(relaxed = true)

    private val view = mockk<AccountSetting.View>(relaxed = true)
    private lateinit var presenter: AccountSettingPresenter

    private val mockGraphqlResponse = mockk<GraphqlResponse>(relaxed = true)

    @Before
    fun setUp() {
        presenter = AccountSettingPresenter(getAccountSettingConfigUseCase)
        presenter.attachView(view)
    }

    @Test
    fun `Get Menu Account Settings Success`() {
        val mockConfig = mockk<AccountSettingConfig>(relaxed = true)

        every {
            mockGraphqlResponse.getData<AccountSettingConfig>(any())
        } returns mockConfig

        every {
            getAccountSettingConfigUseCase.execute(any(), any())
        } answers {
            (secondArg() as GetAccountSettingConfigSubscriber).onNext(mockGraphqlResponse)
        }

        presenter.getMenuAccountSetting()

        verify {
            view.onSuccessGetConfig(mockConfig)
        }
    }

    @Test
    fun `Get Menu Account Settings Success but null`() {

        every {
            mockGraphqlResponse.getData<AccountSettingConfig>(any())
        } returns null

        every {
            getAccountSettingConfigUseCase.execute(any(), any())
        } answers {
            (secondArg() as GetAccountSettingConfigSubscriber).onNext(mockGraphqlResponse)
        }

        presenter.getMenuAccountSetting()

        verify {
            view.showError(any())
        }
    }

    @Test
    fun `Get Menu Account Settings onError`() {
        val mockThrowable = mockk<Throwable>(relaxed = true)

        every {
            getAccountSettingConfigUseCase.execute(any(), any())
        } answers {
            (secondArg() as GetAccountSettingConfigSubscriber).onError(mockThrowable)
        }

        presenter.getMenuAccountSetting()

        verify {
            view.showError(mockThrowable, any())
        }
    }

    @Test
    fun `onDetach called`() {
        presenter.detachView()

        verify { getAccountSettingConfigUseCase.unsubscribe() }
    }
}