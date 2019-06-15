package com.tokopedia.search.result.presentation.view.listener;

public interface CatalogListener {

    void setOnCatalogClicked(String catalogID, String catalogName);

    String getRegistrationId();

    String getUserId();
}
