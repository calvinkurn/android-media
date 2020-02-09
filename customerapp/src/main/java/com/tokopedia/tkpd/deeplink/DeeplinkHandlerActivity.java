package com.tokopedia.tkpd.deeplink;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.app.TaskStackBuilder;

import com.airbnb.deeplinkdispatch.DeepLink;
import com.airbnb.deeplinkdispatch.DeepLinkHandler;
import com.appsflyer.AppsFlyerLib;
import com.tokopedia.applink.ApplinkConst;
import com.tokopedia.applink.ApplinkDelegate;
import com.tokopedia.applink.ApplinkRouter;
import com.tokopedia.applink.DeeplinkMapper;
import com.tokopedia.applink.RouteManager;
import com.tokopedia.applink.SessionApplinkModule;
import com.tokopedia.applink.SessionApplinkModuleLoader;
import com.tokopedia.applink.TkpdApplinkDelegate;
import com.tokopedia.browse.common.applink.DigitalBrowseApplinkModule;
import com.tokopedia.browse.common.applink.DigitalBrowseApplinkModuleLoader;
import com.tokopedia.cachemanager.PersistentCacheManager;
import com.tokopedia.changepassword.common.applink.ChangePasswordDeeplinkModule;
import com.tokopedia.changepassword.common.applink.ChangePasswordDeeplinkModuleLoader;
import com.tokopedia.core.analytics.AppEventTracking;
import com.tokopedia.core.app.TkpdCoreRouter;
import com.tokopedia.core.deeplink.CoreDeeplinkModule;
import com.tokopedia.core.deeplink.CoreDeeplinkModuleLoader;
import com.tokopedia.core.gcm.Constants;
import com.tokopedia.core.router.home.HomeRouter;
import com.tokopedia.config.GlobalConfig;
import com.tokopedia.core.var.TkpdCache;
import com.tokopedia.createpost.view.applink.CreatePostModule;
import com.tokopedia.createpost.view.applink.CreatePostModuleLoader;
import com.tokopedia.developer_options.presentation.applink.RNDevOptionsApplinkModule;
import com.tokopedia.developer_options.presentation.applink.RNDevOptionsApplinkModuleLoader;
import com.tokopedia.discovery.applink.DiscoveryApplinkModule;
import com.tokopedia.discovery.applink.DiscoveryApplinkModuleLoader;
import com.tokopedia.events.deeplink.EventsDeepLinkModule;
import com.tokopedia.events.deeplink.EventsDeepLinkModuleLoader;
import com.tokopedia.explore.applink.ExploreApplinkModule;
import com.tokopedia.explore.applink.ExploreApplinkModuleLoader;
import com.tokopedia.feedplus.view.deeplink.FeedDeeplinkModule;
import com.tokopedia.feedplus.view.deeplink.FeedDeeplinkModuleLoader;
import com.tokopedia.home.account.applink.AccountHomeApplinkModule;
import com.tokopedia.home.account.applink.AccountHomeApplinkModuleLoader;
import com.tokopedia.home.applink.HomeApplinkModule;
import com.tokopedia.home.applink.HomeApplinkModuleLoader;
import com.tokopedia.home_recom.deeplink.RecommendationDeeplinkModule;
import com.tokopedia.home_recom.deeplink.RecommendationDeeplinkModuleLoader;
import com.tokopedia.homecredit.applink.HomeCreditAppLinkModule;
import com.tokopedia.homecredit.applink.HomeCreditAppLinkModuleLoader;
import com.tokopedia.inbox.deeplink.InboxDeeplinkModule;
import com.tokopedia.inbox.deeplink.InboxDeeplinkModuleLoader;
import com.tokopedia.instantdebitbca.data.view.applink.InstantDebitBcaApplinkModule;
import com.tokopedia.instantdebitbca.data.view.applink.InstantDebitBcaApplinkModuleLoader;
import com.tokopedia.interestpick.applink.InterestPickApplinkModule;
import com.tokopedia.interestpick.applink.InterestPickApplinkModuleLoader;
import com.tokopedia.kol.applink.KolApplinkModule;
import com.tokopedia.kol.applink.KolApplinkModuleLoader;
import com.tokopedia.kyc.deeplink.OvoUpgradeDeeplinkModule;
import com.tokopedia.kyc.deeplink.OvoUpgradeDeeplinkModuleLoader;
import com.tokopedia.linker.LinkerManager;
import com.tokopedia.linker.interfaces.DefferedDeeplinkCallback;
import com.tokopedia.linker.model.LinkerDeeplinkResult;
import com.tokopedia.linker.model.LinkerError;
import com.tokopedia.loginregister.common.applink.LoginRegisterApplinkModule;
import com.tokopedia.loginregister.common.applink.LoginRegisterApplinkModuleLoader;
import com.tokopedia.loyalty.applink.LoyaltyAppLinkModule;
import com.tokopedia.loyalty.applink.LoyaltyAppLinkModuleLoader;
import com.tokopedia.navigation.applink.HomeNavigationApplinkModule;
import com.tokopedia.navigation.applink.HomeNavigationApplinkModuleLoader;
import com.tokopedia.officialstore.applink.OfficialStoreApplinkModule;
import com.tokopedia.officialstore.applink.OfficialStoreApplinkModuleLoader;
import com.tokopedia.phoneverification.applink.PhoneVerificationApplinkModule;
import com.tokopedia.phoneverification.applink.PhoneVerificationApplinkModuleLoader;
import com.tokopedia.pms.howtopay.HowtopayApplinkModule;
import com.tokopedia.pms.howtopay.HowtopayApplinkModuleLoader;
import com.tokopedia.product.detail.applink.ProductDetailApplinkModule;
import com.tokopedia.product.detail.applink.ProductDetailApplinkModuleLoader;
import com.tokopedia.promotionstarget.presentation.subscriber.GratificationSubscriber;
import com.tokopedia.pushnotif.Constant;
import com.tokopedia.pushnotif.HistoryNotification;
import com.tokopedia.recentview.view.applink.RecentViewApplinkModule;
import com.tokopedia.recentview.view.applink.RecentViewApplinkModuleLoader;
import com.tokopedia.seller.applink.SellerApplinkModule;
import com.tokopedia.seller.applink.SellerApplinkModuleLoader;
import com.tokopedia.shop.applink.ShopAppLinkModule;
import com.tokopedia.shop.applink.ShopAppLinkModuleLoader;
import com.tokopedia.tkpd.deeplink.presenter.DeepLinkAnalyticsImpl;
import com.tokopedia.tkpd.redirect.RedirectCreateShopActivity;
import com.tokopedia.tkpd.tkpdreputation.applink.ReputationApplinkModule;
import com.tokopedia.tkpd.tkpdreputation.applink.ReputationApplinkModuleLoader;
import com.tokopedia.topchat.deeplink.TopChatAppLinkModule;
import com.tokopedia.topchat.deeplink.TopChatAppLinkModuleLoader;
import com.tokopedia.track.TrackApp;
import com.tokopedia.logisticorder.applink.TrackingAppLinkModule;
import com.tokopedia.logisticorder.applink.TrackingAppLinkModuleLoader;
import com.tokopedia.transaction.applink.TransactionApplinkModule;
import com.tokopedia.transaction.applink.TransactionApplinkModuleLoader;
import com.tokopedia.updateinactivephone.applink.ChangeInactivePhoneApplinkModule;
import com.tokopedia.updateinactivephone.applink.ChangeInactivePhoneApplinkModuleLoader;
import com.tokopedia.url.TokopediaUrl;
import com.tokopedia.useridentification.applink.UserIdentificationApplinkModule;
import com.tokopedia.useridentification.applink.UserIdentificationApplinkModuleLoader;
import com.tokopedia.webview.WebViewApplinkModule;
import com.tokopedia.webview.WebViewApplinkModuleLoader;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

