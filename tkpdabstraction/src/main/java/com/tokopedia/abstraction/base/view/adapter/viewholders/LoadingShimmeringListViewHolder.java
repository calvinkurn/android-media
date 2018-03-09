package com.tokopedia.abstraction.base.view.adapter.viewholders;

import android.view.View;

import com.tokopedia.abstraction.base.view.adapter.model.LoadingModelShimmeringList;

/**
 * Created by zulfikarrahman on 3/9/18.
 */

public class LoadingShimmeringListViewHolder extends AbstractViewHolder<LoadingModelShimmeringList>{
    public static final int LAYOUT = com.tokopedia.design.R.layout.item_shimmering_list;

    public LoadingShimmeringListViewHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void bind(LoadingModelShimmeringList element) {

    }
}
