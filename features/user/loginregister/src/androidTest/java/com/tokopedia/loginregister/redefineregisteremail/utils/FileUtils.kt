package com.tokopedia.loginregister.redefineregisteremail.utils

import androidx.test.platform.app.InstrumentationRegistry
import com.google.gson.Gson
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.test.application.graphql.GqlMockUtil
import com.tokopedia.test.application.util.InstrumentationMockHelper
import java.io.StringReader
import java.lang.reflect.Type

object FileUtils {

    inline fun <reified DataModel : Any> createResponseFromJson(resourceJson: Int): GraphqlResponse {
        val response = parseRaw<DataModel>(resourceJson, DataModel::class.java)
        return GqlMockUtil.createSuccessResponse(response)
    }

    fun <T> parseRaw(
        resId: Int,
        typeOfT: Type
    ): T {
        val stringFile = fileContent(resId)
        return fromJson(stringFile, typeOfT)
    }

    private fun fileContent(resId: Int): String {
        val context = InstrumentationRegistry
            .getInstrumentation()
            .context
        return InstrumentationMockHelper.getRawString(
            context,
            resId
        )
    }

    private fun <T> fromJson(json: String?, typeOfT: Type?): T {
        if (json == null) {
            throw NullPointerException()
        }
        val reader = StringReader(json)
        return Gson().fromJson(reader, typeOfT)
    }

}
