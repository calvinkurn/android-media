package com.tokopedia.search.result.presentation.view.listener;

public interface RedirectionListener {
    void performNewProductSearch(String queryParams);
    void showSearchInputView();
    void onProductLoadingFinished();
    void startActivityWithApplink(String applink, String... parameter);
    void startActivityWithUrl(String url, String... parameter);
}