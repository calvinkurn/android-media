package com.tokopedia.search.result.presentation.view.adapter.viewholder.product;

import android.view.View;

import androidx.annotation.LayoutRes;

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.search.R;
import com.tokopedia.search.result.presentation.model.CpmDataView;
import com.tokopedia.search.result.presentation.view.listener.BannerAdsListener;
import com.tokopedia.topads.sdk.domain.model.CpmData;
import com.tokopedia.topads.sdk.listener.TopAdsItemImpressionListener;
import com.tokopedia.topads.sdk.widget.TopAdsBannerView;

public class CpmViewHolder extends AbstractViewHolder<CpmDataView> {

    @LayoutRes
    public static final int LAYOUT = R.layout.search_result_product_top_ads_banner_layout;

    private TopAdsBannerView adsBannerView;

    public CpmViewHolder(View itemView,
                         BannerAdsListener bannerAdsListener) {
        super(itemView);
        adsBannerView = itemView.findViewById(R.id.ads_banner);
        adsBannerView.setTopAdsBannerClickListener((position, applink, data) -> {
            if (bannerAdsListener != null) {
                bannerAdsListener.onBannerAdsClicked(position, applink, data);
            }
        });
        adsBannerView.setTopAdsImpressionListener(new TopAdsItemImpressionListener() {
            @Override
            public void onImpressionHeadlineAdsItem(int position, CpmData data) {
                if(bannerAdsListener != null) {
                    bannerAdsListener.onBannerAdsImpressionListener(position, data);
                }
            }
        });
    }

    @Override
    public void bind(final CpmDataView element) {
        bindAdsBannerView(element);
    }

    private void bindAdsBannerView(final CpmDataView element) {
        adsBannerView.displayAds(element.getCpmModel());
    }
}
