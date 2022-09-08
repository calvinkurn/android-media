package com.tokopedia.ovop2p.domain.usecase

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.ovop2p.constants.GQL_GET_WALLET_DETAIL
import com.tokopedia.ovop2p.domain.model.WalletDataBase
import javax.inject.Inject


@GqlQuery("WalletDetailQuery", GQL_GET_WALLET_DETAIL)
class GetWalletBalanceUseCase @Inject constructor(graphqlRepository: GraphqlRepository) :
    GraphqlUseCase<WalletDataBase>(graphqlRepository) {

    fun getWalletDetail(
        onSuccess: (WalletDataBase) -> Unit,
        onError: (Throwable) -> Unit
    ) {
        try {
            this.setTypeClass(WalletDataBase::class.java)
            this.setGraphqlQuery(WalletDetailQuery.GQL_QUERY)
            this.execute(
                { result ->
                    result.wallet?.let {
                        onSuccess(result)
                    }?: kotlin.run {
                        onError(NullPointerException("Wallet Empty"))
                    }
                }, { error ->
                    onError(error)
                }
            )
        } catch (throwable: Throwable) {
            onError(throwable)
        }
    }

}