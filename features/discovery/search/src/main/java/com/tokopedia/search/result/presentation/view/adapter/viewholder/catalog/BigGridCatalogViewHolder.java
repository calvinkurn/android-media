package com.tokopedia.search.result.presentation.view.adapter.viewholder.catalog;

import android.support.annotation.LayoutRes;
import android.view.View;

import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.search.R;
import com.tokopedia.search.result.presentation.model.CatalogViewModel;
import com.tokopedia.search.result.presentation.view.listener.CatalogListener;

public class BigGridCatalogViewHolder extends GridCatalogViewHolder {

    @LayoutRes
    public static final int LAYOUT = R.layout.search_gridview_big_browse_catalog;

    public BigGridCatalogViewHolder(View itemView, CatalogListener mCatalogListener) {
        super(itemView, mCatalogListener);
    }

    @Override
    public void setCatalogImage(CatalogViewModel catalog) {
        ImageHandler.loadImageThumbs(context, catalogImage, catalog.getImage300());
    }
}
