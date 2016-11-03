package com.tokopedia.tkpd.dynamicfilter.presenter;

import com.tokopedia.tkpd.dynamicfilter.adapter.DynamicFilterOtherAdapter;
import com.tokopedia.tkpd.presenter.BaseView;
import com.tokopedia.tkpd.var.RecyclerViewItem;

import java.util.List;

/**
 * Created by noiz354 on 7/12/16.
 */
public interface DynamicFilterOtherView extends BaseView{
    int FRAGMENT_ID = 192_764;
    String FRAGMENT_TAG = "DynamicFilterOtherView";

    void toggleSearch(boolean status, String hint);

    void setupAdapter(List<RecyclerViewItem> items);

    void addListItem(List<RecyclerViewItem> items);

    void setListItem(List<RecyclerViewItem> items);

    void setupRecylerView();

    void setIsLoading(boolean loading);

    boolean isLoading();

    DynamicFilterOtherAdapter getAdapter();
}
