package com.tokopedia.pms.howtopay.domain

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.pms.howtopay.data.model.AppLinkPaymentInfo
import com.tokopedia.pms.howtopay.data.model.HowToPayGqlResponse
import com.tokopedia.pms.howtopay.domain.GetGqlHowToPayInstructions.Companion.GQL_HTP_DATA
import javax.inject.Inject

@GqlQuery("HowToPay", GQL_HTP_DATA)
class GetGqlHowToPayInstructions @Inject constructor(
    graphqlRepository: GraphqlRepository
) : GraphqlUseCase<HowToPayGqlResponse>(graphqlRepository) {

    fun getGqlHowToPayInstruction(
        onSuccess: (HowToPayGqlResponse) -> Unit,
        onFail: (Throwable) -> Unit,
        appLinkPaymentInfo: AppLinkPaymentInfo
    ) {
        try {
            this.setGraphqlQuery(HowToPay.GQL_QUERY)
            this.setTypeClass(HowToPayGqlResponse::class.java)
            this.setRequestParams(
                getRequestParams(appLinkPaymentInfo)
            )
            this.execute(
                { result ->
                    onSuccess(result)
                }, { error ->
                    onFail(error)
                }
            )
        } catch (e: Exception) {
            onFail(e)
        }
    }

    private fun getRequestParams(appLinkPaymentInfo: AppLinkPaymentInfo) =
        mapOf(
            TRANSACTION_ID to appLinkPaymentInfo.transactionId,
            MERCHANT_CODE to appLinkPaymentInfo.merchantCode
        )


    companion object {

        const val TRANSACTION_ID = "transactionID"
        const val MERCHANT_CODE = "merchantCode"

        const val GQL_HTP_DATA =
            """query howToPayData(${'$'}transactionID: String!,${'$'}merchantCode:String!) {
            howToPayData(transactionID: ${'$'}transactionID, merchantCode: ${'$'}merchantCode) {
                expiredIn
                leftPaymentAmountUnf
                combineAmount
                gatewayImage
                gatewayName
                gatewayCode
                transactionCode
                paymentCodeHint
                hideCopyAmount
                hideCopyAccountNum
                isOfflineStore
                isManualTransfer
                destBankName
                destBankBranch
                destAccountName
                helpPageJSON
            }
        }
    """
    }
}

