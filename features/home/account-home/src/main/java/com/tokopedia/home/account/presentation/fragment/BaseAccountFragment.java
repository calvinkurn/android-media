package com.tokopedia.home.account.presentation.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.webkit.URLUtil;

import com.tokopedia.abstraction.base.view.fragment.TkpdBaseV4Fragment;
import com.tokopedia.analytics.TrackAnalytics;
import com.tokopedia.analytics.firebase.FirebaseEvent;
import com.tokopedia.applink.ApplinkConst;
import com.tokopedia.applink.ApplinkRouter;
import com.tokopedia.applink.RouteManager;
import com.tokopedia.design.bottomsheet.BottomSheetView;
import com.tokopedia.gm.resource.GMConstant;
import com.tokopedia.home.account.AccountConstants;
import com.tokopedia.home.account.AccountHomeRouter;
import com.tokopedia.home.account.AccountHomeUrl;
import com.tokopedia.home.account.R;
import com.tokopedia.home.account.analytics.AccountAnalytics;
import com.tokopedia.home.account.presentation.activity.TkpdPaySettingActivity;
import com.tokopedia.home.account.presentation.adapter.AccountTypeFactory;
import com.tokopedia.home.account.presentation.listener.AccountItemListener;
import com.tokopedia.home.account.presentation.util.AccountByMeHelper;
import com.tokopedia.home.account.presentation.view.SaldoInfoBottomSheet;
import com.tokopedia.home.account.presentation.view.SeeAllView;
import com.tokopedia.home.account.presentation.viewmodel.BuyerCardViewModel;
import com.tokopedia.home.account.presentation.viewmodel.InfoCardViewModel;
import com.tokopedia.home.account.presentation.viewmodel.MenuGridItemViewModel;
import com.tokopedia.home.account.presentation.viewmodel.MenuGridViewModel;
import com.tokopedia.home.account.presentation.viewmodel.MenuListViewModel;
import com.tokopedia.home.account.presentation.viewmodel.SellerSaldoViewModel;
import com.tokopedia.home.account.presentation.viewmodel.ShopCardViewModel;
import com.tokopedia.home.account.presentation.viewmodel.TokopediaPayBSModel;
import com.tokopedia.showcase.ShowCaseBuilder;
import com.tokopedia.showcase.ShowCaseDialog;
import com.tokopedia.topads.common.constant.TopAdsCommonConstant;
import com.tokopedia.user.session.UserSession;
import com.tokopedia.user_identification_common.KycCommonUrl;

import java.util.HashMap;

