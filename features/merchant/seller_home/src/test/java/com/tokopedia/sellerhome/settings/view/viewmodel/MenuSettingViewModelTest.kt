package com.tokopedia.sellerhome.settings.view.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.abstraction.common.network.exception.ResponseErrorException
import com.tokopedia.sellerhome.settings.view.uimodel.menusetting.MenuSettingAccess
import com.tokopedia.shop.common.domain.interactor.AuthorizeAccessUseCase
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Provider

@ExperimentalCoroutinesApi
class MenuSettingViewModelTest {

    @RelaxedMockK
    lateinit var authorizeAccessUseCaseProvider: Provider<AuthorizeAccessUseCase>

    @RelaxedMockK
    lateinit var authorizeAccessUseCase: AuthorizeAccessUseCase

    @RelaxedMockK
    lateinit var userSession: UserSessionInterface

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private lateinit var viewModel: MenuSettingViewModel

    @Before
    fun setup() {
        MockKAnnotations.init(this)

        viewModel = MenuSettingViewModel(authorizeAccessUseCaseProvider, userSession, CoroutineTestDispatchersProvider)
    }

    @Test
    fun `check shop setting access if shop owner should update value to success`() {
        every { userSession.isShopOwner } returns true
        everyProviderGetUseCase()

        viewModel.checkShopSettingAccess()

        assert(viewModel.shopSettingAccessLiveData.value is Success)
    }

    @Test
    fun `check shop setting access if not shop owner should update value to success if response success`() = runBlocking {
        val expectedEligibility = false
        everyProviderGetUseCase()
        every { userSession.isShopOwner } returns false
        everyCheckAccessRoleShouldSuccess(expectedEligibility)

        viewModel.checkShopSettingAccess()

        Assert.assertEquals(viewModel.shopSettingAccessLiveData.value, Success(
                MenuSettingAccess(expectedEligibility, expectedEligibility, expectedEligibility, expectedEligibility)
        ))
    }

    @Test
    fun `check shop setting access if not shop owner should update value to fail if response failed`() = runBlocking {
        every { userSession.isShopOwner } returns false
        everyProviderGetUseCase()
        everyCheckAccessRoleShouldFail()

        viewModel.checkShopSettingAccess()

        assert(viewModel.shopSettingAccessLiveData.value is Fail)
    }

    private fun everyProviderGetUseCase() {
        every { authorizeAccessUseCaseProvider.get() } returns authorizeAccessUseCase
    }

    private fun everyCheckAccessRoleShouldSuccess(isEligible: Boolean) {
        coEvery { authorizeAccessUseCase.execute(any()) } returns isEligible
    }

    private fun everyCheckAccessRoleShouldFail() {
        coEvery { authorizeAccessUseCase.execute(any()) } throws ResponseErrorException()
    }

}