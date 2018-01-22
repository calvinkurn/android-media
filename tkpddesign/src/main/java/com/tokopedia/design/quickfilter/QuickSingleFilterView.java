package com.tokopedia.design.quickfilter;

import android.content.Context;
import android.os.Handler;
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
    private View rootView;
    private RecyclerView recyclerView;
    private BaseQuickSingleFilterAdapter adapterFilter;
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

    private void init() {
        rootView = inflate(getContext(), R.layout.widget_quick_filter, this);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.rv_filter);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(),
                LinearLayoutManager.HORIZONTAL, false));

        adapterFilter = new QuickSingleFilterAdapter(getFilterTokoCashListener());
        recyclerView.setAdapter(adapterFilter);
    }

    public void setAdapterFilter(BaseQuickSingleFilterAdapter adapterFilter) {
        this.adapterFilter = adapterFilter;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(),
                LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setAdapter(adapterFilter);
    }

    public void renderFilter(List<QuickFilterItem> quickFilterItems) {
        adapterFilter.addFilterTokoCashList(quickFilterItems);
    }

    private QuickSingleFilterListener getFilterTokoCashListener() {
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
                        listener.selectFilter(items.get(indexOf).getType());
                    }
                } else {
                    listener.selectFilter(quickFilterItem.getType());
                }
                adapterFilter.notifyDataSetChanged();
            }
        };
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

    public interface ActionListener {

        void selectFilter(String typeFilter);
    }
}
