package com.tokopedia.home.account.presentation.fragment;

import android.content.Intent;
import android.text.TextUtils;

import com.tokopedia.abstraction.base.view.fragment.TkpdBaseV4Fragment;
import com.tokopedia.applink.ApplinkConst;
import com.tokopedia.applink.RouteManager;
import com.tokopedia.home.account.AccountConstants;
import com.tokopedia.home.account.presentation.activity.TkpdPaySettingActivity;
import com.tokopedia.home.account.presentation.listener.AccountItemListener;
import com.tokopedia.home.account.presentation.view.SeeAllView;
import com.tokopedia.home.account.presentation.viewmodel.BuyerCardViewModel;
import com.tokopedia.home.account.presentation.viewmodel.InfoCardViewModel;
import com.tokopedia.home.account.presentation.viewmodel.MenuGridItemViewModel;
import com.tokopedia.home.account.presentation.viewmodel.MenuGridViewModel;
import com.tokopedia.home.account.presentation.viewmodel.MenuListViewModel;
import com.tokopedia.home.account.presentation.viewmodel.ShopCardViewModel;

/**
 * @author okasurya on 7/26/18.
 */
public abstract class BaseAccountFragment extends TkpdBaseV4Fragment implements AccountItemListener {
    public static final String PARAM_USER_ID = "{user_id}";
    public static final String PARAM_SHOP_ID = "{shop_id}";
    SeeAllView seeAllView;

    protected void openApplink(String applink) {
        if(getContext() != null && !TextUtils.isEmpty(applink)) {
            RouteManager.route(getContext(), applink);
        }
    }

    @Override
    public void onBuyerAvatarClicked(BuyerCardViewModel element) {
        openApplink(ApplinkConst.PROFILE.replace(PARAM_USER_ID, element.getUserId()));
    }

    @Override
    public void onBuyerNameClicked(BuyerCardViewModel element) {
        openApplink(ApplinkConst.PROFILE.replace(PARAM_USER_ID, element.getUserId()));
    }

    @Override
    public void onProfileCompletionClicked(BuyerCardViewModel element) {

    }

    @Override
    public void onBuyerTokopointClicked(BuyerCardViewModel element) {

    }

    @Override
    public void onBuyerVoucherClicked(BuyerCardViewModel element) {

    }

    @Override
    public void onTokopediaPayLinkClicked() {
        if(getActivity() != null) {
            getActivity().startActivity(new Intent(getContext(), TkpdPaySettingActivity.class));
        }
    }

    @Override
    public void onMenuGridItemClicked(MenuGridItemViewModel item) {
        if (TextUtils.equals(item.getApplink(), AccountConstants.KEY_SEE_ALL)){
            seeAllView = new SeeAllView();
            seeAllView.setListener(this);
            seeAllView.show(getActivity().getSupportFragmentManager(), "Tes");
        }
        openApplink(item.getApplink());
    }

    @Override
    public void onMenuGridLinkClicked(MenuGridViewModel item) {
        openApplink(item.getApplinkUrl());
    }

    @Override
    public void onInfoCardClicked(InfoCardViewModel item) {
        openApplink(item.getApplink());
    }

    @Override
    public void onMenuListClicked(MenuListViewModel item) {
        openApplink(item.getApplink());
    }

    @Override
    public void onShopAvatarClicked(ShopCardViewModel element) {
        openApplink(ApplinkConst.SHOP.replace(PARAM_SHOP_ID, element.getShopId()));
    }

    @Override
    public void onShopNameClicked(ShopCardViewModel element) {
        openApplink(ApplinkConst.SHOP.replace(PARAM_SHOP_ID, element.getShopId()));
    }

    @Override
    public void onAddProductClicked() {
        openApplink(ApplinkConst.PRODUCT_ADD);
    }
}
