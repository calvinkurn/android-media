package com.tokopedia.thankyou_native.domain.usecase

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.thankyou_native.domain.model.PurchaseInfo
import com.tokopedia.thankyou_native.domain.model.PurchaseInfoResponse
import javax.inject.Inject

class FetchPurchaseInfoUseCase @Inject constructor(
    graphqlRepository: GraphqlRepository
) : GraphqlUseCase<PurchaseInfoResponse>(graphqlRepository) {

    operator fun invoke(onSuccess: (PurchaseInfo) -> Unit,
                        onError: (Throwable) -> Unit, paymentId: String, merchantCode: String,
                        signature: String) {
        setTypeClass(PurchaseInfoResponse::class.java)
        setGraphqlQuery(QUERY)

        try {
            this.setTypeClass(PurchaseInfoResponse::class.java)
            this.setRequestParams(getRequestParams(paymentId, merchantCode, signature))
            this.setGraphqlQuery(QUERY)
            this.execute(
                { result ->
                    onSuccess(result.purchaseInfo)
                }, { error ->
                    onError(error)
                }
            )
        } catch (throwable: Throwable) {
            onError(throwable)
        }
    }

    private fun getRequestParams(paymentId: String, merchantCode: String, signature: String): Map<String, Any> {
        return mapOf(
            PARAM_TRANSACTION_ID to paymentId,
            PARAM_MERCHANT_CODE to merchantCode,
            PARAM_SIGNATURE to signature
        )
    }

    companion object {
        private const val PARAM_TRANSACTION_ID = "transactionID"
        private const val PARAM_MERCHANT_CODE = "merchantCode"
        private const val PARAM_SIGNATURE = "signature"
        private const val QUERY = """
            query getPurchaseInfo(${'$'}merchantCode: String!, ${'$'}transactionID: String!, ${'$'}signature: String!) {
              getPurchaseInfo(merchantCode: ${'$'}merchantCode, transactionID: ${'$'}transactionID, signature: ${'$'}signature) {
                summarySection {
                  type
                  name
                  price
                  priceStr
                  totalPrice
                  totalPriceStr
                  quantity
                  details
                  textType
                  colorName
                  colorPrice
                  iconURL
                }
                orderSection {
                  type
                  name
                  price
                  priceStr
                  totalPrice
                  totalPriceStr
                  quantity
                  details
                  textType
                  colorName
                  colorPrice
                  iconURL
                }
              }
            }
        """
    }
}
