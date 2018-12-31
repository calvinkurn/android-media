package com.tokopedia.travelcalendar.view.widget;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.view.View;

import com.tokopedia.design.base.BaseCustomView;
import com.tokopedia.travelcalendar.R;

/**
 * Created by nabillasabbaha on 28/12/18.
 */
public class CustomQuickFilterMonthView extends BaseCustomView {

    private AppCompatTextView monthName;

    public CustomQuickFilterMonthView(@NonNull Context context) {
        super(context);
        init();
    }

    public CustomQuickFilterMonthView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CustomQuickFilterMonthView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        View view = inflate(getContext(), R.layout.item_month_quick_filter, this);
        monthName = view.findViewById(R.id.month);
    }

    public void setTextMonth(String month) {
        monthName.setText(month);
    }
}
