package com.tokopedia.tokopoints.view.addPoint

import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.tokopoints.di.TokoPointScope
import com.tokopedia.tokopoints.view.model.addpointsection.RewardPointResponse
import com.tokopedia.tokopoints.view.util.CommonConstant.GQLQuery.TP_GQL_ADD_POINT_REWARD
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Named

@TokoPointScope
class RewardUseCase @Inject constructor(@Named(TP_GQL_ADD_POINT_REWARD) private val tp_gql_add_point_rewards: String, private val useCase: MultiRequestGraphqlUseCase) {
    suspend fun execute() = withContext(Dispatchers.IO) {
        val graphqlRequestPoints = GraphqlRequest(tp_gql_add_point_rewards,
                RewardPointResponse::class.java, false)
        useCase.addRequest(graphqlRequestPoints)
        useCase.executeOnBackground().getSuccessData<RewardPointResponse>()
    }
}