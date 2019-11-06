package com.tokopedia.atc_common.domain.mapper

import com.tokopedia.atc_common.MockResponseProvider
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Test

/**
 * Created by Irfan Khoirul on 2019-09-11.
 */

class AddToCartDataMapperTest {

    private val mapper = AddToCartDataMapper()

    @Test
    fun mapAddToCartResponseSuccess_statusIsEqual() {
        //given
        val mockResponse = MockResponseProvider.getResponseAtcSuccess()
        //when
        val result = mapper.mapAddToCartResponse(mockResponse)
        //then
        assertEquals(result.status, mockResponse.addToCartResponse.status)
    }

    @Test
    fun mapAddToCartResponseSuccess_successIsOne() {
        //given
        val mockResponse = MockResponseProvider.getResponseAtcSuccess()
        //when
        val result = mapper.mapAddToCartResponse(mockResponse)
        //then
        assertEquals(1, result.data.success)
    }

    @Test
    fun mapAddToCartResponseSuccess_responseJsonShouldNotBeEmpty() {
        //given
        val mockResponse = MockResponseProvider.getResponseAtcSuccess()
        //when
        val result = mapper.mapAddToCartResponse(mockResponse)
        //then
        assertNotEquals("", result.responseJson)
    }

    @Test
    fun mapAddToCartResponseError_sucessIsZero() {
        //given
        val mockResponse = MockResponseProvider.getResponseAtcError()
        //when
        val result = mapper.mapAddToCartResponse(mockResponse)
        //then
        assertEquals(0, result.data.success)
    }

    @Test
    fun mapAddToCartResponseError_responseJsonShouldNotBeEmpty() {
        //given
        val mockResponse = MockResponseProvider.getResponseAtcError()
        //when
        val result = mapper.mapAddToCartResponse(mockResponse)
        //then
        assertNotEquals("", result.responseJson)
    }

}