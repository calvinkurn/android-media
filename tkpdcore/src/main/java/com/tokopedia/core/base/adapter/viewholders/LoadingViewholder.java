package com.tokopedia.core.base.adapter.viewholders;

import android.support.annotation.LayoutRes;
import android.view.View;

import com.tokopedia.core.R;
import com.tokopedia.core.base.adapter.model.LoadingModel;


/**
 * @author Kulomady on 1/25/17.
 */

/**
 * Use abstract view holder from tkpd abstraction
 */
@Deprecated
public class LoadingViewholder extends AbstractViewHolder<LoadingModel> {
    @LayoutRes
    public final static int LAYOUT = R.layout.loading_layout;

    public LoadingViewholder(View itemView) {
        super(itemView);
    }

    @Override
    public void bind(LoadingModel element) {

    }
}
