package com.tokopedia.contactus.inboxticket2.view.customview;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import com.tokopedia.contactus.inboxticket2.view.customview.adapter.CustomQuickOptionViewAdapter;
import com.tokopedia.design.quickfilter.QuickSingleFilterView;

public class CustomQuickOptionView extends QuickSingleFilterView {
    public CustomQuickOptionView(@NonNull Context context) {
        super(context);
    }

    public CustomQuickOptionView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomQuickOptionView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void initialAdapter() {
        this.adapterFilter = new CustomQuickOptionViewAdapter(getQuickSingleFilterListener());
    }
}
