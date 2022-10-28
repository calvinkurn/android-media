package com.tokopedia.product.manage.feature.list.view.viewmodel

import com.tokopedia.product.manage.common.feature.list.data.model.ProductManageAccessResponse
import com.tokopedia.product.manage.feature.list.data.repository.MockedDraftRepository
import com.tokopedia.product.manage.feature.list.data.repository.MockedDraftRepositoryException
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.unit.test.ext.verifyErrorEquals
import com.tokopedia.unit.test.ext.verifySuccessEquals
import com.tokopedia.unit.test.ext.verifyValueEquals
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.runBlocking
import org.junit.Test

class ProductDraftListCountViewModelTest: ProductDraftListCountViewModelTestFixture() {

    @Test
    fun `given shop admin does NOT have add product access when getAllDraftCount should return null`() = runBlocking {
        initUseCases(draftRepository = MockedDraftRepository())

        val isShopOwner = false // isShopOwner false = shop admin
        val accessData = ProductManageAccessResponse.Data(listOf()) // empty access
        val accessResponse = ProductManageAccessResponse.Response(data = accessData)

        onGetIsShopOwner_thenReturn(isShopOwner)
        onGetProductManageAccess_thenReturn(accessResponse)

        // the getAllDraftProductsCountFlowUseCase will be called after viewModel is initiated
        val viewModel = ProductDraftListCountViewModel(
            getAllDraftProductsCountFlowUseCase,
            clearAllDraftProductsCountUseCase,
            getProductManageAccessUseCase,
            userSessionInterface,
            CoroutineTestDispatchersProvider
        )

        val expectedResult = null
        viewModel.getAllDraftCountResult
            .verifyValueEquals(expectedResult)
    }

    @Test
    fun `when getAllDraftCount success should set result success`() = runBlocking {
        initUseCases(draftRepository = MockedDraftRepository())

        val isShopOwner = true // isShopOwner false = shop admin
        val accessData = ProductManageAccessResponse.Data(listOf()) // empty access
        val accessResponse = ProductManageAccessResponse.Response(data = accessData)

        onGetIsShopOwner_thenReturn(isShopOwner)
        onGetProductManageAccess_thenReturn(accessResponse)

        // the getAllDraftProductsCountFlowUseCase will be called after viewModel is initiated
        viewModel = ProductDraftListCountViewModel(
            getAllDraftProductsCountFlowUseCase,
            clearAllDraftProductsCountUseCase,
            getProductManageAccessUseCase,
            userSessionInterface,
            CoroutineTestDispatchersProvider
        )

        val draftCount = 4L
        val expectedResult = Success(draftCount)
        viewModel.getAllDraftCountResult
            .verifySuccessEquals(expectedResult)
    }

    @Test
    fun `when getAllDraftCount error should set result fail`() {
        initUseCases(draftRepository = MockedDraftRepositoryException())

        val isShopOwner = true
        onGetIsShopOwner_thenReturn(isShopOwner)

        // the getAllDraftProductsCountFlowUseCase will be called after viewModel is initiated
        viewModel = ProductDraftListCountViewModel(
            getAllDraftProductsCountFlowUseCase,
            clearAllDraftProductsCountUseCase,
            getProductManageAccessUseCase,
            userSessionInterface,
            CoroutineTestDispatchersProvider
        )

        val error = NullPointerException()
        val expectedResult = Fail(error)
        viewModel.getAllDraftCountResult
            .verifyErrorEquals(expectedResult)
    }

    @Test
    fun `when clearAllDraft should call clear draft use case`() {
        initUseCases(draftRepository = MockedDraftRepository())

        val isShopOwner = true
        onGetIsShopOwner_thenReturn(isShopOwner)

        // the getAllDraftProductsCountFlowUseCase will be called after viewModel is initiated
        viewModel = ProductDraftListCountViewModel(
            getAllDraftProductsCountFlowUseCase,
            clearAllDraftProductsCountUseCase,
            getProductManageAccessUseCase,
            userSessionInterface,
            CoroutineTestDispatchersProvider
        )

        viewModel.clearAllDraft()

        verifyClearAllDraftCalled()
    }

    @Test
    fun `when clearAllDraft error should do nothing`() {
        initUseCases(draftRepository = MockedDraftRepository())

        val isShopOwner = true
        onGetIsShopOwner_thenReturn(isShopOwner)

        // the getAllDraftProductsCountFlowUseCase will be called after viewModel is initiated
        viewModel = ProductDraftListCountViewModel(
            getAllDraftProductsCountFlowUseCase,
            clearAllDraftProductsCountUseCase,
            getProductManageAccessUseCase,
            userSessionInterface,
            CoroutineTestDispatchersProvider
        )

        onClearAllDraft_thenThrow(NullPointerException())

        viewModel.clearAllDraft()
    }

}