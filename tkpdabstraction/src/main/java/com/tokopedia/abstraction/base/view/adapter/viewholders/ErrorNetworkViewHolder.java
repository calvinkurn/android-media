package com.tokopedia.abstraction.base.view.adapter.viewholders;


import android.support.annotation.LayoutRes;
import android.view.View;

import com.tokopedia.abstraction.R;
import com.tokopedia.abstraction.base.view.adapter.model.ErrorNetworkModel;


/**
 * @author by erry on 02/02/17.
 */

public class ErrorNetworkViewHolder extends AbstractViewHolder<ErrorNetworkModel> {

    @LayoutRes
    public final static int LAYOUT = R.layout.design_error_network;

    public ErrorNetworkViewHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void bind(ErrorNetworkModel element) {

    }
}
