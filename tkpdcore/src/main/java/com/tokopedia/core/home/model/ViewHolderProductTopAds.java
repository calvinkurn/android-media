package com.tokopedia.core.home.model;

import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * @author Kulomady on 11/22/16.
 */
public class ViewHolderProductTopAds extends RecyclerView.ViewHolder {

    public RecyclerView listTopAdProduct;

    public ViewHolderProductTopAds(View itemLayoutView) {
        super(itemLayoutView);
        listTopAdProduct = (RecyclerView) itemLayoutView
                .findViewById(com.tokopedia.core2.R.id.top_ads_recycler_view);
    }
}
