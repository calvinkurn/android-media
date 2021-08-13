package com.tokopedia.product.manage.feature.list.view.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.product.manage.common.feature.draft.domain.usecase.ClearAllDraftProductsUseCase
import com.tokopedia.product.manage.common.feature.draft.domain.usecase.GetAllDraftProductsCountUseCase
import com.tokopedia.product.manage.common.feature.list.data.model.ProductManageAccessResponse
import com.tokopedia.product.manage.common.feature.list.domain.usecase.GetProductManageAccessUseCase
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.usecase.RequestParams
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.mockk.verifyAll
import org.junit.Before
import org.junit.Rule

abstract class ProductDraftListCountViewModelTestFixture {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private lateinit var getAllDraftProductsCountUseCase: GetAllDraftProductsCountUseCase

    private lateinit var clearAllDraftProductsCountUseCase: ClearAllDraftProductsUseCase

    private lateinit var getProductManageAccessUseCase: GetProductManageAccessUseCase

    private lateinit var userSessionInterface: UserSessionInterface

    protected lateinit var viewModel: ProductDraftListCountViewModel

    @Before
    fun setUp() {
        getAllDraftProductsCountUseCase = mockk(relaxed = true)
        clearAllDraftProductsCountUseCase = mockk(relaxed = true)
        getProductManageAccessUseCase = mockk(relaxed = true)
        userSessionInterface = mockk(relaxed = true)

        viewModel = ProductDraftListCountViewModel(
                getAllDraftProductsCountUseCase,
                clearAllDraftProductsCountUseCase,
                getProductManageAccessUseCase,
                userSessionInterface,
                CoroutineTestDispatchersProvider
        )

        onGetIsShopOwner_thenReturn(isShopOwner = true)
    }

    protected fun onGetAllDraftCount_thenReturn(draftCount: Long) {
        every { getAllDraftProductsCountUseCase.getData(any()) } returns draftCount
    }

    protected fun onGetAllDraftCount_thenReturn(error: Throwable) {
        every { getAllDraftProductsCountUseCase.getData(any()) } throws error
    }

    protected fun onGetIsShopOwner_thenReturn(isShopOwner: Boolean) {
        every { userSessionInterface.isShopOwner } returns isShopOwner
    }

    protected fun onGetProductManageAccess_thenReturn(response: ProductManageAccessResponse.Response) {
        coEvery { getProductManageAccessUseCase.execute(any()) } returns response
    }

    protected fun verifyGetAllDraftCountCalled() {
        verify { getAllDraftProductsCountUseCase.getData(RequestParams.EMPTY) }
    }

    protected fun verifyClearAllDraftCalled() {
        verify { clearAllDraftProductsCountUseCase.getData(RequestParams.EMPTY) }
    }

    protected fun verifyUnsubscribeUseCaseCalled() {
        verifyAll {
            getAllDraftProductsCountUseCase.unsubscribe()
            clearAllDraftProductsCountUseCase.unsubscribe()
        }
    }
}