package com.tokopedia.common_tradein.viewmodel;

import android.app.Application;

import com.tokopedia.common_tradein.model.TradeInParams;

public interface ITradeInParamReceiver {
    void checkTradeIn(TradeInParams tradeInParams, boolean hide, Application application);
}