@DeepLinkHandler({
        ConsumerDeeplinkModule.class,
        CoreDeeplinkModule.class,
        InboxDeeplinkModule.class,
        SellerApplinkModule.class,
        TransactionApplinkModule.class,
        ProductDetailApplinkModule.class,
        HomeApplinkModule.class,
        DiscoveryApplinkModule.class,
        SessionApplinkModule.class,
        FeedDeeplinkModule.class,
        InstantDebitBcaApplinkModule.class,
        DigitalBrowseApplinkModule.class,
        ReputationApplinkModule.class,
        EventsDeepLinkModule.class,
        OvoUpgradeDeeplinkModule.class,
        LoyaltyAppLinkModule.class,
        ShopAppLinkModule.class,
        CreatePostModule.class,
        KolApplinkModule.class,
        ExploreApplinkModule.class,
        InterestPickApplinkModule.class,
        TrackingAppLinkModule.class,
        HowtopayApplinkModule.class,
        TopChatAppLinkModule.class,
        HomeNavigationApplinkModule.class,
        AccountHomeApplinkModule.class,
        RecentViewApplinkModule.class,
        ChangePasswordDeeplinkModule.class,
        LoginRegisterApplinkModule.class,
        ChangeInactivePhoneApplinkModule.class,
        PhoneVerificationApplinkModule.class,
        RNDevOptionsApplinkModule.class,
        UserIdentificationApplinkModule.class,
        HomeCreditAppLinkModule.class,
        OfficialStoreApplinkModule.class,
        WebViewApplinkModule.class,
        RecommendationDeeplinkModule.class
})

