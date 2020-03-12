package com.tokopedia.analyticsdebugger.debugger.ui.viewholder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.LayoutRes;

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.analyticsdebugger.R;
import com.tokopedia.analyticsdebugger.debugger.ui.model.ApplinkDebuggerViewModel;

public class ApplinkDebuggerViewHolder extends AbstractViewHolder<ApplinkDebuggerViewModel> {
    @LayoutRes
    public static final int LAYOUT = R.layout.item_applink_debugger;

    private TextView applinkName;
    private TextView traces;
    private TextView timestamp;


    public ApplinkDebuggerViewHolder(View itemView) {
        super(itemView);

        applinkName = itemView.findViewById(R.id.applink_text_name);
        traces = itemView.findViewById(R.id.applink_text_traces);
        timestamp = itemView.findViewById(R.id.applink_text_timestamp);
    }

    @Override
    public void bind(ApplinkDebuggerViewModel element) {
        applinkName.setText(element.getApplink());
        traces.setText(element.getPreviewTrace());
        timestamp.setText(element.getTimestamp());
    }
}
