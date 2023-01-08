package com.tokopedia.play.broadcaster.domain.usecase

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.play.broadcaster.domain.model.SetPinnedProduct
import com.tokopedia.play.broadcaster.ui.model.product.ProductUiModel
import com.tokopedia.play_common.domain.usecase.RetryableGraphqlUseCase
import javax.inject.Inject

/**
 * @author by astidhiyaa on 13/07/22
 */
@GqlQuery(SetPinnedProductUseCase.QUERY_NAME, SetPinnedProductUseCase.QUERY)
class SetPinnedProductUseCase @Inject constructor(
    graphqlRepository: GraphqlRepository,
) : RetryableGraphqlUseCase<SetPinnedProduct>(graphqlRepository) {

    init {
        setGraphqlQuery(SetPinnedProductUseCaseQuery())
        setCacheStrategy(
            GraphqlCacheStrategy
                .Builder(CacheType.ALWAYS_CLOUD).build()
        )
        setTypeClass(SetPinnedProduct::class.java)
    }

    fun createParam(channelId: String, product: ProductUiModel): Map<String, Any> {
        val productId = if (product.pinStatus.isPinned) "0" else product.id
        return mapOf(
            PARAM_CHANNEL_ID to channelId.toLongOrZero(),
            PARAM_PRODUCT_ID to productId.toLongOrZero(),
        )
    }

    companion object {
        private const val PARAM_CHANNEL_ID = "channelID"
        private const val PARAM_PRODUCT_ID = "productID"
        const val QUERY_NAME = "SetPinnedProductUseCaseQuery"
        const val QUERY = """
            mutation broPinProduct(${"$${PARAM_CHANNEL_ID}"}: Int64!, ${"$${PARAM_PRODUCT_ID}"}: Int64) {
                broadcasterSetPinnedProductTag(
                $PARAM_CHANNEL_ID: ${"$${PARAM_CHANNEL_ID}"},
                $PARAM_PRODUCT_ID: ${"$${PARAM_PRODUCT_ID}"}
                ){
                  success
                }
            }
            """
    }
}