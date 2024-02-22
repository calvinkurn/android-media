package com.tokopedia.home_account.ui.accountsettings

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.tokopedia.home_account.account_settings.data.model.AccountSettingResponse
import com.tokopedia.home_account.domain.usecase.GetAccountSettingUseCase
import com.tokopedia.unit.test.rule.UnconfinedTestRule
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class AccountSettingViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @OptIn(ExperimentalCoroutinesApi::class)
    @get:Rule
    val coroutineTestRule = UnconfinedTestRule()

    lateinit var sut: AccountSettingViewModel
    private val getAccountSetting: GetAccountSettingUseCase = mockk()
    private val stateObserver: Observer<AccountSettingUiModel> = mockk(relaxed = true)
    private val errorObserver: Observer<Throwable> = mockk(relaxed = true)

    @Before
    fun setup() {
        sut = AccountSettingViewModel(getAccountSetting)
        sut.state.observeForever(stateObserver)
        sut.errorToast.observeForever(errorObserver)
    }

    @Test
    fun `given success response state should display content`() {
        val success = AccountSettingResponse.Config(dokumenDataDiri = true)
        coEvery { getAccountSetting(Unit) } returns success
        sut.fetch()

        verify {
            stateObserver.onChanged(AccountSettingUiModel.Loading)
            stateObserver.onChanged(AccountSettingUiModel.Display(success))
        }
    }

    @Test
    fun `given error usecase state should display default content`() {
        coEvery { getAccountSetting(Unit) } throws Exception()

        sut.fetch()

        verify {
            stateObserver.onChanged(AccountSettingUiModel.Display())
            errorObserver.onChanged(any())
        }
    }
}
