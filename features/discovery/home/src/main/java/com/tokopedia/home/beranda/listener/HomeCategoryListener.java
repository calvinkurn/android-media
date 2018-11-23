package com.tokopedia.home.beranda.listener;

import com.tokopedia.home.beranda.domain.model.banner.BannerSlidesModel;
import com.tokopedia.home.beranda.presentation.view.adapter.viewmodel.CashBackData;

/**
 * @author by errysuprayogi on 11/29/17.
 */

public interface HomeCategoryListener {

    void onSectionItemClicked(String actionLink);

    void onDigitalMoreClicked(int pos);

    void onCloseTicker(int pos);

    void onPromoClick(int position, BannerSlidesModel slidesModel, String attribution);

    void openShop();

    void actionAppLinkWalletHeader(String appLinkBalance);

    void onRequestPendingCashBack();

    void actionInfoPendingCashBackTokocash(CashBackData cashBackData, String appLinkActionButton);

    void actionTokoPointClicked(String tokoPointUrl, String pageTitle);

    boolean isMainViewVisible();

    void showNetworkError(String message);

    void onDynamicChannelClicked(String applink, String trackingAttribution);

    void onRefreshTokoPointButtonClicked();

    void onRefreshTokoCashButtonClicked();

    void onSixGridItemClicked(String actionLink, String trackingAttribution);

    void onPromoScrolled(BannerSlidesModel bannerSlidesModel);

    void onServerTimeReceived(long serverTimeUnix);

    long getServerTimeOffset();
  
    boolean isHomeFragment();
}
