package com.tokopedia.loginregister.shopcreation.domain.usecase

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.loginregister.shopcreation.domain.param.ShopInfoParam
import com.tokopedia.loginregister.shopcreation.domain.pojo.ShopInfoPojo
import com.tokopedia.loginregister.shopcreation.domain.query.QueryShopInfo
import javax.inject.Inject

class ShopInfoUseCase @Inject constructor(
    private val graphqlRepository: GraphqlRepository,
    dispatcher: CoroutineDispatchers
) : CoroutineUseCase<ShopInfoParam, ShopInfoPojo>(dispatcher.io) {

    override fun graphqlQuery(): String {
        return QueryShopInfo.getQuery()
    }

    override suspend fun execute(params: ShopInfoParam): ShopInfoPojo {
        return graphqlRepository.request(graphqlQuery(), params.toMap())
    }
}