package com.tokopedia.product.util

import com.tokopedia.graphql.CommonUtils
import com.tokopedia.product.viewmodel.DynamicProductDetailViewModelTest
import com.tokopedia.recommendation_widget_common.data.RecomendationEntity
import java.io.File

/**
 * Created by Yehezkiel on 01/04/20
 */
object JsonFormatter {

    inline fun <reified T> createMockGraphqlSuccessResponse(jsonLocation: String, typeOfT: Class<T>): T {
        return CommonUtils.fromJson(
                getJsonFromFile(jsonLocation),
                typeOfT) as T
    }

    fun getJsonFromFile(path: String): String {
        val uri = ClassLoader.getSystemClassLoader().getResource(path)
        val file = File(uri.path)
        return String(file.readBytes())
    }

}
