package com.tokopedia.search.result.presentation.view.listener;

import com.tokopedia.search.result.presentation.model.SuggestionViewModel;

public interface SuggestionListener {

    void onSuggestionClicked(SuggestionViewModel suggestionViewModel);
}