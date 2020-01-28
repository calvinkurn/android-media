package com.tokopedia.atc_common.domain.mapper

import com.tokopedia.atc_common.MockResponseProvider
import com.tokopedia.atc_common.data.model.response.AddToCartGqlResponse
import com.tokopedia.atc_common.domain.model.response.AddToCartDataModel
import org.junit.Assert
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature

/**
 * Created by Irfan Khoirul on 2019-11-06.
 */

class AddToCartDataMapperTest : Spek({
    Feature("Add to cart") {

        val mapper by memoized {
            AddToCartDataMapper()
        }

        Scenario("add to cart failed") {

            lateinit var mockResponse: AddToCartGqlResponse
            lateinit var addToCartDataModel: AddToCartDataModel

            Given("api response") {
                mockResponse = MockResponseProvider.getResponseAtcError()
            }

            When("map response") {
                addToCartDataModel = mapper.mapAddToCartResponse(mockResponse)
            }

            Then("status is OK") {
                Assert.assertEquals("OK", addToCartDataModel.status)
            }

            Then("success is 0") {
                Assert.assertEquals(0, addToCartDataModel.data.success)
            }

            Then("message error is not empty") {
                Assert.assertTrue(addToCartDataModel.errorMessage.size > 0)
            }

        }

        Scenario("add to cart success") {

            lateinit var mockResponse: AddToCartGqlResponse
            lateinit var addToCartDataModel: AddToCartDataModel

            Given("api response") {
                mockResponse = MockResponseProvider.getResponseAtcSuccess()
            }

            When("map response") {
                addToCartDataModel = mapper.mapAddToCartResponse(mockResponse)
            }

            Then("status is OK") {
                Assert.assertEquals("OK", addToCartDataModel.status)
            }

            Then("success is 1") {
                Assert.assertEquals(1, addToCartDataModel.data.success)
            }

            Then("message success is not empty") {
                Assert.assertTrue(addToCartDataModel.data.message.size > 0)
            }

        }

    }

})