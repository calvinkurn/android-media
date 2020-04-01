package com.tokopedia.autocomplete;

import android.view.View;

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.autocomplete.initialstate.InitialStateViewModel;

public interface HostAutoCompleteTypeFactory {

    int type(InitialStateViewModel viewModel);

    int type(TabSuggestionViewModel viewModel);

    AbstractViewHolder createViewHolder(View view, int viewType);
}
