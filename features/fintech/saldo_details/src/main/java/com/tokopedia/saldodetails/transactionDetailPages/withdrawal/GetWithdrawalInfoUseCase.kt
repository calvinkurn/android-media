package com.tokopedia.saldodetails.transactionDetailPages.withdrawal

import android.content.Context
import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.saldodetails.R
import com.tokopedia.saldodetails.commom.analytics.SaldoDetailsConstants.DetailScreenParams.Companion.WITHDRAWAL_ID
import com.tokopedia.saldodetails.commom.di.scope.SaldoDetailsScope
import com.tokopedia.saldodetails.commom.query.GQL_WITHDRAWAL_DETAIL
import javax.inject.Inject

@GqlQuery("GetWithdrawalInfoQuery", GQL_WITHDRAWAL_DETAIL)
class GetWithdrawalInfoUseCase
@Inject constructor(
    @SaldoDetailsScope val context: Context,
    graphqlRepository: GraphqlRepository
) : GraphqlUseCase<ResponseGetWithdrawalInfo>(graphqlRepository) {

    fun getWithdrawalInfo(onSuccess: (WithdrawalInfoData) -> Unit, onError: (Throwable) -> Unit, withdrawalId: String) {
        try {
            this.setTypeClass(ResponseGetWithdrawalInfo::class.java)
            this.setRequestParams(getRequestParams(withdrawalId))
            this.setGraphqlQuery(GetWithdrawalInfoQuery.GQL_QUERY)
            execute({
                parseResponse(it.data, onSuccess, onError)
            }, {
                onError(it)
            })
        } catch (e: Exception) {
            onError(e)
        }
    }

    private fun parseResponse(data: WithdrawalInfoResult, onSuccess: (WithdrawalInfoData) -> Unit, onError: (Throwable) -> Unit) {
        if (data.withdrawalData.isSuccess == 1) {
            data.withdrawalData.withdrawalInfoData.feeDetailData =
                getFeeDetailData(data.withdrawalData.withdrawalInfoData)
            onSuccess(data.withdrawalData.withdrawalInfoData)
        } else onError(NullPointerException(data.error.getOrNull(0)))
    }

    private fun getFeeDetailData(data: WithdrawalInfoData) =
        arrayListOf(
            FeeDetailData(context.getString(R.string.saldo_withdrawal_amount), data.amount,DEFAULT_LATE_STATUS,
                DEFAULT_LATE_MESSAGE),
            FeeDetailData(context.getString(R.string.saldo_withdrawal_fee), data.fee,DEFAULT_LATE_STATUS,DEFAULT_LATE_MESSAGE),
            FeeDetailData(context.getString(R.string.saldo_transferred_amount), data.transferredAmount,DEFAULT_LATE_STATUS,DEFAULT_LATE_MESSAGE)
        )

    private fun getRequestParams(withdrawalId: String) =
        mutableMapOf(
            WITHDRAWAL_ID to withdrawalId,
        )

    companion object
    {
        const val DEFAULT_LATE_STATUS = false
        const val DEFAULT_LATE_MESSAGE=""
    }
}