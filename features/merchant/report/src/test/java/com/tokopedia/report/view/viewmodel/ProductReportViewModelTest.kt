package com.tokopedia.report.view.viewmodel

import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.data.model.GraphqlError
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
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
                mapOf(ProductReportReason.Response::class.java to ProductReportReason.Response()) as MutableMap<Type, Any>,
                HashMap<Type, List<GraphqlError>>(),
                false
        )
        onGetReportReasonSuccess_thenReturn(expectedResponse)
        viewModel = ProductReportViewModel(graphqlRepository, CoroutineTestDispatchersProvider)
        verifyUseCaseCalled()
        verifyGetReportReasonSuccess(Success(expectedResponse.getSuccessData<ProductReportReason.Response>().data))
    }

    @Test
    fun `when getReportReason error should return expected throwable`() {
        val errorGql = GraphqlError()
        errorGql.message = "Error getReportReason"
        onGetReportReasonError_thenReturn(errorGql)
        viewModel = ProductReportViewModel(graphqlRepository, CoroutineTestDispatchersProvider)
        verifyUseCaseCalled()
        verifyGetReportReasonFails(Fail(MessageErrorException(errorGql.message)))
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