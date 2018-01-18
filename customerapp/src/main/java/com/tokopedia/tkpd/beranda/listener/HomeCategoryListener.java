package com.tokopedia.tkpd.beranda.listener;

import com.tokopedia.digital.tokocash.model.CashBackData;
import com.tokopedia.tkpd.beranda.domain.model.banner.BannerSlidesModel;
import com.tokopedia.tkpd.beranda.domain.model.brands.BrandDataModel;
import com.tokopedia.tkpd.beranda.domain.model.category.CategoryLayoutRowModel;
import com.tokopedia.tkpd.beranda.domain.model.toppicks.TopPicksItemModel;
import com.tokopedia.tkpd.beranda.presentation.view.adapter.viewmodel.LayoutSections;

/**
 * @author by errysuprayogi on 11/29/17.
 */

public interface HomeCategoryListener {

    void onSectionItemClicked(LayoutSections sections, int parentPosition, int childPosition);

    void onMarketPlaceItemClicked(CategoryLayoutRowModel data, int parentPosition, int childPosition);

    void onDigitalItemClicked(CategoryLayoutRowModel data, int parentPosition, int childPosition);

    void onGimickItemClicked(CategoryLayoutRowModel data, int parentPosition, int childPosition);

    void onApplinkClicked(CategoryLayoutRowModel data, int parentPosition, int childPosition);

    void onTopPicksItemClicked(TopPicksItemModel data, int parentPosition, int childPosition);

    void onTopPicksMoreClicked(String url, int pos);

    void onBrandsItemClicked(BrandDataModel data, int parentPosition, int childPosition);

    void onBrandsMoreClicked(int pos);

    void onDigitalMoreClicked(int pos);

    void onCloseTicker(int pos);

    void onPromoClick(BannerSlidesModel slidesModel);

    void openShop();

    void actionAppLinkWalletHeader(String redirectUrlBalance, String appLinkBalance);

    void onRequestPendingCashBack();

    void actionInfoPendingCashBackTokocash(CashBackData cashBackData, String redirectUrlActionButton, String appLinkActionButton);

    void actionTokoPointClicked(String tokoPointUrl, String pageTitle);

    void actionScannerQRTokoCash();

    boolean isMainViewVisible();

    void showNetworkError(String message);
}
