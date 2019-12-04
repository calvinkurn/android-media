package com.tokopedia.productcard.options

import com.tokopedia.discovery.common.model.ProductCardOptionsModel
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature

internal class CreateProductCardOptionsViewModelTest: Spek({

    Feature("Create Product Card Options View Model") {

        Scenario("Given Product Card Options Model with empty options") {
            val productCardOptionsModel = ProductCardOptionsModel()

            lateinit var productCardOptionsViewModel: ProductCardOptionsViewModel

            When("Product Card Options View Model with empty options") {
                productCardOptionsViewModel = createProductCardOptionsViewModel(productCardOptionsModel)
            }

            Then("Assert product card options item model list is empty") {
                val productCardOptionsItemModelList = productCardOptionsViewModel.getItemList().value

                productCardOptionsItemModelList shouldHaveSize 0
            }
        }
    }
})