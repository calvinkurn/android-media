package com.tokopedia.search.result.presentation.view.listener;

public interface RedirectionListener {
    void performNewProductSearch(String queryParams);
    void showSearchInputView();
    void onProductDataReady();
}