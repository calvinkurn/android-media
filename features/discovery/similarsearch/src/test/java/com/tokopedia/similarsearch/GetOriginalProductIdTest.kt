package com.tokopedia.similarsearch

import com.tokopedia.similarsearch.getsimilarproducts.model.SimilarProductModel
import com.tokopedia.similarsearch.testinstance.getSimilarProductModelCommon
import com.tokopedia.usecase.coroutines.UseCase
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature

internal class GetOriginalProductIdTest: Spek({

    Feature("Get Original Product Id") {
        createTestInstance()

        Scenario("Get Original Product Id after Get Similar Products API success") {
            val similarProductModelCommon = getSimilarProductModelCommon()
            var originalProductId = ""

            lateinit var similarSearchViewModel: SimilarSearchViewModel

            Given("similar search view model") {
                similarSearchViewModel = createSimilarSearchViewModel()
            }

            Given("get similar product will be successful") {
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