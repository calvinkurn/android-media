package com.tokopedia.checkoutpayment.domain

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.checkoutpayment.data.GetPaymentWidgetRequest
import com.tokopedia.checkoutpayment.data.GetPaymentWidgetResponse
import com.tokopedia.checkoutpayment.data.PaymentAmountValidationResponse
import com.tokopedia.checkoutpayment.data.PaymentFeeDetailResponse
import com.tokopedia.checkoutpayment.data.PaymentWidgetDataResponse
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import javax.inject.Inject

class GetPaymentWidgetUseCase @Inject constructor(
    @ApplicationContext private val repository: GraphqlRepository,
    dispatchers: CoroutineDispatchers
) : CoroutineUseCase<GetPaymentWidgetRequest, PaymentWidgetListData>(dispatchers.io) {

    override fun graphqlQuery(): String {
        return """
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
                    }
                    wallet_data {
                      wallet_type
                      wallet_amount
                      phone_numbered_registered
                    }
                    ticker_message
                    error_details {
                      message
                    }
                    mandatory_hit
                    metadata
                  }
                }
              }
            }
        """.trimIndent()
    }

    override suspend fun execute(params: GetPaymentWidgetRequest): PaymentWidgetListData {
        mapResponse(repository.response(listOf(GraphqlRequest(graphqlQuery(), GetPaymentWidgetResponse::class.java, mapOf("params" to params)))).getSuccessData())
        return mapResponse(
            GetPaymentWidgetResponse(
                paymentWidgetData = listOf(
                    PaymentWidgetDataResponse(
                        gatewayCode = "ALFAMART",
                        gatewayName = "Alfamart / Alfamidi / Lawson / Dan+Dan",
                        imageUrl = "https://images.tokopedia.net/img/toppay/payment-logo/alfamart.png",
                        amountValidation = PaymentAmountValidationResponse(
                            minimumAmount = 10000,
                            maximumAmount = 50000,
                            minimumAmountErrorMessage = "too low",
                            maximumAmountErrorMessage = "too high"
                        )
                    )
                ),
                paymentFeeDetails = listOf(
                    PaymentFeeDetailResponse(
                        title = "testing title",
                        amount = 10000.0,
                        showTooltip = true,
                        showSlashed = true,
                        slashedFee = 3000,
                        tooltipInfo = "hallo"
                    ),
                    PaymentFeeDetailResponse(
                        title = "testing title2",
                        amount = 1000.0,
                        showTooltip = true,
                        showSlashed = true,
                        slashedFee = 3000,
                        tooltipInfo = "hallo"
                    )
                )
            )
        )
    }

    private fun mapResponse(response: GetPaymentWidgetResponse): PaymentWidgetListData {
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
                phoneNumberRegistered = response.walletData.phoneNumberRegistered,
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
