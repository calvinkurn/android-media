package com.tokopedia.tkpd.deeplink.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.TaskStackBuilder;

import com.google.android.play.core.splitcompat.SplitCompat;
import com.newrelic.agent.android.NewRelic;
import com.tokopedia.applink.ApplinkConst;
import com.tokopedia.applink.DeepLinkChecker;
import com.tokopedia.applink.DeeplinkMapper;
import com.tokopedia.applink.RouteManager;
import com.tokopedia.applink.digital.DeeplinkMapperDigital;
import com.tokopedia.core.analytics.AppScreen;
import com.tokopedia.core.analytics.deeplink.DeeplinkUTMUtils;
import com.tokopedia.core.analytics.nishikino.model.Campaign;
import com.tokopedia.core.gcm.Constants;
import com.tokopedia.keys.Keys;
import com.tokopedia.linker.FirebaseDLWrapper;
import com.tokopedia.linker.LinkerManager;
import com.tokopedia.logger.ServerLogger;
import com.tokopedia.logger.utils.Priority;
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl;
import com.tokopedia.remoteconfig.RemoteConfig;
import com.tokopedia.remoteconfig.RemoteConfigKey;
import com.tokopedia.tkpd.deeplink.listener.DeepLinkView;
import com.tokopedia.tkpd.deeplink.presenter.DeepLinkPresenter;
import com.tokopedia.tkpd.deeplink.presenter.DeepLinkPresenterImpl;
import com.tokopedia.track.TrackApp;
import com.tokopedia.user.session.UserSession;
import com.tokopedia.user.session.UserSessionInterface;
import com.tokopedia.utils.uri.DeeplinkUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import timber.log.Timber;

/**
 * @author by Angga.Prasetiyo on 14/12/2015.
 * modified Alvarisi
 */
