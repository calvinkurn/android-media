package com.tokopedia.topads.sdk.view.adapter.viewholder.banner;

import androidx.annotation.LayoutRes;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.tokopedia.topads.sdk.R;
import com.tokopedia.topads.sdk.base.adapter.viewholder.AbstractViewHolder;
import com.tokopedia.topads.sdk.domain.model.Cpm;
import com.tokopedia.topads.sdk.listener.TopAdsBannerClickListener;
import com.tokopedia.topads.sdk.listener.TopAdsItemImpressionListener;
import com.tokopedia.topads.sdk.utils.ImpresionTask;
import com.tokopedia.topads.sdk.widget.TopAdsBannerView;
import com.tokopedia.topads.sdk.view.adapter.viewmodel.banner.BannerShopViewModel;

/**
 * Created by errysuprayogi on 4/16/18.
 */

public class BannerShopViewHolder extends AbstractViewHolder<BannerShopViewModel> {

    @LayoutRes
    public static int LAYOUT = R.layout.layout_ads_banner_shop_a;
    private static final String TAG = BannerShopViewHolder.class.getSimpleName();
    private TextView descriptionTxt;
    private TextView ctaTxt;
    private TextView shopName;
    private ImageView bg;
    private ImageView shopImage;
    private ImageView shopBadge;
    private final TopAdsBannerClickListener topAdsBannerClickListener;
    private final TopAdsItemImpressionListener impressionListener;

    public BannerShopViewHolder(View itemView, final TopAdsBannerClickListener topAdsBannerClickListener,
                                TopAdsItemImpressionListener itemImpressionListener) {
        super(itemView);
        this.topAdsBannerClickListener = topAdsBannerClickListener;
        this.impressionListener = itemImpressionListener;
        descriptionTxt = (TextView) itemView.findViewById(R.id.description);
        ctaTxt = (TextView) itemView.findViewById(R.id.kunjungi_toko);
        bg = itemView.findViewById(R.id.bg);
        shopName = itemView.findViewById(R.id.shop_name);
        shopImage = itemView.findViewById(R.id.shop_image);
        shopBadge = itemView.findViewById(R.id.shop_badge);
    }

    @Override
    public void bind(final BannerShopViewModel element) {
        final Cpm cpm = element.getCpmData().getCpm();
        if(cpm!=null) {
            descriptionTxt.setText(TopAdsBannerView.Companion.escapeHTML(cpm.getCpmShop().getSlogan()));
            ctaTxt.setText(cpm.getCta());
            ctaTxt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(topAdsBannerClickListener!=null) {
                        topAdsBannerClickListener.onBannerAdsClicked(getAdapterPosition(), element.getAppLink(), element.getCpmData());
                        new ImpresionTask(BannerShopViewHolder.class).execute(element.getAdsClickUrl());
                    }
                }
            });
            if(bg!=null) {
                if (cpm.getCpmShop().isPowerMerchant()) {
                    bg.setImageResource(R.drawable.bg_pm_ads);
                } else {
                    bg.setImageResource(R.drawable.bg_rm_ads);
                }
                if (cpm.getCpmShop().isOfficial()) {
                    bg.setImageResource(R.drawable.bg_os_ads);
                }
            }
            if(shopImage!=null){
                Glide.with(shopImage).load(cpm.getCpmShop().getImageShop().getsEcs()).into(shopImage);
            }
            if(shopName!=null){
                shopName.setText(cpm.getCpmShop().getName());
            }
            if(shopBadge!=null){
                if (cpm.getBadges().size() > 0) {
                    shopBadge.setVisibility(View.VISIBLE);
                    Glide.with(shopBadge).load(cpm.getBadges().get(0).getImageUrl()).into(shopBadge);
                } else {
                    shopBadge.setVisibility(View.GONE);
                }
            }
        }
    }
}
