package com.tokopedia.design.quickfilter.custom.multiple.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.tokopedia.design.quickfilter.custom.multiple.adapter.viewholder.BaseMultipleQuickFilterViewHolder;
import com.tokopedia.design.quickfilter.custom.multiple.item.QuickMultipleFilterItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yfsx on 30/01/18.
 */

public abstract class BaseQuickMultipleFilterAdapter<T extends BaseMultipleQuickFilterViewHolder> extends RecyclerView.Adapter<T> {

    protected List<QuickMultipleFilterItem> filterList;

    protected List<Integer> selectedIdList;

    public BaseQuickMultipleFilterAdapter() {
        this.filterList = new ArrayList<>();
        this.selectedIdList = new ArrayList<>();
    }

    public abstract T onCreateViewHolder(ViewGroup parent, int viewType);

    public abstract void onBindViewHolder(T holder, int position);

    @Override
    public int getItemCount() {
        return filterList.size();
    }

    public void addQuickMultipleFilterItems(List<QuickMultipleFilterItem> filterList) {
        this.filterList.clear();
        this.filterList.addAll(filterList);
        notifyDataSetChanged();
    }

    public void itemClicked(int i) {
        if (selectedIdList.contains(i)) {
            List<Integer> newIdList = new ArrayList<>();
            for (Integer oldId : selectedIdList) {
                if (oldId != i) newIdList.add(oldId);
            }
            selectedIdList = newIdList;
        } else {
            selectedIdList.add(i);
        }
        for (QuickMultipleFilterItem item : filterList) {
            item.setSelected(selectedIdList.contains(i));
        }
        notifyDataSetChanged();
    }

    public List<Integer> getSelectedIdList() {
        return this.selectedIdList;
    }

    public List<QuickMultipleFilterItem> getDataList() {
        return this.filterList;
    }
}