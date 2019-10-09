package com.tokopedia.common_tradein.viewmodel;

import com.tokopedia.common_tradein.model.TradeInParams;

public interface ITradeInParamReceiver {
    void checkTradeIn(TradeInParams tradeInParams, boolean hide);
}
