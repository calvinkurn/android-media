package com.tokopedia.kyc_centralized.di

import androidx.test.core.app.ApplicationProvider
import com.google.gson.Gson
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.kyc_centralized.test.R
import com.tokopedia.test.application.graphql.GqlMockUtil
import com.tokopedia.test.application.graphql.GqlQueryParser
import com.tokopedia.test.application.util.InstrumentationMockHelper.getRawString
import com.tokopedia.user_identification_common.domain.pojo.KycUserProjectInfoPojo

class FakeGraphqlRepository : GraphqlRepository {

    var infoCount = 0

    override suspend fun response(
        requests: List<GraphqlRequest>,
        cacheStrategy: GraphqlCacheStrategy
    ): GraphqlResponse {
        return when (GqlQueryParser.parse(requests).first()) {
            "kycProjectInfo" -> {
                val obj = when(infoCount) {
                    0 -> MockProvider.notRegisteredUser()
                    else -> MockProvider.pending()
                }
                infoCount += 1
                GqlMockUtil.createSuccessResponse(obj)
            }
            else -> throw IllegalArgumentException()
        }
    }

    private object MockProvider {
        fun notRegisteredUser(): KycUserProjectInfoPojo {
            val content = getRawString(
                ApplicationProvider.getApplicationContext(),
                R.raw.get_project_info_not_verified
            )
            return Gson().fromJson(content, KycUserProjectInfoPojo::class.java)
        }

        fun pending(): KycUserProjectInfoPojo {
            return KycUserProjectInfoPojo().apply {
                kycProjectInfo = KycProjectInfo().apply {
                    isSelfie = false
                    status = 0
                    setReasonList(arrayListOf())
                }
            }
        }
    }

}