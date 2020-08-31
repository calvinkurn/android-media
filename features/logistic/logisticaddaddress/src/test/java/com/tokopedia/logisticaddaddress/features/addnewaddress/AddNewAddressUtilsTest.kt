package com.tokopedia.logisticaddaddress.features.addnewaddress

import com.tokopedia.logisticaddaddress.common.AddressConstants
import org.junit.Assert
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature

object AddNewAddressUtilsTest: Spek ({

    Feature("execute given value") {

        Scenario("givenExactValue_whenExecuted") {
            val tLat = AddressConstants.DEFAULT_LAT
            val tLong = AddressConstants.DEFAULT_LONG

            val actual = AddNewAddressUtils.hasDefaultCoordinate(tLat, tLong)

            Then("return true") {
                Assert.assertTrue(actual)
            }
        }

        Scenario("givenSlightlyMiss_whenExecuted") {
            val tLat = AddressConstants.DEFAULT_LAT - 0.000009
            val tLong = AddressConstants.DEFAULT_LONG + 0.000006

            val actual = AddNewAddressUtils.hasDefaultCoordinate(tLat, tLong)

            Then("return true") {
                Assert.assertTrue(actual)
            }
        }
    }
})