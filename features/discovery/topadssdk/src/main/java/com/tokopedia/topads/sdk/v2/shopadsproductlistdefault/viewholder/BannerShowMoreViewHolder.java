package com.tokopedia.topads.sdk.v2.shopadsproductlistdefault.viewholder;

import android.view.View;

import androidx.annotation.LayoutRes;

import com.tokopedia.topads.sdk.common.adapter.viewholder.AbstractViewHolder;
import com.tokopedia.topads.sdk.v2.listener.TopAdsBannerClickListener;
import com.tokopedia.topads.sdk.utils.TopAdsUrlHitter;
import com.tokopedia.topads.sdk.v2.shopadsproductlistdefault.uimodel.BannerShopViewMoreUiModel;

/**
 * Created by errysuprayogi on 4/16/18.
 */

public class BannerShowMoreViewHolder extends AbstractViewHolder<BannerShopViewMoreUiModel> {

    @LayoutRes
    public static int LAYOUT = com.tokopedia.topads.sdk.R.layout.layout_ads_banner_shop_a_more;
    private static final String className = BannerShowMoreViewHolder.class.getSimpleName();
    private final TopAdsBannerClickListener topAdsBannerClickListener;

    public BannerShowMoreViewHolder(View itemView, TopAdsBannerClickListener topAdsBannerClickListener) {
        super(itemView);
        this.topAdsBannerClickListener = topAdsBannerClickListener;
    }

    @Override
    public void bind(final BannerShopViewMoreUiModel element) {
        itemView.setOnClickListener(v -> {
            if(topAdsBannerClickListener!=null) {
                topAdsBannerClickListener.onBannerAdsClicked(getAdapterPosition(), element.getAppLink(), element.getCpmData());
                new TopAdsUrlHitter(itemView.getContext()).hitClickUrl(className, element.getAdsClickUrl(),"","","");
            }
        });
    }
}
