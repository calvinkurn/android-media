package com.tokopedia.core.base.adapter.viewholders;

import android.support.annotation.LayoutRes;
import android.view.View;

import com.tokopedia.core.R;
import com.tokopedia.core.base.adapter.model.EmptyModel;

/**
 * @author kulomady on 1/24/17.
 */

public class EmptyViewHolder extends AbstractViewHolder<EmptyModel> {

    @LayoutRes
    public static final int LAYOUT = R.layout.view_no_result;

    public EmptyViewHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void bind(EmptyModel element) {

    }

}
