package com.tokopedia.analytics.debugger.ui.viewholder;

import androidx.annotation.LayoutRes;
import android.view.View;
import android.widget.TextView;

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.analytics.R;
import com.tokopedia.analytics.debugger.ui.model.AnalyticsDebuggerViewModel;

/**
 * @author okasurya on 5/16/18.
 */
public class AnalyticsDebuggerViewHolder extends AbstractViewHolder<AnalyticsDebuggerViewModel> {
    @LayoutRes
    public static final int LAYOUT = R.layout.item_analytics_debugger;

    private TextView eventName;
    private TextView eventCategory;
    private TextView data;
    private TextView timestamp;


    public AnalyticsDebuggerViewHolder(View itemView) {
        super(itemView);

        eventName = itemView.findViewById(R.id.text_event_name);
        eventCategory = itemView.findViewById(R.id.text_event_category);
        data = itemView.findViewById(R.id.text_data_excerpt);
        timestamp = itemView.findViewById(R.id.text_timestamp);
    }

    @Override
    public void bind(AnalyticsDebuggerViewModel element) {
        eventName.setText(element.getName());
        eventCategory.setText(element.getCategory());
        data.setText(element.getDataExcerpt());
        timestamp.setText(element.getTimestamp());
    }
}
