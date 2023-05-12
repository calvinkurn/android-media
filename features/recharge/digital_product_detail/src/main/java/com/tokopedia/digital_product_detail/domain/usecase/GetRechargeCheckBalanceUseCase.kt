package com.tokopedia.digital_product_detail.domain.usecase

import com.tokopedia.common.topupbills.data.requests.DigiPersoRequestParam
import com.tokopedia.digital_product_detail.data.model.data.DigitalPersoData
import com.tokopedia.digital_product_detail.domain.usecase.GetRechargeCheckBalanceUseCase.Companion.QUERY_NAME_RECHARGE_CHECK_BALANCE
import com.tokopedia.digital_product_detail.domain.usecase.GetRechargeCheckBalanceUseCase.Companion.QUERY_RECHARGE_CHECK_BALANCE
import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import javax.inject.Inject

@GqlQuery(QUERY_NAME_RECHARGE_CHECK_BALANCE, QUERY_RECHARGE_CHECK_BALANCE)
class GetRechargeCheckBalanceUseCase @Inject constructor(
    graphqlRepository: GraphqlRepository
): GraphqlUseCase<DigitalPersoData>(graphqlRepository) {

    init {
        setTypeClass(DigitalPersoData::class.java)
        setCacheStrategy(GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build())
        setGraphqlQuery(RechargeCheckBalanceQuery())
    }

    fun setParams(
        clientNumbers: List<String>,
        dgCategoryIds: List<Int>,
        dgOperatorIds: List<Int>,
        channelName: String,
    ) {
        val params = mapOf(
            KEY_DIGI_PERSO_INPUT to DigiPersoRequestParam(
                channelName = channelName,
                clientNumbers = clientNumbers,
                dgCategoryIDs = dgCategoryIds,
                pgCategoryIDs = emptyList(),
                dgOperatorIds = dgOperatorIds
            )
        )
        setRequestParams(params)
    }

    override suspend fun executeOnBackground(): DigitalPersoData {
        return super.executeOnBackground()
    }

    companion object {
        private const val KEY_DIGI_PERSO_INPUT = "input"

        const val QUERY_NAME_RECHARGE_CHECK_BALANCE = "RechargeCheckBalanceQuery"
        const val QUERY_RECHARGE_CHECK_BALANCE = """
            query rechargeCheckBalanceOtp(${'$'}input: DigiPersoGetPersonalizedItemsRequest!) {
              digiPersoGetPersonalizedItems(input: ${'$'}input) {
                items {
                  title
                  campaignLabelText
                  campaignLabelTextColor
                  iconURL
                  widgets {
                    title
                    subtitle
                    iconURL
                  }
                  products {
                    title
                    subtitle
                    subtitleColor
                    applink
                    weblink
                  }
                }
                __typename
              }
            }
        """
    }
}
