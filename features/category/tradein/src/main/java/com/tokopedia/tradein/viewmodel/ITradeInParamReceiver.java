package com.tokopedia.tradein.viewmodel;

import com.tokopedia.tradein.model.TradeInParams;

public interface ITradeInParamReceiver {
    void checkTradeIn(TradeInParams tradeInParams, boolean hide);
}
