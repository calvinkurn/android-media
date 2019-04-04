package com.tokopedia.topchat.chatroom.domain.usecase

import android.content.res.Resources
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.topchat.R
import com.tokopedia.topchat.chatroom.domain.pojo.ShopFollowingPojo
import rx.Subscriber
import javax.inject.Inject

/**
 * @author by nisie on 16/01/19.
 */

class GetShopFollowingUseCase @Inject constructor(
        val resources: Resources,
        private val graphqlUseCase: GraphqlUseCase
) {

    fun execute(requestParams: Map<String, Any>, subscriber: Subscriber<GraphqlResponse>) {
        val query = GraphqlHelper.loadRawString(resources, R.raw.query_get_shop_following_status)
        val graphqlRequest = GraphqlRequest(query,
                ShopFollowingPojo::class.java, requestParams, false)

        graphqlUseCase.clearRequest()
        graphqlUseCase.addRequest(graphqlRequest)
        graphqlUseCase.execute(subscriber)
    }

    companion object {

        private val PARAM_SHOP_IDS: String = "shopIDs"
        private val PARAM_INPUT_FIELDS: String = "inputFields"
        private val DEFAULT_FAVORITE: String = "favorite"


        fun generateParam(shopId: Int):
                Map<String, Any> {
            val shopIds = ArrayList<Int>()
            shopIds.add(shopId)

            val inputFields = ArrayList<String>()
            inputFields.add(DEFAULT_FAVORITE)
            val requestParams = HashMap<String, Any>()
            requestParams[PARAM_SHOP_IDS] = shopIds
            requestParams[PARAM_INPUT_FIELDS] = inputFields
            return requestParams
        }
    }

    fun unsubscribe() {
        graphqlUseCase.unsubscribe()
    }

}