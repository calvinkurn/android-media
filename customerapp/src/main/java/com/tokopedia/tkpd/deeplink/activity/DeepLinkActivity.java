package com.tokopedia.tkpd.deeplink.activity;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import androidx.core.app.TaskStackBuilder;

import com.airbnb.deeplinkdispatch.DeepLink;
import com.tkpd.library.ui.utilities.TkpdProgressDialog;
import com.tokopedia.applink.ApplinkConst;
import com.tokopedia.applink.DeeplinkMapper;
import com.tokopedia.applink.RouteManager;
import com.tokopedia.core.analytics.AppScreen;
import com.tokopedia.core.analytics.TrackingUtils;
import com.tokopedia.core.app.BasePresenterActivity;
import com.tokopedia.core.discovery.catalog.listener.ICatalogActionFragment;
import com.tokopedia.core.gcm.Constants;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.core.router.discovery.DetailProductRouter;
import com.tokopedia.core.router.home.HomeRouter;
import com.tokopedia.linker.model.LinkerData;
import com.tokopedia.tkpd.R;
import com.tokopedia.tkpd.deeplink.listener.DeepLinkView;
import com.tokopedia.tkpd.deeplink.presenter.DeepLinkPresenter;
import com.tokopedia.tkpd.deeplink.presenter.DeepLinkPresenterImpl;

import java.util.List;

import timber.log.Timber;

/**
 * @author by Angga.Prasetiyo on 14/12/2015.
 * modified Alvarisi
 */
public class DeepLinkActivity extends BasePresenterActivity<DeepLinkPresenter> implements
        DeepLinkView,
        ICatalogActionFragment {

    private TkpdProgressDialog progressDialog;

    private Uri uriData;
    private static final String EXTRA_STATE_APP_WEB_VIEW = "EXTRA_STATE_APP_WEB_VIEW";
    private static final String APPLINK_URL = "url";
    private static final String AMP = "amp";
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
        isAllowFetchDepartmentView = true;
        presenter.sendAuthenticatedEvent(uriData, getScreenName());
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

    @Override
    protected void setupURIPass(Uri data) {
        this.uriData = removeAmpFromLink(data);
    }

    @Override
    protected void setupBundlePass(Bundle extras) { }

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
    public void showLoading() {
        showProgressService();
    }

    @Override
    public void finishLoading() {
        if (progressDialog != null && progressDialog.isProgress()) progressDialog.dismiss();
    }

    @Override
    public void inflateFragment(Fragment fragment, String tag) {
        getFragmentManager().beginTransaction().add(R.id.main_view, fragment, tag).commit();
    }

    @Override
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() > 0) {
            getFragmentManager().popBackStack();
        } else {
            if (getIntent().getExtras() != null && getIntent().getExtras().getBoolean(Constants.EXTRA_APPLINK_FROM_INTERNAL, false)) {
                super.onBackPressed();
            } else {
                Intent intent = new Intent(this, HomeRouter.getHomeActivityClass());
                this.startActivity(intent);
                this.finish();
            }
        }
    }

    @Override
    public void initDeepLink() {
        if (uriData != null || getIntent().getBooleanExtra(EXTRA_STATE_APP_WEB_VIEW, false)) {
            if (getIntent().getBooleanExtra(DeepLink.IS_DEEP_LINK, false)) {
                Bundle bundle = getIntent().getExtras();
                uriData = removeAmpFromLink(Uri.parse(bundle.getString(APPLINK_URL)));
                presenter.actionGotUrlFromApplink(uriData);
            } else {
                presenter.checkUriLogin(uriData);
                presenter.processDeepLinkAction(DeepLinkActivity.this, uriData);
            }
        }
    }

    private void showProgressService() {
        if (isFinishing())
            return;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1 && isDestroyed())
            return;

        if (progressDialog != null && progressDialog.isProgress()) return;

        progressDialog = new TkpdProgressDialog(this, TkpdProgressDialog.NORMAL_PROGRESS);
        progressDialog.setCancelable(false);
        progressDialog.showDialog();
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
            uriData = removeAmpFromLink(intent.getData());
        }
    }

    private Uri removeAmpFromLink(Uri uriData){
        List<String> path = uriData.getPathSegments();
        if (path!= null && path.size() > 1 && path.get(0).equals(AMP)) {
            return Uri.parse(uriData.toString().replaceFirst(AMP + "/", ""));
        }
        return uriData;
    }

    @Override
    public void navigateToCatalogProductList(String catalogId) {
        getFragmentManager().beginTransaction().replace(R.id.main_view,
                DetailProductRouter.getCatalogDetailListFragment(this, catalogId))
                .addToBackStack(null).commit();
    }

    @Override
    public void deliverCatalogShareData(LinkerData shareData) {

    }
}
