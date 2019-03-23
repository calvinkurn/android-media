package com.tokopedia.home.beranda.listener;

import com.tokopedia.home.beranda.domain.model.banner.BannerSlidesModel;
import com.tokopedia.home.beranda.presentation.view.adapter.viewholder.BannerViewHolder;
import com.tokopedia.home.beranda.presentation.view.adapter.viewmodel.CashBackData;

/**
 * @author by errysuprayogi on 11/29/17.
 */

public interface HomeCategoryListener {

    boolean isMainViewVisible();

    void onSectionItemClicked(String actionLink);

    void onDigitalMoreClicked(int pos);

    void onCloseTicker(int pos);

    void onPromoClick(int position, BannerSlidesModel slidesModel, String attribution);

    void openShop();

    void actionAppLinkWalletHeader(String appLinkBalance);

    void onRequestPendingCashBack();

    void actionInfoPendingCashBackTokocash(CashBackData cashBackData, String appLinkActionButton);

    void actionTokoPointClicked(String appLink, String tokoPointUrl, String pageTitle);

    void showNetworkError(String message);

    void onDynamicChannelClicked(String applink, String trackingAttribution);

    void onRefreshTokoPointButtonClicked();

    void onRefreshTokoCashButtonClicked();

    void onSixGridItemClicked(String actionLink, String trackingAttribution);

    void onThreeGridItemClicked(String actionLink, String trackingAttribution);

    void onPromoScrolled(BannerSlidesModel bannerSlidesModel);

    void onPromoAllClick();

    boolean isHomeFragment();

    void onPromoDragStart();

    void onPromoDragEnd();

    void setActivityStateListener(ActivityStateListener activityStateListener);

    void onDynamicIconScrollStart();

    void onDynamicIconScrollEnd();

    void onSpotlightItemClicked(String actionLink);

    void onTokopointCheckNowClicked(String applink);
}
