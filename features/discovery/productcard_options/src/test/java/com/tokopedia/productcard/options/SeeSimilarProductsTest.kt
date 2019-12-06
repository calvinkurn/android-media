package com.tokopedia.productcard.options

import com.tokopedia.discovery.common.model.ProductCardOptionsModel
import com.tokopedia.productcard.options.testutils.InstantTaskExecutorRuleSpek
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature

internal class SeeSimilarProductsTest: Spek({

    InstantTaskExecutorRuleSpek(this)

    val productCardOptionsModel = ProductCardOptionsModel(hasSimilarSearch = true)

    Feature("Click option see similar products") {
        createTestInstance()

        Scenario("Click see similar products") {
            lateinit var productCardOptionsViewModel: ProductCardOptionsViewModel

            Given("Product Card Options View Model") {
                productCardOptionsViewModel = createProductCardOptionsViewModel(productCardOptionsModel)
            }

            When("Click see similar products") {
                productCardOptionsViewModel.getOption(SEE_SIMILAR_PRODUCTS).onClick()
            }

            Then("Route to Similar Search Event Live Data is true") {
                val routeToSimilarSearchEvent = productCardOptionsViewModel.getRouteToSimilarSearchEventLiveData().value

                routeToSimilarSearchEvent?.getContentIfNotHandled() shouldBe true
            }

            Then("Close Product Card Options Event Live Data is true") {
                val closeProductCardOptionsEvent = productCardOptionsViewModel.getCloseProductCardOptionsEventLiveData().value

                closeProductCardOptionsEvent?.getContentIfNotHandled() shouldBe true
            }
        }
    }
})