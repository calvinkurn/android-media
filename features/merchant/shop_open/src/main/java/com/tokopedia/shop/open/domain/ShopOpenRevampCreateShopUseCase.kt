package com.tokopedia.shop.open.domain

import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.shop.open.data.model.CreateShop
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.UseCase

import javax.inject.Inject

class ShopOpenRevampCreateShopUseCase @Inject constructor(
        private val graphqlUseCase: MultiRequestGraphqlUseCase
): UseCase<CreateShop>() {

    var params: RequestParams = RequestParams.EMPTY

    override suspend fun executeOnBackground(): CreateShop {
        val createShop = GraphqlRequest(QUERY, CreateShop::class.java, params.parameters)
        graphqlUseCase.clearRequest()
        graphqlUseCase.addRequest(createShop)
        val gqlResponse = graphqlUseCase.executeOnBackground()
        val error = gqlResponse.getError(CreateShop::class.java) ?: listOf()
        if (error == null || error.isEmpty()) {
            return gqlResponse.run {
                getData<CreateShop>(CreateShop::class.java)
            }
        } else  {
            throw MessageErrorException(error.mapNotNull {
                it.message
            }.joinToString(separator = ", "))
        }
    }

    companion object {
        private const val SHOP_NAME = "name"
        private const val DOMAIN_NAME = "domain"
        private const val DISTRICT_ID = "districtID"
        private const val POSTAL_CODE = "postalCode"
        private const val SKIP_LOCATION  = "skipLocation"
        private const val QUERY = "mutation CreateShop(\$name:String!, \$domain: String!, \$postalCode: Int!, \$districtID: Int!) {\n" +
                "    createShop(\n" +
                "        input: {\n" +
                "            name: \$name,\n" +
                "            domain: \$domain,\n" +
                "            postalCode: \$postalCode,\n" +
                "            districtID: \$districtID,\n" +
                "            skipLocation: true\n" +
                "        }\n" +
                "    ){\n" +
                "        success\n" +
                "        message\n" +
                "        createdId\n" +
                "    }\n" +
                "}"

        fun createRequestParams(domainName: String, shopName: String): RequestParams = RequestParams.create().apply {
            putString(SHOP_NAME, shopName)
            putString(DOMAIN_NAME, domainName)
            putInt(DISTRICT_ID, 0)
            putInt(POSTAL_CODE, 0)
            putBoolean(SKIP_LOCATION, true)
        }
    }

}