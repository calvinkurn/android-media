package com.tokopedia.topads.sdk.view.adapter.viewholder.banner;

import android.content.Context;
import androidx.annotation.LayoutRes;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tokopedia.topads.sdk.R;
import com.tokopedia.topads.sdk.base.adapter.viewholder.AbstractViewHolder;
import com.tokopedia.topads.sdk.domain.model.Cpm;
import com.tokopedia.topads.sdk.listener.TopAdsBannerClickListener;
import com.tokopedia.topads.sdk.listener.TopAdsItemImpressionListener;
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
    private TextView descriptionTxt;
    private TextView ctaTxt;
    private final TopAdsBannerClickListener topAdsBannerClickListener;
    private final TopAdsItemImpressionListener impressionListener;

    public BannerShopViewHolder(View itemView, final TopAdsBannerClickListener topAdsBannerClickListener,
                                TopAdsItemImpressionListener itemImpressionListener) {
        super(itemView);
        this.topAdsBannerClickListener = topAdsBannerClickListener;
        this.impressionListener = itemImpressionListener;
        descriptionTxt = (TextView) itemView.findViewById(R.id.description);
        ctaTxt = (TextView) itemView.findViewById(R.id.kunjungi_toko);
    }

    @Override
    public void bind(final BannerShopViewModel element) {
        final Cpm cpm = element.getCpmData().getCpm();
        if(cpm!=null) {
            descriptionTxt.setText(TopAdsBannerView.escapeHTML(cpm.getCpmShop().getSlogan()));
            ctaTxt.setText(cpm.getCta());
            ctaTxt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(topAdsBannerClickListener!=null) {
                        topAdsBannerClickListener.onBannerAdsClicked(getAdapterPosition(), element.getAppLink(), element.getCpmData());
                        new ImpresionTask().execute(element.getAdsClickUrl());
                    }
                }
            });
        }
    }
}
