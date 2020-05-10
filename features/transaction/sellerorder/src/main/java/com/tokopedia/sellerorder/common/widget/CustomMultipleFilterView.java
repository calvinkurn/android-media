package com.tokopedia.sellerorder.common.widget;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import android.util.AttributeSet;

import com.tokopedia.design.R;
import com.tokopedia.design.quickfilter.QuickFilterItem;
import com.tokopedia.design.quickfilter.QuickSingleFilterView;
import com.tokopedia.design.quickfilter.custom.CustomViewRoundedCornerAdapter;

import java.util.List;

public class CustomMultipleFilterView extends QuickSingleFilterView {
    public CustomMultipleFilterView(@NonNull Context context) {
        super(context);
    }

    public CustomMultipleFilterView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomMultipleFilterView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
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
        return quickFilterItem.getType();
    }

    public void renderFilter(List<QuickFilterItem> quickFilterItems, int selectedPos) {
        super.renderFilter(quickFilterItems);
        recyclerView.scrollToPosition(selectedPos);
    }

    @Override
    protected boolean isMultipleSelectionAllowed() {
        return true;
    }
}
