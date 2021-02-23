package com.tokopedia.topads.sdk.view.adapter.viewholder.banner;

import android.view.View;

import androidx.annotation.LayoutRes;

import com.tokopedia.topads.sdk.R;
import com.tokopedia.topads.sdk.base.adapter.viewholder.AbstractViewHolder;
import com.tokopedia.topads.sdk.listener.TopAdsBannerClickListener;
import com.tokopedia.topads.sdk.utils.TopAdsUrlHitter;
import com.tokopedia.topads.sdk.view.adapter.viewmodel.banner.BannerShopViewMoreModel;

/**
 * Created by errysuprayogi on 4/16/18.
 */

public class BannerShowMoreViewHolder extends AbstractViewHolder<BannerShopViewMoreModel> {

    @LayoutRes
    public static int LAYOUT = R.layout.layout_ads_banner_shop_a_more;
    private static final String className = BannerShowMoreViewHolder.class.getSimpleName();
    private final TopAdsBannerClickListener topAdsBannerClickListener;

    public BannerShowMoreViewHolder(View itemView, TopAdsBannerClickListener topAdsBannerClickListener) {
        super(itemView);
        this.topAdsBannerClickListener = topAdsBannerClickListener;
    }

    @Override
    public void bind(final BannerShopViewMoreModel element) {
        itemView.setOnClickListener(v -> {
            if(topAdsBannerClickListener!=null) {
                topAdsBannerClickListener.onBannerAdsClicked(getAdapterPosition(), element.getAppLink(), element.getCpmData());
                new TopAdsUrlHitter(itemView.getContext()).hitClickUrl(className, element.getAdsClickUrl(),"","","");
            }
        });
    }
}
