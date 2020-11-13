package com.tokopedia.report.view.viewmodel

import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.data.model.GraphqlError
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.report.coroutine.TestCoroutineDispatchers
import com.tokopedia.report.data.model.ProductReportReason
import com.tokopedia.report.verifyErrorEquals
import com.tokopedia.report.verifySuccessEquals
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import org.junit.Assert
import org.junit.Test
import org.mockito.ArgumentMatchers.anyString
import java.lang.reflect.Type

class ProductReportViewModelTest : ProductReportViewModelTestFixture() {

    @Test
    fun `when getReportReason success should return expected result`() {
        val expectedResponse = GraphqlResponse(
                mapOf(ProductReportReason.Response::class.java to ProductReportReason.Response()) as MutableMap<Type, Any>,
                HashMap<Type, List<GraphqlError>>(),
                false
        )
        viewModel = ProductReportViewModel(graphqlRepository, anyString(), TestCoroutineDispatchers)
        onGetReportReasonSuccess_thenReturn(expectedResponse)
        verifyUseCaseCalled()
        verifyGetReportReasonSuccess(Success(expectedResponse.getSuccessData<ProductReportReason.Response>().data))
    }

    @Test
    fun `when getReportReason error should return expected throwable`() {
        val errorGql = GraphqlError()
        errorGql.message = "Error getReportReason"
        viewModel = ProductReportViewModel(graphqlRepository, anyString(), TestCoroutineDispatchers)
        onGetReportReasonError_thenReturn(errorGql)
        verifyUseCaseCalled()
        verifyGetReportReasonFails(Fail(Throwable(errorGql.message)))
    }

    private fun onGetReportReasonSuccess_thenReturn(graphqlResponse: GraphqlResponse) {
        coEvery { graphqlRepository.getReseponse(any(), any()) } coAnswers { graphqlResponse }
    }

    private fun onGetReportReasonError_thenReturn(errorGql: GraphqlError) {
        val errors = HashMap<Type, List<GraphqlError>>()
        errors[ ProductReportReason.Response::class.java] = listOf(errorGql)
        coEvery { graphqlRepository.getReseponse(any(), any()) } coAnswers { GraphqlResponse(HashMap<Type, Any?>(), errors, false) }
    }

    private fun verifyUseCaseCalled() {
        coVerify { graphqlRepository.getReseponse(any(), any()) }
    }

    private fun verifyGetReportReasonSuccess(success: Success<List<ProductReportReason>>) {
        viewModel.reasonResponse.verifySuccessEquals(success)
    }

    private fun verifyGetReportReasonFails(fail: Fail) {
        viewModel.reasonResponse.verifyErrorEquals(fail)
    }
}