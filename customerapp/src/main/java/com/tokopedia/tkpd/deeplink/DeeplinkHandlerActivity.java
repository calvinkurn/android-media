package com.tokopedia.tkpd.deeplink;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.AppCompatActivity;

import com.airbnb.deeplinkdispatch.DeepLink;
import com.airbnb.deeplinkdispatch.DeepLinkHandler;
import com.appsflyer.AppsFlyerLib;
import com.tokopedia.affiliate.applink.AffiliateApplinkModule;
import com.tokopedia.affiliate.applink.AffiliateApplinkModuleLoader;
import com.tokopedia.applink.ApplinkDelegate;
import com.tokopedia.applink.ApplinkRouter;
import com.tokopedia.applink.SessionApplinkModule;
import com.tokopedia.applink.SessionApplinkModuleLoader;
import com.tokopedia.applink.TkpdApplinkDelegate;
import com.tokopedia.browse.common.applink.DigitalBrowseApplinkModule;
import com.tokopedia.browse.common.applink.DigitalBrowseApplinkModuleLoader;
import com.tokopedia.challenges.deeplinkmodule.ChallengesDeepLinkModule;
import com.tokopedia.challenges.deeplinkmodule.ChallengesDeepLinkModuleLoader;
import com.tokopedia.changepassword.common.applink.ChangePasswordDeeplinkModule;
import com.tokopedia.changepassword.common.applink.ChangePasswordDeeplinkModuleLoader;
import com.tokopedia.checkout.applink.CheckoutAppLinkModule;
import com.tokopedia.checkout.applink.CheckoutAppLinkModuleLoader;
import com.tokopedia.contact_us.applink.CustomerCareApplinkModule;
import com.tokopedia.contact_us.applink.CustomerCareApplinkModuleLoader;
import com.tokopedia.core.analytics.TrackingUtils;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.app.TkpdCoreRouter;
import com.tokopedia.core.deeplink.CoreDeeplinkModule;
import com.tokopedia.core.deeplink.CoreDeeplinkModuleLoader;
import com.tokopedia.core.gcm.Constants;
import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.router.home.HomeRouter;
import com.tokopedia.core.util.CacheUtil;
import com.tokopedia.core.util.GlobalConfig;
import com.tokopedia.developer_options.presentation.applink.RNDevOptionsApplinkModule;
import com.tokopedia.developer_options.presentation.applink.RNDevOptionsApplinkModuleLoader;
import com.tokopedia.digital.applink.DigitalApplinkModule;
import com.tokopedia.digital.applink.DigitalApplinkModuleLoader;
import com.tokopedia.digital_deals.deeplinkmodule.DealsDeepLinkModule;
import com.tokopedia.digital_deals.deeplinkmodule.DealsDeepLinkModuleLoader;
import com.tokopedia.discovery.applink.DiscoveryApplinkModule;
import com.tokopedia.discovery.applink.DiscoveryApplinkModuleLoader;
import com.tokopedia.events.deeplink.EventsDeepLinkModule;
import com.tokopedia.events.deeplink.EventsDeepLinkModuleLoader;
import com.tokopedia.explore.applink.ExploreApplinkModule;
import com.tokopedia.explore.applink.ExploreApplinkModuleLoader;
import com.tokopedia.feedplus.view.deeplink.FeedDeeplinkModule;
import com.tokopedia.feedplus.view.deeplink.FeedDeeplinkModuleLoader;
import com.tokopedia.flight.applink.FlightApplinkModule;
import com.tokopedia.flight.applink.FlightApplinkModuleLoader;
import com.tokopedia.gamification.applink.GamificationApplinkModule;
import com.tokopedia.gamification.applink.GamificationApplinkModuleLoader;
import com.tokopedia.groupchat.common.applink.GroupChatApplinkModule;
import com.tokopedia.groupchat.common.applink.GroupChatApplinkModuleLoader;
import com.tokopedia.home.account.applink.AccountHomeApplinkModule;
import com.tokopedia.home.account.applink.AccountHomeApplinkModuleLoader;
import com.tokopedia.home.applink.HomeApplinkModule;
import com.tokopedia.home.applink.HomeApplinkModuleLoader;
import com.tokopedia.homecredit.applink.HomeCreditAppLinkModule;
import com.tokopedia.homecredit.applink.HomeCreditAppLinkModuleLoader;
import com.tokopedia.inbox.deeplink.InboxDeeplinkModule;
import com.tokopedia.inbox.deeplink.InboxDeeplinkModuleLoader;
import com.tokopedia.instantloan.deeplink.InstantLoanAppLinkModule;
import com.tokopedia.instantloan.deeplink.InstantLoanAppLinkModuleLoader;
import com.tokopedia.interestpick.applink.InterestPickApplinkModule;
import com.tokopedia.interestpick.applink.InterestPickApplinkModuleLoader;
import com.tokopedia.kol.applink.KolApplinkModule;
import com.tokopedia.kol.applink.KolApplinkModuleLoader;
import com.tokopedia.linker.LinkerManager;
import com.tokopedia.linker.LinkerUtils;
import com.tokopedia.linker.interfaces.DefferedDeeplinkCallback;
import com.tokopedia.linker.model.LinkerDeeplinkData;
import com.tokopedia.linker.model.LinkerDeeplinkResult;
import com.tokopedia.linker.model.LinkerError;
import com.tokopedia.loginregister.common.applink.LoginRegisterApplinkModule;
import com.tokopedia.loginregister.common.applink.LoginRegisterApplinkModuleLoader;
import com.tokopedia.loyalty.applink.LoyaltyAppLinkModule;
import com.tokopedia.loyalty.applink.LoyaltyAppLinkModuleLoader;
import com.tokopedia.navigation.applink.HomeNavigationApplinkModule;
import com.tokopedia.navigation.applink.HomeNavigationApplinkModuleLoader;
import com.tokopedia.notifcenter.applink.NotifCenterApplinkModule;
import com.tokopedia.notifcenter.applink.NotifCenterApplinkModuleLoader;
import com.tokopedia.phoneverification.applink.PhoneVerificationApplinkModule;
import com.tokopedia.phoneverification.applink.PhoneVerificationApplinkModuleLoader;
import com.tokopedia.pms.howtopay.HowtopayApplinkModule;
import com.tokopedia.pms.howtopay.HowtopayApplinkModuleLoader;
import com.tokopedia.product.manage.item.utils.ProductAddDeeplinkModule;
import com.tokopedia.product.manage.item.utils.ProductAddDeeplinkModuleLoader;
import com.tokopedia.product.manage.list.applink.ProductManageApplinkModule;
import com.tokopedia.product.manage.list.applink.ProductManageApplinkModuleLoader;
import com.tokopedia.profile.applink.ProfileApplinkModule;
import com.tokopedia.profile.applink.ProfileApplinkModuleLoader;
import com.tokopedia.pushnotif.Constant;
import com.tokopedia.pushnotif.HistoryNotification;
import com.tokopedia.recentview.view.applink.RecentViewApplinkModule;
import com.tokopedia.recentview.view.applink.RecentViewApplinkModuleLoader;
import com.tokopedia.referral.deeplink.ReferralDeeplinkModule;
import com.tokopedia.referral.deeplink.ReferralDeeplinkModuleLoader;
import com.tokopedia.saldodetails.applink.SaldoDetailsAppLinkModule;
import com.tokopedia.saldodetails.applink.SaldoDetailsAppLinkModuleLoader;
import com.tokopedia.seller.applink.SellerApplinkModule;
import com.tokopedia.seller.applink.SellerApplinkModuleLoader;
import com.tokopedia.settingbank.applink.SettingBankApplinkModule;
import com.tokopedia.settingbank.applink.SettingBankApplinkModuleLoader;
import com.tokopedia.shop.applink.ShopAppLinkModule;
import com.tokopedia.shop.applink.ShopAppLinkModuleLoader;
import com.tokopedia.talk.common.applink.InboxTalkApplinkModule;
import com.tokopedia.talk.common.applink.InboxTalkApplinkModuleLoader;
import com.tokopedia.tkpd.deeplink.presenter.DeepLinkAnalyticsImpl;
import com.tokopedia.tkpd.redirect.RedirectCreateShopActivity;
import com.tokopedia.tkpd.tkpdreputation.applink.ReputationApplinkModule;
import com.tokopedia.tkpd.tkpdreputation.applink.ReputationApplinkModuleLoader;
import com.tokopedia.tkpdpdp.applink.PdpApplinkModule;
import com.tokopedia.tkpdpdp.applink.PdpApplinkModuleLoader;
import com.tokopedia.tokocash.applink.TokoCashApplinkModule;
import com.tokopedia.tokocash.applink.TokoCashApplinkModuleLoader;
import com.tokopedia.tokopoints.TokopointApplinkModule;
import com.tokopedia.tokopoints.TokopointApplinkModuleLoader;
import com.tokopedia.topchat.deeplink.TopChatAppLinkModule;
import com.tokopedia.topchat.deeplink.TopChatAppLinkModuleLoader;
import com.tokopedia.tracking.applink.TrackingAppLinkModule;
import com.tokopedia.tracking.applink.TrackingAppLinkModuleLoader;
import com.tokopedia.train.applink.TrainApplinkModule;
import com.tokopedia.train.applink.TrainApplinkModuleLoader;
import com.tokopedia.transaction.applink.TransactionApplinkModule;
import com.tokopedia.transaction.applink.TransactionApplinkModuleLoader;
import com.tokopedia.updateinactivephone.applink.ChangeInactivePhoneApplinkModule;
import com.tokopedia.updateinactivephone.applink.ChangeInactivePhoneApplinkModuleLoader;
import com.tokopedia.useridentification.applink.UserIdentificationApplinkModule;
import com.tokopedia.useridentification.applink.UserIdentificationApplinkModuleLoader;

