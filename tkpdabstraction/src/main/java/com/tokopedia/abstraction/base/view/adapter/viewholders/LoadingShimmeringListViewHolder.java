package com.tokopedia.abstraction.base.view.adapter.viewholders;

import android.view.View;

import com.tokopedia.abstraction.R;
import com.tokopedia.abstraction.base.view.adapter.model.LoadingModel;

/**
 * Created by zulfikarrahman on 3/9/18.
 */

public class LoadingShimmeringListViewHolder extends AbstractViewHolder<LoadingModel>{
    public static final int LAYOUT = R.layout.item_shimmering_list;

    public LoadingShimmeringListViewHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void bind(LoadingModel element) {

    }
}
