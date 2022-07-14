package com.tokopedia.shopadmin.feature.invitationaccepted

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.shopadmin.feature.invitationaccepted.domain.usecase.GetArticleDetailUseCase
import com.tokopedia.shopadmin.feature.invitationaccepted.domain.usecase.GetPermissionListUseCase
import com.tokopedia.shopadmin.feature.invitationaccepted.presentation.model.AdminPermissionUiModel
import com.tokopedia.shopadmin.feature.invitationaccepted.presentation.model.ArticleDetailUiModel
import com.tokopedia.shopadmin.feature.invitationaccepted.presentation.viewmodel.InvitationAcceptedViewModel
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import dagger.Lazy
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.unmockkAll
import org.junit.After
import org.junit.Before
import org.junit.Rule

abstract class InvitationAcceptedViewModelTestFixture {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @RelaxedMockK
    protected lateinit var getPermissionListUseCase: Lazy<GetPermissionListUseCase>

    @RelaxedMockK
    protected lateinit var getArticleDetailUseCase: Lazy<GetArticleDetailUseCase>

    private lateinit var dispatchers: CoroutineDispatchers

    protected lateinit var viewModel: InvitationAcceptedViewModel

    protected val shopId = "1234"

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        dispatchers = CoroutineTestDispatchersProvider
        viewModel = InvitationAcceptedViewModel(
            getPermissionListUseCase,
            getArticleDetailUseCase,
            dispatchers
        )
    }

    @After
    fun finish() {
        unmockkAll()
    }

    protected fun onGetPermissionListUseCase_thenReturn(
        shopId: String,
        adminPermissionList: List<AdminPermissionUiModel>
    ) {
        coEvery { getPermissionListUseCase.get().execute(shopId) } returns adminPermissionList
    }

    protected fun onGetPermissionListUseCaseError_thenReturn(shopId: String, exception: Throwable) {
        coEvery { getPermissionListUseCase.get().execute(shopId) } throws exception
    }

    protected fun verifyGetPermissionListUseCaseCalled(shopId: String) {
        coVerify { getPermissionListUseCase.get().execute(shopId) }
    }

    protected fun onGetArticleDetailUseCase_thenReturn(
        articleDetailUiModel: ArticleDetailUiModel
    ) {
        coEvery { getArticleDetailUseCase.get().execute() } returns articleDetailUiModel
    }

    protected fun onGetArticleDetailUseCaseError_thenReturn(exception: Throwable) {
        coEvery { getArticleDetailUseCase.get().execute() } throws exception
    }

    protected fun verifyGetArticleDetailUseCaseCalled() {
        coVerify { getArticleDetailUseCase.get().execute() }
    }

}