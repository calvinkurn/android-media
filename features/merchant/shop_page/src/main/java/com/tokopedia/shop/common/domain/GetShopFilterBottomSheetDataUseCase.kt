package com.tokopedia.shop.common.domain

import com.tokopedia.filter.common.data.DynamicFilterModel
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.shop.common.data.response.GqlDynamicFilterResponse
import com.tokopedia.shop.common.util.UrlParamUtil
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject

class GetShopFilterBottomSheetDataUseCase @Inject constructor(
        private val graphqlRepository: GraphqlRepository
) : GraphqlUseCase<DynamicFilterModel>(graphqlRepository) {

    companion object {
        private const val KEY_PARAMS = "params"
        private const val KEY_SOURCE = "source"
        private const val KEY_DEVICE = "device"
        private const val VALUE_SOURCE = "shop_product"
        private const val DEVICE_VALUE = "android"

        fun createParams() = mapOf(
                KEY_PARAMS to UrlParamUtil.convertMapToStringParam(mapOf(
                        KEY_SOURCE to VALUE_SOURCE,
                        KEY_DEVICE to DEVICE_VALUE
                ))
        )
    }

    var params = mapOf<String, Any>()

    override suspend fun executeOnBackground(): DynamicFilterModel {
        val request = GraphqlRequest(GQL_QUERY, GqlDynamicFilterResponse::class.java, params)
        val cacheStrategy = GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build()
        val gqlResponse = graphqlRepository.getReseponse(listOf(request), cacheStrategy)
        val error = gqlResponse.getError(GqlDynamicFilterResponse::class.java)
        if (error == null || error.isEmpty()) {
            return gqlResponse.getData<GqlDynamicFilterResponse>(GqlDynamicFilterResponse::class.java).dynamicFilterModel
        } else {
            throw MessageErrorException(error.joinToString(", ") { it.message })
        }
    }

    private val GQL_QUERY = "query SearchProduct(\n" +
            " \$params: String!\n" +
            ") {\n" +
            "filter_sort_product(params: \$params) {\n" +
            "      data {\n" +
            "        filter {\n" +
            "          title\n" +
            "          search {\n" +
            "            searchable\n" +
            "            placeholder\n" +
            "          }\n" +
            "      template_name\n" +
            "          options {\n" +
            "            name\n" +
            "            key\n" +
            "            icon\n" +
            "            Description\n" +
            "            value\n" +
            "            inputType\n" +
            "            totalData\n" +
            "            valMax\n" +
            "            valMin\n" +
            "            isPopular\n" +
            "            isNew\n" +
            "            hexColor\n" +
            "            child {\n" +
            "              key\n" +
            "              value\n" +
            "              name\n" +
            "              icon\n" +
            "              inputType\n" +
            "              totalData\n" +
            "              isPopular\n" +
            "              child {\n" +
            "                key\n" +
            "                value\n" +
            "                name\n" +
            "                icon\n" +
            "                inputType\n" +
            "                totalData\n" +
            "                isPopular\n" +
            "              }\n" +
            "            }\n" +
            "          }\n" +
            "        }\n" +
            "        sort {\n" +
            "          name\n" +
            "          key\n" +
            "          value\n" +
            "          inputType\n" +
            "          applyFilter\n" +
            "        }\n" +
            "     }\n" +
            "  }\n" +
            "}"
}