package com.tokopedia.report.view.viewmodel

import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.report.coroutine.TestCoroutineDispatchers
import com.tokopedia.report.data.model.ProductReportReason
import com.tokopedia.report.verifyErrorEquals
import com.tokopedia.report.verifySuccessEquals
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import io.mockk.coEvery
import io.mockk.coVerify
import org.junit.Test
import org.mockito.ArgumentMatchers.anyString
import java.lang.reflect.Type

class ProductReportViewModelTest : ProductReportViewModelTestFixture() {

    @Test
    fun `when getReportReason success should return expected result`() {
        val expectedResponse = GraphqlResponse(
                mapOf<Type, Any>(
                        Pair(ProductReportReason.Response::class.java, ProductReportReason.Response())
                ),
                emptyMap(),
                false
        )
        viewModel = ProductReportViewModel(graphqlRepository, anyString(), TestCoroutineDispatchers)
        onGetReportReasonSuccess_thenReturn(expectedResponse)
        verifyUseCaseCalled()
        verifyGetReportReasonSuccess(Success(expectedResponse.getSuccessData<ProductReportReason.Response>().data))
    }

    @Test
    fun `when getReportReason error should return expected throwable`() {
        val expectedError = Throwable()
        viewModel = ProductReportViewModel(graphqlRepository, anyString(), TestCoroutineDispatchers)
        onGetReportReasonError_thenReturn(expectedError)
        verifyUseCaseCalled()
        verifyGetReportReasonFails(Fail(expectedError))
    }

    private fun onGetReportReasonSuccess_thenReturn(response: GraphqlResponse) {
        coEvery { graphqlRepository.getReseponse(any()) } returns response
    }

    private fun onGetReportReasonError_thenReturn(throwable: Throwable) {
        coEvery { graphqlRepository.getReseponse(any()) } throws throwable
    }

    private fun verifyUseCaseCalled() {
        coVerify { graphqlRepository.getReseponse(any()) }
    }

    private fun verifyGetReportReasonSuccess(success: Success<List<ProductReportReason>>) {
        viewModel.reasonResponse.verifySuccessEquals(success)
    }

    private fun verifyGetReportReasonFails(fail: Fail) {
        viewModel.reasonResponse.verifyErrorEquals(fail)
    }
}