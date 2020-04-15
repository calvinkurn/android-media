package com.tokopedia.talk.feature.reading.domain.usecase

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.talk.feature.reading.data.model.DiscussionAggregate
import com.tokopedia.talk.feature.reading.data.model.DiscussionAggregateResponse
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject

class GetDiscussionAggregateUseCase @Inject constructor(graphqlRepository: GraphqlRepository) : GraphqlUseCase<DiscussionAggregateResponse>(graphqlRepository) {

    companion object {
        const val PARAM_PRODUCT_ID = "productID"
        private val query by lazy {
            val productID = "\$productID"
            """
            query discussionAggregateByProductID($productID: Int!) {
                discussionAggregateByProductID(productID: $productID) {
                    productName
                    thumbnail
                    category {
                        name
                        text
                        counter
                    }
                }
            }
        """.trimIndent()
        }
    }

    init {
        setGraphqlQuery(query)
        setTypeClass(DiscussionAggregateResponse::class.java)
    }

    fun setParams(productId: Int) {
        val requestParams = RequestParams()
        requestParams.putInt(PARAM_PRODUCT_ID, productId)
        setRequestParams(requestParams.parameters)
    }

}