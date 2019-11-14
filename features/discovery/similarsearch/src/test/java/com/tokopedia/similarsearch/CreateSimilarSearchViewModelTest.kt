package com.tokopedia.similarsearch

import com.tokopedia.similarsearch.testinstance.getSimilarSearchSelectedProductNotWishlisted
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature

internal class CreateSimilarSearchViewModelTest: Spek({

    InstantTaskExecutorRuleSpek(this)

    Feature("Create Similar Search View Model") {
        createTestInstance()

        Scenario("Create Similar Search View Model") {
            val similarSearchSelectedProductNotWishlisted = getSimilarSearchSelectedProductNotWishlisted()
            lateinit var similarSearchViewModel: SimilarSearchViewModel

            When("create similar search view model") {
                similarSearchViewModel = createSimilarSearchViewModel()
            }

            Then("similar search view model should have selected product") {
                similarSearchViewModel.similarSearchSelectedProduct.shouldBe(similarSearchSelectedProductNotWishlisted)
            }
        }
    }
})