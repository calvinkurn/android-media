package com.tokopedia.emoney.domain.request

import com.google.gson.Gson
import com.tokopedia.kotlin.extensions.view.ZERO

object JakCardRequestMapper {

    private const val EMPTY_REQ = ""

    fun createGetPendingBalanceParam(cardData: String, cardNumber: String, lastBalance: Int, gson: Gson): String {
       return createJsonStringBody(createJakCardParam(
            cardNumber = cardNumber,
            cardData = cardData,
            amount = Int.ZERO,
            lastBalance = lastBalance,
            stan = EMPTY_REQ,
            refNo = EMPTY_REQ,
            action = JakCardAction.GET_PENDING_BALANCE.action
       ), gson)
    }

    fun createGetTopUpParam(cardData: String, cardNumber: String, lastBalance: Int, amount: Int, gson: Gson): String {
        return createJsonStringBody(createJakCardParam(
            cardNumber = cardNumber,
            cardData = cardData,
            amount = amount,
            lastBalance = lastBalance,
            stan = EMPTY_REQ,
            refNo = EMPTY_REQ,
            action = JakCardAction.TOP_UP.action
        ), gson)
    }

    fun createGetTopUpConfirmationParam(cardData: String, cardNumber: String, lastBalance: Int, amount: Int, stan: String, refNo: String, gson: Gson): String {
        return createJsonStringBody(createJakCardParam(
            cardNumber = cardNumber,
            cardData = cardData,
            amount = amount,
            lastBalance = lastBalance,
            stan = stan,
            refNo = refNo,
            action = JakCardAction.TOP_UP_CONFIRMATION.action
        ), gson)
    }

    fun createEncryptedParam(encKey: String, encPayload: String): JakCardRequest {
        return JakCardRequest(
            body = JakCardBodyEnc(
                encKey, encPayload
            )
        )
    }

    private fun createJsonStringBody(jakCardBody: JakCardBody, gson: Gson): String {
        return gson.toJson(jakCardBody)
    }

    private fun createJakCardParam(cardNumber: String, cardData: String, amount: Int,
                                   lastBalance: Int, stan: String, refNo: String,
                                   action: Int): JakCardBody {
        return JakCardBody(
                cardNumber = cardNumber,
                cardData = cardData,
                amount = amount,
                lastBalance = lastBalance,
                stan = stan,
                refNo = refNo,
                action = action
            )
    }
}
