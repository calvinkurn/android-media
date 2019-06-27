package com.tokopedia.home.account.presentation.listener;

import com.tokopedia.home.account.presentation.viewmodel.BuyerCardViewModel;
import com.tokopedia.home.account.presentation.viewmodel.InfoCardViewModel;
import com.tokopedia.home.account.presentation.viewmodel.MenuGridItemViewModel;
import com.tokopedia.home.account.presentation.viewmodel.MenuGridViewModel;
import com.tokopedia.home.account.presentation.viewmodel.MenuListViewModel;
import com.tokopedia.home.account.presentation.viewmodel.SellerSaldoViewModel;
import com.tokopedia.home.account.presentation.viewmodel.ShopCardViewModel;
import com.tokopedia.home.account.presentation.viewmodel.TokopediaPayBSModel;

/**
 * @author okasurya on 7/26/18.
 */
public interface AccountItemListener {

    void onProfileClicked(BuyerCardViewModel element);

    void onProfileCompletionClicked(BuyerCardViewModel element);

    void onBuyerTokopointClicked(BuyerCardViewModel element);

    void onBuyerVoucherClicked(BuyerCardViewModel element);

    void onByMeClicked();

    void onTokopediaPayLinkClicked();

    void onMenuGridItemClicked(MenuGridItemViewModel item);

    void onMenuGridLinkClicked(MenuGridViewModel item);

    void onInfoCardClicked(InfoCardViewModel item, int adapterPosition);

    void onMenuListClicked(MenuListViewModel item);

    void onShopAvatarClicked(ShopCardViewModel element);

    void onShopNameClicked(ShopCardViewModel element);

    void onAddProductClicked();

    void onTokopediaPayLeftItemClicked(String label, String applink, TokopediaPayBSModel bsData,
                                       boolean isLinked, String walletType);

    void onTokopediaPayRightItemClicked(boolean isRightSaldo, String label, String vccStatus, String applink, TokopediaPayBSModel bsData);

    void onDepositClicked(SellerSaldoViewModel element);

    void onTopadsInfoClicked();

    void onGMInfoClicked();

    void onSellerCenterInfoClicked();

    void onOpenShopClicked();

    void onLearnMoreSellerClicked();

    void onKycLinkClicked(int verificationStatus);

    void onTickerLinkClicked(String url);

    void onTickerClosed();

    void onTopAdsMenuClicked();

    void onShopStatusInfoButtonClicked();

}
