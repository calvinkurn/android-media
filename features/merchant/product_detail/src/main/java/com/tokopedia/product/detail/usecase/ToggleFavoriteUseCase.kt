package com.tokopedia.product.detail.usecase

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.product.detail.common.ProductDetailCommonConstant.PARAM_INPUT
import com.tokopedia.product.detail.common.ProductDetailCommonConstant.PARAM_SHOP_ID
import com.tokopedia.product.detail.di.RawQueryKeyConstant
import com.tokopedia.shop.common.domain.interactor.model.favoriteshop.DataFollowShop
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject

class ToggleFavoriteUseCase @Inject constructor(private val rawQueries: Map<String, String>,
                                                private val graphqlRepository: GraphqlRepository) : UseCase<DataFollowShop>() {

    var shopId: String = ""

    fun createRequestParam(shopId: String) {
        this.shopId = shopId
    }

    override suspend fun executeOnBackground(): DataFollowShop {
        val variables = HashMap<String, Any>()
        variables[PARAM_INPUT] = mapOf(PARAM_SHOP_ID to shopId)

        val request = GraphqlRequest(rawQueries[RawQueryKeyConstant.MUTATION_FAVORITE_SHOP],
                DataFollowShop::class.java, variables)

        val gqlResponse = graphqlRepository.getReseponse(listOf(request))
        val result = gqlResponse.getData<DataFollowShop>(DataFollowShop::class.java)
        val errorResult = gqlResponse.getError(DataFollowShop::class.java)

        if (result == null) {
            throw RuntimeException()
        } else if (errorResult != null && errorResult.isNotEmpty() && errorResult.first().message.isNotEmpty()) {
            throw MessageErrorException(errorResult.first().message)
        }

        return result
    }
}