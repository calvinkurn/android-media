package com.tokopedia.sellerorder.list.domain.usecases

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.sellerorder.list.domain.mapper.HeaderIconsInfoMapper
import com.tokopedia.sellerorder.list.domain.model.SomListHeaderIconsInfoResponse
import com.tokopedia.sellerorder.list.presentation.models.SomListHeaderIconsInfoUiModel
import javax.inject.Inject

class SomListGetHeaderIconsInfoUseCase @Inject constructor(
        private val gqlRepository: GraphqlRepository,
        private val mapper: HeaderIconsInfoMapper
) : BaseGraphqlUseCase<SomListHeaderIconsInfoUiModel>(gqlRepository) {

    override suspend fun executeOnBackground(): SomListHeaderIconsInfoUiModel {
        return executeOnBackground(false)
    }

    override suspend fun executeOnBackground(useCache: Boolean): SomListHeaderIconsInfoUiModel {
        val cacheStrategy = getCacheStrategy(useCache)
        val gqlRequest = GraphqlRequest(QUERY, SomListHeaderIconsInfoResponse.Data::class.java, params.parameters)
        val gqlResponse = gqlRepository.response(listOf(gqlRequest), cacheStrategy)

        val errors = gqlResponse.getError(SomListHeaderIconsInfoResponse.Data::class.java)
        if (errors.isNullOrEmpty()) {
            val response = gqlResponse.getData<SomListHeaderIconsInfoResponse.Data>()
            return mapper.mapResponseToUiModel(response.orderFilterSom)
        } else {
            throw RuntimeException(errors.joinToString(", ") { it.message })
        }
    }

    companion object {
        private val QUERY = """
            query GetWaitingPaymentCounter {
              orderFilterSom {
                waiting_payment_counter {
                  text
                  amount
                }
                seller_info{
                  plus_logo
                  edu_url
                }
              }
            }
        """.trimIndent()
    }
}