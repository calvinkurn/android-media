package com.tokopedia.thankyou_native.domain.usecase

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.thankyou_native.domain.model.*
import com.tokopedia.thankyou_native.domain.query.GQL_GET_WALLET_BALANCE
import javax.inject.Inject

@GqlQuery("GetWalletBalanceQuery", GQL_GET_WALLET_BALANCE)
class FetchWalletBalanceUseCase @Inject constructor(
    graphqlRepository: GraphqlRepository
) : GraphqlUseCase<WalletBalanceGqlResponse>(graphqlRepository) {

    fun getGoPayBalance(onResult: (WalletBalance?) -> Unit) {
        this.setTypeClass(WalletBalanceGqlResponse::class.java)
        this.setRequestParams(getRequestParams())
        this.setGraphqlQuery(GetWalletBalanceQuery.GQL_QUERY)
        this.execute(
            { result -> if(result.walletBalance.code == CODE_SUCCESS)
                    onResult(result.walletBalance)
                else onResult(null)
            }, { onResult(null) }
        )
    }

    private fun getRequestParams() = mapOf(
        KEY_PARTNER_CODE to PARTNER_CODE,
    )


    companion object {
        const val KEY_PARTNER_CODE = "partnerCode"
        const val PARTNER_CODE = "PEMUDA"
        const val CODE_SUCCESS = "SUCCESS"
    }
}
