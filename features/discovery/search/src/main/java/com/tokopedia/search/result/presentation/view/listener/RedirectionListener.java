package com.tokopedia.search.result.presentation.view.listener;

public interface RedirectionListener {
    void performNewProductSearch(String query, boolean forceSearch);
    void showSearchInputView();
}