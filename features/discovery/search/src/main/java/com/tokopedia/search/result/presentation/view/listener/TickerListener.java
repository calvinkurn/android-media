package com.tokopedia.search.result.presentation.view.listener;

public interface TickerListener {

    void onTickerClicked(String queryParams);

    void onTickerDismissed();

    boolean isTickerHasDismissed();
}