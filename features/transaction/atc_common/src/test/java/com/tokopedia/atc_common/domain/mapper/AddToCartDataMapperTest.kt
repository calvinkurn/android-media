package com.tokopedia.atc_common.domain.mapper

import com.tokopedia.atc_common.MockResponseProvider
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * Created by Irfan Khoirul on 2019-11-06.
 */
class AddToCartDataMapperTest {

    @Test
    fun `Add To Cart Failed Should Have Success As 0`() {
        // Given
        val mockResponse = MockResponseProvider.getResponseAtcError()

        // When
        val mapper = AddToCartDataMapper()
        val addToCartDataModel = mapper.mapAddToCartResponse(mockResponse)

        // Then
        assertEquals("OK", addToCartDataModel.status)
        assertEquals(0, addToCartDataModel.data.success)
        assertTrue(addToCartDataModel.errorMessage.size > 0)
    }

    @Test
    fun `Add To Cart Success Should Have Success As 1`() {
        // Given
        val mockResponse = MockResponseProvider.getResponseAtcSuccess()

        // When
        val mapper = AddToCartDataMapper()
        val addToCartDataModel = mapper.mapAddToCartResponse(mockResponse)

        // Then
        assertEquals("OK", addToCartDataModel.status)
        assertEquals(1, addToCartDataModel.data.success)
        assertTrue(addToCartDataModel.errorMessage.size > 0)
    }
}