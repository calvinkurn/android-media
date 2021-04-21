package com.tokopedia.checkout.domain.mapper

import com.google.gson.Gson
import com.tokopedia.checkout.data.model.response.checkout.*
import com.tokopedia.checkout.domain.model.checkout.*
import java.util.*
import javax.inject.Inject

class CheckoutMapper @Inject constructor(private val gson: Gson) {

    fun convertCheckoutData(checkoutResponse: CheckoutResponse): CheckoutData {
        val checkoutDataResponse = checkoutResponse.data
        return CheckoutData().apply {
            jsonResponse = gson.toJson(checkoutResponse)
            isError = checkoutDataResponse.success != 1
            errorMessage = checkoutDataResponse.error
            priceValidationData = mapPriceValidationData(checkoutDataResponse.data.priceValidation)
            errorReporter = mapErrorReporter(checkoutResponse.errorReporter)
            if (!isError && checkoutDataResponse.data.parameter.transactionId != null) {
                transactionId = checkoutDataResponse.data.parameter.transactionId
                paymentId = checkoutDataResponse.data.parameter.transactionId
                queryString = checkoutDataResponse.data.queryString
                redirectUrl = checkoutDataResponse.data.redirectUrl
                callbackSuccessUrl = checkoutDataResponse.data.callbackUrl
                callbackFailedUrl = checkoutDataResponse.data.callbackUrl
            }
        }
    }

    private fun mapPriceValidationData(priceValidation: PriceValidation): PriceValidationData {
        return PriceValidationData().apply {
            isUpdated = priceValidation.isUpdated
            message = mapMessageData(priceValidation.message)
            trackerData = mapTrackerData(priceValidation.trackerData)
        }
    }

    private fun mapTrackerData(tracker: Tracker): TrackerData {
        return TrackerData().apply {
            campaignType = tracker.campaignType
            productChangesType = tracker.productChangesType
            val tmpProductIds: MutableList<String> = ArrayList()
            for (aLong in tracker.productIds) {
                tmpProductIds.add(aLong.toString())
            }
            productIds = tmpProductIds
        }
    }

    private fun mapMessageData(message: Message): MessageData {
        return MessageData().apply {
            title = message.title
            desc = message.desc
            action = message.action
        }
    }

    private fun mapErrorReporter(errorReporterResponse: ErrorReporterResponse): ErrorReporter {
        return ErrorReporter().apply {
            eligible = errorReporterResponse.eligible
            texts = mapErrorReporterText(errorReporterResponse.texts)
        }
    }

    private fun mapErrorReporterText(texts: ErrorReporterTextResponse): ErrorReporterText {
        return ErrorReporterText().apply {
            submitTitle = texts.submitTitle
            submitDescription = texts.submitDescription
            submitButton = texts.submitButton
            cancelButton = texts.cancelButton
        }
    }
}