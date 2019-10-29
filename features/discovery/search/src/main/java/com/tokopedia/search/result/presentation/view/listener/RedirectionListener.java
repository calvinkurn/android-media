package com.tokopedia.search.result.presentation.view.listener;

public interface RedirectionListener {
    void showSearchInputView();
    void setAutocompleteApplink(String autocompleteApplink);
    void startActivityWithApplink(String applink, String... parameter);
    void startActivityWithUrl(String url, String... parameter);
}