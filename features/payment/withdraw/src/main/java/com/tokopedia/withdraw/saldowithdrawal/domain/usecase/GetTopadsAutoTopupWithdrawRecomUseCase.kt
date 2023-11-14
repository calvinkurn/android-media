package com.tokopedia.withdraw.saldowithdrawal.domain.usecase

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.withdraw.saldowithdrawal.domain.helper.WithdrawalDomainConstant
import com.tokopedia.withdraw.saldowithdrawal.domain.model.GqlTopadsAutoTopupWithdrawRecomResponse
import com.tokopedia.usecase.coroutines.Result
import javax.inject.Inject
import javax.inject.Named

class GetTopadsAutoTopupWithdrawRecomUseCase @Inject constructor(
    @Named(WithdrawalDomainConstant.GQL_TOPADS_AUTO_TOPUP_WITHDRAW_RECOM) private val query: String,
    graphqlRepository: GraphqlRepository
) : SaldoGQLUseCase<GqlTopadsAutoTopupWithdrawRecomResponse>(graphqlRepository) {

    suspend operator fun invoke(shopId: String): Result<GqlTopadsAutoTopupWithdrawRecomResponse> {
        this.setTypeClass(GqlTopadsAutoTopupWithdrawRecomResponse::class.java)
        this.setGraphqlQuery(query)
        this.setRequestParams(mapOf(SHOP_ID to shopId))
        return executeUseCase()
    }

    companion object {
        private const val SHOP_ID = "shop_id"
    }
}
