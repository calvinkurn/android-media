package com.tokopedia.home.account.presentation.listener;

import com.tokopedia.home.account.presentation.viewmodel.BuyerCardViewModel;
import com.tokopedia.home.account.presentation.viewmodel.InfoCardViewModel;
import com.tokopedia.home.account.presentation.viewmodel.MenuGridItemViewModel;
import com.tokopedia.home.account.presentation.viewmodel.MenuGridViewModel;
import com.tokopedia.home.account.presentation.viewmodel.MenuListViewModel;
import com.tokopedia.home.account.presentation.viewmodel.ShopCardViewModel;

/**
 * @author okasurya on 7/26/18.
 */
public interface AccountItemListener {

    void onProfileClicked(BuyerCardViewModel element);

    void onProfileCompletionClicked(BuyerCardViewModel element);

    void onBuyerTokopointClicked(BuyerCardViewModel element);

    void onBuyerVoucherClicked(BuyerCardViewModel element);

    void onTokopediaPayLinkClicked();

    void onMenuGridItemClicked(MenuGridItemViewModel item);

    void onMenuGridLinkClicked(MenuGridViewModel item);

    void onInfoCardClicked(InfoCardViewModel item);

    void onMenuListClicked(MenuListViewModel item);

    void onShopAvatarClicked(ShopCardViewModel element);

    void onShopNameClicked(ShopCardViewModel element);

    void onAddProductClicked();

    void onTokopediaPayItemClicked(String applink);

    void onDepositClicked(ShopCardViewModel element);
}
