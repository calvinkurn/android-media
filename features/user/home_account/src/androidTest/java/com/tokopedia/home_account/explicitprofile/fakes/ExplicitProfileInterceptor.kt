package com.tokopedia.home_account.explicitprofile.fakes

import android.content.Context
import com.tokopedia.home_account.explicitprofile.common.BaseExplicitProfileInterceptor
import com.tokopedia.home_account.test.R
import com.tokopedia.test.application.util.InstrumentationMockHelper
import okhttp3.Interceptor
import okhttp3.Response

class ExplicitProfileInterceptor constructor(
    private val context: Context
): BaseExplicitProfileInterceptor() {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder().build()
        val query = chain.request().headers[HEADER_GQL_QUERY].orEmpty()

        return when {
            query.contains(Query.GET_CATEGORIES) -> {
                mockResponse(request, getRaw(RawResponse.GET_CATEGORY))
            }
            query.contains(Query.GET_QUESTION) -> {
                mockResponse(request, getRaw(RawResponse.GET_QUESTION))
            }
            query.contains(Query.SAVE_MULTIPLE_ANSWER) -> {
                mockResponse(request, getRaw(RawResponse.SAVE_MULTIPLE_ANSWER))
            }
            else -> {
                chain.proceed(chain.request())
            }
        }
    }

    private fun getRaw(raw: Int) : String {
        return InstrumentationMockHelper.getRawString(context, raw)
    }

    companion object {
        const val HEADER_GQL_QUERY = "x-tkpd-gql-query"

        object Query {
            const val GET_CATEGORIES = "explicitprofileGetAllCategories"
            const val GET_QUESTION = "explicitprofileGetQuestion"
            const val SAVE_MULTIPLE_ANSWER = "explicitprofileSaveMultiAnswers"
        }

        object RawResponse {
            val GET_CATEGORY = R.raw.explicit_profile_get_categories
            val GET_QUESTION = R.raw.explicit_profile_get_questions
            val SAVE_MULTIPLE_ANSWER = R.raw.explicit_profile_save_multiple_answer
        }
    }
}