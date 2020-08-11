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
import com.tokopedia.applink.TkpdApplinkDelegate;
import com.tokopedia.browse.common.applink.DigitalBrowseApplinkModule;
import com.tokopedia.browse.common.applink.DigitalBrowseApplinkModuleLoader;
import com.tokopedia.buyerorder.common.applink.TransactionApplinkModule;
import com.tokopedia.buyerorder.common.applink.TransactionApplinkModuleLoader;
import com.tokopedia.cachemanager.PersistentCacheManager;
import com.tokopedia.config.GlobalConfig;
import com.tokopedia.core.analytics.AppEventTracking;
import com.tokopedia.core.app.TkpdCoreRouter;
import com.tokopedia.core.gcm.Constants;
import com.tokopedia.core.var.TkpdCache;
import com.tokopedia.explore.applink.ExploreApplinkModule;
import com.tokopedia.explore.applink.ExploreApplinkModuleLoader;
import com.tokopedia.feedplus.view.deeplink.FeedDeeplinkModule;
import com.tokopedia.feedplus.view.deeplink.FeedDeeplinkModuleLoader;
import com.tokopedia.homecredit.applink.HomeCreditAppLinkModule;
import com.tokopedia.homecredit.applink.HomeCreditAppLinkModuleLoader;
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
import com.tokopedia.officialstore.applink.OfficialStoreApplinkModule;
import com.tokopedia.officialstore.applink.OfficialStoreApplinkModuleLoader;
import com.tokopedia.phoneverification.applink.PhoneVerificationApplinkModule;
import com.tokopedia.phoneverification.applink.PhoneVerificationApplinkModuleLoader;
import com.tokopedia.pms.howtopay.HowtopayApplinkModule;
import com.tokopedia.pms.howtopay.HowtopayApplinkModuleLoader;
import com.tokopedia.product.detail.applink.ProductDetailApplinkModule;
import com.tokopedia.product.detail.applink.ProductDetailApplinkModuleLoader;
import com.tokopedia.promotionstarget.presentation.subscriber.GratificationSubscriber;
import com.tokopedia.pushnotif.data.constant.Constant;
import com.tokopedia.pushnotif.data.repository.HistoryRepository;
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl;
import com.tokopedia.remoteconfig.RemoteConfig;
import com.tokopedia.remoteconfig.RemoteConfigKey;
import com.tokopedia.seller.applink.SellerApplinkModule;
import com.tokopedia.seller.applink.SellerApplinkModuleLoader;
import com.tokopedia.tkpd.deeplink.presenter.DeepLinkAnalyticsImpl;
import com.tokopedia.tkpd.redirect.RedirectCreateShopActivity;
import com.tokopedia.track.TrackApp;
import com.tokopedia.updateinactivephone.common.applink.ChangeInactivePhoneApplinkModule;
import com.tokopedia.updateinactivephone.common.applink.ChangeInactivePhoneApplinkModuleLoader;
import com.tokopedia.url.TokopediaUrl;
import com.tokopedia.utils.uri.DeeplinkUtils;
import com.tokopedia.weaver.WeaveInterface;
import com.tokopedia.weaver.Weaver;
import com.tokopedia.webview.WebViewApplinkModule;
import com.tokopedia.webview.WebViewApplinkModuleLoader;

import org.jetbrains.annotations.NotNull;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;

@DeepLinkHandler({
        ConsumerDeeplinkModule.class,
        SellerApplinkModule.class,
        TransactionApplinkModule.class,
        ProductDetailApplinkModule.class,
        FeedDeeplinkModule.class,
        DigitalBrowseApplinkModule.class,
        OvoUpgradeDeeplinkModule.class,
        LoyaltyAppLinkModule.class,
        ExploreApplinkModule.class,
        HowtopayApplinkModule.class,
        LoginRegisterApplinkModule.class,
        ChangeInactivePhoneApplinkModule.class,
        PhoneVerificationApplinkModule.class,
        HomeCreditAppLinkModule.class,
        OfficialStoreApplinkModule.class,
        WebViewApplinkModule.class,
})

