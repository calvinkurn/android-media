package com.tokopedia.tradein.viewmodel

import com.tokopedia.common_tradein.model.TradeInParams
import kotlinx.coroutines.CoroutineScope
import javax.inject.Inject

class TradeInHomeViewModel @Inject constructor() : BaseTradeInViewModel(), CoroutineScope {
    var tradeInParams : TradeInParams? = null
}
