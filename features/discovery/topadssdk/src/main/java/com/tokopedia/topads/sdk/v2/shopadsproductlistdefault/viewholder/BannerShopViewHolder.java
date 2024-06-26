package com.tokopedia.topads.sdk.v2.shopadsproductlistdefault.viewholder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.LayoutRes;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.tokopedia.media.loader.JvmMediaLoader;
import com.tokopedia.topads.sdk.R;
import com.tokopedia.topads.sdk.common.adapter.viewholder.AbstractViewHolder;
import com.tokopedia.topads.sdk.domain.model.Cpm;
import com.tokopedia.topads.sdk.v2.listener.TopAdsBannerClickListener;
import com.tokopedia.topads.sdk.v2.listener.TopAdsItemImpressionListener;
import com.tokopedia.topads.sdk.utils.TopAdsUrlHitter;
import com.tokopedia.topads.sdk.v2.shopadsproductlistdefault.uimodel.BannerShopUiModel;
import com.tokopedia.topads.sdk.widget.TopAdsBannerView;

/**
 * Created by errysuprayogi on 4/16/18.
 */

public class BannerShopViewHolder extends AbstractViewHolder<BannerShopUiModel> {

    @LayoutRes
    public static int LAYOUT = com.tokopedia.topads.sdk.R.layout.layout_ads_banner_shop_a;
    private static final String TAG = BannerShopViewHolder.class.getSimpleName();
    private TextView descriptionTxt;
    private TextView ctaTxt;
    private TextView shopName;
    private ImageView bg;
    private ImageView shopImage;
    private ImageView shopBadge;
    private ConstraintLayout cardView;
    private final TopAdsBannerClickListener topAdsBannerClickListener;
    private final TopAdsItemImpressionListener impressionListener;
    private String className = "com.tokopedia.topads.sdk.view.adapter.viewholder.banner.BannerShopViewHolder";

    public BannerShopViewHolder(View itemView, final TopAdsBannerClickListener topAdsBannerClickListener,
                                TopAdsItemImpressionListener itemImpressionListener) {
        super(itemView);
        this.topAdsBannerClickListener = topAdsBannerClickListener;
        this.impressionListener = itemImpressionListener;
        descriptionTxt = (TextView) itemView.findViewById(R.id.description);
        cardView = itemView.findViewById(R.id.cardView);
        ctaTxt = (TextView) itemView.findViewById(R.id.kunjungi_toko);
        bg = itemView.findViewById(R.id.bg);
        shopName = itemView.findViewById(R.id.shop_name);
        shopImage = itemView.findViewById(R.id.shop_image);
        shopBadge = itemView.findViewById(R.id.shop_badge);
    }

    @Override
    public void bind(final BannerShopUiModel element) {
        final Cpm cpm = element.getCpmData().getCpm();
        if(cpm!=null) {
            descriptionTxt.setText(TopAdsBannerView.Companion.escapeHTML(cpm.getCpmShop().getSlogan()));
            if (element.isShowCta()) {
                ctaTxt.setText(cpm.getCta());
                ctaTxt.setVisibility(View.VISIBLE);
            } else {
                ctaTxt.setVisibility(View.GONE);
            }
            cardView.setOnClickListener(v -> {
                if(topAdsBannerClickListener!=null) {
                    topAdsBannerClickListener.onBannerAdsClicked(getAdapterPosition(), element.getAppLink(), element.getCpmData());
                    new TopAdsUrlHitter(itemView.getContext()).hitClickUrl(className, element.getAdsClickUrl(),"","","");
                }
            });
            if(bg!=null) {
                if (cpm.getCpmShop().isOfficial()) {
                    bg.setImageResource(R.drawable.bg_os_ads);
                } else if(cpm.getCpmShop().isPMPro()) {
                    bg.setImageResource(R.drawable.bg_pm_pro_ads);
                } else if (cpm.getCpmShop().isPowerMerchant()) {
                    bg.setImageResource(R.drawable.bg_pm_ads);
                } else {
                    bg.setImageResource(R.drawable.bg_rm_ads);
                }
            }
            if(shopImage!=null){
                if (cpm.getCpmShop().getImageShop().getSEcs() != null) {
                    JvmMediaLoader.loadImage(shopImage, cpm.getCpmShop().getImageShop().getSEcs());
                }

            }
            if(shopName!=null){
                shopName.setText(cpm.getCpmShop().getName());
            }
            if(shopBadge!=null){
                if (cpm.getBadges().size() > 0) {
                    shopBadge.setVisibility(View.VISIBLE);
                    JvmMediaLoader.loadImage(shopBadge, cpm.getBadges().get(0).getImageUrl());
                } else {
                    shopBadge.setVisibility(View.GONE);
                }
            }
        }
    }
}
