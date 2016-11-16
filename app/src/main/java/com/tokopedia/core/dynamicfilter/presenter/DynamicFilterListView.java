package com.tokopedia.core.dynamicfilter.presenter;

import com.tokopedia.core.dynamicfilter.model.DynamicFilterModel;
import com.tokopedia.core.presenter.BaseView;

import java.util.List;

/**
 * Created by noiz354 on 7/11/16.
 */
public interface DynamicFilterListView extends BaseView {
    String FRAGMENT_TAG = "DynamicFilterListView";
    int FRAGMENT_ID = 121_990_213;

    void setupRecyclerView();

    void setupAdapter(List<DynamicFilterModel.Filter> dataList);
}
