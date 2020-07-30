package com.tokopedia.saldodetails.usecase

import com.tokopedia.saldodetails.data.GqlUseCaseWrapper
import com.tokopedia.saldodetails.di.GqlQueryModule.Companion.DEPOSITE_DETAIL_FOR_ALL_QUERY
import com.tokopedia.saldodetails.response.model.GqlAllDepositSummaryResponse
import javax.inject.Inject
import javax.inject.Named

class GetDepositSummaryUseCase @Inject
constructor(@Named(DEPOSITE_DETAIL_FOR_ALL_QUERY) private val query: String, private val graphqlUseCase: GqlUseCaseWrapper) {

    suspend fun execute(map: Map<String, Any>): GqlAllDepositSummaryResponse {
        return graphqlUseCase.getResponse(GqlAllDepositSummaryResponse::class.java, query,
                map)


    }
}
