package com.tokopedia.topads.sdk.view.adapter.viewholder.banner;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.LayoutRes;

import com.bumptech.glide.Glide;
import com.tokopedia.topads.sdk.R;
import com.tokopedia.topads.sdk.base.adapter.viewholder.AbstractViewHolder;
import com.tokopedia.topads.sdk.domain.model.Cpm;
import com.tokopedia.topads.sdk.listener.TopAdsBannerClickListener;
import com.tokopedia.topads.sdk.listener.TopAdsItemImpressionListener;
import com.tokopedia.topads.sdk.utils.ImpresionTask;
import com.tokopedia.topads.sdk.view.adapter.viewmodel.banner.BannerShopViewModel;
import com.tokopedia.topads.sdk.widget.TopAdsBannerView;

import okhttp3.Route;

/**
 * Created by errysuprayogi on 4/16/18.
 */

public class BannerShowMoreViewHolder extends AbstractViewHolder<BannerShopViewModel> {

    @LayoutRes
    public static int LAYOUT = R.layout.layout_ads_banner_shop_a_more;
    private static final String TAG = BannerShowMoreViewHolder.class.getSimpleName();
    private final TopAdsBannerClickListener topAdsBannerClickListener;
    private TextView txtMore;

    public BannerShowMoreViewHolder(View itemView, TopAdsBannerClickListener topAdsBannerClickListener) {
        super(itemView);
        this.topAdsBannerClickListener = topAdsBannerClickListener;
        txtMore = itemView.findViewById(R.id.txt_more);
    }

    @Override
    public void bind(final BannerShopViewModel element) {
        txtMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(topAdsBannerClickListener!=null) {
                    topAdsBannerClickListener.onBannerAdsClicked(getAdapterPosition(), element.getAppLink(), element.getCpmData());
                    new ImpresionTask().execute(element.getAdsClickUrl());
                }
            }
        });
    }
}
