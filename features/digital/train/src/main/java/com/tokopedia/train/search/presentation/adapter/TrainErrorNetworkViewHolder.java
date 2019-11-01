package com.tokopedia.train.search.presentation.adapter;

import androidx.annotation.LayoutRes;
import android.view.View;

import com.tokopedia.abstraction.base.view.adapter.viewholders.ErrorNetworkViewHolder;
import com.tokopedia.tkpdtrain.R;

/**
 * Created by nabillasabbaha on 3/16/18.
 */

public class TrainErrorNetworkViewHolder extends ErrorNetworkViewHolder {

    @LayoutRes
    public final static int LAYOUT = R.layout.view_train_error_network;

    public TrainErrorNetworkViewHolder(View itemView) {
        super(itemView);
    }
}
