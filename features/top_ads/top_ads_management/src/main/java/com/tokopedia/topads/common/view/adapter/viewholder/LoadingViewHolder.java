package com.tokopedia.topads.common.view.adapter.viewholder;

import androidx.annotation.LayoutRes;
import android.view.View;

import com.tokopedia.abstraction.base.view.adapter.viewholders.LoadingViewholder;
import com.tokopedia.topads.R;

/**
 * Created by hadi.putra on 07/05/18.
 */

public class LoadingViewHolder extends LoadingViewholder{
    @LayoutRes
    public final static int LAYOUT = R.layout.top_ads_loading_layout;

    public LoadingViewHolder(View itemView) {
        super(itemView);
    }
}
