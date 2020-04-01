package com.tokopedia.search.result.presentation.presenter.product

import com.tokopedia.search.result.presentation.ProductListSectionContract
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.verifyOrder
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature

internal class HandleViewCreatedTest: Spek({

    Feature("Handle view created") {
        createTestInstance()

        Scenario("Handle view created when Product is first active tab") {
            val productListView by memoized<ProductListSectionContract.View>()
            lateinit var presenter: ProductListPresenter

            Given("Product list presenter") {
                presenter = createProductListPresenter()
            }

            Given("View is first active tab") {
                every { productListView.isFirstActiveTab }.returns(true)
            }

            When("handle view created for first active tab") {
                presenter.onViewCreated()
            }

            Then("Verify view interaction on view created") {
                verifyOrder {
                    productListView.isFirstActiveTab
                    productListView.reloadData()
                }

                confirmVerified(productListView)
            }
        }

        Scenario("Handle view created when product is not first active tab") {
            val productListView by memoized<ProductListSectionContract.View>()
            lateinit var presenter: ProductListPresenter

            Given("Product list presenter") {
                presenter = createProductListPresenter()
            }

            Given("View is NOT first active tab") {
                every { productListView.isFirstActiveTab }.returns(false)
            }

            When("handle view created for first active tab") {
                presenter.onViewCreated()
            }

            Then("Verify view interaction on view created") {
                verifyOrder {
                    productListView.isFirstActiveTab
                }

                confirmVerified(productListView)
            }
        }

        Scenario("Handle view created multiple times") {
            val productListView by memoized<ProductListSectionContract.View>()
            lateinit var presenter: ProductListPresenter

            Given("Product list presenter") {
                presenter = createProductListPresenter()
            }

            Given("View is first active tab") {
                every { productListView.isFirstActiveTab }.returns(true)
            }

            When("handle view created for first active tab") {
                presenter.onViewCreated()
                presenter.onViewCreated()
                presenter.onViewCreated()
            }

            Then("Verify view interaction on view created") {
                verifyOrder {
                    productListView.isFirstActiveTab
                    productListView.reloadData()
                    productListView.isFirstActiveTab
                    productListView.isFirstActiveTab
                }

                confirmVerified(productListView)
            }
        }
    }
})