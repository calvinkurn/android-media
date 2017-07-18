package com.tokopedia.tkpd.deeplink.activity;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.view.MenuItem;

import com.airbnb.deeplinkdispatch.DeepLink;
import com.localytics.android.Localytics;
import com.tkpd.library.ui.utilities.TkpdProgressDialog;
import com.tkpd.library.utils.CommonUtils;
import com.tokopedia.core.analytics.AppEventTracking;
import com.tokopedia.core.analytics.AppScreen;
import com.tokopedia.core.analytics.TrackingUtils;
import com.tokopedia.core.app.BasePresenterActivity;
import com.tokopedia.core.discovery.catalog.listener.ICatalogActionFragment;
import com.tokopedia.core.gcm.Constants;
import com.tokopedia.core.product.intentservice.ProductInfoIntentService;
import com.tokopedia.core.product.intentservice.ProductInfoResultReceiver;
import com.tokopedia.core.product.listener.DetailFragmentInteractionListener;
import com.tokopedia.core.product.listener.FragmentDetailParent;
import com.tokopedia.core.product.listener.ReportFragmentListener;
import com.tokopedia.core.product.model.productdetail.ProductDetailData;
import com.tokopedia.core.product.model.share.ShareData;
import com.tokopedia.core.router.discovery.DetailProductRouter;
import com.tokopedia.core.router.home.HomeRouter;
import com.tokopedia.core.router.productdetail.PdpRouter;
import com.tokopedia.core.router.productdetail.passdata.ProductPass;
import com.tokopedia.core.service.HadesService;
import com.tokopedia.core.share.fragment.ProductShareFragment;
import com.tokopedia.core.webview.fragment.FragmentGeneralWebView;
import com.tokopedia.core.webview.listener.DeepLinkWebViewHandleListener;
import com.tokopedia.tkpd.R;
import com.tokopedia.tkpd.deeplink.listener.DeepLinkView;
import com.tokopedia.tkpd.deeplink.presenter.DeepLinkPresenter;
import com.tokopedia.tkpd.deeplink.presenter.DeepLinkPresenterImpl;

/**
 * @author by Angga.Prasetiyo on 14/12/2015.
 *         modified Alvarisi
 */
public class DeepLinkActivity extends BasePresenterActivity<DeepLinkPresenter> implements
        DeepLinkView, DeepLinkWebViewHandleListener,
        DetailFragmentInteractionListener,
        FragmentGeneralWebView.OnFragmentInteractionListener,
        ReportFragmentListener,
        ProductInfoResultReceiver.Receiver,
        ICatalogActionFragment {

    private TkpdProgressDialog progressDialog;

    private static final String TAG = DeepLinkActivity.class.getSimpleName();
    private Uri uriData;
    private static final String EXTRA_STATE_APP_WEB_VIEW = "EXTRA_STATE_APP_WEB_VIEW";
    private static final String APPLINK_URL = "url";
    private Bundle mExtras;

    @DeepLink(Constants.Applinks.WEBVIEW)
    public static Intent getCallingIntent(Context context, Bundle extras) {
        Uri.Builder uri = Uri.parse(extras.getString(DeepLink.URI)).buildUpon();
        return new Intent(context, DeepLinkActivity.class)
                .setData(uri.build())
                .putExtra(EXTRA_STATE_APP_WEB_VIEW, true)
                .putExtras(extras);
    }

    @Override
    public String getScreenName() {
        return AppScreen.SCREEN_DEEP_LINK;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initDeepLink();
        isAllowFetchDepartmentView = true;
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
    public void shareProductInfo(@NonNull ShareData shareData) {
        replaceFragment(ProductShareFragment.newInstance(shareData),
                ProductShareFragment.class.getSimpleName());
    }

    @Override
    public void hideActionBar() {
        getSupportActionBar().hide();
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
            ((PdpRouter) getApplication()).goToProductDetail(this, productPass);
        }
    }

    @Override
    public void onWebViewSuccessLoad() {
    }

    @Override
    public void onWebViewErrorLoad() {
    }

    @Override
    public void onWebViewProgressLoad() {
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
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void catchToWebView(String url) {
        getFragmentManager().beginTransaction()
                .replace(R.id.main_view, FragmentGeneralWebView.createInstance(url))
                .commit();
    }

    @Override
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() > 0) {
            getFragmentManager().popBackStack();
        } else {
            Intent intent = new Intent(this, HomeRouter.getHomeActivityClass());
            this.startActivity(intent);
            this.finish();
        }
    }

    private void initDeepLink() {
        if (uriData != null || getIntent().getBooleanExtra(EXTRA_STATE_APP_WEB_VIEW, false)) {
            if (getIntent().getBooleanExtra(DeepLink.IS_DEEP_LINK, false)) {
                Bundle bundle = getIntent().getExtras();
                uriData = Uri.parse(bundle.getString(APPLINK_URL));
                presenter.actionGotUrlFromApplink(uriData);
            } else {
                presenter.checkUriLogin(uriData);
                if (presenter.isLandingPageWebView(uriData)) {
                    CommonUtils.dumper("GAv4 Escape HADES webview");
                    presenter.processDeepLinkAction(uriData);
                } else {
                    if (verifyFetchDepartment() || HadesService.getIsHadesRunning()) {
                        CommonUtils.dumper("GAv4 Entering HADES");
                        showProgressService();
                    } else {
                        CommonUtils.dumper("GAv4 Escape HADES non webview");
                        presenter.processDeepLinkAction(uriData);
                    }
                }
            }

        } else {
            if (verifyFetchDepartment() || HadesService.getIsHadesRunning()) {
                CommonUtils.dumper("GAv4 Entering HADES null Uri");
                showProgressService();
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

    @Override
    protected void onSaveInstanceState(Bundle outState) {
    }

    @Override
    public void onResume() {
        super.onResume();
        isAllowFetchDepartmentView = true;
        sendNotifLocalyticsCallback(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        CommonUtils.dumper("FCM onNewIntent "+intent.getData());
        if(intent.getData()!=null)
        {
            uriData = intent.getData();
        }
        sendNotifLocalyticsCallback(intent);
    }

    private void sendNotifLocalyticsCallback(Intent intent) {
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            if (bundle.containsKey(AppEventTracking.LOCA.NOTIFICATION_BUNDLE)) {
                TrackingUtils.eventLocaNotificationCallback(getIntent());
                Localytics.onNewIntent(this, intent);
            }
        }
    }

    @Override
    public void navigateToCatalogProductList(String catalogId) {
        getFragmentManager().beginTransaction().replace(R.id.main_view,
                DetailProductRouter.getCatalogDetailListFragment(this, catalogId))
                .addToBackStack(null).commit();
    }

    @Override
    public void deliverCatalogShareData(ShareData shareData) {

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
