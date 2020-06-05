package com.tokopedia.reputation.common.domain.usecase

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.reputation.common.data.model.ProductrevReviewTabCounterResponseWrapper
import javax.inject.Inject

class ProductrevReviewTabCounterUseCase @Inject constructor(graphqlRepository: GraphqlRepository) : GraphqlUseCase<ProductrevReviewTabCounterResponseWrapper>(graphqlRepository) {

    companion object {

        private val query by lazy {
            """
                query{
                  productrevReviewTabCounter{
                    list{
                      count
                      tabName
                    }
                  }
                }
            """.trimIndent()
        }
    }

    init {
        setGraphqlQuery(query)
        setTypeClass(ProductrevReviewTabCounterResponseWrapper::class.java)
    }
}