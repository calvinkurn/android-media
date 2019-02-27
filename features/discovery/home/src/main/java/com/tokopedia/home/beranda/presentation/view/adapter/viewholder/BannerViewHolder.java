package com.tokopedia.home.beranda.presentation.view.adapter.viewholder;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.view.View;

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.design.banner.BannerView;
import com.tokopedia.home.R;
import com.tokopedia.home.analytics.HomePageTracking;
import com.tokopedia.home.beranda.data.model.Promotion;
import com.tokopedia.home.beranda.domain.model.banner.BannerSlidesModel;
import com.tokopedia.home.beranda.listener.ActivityStateListener;
import com.tokopedia.home.beranda.listener.HomeCategoryListener;
import com.tokopedia.home.beranda.presentation.view.adapter.viewmodel.BannerViewModel;
import com.tokopedia.home.beranda.presentation.view.analytics.HomeTrackingUtils;
import com.tokopedia.home.beranda.presentation.view.customview.HomeBannerView;

import java.util.ArrayList;
import java.util.List;

/**
 * @author by errysuprayogi on 11/28/17.
 */

public class BannerViewHolder extends AbstractViewHolder<BannerViewModel> implements BannerView.OnPromoClickListener, BannerView.OnPromoScrolledListener,
        BannerView.OnPromoAllClickListener, BannerView.OnPromoLoadedListener, BannerView.OnPromoDragListener, ActivityStateListener{

    @LayoutRes
    public static final int LAYOUT = R.layout.home_banner;
    public static final String ATTRIBUTION = "attribution";
    private HomeBannerView bannerView;
    private final HomeCategoryListener listener;
    private final Context context;
    private List<BannerSlidesModel> slidesList;
    private boolean hasSendBannerImpression = false;

    public BannerViewHolder(View itemView, HomeCategoryListener listener) {
        super(itemView);
        this.listener = listener;
        this.context = itemView.getContext();
        bannerView = itemView.findViewById(R.id.banner);
        bannerView.setOnPromoAllClickListener(this);
        bannerView.setOnPromoClickListener(this);
        bannerView.setOnPromoScrolledListener(this);
        bannerView.setOnPromoLoadedListener(this);
        bannerView.setOnPromoDragListener(this);
        listener.setActivityStateListener(this);
    }

    @Override
    public void bind(BannerViewModel element) {
        try {
            slidesList = element.getSlides();
            List<String> promoUrls = new ArrayList<>();
            for (BannerSlidesModel slidesModel : slidesList) {
                promoUrls.add(slidesModel.getImageUrl());
            }
            bannerView.setPromoList(promoUrls);
            bannerView.buildView();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Promotion getPromotion(int position) {
        BannerSlidesModel model = slidesList.get(position);
        Promotion promotion = new Promotion();
        promotion.setPromotionID(String.valueOf(model.getId()));
        promotion.setPromotionName("/ - p1 - promo");
        promotion.setPromotionAlias(model.getTitle().trim().replaceAll(" ", "_"));
        promotion.setPromotionPosition(position + 1);
        promotion.setRedirectUrl(slidesList.get(position).getRedirectUrl());
        promotion.setPromoCode(model.getPromoCode());
        return promotion;
    }

    @Override
    public void onPromoClick(int position) {
        Promotion promotion = getPromotion(position);
        HomePageTracking.eventPromoClick(context, promotion);
        listener.onPromoClick(position, slidesList.get(position),
                String.valueOf(promotion.getImpressionDataLayer().get(ATTRIBUTION)));
        HomeTrackingUtils.homeSlidingBannerClick(context, slidesList.get(position), position);
    }

    @Override
    public void onPromoScrolled(int position) {
        if (listener.isHomeFragment()) {
            HomeTrackingUtils.homeSlidingBannerImpression(context, slidesList.get(position), position);
            listener.onPromoScrolled(slidesList.get(position));
        }
    }

    @Override
    public void onPromoAllClick() {
        listener.onPromoAllClick();
    }

    @Override
    public void onPromoLoaded() {
        if (listener.isHomeFragment() && slidesList != null && slidesList.size() > 0 &&
                !hasSendBannerImpression) {
            List<Promotion> promotionList = new ArrayList<>();
            for (int i = 0, sizei = slidesList.size(); i < sizei; i++) {
                promotionList.add(getPromotion(i));
            }
            HomePageTracking.eventPromoImpression(context, promotionList);
            hasSendBannerImpression = true;
        }
    }

    @Override
    public void onPromoDragStart() {
        listener.onPromoDragStart();
    }

    @Override
    public void onPromoDragEnd() {
        listener.onPromoDragEnd();
    }

    @Override
    public void onPause() {
        bannerView.stopAutoScrollBanner();
    }

    @Override
    public void onResume() {
        bannerView.startAutoScrollBanner();
    }
}
