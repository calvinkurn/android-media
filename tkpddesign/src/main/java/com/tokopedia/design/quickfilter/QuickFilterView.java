package com.tokopedia.design.quickfilter;

import android.content.Context;
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

public class QuickFilterView extends BaseCustomView {

    private View rootView;
    private RecyclerView recyclerView;
    private QuickFilterAdapter adapterFilter;
    private ActionListener listener;

    public QuickFilterView(@NonNull Context context) {
        super(context);
        init();
    }

    public QuickFilterView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public QuickFilterView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
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
        adapterFilter = new QuickFilterAdapter();
        recyclerView.setAdapter(adapterFilter);
    }

    public void renderFilter(List<QuickFilterItem> quickFilterItems) {
        adapterFilter.setListener(getFilterTokoCashListener());
        adapterFilter.addFilterTokoCashList(quickFilterItems);
    }

    @NonNull
    private QuickFilterAdapter.ActionListener getFilterTokoCashListener() {
        return new QuickFilterAdapter.ActionListener() {
            @Override
            public void clearFilter() {
                listener.clearFilter();
            }

            @Override
            public void selectFilter(String typeFilter) {
                listener.selectFilter();
            }
        };
    }

    interface ActionListener {
        void clearFilter();

        void selectFilter();
    }
}
