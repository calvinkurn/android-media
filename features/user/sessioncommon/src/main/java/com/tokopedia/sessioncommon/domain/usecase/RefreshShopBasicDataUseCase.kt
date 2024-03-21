package com.tokopedia.sessioncommon.domain.usecase

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.sessioncommon.data.admin.RefreshShopDataResponse
import com.tokopedia.sessioncommon.data.profile.ShopData
import javax.inject.Inject

class RefreshShopBasicDataUseCase @Inject constructor(
    private val gqlRepository: GraphqlRepository,
    dispatchers: CoroutineDispatchers
) : CoroutineUseCase<Unit, ShopData>(dispatchers.io) {

    override fun graphqlQuery(): String {
        return "query RefreshShopBasicData {\n" +
            "  shopBasicData {\n" +
            "    result {\n" +
            "      shopID\n" +
            "      name\n" +
            "      logo\n" +
            "      level\n" +
            "    }\n" +
            "  }\n" +
            "}"
    }

    override suspend fun execute(params: Unit): ShopData {
        val response = gqlRepository.request<Unit, RefreshShopDataResponse>(graphqlQuery(), params)
        return response.shopInfo.shopData
    }
}
