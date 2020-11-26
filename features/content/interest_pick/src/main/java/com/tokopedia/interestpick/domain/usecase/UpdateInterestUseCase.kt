package com.tokopedia.interestpick.domain.usecase

import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.interestpick.data.pojo.UpdateInterestData
import com.tokopedia.interestpick.data.raw.GQL_MUTATION_UPDATE_INTEREST
import rx.Subscriber
import javax.inject.Inject

/**
 * @author by milhamj on 10/09/18.
 */
@Deprecated("use SubmitInterestPickUseCase instead", ReplaceWith("SubmitInterestPickUseCase"))
class UpdateInterestUseCase @Inject constructor(val graphqlUseCase: GraphqlUseCase) {
    fun execute(variables: HashMap<String, Any>, subscriber: Subscriber<GraphqlResponse>?) {
        val query = GQL_MUTATION_UPDATE_INTEREST
        val graphqlRequest = GraphqlRequest(query, UpdateInterestData::class.java, variables)

        graphqlUseCase.clearRequest()
        graphqlUseCase.addRequest(graphqlRequest)
        graphqlUseCase.execute(subscriber)
    }

    fun unsubscribe() = graphqlUseCase.unsubscribe()

    companion object {
        private const val PARAM_INTERESTS_ID = "interestID"
        private const val PARAM_ACTION = "action"
        private const val ACTION_SKIP = "skip"

        fun getRequestParams(interestIds: List<Int>): HashMap<String, Any> {
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