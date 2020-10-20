package com.tokopedia.reputation.common.domain.usecase

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.reputation.common.data.source.cloud.model.ProductrevReviewTabCounterResponseWrapper
import javax.inject.Inject

class ProductrevReviewTabCounterUseCase @Inject constructor(graphqlRepository: GraphqlRepository) : GraphqlUseCase<ProductrevReviewTabCounterResponseWrapper>(graphqlRepository) {

    companion object {

        private val query by lazy {
            """
                query productrevInboxReviewCounter {
                  productrevInboxReviewCounter {
                    count
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