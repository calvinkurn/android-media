package com.tokopedia.analyticsdebugger.debugger.ui.adapter;

import android.view.View;

import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory;
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.analyticsdebugger.debugger.ui.model.FpmDebuggerViewModel;
import com.tokopedia.analyticsdebugger.debugger.ui.viewholder.FpmDebuggerViewHolder;

/**
 * @author okasurya on 5/16/18.
 */
public class FpmDebuggerTypeFactory extends BaseAdapterTypeFactory {

    @Override
    public AbstractViewHolder createViewHolder(View parent, int type) {
        if(type == FpmDebuggerViewHolder.LAYOUT) {
            return new FpmDebuggerViewHolder(parent);
        }

        return super.createViewHolder(parent, type);
    }

    public int type(FpmDebuggerViewModel fpmDebuggerViewModel) {
        return FpmDebuggerViewHolder.LAYOUT;
    }
}
