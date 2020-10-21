package com.tokopedia.search.result.presentation.presenter.product

import com.tokopedia.search.result.presentation.model.InspirationCarouselViewModel
import io.mockk.confirmVerified
import io.mockk.verify
import org.junit.Test

internal class SearchProductHandleInspirationCarouseImpressionlTest: ProductListPresenterTestFixtures() {

    @Test
    fun `Handle onInspirationCarouselListImpressed`() {
        val product = InspirationCarouselViewModel.Option.Product(
            id = "12345",
            name = "Samsung Galaxy A50S 4/64 Ram 4gb Rom 64gb Garansi Resmi",
            price = 8000000,
            priceStr = "Rp8.000.000",
            imgUrl = "https://ecs7.tokopedia.net/img/cache/200-square/product-1/2019/9/28/569641896/569641896_3a21ae42-10ee-45e6-b7ad-14c7a461de3b_1800_1800.jpg",
            rating = 4,
            countReview = 100,
            url = "",
            applink = "tokopedia://product/12345"
        )

        `When inspiration carousel list impressed`(product)

        `Then verify interaction for Inspiration Carousel List product impression`(product)
    }

    private fun `When inspiration carousel list impressed`(product: InspirationCarouselViewModel.Option.Product?) {
        productListPresenter.onInspirationCarouselListImpressed(product)
    }

    private fun `Then verify interaction for Inspiration Carousel List product impression`(product: InspirationCarouselViewModel.Option.Product) {
        verify {
            productListView.sendImpressionInspirationCarouselList(product)
        }

        confirmVerified(productListView)
    }

    @Test
    fun `Handle onInspirationCarouselListImpressed with null product (degenerate cases)`() {
        `When inspiration carousel list impressed`(null)

        `Then verify view do nothing`()
    }

    private fun `Then verify view do nothing`() {
        confirmVerified(productListView)
    }

    @Test
    fun `Handle onInspirationCarouselInfoImpressed`() {
        val product = InspirationCarouselViewModel.Option.Product(
                id = "12345",
                name = "Samsung Galaxy A50S 4/64 Ram 4gb Rom 64gb Garansi Resmi",
                price = 8000000,
                priceStr = "Rp8.000.000",
                imgUrl = "https://ecs7.tokopedia.net/img/cache/200-square/product-1/2019/9/28/569641896/569641896_3a21ae42-10ee-45e6-b7ad-14c7a461de3b_1800_1800.jpg",
                rating = 4,
                countReview = 100,
                url = "",
                applink = "tokopedia://product/12345"
        )

        `When inspiration carousel info impressed`(product)

        `Then verify interaction for Inspiration Carousel Info product impression`(product)
    }

    private fun `When inspiration carousel info impressed`(product: InspirationCarouselViewModel.Option.Product?) {
        productListPresenter.onInspirationCarouselInfoImpressed(product)
    }

    private fun `Then verify interaction for Inspiration Carousel Info product impression`(product: InspirationCarouselViewModel.Option.Product) {
        verify {
            productListView.sendImpressionInspirationCarouselInfo(product)
        }

        confirmVerified(productListView)
    }

    @Test
    fun `Handle onInspirationCarouselInfoImpressed with null product (degenerate cases)`() {
        `When inspiration carousel card info impressed`(null)

        `Then verify view do nothing`()
    }

    private fun `When inspiration carousel card info impressed`(product: InspirationCarouselViewModel.Option.Product?) {
        productListPresenter.onInspirationCarouselInfoImpressed(product)
    }
}