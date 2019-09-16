package com.tokopedia.search.utils

import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature

class SearchKotlinExtTest: Spek({

    Feature("Get list between first and last element of a list") {

        Scenario("Original list is null") {
            val originalList: List<String>? = null
            lateinit var betweenFirstAndLastList: List<String>

            When("get between first and last of original list") {
                betweenFirstAndLastList = originalList.betweenFirstAndLast()
            }

            Then("between first and last list should be empty list") {
                betweenFirstAndLastList shouldHaveSize 0
            }
        }

        val originalList by memoized { mutableListOf<String>() }
        lateinit var betweenFirstAndLastList: List<String>

        Scenario("Original list has less than 3 elements") {

            Given("original list with 2 elements") {
                originalList.add("1")
                originalList.add("2")
            }

            When("get between first and last of original list") {
                betweenFirstAndLastList = originalList.betweenFirstAndLast()
            }

            Then("between first and last list should be empty list") {
                betweenFirstAndLastList shouldHaveSize 0
            }
        }

        Scenario("Original list have more than or equal 3 elements") {

            Given("original list with 5 elements") {
                originalList.add("1")
                originalList.add("2")
                originalList.add("3")
                originalList.add("4")
                originalList.add("5")
            }

            When("get between first and last of original list") {
                betweenFirstAndLastList = originalList.betweenFirstAndLast()
            }

            Then("between first and last list size should be 3") {
                betweenFirstAndLastList shouldHaveSize 3
            }

            Then("between first and last list should be the second to forth element") {
                betweenFirstAndLastList shouldContain "2"
                betweenFirstAndLastList shouldContain "3"
                betweenFirstAndLastList shouldContain "4"
            }
        }
    }

    Feature("Get second to last element of a list") {

        Scenario("Original list is null") {
            val originalList: List<String>? = null
            lateinit var secondToLastList: List<String>

            When("get between first and last of original list") {
                secondToLastList = originalList.secondToLast()
            }

            Then("between first and last list should be empty list") {
                secondToLastList shouldHaveSize 0
            }
        }

        val originalList by memoized { mutableListOf<String>() }
        lateinit var secondToLastList: List<String>

        Scenario("Original list has less than 2 elements") {

            Given("original list with 1 element") {
                originalList.add("1")
            }

            When("get second to last of original list") {
                secondToLastList = originalList.secondToLast()
            }

            Then("between first and last list should be empty list") {
                secondToLastList shouldHaveSize 0
            }
        }

        Scenario("Original list have more than or equal 2 elements") {

            Given("original list with 5 elements") {
                originalList.add("1")
                originalList.add("2")
                originalList.add("3")
                originalList.add("4")
                originalList.add("5")
            }

            When("get second to last of original list") {
                secondToLastList = originalList.secondToLast()
            }

            Then("second to last list size should be 4") {
                secondToLastList shouldHaveSize 4
            }

            Then("second to last list list should be the second to fifth element") {
                secondToLastList shouldContain "2"
                secondToLastList shouldContain "3"
                secondToLastList shouldContain "4"
                secondToLastList shouldContain "5"
            }
        }
    }
})

private infix fun List<String>.shouldContain(expectedToContain: String) {
    this.forEach {
        if (it == expectedToContain) {
            return
        }
    }

    throw AssertionError("List does not cointain $expectedToContain")
}

private infix fun List<*>.shouldHaveSize(expectedSize: Int) {
    if (this.size != expectedSize) {
        throw AssertionError("List size is ${this.size}, expected size: $expectedSize")
    }
}