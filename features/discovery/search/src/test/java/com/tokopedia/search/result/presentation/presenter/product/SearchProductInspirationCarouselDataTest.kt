package com.tokopedia.search.result.presentation.presenter.product

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.discovery.common.constants.SearchApiConst
import com.tokopedia.discovery.common.constants.SearchConstant
import com.tokopedia.search.jsonToObject
import com.tokopedia.search.listShouldBe
import com.tokopedia.search.result.complete
import com.tokopedia.search.result.domain.model.SearchProductModel
import com.tokopedia.search.result.presentation.model.InspirationCarouselViewModel
import com.tokopedia.search.result.presentation.model.ProductItemViewModel
import com.tokopedia.search.result.shop.presentation.viewmodel.shouldBeInstanceOf
import com.tokopedia.search.shouldBe
import io.mockk.*
import org.junit.Test
import rx.Subscriber
import kotlin.math.exp

private const val inFirstPage = "searchproduct/inspirationcarousel/in-first-page.json"

internal class SearchProductInspirationCarouselDataTest: ProductListPresenterTestFixtures() {

    private val visitableListSlot = slot<List<Visitable<*>>>()

    @Test
    fun `Verify inspiration carousel data`() {
        `Given Search Product API will return SearchProductModel with Inspiration Carousel`(inFirstPage.jsonToObject())
        `Given Mechanism to save and get product position from cache`()

        `When Load Data`()

        `Then verify view set product list`()
        `Then verify carousel data`(inFirstPage.jsonToObject())
    }

    private fun `Given Search Product API will return SearchProductModel with Inspiration Carousel`(searchProductModel: SearchProductModel) {
        every { searchProductFirstPageUseCase.execute(any(), any()) }.answers {
            secondArg<Subscriber<SearchProductModel>>().complete(searchProductModel)
        }

        every { searchProductLoadMoreUseCase.execute(any(), any()) }.answers {
            secondArg<Subscriber<SearchProductModel>>().complete(searchProductCommonResponseJSON.jsonToObject())
        }
    }

    private fun `Given Mechanism to save and get product position from cache`() {
        val lastProductPositionSlot = slot<Int>()

        every { productListView.lastProductItemPositionFromCache }.answers {
            if (lastProductPositionSlot.isCaptured) lastProductPositionSlot.captured else 0
        }

        every { productListView.saveLastProductItemPositionToCache(capture(lastProductPositionSlot)) } just runs
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

    private fun `Then verify view set product list`() {
        verifyOrder {
            productListView.setProductList(capture(visitableListSlot))
        }
    }

    private fun `Then verify carousel data`(searchProductModel: SearchProductModel) {
        val visitableList = visitableListSlot.captured
        val expectedInspirationCarouselList = searchProductModel.searchInspirationCarousel.data

        (visitableList[4] as InspirationCarouselViewModel).assertInspirationCarouselViewModel(expectedInspirationCarouselList[1])
        (visitableList[9] as InspirationCarouselViewModel).assertInspirationCarouselViewModel(expectedInspirationCarouselList[2])
        (visitableList[14] as InspirationCarouselViewModel).assertInspirationCarouselViewModel(expectedInspirationCarouselList[3])
    }

    private fun InspirationCarouselViewModel.assertInspirationCarouselViewModel(inspirationCarouselData: SearchProductModel.InspirationCarouselData, shouldAddBannerCard: Boolean = false) {
        this.layout shouldBe inspirationCarouselData.layout
        this.type shouldBe inspirationCarouselData.type
        this.position shouldBe inspirationCarouselData.position
        this.title shouldBe inspirationCarouselData.title
        this.options.listShouldBe(inspirationCarouselData.inspirationCarouselOptions) { actual, expected ->
            actual.title shouldBe expected.title
            actual.url shouldBe expected.url
            actual.applink shouldBe expected.applink
            actual.bannerImageUrl shouldBe expected.bannerImageUrl
            actual.bannerLinkUrl shouldBe expected.bannerLinkUrl
            actual.bannerApplinkUrl shouldBe expected.bannerApplinkUrl
            actual.product.listShouldBe(expected.inspirationCarouselProducts) { actualProduct, expectedProduct ->
                actualProduct.id shouldBe expectedProduct.id
                actualProduct.name shouldBe expectedProduct.name
                actualProduct.price shouldBe expectedProduct.price
                actualProduct.priceStr shouldBe expectedProduct.priceStr
                actualProduct.imgUrl shouldBe expectedProduct.imgUrl
                actualProduct.rating shouldBe expectedProduct.rating
                actualProduct.countReview shouldBe expectedProduct.countReview
                actualProduct.url shouldBe expectedProduct.url
                actualProduct.applink shouldBe expectedProduct.applink
                actualProduct.description shouldBe expectedProduct.description
                actualProduct.inspirationCarouselType shouldBe this.type
                actualProduct.ratingAverage shouldBe expectedProduct.ratingAverage
                actualProduct.labelGroupList shouldBe expectedProduct.labelGroupList
                actualProduct.layout shouldBe this.layout
                actualProduct.originalPrice shouldBe expectedProduct.originalPrice
                actualProduct.discountPercentage shouldBe expectedProduct.discountPercentage
            }
            actual.inspirationCarouselType shouldBe this.type
            actual.layout shouldBe this.layout
            actual.position shouldBe this.position
            actual.carouselTitle shouldBe this.title
            actual.shouldAddBannerCard() shouldBe shouldAddBannerCard
        }
    }
}