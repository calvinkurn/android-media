package com.tokopedia.variant_common.use_case

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.UseCase
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.variant_common.constant.VariantConstant
import com.tokopedia.variant_common.model.ProductDetailVariantCommonResponse
import javax.inject.Inject
import javax.inject.Named


/**
 * Created by mzennis on 2020-03-09.
 */
class GetProductVariantUseCase @Inject constructor(
        @Named(VariantConstant.QUERY_VARIANT) val rawQuery: String,
        private val userSessionInterface: UserSessionInterface,
        private val graphqlRepository: GraphqlRepository):
        UseCase<ProductDetailVariantCommonResponse>() {

    var params: RequestParams = RequestParams.EMPTY

    override suspend fun executeOnBackground(): ProductDetailVariantCommonResponse {
        val gqlRequest = GraphqlRequest(rawQuery, ProductDetailVariantCommonResponse::class.java, params.parameters)
        val gqlResponse = graphqlRepository.getReseponse(listOf(gqlRequest), GraphqlCacheStrategy
                .Builder(CacheType.ALWAYS_CLOUD).build())
        val error = gqlResponse.getError(ProductDetailVariantCommonResponse::class.java)
        if (error == null || error.isEmpty()) {
            return (gqlResponse.getData(ProductDetailVariantCommonResponse::class.java) as ProductDetailVariantCommonResponse)
        } else {
            throw MessageErrorException(error.mapNotNull { it.message }.joinToString(separator = ", "))
        }
    }

    fun createParams(productId: String): RequestParams {
        val params = RequestParams.create()
        params.putString("productID", productId)

        val variantOption = mutableMapOf<String, Any>()
        variantOption["includeCampaign"] = true
        variantOption["userID"] = if (userSessionInterface.userId.isNotEmpty()) userSessionInterface.userId else "0"
        variantOption["includeWarehouse"] = true

        val parent = mutableMapOf<String, Any>()
        parent["option"] = variantOption

        params.putAll(parent)
        return params
    }
}