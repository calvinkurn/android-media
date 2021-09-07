package com.tokopedia.search.result.presentation.presenter.product

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.remoteconfig.RollenceKey
import com.tokopedia.search.jsonToObject
import com.tokopedia.search.result.complete
import com.tokopedia.search.result.domain.model.SearchProductModel
import com.tokopedia.search.result.presentation.model.SearchProductCountDataView
import com.tokopedia.search.shouldBeInstanceOf
import io.mockk.every
import io.mockk.runs
import io.mockk.slot
import org.junit.Test
import rx.Subscriber
import io.mockk.just

internal class SearchProductCountTitleTest: ProductListPresenterTestFixtures() {

    private val visitableListSlot = slot<List<Visitable<*>>>()

    // TODO:: Remove this test class, and combine this test case with SearchProductFirstPageTest
    @Test
    fun `Show ProductCountViewModel in Navigation Revamp`() {
        `Given Search Product API will return SearchProductModel`(searchProductCommonResponseJSON.jsonToObject())
        `Given AB Test return navigation revamp`(RollenceKey.NAVIGATION_VARIANT_REVAMP)
        `Given AB Test return navigation revamp`(RollenceKey.NAVIGATION_VARIANT_REVAMP2)
        setUp()
        `Given visitable list will be captured`()
        `When Load Data`()
        `Verify SearchProductCountViewModel is at the top of visitableList`()
    }

    private fun `Given Search Product API will return SearchProductModel`(searchProductModel: SearchProductModel) {
        every { searchProductFirstPageUseCase.execute(any(), any()) }.answers {
            secondArg<Subscriber<SearchProductModel>>().complete(searchProductModel)
        }
    }

    private fun `Given AB Test return navigation revamp`(variant: String) {
        every {
            productListView.abTestRemoteConfig?.getString(
                RollenceKey.NAVIGATION_EXP_TOP_NAV,
                productListView.abTestRemoteConfig?.getString(
                    RollenceKey.NAVIGATION_EXP_TOP_NAV2,
                    RollenceKey.NAVIGATION_VARIANT_OLD
                )
            )
        } returns (variant)
    }

    private fun `Given visitable list will be captured`() {
        every { productListView.setProductList(capture(visitableListSlot)) } just runs
    }

    private fun `When Load Data`() {
        productListPresenter.loadData(mapOf())
    }

    private fun `Verify SearchProductCountViewModel is at the top of visitableList`() {
        val visitableList = visitableListSlot.captured

        visitableList[0].shouldBeInstanceOf<SearchProductCountDataView>()
    }
}