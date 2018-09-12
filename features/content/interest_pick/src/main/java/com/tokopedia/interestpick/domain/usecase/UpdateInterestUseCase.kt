package com.tokopedia.interestpick.domain.usecase

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.interestpick.R
import com.tokopedia.interestpick.data.pojo.UpdateInterestData
import rx.Subscriber
import javax.inject.Inject

/**
 * @author by milhamj on 10/09/18.
 */
class UpdateInterestUseCase @Inject constructor(@ApplicationContext val context: Context,
                                             val graphqlUseCase: GraphqlUseCase) {
    fun execute(variables: HashMap<String, Any>, subscriber: Subscriber<GraphqlResponse>?) {
        val query = GraphqlHelper.loadRawString(context.resources, R.raw.mutation_update_interest)
        val graphqlRequest = GraphqlRequest(query, UpdateInterestData::class.java, variables)

        graphqlUseCase.clearRequest()
        graphqlUseCase.addRequest(graphqlRequest)
        graphqlUseCase.execute(subscriber)
    }

    fun unsubscribe() = graphqlUseCase.unsubscribe()

    companion object {
        private val PARAM_INTERESTS_ID = "interestID"
        private val PARAM_ACTION = "action"
        private val ACTION_SKIP = "skip"

        fun getRequestParams(interestIds: Array<Int>): HashMap<String, Any> {
            val variables: HashMap<String, Any> = HashMap()
            variables.put(PARAM_INTERESTS_ID, interestIds)
            return variables
        }

        fun getRequestParamsSkip(): HashMap<String, Any> {
            val variables: HashMap<String, Any> = HashMap()
            val emptyArray: Array<Int> = emptyArray()
            variables.put(PARAM_INTERESTS_ID, emptyArray)
            variables.put(PARAM_ACTION, ACTION_SKIP)
            return variables
        }
    }
}