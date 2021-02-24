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

@ExperimentalCoroutinesApi
class MenuSettingViewModelTest {

    @RelaxedMockK
    lateinit var authorizeAddressAccessUseCase: AuthorizeAccessUseCase

    @RelaxedMockK
    lateinit var authorizeInfoAccessUseCase: AuthorizeAccessUseCase

    @RelaxedMockK
    lateinit var authorizeNotesAccessUseCase: AuthorizeAccessUseCase

    @RelaxedMockK
    lateinit var authorizeShipmentAccessUseCase: AuthorizeAccessUseCase

    @RelaxedMockK
    lateinit var userSession: UserSessionInterface

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private lateinit var viewModel: MenuSettingViewModel

    @Before
    fun setup() {
        MockKAnnotations.init(this)

        viewModel = MenuSettingViewModel(
                authorizeAddressAccessUseCase, authorizeInfoAccessUseCase, authorizeNotesAccessUseCase,
                authorizeShipmentAccessUseCase, userSession, CoroutineTestDispatchersProvider)
    }

    @Test
    fun `check shop setting access if shop owner should update value to success`() {
        every { userSession.isShopOwner } returns true

        viewModel.checkShopSettingAccess()

        assert(viewModel.shopSettingAccessLiveData.value is Success)
    }

    @Test
    fun `check shop setting access if not shop owner should update value to success if response success`() = runBlocking {
        val expectedEligibility = true
        every { userSession.isShopOwner } returns false
        everyCheckAccessRoleShouldSuccess(expectedEligibility, expectedEligibility, expectedEligibility, expectedEligibility)

        viewModel.checkShopSettingAccess()

        Assert.assertEquals(viewModel.shopSettingAccessLiveData.value, Success(
                MenuSettingAccess(expectedEligibility, expectedEligibility, expectedEligibility, expectedEligibility)
        ))
    }

    @Test
    fun `check shop setting access if not shop owner should update value to fail if response failed`() = runBlocking {
        every { userSession.isShopOwner } returns false
        everyCheckAccessRoleShouldFail()

        viewModel.checkShopSettingAccess()

        assert(viewModel.shopSettingAccessLiveData.value is Fail)
    }


    private fun everyCheckAccessRoleShouldSuccess(addressRole: Boolean,
                                                  infoRole: Boolean,
                                                  notesRole: Boolean,
                                                  shipmentRole: Boolean) {
        coEvery { authorizeAddressAccessUseCase.execute(any()) } returns addressRole
        coEvery { authorizeInfoAccessUseCase.execute(any()) } returns infoRole
        coEvery { authorizeNotesAccessUseCase.execute(any()) } returns notesRole
        coEvery { authorizeShipmentAccessUseCase.execute(any()) } returns shipmentRole
    }

    private fun everyCheckAccessRoleShouldFail() {
        coEvery { authorizeAddressAccessUseCase.execute(any()) } throws ResponseErrorException()
        coEvery { authorizeInfoAccessUseCase.execute(any()) } throws ResponseErrorException()
        coEvery { authorizeNotesAccessUseCase.execute(any()) } throws ResponseErrorException()
        coEvery { authorizeShipmentAccessUseCase.execute(any()) } throws ResponseErrorException()
    }

}