import static com.tokopedia.home.account.AccountConstants.Analytics.AKUN_SAYA;
import static com.tokopedia.home.account.AccountConstants.Analytics.BY_ME_CURATION;
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
    public static final String OVO = "OVO";

    public AccountTypeFactory accountTypeFactory;
    private SeeAllView seeAllView;
    private AccountAnalytics accountAnalytics;
    UserSession userSession;

    abstract void notifyItemChanged(int position);

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        accountAnalytics = new AccountAnalytics(getActivity());
        userSession = new UserSession(getContext());
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
    public void onByMeClicked() {
        sendTracking(PEMBELI, BY_ME_CURATION, "", true);
        openApplink(ApplinkConst.AFFILIATE_EXPLORE);
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
    public void onInfoCardClicked(InfoCardViewModel item, int adapterPosition) {

        if (getContext() != null && getContext().getString(R.string.title_menu_loan)
                .equalsIgnoreCase(item.getMainText())) {
            TrackAnalytics.sendEvent(FirebaseEvent.SellerHome.HOMEPAGE_AKUN_PENJUAL_CLICK,
                    new HashMap<>(), getContext());
        }

        if (getContext() != null
                && item.getMainText().equals(
                getContext().getResources().getString(R.string.title_menu_affiliate))) {
            item.setIconRes(R.drawable.ic_byme_card);
            notifyItemChanged(adapterPosition);
            AccountByMeHelper.setFirstTimeByme(getContext());
            sendTracking(
                    item.getTitleTrack(),
                    item.getSectionTrack(),
                    !TextUtils.isEmpty(item.getItemTrack()) ? item.getItemTrack() : item.getMainText(),
                    true
            );
        } else {
            sendTracking(
                    item.getTitleTrack(),
                    item.getSectionTrack(),
                    !TextUtils.isEmpty(item.getItemTrack()) ? item.getItemTrack() : item.getMainText()
            );
        }
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
    public void onTokopediaPayLeftItemClicked(String label, String applink, TokopediaPayBSModel bsData,
                                              boolean isLinked, String walletType) {
        sendTracking(PEMBELI, getString(R.string.label_tokopedia_pay_title), label);

        if (walletType.equals(OVO) && !isLinked) {
            sendTrackingOvoActivation();
        }

        if (applink != null && applink.startsWith("http")) {
            openApplink(String.format("%s?url=%s",
                    ApplinkConst.WEBVIEW,
                    applink));
        } else if (applink != null && applink.startsWith("tokopedia")) {
            openApplink(applink);
        }
    }

    @Override
    public void onTokopediaPayRightItemClicked(boolean isRightSaldo, String label, String vccStatus, String applink, TokopediaPayBSModel bsData) {

        sendTracking(PEMBELI, getString(R.string.label_tokopedia_pay_title), label);
        if (bsData != null) {
            sendOVOTracking(AccountConstants.Analytics.OVO_PAY_LATER_CATEGORY,
                    AccountConstants.Analytics.OVO_PAY_ICON_CLICK,
                    String.format(AccountConstants.Analytics.OVO_PAY_LATER_LABEL, vccStatus));
        }
        if (applink != null && applink.startsWith("http")) {
            openApplink(String.format("%s?url=%s",
                    ApplinkConst.WEBVIEW,
                    applink));
        } else if (applink != null && applink.startsWith("tokopedia")) {
            if (isRightSaldo) {
                openSladoPage(applink);
            } else {
                openApplink(applink);
            }
        } else {
            if (getContext() == null
                    || bsData == null
                    || bsData.isInvalid()) {
                return;
            }

            BottomSheetView toolTip = new BottomSheetView(getContext());
            toolTip.renderBottomSheet(new BottomSheetView.BottomSheetField
                    .BottomSheetFieldBuilder()
                    .setTitle(bsData.getTitle())
                    .setBody(bsData.getBody())
                    .setCloseButton(bsData.getButtonText() == null || bsData.getButtonText().trim().isEmpty() ? getString(R.string.error_no_password_no) : bsData.getButtonText())
                    .build());

            if (URLUtil.isValidUrl(bsData.getButtonRedirectionUrl())) {
                toolTip.setBtnCloseOnClick(dialogInterface -> {
                    toolTip.cancel();
                    openApplink(String.format("%s?url=%s",
                            ApplinkConst.WEBVIEW,
                            bsData.getButtonRedirectionUrl()));
                });
            }

            toolTip.show();
        }
    }

    private void openSladoPage(String applink) {
        if (userSession.hasShownSaldoIntroScreen()) {
            openApplink(applink);
        } else {
            userSession.setSaldoIntroPageStatus(true);
            openApplink(ApplinkConst.SALDO_INTRO);
        }
    }

    @Override
    public void onDepositClicked(SellerSaldoViewModel element) {
        // TODO: 24/1/19 update deeplink for seller saldo tab
        openSladoPage(ApplinkConst.DEPOSIT);
    }

    @Override
    public void onTopadsInfoClicked() {
        if (getContext().getApplicationContext() instanceof AccountHomeRouter) {
            openApplink(String.format("%s?url=%s", ApplinkConst.WEBVIEW, TopAdsCommonConstant.TOPADS_SELLER_CENTER));
        }
    }

    @Override
    public void onGMInfoClicked() {
        if (getContext().getApplicationContext() instanceof AccountHomeRouter) {
            openApplink(String.format("%s?url=%s", ApplinkConst.WEBVIEW, GMConstant.getGMEduUrl(getContext())));
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
                AccountHomeUrl.MORE_SELLER));
    }

    @Override
    public void onKycLinkClicked(int verificationStatus) {
        if (getActivity() != null && getActivity().getApplicationContext() instanceof ApplinkRouter) {
            accountAnalytics.eventClickKYCSellerAccountPage(verificationStatus);
            ApplinkRouter applinkRouter = ((ApplinkRouter) getActivity().getApplicationContext());
            applinkRouter.goToApplinkActivity(getActivity(), ApplinkConst.KYC_SELLER_DASHBOARD);
        }
    }

    @Override
    public void onTickerLinkClicked(String url) {
        if (getActivity() != null
                && getActivity().getApplicationContext() instanceof ApplinkRouter) {
            ApplinkRouter applinkRouter = ((ApplinkRouter) getActivity().getApplicationContext());
            applinkRouter.goToApplinkActivity(getActivity(),
                    String.format("%s?url=%s", ApplinkConst.WEBVIEW, url));
        }
    }

    private void sendOVOTracking(String title, String section, String item) {
        if (accountAnalytics == null)
            return;

        if (title == null || section == null || item == null)
            return;

        accountAnalytics.eventClickOVOPayLater(
                title.toLowerCase(),
                section.toLowerCase(),
                item.toLowerCase());
    }

    private void sendTracking(String title, String section, String item) {
        sendTracking(title, section, item, false);
    }

    private void sendTracking(String title, String section, String item, boolean withUserId) {
        if (accountAnalytics == null)
            return;

        if (title == null || section == null || item == null)
            return;

        accountAnalytics.eventClickAccount(
                title.toLowerCase(),
                section.toLowerCase(),
                item.toLowerCase(),
                withUserId
        );
    }

    private void sendTrackingOvoActivation() {
        if (accountAnalytics == null)
            return;

        accountAnalytics.eventClickActivationOvoMyAccount();
    }

    @Override
    public void onTickerClosed() {

    }

    @Override
    public void onTopAdsMenuClicked() {
        if (getContext().getApplicationContext() instanceof AccountHomeRouter) {
            ((AccountHomeRouter) getContext().getApplicationContext()).
                    gotoTopAdsDashboard(getContext());
        }
    }

    @Override
    public void onShopStatusInfoButtonClicked() {
        RouteManager.route(getActivity(), KycCommonUrl.APPLINK_TERMS_AND_CONDITION);
    }

    @SuppressLint("PrivateResource")
    public ShowCaseDialog createShowCaseDialog() {
        return new ShowCaseBuilder()
                .customView(R.layout.show_case_saldo)
                .titleTextColorRes(R.color.white)
                .spacingRes(R.dimen.dp_12)
                .arrowWidth(R.dimen.dp_16)
                .textColorRes(R.color.grey_400)
                .shadowColorRes(R.color.shadow)
                .backgroundContentColorRes(R.color.black)
                .circleIndicatorBackgroundDrawableRes(R.drawable.selector_circle_green)
                .textSizeRes(R.dimen.sp_12)
                .finishStringRes(R.string.intro_seller_saldo_finish_string)
                .useCircleIndicator(true)
                .clickable(true)
                .useArrow(true)
                .useSkipWord(false)
                .build();
    }

    @Override
    public void onSaldoInfoIconClicked(boolean isSeller) {
        if (getContext() == null) {
            return;
        }
        SaldoInfoBottomSheet saldoInfoBottomSheet =
                new SaldoInfoBottomSheet(getContext());

        if (isSeller) {
            saldoInfoBottomSheet.setBody(getResources().getString(R.string.seller_saldo_on_boarding_desc));
            saldoInfoBottomSheet.setTitle(getResources().getString(R.string.seller_saldo_on_boarding_title));
        } else {
            saldoInfoBottomSheet.setBody(getResources().getString(R.string.buyer_saldo_on_boarding_desc));
            saldoInfoBottomSheet.setTitle(getResources().getString(R.string.buyer_saldo_on_boarding_title));
        }

        saldoInfoBottomSheet.setButtonText(getResources().getString(R.string.saldo_info_bottom_sheet_btn));
        saldoInfoBottomSheet.show();
    }
}
