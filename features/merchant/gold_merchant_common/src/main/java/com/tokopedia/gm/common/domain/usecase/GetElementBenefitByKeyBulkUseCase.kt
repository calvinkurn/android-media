package com.tokopedia.gm.common.domain.usecase

import com.tokopedia.gm.common.domain.model.GetElementBenefitByKeyBulkData
import com.tokopedia.gm.common.domain.model.GetElementBenefitByKeyBulkResponse
import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.GraphqlConstant
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.usecase.RequestParams
import okhttp3.internal.toLongOrDefault
import javax.inject.Inject

/**
 * Created by @ilhamsuaib on 5/3/24.
 */

@GqlQuery("GetElementBenefitByKeyBulkQuery", GetElementBenefitByKeyBulkUseCase.QUERY)
class GetElementBenefitByKeyBulkUseCase @Inject constructor() :
    GraphqlUseCase<GetElementBenefitByKeyBulkResponse>() {

    init {
        setTypeClass(GetElementBenefitByKeyBulkResponse::class.java)
        setGraphqlQuery(GetElementBenefitByKeyBulkQuery())
    }

    suspend fun execute(
        shopId: String,
        elementKeys: List<String>,
        source: String = PARAM_SOURCE_DEFAULT,
        useCache: Boolean = true
    ): GetElementBenefitByKeyBulkData {
        setRequestParams(createParam(shopId, elementKeys, source).parameters)
        setCacheStrategy(createCachedCacheStrategy(useCache))
        val response = executeOnBackground()
        return response.getElementBenefitByElementKeyBulk
    }

    private fun createParam(
        shopId: String,
        elementKeys: List<String>,
        source: String
    ): RequestParams {
        val shopIdLong = shopId.toLongOrDefault(0)
        return RequestParams.create().apply {
            putObject(
                KEY_INPUT, mapOf(
                    KEY_PACKAGE_OWNER_ID to shopIdLong,
                    KEY_PACKAGE_OWNER_TYPE to PARAM_PACKAGE_OWNER_TYPE,
                    KEY_PACKAGE_BENEFICIARY_ID to shopIdLong,
                    KEY_PACKAGE_BENEFICIARY_TYPE to PARAM_PACKAGE_BENEFICIARY_TYPE,
                    KEY_SOURCE to source,
                    KEY_ELEMENT_KEYS to elementKeys
                )
            )
        }
    }

    private fun createCachedCacheStrategy(useCache: Boolean): GraphqlCacheStrategy {
        return if (useCache) {
            GraphqlCacheStrategy.Builder(CacheType.CACHE_FIRST)
                .setExpiryTime(GraphqlConstant.ExpiryTimes.HOUR.`val`())
                .setSessionIncluded(true)
                .build()
        } else {
            GraphqlCacheStrategy.Builder(CacheType.NONE).build()
        }
    }

    companion object {
        private const val KEY_INPUT = "input"
        private const val KEY_PACKAGE_OWNER_ID = "packageOwnerID"
        private const val KEY_PACKAGE_OWNER_TYPE = "packageOwnerType"
        private const val KEY_PACKAGE_BENEFICIARY_ID = "beneficiaryID"
        private const val KEY_PACKAGE_BENEFICIARY_TYPE = "beneficiaryType"
        private const val KEY_SOURCE = "source"
        private const val KEY_ELEMENT_KEYS = "elementKeys"

        private const val PARAM_PACKAGE_OWNER_TYPE = 1
        private const val PARAM_PACKAGE_BENEFICIARY_TYPE = 1
        private const val PARAM_SOURCE_DEFAULT = ""

        internal const val QUERY = """
            query getElementBenefitByElementKeyBulk(${'$'}input: ParamGetElementBenefitByElementKeyBulk!) {
              getElementBenefitByElementKeyBulk(input: ${'$'}input) {
                result {
                  elementKey
                  value
                }
              }
            }
        """

        object Keys {
            const val STATISTIC_PAYWALL_ACCESS = "WPESELLER_PAYWALL_WAWASAN_ACCESS"
        }

        object Sources {
            const val STATISTIC = "android-statistic"
        }
    }
}
