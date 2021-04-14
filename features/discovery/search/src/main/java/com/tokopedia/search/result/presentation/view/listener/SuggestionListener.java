package com.tokopedia.search.result.presentation.view.listener;

import com.tokopedia.search.result.presentation.model.SuggestionDataView;

public interface SuggestionListener {

    void onSuggestionClicked(SuggestionDataView suggestionDataView);
}