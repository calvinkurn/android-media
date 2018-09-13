package com.tokopedia.home.account.presentation.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.webkit.URLUtil;

import com.tokopedia.abstraction.base.view.fragment.TkpdBaseV4Fragment;
import com.tokopedia.applink.ApplinkConst;
import com.tokopedia.applink.RouteManager;

import com.tokopedia.design.bottomsheet.BottomSheetView;
import com.tokopedia.home.account.AccountConstants;

import com.tokopedia.home.account.R;
import com.tokopedia.home.account.analytics.AccountAnalytics;
import com.tokopedia.home.account.AccountHomeRouter;
import com.tokopedia.home.account.presentation.activity.TkpdPaySettingActivity;
import com.tokopedia.home.account.presentation.listener.AccountItemListener;
import com.tokopedia.home.account.presentation.view.SeeAllView;
import com.tokopedia.home.account.presentation.viewmodel.BuyerCardViewModel;
import com.tokopedia.home.account.presentation.viewmodel.InfoCardViewModel;
import com.tokopedia.home.account.presentation.viewmodel.MenuGridItemViewModel;
import com.tokopedia.home.account.presentation.viewmodel.MenuGridViewModel;
import com.tokopedia.home.account.presentation.viewmodel.MenuListViewModel;
import com.tokopedia.home.account.presentation.viewmodel.ShopCardViewModel;

import static com.tokopedia.home.account.AccountConstants.Analytics.AKUN_SAYA;
import static com.tokopedia.home.account.AccountConstants.Analytics.CLICK;
import static com.tokopedia.home.account.AccountConstants.Analytics.MY_COUPON;
import static com.tokopedia.home.account.AccountConstants.Analytics.PEMBELI;
import static com.tokopedia.home.account.AccountConstants.Analytics.PENJUAL;
import static com.tokopedia.home.account.AccountConstants.Analytics.PROFILE;
import static com.tokopedia.home.account.AccountConstants.Analytics.TOKOPOINTS;
import static com.tokopedia.home.account.AccountConstants.TOP_SELLER_APPLICATION_PACKAGE;

/**
 * @author okasurya on 7/26/18.
 */
