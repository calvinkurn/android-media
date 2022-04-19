package com.tokopedia.talk.feature.reading.domain.usecase

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.talk.feature.reading.data.model.discussionaggregate.DiscussionAggregateResponse
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject

class GetDiscussionAggregateUseCase @Inject constructor(graphqlRepository: GraphqlRepository) : GraphqlUseCase<DiscussionAggregateResponse>(graphqlRepository) {

    companion object {
        const val PARAM_PRODUCT_ID = "productID"
        const val PARAM_SHOP_ID = "shopID"
        private const val TALK_DISCUSSION_AGGREGATE_QUERY_CLASS_NAME = "TalkDiscussionAggregate"
        private const val query =
        """
            query discussionAggregateByProductID(${'$'}productID: String!, ${'$'}shopID: String) {
                discussionAggregateByProductID(productID: ${'$'}productID, shopID: ${'$'}shopID) {
                    productName
                    thumbnail
                    url
                    category {
                        name
                        text
                        counter
                    }
                }
            }
        """
    }

    init {
        setupUseCase()
    }

    @GqlQuery(TALK_DISCUSSION_AGGREGATE_QUERY_CLASS_NAME, query)
    private fun setupUseCase() {
        setGraphqlQuery(TalkDiscussionAggregate.GQL_QUERY)
        setTypeClass(DiscussionAggregateResponse::class.java)
    }

    fun setParams(productId: String, shopId: String) {
        setRequestParams(
                RequestParams.create().apply {
                    putString(PARAM_PRODUCT_ID, productId)
                    putString(PARAM_SHOP_ID, shopId)
                }.parameters
        )
    }

}