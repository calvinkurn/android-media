package com.tokopedia.gm.common.domain.interactor

import com.tokopedia.gm.common.data.source.cloud.model.GetPowerMerchantSettingInfoResponse
import com.tokopedia.gm.common.data.source.local.model.PowerMerchantSettingInfoUiModel
import com.tokopedia.gm.common.domain.mapper.PowerMerchantSettingInfoMapper
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlError
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject

/**
 * Created By @ilhamsuaib on 08/03/21
 */

class GetPMSettingInfoUseCase @Inject constructor(
        private val gqlRepository: GraphqlRepository,
        private val mapper: PowerMerchantSettingInfoMapper
) : BaseGqlUseCase<PowerMerchantSettingInfoUiModel>() {

    override suspend fun executeOnBackground(): PowerMerchantSettingInfoUiModel {
        val gqlRequest = GraphqlRequest(QUERY, GetPowerMerchantSettingInfoResponse::class.java, params.parameters)
        val gqlResponse = gqlRepository.getReseponse(listOf(gqlRequest), cacheStrategy)
        val errors: List<GraphqlError>? = gqlResponse.getError(GetPowerMerchantSettingInfoResponse::class.java)

        if (errors.isNullOrEmpty()) {
            val response = gqlResponse.getData<GetPowerMerchantSettingInfoResponse>(GetPowerMerchantSettingInfoResponse::class.java)
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
        private const val KEY_SHOP_ID = "shopID"
        private const val KEY_SOURCE = "source"
        private val QUERY = """
           query goldGetPMSettingInfo(${'$'}shopID: Int!, ${'$'}source: String!) {
             goldGetPMSettingInfo(shopID: ${'$'}shopID, source:${'$'}source) {
               period_type
               period_type_pm_pro
               ticker_list {
                 title
                 text
                 type
               }
             }
           }
        """.trimIndent()

        fun createParams(shopId: String, source: String): RequestParams {
            return RequestParams.create().apply {
                putLong(KEY_SHOP_ID, shopId.toLongOrZero())
                putString(KEY_SOURCE, source)
            }
        }
    }
}