public abstract class BaseAccountFragment extends TkpdBaseV4Fragment implements
        AccountItemListener {
    public static final String PARAM_USER_ID = "{user_id}";
    public static final String PARAM_SHOP_ID = "{shop_id}";

    private SeeAllView seeAllView;
    private AccountAnalytics accountAnalytics;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        accountAnalytics = new AccountAnalytics(getActivity());
    }

    protected void openApplink(String applink) {
        if (getContext() != null && !TextUtils.isEmpty(applink) && isApplink(applink)) {
            RouteManager.route(getContext(), applink);
        }
    }


    private boolean isApplink(String applink) {
        if (RouteManager.isSupportApplink(getContext(), applink)) {
            return true;
        } else if (applink.equals(AccountConstants.Navigation.SEE_ALL)) {
            seeAllView = new SeeAllView();
            seeAllView.setListener(this);
            seeAllView.show(getActivity().getSupportFragmentManager(), SeeAllView.class.getName());
        } else if (applink.equals(AccountConstants.Navigation.TOPADS)
                && getContext().getApplicationContext() instanceof AccountHomeRouter) {
            ((AccountHomeRouter) getContext().getApplicationContext()).gotoTopAdsDashboard
                    (getContext());
        } else if (applink.equals(AccountConstants.Navigation.TRAIN_ORDER_LIST)
                && getContext().getApplicationContext() instanceof AccountHomeRouter) {
            getActivity().startActivity(((AccountHomeRouter) getContext().getApplicationContext()).getTrainOrderListIntent
                    (getContext()));
        } else if (applink.equals(AccountConstants.Navigation.FEATURED_PRODUCT)
                && getContext().getApplicationContext() instanceof AccountHomeRouter) {
            Intent launchIntent = getContext().getPackageManager()
                    .getLaunchIntentForPackage(TOP_SELLER_APPLICATION_PACKAGE);
            if (launchIntent != null) {
                getContext().startActivity(launchIntent);
            } else if (getContext().getApplicationContext() instanceof AccountHomeRouter) {
                ((AccountHomeRouter) getContext().getApplicationContext()).goToCreateMerchantRedirect(getContext());
            }
        }

        return false;
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
        openApplink(ApplinkConst.COUPON_LISTING);
    }

    @Override
    public void onTokopediaPayLinkClicked() {
        if (getActivity() != null) {
            sendTracking(PEMBELI,
                    getString(R.string.title_tkpd_pay_setting),
                    getString(R.string.label_tokopedia_pay_see_all));
            getActivity().startActivity(new Intent(getContext(), TkpdPaySettingActivity.class));
        }
    }

    @Override
    public void onMenuGridItemClicked(MenuGridItemViewModel item) {
        sendTracking(item.getTitleTrack(), item.getSectionTrack(), item.getDescription());
        openApplink(item.getApplink());
    }

    @Override
    public void onMenuGridLinkClicked(MenuGridViewModel item) {
        sendTracking(item.getTitleTrack(), item.getSectionTrack(), item.getLinkText());
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
        if (element != null && !TextUtils.isEmpty(element.getShopId())) {
            openApplink(ApplinkConst.SHOP.replace(PARAM_SHOP_ID, element.getShopId()));
        }
    }

    @Override
    public void onShopNameClicked(ShopCardViewModel element) {
        if (element != null && !TextUtils.isEmpty(element.getShopId())) {
            openApplink(ApplinkConst.SHOP.replace(PARAM_SHOP_ID, element.getShopId()));
        }
    }

    @Override
    public void onAddProductClicked() {
        sendTracking(PENJUAL, getString(R.string.title_menu_product), getString(R.string
                .label_button_add_product));
        openApplink(ApplinkConst.PRODUCT_ADD);
    }

    @Override
    public void onTokopediaPayItemClicked(String label, String applink, String bsTitle,
                                          String bsBody, String bsBtnText, String bsRedUrl) {
        sendTracking(PEMBELI, getString(R.string.label_tokopedia_pay_title), label);

        if (applink != null && applink.startsWith("http")) {
            openApplink(String.format("%s?url=%s",
                    ApplinkConst.WEBVIEW,
                    applink));
        } else if (applink != null && applink.startsWith("tokopedia")) {
            openApplink(applink);
        } else {
            if (getContext() == null
                    || bsTitle == null
                    || bsTitle.trim().isEmpty()
                    || bsBody == null
                    || bsBody.trim().isEmpty()) {
                return;
            }

            BottomSheetView toolTip = new BottomSheetView(getContext());
            toolTip.renderBottomSheet(new BottomSheetView.BottomSheetField
                    .BottomSheetFieldBuilder()
                    .setTitle(bsTitle)
                    .setBody(bsBody)
                    .setCloseButton(bsBtnText == null || bsBtnText.trim().isEmpty() ? getString(R.string.error_no_password_no) : bsBtnText)
                    .build());

            if (URLUtil.isValidUrl(bsRedUrl)) {
                toolTip.setBtnCloseOnClick(dialogInterface -> {
                    toolTip.cancel();
                    openApplink(String.format("%s?url=%s",
                            ApplinkConst.WEBVIEW,
                            bsRedUrl));
                });
            }

            toolTip.show();
        }
    }

    @Override
    public void onDepositClicked(ShopCardViewModel element) {
        openApplink(ApplinkConst.DEPOSIT);
    }

    @Override
    public void onTopadsInfoClicked() {
        if (getContext().getApplicationContext() instanceof AccountHomeRouter) {
            ((AccountHomeRouter) getContext().getApplicationContext()).
                    gotoTopAdsDashboard(getContext());
        }
    }

    @Override
    public void onGMInfoClicked() {
        if (getContext().getApplicationContext() instanceof AccountHomeRouter) {
            ((AccountHomeRouter) getContext().getApplicationContext()).
                    goToGMSubscribe(getContext());
        }
    }

    @Override
    public void onSellerCenterInfoClicked() {
        openApplink(ApplinkConst.SELLER_CENTER);
    }

    @Override
    public void onOpenShopClicked() {
        if (getContext().getApplicationContext() instanceof AccountHomeRouter) {
            startActivity(((AccountHomeRouter) getContext().getApplicationContext()).
                    getIntentCreateShop(getContext()));
        }
    }

    @Override
    public void onLearnMoreSellerClicked() {
        openApplink(String.format("%s?url=%s",
                ApplinkConst.WEBVIEW,
                AccountConstants.Url.MORE_SELLER));
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
