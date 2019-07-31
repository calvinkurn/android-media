package com.tokopedia.search.result.presentation.view.adapter.viewholder.catalog;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.abstraction.common.utils.view.MethodChecker;
import com.tokopedia.search.R;
import com.tokopedia.search.result.presentation.model.CatalogViewModel;
import com.tokopedia.search.result.presentation.view.listener.CatalogListener;
import com.tokopedia.search.result.presentation.view.widget.SquareImageView;

public class GridCatalogViewHolder extends AbstractViewHolder<CatalogViewModel> {

    @LayoutRes
    public static final int LAYOUT = R.layout.search_gridview_browse_catalog;
    protected final View container;

    protected SquareImageView catalogImage;
    protected TextView catalogTitle;
    protected TextView catalogPrice;
    protected TextView catalogSeller;
    protected LinearLayout badgesContainer;

    protected final Context context;
    protected final CatalogListener listener;

    public GridCatalogViewHolder(View itemView, CatalogListener mCatalogListener) {
        super(itemView);
        context = itemView.getContext();
        this.listener = mCatalogListener;
        this.catalogImage = (SquareImageView) itemView.findViewById(R.id.product_image);
        this.catalogTitle = (TextView) itemView.findViewById(R.id.title);
        this.catalogPrice = (TextView) itemView.findViewById(R.id.price);
        this.catalogSeller = (TextView) itemView.findViewById(R.id.seller);
        this.badgesContainer = (LinearLayout) itemView.findViewById(R.id.badges_container);
        this.container = itemView;
    }

    @Override
    public void bind(final CatalogViewModel element) {
        setCatalogImage(element);
        catalogSeller.setText(element.getProductCounter() + " " + context.getString(R.string.title_total_prods));
        catalogTitle.setText(MethodChecker.fromHtml(element.getName()));
        catalogPrice.setText(element.getPrice());
        container.setOnClickListener(v -> listener.setOnCatalogClicked(element.getID(), element.getName()));
    }

    public void setCatalogImage(CatalogViewModel catalog) {
        ImageHandler.loadImageThumbs(context, catalogImage, catalog.getImage());
    }
}