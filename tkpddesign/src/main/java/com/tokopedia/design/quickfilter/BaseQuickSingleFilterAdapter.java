package com.tokopedia.design.quickfilter;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nabillasabbaha on 11/22/17.
 */

public abstract class BaseQuickSingleFilterAdapter<T extends BaseItemFilterViewHolder> extends RecyclerView.Adapter<T> {

    protected List<QuickFilterItem> filterList;

    public BaseQuickSingleFilterAdapter() {
        this.filterList = new ArrayList<>();
    }

    public abstract T onCreateViewHolder(ViewGroup parent, int viewType);

    public abstract void onBindViewHolder(T holder, int position);

    @Override
    public int getItemCount() {
        return filterList.size();
    }

    public void addQuickFilterList(List<QuickFilterItem> filterList) {
        this.filterList.clear();
        this.filterList.addAll(filterList);
        notifyDataSetChanged();
    }

    public List<QuickFilterItem> getDataList() {
        return this.filterList;
    }
}