package com.tokopedia.gm.common.domain.interactor

import com.tokopedia.gm.common.data.source.cloud.model.GetPowerMerchantSettingInfoResponse
import com.tokopedia.gm.common.data.source.local.model.PowerMerchantSettingInfoUiModel
import com.tokopedia.gm.common.domain.mapper.PowerMerchantSettingInfoMapper
import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlError
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject

/**
 * Created By @ilhamsuaib on 08/03/21
 */

@GqlQuery("GetPMSettingInfoGqlQuery", GetPMSettingInfoUseCase.QUERY)
class GetPMSettingInfoUseCase @Inject constructor(
    private val gqlRepository: GraphqlRepository,
    private val mapper: PowerMerchantSettingInfoMapper
) : BaseGqlUseCase<PowerMerchantSettingInfoUiModel>() {

    override suspend fun executeOnBackground(): PowerMerchantSettingInfoUiModel {
        val gqlRequest = GraphqlRequest(
            GetPMSettingInfoGqlQuery(),
            GetPowerMerchantSettingInfoResponse::class.java,
            params.parameters
        )
        val gqlResponse = gqlRepository.response(listOf(gqlRequest), cacheStrategy)
        val errors: List<GraphqlError>? =
            gqlResponse.getError(GetPowerMerchantSettingInfoResponse::class.java)

        if (errors.isNullOrEmpty()) {
            val response = gqlResponse.getData<GetPowerMerchantSettingInfoResponse>(
                GetPowerMerchantSettingInfoResponse::class.java
            )
            if (response != null) {
                return mapper.mapRemoteModelToUiModel(response.goldGetPMSettingInfo)
            } else {
                throw RuntimeException("${getClassName()} : returns null")
            }
        } else {
            throw MessageErrorException(errors.firstOrNull()?.message.orEmpty())
        }
    }

    companion object {
        internal const val QUERY = """
           query goldGetPMSettingInfo(${'$'}shopID: Int!, ${'$'}source: String!) {
             goldGetPMSettingInfo(shopID: ${'$'}shopID, source:${'$'}source) {
               period_type
               ticker_list {
                 title
                 text
                 type
               }
             }
           }
        """
        private const val KEY_SHOP_ID = "shopID"
        private const val KEY_SOURCE = "source"

        fun createParams(shopId: String, source: String): RequestParams {
            return RequestParams.create().apply {
                putLong(KEY_SHOP_ID, shopId.toLongOrZero())
                putString(KEY_SOURCE, source)
            }
        }

        fun getCacheStrategy(shouldFromCache: Boolean): GraphqlCacheStrategy {
            val cacheType = if (shouldFromCache) {
                CacheType.CACHE_FIRST
            } else {
                CacheType.ALWAYS_CLOUD
            }
            return GraphqlCacheStrategy.Builder(cacheType).build()
        }
    }
}