package com.tokopedia.search.result.presentation.view.listener;

import com.tokopedia.search.result.presentation.model.GlobalNavViewModel;
import com.tokopedia.search.result.presentation.model.SingleGlobalNavViewModel;

public interface GlobalNavListener {

    void onGlobalNavWidgetClicked(GlobalNavViewModel.Item item, String keyword);

    void onSingleGlobalNavClicked(SingleGlobalNavViewModel.Item item, String keyword);

    void onGlobalNavWidgetClickSeeAll(GlobalNavViewModel globalNavViewModel);
}