import org.json.JSONObject;

@DeepLinkHandler({
        ConsumerDeeplinkModule.class,
        ProductAddDeeplinkModule.class,
        CoreDeeplinkModule.class,
        InboxDeeplinkModule.class,
        ReferralDeeplinkModule.class,
        SellerApplinkModule.class,
        TransactionApplinkModule.class,
        DigitalApplinkModule.class,
        PdpApplinkModule.class,
        HomeApplinkModule.class,
        DiscoveryApplinkModule.class,
        SessionApplinkModule.class,
        FeedDeeplinkModule.class,
        FlightApplinkModule.class,
        TrainApplinkModule.class,
        DigitalBrowseApplinkModule.class,
        ReputationApplinkModule.class,
        TokoCashApplinkModule.class,
        EventsDeepLinkModule.class,
        LoyaltyAppLinkModule.class,
        DealsDeepLinkModule.class,
        ShopAppLinkModule.class,
        GroupChatApplinkModule.class,
        GamificationApplinkModule.class,
        ProfileApplinkModule.class,
        KolApplinkModule.class,
        ExploreApplinkModule.class,
        InterestPickApplinkModule.class,
        TrackingAppLinkModule.class,
        CheckoutAppLinkModule.class,
        HowtopayApplinkModule.class,
        CustomerCareApplinkModule.class,
        TopChatAppLinkModule.class,
        TokopointApplinkModule.class,
        NotifCenterApplinkModule.class,
        HomeNavigationApplinkModule.class,
        AccountHomeApplinkModule.class,
        InstantLoanAppLinkModule.class,
        RecentViewApplinkModule.class,
        ChangePasswordDeeplinkModule.class,
        AffiliateApplinkModule.class,
        SettingBankApplinkModule.class,
        ChallengesDeepLinkModule.class,
        InboxTalkApplinkModule.class,
        ProductManageApplinkModule.class,
        LoginRegisterApplinkModule.class,
        ChangeInactivePhoneApplinkModule.class,
        PhoneVerificationApplinkModule.class,
        RNDevOptionsApplinkModule.class,
        UserIdentificationApplinkModule.class,
        SaldoDetailsAppLinkModule.class,
        HomeCreditAppLinkModule.class
})

