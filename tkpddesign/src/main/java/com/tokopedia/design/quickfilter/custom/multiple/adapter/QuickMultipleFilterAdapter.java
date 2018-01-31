package com.tokopedia.design.quickfilter.custom.multiple.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.design.R;
import com.tokopedia.design.quickfilter.BaseQuickSingleFilterAdapter;
import com.tokopedia.design.quickfilter.ItemFilterViewHolder;
import com.tokopedia.design.quickfilter.QuickFilterItem;
import com.tokopedia.design.quickfilter.QuickSingleFilterListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yfsx on 30/01/18.
 */

public class QuickMultipleFilterAdapter extends BaseQuickSingleFilterAdapter<ItemFilterViewHolder> {
    private QuickSingleFilterListener listener;

    public QuickMultipleFilterAdapter(QuickSingleFilterListener listener) {
        this.listener = listener;
    }

    @Override
    public ItemFilterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_quick_multiple_filter_view, parent, false);
        return new ItemFilterViewHolder(view, parent.getContext(), listener);
    }

    @Override
    public void onBindViewHolder(ItemFilterViewHolder holder, int position) {
        ((ItemFilterViewHolder) holder).renderItemViewHolder(filterList.get(position));
    }

    public void itemClicked(int id) {
        for (QuickFilterItem item : filterList) {
            if(item.getId() == id) {
                item.setSelected(!item.isSelected());
            }
        }
        notifyDataSetChanged();
    }

    public List<QuickFilterItem> getSelectedItemList() {
        List<QuickFilterItem> selectedList = new ArrayList<>();
        for (QuickFilterItem item : filterList) {
            if (item.isSelected()) {
                selectedList.add(item);
            }
        }
        return selectedList;
    }

    public List<Integer> getSelectedIdList() {
        List<Integer> selectedIdList = new ArrayList<>();
        for (QuickFilterItem item : getSelectedItemList()) {
            selectedIdList.add(item.getId());
        }
        return selectedIdList;
    }
}
