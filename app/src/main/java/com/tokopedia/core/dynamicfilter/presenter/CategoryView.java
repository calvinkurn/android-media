package com.tokopedia.core.dynamicfilter.presenter;

import com.tokopedia.core.dynamicfilter.model.DynamicObject;
import com.tokopedia.core.presenter.BaseView;

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
