package com.tokopedia.search.result.presentation.presenter.product

import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.verifyOrder
import org.junit.Test

internal class SearchProductHandleViewCreatedTest: ProductListPresenterTestFixtures() {

    @Test
    fun `Handle view created when Product is first active tab`() {
        `Given View is first active tab`()

        `When handle view created`()

        `Then verify view interaction when created and is first active tab`()
    }

    private fun `Given View is first active tab`() {
        every { productListView.isFirstActiveTab }.returns(true)
    }

    private fun `When handle view created`() {
        productListPresenter.onViewCreated()
    }

    private fun `Then verify view interaction when created and is first active tab`() {
        verifyOrder {
            productListView.isFirstActiveTab
            productListView.reloadData()
        }

        confirmVerified(productListView)
    }

    @Test
    fun `Handle view created when product is not first active tab`() {
        `Given view is NOT first active tab`()

        `When handle view created`()

        `Then verify view interaction when created and not first active tab`()
    }

    private fun `Given view is NOT first active tab`() {
        every { productListView.isFirstActiveTab }.returns(false)
    }

    private fun `Then verify view interaction when created and not first active tab`() {
        verifyOrder {
            productListView.isFirstActiveTab
        }

        confirmVerified(productListView)
    }

    @Test
    fun `Handle view created multiple times (edge cases)`() {
        `Given View is first active tab`()

        `When handle view created multiple times`()

        `Then verify view interaction when created multiple times`()
    }

    private fun `When handle view created multiple times`() {
        productListPresenter.onViewCreated()
        productListPresenter.onViewCreated()
        productListPresenter.onViewCreated()
    }

    private fun `Then verify view interaction when created multiple times`() {
        verifyOrder {
            productListView.isFirstActiveTab
            productListView.reloadData()
            productListView.isFirstActiveTab
            productListView.isFirstActiveTab
        }

        confirmVerified(productListView)
    }
}