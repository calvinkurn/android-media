package com.tokopedia.emoney.domain.request

import com.tokopedia.kotlin.extensions.view.ZERO

object JakCardRequestMapper {

    const val EMPTY_REQ = ""

    fun createGetPendingBalanceParam(cardData: String, cardNumber: String, lastBalance: Int): JakCardRequest {
       return createJakCardParam(
            cardNumber = cardNumber,
            cardData = cardData,
            amount = Int.ZERO,
            lastBalance = lastBalance,
            stan = EMPTY_REQ,
            refNo = EMPTY_REQ,
            action = JakCardAction.GET_PENDING_BALANCE.action
       )
    }



    private fun createJakCardParam(cardNumber: String, cardData: String, amount: Int,
                                   lastBalance: Int, stan: String, refNo: String,
                                   action: Int): JakCardRequest {
        return JakCardRequest(
            body = JakCardBody(
                cardNumber = cardNumber,
                cardData = cardData,
                amount = amount,
                lastBalance = lastBalance,
                stan = stan,
                refNo = refNo,
                action = action
            )
        )
    }
}
