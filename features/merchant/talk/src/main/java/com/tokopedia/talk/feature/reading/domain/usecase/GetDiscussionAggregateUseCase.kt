package com.tokopedia.talk.feature.reading.domain.usecase

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.talk.feature.reading.data.model.DiscussionAggregate
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject

class GetDiscussionAggregateUseCase @Inject constructor(graphqlRepository: GraphqlRepository) : GraphqlUseCase<DiscussionAggregate>(graphqlRepository) {

    companion object {
        val query = """
            query discussionAggregateByProductID(productID: Int!) {
                productName
                thumbnail
                category {
                    name
                    text
                    counter
                }
            }
        """.trimIndent()
        const val PRODUCT_ID_PARAM = "productID"
    }

    fun setParams(productId: Int) {
        val requestParams = RequestParams()
        requestParams.putInt(PRODUCT_ID_PARAM, productId)
        setGraphqlQuery(query)
        setRequestParams(requestParams.parameters)
        setTypeClass(DiscussionAggregate::class.java)
    }

}