package com.tokopedia.product.manage.feature.list.view.viewmodel

import com.tokopedia.product.manage.common.feature.list.data.model.ProductManageAccessResponse
import com.tokopedia.unit.test.ext.verifyErrorEquals
import com.tokopedia.unit.test.ext.verifySuccessEquals
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import org.junit.Test

class ProductDraftListCountViewModelTest: ProductDraftListCountViewModelTestFixture() {

    @Test
    fun `given shop admin does NOT have add product access when getAllDraftCount should return 0`() {
        val isShopOwner = false // isShopOwner false = shop admin
        val accessData = ProductManageAccessResponse.Data(listOf()) // empty access
        val accessResponse = ProductManageAccessResponse.Response(data = accessData)

        onGetIsShopOwner_thenReturn(isShopOwner)
        onGetProductManageAccess_thenReturn(accessResponse)

        viewModel.getAllDraftCount()

        val draftCount = 0L
        val expectedResult = Success(draftCount)

        viewModel.getAllDraftCountResult
            .verifySuccessEquals(expectedResult)
    }

    @Test
    fun `when getAllDraftCount success should set result success`() {
        val draftCount = 5L

        onGetAllDraftCount_thenReturn(draftCount)

        viewModel.getAllDraftCount()

        val expectedResult = Success(draftCount)

        viewModel.getAllDraftCountResult
            .verifySuccessEquals(expectedResult)
    }

    @Test
    fun `when getAllDraftCount error should set result fail`() {
        val error = NullPointerException()

        onGetAllDraftCount_thenReturn(error)

        viewModel.getAllDraftCount()

        val expectedResult = Fail(error)

        viewModel.getAllDraftCountResult
            .verifyErrorEquals(expectedResult)
    }

    @Test
    fun `when getAllDraftCount should call get all draft count use case`() {
        viewModel.getAllDraftCount()

        verifyGetAllDraftCountCalled()
    }

    @Test
    fun `when clearAllDraft should call clear draft use case`() {
        viewModel.clearAllDraft()

        verifyClearAllDraftCalled()
    }

    @Test
    fun `when detachView should unsubscribe all use cases`() {
        viewModel.detachView()

        verifyUnsubscribeUseCaseCalled()
    }
}