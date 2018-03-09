package com.tokopedia.abstraction.base.view.adapter.viewholders;

import android.view.View;

import com.tokopedia.abstraction.base.view.adapter.model.LoadingModelShimmeringGrid;

/**
 * Created by zulfikarrahman on 3/9/18.
 */

public class LoadingShimmeringGridViewHolder extends AbstractViewHolder<LoadingModelShimmeringGrid> {
    public static final int LAYOUT = com.tokopedia.design.R.layout.item_shimmering_grid;

    public LoadingShimmeringGridViewHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void bind(LoadingModelShimmeringGrid element) {

    }
}
