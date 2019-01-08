package com.tokopedia.design.quickfilter.custom;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.util.AttributeSet;

import com.tokopedia.design.R;
import com.tokopedia.design.quickfilter.QuickFilterItem;
import com.tokopedia.design.quickfilter.QuickSingleFilterView;

public class CustomViewRounderCornerFilterView extends QuickSingleFilterView {
    public CustomViewRounderCornerFilterView(@NonNull Context context) {
        super(context);
    }

    public CustomViewRounderCornerFilterView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomViewRounderCornerFilterView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    protected void initialAdapter() {
        this.adapterFilter = new CustomViewRoundedCornerAdapter(getQuickSingleFilterListener());
    }

    @Override
    protected void init() {
        super.init();
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getContext(), DividerItemDecoration.HORIZONTAL);
        dividerItemDecoration.setDrawable(ContextCompat.getDrawable(getContext(), R.drawable.divider_horizontal_custom_quick_filter));
        recyclerView.addItemDecoration(dividerItemDecoration);
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.widget_rounded_corner_filter;
    }

    protected String getDefaultSelectedFilterType(QuickFilterItem quickFilterItem) {
        if (quickFilterItem.isSelected()) {
            return quickFilterItem.getType();
        }
        return "0";
    }

}