public class DeeplinkHandlerActivity extends AppCompatActivity implements DefferedDeeplinkCallback {

    private static ApplinkDelegate applinkDelegate;

    public static ApplinkDelegate getApplinkDelegateInstance() {
        if (applinkDelegate == null) {
            applinkDelegate = new TkpdApplinkDelegate(
                    new ConsumerDeeplinkModuleLoader(),
                    new ProductAddDeeplinkModuleLoader(),
                    new CoreDeeplinkModuleLoader(),
                    new InboxDeeplinkModuleLoader(),
                    new ReferralDeeplinkModuleLoader(),
                    new SellerApplinkModuleLoader(),
                    new TransactionApplinkModuleLoader(),
                    new DigitalApplinkModuleLoader(),
                    new PdpApplinkModuleLoader(),
                    new HomeApplinkModuleLoader(),
                    new DiscoveryApplinkModuleLoader(),
                    new SessionApplinkModuleLoader(),
                    new FeedDeeplinkModuleLoader(),
                    new FlightApplinkModuleLoader(),
                    new TrainApplinkModuleLoader(),
                    new DigitalBrowseApplinkModuleLoader(),
                    new ReputationApplinkModuleLoader(),
                    new TokoCashApplinkModuleLoader(),
                    new EventsDeepLinkModuleLoader(),
                    new LoyaltyAppLinkModuleLoader(),
                    new DealsDeepLinkModuleLoader(),
                    new ShopAppLinkModuleLoader(),
                    new GroupChatApplinkModuleLoader(),
                    new GamificationApplinkModuleLoader(),
                    new ProfileApplinkModuleLoader(),
                    new KolApplinkModuleLoader(),
                    new ExploreApplinkModuleLoader(),
                    new InterestPickApplinkModuleLoader(),
                    new TrackingAppLinkModuleLoader(),
                    new CheckoutAppLinkModuleLoader(),
                    new HowtopayApplinkModuleLoader(),
                    new CustomerCareApplinkModuleLoader(),
                    new TopChatAppLinkModuleLoader(),
                    new TokopointApplinkModuleLoader(),
                    new NotifCenterApplinkModuleLoader(),
                    new HomeNavigationApplinkModuleLoader(),
                    new AccountHomeApplinkModuleLoader(),
                    new InstantLoanAppLinkModuleLoader(),
                    new RecentViewApplinkModuleLoader(),
                    new ChangePasswordDeeplinkModuleLoader(),
                    new AffiliateApplinkModuleLoader(),
                    new SettingBankApplinkModuleLoader(),
                    new ChallengesDeepLinkModuleLoader(),
                    new InboxTalkApplinkModuleLoader(),
                    new ProductManageApplinkModuleLoader(),
                    new LoginRegisterApplinkModuleLoader(),
                    new ChangeInactivePhoneApplinkModuleLoader(),
                    new PhoneVerificationApplinkModuleLoader(),
                    new RNDevOptionsApplinkModuleLoader(),
                    new UserIdentificationApplinkModuleLoader(),
                    new SaldoDetailsAppLinkModuleLoader(),
                    new HomeCreditAppLinkModuleLoader()
            );
        }

        return applinkDelegate;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initBranchSession();
        ApplinkDelegate deepLinkDelegate = getApplinkDelegateInstance();

        if (GlobalConfig.isCustomerApp()) {
            AppsFlyerLib.getInstance().sendDeepLinkData(this);
        }

        DeepLinkAnalyticsImpl presenter = new DeepLinkAnalyticsImpl();
        if (getIntent() != null) {
            Intent intent = getIntent();
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            Uri applink = Uri.parse(intent.getData().toString().replaceAll("%", "%25"));
            presenter.processUTM(this, applink);
            if (deepLinkDelegate.supportsUri(applink.toString())) {
                routeFromApplink(applink);
            } else {
                Intent homeIntent = HomeRouter.getHomeActivityInterfaceRouter(this);
                homeIntent.putExtra(HomeRouter.EXTRA_APPLINK_UNSUPPORTED, true);
                if (getIntent() != null && getIntent().getExtras() != null)
                    homeIntent.putExtras(getIntent().getExtras());
                startActivity(homeIntent);
            }

            if (getIntent().getExtras() != null) {
                Bundle bundle = getIntent().getExtras();
                if (bundle.getBoolean(Constants.EXTRA_PUSH_PERSONALIZATION, false)) {
                    UnifyTracking.eventPersonalizedClicked(this, bundle.getString(Constants.EXTRA_APPLINK_CATEGORY));
                } else if (bundle.getBoolean(Constant.EXTRA_APPLINK_FROM_PUSH, false)) {
                    int notificationType = bundle.getInt(Constant.EXTRA_NOTIFICATION_TYPE, 0);
                    int notificationId = bundle.getInt(Constant.EXTRA_NOTIFICATION_ID, 0);

                    if (notificationId == 0) {
                        HistoryNotification.clearAllHistoryNotification(notificationType);
                    } else {
                        HistoryNotification.clearHistoryNotification(notificationType, notificationId);
                    }

                    NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(this);
                    notificationManagerCompat.cancel(notificationId);

                    //clear summary notification if group notification only have 1 left
                    if (notificationId != 0 && HistoryNotification.isSingleNotification(notificationType)) {
                        notificationManagerCompat.cancel(notificationType);
                    }


                }
//                NotificationModHandler.clearCacheIfFromNotification(bundle.getString(Constants.EXTRA_APPLINK_CATEGORY));
            }
        }
        finish();
    }

