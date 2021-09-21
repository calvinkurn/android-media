package com.tokopedia.sellerorder.list.domain.usecases

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.sellerorder.list.domain.mapper.FilterResultMapper
import com.tokopedia.sellerorder.list.domain.model.SomListFilterResponse
import com.tokopedia.sellerorder.list.presentation.models.SomListFilterUiModel
import javax.inject.Inject

/**
 * Created by fwidjaja on 08/05/20.
 */
class SomListGetFilterListUseCase @Inject constructor(
        private val gqlRepository: GraphqlRepository,
        private val mapper: FilterResultMapper
) : BaseGraphqlUseCase<SomListFilterUiModel>(gqlRepository) {

    override suspend fun executeOnBackground(): SomListFilterUiModel {
        return executeOnBackground(false)
    }

    override suspend fun executeOnBackground(useCache: Boolean): SomListFilterUiModel {
        val cacheStrategy = getCacheStrategy(useCache)
        val gqlRequest = GraphqlRequest(QUERY, SomListFilterResponse.Data::class.java)
        val gqlResponse = gqlRepository.getReseponse(listOf(gqlRequest), cacheStrategy)

        val errors = gqlResponse.getError(SomListFilterResponse.Data::class.java)
        if (errors.isNullOrEmpty()) {
            val response = gqlResponse.getData<SomListFilterResponse.Data>()
            return mapper.mapResponseToUiModel(response.orderFilterSom, useCache)
        } else {
            throw RuntimeException(errors.joinToString(", ") { it.message })
        }
    }

    companion object {
        private val QUERY = """
            query GetOrderFilterSom {
              orderFilterSom {
                status_list {
                  order_status_id
                  key
                  order_status
                  order_status_amount
                  is_checked
                  child_status {
                    id
                    key
                    text
                    amount
                    is_checked
                  }
                }
              }
              status
            }
        """.trimIndent() //Don't remove `status` field since it's necessary for refresh token flow
    }
}