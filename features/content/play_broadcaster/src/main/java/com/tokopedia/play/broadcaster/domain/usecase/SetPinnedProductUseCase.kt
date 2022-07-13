package com.tokopedia.play.broadcaster.domain.usecase

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.play.broadcaster.domain.model.SetPinnedProduct
import com.tokopedia.play_common.domain.usecase.RetryableGraphqlUseCase
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * @author by astidhiyaa on 13/07/22
 */
@GqlQuery(SetPinnedProductUseCase.QUERY_NAME, SetPinnedProductUseCase.QUERY)
class SetPinnedProductUseCase @Inject constructor(
    private val dispatchers: CoroutineDispatchers,
    graphqlRepository: GraphqlRepository
) : RetryableGraphqlUseCase<SetPinnedProduct>(graphqlRepository) {

    init {
        setGraphqlQuery(SetPinnedProductUseCaseQuery())
        setCacheStrategy(
            GraphqlCacheStrategy
                .Builder(CacheType.ALWAYS_CLOUD).build())
        setTypeClass(SetPinnedProduct::class.java)
    }

    override suspend fun executeOnBackground(): SetPinnedProduct = withContext(dispatchers.io) {
        executeOnBackground()
    }

    fun createParam(channelId: String, productId: String): Map<String, Any> {
        return mapOf(
            PARAM_CHANNEL_ID to channelId,
            PARAM_PRODUCT_ID to productId,
        )
    }

    companion object {
        private const val PARAM_CHANNEL_ID = "channelID"
        private const val PARAM_PRODUCT_ID = "productID"
        const val QUERY_NAME = "SetPinnedProductUseCaseQuery"
        const val QUERY = """
            broadcasterSetPinnedProductTag(
⠀⠀                  (${"$${PARAM_CHANNEL_ID}"}: Int64!,
⠀⠀                  (${"$${PARAM_PRODUCT_ID}"}: Int64
            ){
⠀⠀              success
            }
            """
    }
}