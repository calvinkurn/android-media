package com.tokopedia.similarsearch.viewmodel

import com.tokopedia.similarsearch.*
import com.tokopedia.similarsearch.getsimilarproducts.model.SimilarProductModel
import com.tokopedia.similarsearch.testutils.InstantTaskExecutorRuleSpek
import com.tokopedia.similarsearch.viewmodel.testinstance.getSimilarProductModelCommon
import com.tokopedia.similarsearch.testutils.TestException
import com.tokopedia.similarsearch.testutils.shouldBe
import com.tokopedia.similarsearch.testutils.stubExecute
import com.tokopedia.usecase.coroutines.UseCase
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature

internal class GetOriginalProductIdTest: Spek({

    InstantTaskExecutorRuleSpek(this)

    Feature("Get Original Product Id") {
        createTestInstance()

        Scenario("Get Original Product Id after Get Similar Products API success") {
            val similarProductModelCommon = getSimilarProductModelCommon()
            var originalProductId = ""

            lateinit var similarSearchViewModel: SimilarSearchViewModel

            Given("similar search view model") {
                similarSearchViewModel = createSimilarSearchViewModel()
            }

            Given("get similar product will be successful", timeout = 100000) {
                val getSimilarProductsUseCase by memoized<UseCase<SimilarProductModel>>()
                getSimilarProductsUseCase.stubExecute().returns(similarProductModelCommon)
                similarSearchViewModel.onViewCreated()
            }

            When("Get Original Product Id") {
                originalProductId = similarSearchViewModel.getOriginalProductId()
            }

            Then("Original Product Id should be original product id from API") {
                originalProductId shouldBe similarProductModelCommon.getOriginalProduct().id
            }
        }

        Scenario("Get Original Product Id after Get Similar Products API failed") {
            var originalProductId = ""

            lateinit var similarSearchViewModel: SimilarSearchViewModel

            Given("similar search view model") {
                similarSearchViewModel = createSimilarSearchViewModel()
            }

            Given("get similar product will fail") {
                val getSimilarProductsUseCase by memoized<UseCase<SimilarProductModel>>()
                getSimilarProductsUseCase.stubExecute().throws(TestException())
                similarSearchViewModel.onViewCreated()
            }

            When("Get Original Product Id") {
                originalProductId = similarSearchViewModel.getOriginalProductId()
            }

            Then("Original Product Id should be an empty string") {
                originalProductId shouldBe ""
            }
        }
    }
})