package com.tokopedia.withdraw.domain.coroutine.usecase

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.withdraw.constant.WithdrawalDomainConstant
import com.tokopedia.withdraw.domain.model.premiumAccount.CheckEligible
import com.tokopedia.withdraw.domain.model.premiumAccount.GqlRekeningPremiumResponse
import javax.inject.Inject
import javax.inject.Named

class GqlRekeningPremiumDataUseCase @Inject constructor(
        @Named(WithdrawalDomainConstant.GQL_QUERY_GET_REKENING_PREMIUM) val query: String,
        graphqlRepository: GraphqlRepository)
    : GraphqlUseCase<GqlRekeningPremiumResponse>(graphqlRepository) {

    fun getRekeningPremiumData(onSuccess: (checkEligible :CheckEligible) -> Unit,
                           onError: (Throwable) -> Unit) {
        try {
            this.setTypeClass(GqlRekeningPremiumResponse::class.java)
            this.setGraphqlQuery(query)
            this.execute(
                    {
                        onSuccess(it.checkEligible)
                    }, { error ->
                onError(error)
            }
            )
        } catch (throwable: Throwable) {
            throw throwable
        }
    }
}