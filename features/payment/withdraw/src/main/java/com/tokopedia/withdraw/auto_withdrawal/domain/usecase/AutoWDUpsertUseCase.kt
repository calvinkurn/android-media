package com.tokopedia.withdraw.auto_withdrawal.domain.usecase

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.withdraw.auto_withdrawal.domain.model.*
import com.tokopedia.withdraw.auto_withdrawal.domain.query.GQL_UPSERT_AUTO_WD
import javax.inject.Inject

class AutoWDUpsertUseCase @Inject constructor(graphqlRepository: GraphqlRepository)
    : GraphqlUseCase<AutoWDUpsertResponse>(graphqlRepository) {

    fun getAutoWDUpsert(paramsMap: Map<String, Any?>,
                        onSuccess: (UpsertResponse) -> Unit,
                        onError: (Throwable) -> Unit) {
        try {
            this.setTypeClass(AutoWDUpsertResponse::class.java)
            this.setGraphqlQuery(GQL_UPSERT_AUTO_WD)
            this.setRequestParams(paramsMap)
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

    fun getRequestParams(request: AutoWithdrawalUpsertRequest): MutableMap<String, Any?> {
        request.apply {
            val map: MutableMap<String, Any?> = mutableMapOf(
                    "autoWDUserId" to autoWDStatusData.auto_wd_user_id,
                    "isUpdate" to isUpdating,
                    "isQuit" to isQuit
            )

            oldSchedule?.let {
                map["oldAutoWDScheduleId"] = it.autoWithdrawalScheduleId
            }
            newSchedule?.let {
                map["scheduleType"] = it.scheduleType
            }

            map["validateToken"] = token ?: ""

            bankAccount?.apply {
                map["accId"] = bankAccountID
                map["accNo"] = accountNo
                map["bankId"] = bankID
            }
            return map
        }
    }
}