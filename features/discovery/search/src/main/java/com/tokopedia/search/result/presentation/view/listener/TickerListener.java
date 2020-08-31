package com.tokopedia.search.result.presentation.view.listener;

import com.tokopedia.search.result.presentation.model.TickerViewModel;

public interface TickerListener {

    void onTickerClicked(TickerViewModel tickerViewModel);

    void onTickerDismissed();

    boolean isTickerHasDismissed();
}