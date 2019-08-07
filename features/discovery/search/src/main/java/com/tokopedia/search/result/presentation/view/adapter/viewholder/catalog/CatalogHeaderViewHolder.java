package com.tokopedia.search.result.presentation.view.adapter.viewholder.catalog;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.view.View;

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.search.R;
import com.tokopedia.search.result.presentation.model.CatalogHeaderViewModel;
import com.tokopedia.search.result.presentation.view.listener.BannerAdsListener;
import com.tokopedia.search.result.presentation.view.listener.CatalogListener;
import com.tokopedia.topads.sdk.analytics.TopAdsGtmTracker;
import com.tokopedia.topads.sdk.base.Config;
import com.tokopedia.topads.sdk.base.Endpoint;
import com.tokopedia.topads.sdk.domain.TopAdsParams;
import com.tokopedia.topads.sdk.domain.model.CpmData;
import com.tokopedia.topads.sdk.listener.TopAdsBannerClickListener;
import com.tokopedia.topads.sdk.listener.TopAdsItemImpressionListener;
import com.tokopedia.topads.sdk.widget.TopAdsBannerView;

public class CatalogHeaderViewHolder extends AbstractViewHolder<CatalogHeaderViewModel> {

    @LayoutRes
    public static final int LAYOUT = R.layout.catalog_header_layout;
    public static final String DEFAULT_ITEM_VALUE = "1";
    private TopAdsBannerView adsBannerView;
    private Context context;
    public static final String KEYWORD = "keyword";
    public static final String ETALASE_NAME = "etalase_name";
    private CatalogListener catalogListener;
    private BannerAdsListener bannerAdsListener;
    public static final String SHOP = "shop";

    public CatalogHeaderViewHolder(View itemView, CatalogListener catalogListener, BannerAdsListener bannerAdsListener, Config topAdsConfig) {
        super(itemView);
        context = itemView.getContext();
        this.catalogListener = catalogListener;
        this.bannerAdsListener = bannerAdsListener;
        adsBannerView = (TopAdsBannerView) itemView.findViewById(R.id.ads_banner);
        initTopAds(topAdsConfig);
    }

    private void initTopAds(Config topAdsConfig) {
        TopAdsParams adsParams = new TopAdsParams();
        adsParams.getParam().putAll(topAdsConfig.getTopAdsParams().getParam());
        adsParams.getParam().put(TopAdsParams.KEY_ITEM, DEFAULT_ITEM_VALUE);

        Config config = new Config.Builder()
                .setSessionId(catalogListener.getRegistrationId())
                .setUserId(catalogListener.getUserId())
                .setEndpoint(Endpoint.CPM)
                .topAdsParams(adsParams)
                .build();
        adsBannerView.setConfig(config);
        adsBannerView.loadTopAds();
        adsBannerView.setTopAdsBannerClickListener((position, applink, data) -> {
            if (bannerAdsListener != null) {
                bannerAdsListener.onBannerAdsClicked(position, applink, data);
            }
        });
        adsBannerView.setTopAdsImpressionListener(new TopAdsItemImpressionListener() {
            @Override
            public void onImpressionHeadlineAdsItem(int position, CpmData data) {
                if (bannerAdsListener != null) {
                    bannerAdsListener.onBannerAdsImpressionListener(position, data);
                }
            }
        });
    }

    @Override
    public void bind(final CatalogHeaderViewModel element) {

    }
}
