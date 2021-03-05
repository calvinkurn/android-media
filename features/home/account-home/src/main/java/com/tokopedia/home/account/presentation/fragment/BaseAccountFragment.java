package com.tokopedia.home.account.presentation.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.webkit.URLUtil;

import androidx.annotation.Nullable;

import com.tokopedia.abstraction.base.view.fragment.TkpdBaseV4Fragment;
import com.tokopedia.affiliatecommon.data.util.AffiliatePreference;
import com.tokopedia.applink.ApplinkConst;
import com.tokopedia.applink.ApplinkRouter;
import com.tokopedia.applink.RouteManager;
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal;
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace;
import com.tokopedia.applink.internal.ApplinkConstInternalTopAds;
import com.tokopedia.applink.internal.ApplinkConstInternalTravel;
import com.tokopedia.design.bottomsheet.BottomSheetView;
import com.tokopedia.home.account.AccountConstants;
import com.tokopedia.home.account.AccountHomeUrl;
import com.tokopedia.home.account.R;
import com.tokopedia.home.account.analytics.AccountAnalytics;
import com.tokopedia.home.account.constant.SettingConstant;
import com.tokopedia.home.account.presentation.activity.TkpdPaySettingActivity;
import com.tokopedia.home.account.presentation.listener.AccountItemListener;
import com.tokopedia.home.account.presentation.util.AccountByMeHelper;
import com.tokopedia.home.account.presentation.view.SeeAllView;
import com.tokopedia.home.account.presentation.viewmodel.BuyerCardViewModel;
import com.tokopedia.home.account.presentation.viewmodel.InfoCardViewModel;
import com.tokopedia.home.account.presentation.viewmodel.MenuGridIconNotificationItemViewModel;
import com.tokopedia.home.account.presentation.viewmodel.MenuGridIconNotificationViewModel;
import com.tokopedia.home.account.presentation.viewmodel.MenuGridItemViewModel;
import com.tokopedia.home.account.presentation.viewmodel.MenuGridViewModel;
import com.tokopedia.home.account.presentation.viewmodel.MenuListViewModel;
import com.tokopedia.home.account.presentation.viewmodel.TokopediaPayBSModel;
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem;
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl;
import com.tokopedia.remoteconfig.RemoteConfig;
import com.tokopedia.topads.common.constant.TopAdsCommonConstant;
import com.tokopedia.trackingoptimizer.TrackingQueue;
import com.tokopedia.user.session.UserSession;
import com.tokopedia.user_identification_common.KycCommonUrl;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

import static com.tokopedia.affiliatecommon.AffiliateCommonConstantKt.DISCOVERY_BY_ME;
import static com.tokopedia.home.account.AccountConstants.Analytics.AKUN_SAYA;
import static com.tokopedia.home.account.AccountConstants.Analytics.BY_ME_CURATION;
import static com.tokopedia.home.account.AccountConstants.Analytics.CLICK;
import static com.tokopedia.home.account.AccountConstants.Analytics.EVENT_CATEGORY_AKUN_PEMBELI;
import static com.tokopedia.home.account.AccountConstants.Analytics.PEMBELI;
import static com.tokopedia.home.account.AccountConstants.Analytics.PROFILE;
import static com.tokopedia.home.account.AccountConstants.TITLE_UOH_ETICKET;
import static com.tokopedia.home.account.constant.SettingConstant.POWER_MERCHANT_URL;
import static com.tokopedia.home.account.constant.SettingConstant.SELLER_CENTER_URL;
import static com.tokopedia.remoteconfig.RemoteConfigKey.APP_ENABLE_SALDO_SPLIT;

/**
 * @author okasurya on 7/26/18.
 */
public abstract class BaseAccountFragment extends TkpdBaseV4Fragment implements AccountItemListener {

