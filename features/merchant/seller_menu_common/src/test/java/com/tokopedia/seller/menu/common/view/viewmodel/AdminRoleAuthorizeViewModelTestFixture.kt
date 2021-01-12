package com.tokopedia.seller.menu.common.view.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.tokopedia.seller.menu.common.domain.usecase.AdminPermissionUseCase
import com.tokopedia.seller.menu.common.view.mapper.AdminPermissionMapper
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.RelaxedMockK
import org.junit.After
import org.junit.Before
import org.junit.Rule

open class AdminRoleAuthorizeViewModelTestFixture {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @RelaxedMockK
    lateinit var adminPermissionUseCase: AdminPermissionUseCase

    @RelaxedMockK
    lateinit var userSession: UserSessionInterface

    @RelaxedMockK
    lateinit var mapper: AdminPermissionMapper

    @RelaxedMockK
    lateinit var isRoleAuthorizedObserver: Observer<in Result<Boolean>>

    protected lateinit var viewModel: AdminRoleAuthorizeViewModel

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        viewModel = AdminRoleAuthorizeViewModel(
                adminPermissionUseCase,
                userSession,
                mapper,
                CoroutineTestDispatchersProvider
        )
        viewModel.isRoleAuthorizedLiveData.observeForever(isRoleAuthorizedObserver)
    }

    @After
    fun cleanup() {
        viewModel.isRoleAuthorizedLiveData.removeObserver(isRoleAuthorizedObserver)
    }

    protected fun onGetUserSessionIsShopOwner_thenReturn(isShopOwner: Boolean) {
        coEvery {
            userSession.isShopOwner
        } returns isShopOwner
    }

    protected fun onExecuteAdminPermissionUseCaseSuccess_thenReturn(result: Boolean) {
        coEvery {
            adminPermissionUseCase.execute()
        } returns result
    }

    protected fun onExecuteAdminPermissionUseCaseFail_thenThrow(throwable: Throwable) {
        coEvery {
            adminPermissionUseCase.execute()
        } throws throwable
    }

}