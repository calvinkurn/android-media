package com.tokopedia.design.quickfilter.custom;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.util.AttributeSet;

import com.tokopedia.design.R;
import com.tokopedia.design.quickfilter.QuickSingleFilterView;

/**
 * Created by nabillasabbaha on 1/5/18.
 */

public class CustomViewQuickFilterView extends QuickSingleFilterView {

    public CustomViewQuickFilterView(@NonNull Context context) {
        super(context);
    }

    public CustomViewQuickFilterView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomViewQuickFilterView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    protected void initialAdapter() {
        this.adapterFilter = new CustomViewQuickSingleFilterAdapter(getQuickSingleFilterListener());
    }

    @Override
    protected void init() {
        super.init();
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getContext(), DividerItemDecoration.HORIZONTAL);
        dividerItemDecoration.setDrawable(ContextCompat.getDrawable(getContext(), R.drawable.divider_horizontal_custom_quick_filter));
        recyclerView.addItemDecoration(dividerItemDecoration);
    }

    public void scrollToPosition(int position) {
        try {
            recyclerView.scrollToPosition(position);
        } catch (IllegalStateException e) { /*ignore*/ }
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.widget_custom_quick_filter;
    }
}
