package com.tokopedia.review.feature.createreputation.domain.usecase

import com.tokopedia.abstraction.common.network.exception.MessageErrorException
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.review.feature.createreputation.model.ProductRevGetForm
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject

class GetProductReputationForm @Inject constructor(private val graphqlRepository: GraphqlRepository) {

    companion object {
        const val REPUTATION_ID = "reputationId"
        const val PRODUCT_ID = "productId"

        fun createRequestParam(reputationId: Int, productId: Int): RequestParams {
            return RequestParams.create().apply {
                putInt(REPUTATION_ID, reputationId)
                putInt(PRODUCT_ID, productId)
            }
        }
    }

    private val query by lazy {
        """
            query productrevGetForm(${'$'}reputationId:Int!,${'$'}productId:Int!){
              productrevGetForm(reputationID:${'$'}reputationId, productID:${'$'}productId){
                reputationID
                orderID
                validToReview
                productData{
                  productID
                  productName
                  productPageURL
                  productImageURL
                  productStatus
                  productVariant {
                    variantID
                    variantName
                  }
                }
                shopData{
                  shopID
                  shopOpen
                  shopName
                }
                userData{
                  userID
                  userName
                  userStatus
                }
                reputationData{
                  score
                  locked
                  filled
                }
              }
            }
        """.trimIndent()
    }

    var forceRefresh = true

    suspend fun getReputationForm(requestParams: RequestParams): ProductRevGetForm {
        val cacheStrategy = GraphqlCacheStrategy.Builder(if (forceRefresh) CacheType.ALWAYS_CLOUD else CacheType.CACHE_FIRST).build()
        val graphqlRequest = GraphqlRequest(query, ProductRevGetForm::class.java, requestParams.parameters)

        val response = graphqlRepository.getReseponse(listOf(graphqlRequest), cacheStrategy)

        val data: ProductRevGetForm? = response.getData(ProductRevGetForm::class.java)
        val error= response.getError(ProductRevGetForm::class.java)

        if (data == null) {
            throw RuntimeException()
        } else if (error != null && error.isNotEmpty() && error.first().message.isNotEmpty()) {
            throw MessageErrorException(error.first().message)
        }

        return data
    }
}