public class DeepLinkActivity extends AppCompatActivity implements
        DeepLinkView {

    private static final String EXTRA_STATE_APP_WEB_VIEW = "EXTRA_STATE_APP_WEB_VIEW";
    private static final String APPLINK_URL = "url";
    private static final String PATH_FIND = "find";
    private static final String PRODUCT_SEARCH_RESULT = "Product Search Results";
    private Uri uriData;
    private boolean isOriginalUrlAmp;

    private DeepLinkPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getIntent().getData() != null) {
            setupURIPass(getIntent().getData());
        }
        presenter = new DeepLinkPresenterImpl(this);
        initializationNewRelic();

        checkUrlMapToApplink();
        sendCampaignTrack(uriData, isOriginalUrlAmp);
        initBranchIO(this);
        logDeeplink();
        new FirebaseDLWrapper().getFirebaseDynamicLink(this, getIntent());
    }

    private void checkUrlMapToApplink() {
        String uriString = uriData.toString();
        String applink = DeeplinkMapper.getRegisteredNavigation(this, uriString);
        if (!TextUtils.isEmpty(applink) && RouteManager.isSupportApplink(this, applink)) {
            String digitalDeeplink = DeeplinkMapperDigital.INSTANCE.getRegisteredNavigationFromHttpDigital(this, applink);
            String screenName = "";
            if (!TextUtils.isEmpty(digitalDeeplink)) {
                screenName = AppScreen.SCREEN_NATIVE_RECHARGE;
            } else {
                List<String> pathSegments = uriData.getPathSegments();
                if (pathSegments.size() > 0 && pathSegments.get(0).equals(PATH_FIND)) {
                    screenName = PRODUCT_SEARCH_RESULT;
                } else {
                    screenName = uriData.getPath();
                }
            }
            presenter.sendCampaignGTM(this, applink, screenName, isOriginalUrlAmp);
            RouteManager.route(this, applink);
            finish();
        } else {
            initDeepLink();
        }
    }

    private void initBranchIO(Context context) {
        RemoteConfig remoteConfig = new FirebaseRemoteConfigImpl(context);
        if (remoteConfig.getBoolean(RemoteConfigKey.APP_ENABLE_BRANCH_INIT_DEEPLINK_ACTIVITY)) {
            LinkerManager.getInstance().initSession(this, uriHaveCampaignData());
        }
    }

    private boolean uriHaveCampaignData() {
        boolean uriHaveCampaignData = false;
        if (getIntent() != null && getIntent().getData() != null) {
            String applinkString = getIntent().getData().toString().replaceAll("%", "%25");
            Uri applink = Uri.parse(applinkString);
            uriHaveCampaignData = DeeplinkUTMUtils.isValidCampaignUrl(applink);
        }
        return uriHaveCampaignData;
    }

    private void sendCampaignTrack(Uri uriData, boolean isOriginalUrlAmp) {
        Campaign campaign = DeeplinkUTMUtils.convertUrlCampaign(this, Uri.parse(uriData.toString()), isOriginalUrlAmp);
        presenter.sendAuthenticatedEvent(uriData, campaign, AppScreen.SCREEN_DEEP_LINK);
    }

    private void setupURIPass(Uri data) {
        isOriginalUrlAmp = DeepLinkChecker.isAmpUrl(data);
        this.uriData = DeepLinkChecker.getRemoveAmpLink(this, data);
    }

    @Override
    public void goToPage(Intent destination) {
        TaskStackBuilder taskStackBuilder = TaskStackBuilder.create(this);
        if (this.isTaskRoot()) {
            taskStackBuilder.addNextIntent(RouteManager.getIntent(this, ApplinkConst.HOME));
        }
        taskStackBuilder.addNextIntent(destination);
        taskStackBuilder.startActivities();
        finish();
    }

    @Override
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() > 0) {
            getFragmentManager().popBackStack();
        } else {
            if (getIntent().getExtras() != null && getIntent().getExtras().getBoolean(Constants.EXTRA_APPLINK_FROM_INTERNAL, false)) {
                super.onBackPressed();
            } else {
                Intent intent = RouteManager.getIntent(this, ApplinkConst.HOME);
                this.startActivity(intent);
                this.finish();
            }
        }
    }

    @Override
    public void initDeepLink() {
        if (uriData != null || getIntent().getBooleanExtra(EXTRA_STATE_APP_WEB_VIEW, false)) {
            Bundle bundle = getIntent().getExtras();
            String IS_DEEP_LINK = "is_deep_link_flag";
            boolean deeplink = getIntent().getBooleanExtra(IS_DEEP_LINK, false);
            String applinkUrl = null;
            if (bundle != null) {
                applinkUrl = bundle.getString(APPLINK_URL);
            }
            if (deeplink && !TextUtils.isEmpty(applinkUrl)) {
                Uri uri = Uri.parse(applinkUrl);
                isOriginalUrlAmp = DeepLinkChecker.isAmpUrl(uri);
                uriData = DeepLinkChecker.getRemoveAmpLink(this, uri);
                presenter.actionGotUrlFromApplink(uriData);
            } else {
                presenter.checkUriLogin(uriData);
                presenter.processDeepLinkAction(DeepLinkActivity.this, uriData, isOriginalUrlAmp);
            }
        }
    }

    @SuppressLint("MissingSuperCall")
    @Override
    protected void onSaveInstanceState(Bundle outState) {
    }

    @Override
    public void onResume() {
        super.onResume();
        TrackApp.getInstance().getGTM().sendScreenAuthenticated(AppScreen.SCREEN_DEEP_LINK);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Timber.d("FCM onNewIntent " + intent.getData());
        Uri data = intent.getData();
        if (data != null) {
            isOriginalUrlAmp = DeepLinkChecker.isAmpUrl(data);
            uriData = DeepLinkChecker.getRemoveAmpLink(this, data);
        }
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

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(newBase);
        SplitCompat.installActivity(this);
    }
}
