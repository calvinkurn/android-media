package com.tokopedia.search.result.shop.presentation.viewmodel

import io.kotlintest.shouldBe
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature
import kotlin.math.pow

class SearchShopViewModelSpekMockkTest: Spek({

    Feature("Math feature") {

        Scenario("Test addition") {
            var test = 0

            Given("test is 2") {
                test = 2
            }

            When("test added by 2") {
                test += 2
            }

            Then("test should be 4") {
                test.shouldBe(4)
            }
        }

        Scenario("Test addition purposely fail") {
            var test = 0

            Given("test is 3") {
                test = 3
            }

            When("test added by 2") {
                test += 2
            }

            Then("test should be 6") {
                test.shouldBe(6)
            }
        }

        Scenario("Test multiplication") {
            var test = 0

            Given("test is 2") {
                test = 2
            }

            When("test multiplied by 2") {
                test *= 2
            }

            Then("test should be 4") {
                test.shouldBe(4)
            }
        }
    }

    Feature("Another math feature") {

        Scenario("Test division fails") {
            var test = 0

            Given("test is 2") {
                test = 2
            }

            When("test is divided by 0") {
                test /= 0
            }

            Then("this part shouldn't be executed, already fails") {
                test.shouldBe(0)
            }
        }

        Scenario("Test power") {
            var test = 0.0

            Given("test is 2") {
                test = 2.0
            }

            When("test raised to the power of 3") {
                test = test.pow(3.0)
            }

            Then("test should be 8") {
                test.shouldBe(8.0)
            }
        }
    }
})