package com.tokopedia.withdraw.saldowithdrawal.domain.usecase

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.withdraw.saldowithdrawal.domain.helper.WithdrawalDomainConstant
import com.tokopedia.withdraw.saldowithdrawal.domain.model.GQLSubmitWithdrawalResponse
import com.tokopedia.withdraw.saldowithdrawal.domain.model.WithdrawalRequest
import javax.inject.Inject
import javax.inject.Named

class GQLSubmitWithdrawalUseCase @Inject constructor(
        @Named(WithdrawalDomainConstant.GQL_QUERY_SUBMIT_WITHDRAWAL) val query: String,
        graphqlRepository: GraphqlRepository)
    : SaldoGQLUseCase<GQLSubmitWithdrawalResponse>(graphqlRepository) {

    suspend fun submitWithdrawal(withdrawalRequest: WithdrawalRequest,
                                 validateToken: String): Result<GQLSubmitWithdrawalResponse> {
        this.setTypeClass(GQLSubmitWithdrawalResponse::class.java)
        this.setGraphqlQuery(query)
        this.setRequestParams(getRequestParams(withdrawalRequest, validateToken))
        return executeUseCase()
    }

    private fun getRequestParams(withdrawalRequest: WithdrawalRequest,
                                 validateToken: String): Map<String, Any?> {
        return mutableMapOf<String, Any?>(
                PARAM_ACTION_USER to ACTION_USER_VALUE,
                PARAM_DEVICE_TYPE to MOBILE_DEVICE,
                EMAIL to withdrawalRequest.email,
                ACCOUNT_ID to withdrawalRequest.bankAccount.bankAccountID.toString(),
                PARAM_LANG to LANGAUGE,
                PARAM_IS_SELLER to withdrawalRequest.isSellerWithdrawal,
                USER_ID to withdrawalRequest.userId,
                TYPE to WITHDRAWAL_TYPE_VALUE,
                TOKEN to "",
                MASTER_EMAIL to "",
                MASTER_ID to "",
                VALIDATE_TOKEN to validateToken,
                PROGRAM to withdrawalRequest.programName,
                PARAM_BANK_ACC_NAME to withdrawalRequest.bankAccount.accountName,
                PARAM_BANK_ACC_NUMBER to withdrawalRequest.bankAccount.accountNo,
                PARAM_BANK_ID to withdrawalRequest.bankAccount.bankID.toString(),
                PARAM_BANK_NAME to withdrawalRequest.bankAccount.bankName,
                AMOUNT to withdrawalRequest.withdrawal.toString(),
                PARAM_IS_JOIN_RP to withdrawalRequest.isJoinRekeningPremium)
    }


    companion object {
        private const val LANGAUGE = "ID"
        private const val WITHDRAWAL_TYPE_VALUE = 1
        private const val ACTION_USER_VALUE = "user"

        private const val MOBILE_DEVICE = "mobile"
        private const val PARAM_IS_SELLER = "isSeller"
        private const val AMOUNT = "amount"
        private const val USER_ID = "userId"
        private const val EMAIL = "email"
        private const val PARAM_ACTION_USER = "action"
        private const val TYPE = "type"
        private const val PARAM_DEVICE_TYPE = "deviceType"
        private const val TOKEN = "token"
        private const val MASTER_EMAIL = "masterEmail"
        private const val MASTER_ID = "masterID"
        private const val PARAM_LANG = "lang"
        private const val ACCOUNT_ID = "accountID"
        private const val PARAM_BANK_ACC_NAME = "accountName"
        private const val PARAM_BANK_ACC_NUMBER = "accountNumber"
        private const val PARAM_BANK_ID = "bankId"
        private const val PARAM_BANK_NAME = "bankName"
        private const val VALIDATE_TOKEN = "validateToken"
        private const val PROGRAM = "program"
        private const val PARAM_IS_JOIN_RP = "isJoinRP"
    }
}
