package com.tokopedia.search.result.presentation.view.listener;

import com.tokopedia.search.result.presentation.model.GlobalNavDataView;

public interface GlobalNavListener {

    void onGlobalNavWidgetClicked(GlobalNavDataView.Item item, String keyword);

    void onGlobalNavWidgetClickSeeAll(GlobalNavDataView globalNavDataView);
}