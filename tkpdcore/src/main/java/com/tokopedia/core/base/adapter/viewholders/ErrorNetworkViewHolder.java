package com.tokopedia.core.base.adapter.viewholders;


import android.support.annotation.LayoutRes;
import android.view.View;

import com.tokopedia.core.R;
import com.tokopedia.core.base.adapter.model.ErrorNetworkModel;

/**
 * @author by erry on 02/02/17.
 */

/**
 * Use abstract view holder from tkpd abstraction
 */
@Deprecated
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
