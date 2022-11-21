package com.tokopedia.tkpd.deeplink;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationManagerCompat;

import com.newrelic.agent.android.NewRelic;
import com.tokopedia.applink.ApplinkConst;
import com.tokopedia.applink.DeeplinkMapper;
import com.tokopedia.applink.RouteManager;
import com.tokopedia.cachemanager.PersistentCacheManager;
import com.tokopedia.core.analytics.AppEventTracking;
import com.tokopedia.core.analytics.deeplink.DeeplinkUTMUtils;
import com.tokopedia.core.gcm.Constants;
import com.tokopedia.core.var.TkpdCache;
import com.tokopedia.keys.Keys;
import com.tokopedia.linker.LinkerManager;
import com.tokopedia.linker.interfaces.DefferedDeeplinkCallback;
import com.tokopedia.linker.model.LinkerDeeplinkResult;
import com.tokopedia.linker.model.LinkerError;
import com.tokopedia.logger.ServerLogger;
import com.tokopedia.logger.utils.Priority;
import com.tokopedia.promotionstarget.presentation.subscriber.GratificationSubscriber;
import com.tokopedia.pushnotif.data.constant.Constant;
import com.tokopedia.pushnotif.data.repository.HistoryRepository;
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl;
import com.tokopedia.remoteconfig.RemoteConfig;
import com.tokopedia.remoteconfig.RemoteConfigKey;
import com.tokopedia.tkpd.deeplink.presenter.DeepLinkAnalyticsImpl;
import com.tokopedia.track.TrackApp;
import com.tokopedia.user.session.UserSession;
import com.tokopedia.user.session.UserSessionInterface;
import com.tokopedia.utils.uri.DeeplinkUtils;

import java.util.HashMap;
import java.util.Map;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class DeeplinkHandlerActivity extends AppCompatActivity implements DefferedDeeplinkCallback {

    private static final String TOKOPEDIA_DOMAIN = "tokopedia";
    private static final String URL_QUERY_PARAM = "url";
    private Subscription clearNotifUseCase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        DeepLinkAnalyticsImpl presenter = new DeepLinkAnalyticsImpl();
        if (getIntent() != null && getIntent().getData()!= null) {
            String applinkString = getIntent().getData().toString().replaceAll("%", "%25");
            Uri applink = Uri.parse(applinkString);
            presenter.processUTM(this, applink);

            //map applink to internal if any
            String mappedDeeplink = DeeplinkMapper.getRegisteredNavigation(this, applinkString);

            Bundle queryParamBundle = RouteManager.getBundleFromAppLinkQueryParams(applinkString);
            Bundle defaultBundle = new Bundle();
            defaultBundle.putBundle(RouteManager.QUERY_PARAM, queryParamBundle);

            if (TextUtils.isEmpty(mappedDeeplink)) {
                routeApplink(applinkString, defaultBundle);
            } else {
                routeApplink(mappedDeeplink, defaultBundle);
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
        initializationNewRelic();
        iniBranchIO(this);
        logDeeplink();
        logWebViewApplink();
        finish();
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

    private void routeApplink(String applinkString, Bundle defaultBundle) {
        Intent intent = RouteManager.getIntent(this, applinkString);
        if (defaultBundle != null) {
            intent.putExtras(defaultBundle);
        }
        startActivity(intent);
    }

    public void eventPersonalizedClicked(String label) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(
                AppEventTracking.Event.OPEN_PUSH_NOTIFICATION,
                AppEventTracking.Category.PUSH_NOTIFICATION,
                AppEventTracking.Action.OPEN,
                label);
    }

    @Override
    public void onDeeplinkSuccess(LinkerDeeplinkResult linkerDefferedDeeplinkData) {
        PersistentCacheManager.instance.put(TkpdCache.Key.KEY_CACHE_PROMO_CODE, linkerDefferedDeeplinkData.getPromoCode() != null ?
                linkerDefferedDeeplinkData.getPromoCode() : "");
    }

    @Override
    public void onError(LinkerError linkerError) {

    }

    private void iniBranchIO(Context context) {
        RemoteConfig remoteConfig = new FirebaseRemoteConfigImpl(context);
        if (remoteConfig.getBoolean(RemoteConfigKey.APP_ENABLE_BRANCH_INIT_DEEPLINKHANDLER)) {
            LinkerManager.getInstance().initSession(this, uriHaveCampaignData());
        }
    }

    private boolean uriHaveCampaignData(){
        boolean uriHaveCampaignData = false;
        if (getIntent() != null && getIntent().getData()!= null) {
            String applinkString = getIntent().getData().toString().replaceAll("%", "%25");
            Uri applink = Uri.parse(applinkString);
            uriHaveCampaignData = DeeplinkUTMUtils.isValidCampaignUrl(applink);
        }
        return uriHaveCampaignData;
    }

    private void initializationNewRelic() {
        NewRelic.withApplicationToken(Keys.NEW_RELIC_TOKEN_MA)
                .start(this.getApplication());
        UserSessionInterface userSession = new UserSession(this);
        if (userSession.isLoggedIn()) {
            NewRelic.setUserId(userSession.getUserId());
        }
    }

    private void logDeeplink() {
        String referrer = DeeplinkUtils.INSTANCE.getReferrerCompatible(this);
        Uri extraReferrer = DeeplinkUtils.INSTANCE.getExtraReferrer(this);
        Uri uri = DeeplinkUtils.INSTANCE.getDataUri(this);
        Map<String, String> messageMap = new HashMap<>();
        messageMap.put("type", getClass().getSimpleName());
        messageMap.put("referrer", referrer);
        messageMap.put("extra_referrer", extraReferrer.toString());
        messageMap.put("uri", uri.toString());
        ServerLogger.log(Priority.P1, "DEEPLINK_OPEN_APP", messageMap);
    }

    private void logWebViewApplink() {
        Uri uri = DeeplinkUtils.INSTANCE.getDataUri(this);
        if(uri.toString().contains(ApplinkConst.WEBVIEW)) {
            Uri urlToLoad = getUrlToLoad(uri);
            if(urlToLoad != null) {
                String domain = urlToLoad.getHost();
                if(domain != null) {
                    if (!getBaseDomain(domain).equalsIgnoreCase(TOKOPEDIA_DOMAIN)) {
                        Map<String, String> messageMap = new HashMap<>();
                        messageMap.put("type", "applink");
                        messageMap.put("domain", domain);
                        messageMap.put("url", uri.toString());
                        ServerLogger.log(Priority.P1, "WEBVIEW_OPENED", messageMap);
                    }
                }
            }
        }
    }

    private String getBaseDomain(String host) {
        if(host == null) {
            return "";
        }
        String[] split = host.split("\\.");
        if (split.length > 2) {
            return split[1];
        } else {
            return split[0];
        }
    }

    private Uri getUrlToLoad(Uri url) {
        try {
            return Uri.parse(url.getQueryParameter(URL_QUERY_PARAM));
        } catch (Exception e) {
            return null;
        }
    }
}
