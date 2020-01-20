package com.tokopedia.search.utils

import com.tokopedia.search.shouldBe
import com.tokopedia.search.shouldContain
import com.tokopedia.search.shouldHaveKeyValue
import com.tokopedia.search.shouldHaveSize
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

    Feature("Convert Map<String, Any> to Map<String, String>") {

        Scenario("Origin Map is null") {
            val originalMap : Map<String, Any>? = null
            lateinit var mapValuesInString : Map<String, String>

            When("convert values to String") {
                mapValuesInString = originalMap.convertValuesToString()
            }

            Then("Map values in string should be empty map") {
                mapValuesInString shouldHaveSize 0
            }
        }

        Scenario("Original Map is not null") {
            val originalMap = mutableMapOf<String, Any>()
            lateinit var mapValuesInString : Map<String, String>

            Given("original map with various value data types") {
                originalMap["string"] = "string"
                originalMap["integer"] = 1
                originalMap["boolean"] = false
                originalMap["double"] = 1.0
            }

            When("convert values to String") {
                mapValuesInString = originalMap.convertValuesToString()
            }

            Then("Map values in string should have all values converted as string") {
                mapValuesInString.shouldHaveKeyValue("string", "string")
                mapValuesInString.shouldHaveKeyValue("integer", "1")
                mapValuesInString.shouldHaveKeyValue("boolean", "false")
                mapValuesInString.shouldHaveKeyValue("double", "1.0")
            }
        }
    }

    Feature("List exists item of certain type") {

        Scenario("Original list is null") {
            val originalList: List<Any>? = null
            val exists = false

            When("check exists") {
                originalList.exists<String>()
            }

            Then("exists should be false") {
                exists shouldBe false
            }
        }

        val originalList by memoized { mutableListOf<Any>() }
        var exists = false

        class Dummy

        Scenario("Original list exists item of type") {

            Given("original list has Dummy instance") {
                originalList.add(Dummy())
                originalList.add("")
                originalList.add(1)
            }

            When("check if Dummy class exists") {
                exists = originalList.exists<Dummy>()
            }

            Then("exists should be true") {
                exists shouldBe true
            }
        }
    }
})