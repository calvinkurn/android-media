package com.tokopedia.home_recom.model.entity

import com.google.gson.Gson
import junit.framework.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

/**
 * Created by Lukas on 2019-07-13
 */
@RunWith(JUnit4::class)
class PrimaryProductEntityTest{

    private val gson = Gson()
    private val successJson = "primary_product_success_response.json"
    private val errorJson = "primary_product_error_response.json"

    @Test
    fun testSuccessConvertFromJsonToObject(){
        val json = this.javaClass.classLoader?.getResourceAsStream(successJson)?.readBytes()?.toString(Charsets.UTF_8)
        val response = gson.fromJson(json, PrimaryProductEntity::class.java)
        assertNotSame(response.productRecommendationProductDetail.data.size, 0)
        assertNotSame(response.productRecommendationProductDetail.data[0].recommendation.size, 0)
        assertNotNull(response.productRecommendationProductDetail.data[0].recommendation[0].id)
    }

    @Test
    fun testProductIdNotFound(){
        val json = this.javaClass.classLoader?.getResourceAsStream(errorJson)?.readBytes()?.toString(Charsets.UTF_8)
        val response = gson.fromJson(json, PrimaryProductEntity::class.java)
        assertNotSame(response.productRecommendationProductDetail.data.size, 0)
        assertEquals(response.productRecommendationProductDetail.data[0].recommendation.size, 0)
    }
}