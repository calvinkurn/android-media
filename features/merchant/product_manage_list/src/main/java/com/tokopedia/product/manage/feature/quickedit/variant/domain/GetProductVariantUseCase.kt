package com.tokopedia.product.manage.feature.quickedit.variant.domain

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.product.manage.feature.quickedit.variant.data.model.response.GetProductVariantResponse
import com.tokopedia.product.manage.feature.quickedit.variant.data.query.GetProductVariant
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject

class GetProductVariantUseCase @Inject constructor(
    graphqlRepository: GraphqlRepository
): GraphqlUseCase<GetProductVariantResponse>(graphqlRepository) {

    companion object {
        private const val PARAM_PRODUCT_ID = "productID"
        private const val PARAM_OPTIONS = "options"
        private const val PARAM_VARIANT = "variant"
        private const val PARAM_EDIT = "edit"

        fun createRequestParams(productId: String): RequestParams {
            val optionsParam = RequestParams()
            optionsParam.putBoolean(PARAM_VARIANT, true)
            optionsParam.putBoolean(PARAM_EDIT, true)

            return RequestParams().apply {
                putString(PARAM_PRODUCT_ID, productId)
                putObject(PARAM_OPTIONS, optionsParam.parameters)
            }
        }
    }

    init {
        val cacheStrategy = GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build()
        setCacheStrategy(cacheStrategy)

        setGraphqlQuery(GetProductVariant.QUERY)
        setTypeClass(GetProductVariantResponse::class.java)
    }

    suspend fun execute(requestParams: RequestParams): GetProductVariantResponse {
        setRequestParams(requestParams.parameters)
        return executeOnBackground()
    }
}