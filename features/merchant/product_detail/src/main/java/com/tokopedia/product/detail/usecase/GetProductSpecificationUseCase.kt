package com.tokopedia.product.detail.usecase

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlError
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.product.detail.common.ProductDetailCommonConstant
import com.tokopedia.product.detail.data.model.spesification.ProductSpecificationResponse
import com.tokopedia.product.detail.di.RawQueryKeyConstant
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject

/**
 * Created by Yehezkiel on 22/07/20
 */
class GetProductSpecificationUseCase @Inject constructor(private val rawQueries: Map<String, String>,
                                                         private val graphqlRepository: GraphqlRepository) : UseCase<ProductSpecificationResponse>() {

    companion object {
        fun createParams(catalogId: String): RequestParams = RequestParams.create().apply {
            putString(ProductDetailCommonConstant.PARAM_CATALOG_ID, catalogId)
        }
    }

    private var requestParams = RequestParams.EMPTY

    suspend fun executeOnBackground(requestParams: RequestParams): ProductSpecificationResponse {
        this.requestParams = requestParams

        return executeOnBackground()
    }

    override suspend fun executeOnBackground(): ProductSpecificationResponse {
        val productCatalogRequest = GraphqlRequest(rawQueries[RawQueryKeyConstant.QUERY_PRODUCT_CATALOG],
                ProductSpecificationResponse::class.java, requestParams.parameters)
        val cacheStrategy =  GraphqlCacheStrategy.Builder(CacheType.CACHE_FIRST)
                .setSessionIncluded(false)
                .build()

        val gqlResponse = graphqlRepository.getReseponse(listOf(productCatalogRequest), cacheStrategy)
        val error: List<GraphqlError>? = gqlResponse.getError(ProductSpecificationResponse::class.java)
        val data = gqlResponse.getData<ProductSpecificationResponse>(ProductSpecificationResponse::class.java)

        if (error != null && error.isNotEmpty()) {
            throw MessageErrorException(error.firstOrNull()?.message ?: "")
        } else if (data == null || data.productCatalogQuery.data.catalog.specification.isEmpty()) {
            throw RuntimeException()
        }
        return data
    }
}