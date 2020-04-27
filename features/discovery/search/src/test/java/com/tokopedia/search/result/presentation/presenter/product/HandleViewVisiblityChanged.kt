package com.tokopedia.search.result.presentation.presenter.product

import com.tokopedia.search.result.presentation.ProductListSectionContract
import io.mockk.*
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature

internal class HandleViewVisiblityChanged: Spek({

    Feature("Handle view visibility changed") {
        createTestInstance()

        Scenario("Handle view is visible and added") {
            val productListView by memoized<ProductListSectionContract.View>()
            lateinit var presenter: ProductListPresenter

            Given("Product list presenter") {
                presenter = createProductListPresenter()
            }

            When("handle view is visible and added") {
                presenter.onViewVisibilityChanged(true, true)
            }

            Then("Verify view interaction when visibility changed") {
                verifyOrder {
                    verifyIsVisible(productListView)
                    verifyIsAdded(productListView)
                }

                confirmVerified(productListView)
            }
        }

        Scenario("Handle view is added but not visible") {
            val productListView by memoized<ProductListSectionContract.View>()
            lateinit var presenter: ProductListPresenter

            Given("Product list presenter") {
                presenter = createProductListPresenter()
            }

            When("handle view is added but not visible") {
                presenter.onViewVisibilityChanged(false, true)
            }

            Then("Verify view interaction when visibility changed") {
                verify {
                    productListView wasNot Called
                }

                confirmVerified(productListView)
            }
        }

        Scenario("Handle view is visible but not added") {
            val productListView by memoized<ProductListSectionContract.View>()
            lateinit var presenter: ProductListPresenter

            Given("Product list presenter") {
                presenter = createProductListPresenter()
            }

            When("handle view is visible and added") {
                presenter.onViewVisibilityChanged(true, false)
            }

            Then("Verify view interaction when visibility changed") {
                verifyOrder {
                    verifyIsVisible(productListView)
                }

                confirmVerified(productListView)
            }
        }

        Scenario("Handle view is visible and added multiple times") {
            val productListView by memoized<ProductListSectionContract.View>()
            lateinit var presenter: ProductListPresenter

            Given("Product list presenter") {
                presenter = createProductListPresenter()
            }

            When("handle view is visible and added") {
                presenter.onViewVisibilityChanged(true, true)
                presenter.onViewVisibilityChanged(false, true)
                presenter.onViewVisibilityChanged(true, true)
            }

            Then("Verify view interaction when visibility changed") {
                verifyOrder {
                    verifyIsVisible(productListView)
                    verifyIsAdded(productListView)
                    verifyIsVisible(productListView)
                }

                confirmVerified(productListView)
            }
        }

        Scenario("Handle view is created and then goes visible and added while view is first active tab") {
            val productListView by memoized<ProductListSectionContract.View>()
            lateinit var presenter: ProductListPresenter

            Given("Product list presenter") {
                presenter = createProductListPresenter()
            }

            Given("View is first active tab") {
                every { productListView.isFirstActiveTab }.returns(true)
            }

            When("handle view is created and then goes visible and added") {
                presenter.onViewCreated()
                presenter.onViewVisibilityChanged(true, true)
            }

            Then("Verify view interaction when visibility changed") {
                verifyOrder {
                    productListView.isFirstActiveTab
                    productListView.reloadData()
                    verifyIsVisible(productListView)
                }

                confirmVerified(productListView)
            }
        }

        Scenario("Handle view is created and then goes visible and added while view is NOT first active tab") {
            val productListView by memoized<ProductListSectionContract.View>()
            lateinit var presenter: ProductListPresenter

            Given("Product list presenter") {
                presenter = createProductListPresenter()
            }

            Given("View is NOT first active tab") {
                every { productListView.isFirstActiveTab }.returns(false)
            }

            When("handle view is created and then goes visible and added") {
                presenter.onViewCreated()
                presenter.onViewVisibilityChanged(true, true)
            }

            Then("Verify view interaction when visibility changed") {
                verifyOrder {
                    productListView.isFirstActiveTab
                    verifyIsVisible(productListView)
                    verifyIsAdded(productListView)
                }

                confirmVerified(productListView)
            }
        }
    }
})