public class DeeplinkHandlerActivity extends AppCompatActivity implements DefferedDeeplinkCallback {

    private static ApplinkDelegate applinkDelegate;
    private Subscription clearNotifUseCase;
    private static final String ENABLE_ASYNC_APPLINK_DELEGATE_CREATION = "android_async_applink_delegate_creation";


    public static ApplinkDelegate getApplinkDelegateInstance() {
        if (applinkDelegate == null) {
            applinkDelegate = new TkpdApplinkDelegate(
                    new ConsumerDeeplinkModuleLoader(),
                    new OvoUpgradeDeeplinkModuleLoader(),
                    new SellerApplinkModuleLoader(),
                    new TransactionApplinkModuleLoader(),
                    new ProductDetailApplinkModuleLoader(),
                    new FeedDeeplinkModuleLoader(),
                    new DigitalBrowseApplinkModuleLoader(),
                    new LoyaltyAppLinkModuleLoader(),
                    new ExploreApplinkModuleLoader(),
                    new HowtopayApplinkModuleLoader(),
                    new LoginRegisterApplinkModuleLoader(),
                    new ChangeInactivePhoneApplinkModuleLoader(),
                    new PhoneVerificationApplinkModuleLoader(),
                    new HomeCreditAppLinkModuleLoader(),
                    new OfficialStoreApplinkModuleLoader(),
                    new WebViewApplinkModuleLoader()
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
        iniBranchIO(this);
        logDeeplink();
        finish();
    }

    public static void createApplinkDelegateInBackground(Context context){
        WeaveInterface appLinkDelegateWeave = new WeaveInterface() {
            @NotNull
            @Override
            public Object execute() {
                return getAppLinkDelegate();
            }
        };
        Weaver.Companion.executeWeaveCoRoutineWithFirebase(appLinkDelegateWeave, ENABLE_ASYNC_APPLINK_DELEGATE_CREATION, context.getApplicationContext());
    }

    private static boolean getAppLinkDelegate(){
        getApplinkDelegateInstance();
        return true;
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
                        HistoryRepository.clearAllHistoryNotification(DeeplinkHandlerActivity.this, notificationType);
                    } else {
                        HistoryRepository.clearHistoryNotification(DeeplinkHandlerActivity.this, notificationType, notificationId);
                    }

                    return HistoryRepository.isSingleNotification(DeeplinkHandlerActivity.this, notificationType);
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
                                ((com.tokopedia.core.TkpdCoreRouter) getApplicationContext()).getHomeIntent(this)
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

    @Override
    public void onDeeplinkSuccess(LinkerDeeplinkResult linkerDefferedDeeplinkData) {
        PersistentCacheManager.instance.put(TkpdCache.Key.KEY_CACHE_PROMO_CODE, linkerDefferedDeeplinkData.getPromoCode() != null ?
                linkerDefferedDeeplinkData.getPromoCode() : "");
    }

    @Override
    public void onError(LinkerError linkerError) {

    }

    private void iniBranchIO(Context context){
        RemoteConfig remoteConfig = new FirebaseRemoteConfigImpl(context);
        if (remoteConfig.getBoolean(RemoteConfigKey.APP_ENABLE_BRANCH_INIT_DEEPLINKHANDLER)){
            LinkerManager.getInstance().initSession();
        }
    }

    private void logDeeplink() {
        String referrer = DeeplinkUtils.INSTANCE.getReferrerCompatible(this);
        Uri extraReferrer = DeeplinkUtils.INSTANCE.getExtraReferrer(this);
        Uri uri = DeeplinkUtils.INSTANCE.getDataUri(this);
        Timber.w("P1#DEEPLINK_OPEN_APP#%s;referrer='%s';extra_referrer='%s';uri='%s'",
                getClass().getSimpleName(), referrer, extraReferrer.toString(), uri.toString());
    }
}
