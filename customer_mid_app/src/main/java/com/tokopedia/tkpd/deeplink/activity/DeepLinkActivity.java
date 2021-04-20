package com.tokopedia.tkpd.deeplink.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import androidx.core.app.TaskStackBuilder;

import com.airbnb.deeplinkdispatch.DeepLink;
import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.applink.ApplinkConst;
import com.tokopedia.applink.DeepLinkChecker;
import com.tokopedia.applink.DeeplinkMapper;
import com.tokopedia.applink.RouteManager;
import com.tokopedia.core.TkpdCoreRouter;
import com.tokopedia.core.analytics.AppScreen;
import com.tokopedia.core.analytics.TrackingUtils;
import com.tokopedia.core.analytics.deeplink.DeeplinkUTMUtils;
import com.tokopedia.core.analytics.nishikino.model.Campaign;
import com.tokopedia.core.gcm.Constants;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.customer_mid_app.R;
import com.tokopedia.logger.ServerLogger;
import com.tokopedia.logger.utils.Priority;
import com.tokopedia.tkpd.deeplink.listener.DeepLinkView;
import com.tokopedia.tkpd.deeplink.presenter.DeepLinkPresenter;
import com.tokopedia.tkpd.deeplink.presenter.DeepLinkPresenterImpl;
import com.tokopedia.utils.uri.DeeplinkUtils;

import java.util.HashMap;
import java.util.Map;

import timber.log.Timber;

/**
 * @author by Angga.Prasetiyo on 14/12/2015.
 * modified Alvarisi
 */
public class DeepLinkActivity extends BasePresenterActivity<DeepLinkPresenter> implements
        DeepLinkView {

    private static final String EXTRA_STATE_APP_WEB_VIEW = "EXTRA_STATE_APP_WEB_VIEW";
    private static final String APPLINK_URL = "url";
    private Uri uriData;
    private View mainView;

    @Override
    public String getScreenName() {
        return AppScreen.SCREEN_DEEP_LINK;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TrackingUtils.sendAppsFlyerDeeplink(DeepLinkActivity.this);

        checkUrlMapToApplink();
        sendCampaignTrack(uriData);


        isAllowFetchDepartmentView = true;

        ImageView loadingView = findViewById(R.id.iv_loading);
        ImageHandler.loadGif(loadingView, R.drawable.ic_loading_indeterminate, -1);
        logDeeplink();
    }

    private void checkUrlMapToApplink() {
        String applink = DeeplinkMapper.getRegisteredNavigation(this, uriData.toString());
        if (!TextUtils.isEmpty(applink) && RouteManager.isSupportApplink(this, applink)) {
            String screenName = AppScreen.SCREEN_NATIVE_RECHARGE;
            presenter.sendCampaignGTM(this, applink, screenName);
            RouteManager.route(this, applink);
            finish();
        } else {
            initDeepLink();
        }
    }

    private void sendCampaignTrack(Uri uriData) {
        Campaign campaign = DeeplinkUTMUtils.convertUrlCampaign(this, Uri.parse(uriData.toString()));
        presenter.sendAuthenticatedEvent(uriData, campaign, getScreenName());
    }

    @Override
    protected void setupURIPass(Uri data) {
        this.uriData = DeepLinkChecker.getRemoveAmpLink(this, data);
    }

    @Override
    protected void setupBundlePass(Bundle extras) {
    }

    @Override
    protected void initialPresenter() {
        presenter = new DeepLinkPresenterImpl(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_deep_link_viewer;
    }

    @Override
    protected void initView() {
        mainView = findViewById(R.id.main_view);
    }

    @Override
    protected void setViewListener() {

    }

    @Override
    protected void initVar() {

    }

    @Override
    protected void setActionVar() {

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
    public void networkError(final Uri uriData) {
        NetworkErrorHelper.showEmptyState(this, mainView, new NetworkErrorHelper.RetryClickedListener() {
            @Override
            public void onRetryClicked() {
                presenter.processDeepLinkAction(DeepLinkActivity.this, uriData);
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() > 0) {
            getFragmentManager().popBackStack();
        } else {
            if (getIntent().getExtras() != null && getIntent().getExtras().getBoolean(Constants.EXTRA_APPLINK_FROM_INTERNAL, false)) {
                super.onBackPressed();
            } else {
                Intent intent = new Intent(this, ((TkpdCoreRouter) getApplication()).getHomeClass());
                this.startActivity(intent);
                this.finish();
            }
        }
    }

    @Override
    public void initDeepLink() {
        if (uriData != null || getIntent().getBooleanExtra(EXTRA_STATE_APP_WEB_VIEW, false)) {
            Bundle bundle = getIntent().getExtras();
            boolean deeplink = getIntent().getBooleanExtra(DeepLink.IS_DEEP_LINK, false);
            String applinkUrl = null;
            if (bundle != null) {
                applinkUrl = bundle.getString(APPLINK_URL);
            }
            if (deeplink && !TextUtils.isEmpty(applinkUrl)) {
                uriData = DeepLinkChecker.getRemoveAmpLink(this, Uri.parse(applinkUrl));
                presenter.actionGotUrlFromApplink(uriData);
            } else {
                presenter.checkUriLogin(uriData);
                presenter.processDeepLinkAction(DeepLinkActivity.this, uriData);
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
        isAllowFetchDepartmentView = true;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Timber.d("FCM onNewIntent " + intent.getData());
        if (intent.getData() != null) {
            uriData = DeepLinkChecker.getRemoveAmpLink(this, intent.getData());
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
}
