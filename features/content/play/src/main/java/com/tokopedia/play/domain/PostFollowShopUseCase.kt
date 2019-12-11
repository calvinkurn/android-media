package com.tokopedia.play.domain

import com.google.gson.JsonObject
import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.play.data.FollowShop
import com.tokopedia.play.data.ShopInfo
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.UseCase
import java.util.*
import javax.inject.Inject


/**
 * Created by mzennis on 2019-12-10.
 */
class PostFollowShopUseCase @Inject constructor(private val gqlUseCase: MultiRequestGraphqlUseCase): UseCase<Boolean>() {

    var params: RequestParams = RequestParams.EMPTY

    override suspend fun executeOnBackground(): Boolean {
        val variables = HashMap<String, Any>()
        val input = JsonObject()
        input.addProperty(SHOP_ID, params.getString(SHOP_ID, ""))
        input.addProperty(ACTION, params.getString(ACTION, ""))
        variables[INPUT] = input

        val gqlRequest = GraphqlRequest(query, ShopInfo.Response::class.java, params.parameters)
        gqlUseCase.clearRequest()
        gqlUseCase.addRequest(gqlRequest)
        gqlUseCase.setCacheStrategy(GraphqlCacheStrategy
                .Builder(CacheType.ALWAYS_CLOUD).build())

        val gqlResponse = gqlUseCase.executeOnBackground()
        val response = gqlResponse.getData(FollowShop.Data::class.java) as FollowShop.Data
        if (response.data.followShop.success)  {
            return true
        } else {
            throw MessageErrorException(response.data.followShop.message)
        }

    }

    companion object {

        const val SHOP_ID = "shopID"
        const val ACTION = "action"
        const val INPUT = "input"

        private val query by lazy {
            val input = "\$input"

            """
            mutation followShop($input: ParamFollowShop!) {
              followShop(input:\$input){
                success
                message
              }
            }
            """.trimIndent()
        }
    }

    enum class Action (val value: String) {
        FOLLOW("follow"),
        UNFOLLOW("unfollow")
    }

    fun createParam(shopId: String, action: Action): RequestParams {
        val requestParams = RequestParams.create()
        requestParams.putString(SHOP_ID, shopId)
        requestParams.putString(ACTION, action.value)
        return requestParams
    }

}
