package com.tokopedia.sellerhome.settings.domain.usecase

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.sellerhome.settings.domain.entity.ShopInfo
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject

class GetSettingShopInfoUseCase @Inject constructor(private val graphQlRepository: GraphqlRepository) : UseCase<ShopInfo>() {

    companion object {
        const val QUERY = "query ShopInfo(\$userId: Int!) {\n" +
                "  shopInfoMoengage(userID: \$userId) {\n" +
                "    info {\n" +
                "      shop_name\n" +
                "      shop_avatar\n" +
                "    }\n" +
                "  }\n" +
                "  balance {\n" +
                "    seller_usable\n" +
                "    buyer_usable\n" +
                "  }\n" +
                "}"

        private const val USER_ID_KEY = "userId"

        fun createRequestParams(userId: Int) = HashMap<String, Any>().apply {
            put(USER_ID_KEY, userId)
        }
    }

    var params = HashMap<String, Any>()

    override suspend fun executeOnBackground(): ShopInfo {
        val gqlRequest = GraphqlRequest(QUERY, ShopInfo::class.java, params)
        val gqlResponse = graphQlRepository.getReseponse(listOf(gqlRequest))

        val errors = gqlResponse.getError(ShopInfo::class.java)
        if (errors.isNullOrEmpty()) {
            return gqlResponse.getData(ShopInfo::class.java)
        } else throw MessageErrorException(errors.joinToString { it.message })
    }
}