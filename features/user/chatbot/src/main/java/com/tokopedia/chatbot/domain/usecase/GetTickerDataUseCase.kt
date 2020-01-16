package com.tokopedia.chatbot.domain.usecase

import android.content.res.Resources
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.chatbot.R
import com.tokopedia.chatbot.data.TickerData.TickerDataResponse
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.graphql.domain.GraphqlUseCase
import rx.Subscriber
import javax.inject.Inject

class GetTickerDataUseCase @Inject constructor(val resources: Resources,
                                               private val graphqlUseCase: GraphqlUseCase) {

    fun execute(subscriber: Subscriber<GraphqlResponse>) {
        val query = GraphqlHelper.loadRawString(resources, R.raw.query_chip_get_active_ticker)
        val graphqlRequest = GraphqlRequest(query,
                TickerDataResponse::class.java, false)

        graphqlUseCase.clearRequest()
        graphqlUseCase.addRequest(graphqlRequest)
        graphqlUseCase.execute(subscriber)
    }


    fun unsubscribe() {
        graphqlUseCase.unsubscribe()
    }

}