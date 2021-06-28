package com.tokopedia.pms.howtopay_native.domain

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.pms.howtopay_native.data.model.AppLinkPaymentInfo
import com.tokopedia.pms.howtopay_native.data.model.HowToPayGqlResponse
import com.tokopedia.pms.howtopay_native.domain.GetGqlHowToPayInstructions.Companion.GQL_HTP_DATA
import javax.inject.Inject

@GqlQuery("HowToPay", GQL_HTP_DATA)
class GetGqlHowToPayInstructions @Inject constructor(
    graphqlRepository: GraphqlRepository
) : GraphqlUseCase<HowToPayGqlResponse>(graphqlRepository) {

    fun getGqlHowToPayInstruction(
        appLinkPaymentInfo: AppLinkPaymentInfo,
        onSuccess: (HowToPayGqlResponse, AppLinkPaymentInfo) -> Unit,
        onFail: (Throwable) -> Unit
    ) {
        try {
            this.setGraphqlQuery(HowToPay.GQL_QUERY)
            this.setTypeClass(HowToPayGqlResponse::class.java)
            this.setRequestParams(
                getRequestParams(
                    appLinkPaymentInfo.transaction_id,
                    appLinkPaymentInfo.payment_code
                )
            )
            this.execute(
                { result ->
                    onSuccess(result, appLinkPaymentInfo)
                }, { error ->
                    onFail(error)
                }
            )
        } catch (e: Exception) {
            onFail(e)
        }
    }

    private fun getRequestParams(transactionId: String, merchantCode: String) =
        mapOf(TRANSACTION_ID to transactionId, MERCHANT_CODE to merchantCode)

    companion object {

        const val TRANSACTION_ID = "transactionID"
        const val MERCHANT_CODE = "merchantCode"

        const val GQL_HTP_DATA =
            """query howToPayData(${'$'}transactionID: String!",${'$'}merchantCode:String!") {
            howToPayData(${'$'}transactionID: String!",${'$'}merchantCode:String!") {
                expiredIn
                nettAmount
                combineAmount
                gatewayImage
                gatewayName
                gatewayCode
                helpPageJSON
            }
        }
    """
    }
}

