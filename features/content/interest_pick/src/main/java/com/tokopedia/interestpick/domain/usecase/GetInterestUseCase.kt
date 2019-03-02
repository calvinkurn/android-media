package com.tokopedia.interestpick.domain.usecase

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.interestpick.R
import com.tokopedia.interestpick.data.pojo.GetInterestData
import rx.Subscriber
import javax.inject.Inject

/**
 * @author by milhamj on 07/09/18.
 */
class GetInterestUseCase @Inject constructor(@ApplicationContext val context: Context,
                                             val graphqlUseCase: GraphqlUseCase) {
    fun execute(subscriber: Subscriber<GraphqlResponse>) {
        val query = GraphqlHelper.loadRawString(context.resources, R.raw.query_get_interest)
        val graphqlRequest = GraphqlRequest(query, GetInterestData::class.java, false)

        graphqlUseCase.clearRequest()
        graphqlUseCase.addRequest(graphqlRequest)
        graphqlUseCase.execute(subscriber)
    }

    fun unsubscribe() = graphqlUseCase.unsubscribe()
}