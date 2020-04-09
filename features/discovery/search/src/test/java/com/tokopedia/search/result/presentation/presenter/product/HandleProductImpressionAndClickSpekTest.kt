package com.tokopedia.search.result.presentation.presenter.product

import com.tokopedia.search.result.presentation.ProductListSectionContract
import com.tokopedia.search.result.presentation.model.ProductItemViewModel
import com.tokopedia.search.result.presentation.presenter.product.testinstance.topAdsClickUrl
import com.tokopedia.search.result.presentation.presenter.product.testinstance.topAdsImpressionUrl
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.verify
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature

@Deprecated("Migrated to JUnit")
internal class HandleProductImpressionAndClickSpekTest: Spek({

    Feature("Handle onProductImpressed") {
        createTestInstance()

        Scenario("Handle onProductImpression with null ProductItemViewModel") {
            lateinit var productListPresenter: ProductListPresenter

            Given("Product list presenter") {
                productListPresenter = createProductListPresenter()
            }

            When("handle on product impressed") {
                productListPresenter.onProductImpressed(null, -1)
            }

            Then("Verify view does not do anything") {
                val productListView by memoized<ProductListSectionContract.View>()
                confirmVerified(productListView)
            }
        }

        Scenario("Handle onProductImpressed for Top Ads Product") {
            val productItemViewModel = ProductItemViewModel().also {
                it.productID = "12345"
                it.productName = "Hp Samsung"
                it.price = "Rp100.000"
                it.categoryID = 13
                it.isTopAds = true
                it.topadsImpressionUrl = topAdsImpressionUrl
                it.topadsClickUrl = topAdsClickUrl
            }
            val position = 0;

            lateinit var productListPresenter: ProductListPresenter

            Given("Product list presenter") {
                productListPresenter = createProductListPresenter()
            }

            When("handle on product impressed") {
                productListPresenter.onProductImpressed(productItemViewModel, position)
            }

            Then("Verify view interaction for Top Ads Product") {
                val productListView by memoized<ProductListSectionContract.View>()

                verify {
                    productListView.sendTopAdsTrackingUrl(productItemViewModel.topadsImpressionUrl)
                    productListView.sendTopAdsGTMTrackingProductImpression(productItemViewModel, position)
                }

                confirmVerified(productListView)
            }
        }

        Scenario("Handle onProductImpressed for non Top Ads Product") {
            val productItemViewModel = ProductItemViewModel().also {
                it.productID = "12345"
                it.productName = "Hp Samsung"
                it.price = "Rp100.000"
                it.categoryID = 13
                it.isTopAds = false
            }

            lateinit var productListPresenter: ProductListPresenter

            Given("Product list presenter") {
                productListPresenter = createProductListPresenter()
            }

            When("handle on product impressed") {
                productListPresenter.onProductImpressed(productItemViewModel, 0)
            }

            Then("Verify no view interaction for non TopAds product") {
                val productListView by memoized<ProductListSectionContract.View>()
                confirmVerified(productListView)
            }
        }
    }

    Feature("Handle onProductClick") {
        createTestInstance()

        Scenario("Handle onProductClick with null ProductItemViewModel") {
            lateinit var productListPresenter: ProductListPresenter

            Given("Product List Presenter") {
                productListPresenter = createProductListPresenter()
            }

            When("Handle onProductClick") {
                productListPresenter.onProductClick(null, -1)
            }

            Then("Verify view does not do anything") {
                val productListView by memoized<ProductListSectionContract.View>()
                confirmVerified(productListView)
            }
        }

        Scenario("Handle onProductClick for Top Ads Product") {
            val productItemViewModel = ProductItemViewModel().also {
                it.productID = "12345"
                it.productName = "Pixel 4"
                it.price = "Rp100.000.000"
                it.categoryID = 13
                it.isTopAds = true
                it.topadsImpressionUrl = topAdsImpressionUrl
                it.topadsClickUrl = topAdsClickUrl
            }
            val position = 0

            lateinit var productListPresenter: ProductListPresenter

            Given("Product List Presenter") {
                productListPresenter = createProductListPresenter()
            }

            When("Handle onProductClick") {
                productListPresenter.onProductClick(productItemViewModel, position)
            }

            Then("verify view interaction is correct for Top Ads Product") {
                val productListView by memoized<ProductListSectionContract.View>()

                verify {
                    productListView.sendTopAdsTrackingUrl(productItemViewModel.topadsClickUrl)
                    productListView.sendTopAdsGTMTrackingProductClick(productItemViewModel, position)
                    productListView.routeToProductDetail(productItemViewModel, position)
                }

                confirmVerified(productListView)
            }
        }

        Scenario("Handle onProductClick for non Top Ads Product") {
            val productItemViewModel = ProductItemViewModel().also {
                it.productID = "12345"
                it.productName = "Pixel 4"
                it.price = "Rp100.000.000"
                it.categoryID = 13
                it.isTopAds = false
            }
            val position = 0
            val userId = "12345678"

            lateinit var productListPresenter: ProductListPresenter

            Given("Product List Presenter") {
                productListPresenter = createProductListPresenter()
            }

            Given("Mock user session data") {
                val userSession by memoized<UserSessionInterface>()
                every { userSession.isLoggedIn } returns true
                every { userSession.userId } returns userId
            }

            When("Handle onProductClick") {
                productListPresenter.onProductClick(productItemViewModel, position)
            }

            Then("verify view interaction is correct for non Top Ads Product") {
                val productListView by memoized<ProductListSectionContract.View>()

                verify {
                    productListView.sendGTMTrackingProductClick(productItemViewModel, position, userId)
                    productListView.routeToProductDetail(productItemViewModel, position)
                }

                confirmVerified(productListView)
            }
        }
    }
})