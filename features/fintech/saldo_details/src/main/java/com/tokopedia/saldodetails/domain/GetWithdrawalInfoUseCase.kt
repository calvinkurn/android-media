package com.tokopedia.saldodetails.domain

import android.content.Context
import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.saldodetails.R
import com.tokopedia.saldodetails.commom.analytics.SaldoDetailsConstants.DetailScreenParams.Companion.WITHDRAWAL_ID
import com.tokopedia.saldodetails.di.SaldoDetailsScope
import com.tokopedia.saldodetails.domain.query.GQL_WITHDRAWAL_DETAIL
import com.tokopedia.saldodetails.response.model.saldo_detail_info.FeeDetailData
import com.tokopedia.saldodetails.response.model.saldo_detail_info.ResponseGetWithdrawalInfo
import com.tokopedia.saldodetails.response.model.saldo_detail_info.WithdrawalInfoData
import com.tokopedia.saldodetails.response.model.saldo_detail_info.WithdrawalInfoResult
import javax.inject.Inject

@GqlQuery("GetWithdrawalInfoQuery", GQL_WITHDRAWAL_DETAIL)
class GetWithdrawalInfoUseCase
@Inject constructor(
    @SaldoDetailsScope val context: Context,
    graphqlRepository: GraphqlRepository
) : GraphqlUseCase<ResponseGetWithdrawalInfo>(graphqlRepository) {

    fun getWithdrawalInfo(withdrawalId: String, onSuccess: (WithdrawalInfoData) -> Unit, onError: (Throwable) -> Unit) {
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
            FeeDetailData(context.getString(R.string.saldo_withdrawal_amount), data.amount),
            FeeDetailData(context.getString(R.string.saldo_withdrawal_fee), data.fee),
            FeeDetailData(context.getString(R.string.saldo_transferred_amount), data.transferredAmount)
        )

    private fun getRequestParams(withdrawalId: String) =
        mutableMapOf(
            WITHDRAWAL_ID to withdrawalId,
        )
}