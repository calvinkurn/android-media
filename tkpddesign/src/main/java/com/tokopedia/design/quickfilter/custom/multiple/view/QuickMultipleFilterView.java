package com.tokopedia.design.quickfilter.custom.multiple.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

import com.tokopedia.design.R;
import com.tokopedia.design.base.BaseCustomView;
import com.tokopedia.design.quickfilter.QuickFilterItem;
import com.tokopedia.design.quickfilter.QuickSingleFilterListener;
import com.tokopedia.design.quickfilter.custom.multiple.adapter.QuickMultipleFilterAdapter;

import java.util.List;

/**
 * Created by yfsx on 30/01/18.
 */

public class QuickMultipleFilterView extends BaseCustomView {

    private View rootView;
    private RecyclerView recyclerView;
    private QuickMultipleFilterAdapter adapterFilter;
    private ActionListener listener;

    public QuickMultipleFilterView(@NonNull Context context) {
        super(context);
        init();
    }

    public QuickMultipleFilterView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public QuickMultipleFilterView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }


    public void setListener(ActionListener listener) {
        this.listener = listener;
    }

    public void renderFilter(List<QuickFilterItem> quickFilterItems) {
        adapterFilter.addFilterTokoCashList(quickFilterItems);
    }


    private void init() {
        rootView = inflate(getContext(), R.layout.widget_quick_filter, this);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.rv_filter);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(),
                LinearLayoutManager.HORIZONTAL, false));

        adapterFilter = new QuickMultipleFilterAdapter(getListener());
        recyclerView.setAdapter(adapterFilter);
    }

    private QuickSingleFilterListener getListener() {
        return new QuickSingleFilterListener() {
            @Override
            public void selectFilter(QuickFilterItem filterItem) {
                adapterFilter.itemClicked(filterItem.getId());
                listener.filterClicked(adapterFilter.getSelectedIdList());
            }
        };
    }

    public interface ActionListener {
        void filterClicked(List<Integer> selectedIdList);
    }
}
