package com.tokopedia.talk.feature.reading.domain.usecase

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.talk.feature.reading.data.model.discussionaggregate.DiscussionAggregateResponse
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject

class GetDiscussionAggregateUseCase @Inject constructor(graphqlRepository: GraphqlRepository) : GraphqlUseCase<DiscussionAggregateResponse>(graphqlRepository) {

    companion object {
        const val PARAM_PRODUCT_ID = "productID"
        const val PARAM_SHOP_ID = "shopID"
        private val query by lazy {
            val productID = "\$productID"
            val shopID = "\$shopID"
            """
            query discussionAggregateByProductID($productID: String!, $shopID: String) {
                discussionAggregateByProductID(productID: $productID, shopID: $shopID) {
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
        """.trimIndent()
        }
    }

    init {
        setGraphqlQuery(query)
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