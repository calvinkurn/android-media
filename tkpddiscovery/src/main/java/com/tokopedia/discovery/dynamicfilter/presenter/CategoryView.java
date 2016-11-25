package com.tokopedia.discovery.dynamicfilter.presenter;

import com.tokopedia.core.presenter.BaseView;
import com.tokopedia.discovery.dynamicfilter.model.DynamicObject;

import java.util.List;

/**
 * Created by noiz354 on 7/12/16.
 */
public interface CategoryView extends BaseView {

    int FRAGMENT_ID = 183_671;
    String FRAGMENT_TAG = "CategoryView";

    void setupAdapter(List<DynamicObject> dynamicParentObject);

    void setupRecyclerView();

    void showLoading(boolean bool);
}
