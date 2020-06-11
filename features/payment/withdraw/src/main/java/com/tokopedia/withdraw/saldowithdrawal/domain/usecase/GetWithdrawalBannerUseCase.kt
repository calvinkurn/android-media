package com.tokopedia.withdraw.saldowithdrawal.domain.usecase

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.withdraw.saldowithdrawal.domain.helper.WithdrawalDomainConstant
import com.tokopedia.withdraw.saldowithdrawal.domain.model.RekeningData
import javax.inject.Inject
import javax.inject.Named

class GetWithdrawalBannerUseCase @Inject constructor(
        @Named(WithdrawalDomainConstant.GQL_QUERY_WITHDRAWAL_BANNER) val query: String,
        private val graphqlRepository: GraphqlRepository
) : GraphqlUseCase<RekeningData>(graphqlRepository) {


}