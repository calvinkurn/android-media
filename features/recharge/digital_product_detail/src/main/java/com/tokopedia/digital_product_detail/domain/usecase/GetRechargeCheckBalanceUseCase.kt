package com.tokopedia.digital_product_detail.domain.usecase

import com.tokopedia.common.topupbills.data.requests.DigiPersoRequestParam
import com.tokopedia.digital_product_detail.data.model.data.DigitalDigiPersoGetPersonalizedItem
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
) : GraphqlUseCase<DigitalDigiPersoGetPersonalizedItem>(graphqlRepository) {

    init {
        setTypeClass(DigitalDigiPersoGetPersonalizedItem::class.java)
        setCacheStrategy(GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build())
        setGraphqlQuery(RechargeCheckBalanceQuery())
    }

    fun setParams(
        clientNumbers: List<String>,
        dgCategoryIds: List<Int>,
        dgOperatorIds: List<Int>,
        channelName: String
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

    companion object {
        private const val KEY_DIGI_PERSO_INPUT = "input"

        const val QUERY_NAME_RECHARGE_CHECK_BALANCE = "RechargeCheckBalanceQuery"
        const val QUERY_RECHARGE_CHECK_BALANCE = """
            query digiPersoGetPersonalizedItems(
              ${'$'}input: DigiPersoGetPersonalizedItemsRequest!
            ) {
              digiPersoGetPersonalizedItems(input: ${'$'}input) {
                 items {
                  title
                  subtitle
                  iconURL
                  mediaURL
                  label1
                  textLink
                  appLink
                  mediaURL
                  mediaUrlType
                  campaignLabelText
                  campaignLabelTextColor
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
                    buttonText
                    productID
                    price
                  }
                 }
              }
            }
        """
    }
}
