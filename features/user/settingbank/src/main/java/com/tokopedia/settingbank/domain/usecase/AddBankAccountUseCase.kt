package com.tokopedia.settingbank.domain.usecase

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.settingbank.domain.GQL_ADD_BANK_ACCOUNT
import com.tokopedia.settingbank.domain.model.AddBankRequest
import com.tokopedia.settingbank.domain.model.AddBankResponse
import com.tokopedia.settingbank.domain.model.RichieAddBankAccountNewFlow
import com.tokopedia.settingbank.util.AddBankAccountException
import javax.inject.Inject


@GqlQuery("GQLAddBankAccount", GQL_ADD_BANK_ACCOUNT)
class AddBankAccountUseCase @Inject constructor(graphqlRepository: GraphqlRepository)
: GraphqlUseCase<RichieAddBankAccountNewFlow>(graphqlRepository) {

    private val BANK_ID: String = "bankID"
    private val BANK_NAME: String = "bankName"
    private val ACCOUNT_NUMBER: String = "accountNo"
    private val ACCOUNT_NAME: String = "accountName"
    private val IS_MANUAL: String = "isManual"

    fun addBankAccount(addBankRequest: AddBankRequest, onSuccess: (AddBankResponse) -> Unit,
                       onFail: (Throwable) -> Unit) {
        setTypeClass(RichieAddBankAccountNewFlow::class.java)
        setRequestParams(getAddBankParams(addBankRequest))
        setGraphqlQuery(GQLAddBankAccount.GQL_QUERY)
        execute({
            if (it.response.status == 200) {
                onSuccess(it.response)
            } else {
                onFail(AddBankAccountException(it.response.message))
            }
        }, {
            onFail(it)
        })
    }

    private fun getAddBankParams(addBankRequest: AddBankRequest): Map<String, Any> = mapOf(
            BANK_ID to addBankRequest.bankId,
            BANK_NAME to addBankRequest.bankName,
            ACCOUNT_NUMBER to addBankRequest.accountNo,
            ACCOUNT_NAME to addBankRequest.accountName,
            IS_MANUAL to addBankRequest.isManual
    )

}