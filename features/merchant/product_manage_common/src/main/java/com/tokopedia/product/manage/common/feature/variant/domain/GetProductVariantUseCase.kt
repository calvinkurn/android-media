package com.tokopedia.product.manage.common.feature.variant.domain

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.product.manage.common.feature.variant.data.model.response.GetProductVariantResponse
import com.tokopedia.product.manage.common.feature.variant.data.query.GetProductVariant
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject

class GetProductVariantUseCase @Inject constructor(
    graphqlRepository: GraphqlRepository
): GraphqlUseCase<GetProductVariantResponse>(graphqlRepository) {

    companion object {
        private const val PARAM_PRODUCT_ID = "productID"
        private const val PARAM_OPTIONS = "options"
        private const val PARAM_EXTRA_INFO = "extraInfo"
        private const val PARAM_CAMPAIGN = "campaign"
        private const val PARAM_EVENT = "event"
        private const val PARAM_VARIANT = "variant"
        private const val PARAM_EDIT = "edit"
        private const val PARAM_WAREHOUSE_ID = "warehouseID"

        fun createRequestParams(productId: String, paramEdit: Boolean = true, warehouseId: String? = null): RequestParams {
            val optionsParam = RequestParams().apply {
                putBoolean(PARAM_VARIANT, true)
                putBoolean(PARAM_EDIT, paramEdit)
                putBoolean(PARAM_CAMPAIGN, true)
            }.parameters

            val extraInfoParam = RequestParams().apply {
                putBoolean(PARAM_EVENT, true)
            }.parameters

            return RequestParams().apply {
                putString(PARAM_PRODUCT_ID, productId)
                putObject(PARAM_OPTIONS, optionsParam)
                putObject(PARAM_EXTRA_INFO, extraInfoParam)
                putObject(PARAM_WAREHOUSE_ID, warehouseId)
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