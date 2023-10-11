package com.tokopedia.search.result.presentation.presenter.product

import com.tokopedia.remoteconfig.RollenceKey
import com.tokopedia.search.result.presentation.model.ProductItemDataView
import io.mockk.every
import io.mockk.verify
import org.junit.Test

internal class SearchProductSimilarSearchOnBoardingTest: ProductListPresenterTestFixtures() {
    private fun `Given similar search ab test active`() {
        every {
            abTestRemoteConfig.getString(RollenceKey.SEARCH_SIMILAR_SEARCH_COACHMARK, any())
        } returns RollenceKey.SEARCH_SIMILAR_SEARCH_COACHMARK_VARIANT
    }

    private fun `Given similar search ab test inactive`() {
        every {
            abTestRemoteConfig.getString(RollenceKey.SEARCH_SIMILAR_SEARCH_COACHMARK, any())
        } returns ""
    }

    private fun `Given similar search coach mark local cache return true`() {
        every {
            similarSearchCoachMarkLocalCache.shouldShowThreeDotsCoachmark()
        } returns true
    }

    private fun `Given similar search coach mark local cache return false`() {
        every { similarSearchCoachMarkLocalCache.shouldShowThreeDotsCoachmark() } returns false
    }

    private fun `When product item selected`(item: ProductItemDataView, position: Int) {
        productListPresenter.onProductClick(item, position)
    }

    private fun `Then verify show similar search coach mark is called`(
        item: ProductItemDataView,
        position: Int,
    ) {
        verify { similarSearchOnBoardingView.showSimilarSearchThreeDotsCoachmark(item, position) }
    }

    private fun `Then verify show similar search coach mark is not called`() {
        verify(exactly = 0) { similarSearchOnBoardingView.showSimilarSearchThreeDotsCoachmark(any(), any()) }
    }

    @Test
    fun `Similar Search OnBoarding should be called when ab test active and coach mark local cache return true`() {
        val position = 1
        val item = ProductItemDataView().apply {
            isWishlistButtonEnabled = true
        }
        `Given similar search ab test active`()
        `Given similar search coach mark local cache return true`()

        `When product item selected`(item, position)

        `Then verify show similar search coach mark is called`(item, position)
    }

    @Test
    fun `Similar Search OnBoarding should not be called when ab test aactive and coach mark local cache return false`() {
        val position = 1
        val item = ProductItemDataView().apply {
            isWishlistButtonEnabled = true
        }
        `Given similar search ab test active`()
        `Given similar search coach mark local cache return false`()

        `When product item selected`(item, position)

        `Then verify show similar search coach mark is not called`()
    }

    @Test
    fun `Similar Search OnBoarding should not be called when ab test inactive and coach mark local cache return true`() {
        val position = 1
        val item = ProductItemDataView().apply {
            isWishlistButtonEnabled = true
        }
        `Given similar search ab test inactive`()
        `Given similar search coach mark local cache return true`()

        `When product item selected`(item, position)

        `Then verify show similar search coach mark is not called`()
    }

    @Test
    fun `Similar Search OnBoarding should not be called when ab test inactive and coach mark local cache return false`() {
        val position = 1
        val item = ProductItemDataView().apply {
            isWishlistButtonEnabled = true
        }
        `Given similar search ab test inactive`()
        `Given similar search coach mark local cache return false`()

        `When product item selected`(item, position)

        `Then verify show similar search coach mark is not called`()
    }
}
