package com.tokopedia.kyc_centralized.fakes

import androidx.test.core.app.ApplicationProvider
import com.google.gson.Gson
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.kyc_centralized.data.model.KycProjectInfo
import com.tokopedia.kyc_centralized.test.R
import com.tokopedia.test.application.graphql.GqlMockUtil
import com.tokopedia.test.application.graphql.GqlQueryParser
import com.tokopedia.test.application.util.InstrumentationMockHelper.getRawString
import com.tokopedia.kyc_centralized.data.model.KycUserProjectInfoPojo
import javax.inject.Inject

class FakeGraphqlRepository @Inject constructor() : GraphqlRepository {

    var infoCount = 0

    override suspend fun response(
        requests: List<GraphqlRequest>,
        cacheStrategy: GraphqlCacheStrategy,
    ): GraphqlResponse {
        return when (GqlQueryParser.parse(requests).first()) {
            "kycProjectInfo" -> {
                val obj = when (infoCount) {
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
                kycProjectInfo = KycProjectInfo(
                    isSelfie = false,
                    status = 0,
                    reasonList = arrayListOf())
            }
        }
    }
}
