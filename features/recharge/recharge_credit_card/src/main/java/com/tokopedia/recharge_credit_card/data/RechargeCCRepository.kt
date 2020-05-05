package com.tokopedia.recharge_credit_card.data

interface RechargeCCRepository {
    suspend fun postCreditCardNumber(mapParam: HashMap<String, String>): CCRedirectUrl
}