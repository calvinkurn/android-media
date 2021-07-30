package com.tokopedia.saldodetails.domain

import android.content.Context
import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.saldodetails.R
import com.tokopedia.saldodetails.di.SaldoDetailsScope
import com.tokopedia.saldodetails.domain.GetWithdrawalInfoUseCase.Companion.GQL_WITHDRAWAL_DETAIL
import com.tokopedia.saldodetails.response.model.saldo_detail_info.FeeDetailData
import com.tokopedia.saldodetails.response.model.saldo_detail_info.ResponseGetWithdrawalInfo
import com.tokopedia.saldodetails.response.model.saldo_detail_info.WithdrawalInfoData
import javax.inject.Inject

@GqlQuery("GetWithdrawalInfoQuery", GQL_WITHDRAWAL_DETAIL)
class GetWithdrawalInfoUseCase
@Inject constructor(
    @SaldoDetailsScope val context: Context,
    graphqlRepository: GraphqlRepository
) :
    GraphqlUseCase<ResponseGetWithdrawalInfo>(graphqlRepository) {

    fun getWithdrawalInfo(
        withdrawalId: String,
        onSuccess: (WithdrawalInfoData) -> Unit,
        onError: (Throwable) -> Unit
    ) {
        try {
            this.setTypeClass(ResponseGetWithdrawalInfo::class.java)
            this.setRequestParams(getRequestParams(withdrawalId))
            this.setGraphqlQuery(GetWithdrawalInfoQuery.GQL_QUERY)
            execute({
                if (it.data.withdrawalData.isSuccess == 1) {
                    it.data.withdrawalData.withdrawalInfoData.feeDetailData =
                        getFeeDetailData(it.data.withdrawalData.withdrawalInfoData)
                    onSuccess(it.data.withdrawalData.withdrawalInfoData)
                } else onError(NullPointerException(it.data.error.getOrNull(0)))
            }, {
                onError(it)
            })
        } catch (e: Exception) {
            onError(e)
        }

    }

    private fun getFeeDetailData(data: WithdrawalInfoData) =
        arrayListOf(
            FeeDetailData(context.getString(R.string.saldo_withdrawal_amount), data.amount),
            FeeDetailData(context.getString(R.string.saldo_withdrawal_fee), data.fee),
            FeeDetailData(
                context.getString(R.string.saldo_transferred_amount),
                data.transferredAmount
            )
        )


    private fun getRequestParams(withdrawalId: String): MutableMap<String, Any?> {
        return mutableMapOf(
            WITHDRAWAL_ID to withdrawalId,
        )
    }

    companion object {
        private val WITHDRAWAL_ID = "withdrawalID"

        const val GQL_WITHDRAWAL_DETAIL =
            """query RichieGetWithdrawalInfo(${'$'}withdrawalID: String) {
            RichieGetWithdrawalInfo(withdrawalID: ${'$'}withdrawalID) {
                data {
                    is_success
                    withdrawal_info {
                        withdrawal_id
                        status
                        status_string
                        status_color
                        amount
                        fee
                        transferred_amount
                        create_time
                        bank_name
                        acc_no
                        acc_name
                        history {
                          status
                          title
                          description
                          create_time
                        }
                    }
                }
                message_error
            }
        }"""
    }

}