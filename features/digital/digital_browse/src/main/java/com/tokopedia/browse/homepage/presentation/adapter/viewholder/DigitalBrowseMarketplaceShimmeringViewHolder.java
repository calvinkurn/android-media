package com.tokopedia.browse.homepage.presentation.adapter.viewholder;

import android.support.annotation.LayoutRes;
import android.view.View;

import com.tokopedia.abstraction.base.view.adapter.model.LoadingModel;
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.browse.R;

/**
 * @author by furqan on 03/09/18.
 */

public class DigitalBrowseMarketplaceShimmeringViewHolder extends AbstractViewHolder<LoadingModel> {
    @LayoutRes
    public static int LAYOUT = R.layout.item_digital_browser_marketplace_shimmering_loading;

    public DigitalBrowseMarketplaceShimmeringViewHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void bind(LoadingModel element) {

    }
}
