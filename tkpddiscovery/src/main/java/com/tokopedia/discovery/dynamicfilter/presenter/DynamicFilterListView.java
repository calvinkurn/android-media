package com.tokopedia.discovery.dynamicfilter.presenter;

import com.tokopedia.core.discovery.model.Filter;
import com.tokopedia.core.presenter.BaseView;

import java.util.List;

/**
 * Created by noiz354 on 7/11/16.
 */
public interface DynamicFilterListView extends BaseView {
    String FRAGMENT_TAG = "DynamicFilterListView";
    int FRAGMENT_ID = 121_990_213;

    void setupRecyclerView();

    void setupAdapter(List<Filter> dataList);
}
