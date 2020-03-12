package com.tokopedia.analyticsdebugger.debugger.ui.adapter;

import android.view.View;

import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory;
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.analyticsdebugger.debugger.ui.model.ApplinkDebuggerViewModel;
import com.tokopedia.analyticsdebugger.debugger.ui.viewholder.ApplinkDebuggerViewHolder;

public class ApplinkDebuggerTypeFactory extends BaseAdapterTypeFactory {

    @Override
    public AbstractViewHolder createViewHolder(View parent, int type) {
        if(type == ApplinkDebuggerViewHolder.LAYOUT) {
            return new ApplinkDebuggerViewHolder(parent);
        }

        return super.createViewHolder(parent, type);
    }

    public int type(ApplinkDebuggerViewModel applinkDebuggerViewModel) {
        return ApplinkDebuggerViewHolder.LAYOUT;
    }
}
