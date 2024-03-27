package com.tokopedia.checkoutpayment.domain

import android.util.Log
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.checkoutpayment.data.GetPaymentWidgetDataResponse
import com.tokopedia.checkoutpayment.data.GetPaymentWidgetGqlResponse
import com.tokopedia.checkoutpayment.data.GetPaymentWidgetRequest
import com.tokopedia.checkoutpayment.data.PaymentWidgetDataResponse
import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import javax.inject.Inject

class GetPaymentWidgetUseCase @Inject constructor(
    @ApplicationContext private val repository: GraphqlRepository,
    dispatchers: CoroutineDispatchers
) : CoroutineUseCase<GetPaymentWidgetRequest, PaymentWidgetListData>(dispatchers.io) {

    companion object {
        private const val QUERY = """
            query getPaymentWidget(${"$"}params: GetPaymentWidgetParams) {
                get_payment_widget(params: ${"$"}params) {
                    error_message
                    status
                    data {
                      payment_data {
                        gateway_code
                        gateway_name
                        image_url
                        description
                        amount_validation {
                          minimum_amount
                          maximum_amount
                          minimum_amount_error_message
                          maximum_amount_error_message
                        }
                        profile_code
                        merchant_code
                        unix_timestamp
                        bid
                        callback_url
                        installment_payment_data {
                          selected_tenure
                          error_message_invalid_tenure
                          error_message_unavailable_tenure
                          error_message_top_limit
                          error_message_bottom_limit
                          credit_card_attribute {
                            bank_code
                            card_type
                            tnc_info
                            token_id
                            tenure_signature
                            masked_number
                          }
                        }
                        wallet_data {
                          wallet_type
                          wallet_amount
                          activation_details {
                            is_required
                            header_title
                            button_title
                            is_hide_digital
                            url_link
                            success_toaster
                            error_toaster
                            error_message
                          }
                          top_up_details {
                            is_required
                            header_title
                            button_title
                            is_hide_digital
                            url_link
                            success_toaster
                            error_toaster
                            error_message
                          }
                          phone_number_registration {
                            is_required
                            header_title
                            button_title
                            is_hide_digital
                            url_link
                            success_toaster
                            error_toaster
                            error_message
                          }
                        }
                        ticker_message
                        error_details {
                          message
                        }
                        mandatory_hit
                        metadata
                      }
                      payment_fee_detail {
                        title
                        amount
                        type
                        show_tooltip
                        tooltip_info
                        show_slashed
                        slashed_fee
                      }
                    }
                }
            }
        """
    }

    override fun graphqlQuery(): String {
        return QUERY
    }

    @GqlQuery("GetPaymentWidgetQuery", QUERY)
    override suspend fun execute(params: GetPaymentWidgetRequest): PaymentWidgetListData {
        val response = repository.response(listOf(GraphqlRequest(GetPaymentWidgetQuery(), GetPaymentWidgetGqlResponse::class.java, mapOf("params" to params))))
        val gqlResponse = response.getSuccessData<GetPaymentWidgetGqlResponse>()
        return mapResponse(
            gqlResponse.response.data
        )
    }

    private fun mapResponse(response: GetPaymentWidgetDataResponse): PaymentWidgetListData {
        return PaymentWidgetListData(
            paymentWidgetData = response.paymentWidgetData.map {
                mapPaymentWidgetData(it)
            },
            paymentFeeDetails = response.paymentFeeDetails.map {
                PaymentFeeDetail(
                    title = it.title,
                    amount = it.amount,
                    type = it.type,
                    showTooltip = it.showTooltip,
                    tooltipInfo = it.tooltipInfo,
                    showSlashed = it.showSlashed,
                    slashedFee = it.slashedFee
                )
            }
        )
    }

    private fun mapPaymentWidgetData(response: PaymentWidgetDataResponse): PaymentWidgetData {
        return PaymentWidgetData(
            gatewayCode = response.gatewayCode,
            gatewayName = response.gatewayName,
            imageUrl = response.imageUrl,
            description = response.description,
            amountValidation = PaymentAmountValidation(
                minimumAmount = response.amountValidation.minimumAmount,
                maximumAmount = response.amountValidation.maximumAmount,
                minimumAmountErrorMessage = response.amountValidation.minimumAmountErrorMessage,
                maximumAmountErrorMessage = response.amountValidation.maximumAmountErrorMessage
            ),
            profileCode = response.profileCode,
            merchantCode = response.merchantCode,
            unixTimestamp = response.unixTimestamp,
            bid = response.bid,
            callbackUrl = response.callbackUrl,
            installmentPaymentData = PaymentInstallmentData(
                selectedTenure = response.installmentPaymentData.selectedTenure,
                creditCardAttribute = PaymentCreditCardData(
                    bankCode = response.installmentPaymentData.creditCardAttribute.bankCode,
                    cardType = response.installmentPaymentData.creditCardAttribute.cardType,
                    tncInfo = response.installmentPaymentData.creditCardAttribute.tncInfo,
                    tokenId = response.installmentPaymentData.creditCardAttribute.tokenId,
                    tenureSignature = response.installmentPaymentData.creditCardAttribute.tenureSignature,
                    maskedNumber = response.installmentPaymentData.creditCardAttribute.maskedNumber
                ),
                errorMessageInvalidTenure = response.installmentPaymentData.errorMessageInvalidTenure,
                errorMessageUnavailableTenure = response.installmentPaymentData.errorMessageUnavailableTenure,
                errorMessageTopLimit = response.installmentPaymentData.errorMessageTopLimit,
                errorMessageBottomLimit = response.installmentPaymentData.errorMessageBottomLimit
            ),
            walletData = PaymentWalletData(
                walletType = response.walletData.walletType,
                walletAmount = response.walletData.walletAmount,
                activation = PaymentWalletAction(
                    isRequired = response.walletData.activation.isRequired,
                    headerTitle = response.walletData.activation.headerTitle,
                    buttonTitle = response.walletData.activation.buttonTitle,
                    successToaster = response.walletData.activation.successToaster,
                    errorToaster = response.walletData.activation.errorToaster,
                    errorMessage = response.walletData.activation.errorMessage,
                    isHideDigital = response.walletData.activation.isHideDigital,
                    urlLink = response.walletData.activation.urlLink,
                ),
                topUp = PaymentWalletAction(
                    isRequired = response.walletData.topUp.isRequired,
                    headerTitle = response.walletData.topUp.headerTitle,
                    buttonTitle = response.walletData.topUp.buttonTitle,
                    successToaster = response.walletData.topUp.successToaster,
                    errorToaster = response.walletData.topUp.errorToaster,
                    errorMessage = response.walletData.topUp.errorMessage,
                    isHideDigital = response.walletData.topUp.isHideDigital,
                    urlLink = response.walletData.topUp.urlLink,
                ),
                phoneNumberRegistration = PaymentWalletAction(
                    isRequired = response.walletData.phoneNumberRegistration.isRequired,
                    headerTitle = response.walletData.phoneNumberRegistration.headerTitle,
                    buttonTitle = response.walletData.phoneNumberRegistration.buttonTitle,
                    successToaster = response.walletData.phoneNumberRegistration.successToaster,
                    errorToaster = response.walletData.phoneNumberRegistration.errorToaster,
                    errorMessage = response.walletData.phoneNumberRegistration.errorMessage,
                    isHideDigital = response.walletData.phoneNumberRegistration.isHideDigital,
                    urlLink = response.walletData.phoneNumberRegistration.urlLink,
                )
            ),
            tickerMessage = response.tickerMessage,
            errorDetails = PaymentErrorDetail(
                message = response.errorDetails.message,
                button = PaymentErrorDetailButton(
                    text = response.errorDetails.button.text,
                    action = response.errorDetails.button.action
                )
            ),
            mandatoryHit = response.mandatoryHit,
            metadata = response.metadata
        )
    }
}
