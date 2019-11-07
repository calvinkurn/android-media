package com.tokopedia.atc_common.domain.mapper

import com.tokopedia.atc_common.MockResponseProvider
import io.mockk.spyk
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on
import org.junit.Assert
import org.junit.platform.runner.JUnitPlatform
import org.junit.runner.RunWith

/**
 * Created by Irfan Khoirul on 2019-11-06.
 */

@RunWith(JUnitPlatform::class)
class AddToCartDataMapperTest : Spek({
    given("AddToCartDataMapper") {
        val mapper : AddToCartDataMapper = spyk()

        on("given response") {
            val mockResponse = MockResponseProvider.getResponseAtcSuccess()
            val result = mapper.mapAddToCartResponse(mockResponse)

            it("domain model and data model status should be equal") {
                Assert.assertEquals(result.status, mockResponse.addToCartResponse.status)
            }

            it("domain model and data model success should be equal") {
                Assert.assertEquals(result.data.success, mockResponse.addToCartResponse.data.success)
            }

            it("domain model and data model error message size should be equal") {
                Assert.assertEquals(result.errorMessage.size, mockResponse.addToCartResponse.errorMessage.size)
            }

            it("domain model and data model error reporter eligible should be equal") {
                Assert.assertEquals(result.errorReporter.eligible, mockResponse.addToCartResponse.errorReporter.eligible)
            }

            it("domain model and data model error reporter text submit title should be equal") {
                Assert.assertEquals(result.errorReporter.texts.submitTitle, mockResponse.addToCartResponse.errorReporter.texts.submitTitle)
            }

            it("domain model and data model error reporter text submit description should be equal") {
                Assert.assertEquals(result.errorReporter.texts.submitDescription, mockResponse.addToCartResponse.errorReporter.texts.submitDescription)
            }

            it("domain model and data model error reporter text submit button should be equal") {
                Assert.assertEquals(result.errorReporter.texts.submitButton, mockResponse.addToCartResponse.errorReporter.texts.submitButton)
            }

            it("domain model and data model error reporter text cancel button should be equal") {
                Assert.assertEquals(result.errorReporter.texts.cancelButton, mockResponse.addToCartResponse.errorReporter.texts.cancelButton)
            }

            it("domain model and data model cart id should be equal") {
                Assert.assertEquals(result.data.cartId, mockResponse.addToCartResponse.data.cartId)
            }

            it("domain model and data model product id should be equal") {
                Assert.assertEquals(result.data.productId, mockResponse.addToCartResponse.data.productId)
            }

            it("domain model and data model quantity should be equal") {
                Assert.assertEquals(result.data.quantity, mockResponse.addToCartResponse.data.quantity)
            }

            it("domain model and data model notes should be equal") {
                Assert.assertEquals(result.data.notes, mockResponse.addToCartResponse.data.notes)
            }

            it("domain model and data model shop id should be equal") {
                Assert.assertEquals(result.data.shopId, mockResponse.addToCartResponse.data.shopId)
            }

            it("domain model and data model customer id") {
                Assert.assertEquals(result.data.customerId, mockResponse.addToCartResponse.data.customerId)
            }

            it("domain model and data model warehouse id should be equal") {
                Assert.assertEquals(result.data.warehouseId, mockResponse.addToCartResponse.data.warehouseId)
            }

            it("domain model and data model tracker attribution should be equal") {
                Assert.assertEquals(result.data.trackerAttribution, mockResponse.addToCartResponse.data.trackerAttribution)
            }

            it("domain model and data model tracker list name should be equal") {
                Assert.assertEquals(result.data.trackerListName, mockResponse.addToCartResponse.data.trackerListName)
            }

            it("domain model and data model uc ut param should be equal") {
                Assert.assertEquals(result.data.ucUtParam, mockResponse.addToCartResponse.data.ucUtParam)
            }

            it("domain model and data model is trade in should be equal") {
                Assert.assertEquals(result.data.isTradeIn, mockResponse.addToCartResponse.data.isTradeIn)
            }

            it("domain model and data model message size is equal") {
                Assert.assertEquals(result.data.message.size, mockResponse.addToCartResponse.data.message.size)
            }
        }
    }
})