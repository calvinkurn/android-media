package com.tokopedia.topchat.chatroom.domain.usecase

import android.content.res.Resources
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.topchat.R
import com.tokopedia.topchat.chatroom.domain.pojo.GetExistingMessageIdPojo
import rx.Subscriber
import javax.inject.Inject

/**
 * @author by nisie on 09/01/19.
 */

class GetExistingMessageIdUseCase @Inject constructor(
        val resources: Resources,
        private val graphqlUseCase: GraphqlUseCase
) {

    fun execute(requestParams: Map<String, Any>, subscriber: Subscriber<GraphqlResponse>) {
        val query = GraphqlHelper.loadRawString(resources, R.raw.query_get_existing_message_id)
        val graphqlRequest = GraphqlRequest(query,
                GetExistingMessageIdPojo::class.java, requestParams)

        graphqlUseCase.clearRequest()
        graphqlUseCase.addRequest(graphqlRequest)
        graphqlUseCase.execute(subscriber)
    }

    companion object {

        private val PARAM_TO_USER_ID: String = "toUserId"
        private val PARAM_TO_SHOP_ID: String = "toShopId"
        private val PARAM_SOURCE: String = "source"


        fun generateParam(toShopId: String, toUserId: String, source: String):
                Map<String, Any> {
            val requestParams = HashMap<String, Any>()
            requestParams[PARAM_TO_SHOP_ID] = if (toShopId.isNotBlank()) toShopId.toInt() else 0
            requestParams[PARAM_TO_USER_ID] = if (toUserId.isNotBlank()) toUserId.toInt() else 0
            requestParams[PARAM_SOURCE] = source
            return requestParams
        }
    }

    fun unsubscribe() {
        graphqlUseCase.unsubscribe()
    }

}