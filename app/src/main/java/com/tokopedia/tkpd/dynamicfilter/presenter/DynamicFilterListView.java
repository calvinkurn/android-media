package com.tokopedia.tkpd.dynamicfilter.presenter;

import com.tokopedia.tkpd.dynamicfilter.model.DynamicFilterModel;
import com.tokopedia.tkpd.presenter.BaseView;

import java.util.List;

/**
 * Created by noiz354 on 7/11/16.
 */
public interface DynamicFilterListView extends BaseView{
    String FRAGMENT_TAG = "DynamicFilterListView";
    int FRAGMENT_ID = 121_990_213;
    void setupRecyclerView();
    @Deprecated
    void setupAdapter(List<String> title);
    void setupAdapter2(List<DynamicFilterModel.Filter> dataList);
}
