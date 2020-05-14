package com.tokopedia.withdraw.domain.coroutine.usecase

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.withdraw.constant.WithdrawalDomainConstant
import com.tokopedia.withdraw.domain.model.BankAccount
import com.tokopedia.withdraw.domain.model.validatePopUp.GqlGetValidatePopUpResponse
import com.tokopedia.withdraw.domain.model.validatePopUp.ValidatePopUpWithdrawal
import javax.inject.Inject
import javax.inject.Named

class GQLValidateWithdrawalUseCase @Inject constructor(
        @Named(WithdrawalDomainConstant.GQL_QUERY_VALIDATE_POP_UP_WITHDRAWAL) val query: String,
        graphqlRepository: GraphqlRepository)
    : GraphqlUseCase<GqlGetValidatePopUpResponse>(graphqlRepository) {

    fun getValidatePopUpData(bankAccount: BankAccount,
                             onSuccess: (ValidatePopUpWithdrawal) -> Unit,
                             onError: (Throwable) -> Unit) {
        try {
            this.setTypeClass(GqlGetValidatePopUpResponse::class.java)
            this.setGraphqlQuery(query)
            this.setRequestParams(getRequestParams(bankAccount))
            this.execute(
                    {
                        onSuccess(it.validatePopUpWithdrawal)
                    }, { error ->
                onError(error)
            })
        } catch (throwable: Throwable) {
            onError(throwable)
        }
    }

    private fun getRequestParams(bankAccount: BankAccount): Map<String, Any> {
        return mapOf<String, Any>(
                BANK_ID to bankAccount.bankID.toString(),
                LANGUAGE to LANGUAGE_ID
        )
    }

    companion object {
        const val BANK_ID = "bankID"
        const val LANGUAGE = "language"
        const val LANGUAGE_ID = "ID"
    }

}