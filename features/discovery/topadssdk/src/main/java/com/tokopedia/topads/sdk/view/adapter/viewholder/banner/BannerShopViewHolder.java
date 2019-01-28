package com.tokopedia.topads.sdk.view.adapter.viewholder.banner;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.LayoutRes;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.tokopedia.topads.sdk.R;
import com.tokopedia.topads.sdk.analytics.TopAdsGtmTracker;
import com.tokopedia.topads.sdk.base.adapter.viewholder.AbstractViewHolder;
import com.tokopedia.topads.sdk.domain.model.Cpm;
import com.tokopedia.topads.sdk.listener.TopAdsBannerClickListener;
import com.tokopedia.topads.sdk.utils.ImageLoader;
import com.tokopedia.topads.sdk.utils.ImpresionTask;
import com.tokopedia.topads.sdk.view.ImpressedImageView;
import com.tokopedia.topads.sdk.widget.TopAdsBannerView;
import com.tokopedia.topads.sdk.view.adapter.viewmodel.banner.BannerShopViewModel;

/**
 * Created by errysuprayogi on 4/16/18.
 */

public class BannerShopViewHolder extends AbstractViewHolder<BannerShopViewModel> {

    @LayoutRes
    public static final int LAYOUT = R.layout.layout_ads_banner_shop;
    private static final String TAG = BannerShopViewHolder.class.getSimpleName();
    private Context context;
    private ImpressedImageView iconImg;
    private TextView nameTxt;
    private TextView descriptionTxt;
    private TextView ctaTxt;
    private ImageLoader imageLoader;
    private LinearLayout layoutContainer;
    private final TopAdsBannerClickListener topAdsBannerClickListener;

    public BannerShopViewHolder(View itemView, final TopAdsBannerClickListener topAdsBannerClickListener) {
        super(itemView);
        this.topAdsBannerClickListener = topAdsBannerClickListener;
        context = itemView.getContext();
        imageLoader = new ImageLoader(context);
        iconImg = (ImpressedImageView) itemView.findViewById(R.id.icon);
        descriptionTxt = (TextView) itemView.findViewById(R.id.description);
        ctaTxt = (TextView) itemView.findViewById(R.id.kunjungi_toko);
        layoutContainer = itemView.findViewById(R.id.layout_container);
    }

    @Override
    public void bind(final BannerShopViewModel element) {
        final Cpm cpm = element.getCpmData().getCpm();
        if(cpm!=null) {
            Glide.with(context).load(cpm.getCpmImage().getFullEcs()).asBitmap().into(new SimpleTarget<Bitmap>() {
                @Override
                public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                    iconImg.setImageBitmap(resource);
                    new ImpresionTask().execute(cpm.getCpmImage().getFullUrl());
                }
            });
            descriptionTxt.setText(TopAdsBannerView.escapeHTML(cpm.getCpmShop().getSlogan()));
            ctaTxt.setText(cpm.getCta());
            layoutContainer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(topAdsBannerClickListener!=null) {
                        topAdsBannerClickListener.onBannerAdsClicked(element.getAppLink(), element.getCpmData());
                        new ImpresionTask().execute(element.getAdsClickUrl());
                    }
                }
            });
        }
    }
}
