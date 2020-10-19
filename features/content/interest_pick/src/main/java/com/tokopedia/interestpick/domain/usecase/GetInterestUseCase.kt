package com.tokopedia.interestpick.domain.usecase

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.interestpick.data.pojo.GetInterestData
import com.tokopedia.interestpick.data.raw.GQL_QUERY_GET_INTEREST
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject

/**
 * @author by milhamj on 07/09/18.
 */
class GetInterestUseCase @Inject constructor(
        private val graphqlUseCase: GraphqlUseCase<GetInterestData>
) : UseCase<GetInterestData>() {

    init {
        graphqlUseCase.setTypeClass(GetInterestData::class.java)
        graphqlUseCase.setGraphqlQuery(GQL_QUERY_GET_INTEREST)
    }

    override suspend fun executeOnBackground(): GetInterestData {
        graphqlUseCase.clearCache()
        return graphqlUseCase.executeOnBackground()
    }
}