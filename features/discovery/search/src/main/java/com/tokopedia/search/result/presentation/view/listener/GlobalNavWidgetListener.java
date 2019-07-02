package com.tokopedia.search.result.presentation.view.listener;

import com.tokopedia.search.result.presentation.model.GlobalNavViewModel;

public interface GlobalNavWidgetListener {

    void onGlobalNavWidgetClicked(GlobalNavViewModel.Item item, String keyword);

    void onGlobalNavWidgetClickSeeAll(String applink, String url, String keyword, String title);
}