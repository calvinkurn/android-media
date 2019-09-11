package com.tokopedia.atc_common.domain.mapper

import com.tokopedia.atc_common.MockResponseProvider
import org.junit.After
import org.junit.Before
import org.junit.Test

import org.junit.Assert.*

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
    fun mapAddToCartResponseSuccess_statusIsOne() {
        //given
        val mockResponse = MockResponseProvider.getResponseAtcSuccess()
        //when
        val result = mapper.mapAddToCartResponse(mockResponse)
        //then
        assertEquals(1, result.status)
    }

    @Test
    fun mapAddToCartResponseSuccess_responseJsonShouldBeEmpty() {
        //given
        val mockResponse = MockResponseProvider.getResponseAtcSuccess()
        //when
        val result = mapper.mapAddToCartResponse(mockResponse)
        //then
        assertEquals("", result.responseJson)
    }

    @Test
    fun mapAddToCartResponseError_statusIsZero() {
        //given
        val mockResponse = MockResponseProvider.getResponseAtcError()
        //when
        val result = mapper.mapAddToCartResponse(mockResponse)
        //then
        assertEquals(0, result.status)
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