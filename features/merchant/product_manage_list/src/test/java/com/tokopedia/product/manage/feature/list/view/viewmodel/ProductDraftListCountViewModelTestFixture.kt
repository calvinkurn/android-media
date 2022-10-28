package com.tokopedia.product.manage.feature.list.view.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.product.manage.common.feature.draft.data.db.repository.AddEditProductDraftRepository
import com.tokopedia.product.manage.common.feature.draft.domain.usecase.ClearAllDraftProductsUseCase
import com.tokopedia.product.manage.common.feature.draft.domain.usecase.GetAllDraftProductsCountFlowUseCase
import com.tokopedia.product.manage.common.feature.list.data.model.ProductManageAccessResponse
import com.tokopedia.product.manage.common.feature.list.domain.usecase.GetProductManageAccessUseCase
import com.tokopedia.usecase.RequestParams
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import org.junit.Rule
import org.mockito.ArgumentMatchers.anyString

abstract class ProductDraftListCountViewModelTestFixture {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    protected lateinit var getAllDraftProductsCountFlowUseCase: GetAllDraftProductsCountFlowUseCase

    protected lateinit var clearAllDraftProductsCountUseCase: ClearAllDraftProductsUseCase

    protected lateinit var getProductManageAccessUseCase: GetProductManageAccessUseCase

    protected lateinit var userSessionInterface: UserSessionInterface

    protected lateinit var viewModel: ProductDraftListCountViewModel

    protected fun initUseCases(draftRepository: AddEditProductDraftRepository) {
        getAllDraftProductsCountFlowUseCase = GetAllDraftProductsCountFlowUseCase(
            draftRepository = draftRepository
        )
        clearAllDraftProductsCountUseCase = mockk(relaxed = true)
        getProductManageAccessUseCase = mockk(relaxed = true)
        userSessionInterface = mockk(relaxed = true)
    }

    protected fun onGetIsShopOwner_thenReturn(isShopOwner: Boolean) {
        every { userSessionInterface.isShopOwner } returns isShopOwner
    }

    protected fun onGetProductManageAccess_thenReturn(response: ProductManageAccessResponse.Response) {
        coEvery { getProductManageAccessUseCase.execute(anyString()) } returns response
    }

    protected fun onClearAllDraft_thenThrow(ex: Exception) {
        coEvery { clearAllDraftProductsCountUseCase.getData(any()) } throws ex
    }

    protected fun verifyClearAllDraftCalled() {
        coVerify { clearAllDraftProductsCountUseCase.getData(RequestParams.EMPTY) }
    }

}