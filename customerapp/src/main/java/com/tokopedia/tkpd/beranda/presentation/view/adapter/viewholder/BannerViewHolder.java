package com.tokopedia.tkpd.beranda.presentation.view.adapter.viewholder;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.LayoutRes;
import android.view.View;

import com.tokopedia.core.analytics.PaymentTracking;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.analytics.nishikino.model.Promotion;
import com.tokopedia.core.base.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.core.home.BannerWebView;
import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.remoteconfig.FirebaseRemoteConfigImpl;
import com.tokopedia.design.banner.BannerView;
import com.tokopedia.loyalty.view.activity.PromoListActivity;
import com.tokopedia.tkpd.R;
import com.tokopedia.tkpd.beranda.domain.model.banner.BannerSlidesModel;
import com.tokopedia.tkpd.beranda.listener.HomeCategoryListener;
import com.tokopedia.tkpd.beranda.presentation.view.adapter.viewmodel.BannerViewModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author by errysuprayogi on 11/28/17.
 */

public class BannerViewHolder extends AbstractViewHolder<BannerViewModel> implements BannerView.OnPromoClickListener, BannerView.OnPromoScrolledListener,
        BannerView.OnPromoAllClickListener {

    @LayoutRes
    public static final int LAYOUT = R.layout.home_banner;
    @BindView(R.id.banner)
    BannerView bannerView;
    private final HomeCategoryListener listener;
    private final Context context;
    private List<BannerSlidesModel> slidesList;

    public BannerViewHolder(View itemView, HomeCategoryListener listener) {
        super(itemView);
        this.listener = listener;
        this.context = itemView.getContext();
        ButterKnife.bind(this, itemView);
        bannerView.setOnPromoAllClickListener(this);
        bannerView.setOnPromoClickListener(this);
        bannerView.setOnPromoScrolledListener(this);
    }

    @Override
    public void bind(BannerViewModel element) {
        slidesList = element.getSlides();
        List<String> promoUrls = new ArrayList<>();
        for (BannerSlidesModel slidesModel : slidesList) {
            promoUrls.add(slidesModel.getImageUrl());
        }
        bannerView.setPromoList(promoUrls);
        bannerView.buildView();
    }

    private Promotion getPromotion(int position) {
        BannerSlidesModel model = slidesList.get(position);
        Promotion promotion = new Promotion();
        promotion.setPromotionID(String.valueOf(model.getId()));
        promotion.setPromotionName(model.getTitle());
        promotion.setPromotionAlias(model.getTitle());
        promotion.setPromotionPosition(position);
        return promotion;
    }

    @Override
    public void onPromoClick(int position) {
        PaymentTracking.eventPromoClick(getPromotion(position));
        listener.onPromoClick(slidesList.get(position));
    }

    @Override
    public void onPromoScrolled(int position) {
        if (listener.isMainViewVisible()) {
            PaymentTracking.eventPromoImpression(getPromotion(position));
        }
    }

    @Override
    public void onPromoAllClick() {
        UnifyTracking.eventClickViewAllPromo();

        boolean remoteConfigEnable;
        FirebaseRemoteConfigImpl remoteConfig = new FirebaseRemoteConfigImpl(context);
        remoteConfigEnable = remoteConfig.getBoolean("mainapp_native_promo_list");

        if (remoteConfigEnable) {
            context.startActivity(PromoListActivity.newInstance(context));
        } else {
            Intent intent = new Intent(context, BannerWebView.class);
            intent.putExtra(BannerWebView.EXTRA_TITLE, context.getString(R.string.title_activity_promo));
            intent.putExtra(BannerWebView.EXTRA_URL,
                    TkpdBaseURL.URL_PROMO + TkpdBaseURL.FLAG_APP
            );
            context.startActivity(intent);
        }


    }
}
