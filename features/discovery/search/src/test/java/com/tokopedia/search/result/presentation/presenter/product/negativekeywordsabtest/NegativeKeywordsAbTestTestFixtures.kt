package com.tokopedia.search.result.presentation.presenter.product.negativekeywordsabtest

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.remoteconfig.RollenceKey
import com.tokopedia.search.result.complete
import com.tokopedia.search.result.domain.model.SearchProductModel
import com.tokopedia.search.result.presentation.model.ProductItemDataView
import com.tokopedia.search.result.presentation.presenter.product.ProductListPresenterTestFixtures
import io.mockk.every
import io.mockk.just
import io.mockk.runs
import io.mockk.slot
import org.junit.Assert
import rx.Subscriber

internal open class NegativeKeywordsAbTestTestFixtures : ProductListPresenterTestFixtures()  {
    protected val searchProductNegativeKeywordsResponseJSON = "searchproduct/negativekeywords/with-negative-keywords.json"
    protected val searchProductWithTopAdsAndHeadlineAdsJSON = "searchproduct/with-topads-and-headline-ads.json"

    protected val visitableListSlot = slot<List<Visitable<*>>>()
    protected val visitableList by lazy { visitableListSlot.captured }

    protected fun `Given view will return no ads ab test`() {
        every {
            productListView.abTestRemoteConfig?.getString(
                RollenceKey.SEARCH_ADVANCED_KEYWORD_ADV_NEG,
                any()
            )
        } answers {
            RollenceKey.SEARCH_ADVANCED_NEGATIVE_NO_ADS
        }
    }

    protected fun `Given search product API will success`(searchProductModel: SearchProductModel) {
        every {
            searchProductFirstPageUseCase.execute(any(), any())
        } answers {
            secondArg<Subscriber<SearchProductModel>>().complete(searchProductModel)
        }
    }

    protected fun `Given view will set and add product list`() {
        every {
            productListView.setProductList(capture(visitableListSlot))
        } just runs
    }

    protected fun `When Load Data`(searchParameter: Map<String, Any>) {
        productListPresenter.loadData(searchParameter)
    }

    protected fun `Then verify that ads is not loaded`() {
        Assert.assertTrue(
            visitableList.filterIsInstance<ProductItemDataView>().all { !it.isTopAds })
    }

    protected fun `Given view will return with ads ab test`() {
        every {
            productListView.abTestRemoteConfig?.getString(
                RollenceKey.SEARCH_ADVANCED_KEYWORD_ADV_NEG,
                any()
            )
        } answers {
            ""
        }
    }

    protected fun `Then verify that ads is loaded`() {
        Assert.assertTrue(visitableList.filterIsInstance<ProductItemDataView>().any { it.isTopAds })
    }
}