package com.tokopedia.search.result.presentation.view.listener;

import com.tokopedia.search.result.presentation.model.TickerDataView;

public interface TickerListener {

    void onTickerClicked(TickerDataView tickerDataView);

    void onTickerDismissed();

    boolean isTickerHasDismissed();
}