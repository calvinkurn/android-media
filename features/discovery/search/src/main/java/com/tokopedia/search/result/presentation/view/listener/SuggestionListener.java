package com.tokopedia.search.result.presentation.view.listener;

import com.tokopedia.discovery.common.data.Option;
import com.tokopedia.search.result.presentation.model.GlobalNavViewModel;
import com.tokopedia.search.result.presentation.model.ProductItemViewModel;

public interface SuggestionListener {

    void onSuggestionClicked(String suggestedQuery);
}