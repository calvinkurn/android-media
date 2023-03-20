package com.tokopedia.report.view.viewmodel

import com.tokopedia.graphql.data.model.GraphqlError
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.report.data.model.ProductReportReason
import com.tokopedia.report.view.fragment.models.ProductReportUiEvent
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class ProductReportViewModelTest : ProductReportViewModelTestFixture() {
    private val categoryIdLvl1 = "1"
    private val categoryIdLvl2 = "2"
    private val categoryIdLvl3 = "3"
    private val dataReasonChildrenEmpty = listOf(ProductReportReason())
    private val dataReasonHaveChildren = listOf(
        ProductReportReason(
            categoryId = categoryIdLvl1,
            children = listOf(ProductReportReason(categoryId = categoryIdLvl2))
        )
    )
    private val dataReasonHaveDeepChildren = listOf(
        ProductReportReason(
            categoryId = categoryIdLvl1,
            children = listOf(
                ProductReportReason(
                    categoryId = categoryIdLvl2,
                    children = listOf(ProductReportReason(categoryId = categoryIdLvl3))
                )
            )
        )
    )

    @Test
    fun `when getReportReason success should return expected result`() {
        val expectedResponse = `get gql response success`(dataReasonChildrenEmpty)
        val expected = `get response success`(expectedResponse)

        runCollectingUiState {
            assertEquals(expected, it.first().data)
        }
    }

    @Test
    fun `when getReportReason error should return expected throwable`() {
        // given
        val errorGql = GraphqlError()
        errorGql.message = "Error getReportReason"
        onGetReportReasonError_thenReturn(errorGql)

        // when get init reason
        viewModel.onEvent(ProductReportUiEvent.LoadData)

        runCollectingUiEvent {
            assertTrue(it.last() is ProductReportUiEvent.OnToasterError)
        }

        verifyUseCaseCalled()
    }

    // region event state
    @Test
    fun `click sub-item from parent item`() {
        // given
        val response = `get gql response success`(dataReasonHaveChildren)
        `get response success`(response)
        val parent = dataReasonHaveChildren.first()

        // when
        viewModel.onEvent(
            ProductReportUiEvent.OnItemClicked(reason = parent)
        )
        // then
        runCollectingUiEvent {
            assertTrue(it.last() is ProductReportUiEvent.OnScrollTop)
        }

        // when
        viewModel.onEvent(
            ProductReportUiEvent.OnItemClicked(reason = parent.children.first())
        )

        // then
        runCollectingUiEvent {
            assertTrue(it.last() is ProductReportUiEvent.OnGoToForm)
        }
    }

    @Test
    fun `click sub-sub-item from parent item`() {
        // given
        val response = `get gql response success`(dataReasonHaveDeepChildren)
        `get response success`(response)
        val parent = dataReasonHaveDeepChildren.first()

        // when click base-parent
        viewModel.onEvent(
            ProductReportUiEvent.OnItemClicked(reason = parent)
        )
        // then
        runCollectingUiEvent {
            assertTrue(it.last() is ProductReportUiEvent.OnScrollTop)
        }

        // when click child level 1
        val childLvl1 = parent.children.first()
        viewModel.onEvent(
            ProductReportUiEvent.OnItemClicked(reason = childLvl1)
        )
        // then
        runCollectingUiEvent {
            assertTrue(it.last() is ProductReportUiEvent.OnScrollTop)
        }

        // when click child level 2
        val childLvl2 = childLvl1.children.first()
        viewModel.onEvent(
            ProductReportUiEvent.OnItemClicked(reason = childLvl2)
        )

        // then
        runCollectingUiEvent {
            assertTrue(it.last() is ProductReportUiEvent.OnGoToForm)
        }
    }

    @Test
    fun `event on item clicked with children is not empty`() {
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
            assertTrue(it.last() is ProductReportUiEvent.OnScrollTop)
        }
    }

    @Test
    fun `event on item clicked with children is empty`() {
        // given
        val response = `get gql response success`(dataReasonChildrenEmpty)
        `get response success`(response)

        // when
        viewModel.onEvent(
            ProductReportUiEvent.OnItemClicked(
                reason = dataReasonChildrenEmpty.first()
            )
        )

        // then
        runCollectingUiEvent {
            assertTrue(it.last() is ProductReportUiEvent.OnGoToForm)
        }
    }

    @Test
    fun `event on back pressed`() {
        // given
        viewModel.onEvent(ProductReportUiEvent.LoadData)

        // when
        viewModel.onEvent(ProductReportUiEvent.OnBackPressed)

        // then
        runCollectingUiEvent {
            assertTrue(it.last() is ProductReportUiEvent.OnBackPressed)
        }

        verifyUseCaseCalled()
    }

    @Test
    fun `on back to parent when at sub-sub-item`() {
        // given
        `click sub-sub-item from parent item`()

        // when
        viewModel.onEvent(ProductReportUiEvent.OnBackPressed)

        // then
        runCollectingUiState {
            assertTrue(it.last().filterId.lastOrNull() == categoryIdLvl1.toIntOrZero())
        }
    }

    @Test
    fun `event on footer click`() {
        // given
        viewModel.onEvent(ProductReportUiEvent.LoadData)

        // when
        viewModel.onEvent(ProductReportUiEvent.OnFooterClicked(""))

        // then
        runCollectingUiEvent {
            assertTrue(it.last() is ProductReportUiEvent.OnFooterClicked)
        }

        verifyUseCaseCalled()
    }
    // endregion event state
}
