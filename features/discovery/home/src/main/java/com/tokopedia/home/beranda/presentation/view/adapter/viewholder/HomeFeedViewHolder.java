package com.tokopedia.home.beranda.presentation.view.adapter.viewholder;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.home.R;
import com.tokopedia.home.beranda.presentation.view.viewmodel.HomeFeedViewModel;

public class HomeFeedViewHolder extends AbstractViewHolder<HomeFeedViewModel> {

    @LayoutRes
    public static final int LAYOUT = R.layout.home_feed_item;

    private final Context context;

    private ImageView productImage;
    private TextView title;
    private TextView price;

    public HomeFeedViewHolder(View itemView) {
        super(itemView);
        context = itemView.getContext();
        productImage = itemView.findViewById(R.id.product_image);
        title = itemView.findViewById(R.id.title);
        price = itemView.findViewById(R.id.price);
    }

    @Override
    public void bind(HomeFeedViewModel element) {
        ImageHandler.loadImageSourceSize(context, productImage, element.getImageUrl());
        title.setText(element.getProductName());
        price.setText(element.getPrice());
    }
}
