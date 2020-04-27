package com.tokopedia.emoney.view.electronicmoney

import android.content.Intent

interface ElectronicMoney {

    fun processTagIntent(intent: Intent)
    fun writeBalanceToCard(intent: Intent, payload: String, id: Int, mapAttributes: HashMap<String, Any>)
}