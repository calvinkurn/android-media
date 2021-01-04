package com.tokopedia.sellerorder.filter.domain.usecase

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.sellerorder.filter.domain.SomFilterResponse
import com.tokopedia.sellerorder.filter.domain.mapper.GetSomFilterMapper
import com.tokopedia.sellerorder.filter.presentation.model.BaseSomFilter
import javax.inject.Inject

class GetSomOrderFilterUseCase @Inject constructor(repository: GraphqlRepository): GraphqlUseCase<SomFilterResponse>(repository) {

    init {
        setGraphqlQuery(QUERY)
        setTypeClass(SomFilterResponse::class.java)
    }

    suspend fun execute(): List<BaseSomFilter> {
        return GetSomFilterMapper.mapToSomFilterVisitable(executeOnBackground())
    }

    companion object {
        val QUERY = """
            query OrderFilterSom {
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
                shipping_list{
                  shipping_id
                  shipping_code
                  shipping_name
                  shipping_desc
                  status
                  img_logo
                }
              }
              orderTypeList {
                id
                key
                name
              }
            }
        """.trimIndent()
    }


}