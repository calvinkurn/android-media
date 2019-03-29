package com.tokopedia.analytics.debugger.ui.adapter;

import android.view.View;

import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory;
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.analytics.debugger.ui.model.AnalyticsDebuggerViewModel;
import com.tokopedia.analytics.debugger.ui.viewholder.AnalyticsDebuggerViewHolder;

/**
 * @author okasurya on 5/16/18.
 */
public class AnalyticsDebuggerTypeFactory extends BaseAdapterTypeFactory {

    @Override
    public AbstractViewHolder createViewHolder(View parent, int type) {
        if(type == AnalyticsDebuggerViewHolder.LAYOUT) {
            return new AnalyticsDebuggerViewHolder(parent);
        }

        return super.createViewHolder(parent, type);
    }

    public int type(AnalyticsDebuggerViewModel analyticsDebuggerViewModel) {
        return AnalyticsDebuggerViewHolder.LAYOUT;
    }
}
