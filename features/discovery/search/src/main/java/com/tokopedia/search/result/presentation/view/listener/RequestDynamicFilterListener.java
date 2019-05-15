package com.tokopedia.search.result.presentation.view.listener;

import com.tokopedia.discovery.common.data.DynamicFilterModel;

public interface RequestDynamicFilterListener {

    void renderDynamicFilter(DynamicFilterModel dynamicFilterModel);

    void renderFailRequestDynamicFilter();
}
