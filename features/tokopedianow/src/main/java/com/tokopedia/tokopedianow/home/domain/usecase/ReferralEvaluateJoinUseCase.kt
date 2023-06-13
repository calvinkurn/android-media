package com.tokopedia.tokopedianow.home.domain.usecase

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.tokopedianow.home.domain.model.ReferralEvaluateJoinResponse
import com.tokopedia.tokopedianow.home.domain.query.ReferralEvaluateJoin
import com.tokopedia.tokopedianow.home.domain.query.ReferralEvaluateJoin.REFERRAL_CODE
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject

class ReferralEvaluateJoinUseCase @Inject constructor(
    graphqlRepository: GraphqlRepository
) : GraphqlUseCase<ReferralEvaluateJoinResponse>() {

    init {
        setTypeClass(ReferralEvaluateJoinResponse::class.java)
        setGraphqlQuery(ReferralEvaluateJoin)
    }

    suspend fun execute(referralCode: String): ReferralEvaluateJoinResponse {
        setRequestParams(
            RequestParams.create().apply {
                putString(REFERRAL_CODE, referralCode)
        }.parameters)
        return executeOnBackground()
    }
}
