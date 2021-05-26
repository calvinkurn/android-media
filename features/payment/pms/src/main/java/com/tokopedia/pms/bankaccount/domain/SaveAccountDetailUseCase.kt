package com.tokopedia.pms.bankaccount.domain

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.pms.bankaccount.data.model.DataEditTransfer
import com.tokopedia.pms.bankaccount.data.model.EditTransfer
import javax.inject.Inject

@GqlQuery("SaveAccountQuery", SaveAccountDetailUseCase.GQL_SAVE_BANK_ACCOUNT)
class SaveAccountDetailUseCase @Inject constructor(
    graphqlRepository: GraphqlRepository
) : GraphqlUseCase<DataEditTransfer>(graphqlRepository) {

    fun saveDetailAccount(
        transactionId: String, merchantCode: String,
        accountName: String, accountNumber: String, notes: String, destinationBankId: String,
        onSuccess: (EditTransfer) -> Unit, onError: (Throwable) -> Unit
    ) {
        this.setRequestParams(
            getRequestParams(
                transactionId,
                merchantCode,
                accountName,
                accountNumber,
                notes,
                destinationBankId
            )
        )
        this.setTypeClass(DataEditTransfer::class.java)
        this.setGraphqlQuery(SaveAccountQuery.GQL_QUERY)
        this.execute(
            { result ->
                onSuccess(result.editTransfer)
            }, { error ->
                onError(error)
            }
        )
    }

    private fun getRequestParams(
        transactionId: String, merchantCode: String,
        accountName: String, accountNumber: String, notes: String, destinationBankId: String
    ) = mapOf(
        TRANSACTION_ID to transactionId,
        MERCHANT_CODE to merchantCode,
        ACC_NAME to accountName,
        ACC_NO to accountNumber,
        NOTE to notes,
        BANK_ID to destinationBankId.toInt()
    )

    companion object {
        const val TRANSACTION_ID = "transactionID"
        const val MERCHANT_CODE = "merchantCode"
        const val ACC_NAME = "accName"
        const val ACC_NO = "accNo"
        const val NOTE = "note"
        const val BANK_ID = "bankID"
        const val GQL_SAVE_BANK_ACCOUNT = """
    mutation editTransfer(${'$'}transactionID:String!, ${'$'}merchantCode:String!, ${'$'}accName:String!, ${'$'}accNo:String!, ${'$'}bankID:Int!, ${'$'}note:String!){
    editTransfer(transactionID:${'$'}transactionID, merchantCode:${'$'}merchantCode, accName:${'$'}accName, accNo:${'$'}accNo, bankID:${'$'}bankID, note:${'$'}note){
        success
        message
    }
}
"""
    }

}