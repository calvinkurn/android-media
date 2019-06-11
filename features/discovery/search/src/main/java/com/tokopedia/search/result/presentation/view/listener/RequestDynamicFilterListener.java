package com.tokopedia.search.result.presentation.view.listener;

import android.content.Context;

import com.tokopedia.discovery.common.data.DynamicFilterModel;

public interface RequestDynamicFilterListener {

    boolean shouldSaveToLocalDynamicFilterDb();

    String getScreenNameId();

    void renderDynamicFilter(DynamicFilterModel dynamicFilterModel);

    void renderFailRequestDynamicFilter();

    Context getContext();
}