    private void routeFromApplink(Uri applink) {
        if (applink != null) {
            try {
                TaskStackBuilder taskStackBuilder = TaskStackBuilder.create(this);
                if (getApplicationContext() instanceof TkpdCoreRouter) {
                    taskStackBuilder.addNextIntent(
                            HomeRouter.getHomeActivityInterfaceRouter(this)
                    );
                }
                Intent nextIntent = ((ApplinkRouter) getApplicationContext()).applinkDelegate().getIntent(this, applink.toString());
                if (getIntent() != null && getIntent().getExtras() != null)
                    nextIntent.putExtras(getIntent().getExtras());
                taskStackBuilder.addNextIntent(nextIntent);
                taskStackBuilder.startActivities();
                return;
            } catch (Exception ignored) {
            }

            try {
                TaskStackBuilder taskStackBuilder = ((ApplinkRouter) getApplicationContext()).applinkDelegate().getTaskStackBuilder(this, applink.toString());
                taskStackBuilder.startActivities();
            } catch (Exception ignored) {
            }
        }
    }

    @DeepLink({Constants.Applinks.SellerApp.SELLER_APP_HOME,
            Constants.Applinks.SellerApp.TOPADS_DASHBOARD,
            Constants.Applinks.SellerApp.PRODUCT_ADD,
            Constants.Applinks.SellerApp.SALES,
            Constants.Applinks.SellerApp.TOPADS_CREDIT,
            Constants.Applinks.SellerApp.TOPADS_PRODUCT_CREATE,
            Constants.Applinks.SellerApp.GOLD_MERCHANT,
            Constants.Applinks.SellerApp.TOPADS_DASHBOARD,
            Constants.Applinks.SellerApp.TOPADS_PRODUCT_DETAIL,
            Constants.Applinks.SellerApp.TOPADS_PRODUCT_DETAIL_CONSTS,
            Constants.Applinks.SellerApp.BROWSER})
    public static Intent getIntentSellerApp(Context context, Bundle extras) {
        Intent launchIntent = context.getPackageManager()
                .getLaunchIntentForPackage(GlobalConfig.PACKAGE_SELLER_APP);

        if (launchIntent == null) {
            return RedirectCreateShopActivity.getCallingIntent(context);
        } else {
            launchIntent.setData(Uri.parse(extras.getString(DeepLink.URI)));
            launchIntent.putExtras(extras);
            launchIntent.putExtra(Constants.EXTRA_APPLINK, extras.getString(DeepLink.URI));
            return launchIntent;
        }
    }

    @DeepLink(Constants.Applinks.BROWSER)
    public static Intent getCallingIntentOpenBrowser(Context context, Bundle extras) {
        String webUrl = extras.getString(
                Constants.ARG_NOTIFICATION_URL, TkpdBaseURL.DEFAULT_TOKOPEDIA_WEBSITE_URL
        );
        Intent destination = new Intent(Intent.ACTION_VIEW);
        destination.setData(Uri.parse(webUrl));
        return destination;
    }

    private void initBranchSession() {

        LinkerDeeplinkData linkerDeeplinkData = new LinkerDeeplinkData();
        linkerDeeplinkData.setClientId(TrackingUtils.getClientID(this));
        linkerDeeplinkData.setReferrable(this.getIntent().getData());

        LinkerManager.getInstance().handleDefferedDeeplink(LinkerUtils.createDeeplinkRequest(0,
                linkerDeeplinkData, this, this));

    }

    @Override
    public void onDeeplinkSuccess(LinkerDeeplinkResult linkerDefferedDeeplinkData) {
        CacheUtil.storeWebToAppPromoCodeIfExist(linkerDefferedDeeplinkData.getPromoCode(), this);
    }

    @Override
    public void onError(LinkerError linkerError) {

    }
}
