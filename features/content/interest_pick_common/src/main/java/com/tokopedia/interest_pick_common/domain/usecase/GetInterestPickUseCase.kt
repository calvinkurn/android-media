package com.tokopedia.interest_pick_common.domain.usecase

import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.interest_pick_common.data.OnboardingData
import com.tokopedia.interest_pick_common.domain.query.GetInterestPickQuery
import com.tokopedia.usecase.coroutines.UseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Named

/**
 * @author by yoasfs on 2019-11-05
 */

private const val PARAM_SOURCE = "source"

class GetInterestPickUseCase constructor(
        @Named(GetInterestPickQuery.QUERY_GET_INTEREST_PICK) private val query: String,
        private val graphqlUseCase: MultiRequestGraphqlUseCase
): UseCase<OnboardingData>() {

    override suspend fun executeOnBackground(): OnboardingData = withContext(Dispatchers.IO) {
        val response = graphqlUseCase.executeOnBackground()
        return@withContext response.getData<OnboardingData>(OnboardingData::class.java)
    }

    fun addRequestWithParam(source: String) {
        graphqlUseCase.addRequest(
                GraphqlRequest(
                        query,
                        OnboardingData::class.java,
                        mapOf(PARAM_SOURCE to source)
                )
        )
    }

    fun clearRequest() = graphqlUseCase.clearRequest()
}