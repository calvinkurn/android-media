package com.tokopedia.search.utils

import com.tokopedia.search.shouldBe
import com.tokopedia.search.shouldContain
import com.tokopedia.search.shouldHaveKeyValue
import com.tokopedia.search.shouldHaveSize
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature

@Deprecated("Migrated to JUnit")
class SearchKotlinExtSpekTest: Spek({

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
})