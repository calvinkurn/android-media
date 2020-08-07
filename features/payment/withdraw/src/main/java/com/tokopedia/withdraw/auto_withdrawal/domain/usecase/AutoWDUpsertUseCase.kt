package com.tokopedia.withdraw.auto_withdrawal.domain.usecase

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.withdraw.auto_withdrawal.domain.model.*
import com.tokopedia.withdraw.auto_withdrawal.domain.query.GQL_UPSERT_AUTO_WD
import javax.inject.Inject

private const val Param_AutoWDUserId = "autoWDUserId"
private const val Param_IsUpdate = "isUpdate"
private const val Param_IsQuit = "isQuit"
private const val Param_OldAutoWDScheduleId = "oldAutoWDScheduleId"
private const val Param_ScheduleType = "scheduleType"
private const val Param_ValidateToken = "validateToken"
private const val Param_Bank_AccId = "accId"
private const val Param_Bank_AccNumber = "accNo"
private const val Param_BankId = "bankId"

class AutoWDUpsertUseCase @Inject constructor(graphqlRepository: GraphqlRepository)
    : GraphqlUseCase<AutoWDUpsertResponse>(graphqlRepository) {

    fun getAutoWDUpsert(request: AutoWithdrawalUpsertRequest,
                        onSuccess: (UpsertResponse) -> Unit,
                        onError: (Throwable) -> Unit) {
        try {
            this.setTypeClass(AutoWDUpsertResponse::class.java)
            this.setGraphqlQuery(GQL_UPSERT_AUTO_WD)
            this.setRequestParams(getRequestParams(request))
            this.execute(
                    { result ->
                        onSuccess(result.upsertResponse)
                    }, { error ->
                onError(error)
            }
            )
        } catch (throwable: Throwable) {
            onError(throwable)
        }
    }

    private fun getRequestParams(request: AutoWithdrawalUpsertRequest): MutableMap<String, Any?> {
        request.apply {
            val map: MutableMap<String, Any?> = mutableMapOf(
                    Param_AutoWDUserId to autoWDStatusData.auto_wd_user_id,
                    Param_IsUpdate to isUpdating,
                    Param_IsQuit to isQuit
            )

            oldSchedule?.let {
                map[Param_OldAutoWDScheduleId] = it.autoWithdrawalScheduleId
            }
            newSchedule?.let {
                map[Param_ScheduleType] = it.scheduleType
            }

            map[Param_ValidateToken] = token ?: ""

            bankAccount?.apply {
                map[Param_Bank_AccId] = bankAccountID
                map[Param_Bank_AccNumber] = accountNo
                map[Param_BankId] = bankID
            }
            return map
        }
    }
}