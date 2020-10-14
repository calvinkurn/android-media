package com.tokopedia.interest_pick_common.domain.usecase

import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.interest_pick_common.data.SubmitInterestResponse
import com.tokopedia.interest_pick_common.domain.query.SubmitInterestPickQuery
import com.tokopedia.usecase.coroutines.UseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Named

/**
 * @author by yoasfs on 2019-11-05
 */

private const val PARAM_ACTION = "action"
private const val PARAM_INTEREST_ID = "interestID"

class SubmitInterestPickUseCase constructor(
        @Named(SubmitInterestPickQuery.MUTATION_SUBMIT_INTEREST_ID) private val query: String,
        private val graphqlUseCase: MultiRequestGraphqlUseCase
): UseCase<SubmitInterestResponse>() {

    override suspend fun executeOnBackground(): SubmitInterestResponse = withContext(Dispatchers.IO)  {
        val response = graphqlUseCase.executeOnBackground()
        return@withContext response.getData<SubmitInterestResponse>(SubmitInterestResponse::class.java)
    }

    fun addRequestWithParam(interestIds: List<Int>) {
        graphqlUseCase.addRequest(
                GraphqlRequest(
                        query,
                        SubmitInterestResponse::class.java,
                        mapOf(PARAM_INTEREST_ID to interestIds,
                                PARAM_ACTION to "")

                )
        )
    }

    fun clearRequest() = graphqlUseCase.clearRequest()
}