    public static final String PARAM_USER_ID = "{user_id}";
    public static final String PARAM_SHOP_ID = "{shop_id}";
    public static final int OPEN_SHOP_SUCCESS = 100;
    public static final int REQUEST_PHONE_VERIFICATION = 123;
    public static final String OVO = "OVO";
    private static final String TOKOPEDIA_TITLE = "Tokopedia";
    private boolean mShowTokopointNative = false;

    private AccountAnalytics accountAnalytics;
    UserSession userSession;
    private AffiliatePreference affiliatePreference;
    private TrackingQueue trackingQueue;
    private RemoteConfig remoteConfig;

    abstract void notifyItemChanged(int position);

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        accountAnalytics = new AccountAnalytics(getActivity());
        userSession = new UserSession(getContext());
        affiliatePreference = new AffiliatePreference(getContext());
        trackingQueue = new TrackingQueue(getActivity());
        remoteConfig = new FirebaseRemoteConfigImpl(getContext());
    }

    @Override
    public void onPause() {
        super.onPause();
        trackingQueue.sendAll();
    }

    protected void openApplink(String applink) {
        if (getContext() != null && !TextUtils.isEmpty(applink) && isApplink(applink)) {
            RouteManager.route(getContext(), applink);
        }
    }

    protected void openWebview(String url) {
        RouteManager.route(
                getActivity(),
                String.format("%s?url=%s", ApplinkConst.WEBVIEW, url)
        );
    }

    private boolean isApplink(String applink) {
        if (RouteManager.isSupportApplink(getContext(), applink)) {
            return true;
        } else if (applink.equals(AccountConstants.Navigation.SEE_ALL)) {
            SeeAllView seeAllView = new SeeAllView();
            seeAllView.setListener(this);
            seeAllView.show(getActivity().getSupportFragmentManager(), SeeAllView.class.getName());
        } else if (applink.equals(AccountConstants.Navigation.TOPADS)) {
            RouteManager.route(getContext(), ApplinkConstInternalTopAds.TOPADS_DASHBOARD_CUSTOMER);
        } else if (applink.equals(AccountConstants.Navigation.TRAIN_ORDER_LIST)) {
            goToTrainOrderListIntent();
        } else if (applink.equals(SettingConstant.RESCENTER_BUYER) || applink.equals(SettingConstant.RESCENTER_SELLER)) {
            return true;
        }

        return false;
    }

    @Override
    public void onProfileClicked(BuyerCardViewModel element) {
        sendTracking(PEMBELI, AKUN_SAYA, String.format("%s %s", CLICK, PROFILE));
        RouteManager.route(getContext(), ApplinkConstInternalGlobal.GENERAL_SETTING);
    }

    @Override
    public void onProfileCompletionClicked(BuyerCardViewModel element) {
        openApplink(ApplinkConst.PROFILE_COMPLETION);
    }

    @Override
    public void onBuyerTokopointClicked(String element, String title) {
        accountAnalytics.eventAccountPromoClick(title);
        RouteManager.route(getContext(), element);
    }

    @Override
    public void onBuyerVoucherClicked(String element, String title) {
        accountAnalytics.eventAccountPromoClick(title);
        openApplink(element);
    }

    @Override
    public void onByMeClicked() {
        if (affiliatePreference.isFirstTimeEducation(userSession.getUserId())) {

            Intent intent = RouteManager.getIntent(
                    getContext(),
                    ApplinkConst.DISCOVERY_PAGE.replace("{page_id}", DISCOVERY_BY_ME)
            );
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            startActivity(intent);
            affiliatePreference.setFirstTimeEducation(userSession.getUserId());

        } else {
            sendTracking(PEMBELI, BY_ME_CURATION, "", true);
            RouteManager.route(getContext(), ApplinkConst.AFFILIATE_DEFAULT_CREATE_POST);
        }
    }

    @Override
    public void onClickMemberDetail() {
        //Fallback strategy for new Rewards Page
        mShowTokopointNative = remoteConfig.getBoolean(ApplinkConst.RewardFallback.RemoteConfig.APP_SHOW_TOKOPOINT_NATIVE, false);
        if (mShowTokopointNative) {
            RouteManager.route(getContext(), ApplinkConst.TokoPoints.HOMEPAGE);
        } else {
            RouteManager.route(getContext(), String.format("%s?title=%s&url=%s", ApplinkConst.WEBVIEW, TOKOPEDIA_TITLE,
                    ApplinkConst.RewardFallback.RewardWebview.REWARD_WEBVIEW));
        }
        accountAnalytics.eventAccountPromoRewardClick();
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
    public void onMenuGridBackgroundItemClicked(@NotNull MenuGridIconNotificationItemViewModel item) {
        if (item.getDescription().equalsIgnoreCase(AccountConstants.TITLE_UOH_MENUNGGU_PEMBAYARAN)) {
            accountAnalytics.clickMenungguPembayaranUoh(userSession.getUserId());
        } else if (item.getDescription().equalsIgnoreCase(AccountConstants.TITLE_UOH_DALAM_PROSES)) {
            accountAnalytics.clickDalamProsesUoh(userSession.getUserId());
        } else if (item.getDescription().equalsIgnoreCase(AccountConstants.TITLE_UOH_SEMUA_TRANSAKSI)) {
            accountAnalytics.clickSemuaTransaksiUoh(userSession.getUserId());
        }
        openApplink(item.getApplink());
    }

    @Override
    public void onMenuGridLinkClicked(MenuGridViewModel item) {
        sendTracking(item.getTitleTrack(), item.getSectionTrack(), item.getLinkText());
        openApplink(item.getApplinkUrl());
    }

    @Override
    public void onMenuGridBackgroundLinkClicked(@NotNull MenuGridIconNotificationViewModel item) {
        sendTracking(item.getTitleTrack(), item.getSectionTrack(), item.getLinkText());
        openApplink(item.getApplinkUrl());
    }

    @Override
    public void onInfoCardClicked(InfoCardViewModel item, int adapterPosition) {

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
        if (item.getMenu().equalsIgnoreCase(TITLE_UOH_ETICKET)) {
            accountAnalytics.clickEticketEvoucherUoh(userSession.getUserId());
        } else {
            sendTracking(item.getTitleTrack(), item.getSectionTrack(), item.getMenu());
        }
        openApplink(item.getApplink());
    }

    @Override
    public void onTokopediaPayLeftItemClicked(String label, String applink, TokopediaPayBSModel bsData,
                                              boolean isLinked, String walletType) {

        sendTracking(AccountConstants.Analytics.CLICK_ACCOUNT, EVENT_CATEGORY_AKUN_PEMBELI, "click tokopedia pay ovo", "");

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
    public void onTokopediaPayCentreItemClicked(@NotNull String label, @NotNull String applink,
                                                @NotNull TokopediaPayBSModel bsData, boolean isLinked,
                                                @NotNull String walletType) {

        sendTracking(AccountConstants.Analytics.CLICK_ACCOUNT, EVENT_CATEGORY_AKUN_PEMBELI, "click tokopedia pay ovo paylater", "");

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

        sendTracking(AccountConstants.Analytics.CLICK_ACCOUNT, EVENT_CATEGORY_AKUN_PEMBELI, "click tokopedia pay saldo", "");
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
        RemoteConfig remoteConfig = new FirebaseRemoteConfigImpl(getContext());

        if (remoteConfig.getBoolean(APP_ENABLE_SALDO_SPLIT, false)) {
            if (userSession.hasShownSaldoIntroScreen()) {
                openApplink(applink);
            } else {
                userSession.setSaldoIntroPageStatus(true);
                openApplink(ApplinkConstInternalGlobal.SALDO_INTRO);
            }
        } else {
            RouteManager.route(getContext(), String.format("%s?url=%s", ApplinkConst.WEBVIEW,
                    ApplinkConst.WebViewUrl.SALDO_DETAIL));
        }
    }

    @Override
    public void onTopadsInfoClicked() {
        openApplink(String.format("%s?url=%s", ApplinkConst.WEBVIEW, TopAdsCommonConstant.TOPADS_SELLER_CENTER));
    }

    @Override
    public void onGMInfoClicked() {
        openApplink(String.format("%s?url=%s", ApplinkConst.WEBVIEW, POWER_MERCHANT_URL));
    }

    @Override
    public void onSellerCenterInfoClicked() {
        openApplink(String.format("%s?url=%s", ApplinkConst.WEBVIEW, SELLER_CENTER_URL));
    }

    @Override
    public void onOpenShopClicked() {
        moveToCreateShop();
    }

    protected void moveToCreateShop() {
        startActivityForResult(RouteManager.getIntent(getContext(), ApplinkConst.CREATE_SHOP), OPEN_SHOP_SUCCESS);
    }

    @Override
    public void onLearnMoreSellerClicked() {
        openApplink(String.format("%s?url=%s",
                ApplinkConst.WEBVIEW,
                AccountHomeUrl.EDU_MORE_SELLER));
    }

    @Override
    public void onKycLinkClicked(int verificationStatus) {
        if (getActivity() != null && getActivity().getApplicationContext() instanceof ApplinkRouter) {
            accountAnalytics.eventClickKYCSellerAccountPage(verificationStatus);
            Intent intent = RouteManager.getIntent(getActivity(), ApplinkConst.KYC);
            intent.putExtra(ApplinkConstInternalGlobal.PARAM_SOURCE, ApplinkConstInternalGlobal.PARAM_SOURCE_KYC_SELLER);
            startActivity(intent);
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

    private void trackClickPowerMerchantSetting() {
        accountAnalytics.eventClickPowerMerchantSetting();
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

    protected void sendTracking(String title, String section, String item) {
        sendTracking(title, section, item, false);
    }

    private void sendTracking(String event, String category, String action, String label) {
        if (accountAnalytics == null)
            return;

        if (event == null || category == null || action == null || label == null)
            return;

        accountAnalytics.eventTokopediaPayClick(
                event.toLowerCase(),
                category.toLowerCase(),
                action.toLowerCase(),
                label
        );
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

    private void goToTrainOrderListIntent() {
        RouteManager.route(getContext(), ApplinkConstInternalTravel.TRAIN_ORDER_LIST);
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
        if (getContext() != null) {
            RouteManager.route(getContext(), ApplinkConstInternalTopAds.TOPADS_DASHBOARD_CUSTOMER);
        }
    }

    @Override
    public void onShopStatusInfoButtonClicked() {
        RouteManager.route(getActivity(), KycCommonUrl.APPLINK_TERMS_AND_CONDITION);
    }

    public void sendProductImpressionTracking(TrackingQueue trackingQueue,
                                              RecommendationItem recommendationItem,
                                              int position) {
        accountAnalytics.eventAccountProductView(trackingQueue, recommendationItem, position);
    }

    public void sendProductClickTracking(RecommendationItem recommendationItem,
                                         int position,
                                         String widgetTitle) {
        accountAnalytics.eventAccountProductClick(recommendationItem, position, widgetTitle);
    }

    public void sendProductWishlistClickTracking(boolean wishlistStatus) {
        accountAnalytics.eventClickWishlistButton(wishlistStatus);
    }

    @Override
    public void onPowerMerchantSettingClicked() {
        trackClickPowerMerchantSetting();
        RouteManager.route(getActivity(), ApplinkConstInternalMarketplace.POWER_MERCHANT_SUBSCRIBE);
    }

    @Override
    public void onTokomemberClicked(String url, String title) {
        accountAnalytics.eventAccountPromoClick(title);
        RouteManager.route(getContext(), url);
    }

    @NotNull
    @Override
    public TrackingQueue getTrackingQueue() {
        return trackingQueue;
    }

    @Override
    public void onAccountItemImpression(@NotNull HashMap<String, Object> data) {
        trackingQueue.putEETracking(data);
    }

    @NotNull
    @Override
    public RemoteConfig getRemoteConfig() {
        return remoteConfig;
    }
}
