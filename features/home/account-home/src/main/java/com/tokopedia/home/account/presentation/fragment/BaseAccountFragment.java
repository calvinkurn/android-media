package com.tokopedia.home.account.presentation.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.tokopedia.abstraction.base.view.fragment.TkpdBaseV4Fragment;
import com.tokopedia.applink.ApplinkConst;
import com.tokopedia.applink.RouteManager;
import com.tokopedia.home.account.AccountAnalytics;
import com.tokopedia.home.account.AccountConstants;
import com.tokopedia.home.account.R;
import com.tokopedia.home.account.presentation.activity.TkpdPaySettingActivity;
import com.tokopedia.home.account.presentation.listener.AccountItemListener;
import com.tokopedia.home.account.presentation.view.SeeAllView;
import com.tokopedia.home.account.presentation.viewmodel.BuyerCardViewModel;
import com.tokopedia.home.account.presentation.viewmodel.InfoCardViewModel;
import com.tokopedia.home.account.presentation.viewmodel.MenuGridItemViewModel;
import com.tokopedia.home.account.presentation.viewmodel.MenuGridViewModel;
import com.tokopedia.home.account.presentation.viewmodel.MenuListViewModel;
import com.tokopedia.home.account.presentation.viewmodel.ShopCardViewModel;
import com.tokopedia.home.account.presentation.viewmodel.TokopediaPayViewModel;

import static com.tokopedia.home.account.AccountConstants.Analytics.*;

/**
 * @author okasurya on 7/26/18.
 */
public abstract class BaseAccountFragment extends TkpdBaseV4Fragment implements AccountItemListener {
    public static final String PARAM_USER_ID = "{user_id}";
    public static final String PARAM_SHOP_ID = "{shop_id}";
    SeeAllView seeAllView;

    private AccountAnalytics accountAnalytics;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        accountAnalytics = new AccountAnalytics(getActivity());
    }

    protected void openApplink(String applink) {
        if(getContext() != null && !TextUtils.isEmpty(applink)) {
            RouteManager.route(getContext(), applink);
        }
    }

    @Override
    public void onProfileClicked(BuyerCardViewModel element) {
        sendTracking(PEMBELI, AKUN_SAYA,
                String.format("%s %s", CLICK, PROFILE));
        openApplink(ApplinkConst.PROFILE.replace(PARAM_USER_ID, element.getUserId()));
    }

    @Override
    public void onProfileCompletionClicked(BuyerCardViewModel element) {
        openApplink(ApplinkConst.PROFILE_COMPLETION);
    }

    @Override
    public void onBuyerTokopointClicked(BuyerCardViewModel element) {
        sendTracking(PEMBELI, AKUN_SAYA, TOKOPOINTS);
        openApplink(ApplinkConst.TOKOPOINTS);
    }

    @Override
    public void onBuyerVoucherClicked(BuyerCardViewModel element) {
        sendTracking(PEMBELI, AKUN_SAYA, MY_COUPON);
        openApplink(ApplinkConst.COUPON);
    }

    @Override
    public void onTokopediaPayLinkClicked() {
        if(getActivity() != null) {
            sendTracking(PEMBELI,
                    getString(R.string.title_tkpd_pay_setting),
                    getString(R.string.label_tokopedia_pay_see_all));
            getActivity().startActivity(new Intent(getContext(), TkpdPaySettingActivity.class));
        }
    }

    @Override
    public void onMenuGridItemClicked(MenuGridItemViewModel item) {
        if (TextUtils.equals(item.getApplink(), AccountConstants.KEY_SEE_ALL)){
            seeAllView = new SeeAllView();
            seeAllView.setListener(this);
            seeAllView.show(getActivity().getSupportFragmentManager(), SeeAllView.class.getName());
        } else {
            sendTracking(item.getTitleTrack(),
                    item.getSectionTrack(),
                    item.getDescription());
            openApplink(item.getApplink());
        }
    }

    @Override
    public void onMenuGridLinkClicked(MenuGridViewModel item) {
        sendTracking(item.getTitleTrack(),
                item.getSectionTrack(),
                item.getLinkText());
        openApplink(item.getApplinkUrl());
    }

    @Override
    public void onInfoCardClicked(InfoCardViewModel item) {
        sendTracking(item.getTitleTrack(), item.getSectionTrack(), item.getMainText());
        openApplink(item.getApplink());
    }

    @Override
    public void onMenuListClicked(MenuListViewModel item) {
        sendTracking(item.getTitleTrack(), item.getSectionTrack(), item.getMenu());
        openApplink(item.getApplink());
    }

    @Override
    public void onShopAvatarClicked(ShopCardViewModel element) {
        if(element != null && !TextUtils.isEmpty(element.getShopId())) {
            openApplink(ApplinkConst.SHOP.replace(PARAM_SHOP_ID, element.getShopId()));
        }
    }

    @Override
    public void onShopNameClicked(ShopCardViewModel element) {
        if(element != null && !TextUtils.isEmpty(element.getShopId())) {
            openApplink(ApplinkConst.SHOP.replace(PARAM_SHOP_ID, element.getShopId()));
        }
    }

    @Override
    public void onAddProductClicked() {
        sendTracking(PENJUAL, getString(R.string.title_menu_product),
                getString(R.string.label_button_add_product));
        openApplink(ApplinkConst.PRODUCT_ADD);
    }

    @Override
    public void onTokopediaPayItemClicked(String label, String applink) {
        sendTracking(PEMBELI, getString(R.string.label_tokopedia_pay_title), label);
        openApplink(applink);
    }

    @Override
    public void onDepositClicked(ShopCardViewModel element) {
        openApplink(ApplinkConst.DEPOSIT);
    }

    private void sendTracking(String title, String section, String item) {
        if (accountAnalytics == null)
            return;

        if (title == null || section == null || item == null)
            return;

        accountAnalytics.eventClickAccount(
                title.toLowerCase(),
                section.toLowerCase(),
                item.toLowerCase());
    }
}
