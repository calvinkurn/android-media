package com.tokopedia.homenav.mainnav.domain.usecases

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.homenav.mainnav.data.pojo.shop.ShopInfoPojo
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject


class GetShopInfoUseCase @Inject constructor(private val gqlUseCase: GraphqlRepository):
        UseCase<Result<ShopInfoPojo>>() {

    var params: RequestParams = RequestParams.EMPTY

    override suspend fun executeOnBackground(): Result<ShopInfoPojo> {
        return try {
            val gqlRequest = GraphqlRequest(query, ShopInfoPojo.Response::class.java, params.parameters)
            val gqlResponse = gqlUseCase.getReseponse(listOf(gqlRequest), GraphqlCacheStrategy
                    .Builder(CacheType.ALWAYS_CLOUD).build())

            val error = gqlResponse.getError(ShopInfoPojo.Response::class.java)
            if (error == null || error.isEmpty()) {
                Success((gqlResponse.getData(ShopInfoPojo.Response::class.java) as ShopInfoPojo.Response).userShopInfo)
            } else {
                Fail(MessageErrorException(error.mapNotNull { it.message }.joinToString(separator = ", ")))
            }
        } catch (e: Exception) {
            Fail(e)
        }
    }

    companion object {

        private val query = getQuery()

        private fun getQuery(): String {


            return """query getShopInfo{
                 userShopInfo{
                    info{
                        shop_name
                        shop_id
                    }
                 }
            }
            """.trimIndent()
        }

    }
}