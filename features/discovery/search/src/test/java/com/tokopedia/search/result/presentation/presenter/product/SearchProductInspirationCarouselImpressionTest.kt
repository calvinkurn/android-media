package com.tokopedia.search.result.presentation.presenter.product

import com.tokopedia.discovery.common.constants.SearchApiConst
import com.tokopedia.search.jsonToObject
import com.tokopedia.search.result.complete
import com.tokopedia.search.result.domain.model.SearchProductModel
import com.tokopedia.search.result.presentation.model.InspirationCarouselViewModel
import io.mockk.every
import io.mockk.slot
import io.mockk.verify
import org.junit.Test
import rx.Subscriber

private const val inFirstPage = "searchproduct/inspirationcarousel/in-first-page.json"

internal class SearchProductInspirationCarouselImpressionTest: ProductListPresenterTestFixtures() {

    private val inspirationCarouselListSlot = slot<InspirationCarouselViewModel>()
    private val inspirationCarouselInfoSlot = slot<InspirationCarouselViewModel>()

    @Test
    fun `Impressed Inspiration Carousel`() {
        `Given Search Product API will return SearchProductModel with Inspiration Carousel`(inFirstPage.jsonToObject())

        `When Load Data`()

        `Then verify interaction for Inspiration Carousel Info impression`()
        `Then verify interaction for Inspiration Carousel List impression`()
    }

    private fun `Given Search Product API will return SearchProductModel with Inspiration Carousel`(searchProductModel: SearchProductModel) {
        every { searchProductFirstPageUseCase.execute(any(), any()) }.answers {
            secondArg<Subscriber<SearchProductModel>>().complete(searchProductModel)
        }
    }

    private fun `When Load Data`() {
        val searchParameter : Map<String, Any> = mutableMapOf<String, Any>().also {
            it[SearchApiConst.Q] = "samsung"
            it[SearchApiConst.START] = "0"
            it[SearchApiConst.UNIQUE_ID] = "unique_id"
            it[SearchApiConst.USER_ID] = productListPresenter.userId
        }

        productListPresenter.loadData(searchParameter)
    }

    private fun `Then verify interaction for Inspiration Carousel Info impression`() {
        verify {
            productListView.sendImpressionInspirationCarouselInfo(capture(inspirationCarouselInfoSlot))
        }
    }

    private fun `Then verify interaction for Inspiration Carousel List impression`() {
        verify {
            productListView.sendImpressionInspirationCarouselList(capture(inspirationCarouselListSlot))
        }
    }
}