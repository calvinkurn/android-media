package com.tokopedia.search.result.presentation.view.listener;

import androidx.annotation.Nullable;

import com.tokopedia.filter.common.data.Option;

import java.util.List;

public interface EmptyStateListener {
    void onEmptyButtonClicked();
    void onSelectedFilterRemoved(String uniqueId);
    String getRegistrationId();
    String getUserId();
    @Nullable
    List<Option> getSelectedFilterAsOptionList();
    void onEmptySearchToGlobalSearchClicked(String applink);
}
