package com.tokopedia.report.view.viewmodel

import com.tokopedia.graphql.data.model.GraphqlError
import com.tokopedia.report.data.model.ProductReportReason
import com.tokopedia.report.view.fragment.models.ProductReportUiEvent
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import junit.framework.Assert
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.assertTrue
import org.junit.Test

class ProductReportViewModelTest : ProductReportViewModelTestFixture() {
    private val dataReasonEmpty = listOf(ProductReportReason())
    private val dataReasonHaveChildren = listOf(
        ProductReportReason(
            children = listOf(ProductReportReason())
        )
    )

    @Test
    fun `when getReportReason success should return expected result`() = runBlockingTest {
        val expectedResponse = `get gql response success`(dataReasonEmpty)
        val expected = `get response success`(expectedResponse)
        val test = viewModel.uiState.first()
        Assert.assertEquals(expected, test.data)
    }

    @Test
    fun `when getReportReason error should return expected throwable`() = runBlockingTest {
        // given
        val errorGql = GraphqlError()
        errorGql.message = "Error getReportReason"
        onGetReportReasonError_thenReturn(errorGql)

        // when get init reason
        viewModel = ProductReportViewModel(graphqlRepository, CoroutineTestDispatchersProvider)
        verifyUseCaseCalled()

        runCollectingUiEvent {
            assertTrue(it.last() is ProductReportUiEvent.OnToasterError)
        }
    }

    @Test
    fun `event on item clicked with children is not empty`() = runBlockingTest {
        // given
        val response = `get gql response success`(dataReasonHaveChildren)
        `get response success`(response)

        // when
        viewModel.onEvent(
            ProductReportUiEvent.OnItemClicked(
                reason = dataReasonHaveChildren.first()
            )
        )

        // then
        runCollectingUiEvent {
            Assert.assertTrue(it.last() is ProductReportUiEvent.OnScrollTop)
        }
    }

    @Test
    fun `event on item clicked with children is empty`() = runBlockingTest {
        // given
        val response = `get gql response success`(dataReasonEmpty)
        `get response success`(response)

        // when
        viewModel.onEvent(
            ProductReportUiEvent.OnItemClicked(
                reason = dataReasonEmpty.first()
            )
        )

        // then
        runCollectingUiEvent {
            Assert.assertTrue(it.last() is ProductReportUiEvent.OnGoToForm)
        }
    }

    @Test
    fun `event on back pressed`() {
        // given
        val response = `get gql response success`(dataReasonEmpty)
        `get response success`(response)

        // when
        viewModel.onEvent(ProductReportUiEvent.OnBackPressed)

        // then
        runCollectingUiEvent {
            Assert.assertTrue(it.last() is ProductReportUiEvent.OnBackPressed)
        }
    }

    @Test
    fun `event on footer click`() {
        // given
        val response = `get gql response success`(dataReasonEmpty)
        `get response success`(response)

        // when
        viewModel.onEvent(ProductReportUiEvent.OnFooterClicked(""))

        // then
        runCollectingUiEvent {
            Assert.assertTrue(it.last() is ProductReportUiEvent.OnFooterClicked)
        }
    }
}
