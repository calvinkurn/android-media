package com.tokopedia.product.util

import com.tokopedia.graphql.CommonUtils
import java.io.File
import java.lang.reflect.Type


/**
 * Created by Yehezkiel on 01/04/20
 */
object JsonFormatter {

    fun getJsonFromFile(path: String): String {
        val uri = ClassLoader.getSystemClassLoader().getResource(path)
        val file = File(uri.path)
        return String(file.readBytes())
    }

    fun <T> createMockGraphqlSuccessResponse(jsonLocation: String, typeOfClass: Type): T {
        return CommonUtils.fromJson(
                getJsonFromFile(jsonLocation),
                typeOfClass) as T
    }

}



