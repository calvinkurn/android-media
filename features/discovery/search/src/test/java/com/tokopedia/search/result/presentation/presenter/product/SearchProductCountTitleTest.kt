package com.tokopedia.search.result.presentation.presenter.product

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.discovery.common.constants.SearchConstant.ABTestRemoteConfigKey
import com.tokopedia.discovery.common.constants.SearchConstant.ABTestRemoteConfigKey.AB_TEST_NAV_REVAMP
import com.tokopedia.discovery.common.constants.SearchConstant.ABTestRemoteConfigKey.AB_TEST_OLD_NAV
import com.tokopedia.search.jsonToObject
import com.tokopedia.search.result.complete
import com.tokopedia.search.result.domain.model.SearchProductModel
import com.tokopedia.search.result.presentation.model.SearchProductCountViewModel
import com.tokopedia.search.shouldBeInstanceOf
import io.mockk.every
import io.mockk.just
import io.mockk.runs
import io.mockk.slot
import org.junit.Test
import rx.Subscriber

internal class SearchProductCountTitleTest: ProductListPresenterTestFixtures() {

    private val visitableListSlot = slot<List<Visitable<*>>>()

    @Test
    fun `Show ProductCountViewModel in Navigation Revamp`() {
        `Given Search Product API will return SearchProductModel`(searchProductCommonResponseJSON.jsonToObject())
        `Given AB Test return navigation revamp`()
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

    private fun `Given AB Test return navigation revamp`() {
        every {
            productListView.abTestRemoteConfig.getString(ABTestRemoteConfigKey.AB_TEST_NAVIGATION_REVAMP, AB_TEST_OLD_NAV)
        }.answers { AB_TEST_NAV_REVAMP }
    }

    private fun `Given visitable list will be captured`() {
        every { productListView.setProductList(capture(visitableListSlot)) } just runs
    }

    private fun `When Load Data`() {
        productListPresenter.loadData(mapOf())
    }

    private fun `Verify SearchProductCountViewModel is at the top of visitableList`() {
        val visitableList = visitableListSlot.captured

        visitableList[0].shouldBeInstanceOf<SearchProductCountViewModel>()
    }

    @Test
    fun `Show ProductCountViewModel in Existing Navigation`() {
        `Given Search Product API will return SearchProductModel`(searchProductCommonResponseJSON.jsonToObject())
        `Given AB Test return existing navigation`()
        setUp()
        `Given visitable list will be captured`()
        `When Load Data`()
        `Verify visitableList does not have SearchProductCountViewModel`()
    }

    private fun `Given AB Test return existing navigation`() {
        every {
            productListView.abTestRemoteConfig.getString(ABTestRemoteConfigKey.AB_TEST_NAVIGATION_REVAMP, AB_TEST_OLD_NAV)
        }.answers { AB_TEST_OLD_NAV }
    }

    private fun `Verify visitableList does not have SearchProductCountViewModel`() {
        val visitableList = visitableListSlot.captured

        assert(visitableList.find{ it is SearchProductCountViewModel } == null) {
            "Visitable list should not have SearchProductCountViewModel"
        }
    }
}