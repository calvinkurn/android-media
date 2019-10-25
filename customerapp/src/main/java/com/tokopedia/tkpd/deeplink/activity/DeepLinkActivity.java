package com.tokopedia.tkpd.deeplink.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.TaskStackBuilder;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.airbnb.deeplinkdispatch.DeepLink;
import com.tkpd.library.ui.utilities.TkpdProgressDialog;
import com.tkpd.library.utils.CommonUtils;
import com.tokopedia.applink.DeeplinkMapper;
import com.tokopedia.applink.RouteManager;
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace;
import com.tokopedia.core.analytics.AppScreen;
import com.tokopedia.core.analytics.TrackingUtils;
import com.tokopedia.core.app.BasePresenterActivity;
import com.tokopedia.core.app.TkpdCoreRouter;
import com.tokopedia.core.discovery.catalog.listener.ICatalogActionFragment;
import com.tokopedia.core.gcm.Constants;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.core.product.intentservice.ProductInfoIntentService;
import com.tokopedia.core.product.intentservice.ProductInfoResultReceiver;
import com.tokopedia.core.product.listener.DetailFragmentInteractionListener;
import com.tokopedia.core.product.listener.FragmentDetailParent;
import com.tokopedia.core.product.listener.ReportFragmentListener;
import com.tokopedia.core.product.model.productdetail.ProductDetailData;
import com.tokopedia.core.router.InboxRouter;
import com.tokopedia.core.router.discovery.DetailProductRouter;
import com.tokopedia.core.router.home.HomeRouter;
import com.tokopedia.core.router.productdetail.PdpRouter;
import com.tokopedia.core.router.productdetail.passdata.ProductPass;
import com.tokopedia.core.share.fragment.ProductShareFragment;
import com.tokopedia.core.webview.listener.DeepLinkWebViewHandleListener;
import com.tokopedia.linker.model.LinkerData;
import com.tokopedia.tkpd.R;
import com.tokopedia.tkpd.deeplink.listener.DeepLinkView;
import com.tokopedia.tkpd.deeplink.presenter.DeepLinkPresenter;
import com.tokopedia.tkpd.deeplink.presenter.DeepLinkPresenterImpl;
import com.tokopedia.webview.BaseSessionWebViewFragment;

import static com.raizlabs.android.dbflow.config.FlowManager.getContext;

/**
 * @author by Angga.Prasetiyo on 14/12/2015.
 * modified Alvarisi
 */
