package com.tokopedia.search.result.presentation.view.listener;

import android.support.annotation.Nullable;

import com.tokopedia.discovery.common.data.Option;

import java.util.List;

public interface EmptyStateListener {
    void onEmptyButtonClicked();
    void onBannerAdsClicked(String appLink);
    void onSelectedFilterRemoved(String uniqueId);
    boolean isUserHasLogin();
    String getRegistrationId();
    String getUserId();
    @Nullable
    List<Option> getSelectedFilterAsOptionList();
}
