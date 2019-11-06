package com.tokopedia.emoney.view.electronicmoney

import android.content.Intent

interface ElectronicMoney {

    fun processTagIntent(intent: Intent)
}

interface MandiriElectronicMoney: ElectronicMoney {
    fun sendCommandToCard(payload: String, id: Int, mapAttributes: HashMap<String, Any>)
}

interface BriElectronicMoney: ElectronicMoney {
    fun sendCommandToCard(intent: Intent)
}