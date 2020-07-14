package com.tokopedia.home.account.presentation.presenter

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.home.account.data.model.AccountSettingConfig
import com.tokopedia.home.account.domain.GetAccountSettingConfigUseCase
import com.tokopedia.home.account.presentation.AccountSetting
import com.tokopedia.home.account.presentation.subscriber.GetAccountSettingConfigSubscriber
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class AccountSettingPresenterTest {

    @RelaxedMockK
    lateinit var getAccountSettingConfigUseCase: GetAccountSettingConfigUseCase

    @RelaxedMockK
    lateinit var graphqlResponse: GraphqlResponse

    @RelaxedMockK
    lateinit var view: AccountSetting.View

    private val accountSettPresenter by lazy {
        AccountSettingPresenter(getAccountSettingConfigUseCase)
    }

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        accountSettPresenter.attachView(view)
    }

    @Test
    fun `get menu account setting`() {
        val accountSettingConfig = AccountSettingConfig()

        every {
            graphqlResponse.getData(AccountSettingConfig::class.java) as AccountSettingConfig
        } returns accountSettingConfig

        every {
            getAccountSettingConfigUseCase.execute(any(), any())
        } answers {
            secondArg<GetAccountSettingConfigSubscriber>().onStart()
            secondArg<GetAccountSettingConfigSubscriber>().onCompleted()
            secondArg<GetAccountSettingConfigSubscriber>().onError(Throwable())
            secondArg<GetAccountSettingConfigSubscriber>().onNext(graphqlResponse)
        }

        accountSettPresenter.getMenuAccountSetting()

        verify {
            getAccountSettingConfigUseCase.execute(any(), any())
        }
    }

    @Test
    fun `on detach view`() {
        accountSettPresenter.detachView()

        verify {
            getAccountSettingConfigUseCase.unsubscribe()
        }
    }
}