package com.tokopedia.design.quickfilter;

import android.content.Context;
import android.os.Handler;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

import com.tokopedia.design.R;
import com.tokopedia.design.base.BaseCustomView;

import java.util.List;

/**
 * Created by nabillasabbaha on 1/5/18.
 */

public class QuickSingleFilterView extends BaseCustomView {

    private QuickFilterItem defaultItem = null;
    protected RecyclerView recyclerView;
    protected BaseQuickSingleFilterAdapter<ItemFilterViewHolder> adapterFilter;
    private ActionListener listener;

    public QuickSingleFilterView(@NonNull Context context) {
        super(context);
        init();
    }

    public QuickSingleFilterView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public QuickSingleFilterView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void setListener(ActionListener listener) {
        this.listener = listener;
    }

    protected void init() {
        View rootView = inflate(getContext(), getLayoutRes(), this);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.rv_filter);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(),
                LinearLayoutManager.HORIZONTAL, false));
        initialAdapter();
        recyclerView.setAdapter(adapterFilter);
    }

    @LayoutRes
    protected int getLayoutRes(){
        return R.layout.widget_quick_filter;
    }

    protected void initialAdapter() {
        this.adapterFilter = new QuickSingleFilterAdapter(getQuickSingleFilterListener());
    }

    public void renderFilter(List<QuickFilterItem> quickFilterItems) {
        adapterFilter.addQuickFilterList(quickFilterItems);
    }

    protected QuickSingleFilterListener getQuickSingleFilterListener() {
        return new QuickSingleFilterListener() {

            @Override
            public void selectFilter(QuickFilterItem quickFilterItem) {
                int totalFalse = 0;
                List<QuickFilterItem> items = adapterFilter.getDataList();
                for (int i = 0; i < items.size(); i++) {
                    if (quickFilterItem.getType().equals(items.get(i).getType())) {
                        if (items.get(i).isSelected()) {
                            items.get(i).setSelected(false);
                            totalFalse++;
                        } else {
                            items.get(i).setSelected(true);
                        }
                    } else {
                        totalFalse++;
                        items.get(i).setSelected(false);
                    }
                }

                if (totalFalse == items.size() && defaultItem != null) {
                    int indexOf = items.indexOf(defaultItem);
                    if (indexOf != -1) {
                        items.get(indexOf).setSelected(true);
                        setSelectedFilter(items.get(indexOf).getType());
                    }
                } else {
                        setSelectedFilter(getDefaultSelectedFilterType(quickFilterItem));
                }
                adapterFilter.notifyDataSetChanged();
            }
        };
    }

    protected String getDefaultSelectedFilterType(QuickFilterItem quickFilterItem) {
            return quickFilterItem.getType();
    }

    private void setSelectedFilter(String type) {
        if (listener != null) {
            listener.selectFilter(type);
        }
    }

    public void setDefaultItem(QuickFilterItem defaultItem) {
        this.defaultItem = defaultItem;
    }

    public void actionSelect(final int position) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (recyclerView != null) {
                    RecyclerView.ViewHolder holder = recyclerView.findViewHolderForAdapterPosition(position);
                    if (holder != null) {
                        View holderItem = holder.itemView;
                        if (holderItem != null) holderItem.performClick();
                    }
                }
            }
        }, 100);

    }

    public String getSelectedFilter(){
        if(defaultItem != null && defaultItem.isSelected()){
            return defaultItem.getType();
        }else {
            String itemSelected = "";
            for (int i= 0; i<adapterFilter.getDataList().size(); i++) {
                QuickFilterItem quickFilterItem = adapterFilter.getDataList().get(i);
                if (quickFilterItem.isSelected()) {
                    itemSelected = quickFilterItem.getType();
                    break;
                }
            }
            return itemSelected;
        }
    }

    public boolean isAnyItemSelected(){
        if(defaultItem != null){
            return true;
        }else {
            boolean isItemSelected = false;
            for (int i= 0; i<adapterFilter.getDataList().size(); i++) {
                QuickFilterItem quickFilterItem = adapterFilter.getDataList().get(i);
                if (quickFilterItem.isSelected()) {
                    isItemSelected = true;
                    break;
                }
            }
            return isItemSelected;
        }
    }

    public interface ActionListener {

        void selectFilter(String typeFilter);
    }
}
