package com.tokopedia.digital_product_detail.domain.usecase

import com.tokopedia.common.topupbills.data.requests.DigiPersoRequestParam
import com.tokopedia.digital_product_detail.data.model.data.DigitalDigiPersoGetPersonalizedItem
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject

class GetRechargeRecommendationUseCase @Inject constructor(
    private val graphqlRepository: GraphqlRepository
): GraphqlUseCase<DigitalDigiPersoGetPersonalizedItem>(graphqlRepository) {

    private var params: RequestParams = RequestParams.EMPTY

    override suspend fun executeOnBackground(): DigitalDigiPersoGetPersonalizedItem {
        val gqlRequest = GraphqlRequest(QUERY, DigitalDigiPersoGetPersonalizedItem::class.java, params.parameters)
        val gqlResponse = graphqlRepository.response(listOf(gqlRequest), GraphqlCacheStrategy
            .Builder(CacheType.ALWAYS_CLOUD).build())

        val error = gqlResponse.getError(DigitalDigiPersoGetPersonalizedItem::class.java)
        if (error == null || error.isEmpty()){
            return (gqlResponse.getData(DigitalDigiPersoGetPersonalizedItem::class.java) as DigitalDigiPersoGetPersonalizedItem)
        } else {
            throw MessageErrorException(error.mapNotNull { it.message }.toString())
        }
    }

    fun createParams(clientNumbers: List<String>, dgCategoryIds: List<Int>) {
        params = RequestParams.create().apply {
            putObject(
                KEY_DIGI_PERSO_INPUT, DigiPersoRequestParam(
                    channelName = CHANNEL_NAME,
                    clientNumbers = clientNumbers,
                    dgCategoryIDs = dgCategoryIds,
                    pgCategoryIDs = emptyList(),
                    dgOperatorIds = emptyList()
                )
            )
        }
    }

    companion object {
        private const val KEY_DIGI_PERSO_INPUT = "input"
        private const val CHANNEL_NAME = "recharge_pdp_last_trx_client_number"
        private const val QUERY = """
        query digiPersoGetPersonalizedItems(${'$'}input: DigiPersoGetPersonalizedItemsRequest!) {
          digiPersoGetPersonalizedItems(input: ${'$'}input) {
            title
            mediaURL
            appLink
            webLink
            textLink
            items {
              id
              title
              mediaURL
              mediaUrlType
              appLink
              webLink
              label1
              label2
              price
              slashedPrice
              discount
              backgroundColor
              trackingData {
                productID
                operatorID
                businessUnit
                itemLabel
                itemType
                clientNumber
                categoryID
                categoryName
              }
            }
          }
        }
        """
    }
}