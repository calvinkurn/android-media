package com.tokopedia.notifications.data

import android.os.Bundle
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.tokopedia.atc_common.domain.usecase.AddToCartUseCase
import com.tokopedia.notifications.common.PayloadConverter.convertToBaseModel
import com.tokopedia.notifications.domain.AttributionUseCase
import com.tokopedia.notifications.generalResponse
import com.tokopedia.notifications.testResponse
import com.tokopedia.notifications.util.assertIsEqualsTo
import io.mockk.every
import io.mockk.mockk
import org.junit.Before
import org.junit.Test

class DataManagerTest {

    private val attributionUseCase: AttributionUseCase = mockk()
    private val atcProductUseCase: AddToCartUseCase = mockk()

    private lateinit var dataManager: DataManager

    @Before fun setUp() {
        dataManager = DataManager(
                attributionUseCase = attributionUseCase,
                atcProductUseCase = atcProductUseCase
        )
    }

    @Test fun `convert general response json to map`() {
        val mapType = object : TypeToken<Map<String?, Any>>() {}.type
        val convertToMap = Gson().fromJson<Map<String, Any>>(generalResponse, mapType)

        convertToMap.size assertIsEqualsTo 22
    }

    @Test fun `convert map to bundle`() {
        val expectedTitleValue = "8966870"

        testResponse.forEach {  response ->
            val mockedBundle = mockk<Bundle>(relaxed = true)
            val mapType = object : TypeToken<Map<String?, String>>() {}.type
            val convertToMap = Gson().fromJson<Map<String, String>>(response, mapType)

            for ((key, value) in convertToMap) {
                every {
                    mockedBundle.getString(key)
                } returns value
            }

            mockedBundle.getString("userId")?.assertIsEqualsTo(expectedTitleValue)
        }
    }

    @Test fun `convert bundle to baseModel`() {
        val expectedTitleValue = "Title 3"
        val mockedBundle = mockk<Bundle>(relaxed = true)
        val mapType = object : TypeToken<Map<String?, String>>() {}.type
        val convertToMap = Gson().fromJson<Map<String, String>>(generalResponse, mapType)

        for ((key, value) in convertToMap) {
            every {
                mockedBundle.getString(key)
            } returns value
        }

        val convertToModel = convertToBaseModel(mockedBundle)

        convertToModel.title?.assertIsEqualsTo(expectedTitleValue)
    }

}