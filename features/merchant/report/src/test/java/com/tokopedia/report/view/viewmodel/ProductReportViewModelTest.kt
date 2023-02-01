package com.tokopedia.report.view.viewmodel

import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.data.model.GraphqlError
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.report.data.model.ProductReportReason
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import io.mockk.coEvery
import io.mockk.coVerify
import junit.framework.Assert
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Test
import java.lang.reflect.Type

class ProductReportViewModelTest : ProductReportViewModelTestFixture() {

    @Test
    fun `when getReportReason success should return expected result`() = runBlockingTest {
        val expectedResponse = `get response success`()
        val expected = `get success`(expectedResponse)
        val test = viewModel.uiState.first()
        Assert.assertEquals(expected, test.data)
    }

    private fun `get response success`() = GraphqlResponse(
        mapOf(ProductReportReason.Response::class.java to ProductReportReason.Response()) as MutableMap<Type, Any>,
        HashMap<Type, List<GraphqlError>>(),
        false
    )

    private fun `get success`(response: GraphqlResponse): List<ProductReportReason> {
        val expected = response.getSuccessData<ProductReportReason.Response>().data
        onGetReportReasonSuccess_thenReturn(response)
        viewModel = ProductReportViewModel(graphqlRepository, CoroutineTestDispatchersProvider)
        verifyUseCaseCalled()
        return expected
    }

    @Test
    fun `when getReportReason error should return expected throwable`() = runBlockingTest {
        val errorGql = GraphqlError()
        val errorMessage = "Error getReportReason"
        errorGql.message = errorMessage
        onGetReportReasonError_thenReturn(errorGql)
        viewModel = ProductReportViewModel(graphqlRepository, CoroutineTestDispatchersProvider)
        verifyUseCaseCalled()
        val test = viewModel.uiState.first()
        Assert.assertEquals(errorMessage, test.error.orEmpty())
    }

    @Test
    fun `event on item clicked`() {

    }

    @Test
    fun `event on back pressed`() {

    }

    @Test
    fun `event on footer click`() {

    }

    private fun onGetReportReasonSuccess_thenReturn(graphqlResponse: GraphqlResponse) {
        coEvery { graphqlRepository.response(any(), any()) } coAnswers { graphqlResponse }
    }

    private fun onGetReportReasonError_thenReturn(errorGql: GraphqlError) {
        val errors = HashMap<Type, List<GraphqlError>>()
        errors[ProductReportReason.Response::class.java] = listOf(errorGql)
        coEvery {
            graphqlRepository.response(
                any(),
                any()
            )
        } coAnswers { GraphqlResponse(HashMap<Type, Any?>(), errors, false) }
    }

    private fun verifyUseCaseCalled() {
        coVerify { graphqlRepository.response(any(), any()) }
    }
}
