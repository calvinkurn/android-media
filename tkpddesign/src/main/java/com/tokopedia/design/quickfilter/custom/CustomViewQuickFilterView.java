package com.tokopedia.design.quickfilter.custom;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

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
}
