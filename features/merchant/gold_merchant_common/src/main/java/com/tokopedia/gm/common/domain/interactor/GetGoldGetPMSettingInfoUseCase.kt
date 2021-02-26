package com.tokopedia.gm.common.domain.interactor

import com.tokopedia.gm.common.data.source.cloud.model.PMPeriodTypeResponse
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import javax.inject.Inject

class GetGoldGetPMSettingInfoUseCase @Inject constructor(
        graphqlRepository: GraphqlRepository
) : GraphqlUseCase<PMPeriodTypeResponse>(graphqlRepository) {

    companion object {
        const val SHOP_ID = "shopID"
        val PM_SETTING_INFO_QUERY = """
            query goldGetPMSettingInfo(${'$'}shopID: Int!){
              goldGetPMSettingInfo(shopID: ${'$'}shopID, source: "goldmerchant") {
                data{
                  period_type
                }
              }
            }
        """.trimIndent()

        @JvmStatic
        fun createParams(shopID: Int): Map<String, Any> = mapOf(SHOP_ID to shopID)
    }

    init {
        setGraphqlQuery(PM_SETTING_INFO_QUERY)
        setTypeClass(PMPeriodTypeResponse::class.java)
    }
}