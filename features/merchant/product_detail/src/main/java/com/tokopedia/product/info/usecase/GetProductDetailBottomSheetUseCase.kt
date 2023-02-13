package com.tokopedia.product.info.usecase

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.product.detail.di.ProductDetailScope
import com.tokopedia.product.info.data.query.GetProductDetailBottomSheetQuery
import com.tokopedia.product.info.data.response.BottomSheetProductDetailInfoResponse
import com.tokopedia.product.info.data.response.PdpGetDetailBottomSheet
import javax.inject.Inject

/**
 * Created by Yehezkiel on 13/10/20
 */
@ProductDetailScope
class GetProductDetailBottomSheetUseCase @Inject constructor(
    graphqlRepository: GraphqlRepository
) : GraphqlUseCase<BottomSheetProductDetailInfoResponse>(graphqlRepository) {

    init {
        setGraphqlQuery(GetProductDetailBottomSheetQuery)
        setTypeClass(BottomSheetProductDetailInfoResponse::class.java)
    }

    suspend fun execute(
        productId: String,
        shopId: String,
        parentId: String,
        isGiftable: Boolean,
        catalogId: String,
        bottomSheetParam: String,
        forceRefresh: Boolean
    ): PdpGetDetailBottomSheet {
        setRequestParams(
            GetProductDetailBottomSheetQuery.createParams(
                productId = productId,
                shopId = shopId,
                parentId = parentId,
                isGiftable = isGiftable,
                catalogId = catalogId,
                bottomSheetParam = bottomSheetParam
            )
        )
        setCacheStrategy(getCacheStrategy(forceRefresh))

        return executeOnBackground().response
    }

    private fun getCacheStrategy(forceRefresh: Boolean): GraphqlCacheStrategy {
        val cacheType = if (forceRefresh) CacheType.ALWAYS_CLOUD else CacheType.CACHE_FIRST

        return GraphqlCacheStrategy.Builder(cacheType)
            .setSessionIncluded(false)
            .build()
    }
}
