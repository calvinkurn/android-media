package com.tokopedia.shop.common.domain.interactor

import android.content.res.Resources
import com.google.gson.JsonObject
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.shop.common.R
import com.tokopedia.shop.common.domain.interactor.model.favoriteshop.DataFollowShop
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import rx.Observable
import java.util.*
import javax.inject.Inject

/**
 * Created by User on 9/8/2017.
 */
class ToggleFavouriteShopUseCase @Inject constructor(private val graphqlUseCase: GraphqlUseCase,
                                                     private val resources: Resources) : UseCase<Boolean>() {
    enum class Action(val actionString: String) {
        FOLLOW("follow"), UNFOLLOW("unfollow");
    }

    override fun createObservable(requestParams: RequestParams): Observable<Boolean> {
        graphqlUseCase.clearRequest()
        val variables: MutableMap<String, Any> = HashMap()
        val input = JsonObject()
        input.addProperty(SHOP_ID_INPUT, requestParams.getString(SHOP_ID, ""))
        input.addProperty(PARAM_ACTION, requestParams.getString(PARAM_ACTION, ""))
        variables[INPUT] = input
        val graphqlRequest = GraphqlRequest(
                GraphqlHelper.loadRawString(resources, R.raw.gql_mutation_favorite_shop),
                DataFollowShop::class.java,
                variables, false)
        graphqlUseCase.addRequest(graphqlRequest)
        return graphqlUseCase.createObservable(requestParams)
                .map { graphqlResponse ->
                    val dataDetailCheckoutPromo: DataFollowShop = graphqlResponse.getData(DataFollowShop::class.java)
                    if (dataDetailCheckoutPromo.followShop != null) {
                        dataDetailCheckoutPromo.followShop?.isSuccess
                    } else {
                        throw RuntimeException()
                    }
                }
    }

    companion object {
        private const val SHOP_ID = "SHOP_ID"
        private const val PARAM_ACTION = "action"
        const val SHOP_ID_INPUT = "shopID"
        const val INPUT = "input"
        fun createRequestParam(shopId: String?): RequestParams {
            val requestParams = RequestParams.create()
            requestParams.putString(SHOP_ID, shopId)
            return requestParams
        }

        fun createRequestParam(shopId: String?, action: Action): RequestParams {
            val requestParams = RequestParams.create()
            requestParams.putString(SHOP_ID, shopId)
            requestParams.putString(PARAM_ACTION, action.actionString)
            return requestParams
        }
    }
}