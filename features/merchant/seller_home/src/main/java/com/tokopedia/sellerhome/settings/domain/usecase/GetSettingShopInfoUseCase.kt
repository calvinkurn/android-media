package com.tokopedia.sellerhome.settings.domain.usecase

import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.sellerhome.settings.domain.entity.ShopInfo
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject

class GetSettingShopInfoUseCase @Inject constructor(private val gqlUseCase: MultiRequestGraphqlUseCase) : UseCase<ShopInfo>() {

    companion object {
        const val QUERY = "query ShopInfo(\$userId: Int!) {\n" +
                "  shopInfoMoengage(userID: \$userId) {\n" +
                "    info {\n" +
                "      shop_name\n" +
                "      shop_avatar\n" +
                "    }\n" +
                "    owner {\n" +
                "      pm_status\n" +
                "      is_gold_merchant\n" +
                "      is_seller\n" +
                "    }\n" +
                "  }\n" +
                "  balance {\n" +
                "    seller_usable\n" +
                "  }\n" +
                "  topadsDeposit(userID: \$userId) {\n" +
                "    topads_amount\n" +
                "    is_topads_user\n" +
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
        gqlUseCase.clearRequest()
        gqlUseCase.addRequest(gqlRequest)
        val gqlResponse = gqlUseCase.executeOnBackground()

        val errors = gqlResponse.getError(ShopInfo::class.java)
        if (errors.isNullOrEmpty()) {
            return gqlResponse.getData(ShopInfo::class.java)
        } else throw MessageErrorException(errors.firstOrNull()?.message)
    }
}