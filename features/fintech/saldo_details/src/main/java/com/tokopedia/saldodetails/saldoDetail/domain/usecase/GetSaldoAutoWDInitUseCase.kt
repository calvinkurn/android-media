package com.tokopedia.saldodetails.saldoDetail.domain.usecase

import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.saldodetails.commom.analytics.SaldoDetailsConstants
import com.tokopedia.saldodetails.commom.di.module.GqlQueryModule
import com.tokopedia.saldodetails.commom.utils.GqlUseCaseWrapper
import com.tokopedia.saldodetails.saldoDetail.domain.data.GqlAutoWDInitResponse
import com.tokopedia.saldodetails.saldoDetail.domain.data.GqlSaldoBalanceResponse
import javax.inject.Inject
import javax.inject.Named

class GetSaldoAutoWDInitUseCase @Inject constructor(
    val gqlUseCaseWrapper: GqlUseCaseWrapper
) {

    suspend operator fun invoke(): GqlAutoWDInitResponse {
        return gqlUseCaseWrapper.getResponse(GqlAutoWDInitResponse::class.java, QUERY, HashMap())
    }

    companion object {
        private const val QUERY = """
            query RichieAutoWDInit {
              RichieAutoWDInit {
                code
                message
                data{
                  is_eligible
                  status
                  schedule_wording
                  redirect_link
                }
              }
            }
        """
    }
}
