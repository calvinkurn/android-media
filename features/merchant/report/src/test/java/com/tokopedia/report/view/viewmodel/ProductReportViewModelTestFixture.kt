package com.tokopedia.report.view.viewmodel

import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlError
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.report.data.model.ProductReportReason
import com.tokopedia.report.view.fragment.models.ProductReportUiEvent
import com.tokopedia.report.view.fragment.models.ProductReportUiState
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.unit.test.rule.CoroutineTestRule
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import java.lang.reflect.Type

abstract class ProductReportViewModelTestFixture {

    @RelaxedMockK
    lateinit var graphqlRepository: GraphqlRepository

    protected lateinit var viewModel: ProductReportViewModel

    @get:Rule
    val rule = CoroutineTestRule()

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        Dispatchers.setMain(TestCoroutineDispatcher())
    }

    @After
    fun cleanup() {
        Dispatchers.resetMain()
    }

    protected fun runCollectingUiState(block: (List<ProductReportUiState>) -> Unit) {
        val scope = CoroutineScope(rule.dispatchers.coroutineDispatcher)
        val uiEvent = mutableListOf<ProductReportUiState>()
        val uiEventCollectorJob = scope.launch {
            viewModel.uiState.toList(uiEvent)
        }
        block.invoke(uiEvent)
        uiEventCollectorJob.cancel()
    }

    protected fun runCollectingUiEvent(block: (List<ProductReportUiEvent>) -> Unit) {
        val scope = CoroutineScope(rule.dispatchers.coroutineDispatcher)
        val uiEvent = mutableListOf<ProductReportUiEvent>()
        val uiEventCollectorJob = scope.launch {
            viewModel.uiEvent.toList(uiEvent)
        }
        block.invoke(uiEvent)
        uiEventCollectorJob.cancel()
    }

    private fun onGetReportReasonSuccess_thenReturn(graphqlResponse: GraphqlResponse) {
        coEvery { graphqlRepository.response(any(), any()) } coAnswers { graphqlResponse }
    }

    protected fun `get gql response success`(data: List<ProductReportReason>) = GraphqlResponse(
        mapOf(ProductReportReason.Response::class.java to ProductReportReason.Response(data)) as MutableMap<Type, Any>,
        HashMap<Type, List<GraphqlError>>(),
        false
    )

    protected fun `get response success`(response: GraphqlResponse): List<ProductReportReason> {
        val expected = response.getSuccessData<ProductReportReason.Response>().data
        onGetReportReasonSuccess_thenReturn(response)
        viewModel = ProductReportViewModel(graphqlRepository, CoroutineTestDispatchersProvider)
        verifyUseCaseCalled()
        return expected
    }

    protected fun onGetReportReasonError_thenReturn(errorGql: GraphqlError) {
        val errors = HashMap<Type, List<GraphqlError>>()
        errors[ProductReportReason.Response::class.java] = listOf(errorGql)
        coEvery {
            graphqlRepository.response(
                any(),
                any()
            )
        } coAnswers { GraphqlResponse(HashMap<Type, Any?>(), errors, false) }
    }

    protected fun verifyUseCaseCalled() {
        coVerify { graphqlRepository.response(any(), any()) }
    }
}
