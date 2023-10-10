package com.tokopedia.emoney.domain.request

import com.google.gson.Gson

object BCAFlazzRequestMapper {

    private const val EMPTY_REQ = ""
    private const val EMPTY_AMOUNT = 0

    fun createGetPendingBalanceParam(gson: Gson, cardNumber: String, lastBalance: Int,
                                     cardType: String): String {
        return createJsonStringBody(
            createBCAFlazzParam(
                cardNumber = cardNumber,
                cardData = EMPTY_REQ,
                amount = EMPTY_AMOUNT,
                lastBalance = lastBalance,
                transactionId = EMPTY_REQ,
                cardType = cardType,
                action = BCAFlazzAction.GET_PENDING_BALANCE.action
            ), gson
        )
    }

    fun createGetBCAGenerateTrxId(gson: Gson, cardNumber: String, lastBalance: Int,
                                  cardType: String): String {
        return createJsonStringBody(
            createBCAFlazzParam(
                cardNumber = cardNumber,
                cardData = EMPTY_REQ,
                amount = EMPTY_AMOUNT,
                lastBalance = lastBalance,
                transactionId = EMPTY_REQ,
                cardType = cardType,
                action = BCAFlazzAction.GENERATE_TRX_ID.action
            ), gson
        )
    }

    fun createGetBCAGenerateSessionKey(gson: Gson, cardNumber: String, cardData: String, lastBalance: Int,
                                 transactionId: String, cardType: String): String {
        return createJsonStringBody(
            createBCAFlazzParam(
                cardNumber = cardNumber,
                cardData = cardData,
                amount = EMPTY_AMOUNT,
                lastBalance = lastBalance,
                transactionId = transactionId,
                cardType = cardType,
                action = BCAFlazzAction.GENERATE_SESSION_KEY.action
            ), gson
        )
    }

    fun createGetBCADataBetweenTopUp(gson: Gson, cardNumber: String, cardData: String, amount: Int, lastBalance: Int,
                                       transactionId: String, cardType: String): String {
        return createJsonStringBody(
            createBCAFlazzParam(
                cardNumber = cardNumber,
                cardData = cardData,
                amount = amount,
                lastBalance = lastBalance,
                transactionId = transactionId,
                cardType = cardType,
                action = BCAFlazzAction.BETWEEN_TOP_UP.action
            ), gson
        )
    }

    fun createGetBCADataACKTopUp(gson: Gson, cardNumber: String, cardData: String, updatedLastBalance: Int,
                                     transactionId: String, cardType: String): String {
        return createJsonStringBody(
            createBCAFlazzParam(
                cardNumber = cardNumber,
                cardData = cardData,
                amount = EMPTY_AMOUNT,
                lastBalance = updatedLastBalance,
                transactionId = transactionId,
                cardType = cardType,
                action = BCAFlazzAction.ACK_AFTER_TOP_UP_2.action
            ), gson
        )
    }

    fun createGetBCADataReversal(gson: Gson, cardNumber: String, cardData: String, amount: Int, lastBalance: Int,
                                 transactionId: String, cardType: String): String {
        return createJsonStringBody(
            createBCAFlazzParam(
                cardNumber = cardNumber,
                cardData = cardData,
                amount = amount,
                lastBalance = lastBalance,
                transactionId = transactionId,
                cardType = cardType,
                action = BCAFlazzAction.REVERSAL.action
            ), gson
        )
    }

    fun createEncryptedParam(encKey: String, encPayload: String): BCAFlazzRequest {
        return BCAFlazzRequest(
            body = CommonBodyEnc(
                encKey, encPayload
            )
        )
    }

    private fun createJsonStringBody(bcaFlazzBody: BCAFlazzBody, gson: Gson): String {
        return gson.toJson(bcaFlazzBody)
    }

    private fun createBCAFlazzParam(cardNumber: String, cardData: String, amount: Int, lastBalance: Int,
                                    transactionId: String, cardType: String, action: Int): BCAFlazzBody {
        return BCAFlazzBody(
            cardNumber = cardNumber,
            cardData = cardData,
            amount = amount,
            lastBalance = lastBalance,
            transactionID = transactionId,
            cardType = cardType,
            action = action
        )
    }

}
