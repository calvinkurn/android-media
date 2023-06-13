package com.tokopedia.product_ar.util

import com.google.gson.Gson
import com.tokopedia.graphql.CommonUtils
import com.tokopedia.product_ar.model.ProductArResponse
import com.tokopedia.product_ar.model.ProductArUiModel
import java.io.File
import java.lang.reflect.Type

object TestUtil {

    private const val MOCK_PRODUCT_AR_LOCATION = "json/gql_get_product_ar.json"

    fun getProductArDataMock(gson: Gson): ProductArUiModel {
        val mockData: ProductArResponse = createMockGraphqlSuccessResponse(MOCK_PRODUCT_AR_LOCATION, ProductArResponse::class.java)
        return ProductArUseCaseMapperTest.mapIntoUiModel(mockData, gson)
    }

    private fun <T> createMockGraphqlSuccessResponse(jsonLocation: String, typeOfClass: Type): T {
        return CommonUtils.fromJson(
                getJsonFromFile(jsonLocation),
                typeOfClass) as T
    }

    private fun getJsonFromFile(path: String): String {
        val uri = ClassLoader.getSystemClassLoader().getResource(path)
        val file = File(uri.path)
        return String(file.readBytes())
    }
}