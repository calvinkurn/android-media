package com.tokopedia.analytics.debugger.ui.viewholder;

import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.LayoutRes;

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.analytics.R;
import com.tokopedia.analytics.debugger.ui.model.AnalyticsDebuggerViewModel;
import com.tokopedia.analytics.debugger.ui.model.FpmDebuggerViewModel;

public class FpmDebuggerViewHolder extends AbstractViewHolder<FpmDebuggerViewModel> {
    @LayoutRes
    public static final int LAYOUT = R.layout.item_fpm_debugger;

    private TextView name;
    private TextView duration;
    private TextView metrics;
    private TextView txtAtrributes;
    private TextView timestamp;


    public FpmDebuggerViewHolder(View itemView) {
        super(itemView);

        name = itemView.findViewById(R.id.fpm_text_name);
        duration = itemView.findViewById(R.id.fpm_text_duration);
        metrics = itemView.findViewById(R.id.fpm_text_metrics);
        txtAtrributes = itemView.findViewById(R.id.fpm_text_attributes);
        timestamp = itemView.findViewById(R.id.text_timestamp);
    }

    @Override
    public void bind(FpmDebuggerViewModel element) {
        String itemName = element.getName();
        if (TextUtils.isEmpty(itemName)) {
            name.setVisibility(View.GONE);
        } else {
            name.setText(itemName);
            name.setVisibility(View.VISIBLE);
        }

        duration.setText(String.valueOf(element.getDuration()));
        metrics.setText(element.getMetrics());
        txtAtrributes.setText(element.getAttributes());
        timestamp.setText(element.getTimestamp());
    }
}