public class DeeplinkHandlerActivity extends AppCompatActivity implements DefferedDeeplinkCallback {

    private static ApplinkDelegate applinkDelegate;
    private Subscription clearNotifUseCase;

    public static ApplinkDelegate getApplinkDelegateInstance() {
        if (applinkDelegate == null) {
            applinkDelegate = new TkpdApplinkDelegate(
                    new ConsumerDeeplinkModuleLoader(),
                    new CoreDeeplinkModuleLoader(),
                    new InboxDeeplinkModuleLoader(),
                    new OvoUpgradeDeeplinkModuleLoader(),
                    new SellerApplinkModuleLoader(),
                    new TransactionApplinkModuleLoader(),
                    new ProductDetailApplinkModuleLoader(),
                    new HomeApplinkModuleLoader(),
                    new DiscoveryApplinkModuleLoader(),
                    new SessionApplinkModuleLoader(),
                    new FeedDeeplinkModuleLoader(),
                    new InstantDebitBcaApplinkModuleLoader(),
                    new DigitalBrowseApplinkModuleLoader(),
                    new ReputationApplinkModuleLoader(),
                    new EventsDeepLinkModuleLoader(),
                    new LoyaltyAppLinkModuleLoader(),
                    new ShopAppLinkModuleLoader(),
                    new CreatePostModuleLoader(),
                    new KolApplinkModuleLoader(),
                    new ExploreApplinkModuleLoader(),
                    new InterestPickApplinkModuleLoader(),
                    new TrackingAppLinkModuleLoader(),
                    new HowtopayApplinkModuleLoader(),
                    new TopChatAppLinkModuleLoader(),
                    new HomeNavigationApplinkModuleLoader(),
                    new AccountHomeApplinkModuleLoader(),
                    new RecentViewApplinkModuleLoader(),
                    new ChangePasswordDeeplinkModuleLoader(),
                    new LoginRegisterApplinkModuleLoader(),
                    new ChangeInactivePhoneApplinkModuleLoader(),
                    new PhoneVerificationApplinkModuleLoader(),
                    new RNDevOptionsApplinkModuleLoader(),
                    new UserIdentificationApplinkModuleLoader(),
                    new HomeCreditAppLinkModuleLoader(),
                    new OfficialStoreApplinkModuleLoader(),
                    new WebViewApplinkModuleLoader(),
                    new RecommendationDeeplinkModuleLoader()
            );
        }

        return applinkDelegate;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        GratificationSubscriber.addActivityNameToExclude(getClass().getCanonicalName());
        super.onCreate(savedInstanceState);
        ApplinkDelegate deepLinkDelegate = getApplinkDelegateInstance();

        if (!GlobalConfig.isSellerApp()) {
            AppsFlyerLib.getInstance().sendDeepLinkData(this);
        }

        DeepLinkAnalyticsImpl presenter = new DeepLinkAnalyticsImpl();
        if (getIntent() != null) {
            String applinkString = getIntent().getData().toString().replaceAll("%", "%25");
            Uri applink = Uri.parse(applinkString);
            presenter.processUTM(this, applink);

            //map applink to internal if any
            String mappedDeeplink = DeeplinkMapper.getRegisteredNavigation(this, applinkString);

            Bundle queryParamBundle = RouteManager.getBundleFromAppLinkQueryParams(applinkString);
            Bundle defaultBundle = new Bundle();
            defaultBundle.putBundle(RouteManager.QUERY_PARAM, queryParamBundle);

            if (TextUtils.isEmpty(mappedDeeplink)) {
                routeApplink(deepLinkDelegate, applinkString, defaultBundle);
            } else {
                routeApplink(deepLinkDelegate, mappedDeeplink, defaultBundle);
            }

            if (getIntent().getExtras() != null) {
                Bundle bundle = getIntent().getExtras();
                if (bundle.getBoolean(Constants.EXTRA_PUSH_PERSONALIZATION, false)) {
                    eventPersonalizedClicked(bundle.getString(Constants.EXTRA_APPLINK_CATEGORY));
                } else if (bundle.getBoolean(Constant.EXTRA_APPLINK_FROM_PUSH, false)) {
                    int notificationType = bundle.getInt(Constant.EXTRA_NOTIFICATION_TYPE, 0);
                    int notificationId = bundle.getInt(Constant.EXTRA_NOTIFICATION_ID, 0);
                    cancelNotification(notificationType, notificationId);
                }
            }
        }
        LinkerManager.getInstance().initSession();
        finish();
    }

