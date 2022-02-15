package com.tokopedia.tradein.viewmodel

import android.content.Context
import com.tokopedia.common_tradein.model.TradeInPDPData
import com.tokopedia.common_tradein.utils.TradeInPDPHelper
import kotlinx.coroutines.CoroutineScope
import javax.inject.Inject

class TradeInHomePageFragmentVM @Inject constructor() : BaseTradeInViewModel(),
    CoroutineScope {
    var data: TradeInPDPData? = null

    fun getPDPData(context: Context, id : String) : TradeInPDPData? {
        data = TradeInPDPHelper.getDataFromPDP(context, id) ?: data
        return data
    }

}