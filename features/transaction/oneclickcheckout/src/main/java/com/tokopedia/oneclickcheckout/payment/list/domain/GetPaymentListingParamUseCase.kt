package com.tokopedia.oneclickcheckout.payment.domain

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.oneclickcheckout.common.DEFAULT_ERROR_MESSAGE
import com.tokopedia.oneclickcheckout.payment.list.data.ListingParam
import com.tokopedia.oneclickcheckout.payment.list.data.PaymentListingParamGqlResponse
import com.tokopedia.oneclickcheckout.payment.list.data.PaymentListingParamRequest
import javax.inject.Inject

interface GetPaymentListingParamUseCase {
    fun execute(param: PaymentListingParamRequest, onSuccess: (ListingParam) -> Unit, onError: (Throwable) -> Unit)
}

class GetPaymentListingParamUseCaseImpl @Inject constructor(private val graphqlUseCase: GraphqlUseCase<PaymentListingParamGqlResponse>) : GetPaymentListingParamUseCase {

    override fun execute(param: PaymentListingParamRequest, onSuccess: (ListingParam) -> Unit, onError: (Throwable) -> Unit) {
        graphqlUseCase.setGraphqlQuery(QUERY)
        graphqlUseCase.setRequestParams(mapOf(
                PARAM_MERCHANT_CODE to param.merchantCode,
                PARAM_PROFILE_CODE to param.profileCode,
                PARAM_CALLBACK_URL to param.callbackUrl,
                PARAM_ADDRESS_ID to param.addressId,
                PARAM_VERSION to param.version
        ))
        graphqlUseCase.setTypeClass(PaymentListingParamGqlResponse::class.java)
        graphqlUseCase.execute({ response: PaymentListingParamGqlResponse ->
            if (response.response.success) {
                onSuccess(response.response.data)
            } else {
                onError(MessageErrorException(response.response.message.ifEmpty { DEFAULT_ERROR_MESSAGE }))
            }
        }, { throwable: Throwable ->
            onError(throwable)
        })
    }

    private val PARAM_MERCHANT_CODE = "merchantCode"
    private val PARAM_PROFILE_CODE = "profileCode"
    private val PARAM_CALLBACK_URL = "callbackURL"
    private val PARAM_ADDRESS_ID = "addressID"
    private val PARAM_VERSION = "version"

    private val QUERY = """
        query getListingParams(
          ${"$"}merchantCode : String!
          ${"$"}profileCode : String!
          ${"$"}callbackURL : String!
          ${"$"}addressID : String!
          ${"$"}version: String
        ) {
          getListingParams(
            merchantCode: ${"$"}merchantCode
            profileCode: ${"$"}profileCode
            callbackURL: ${"$"}callbackURL
            addressID: ${"$"}addressID
            version: ${"$"}version
          ) {
            success
            message
            data {
              profile_code
              merchant_code
              user_id
              callback_url
              hash
              customer_email
              customer_msisdn
              customer_name
              address_id
            }
          }
        }
    """.trimIndent()
}