    public static void createApplinkDelegateInBackground(){
        Observable.fromCallable(() -> {
            getApplinkDelegateInstance();
            return true;
        }).subscribeOn(Schedulers.computation()).observeOn(AndroidSchedulers.mainThread()).subscribe();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (clearNotifUseCase != null && !clearNotifUseCase.isUnsubscribed()) {
            clearNotifUseCase.unsubscribe();
        }
    }

    private void cancelNotification(int notificationType, int notificationId) {
        clearNotifUseCase = Observable.just(true)
                .subscribeOn(Schedulers.io())
                .map(aBoolean -> {
                    if (notificationId == 0) {
                        HistoryNotification.clearAllHistoryNotification(DeeplinkHandlerActivity.this, notificationType);
                    } else {
                        HistoryNotification.clearHistoryNotification(DeeplinkHandlerActivity.this, notificationType, notificationId);
                    }

                    return HistoryNotification.isSingleNotification(DeeplinkHandlerActivity.this, notificationType);
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Boolean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable ignored) {

                    }

                    @Override
                    public void onNext(Boolean isSingleNotif) {
                        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(DeeplinkHandlerActivity.this);
                        notificationManagerCompat.cancel(notificationId);

                        //clear summary notification if group notification only have 1 left
                        if (notificationId != 0 && isSingleNotif) {
                            notificationManagerCompat.cancel(notificationType);
                        }
                    }
                });
    }

    private void routeApplink(ApplinkDelegate deepLinkDelegate, String applinkString, Bundle defaultBundle) {
        if (deepLinkDelegate.supportsUri(applinkString)) {
            routeFromApplink(deepLinkDelegate, Uri.parse(applinkString), defaultBundle);
        } else {
            Intent intent = RouteManager.getIntent(this, applinkString);
            if (defaultBundle != null) {
                intent.putExtras(defaultBundle);
            }
            startActivity(intent);
        }
    }

    public void eventPersonalizedClicked(String label) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(
                AppEventTracking.Event.OPEN_PUSH_NOTIFICATION,
                AppEventTracking.Category.PUSH_NOTIFICATION,
                AppEventTracking.Action.OPEN,
                label);
    }

    private void routeFromApplink(ApplinkDelegate applinkDelegate, Uri applink, Bundle defaultBudle) {
        if (applink != null) {
            try {

                Intent nextIntent = applinkDelegate.getIntent(this, applink.toString());
                if (defaultBudle != null) {
                    nextIntent.putExtras(defaultBudle);
                }
                if (getIntent() != null && getIntent().getExtras() != null)
                    nextIntent.putExtras(getIntent().getExtras());

                if (isTaskRoot()) {
                    TaskStackBuilder taskStackBuilder = TaskStackBuilder.create(this);
                    if (getApplicationContext() instanceof TkpdCoreRouter) {
                        taskStackBuilder.addNextIntent(
                                HomeRouter.getHomeActivityInterfaceRouter(this)
                        );
                        getIntent().addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    }
                    taskStackBuilder.addNextIntent(nextIntent);
                    taskStackBuilder.startActivities();
                } else {
                    startActivity(nextIntent);
                }
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

    @DeepLink(ApplinkConst.BROWSER)
    public static Intent getCallingIntentOpenBrowser(Context context, Bundle extras) {
        String webUrl = extras.getString("url", TokopediaUrl.Companion.getInstance().getWEB()
        );
        Intent destination = new Intent(Intent.ACTION_VIEW);
        String decodedUrl;
        try {
            decodedUrl = URLDecoder.decode(webUrl, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            decodedUrl = webUrl;
        }
        destination.setData(Uri.parse(decodedUrl));
        return destination;
    }

    @Override
    public void onDeeplinkSuccess(LinkerDeeplinkResult linkerDefferedDeeplinkData) {
        PersistentCacheManager.instance.put(TkpdCache.Key.KEY_CACHE_PROMO_CODE, linkerDefferedDeeplinkData.getPromoCode() != null ?
                linkerDefferedDeeplinkData.getPromoCode() : "");
    }

    @Override
    public void onError(LinkerError linkerError) {

    }
}
