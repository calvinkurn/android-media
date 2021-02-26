package com.tokopedia.gm.common.domain.interactor

import com.tokopedia.gm.common.data.source.cloud.model.PMPeriodTypeResponse
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import javax.inject.Inject

class GetPMPeriodTypeUseCase @Inject constructor(
        graphqlRepository: GraphqlRepository
) : GraphqlUseCase<PMPeriodTypeResponse>(graphqlRepository) {

    companion object {
        const val SHOP_ID = "shopID"
        const val SOURCE = "source"
        const val GOLD_MERCHANT_SOURCE = "goldmerchant"
        val PM_SETTING_INFO_QUERY = """
            query goldGetPMSettingInfo(${'$'}shopID: Int!, ${'$'}source: String!){
              goldGetPMSettingInfo(shopID: ${'$'}shopID, source: ${'$'}source) {
                data{
                  period_type
                }
              }
            }
        """.trimIndent()

        @JvmStatic
        fun createParams(shopID: Int): Map<String, Any> = mapOf(SHOP_ID to shopID, SOURCE to GOLD_MERCHANT_SOURCE)
    }

    init {
        setGraphqlQuery(PM_SETTING_INFO_QUERY)
        setTypeClass(PMPeriodTypeResponse::class.java)
    }
}