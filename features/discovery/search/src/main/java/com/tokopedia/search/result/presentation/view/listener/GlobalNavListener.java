package com.tokopedia.search.result.presentation.view.listener;

import com.tokopedia.search.result.presentation.model.GlobalNavViewModel;

public interface GlobalNavListener {

    void onGlobalNavWidgetClicked(GlobalNavViewModel.Item item, String keyword);

    void onGlobalNavWidgetClickSeeAll(GlobalNavViewModel globalNavViewModel);
}