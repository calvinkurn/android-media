package com.tokopedia.search.result.presentation.view.listener;

import com.tokopedia.filter.common.data.Option;

public interface QuickFilterListener {

    void onQuickFilterSelected(Option option);

    boolean isQuickFilterSelected(Option option);
}