package com.tokopedia.interestpick.domain.usecase

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.interestpick.data.pojo.UpdateInterestData
import com.tokopedia.interestpick.data.raw.GQL_MUTATION_UPDATE_INTEREST
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject

/**
 * @author by milhamj on 10/09/18.
 */

@GqlQuery("UpdateInterest", GQL_MUTATION_UPDATE_INTEREST)
class UpdateInterestUseCase @Inject constructor(
        private val graphqlUseCase: GraphqlUseCase<UpdateInterestData>
) : UseCase<UpdateInterestData>() {

    init {
        graphqlUseCase.setTypeClass(UpdateInterestData::class.java)
        graphqlUseCase.setGraphqlQuery(UpdateInterest.GQL_QUERY)
    }

    override suspend fun executeOnBackground(): UpdateInterestData {
        graphqlUseCase.clearCache()
        return graphqlUseCase.executeOnBackground()
    }

    fun setRequestParams(interestIds: List<Int>) {
        graphqlUseCase.setRequestParams(getRequestParams(interestIds))
    }

    fun setRequestParamsSkip() {
        graphqlUseCase.setRequestParams(getRequestParamsSkip())
    }

    companion object {
        private const val PARAM_INTERESTS_ID = "interestID"
        private const val PARAM_ACTION = "action"
        private const val ACTION_SKIP = "skip"

        fun getRequestParams(interestIds: List<Int>): HashMap<String, Any> {
            val variables: HashMap<String, Any> = HashMap()
            variables[PARAM_INTERESTS_ID] = interestIds
            return variables
        }

        fun getRequestParamsSkip(): HashMap<String, Any> {
            val variables: HashMap<String, Any> = HashMap()
            val emptyArray: Array<Int> = emptyArray()
            variables[PARAM_INTERESTS_ID] = emptyArray
            variables[PARAM_ACTION] = ACTION_SKIP
            return variables
        }
    }
}