public class DeepLinkActivity extends BasePresenterActivity<DeepLinkPresenter> implements
        DeepLinkView, DeepLinkWebViewHandleListener,
        DetailFragmentInteractionListener,
        ReportFragmentListener,
        ProductInfoResultReceiver.Receiver,
        ICatalogActionFragment {

    private TkpdProgressDialog progressDialog;

    private static final String TAG = DeepLinkActivity.class.getSimpleName();
    private Uri uriData;
    private static final String EXTRA_STATE_APP_WEB_VIEW = "EXTRA_STATE_APP_WEB_VIEW";
    private static final String APPLINK_URL = "url";
    private Bundle mExtras;
    private boolean isNeedToUseToolbarWithOptions;
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
        this.uriData = data;
    }

    @Override
    protected void setupBundlePass(Bundle extras) {
        mExtras = extras;
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
    public void shareProductInfo(@NonNull LinkerData shareData) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.main_view,
                        ProductShareFragment.newInstance(shareData, false),
                        ProductShareFragment.class.getSimpleName())
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void hideActionBar() {
        getSupportActionBar().hide();
    }

    @Override
    public void actionChangeToolbarWithBackToNative() {
        isNeedToUseToolbarWithOptions = true;
        getSupportActionBar().setHomeAsUpIndicator(com.tokopedia.core2.R.drawable.ic_webview_back_button);
        toolbar.setBackgroundResource(com.tokopedia.core2.R.color.white);
        toolbar.setTitleTextAppearance(this, com.tokopedia.core2.R.style.WebViewToolbarText);
        setSupportActionBar(toolbar);
        invalidateOptionsMenu();
    }

    @Override
    public void goToActivity(Class<? extends Activity> activityClass, Bundle bundle) {
        TaskStackBuilder taskStackBuilder = TaskStackBuilder.create(this);

        if (getApplicationContext() instanceof TkpdCoreRouter) {
            taskStackBuilder.addNextIntent(
                    ((TkpdCoreRouter) getApplicationContext()).getHomeIntent(this)
            );
        }

        taskStackBuilder.addNextIntent(
                new Intent(this, activityClass).putExtras(bundle)
        );

        taskStackBuilder.startActivities();
        finish();
    }

    @Override
    public void goToPage(Intent destination) {
        TaskStackBuilder taskStackBuilder = TaskStackBuilder.create(this);

        if (getApplicationContext() instanceof TkpdCoreRouter) {
            taskStackBuilder.addNextIntent(
                    ((TkpdCoreRouter) getApplicationContext()).getHomeIntent(this)
            );
        }

        taskStackBuilder.addNextIntent(
                destination
        );

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
    public boolean onCreateOptionsMenu(Menu menu) {
        if (isNeedToUseToolbarWithOptions) {
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(com.tokopedia.core2.R.menu.menu_web_view, menu);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onProductDetailLoaded(@NonNull ProductDetailData productData) {

    }

    @Override
    public void onNullResponseData(ProductPass productPass) {

    }

    @Override
    public void jumpOtherProductDetail(ProductPass productPass) {
        if (getApplication() instanceof PdpRouter) {
            RouteManager.route(getContext(), ApplinkConstInternalMarketplace.PRODUCT_DETAIL, productPass.getProductId());
        }
    }

    @Override
    public void inflateFragment(Fragment fragment, String tag) {
        getFragmentManager().beginTransaction().add(R.id.main_view, fragment, tag).commit();
    }

    @Override
    public void inflateFragmentV4(android.support.v4.app.Fragment fragment, String tag) {
        getSupportFragmentManager().beginTransaction().replace(R.id.main_view, fragment, tag).commit();
    }

    @Override
    public void replaceFragment(Fragment fragment, String tag) {
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.main_view, fragment, tag);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        } else if (id == com.tokopedia.core2.R.id.menu_home) {
            onBackPressed();
            return true;
        } else if (id == com.tokopedia.core2.R.id.menu_help) {
            Intent intent = InboxRouter.getContactUsActivityIntent(this);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void catchToWebView(String url) {
        actionChangeToolbarWithBackToNative();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.main_view, BaseSessionWebViewFragment.newInstance(url))
                .commit();
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
                uriData = Uri.parse(bundle.getString(APPLINK_URL));
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
        CommonUtils.dumper("FCM onNewIntent " + intent.getData());
        if (intent.getData() != null) {
            uriData = intent.getData();
        }
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

    @Override
    public void onReportProductSubmited(Bundle bundle) {
        ProductInfoResultReceiver mReceiver = new ProductInfoResultReceiver(new Handler());
        mReceiver.setReceiver(this);
        ProductInfoIntentService.startAction(this, bundle, mReceiver);
    }

    @Override
    public void onReceiveResult(int resultCode, Bundle resultData) {
        Fragment fragment = getFragmentManager().findFragmentById(R.id.main_view);
        if (fragment != null && fragment instanceof FragmentDetailParent) {
            switch (resultCode) {
                case ProductInfoIntentService.STATUS_SUCCESS_REPORT_PRODUCT:
                    onReceiveResultSuccess(fragment, resultData, resultCode);
                    break;
                case ProductInfoIntentService.STATUS_ERROR_REPORT_PRODUCT:
                    onReceiveResultError(fragment, resultData, resultCode);
                    break;
            }
        }
    }

    private void onReceiveResultError(Fragment fragment, Bundle resultData, int resultCode) {
        ((FragmentDetailParent) fragment).onErrorAction(resultData, resultCode);
    }

    private void onReceiveResultSuccess(Fragment fragment, Bundle resultData, int resultCode) {
        ((FragmentDetailParent) fragment).onSuccessAction(resultData, resultCode);
    }

}
