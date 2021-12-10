package com.tokopedia.affiliate.usecase

import com.tokopedia.affiliate.model.raw.GQL_Affiliate_Withdrawal_Detail
import com.tokopedia.affiliate.repository.AffiliateRepository
import com.tokopedia.affiliate.model.response.AffiliateWithdrawalDetailData
import com.tokopedia.affiliate.model.response.WithdrawalInfoData
import javax.inject.Inject

class AffiliateWithdrawalDetailsUseCase@Inject constructor(
        private val repository: AffiliateRepository) {

    private fun createRequestParams(transactionID: String): HashMap<String, Any> {
        val request = HashMap<String, Any>()
        request[TransactionID] = transactionID
        return request
    }

    suspend fun getWithdrawalInfo(onSuccess: (WithdrawalInfoData) -> Unit, onError: (Throwable) -> Unit, withdrawalId: String) {
        return parseResponse(repository.getGQLData(
                GQL_Affiliate_Withdrawal_Detail,
                AffiliateWithdrawalDetailData::class.java,
                createRequestParams(withdrawalId)),onSuccess,onError)
    }

    private fun parseResponse(data: AffiliateWithdrawalDetailData, onSuccess: (WithdrawalInfoData) -> Unit, onError: (Throwable) -> Unit) {
        if (data.data?.withdrawalData?.isSuccess == 1) {
            onSuccess(data.data.withdrawalData)
        } else onError(NullPointerException(data.data?.withdrawalData?.error?.message))
    }

    companion object {
        private const val TransactionID  = "TransactionID"
    }
}