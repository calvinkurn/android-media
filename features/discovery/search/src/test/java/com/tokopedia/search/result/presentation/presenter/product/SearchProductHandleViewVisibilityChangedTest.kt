package com.tokopedia.search.result.presentation.presenter.product

import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.verifyOrder
import org.junit.Test

internal class SearchProductHandleViewVisibilityChangedTest: ProductListPresenterTestFixtures() {

    @Test
    fun `Handle view is visible and added`() {
        `When handle view is visible and added`()

        `Then verify view interaction when visible and added`()
    }

    private fun `When handle view is visible and added`() {
        productListPresenter.onViewVisibilityChanged(true, true)
    }

    private fun `Then verify view interaction when visible and added`() {
        verifyOrder {
            verifyIsVisible(productListView)
            verifyIsAdded(productListView)
        }

        confirmVerified(productListView)
    }

    @Test
    fun `Handle view is added but not visible`() {
        `When handle view is added but not visible`()

        `Then verify view interaction when added but not visible`()
    }

    private fun `When handle view is added but not visible`() {
        productListPresenter.onViewVisibilityChanged(false, true)
    }

    private fun `Then verify view interaction when added but not visible`() {
        confirmVerified(productListView)
    }

    @Test
    fun `Handle view is visible but not added (degenerate cases)`() {
        `When handle view is visible but not added`()

        `Then verify view interaction when visible but not added`()
    }

    private fun `When handle view is visible but not added`() {
        productListPresenter.onViewVisibilityChanged(true, false)
    }

    private fun `Then verify view interaction when visible but not added`() {
        verifyOrder {
            verifyIsVisible(productListView)
        }

        confirmVerified(productListView)
    }

    @Test
    fun `Handle view is visible and added multiple times`() {
        `When handle view is visible and added multiple times`()

        `Then verify view interaction when visible and added multiple times`()
    }

    private fun `When handle view is visible and added multiple times`() {
        productListPresenter.onViewVisibilityChanged(true, true)
        productListPresenter.onViewVisibilityChanged(false, true)
        productListPresenter.onViewVisibilityChanged(true, true)
    }

    private fun `Then verify view interaction when visible and added multiple times`() {
        verifyOrder {
            verifyIsVisible(productListView)
            verifyIsAdded(productListView)
            verifyIsVisible(productListView)
        }

        confirmVerified(productListView)
    }

    @Test
    fun `Handle view is created and then goes visible and added while view is first active tab`() {
        `Given view is first active tab`()
        `Given view is created`()

        `When handle view is visible and added`()

        `Then verify view interaction when created and then goes visible and added while view is first active tab`()
    }

    private fun `Given view is first active tab`() {
        every { productListView.isFirstActiveTab }.returns(true)
    }

    private fun `Given view is created`() {
        productListPresenter.onViewCreated()
    }

    private fun `Then verify view interaction when created and then goes visible and added while view is first active tab`() {
        verifyOrder {
            productListView.isFirstActiveTab
            productListView.reloadData()
            verifyIsVisible(productListView)
        }

        confirmVerified(productListView)
    }

    @Test
    fun `Handle view is created and then goes visible and added while view is NOT first active tab`() {
        `Given view is NOT first active tab`()
        `Given view is created`()

        `When handle view is visible and added`()

        `Then verify view interaction when created and then goes visible and added while view is NOT first active tab`()
    }

    private fun `Given view is NOT first active tab`() {
        every { productListView.isFirstActiveTab }.returns(false)
    }

    private fun `Then verify view interaction when created and then goes visible and added while view is NOT first active tab`() {
        verifyOrder {
            productListView.isFirstActiveTab
            verifyIsVisible(productListView)
            verifyIsAdded(productListView)
        }

        confirmVerified(productListView)
    }
}