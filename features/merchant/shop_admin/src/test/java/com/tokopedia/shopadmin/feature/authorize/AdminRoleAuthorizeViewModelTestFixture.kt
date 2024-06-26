package com.tokopedia.shopadmin.feature.authorize

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.tokopedia.shop.common.domain.interactor.AuthorizeAccessUseCase
import com.tokopedia.shopadmin.common.util.AdminPermissionMapper
import com.tokopedia.shopadmin.feature.authorize.presentation.viewmodel.AdminRoleAuthorizeViewModel
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
    lateinit var authorizeAccessUseCase: AuthorizeAccessUseCase

    @RelaxedMockK
    lateinit var userSession: UserSessionInterface

    @RelaxedMockK
    lateinit var mapper: AdminPermissionMapper

    @RelaxedMockK
    lateinit var isRoleAuthorizedObserver: Observer<in Result<Boolean>>

    @RelaxedMockK
    lateinit var isLoadingLiveDataObserver: Observer<in Boolean>

    protected lateinit var viewModel: AdminRoleAuthorizeViewModel

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        viewModel =
            AdminRoleAuthorizeViewModel(
                authorizeAccessUseCase,
                userSession,
                mapper,
                CoroutineTestDispatchersProvider
            )
        viewModel.isRoleAuthorizedLiveData.observeForever(isRoleAuthorizedObserver)
        viewModel.isLoadingLiveData.observeForever(isLoadingLiveDataObserver)
    }

    @After
    fun cleanup() {
        viewModel.isRoleAuthorizedLiveData.removeObserver(isRoleAuthorizedObserver)
        viewModel.isLoadingLiveData.removeObserver(isLoadingLiveDataObserver)
    }

    protected fun onGetUserSessionIsShopOwner_thenReturn(isShopOwner: Boolean) {
        coEvery {
            userSession.isShopOwner
        } returns isShopOwner
    }

    protected fun onExecuteAuthorizeAccessUseCaseSuccess_thenReturn(result: Boolean) {
        coEvery {
            authorizeAccessUseCase.execute(any())
        } returns result
    }

    protected fun onExecuteAuthorizeAccessUseCaseFail_thenThrow(throwable: Throwable) {
        coEvery {
            authorizeAccessUseCase.execute(any())
        } throws throwable
    }

}
