package com.tokopedia.shop.common.domain

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.shop.common.data.model.RestrictionEngineRequestParams
import com.tokopedia.shop.common.data.response.GqlRestrictionEngineNplResponse
import com.tokopedia.shop.common.data.response.RestrictValidateRestriction
import com.tokopedia.shop.common.data.response.RestrictionEngineData
import com.tokopedia.shop.common.data.response.RestrictionEngineDataResponse
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject

/**
 * Created by Rafli Syam on 08/10/2020
 */
class RestrictionEngineNplUseCase @Inject constructor(
        private val graphqlRepository: GraphqlRepository
): GraphqlUseCase<RestrictValidateRestriction>(graphqlRepository) {

    var params: RequestParams = RequestParams.EMPTY

    override suspend fun executeOnBackground(): RestrictValidateRestriction {
        val request = GraphqlRequest(QUERY, RestrictionEngineData::class.java, params.parameters)
        val cacheStrategy = GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build()
        val gqlResponse = graphqlRepository.getReseponse(listOf(request), cacheStrategy)
        val gqlResponseError = gqlResponse.getError(GqlRestrictionEngineNplResponse::class.java)
        if(gqlResponseError == null || gqlResponseError.isEmpty()) {
            return gqlResponse.getData<RestrictionEngineData>(
                    RestrictionEngineData::class.java
            ).restrictValidateRestriction
        } else {
            throw MessageErrorException(gqlResponseError.joinToString(", ") { it.message })
        }
    }

    companion object {

        private const val INPUT = "input"
        private const val ANDROID_SOURCE = "android_shop_page_npl"

        /**
         * Create request parameters for restriction engine gql query
         */
        fun createRequestParams(
                restrictionEngineRequestParams: RestrictionEngineRequestParams
        ): RequestParams = RequestParams.create().apply {
            restrictionEngineRequestParams.source = ANDROID_SOURCE
            putObject(INPUT, restrictionEngineRequestParams)
        }


        /**
         * GQL Query for Restriction Engine NPL
         */
        private const val QUERY = "query(\$input: ValidateRestrictionRequest!) {\n" +
                "  restrictValidateRestriction(input:\$input) {\n" +
                "    success\n" +
                "    message\n" +
                "    dataResponse{\n" +
                "      productID\n" +
                "      status\n" +
                "      actions{\n" +
                "        actionType\n" +
                "        title\n" +
                "        description\n" +
                "        actionURL\n" +
                "        attributeName\n" +
                "      }\n" +
                "    }\n" +
                "  }\n" +
                "}"

    }
}