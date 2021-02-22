package com.tokopedia.seller.menu.common.view.viewmodel

import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.seller.menu.common.constant.AdminFeature
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import org.junit.Test

class AdminRoleAuthorizeViewModelTest: AdminRoleAuthorizeViewModelTestFixture() {

    @Test
    fun `if user is shop owner, role is always authorized`() {
        onGetUserSessionIsShopOwner_thenReturn(true)

        viewModel.checkAccess(AdminFeature.SALDO)

        assert((viewModel.isRoleAuthorizedLiveData.value as? Success)?.data == true)
    }

    @Test
    fun `success get admin permission when user is not shop owner`() {
        val isRoleAuthorized = true
        onGetUserSessionIsShopOwner_thenReturn(false)
        onExecuteAuthorizeAccessUseCaseSuccess_thenReturn(isRoleAuthorized)

        viewModel.checkAccess(AdminFeature.SALDO)

        assert((viewModel.isRoleAuthorizedLiveData.value as? Success)?.data == isRoleAuthorized)
    }

    @Test
    fun `fail get admin permission when user is not shop owner`() {
        val throwable = MessageErrorException("")
        onGetUserSessionIsShopOwner_thenReturn(false)
        onExecuteAuthorizeAccessUseCaseFail_thenThrow(throwable)

        viewModel.checkAccess(AdminFeature.SALDO)

        assert(viewModel.isRoleAuthorizedLiveData.value is Fail)
    }

}