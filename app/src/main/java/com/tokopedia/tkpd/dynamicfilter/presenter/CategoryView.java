package com.tokopedia.tkpd.dynamicfilter.presenter;

import com.tokopedia.tkpd.dynamicfilter.model.DynamicObject;
import com.tokopedia.tkpd.presenter.BaseView;

import java.util.ArrayList;

/**
 * Created by noiz354 on 7/12/16.
 */
public interface CategoryView extends BaseView {
    int FRAGMENT_ID = 183_671;
    String FRAGMENT_TAG = "CategoryView";

    void setupAdapter(ArrayList<DynamicObject> dynamicParentObject);
    void setupRecyclerView();
    void showLoading(boolean bool);
}
