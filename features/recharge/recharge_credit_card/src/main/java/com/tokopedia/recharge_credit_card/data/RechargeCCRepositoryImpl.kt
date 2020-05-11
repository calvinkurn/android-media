package com.tokopedia.recharge_credit_card.data

import java.io.IOException
import javax.inject.Inject

class RechargeCCRepositoryImpl @Inject constructor(private val rechargeCCApi: RechargeCCApi) : RechargeCCRepository {

    override suspend fun postCreditCardNumber(mapParam: HashMap<String, String>): CCRedirectUrl {
        mapParam[PARAM_ACTION] = VALUE_ACTION
        val response = rechargeCCApi.postCreditCard(mapParam)
        if (response.isSuccessful) {
            return response.body()!!.data
        }
        throw IOException(ERROR_DEFAULT)
    }

    companion object {
        const val PARAM_ACTION = "action"

        const val VALUE_ACTION = "init_data"
        const val ERROR_DEFAULT = "Terjadi Kesalahan, silakan ulang beberapa saat lagi"
    }
}