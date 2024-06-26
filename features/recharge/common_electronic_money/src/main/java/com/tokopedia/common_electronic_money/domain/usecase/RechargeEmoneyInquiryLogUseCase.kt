package com.tokopedia.common_electronic_money.domain.usecase

import com.tokopedia.common_electronic_money.data.RechargeEmoneyInquiryLogRequest
import com.tokopedia.common_electronic_money.data.RechargeEmoneyInquiryLogResponse
import com.tokopedia.common_electronic_money.domain.query.RechargeEmoneyLog
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import javax.inject.Inject

class RechargeEmoneyInquiryLogUseCase @Inject constructor(
    graphqlRepository: GraphqlRepository
): GraphqlUseCase<RechargeEmoneyInquiryLogResponse>(graphqlRepository) {

    init {
        setGraphqlQuery(RechargeEmoneyLog())
        setTypeClass(RechargeEmoneyInquiryLogResponse::class.java)
    }

    suspend fun execute(params: RechargeEmoneyInquiryLogRequest): RechargeEmoneyInquiryLogResponse {
        setRequestParams(params.toMapParam())
        return executeOnBackground()
    }
}
