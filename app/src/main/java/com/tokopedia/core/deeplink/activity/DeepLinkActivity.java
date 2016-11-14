package com.tokopedia.core.deeplink.activity;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.MenuItem;

import com.tkpd.library.utils.DownloadResultReceiver;
import com.tokopedia.core.R;
import com.tokopedia.core.analytics.AppEventTracking;
import com.tokopedia.core.analytics.AppScreen;
import com.tokopedia.core.app.BasePresenterActivity;
import com.tokopedia.core.app.TActivity;
import com.tokopedia.core.router.DiscoveryRouter;
import com.tokopedia.discovery.catalog.listener.ICatalogActionFragment;
import com.tokopedia.core.deeplink.listener.DeepLinkView;
import com.tokopedia.core.deeplink.presenter.DeepLinkPresenter;
import com.tokopedia.core.deeplink.presenter.DeepLinkPresenterImpl;
import com.tokopedia.core.home.ParentIndexHome;
import com.tokopedia.core.product.activity.ProductInfoActivity;
import com.tokopedia.core.product.fragment.ProductDetailFragment;
import com.tokopedia.core.product.model.passdata.ProductPass;
import com.tokopedia.core.product.model.productdetail.ProductDetailData;
import com.tokopedia.core.product.model.share.ShareData;
import com.tokopedia.core.service.DownloadService;
import com.tokopedia.core.service.HadesService;
import com.tokopedia.core.analytics.TrackingUtils;
import com.tokopedia.core.share.fragment.ProductShareFragment;
import com.tokopedia.core.webview.fragment.FragmentGeneralWebView;
import com.tokopedia.core.webview.listener.DeepLinkWebViewHandleListener;

/**
 * Created by Angga.Prasetiyo on 14/12/2015.
 */
public class DeepLinkActivity extends BasePresenterActivity<DeepLinkPresenter> implements
        DeepLinkView, DeepLinkWebViewHandleListener,
        ProductDetailFragment.OnFragmentInteractionListener,
        FragmentGeneralWebView.OnFragmentInteractionListener, ICatalogActionFragment {
    /**
     * [START] This constant for downloading department id
     * {@link TActivity#verifyFetchDepartment()}
     */
    public static final int STD_DEEP_LINK = 28_172_182;

    private static final String TAG = DeepLinkActivity.class.getSimpleName();
    private Uri uriData;
    private DownloadResultReceiver mReceiver;

    @Override
    protected String getScreenName() {
        return AppScreen.SCREEN_DEEP_LINK;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initDeepLink();
        isAllowFetchDepartmentView = true;
        //AppsFlyerLib.getInstance().sendDeepLinkData(this);
    }

    @Override
    protected void setupURIPass(Uri data) {
        this.uriData = data;
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
    public void onProductDetailLoaded(@NonNull ProductDetailData productData) {

    }

    @Override
    public void onNullResponseData(ProductPass productPass) {

    }

    @Override
    public void jumpOtherProductDetail(ProductPass productPass) {
        startActivity(ProductInfoActivity.createInstance(this, productPass));
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

    public void getHotList(int page, int perpage) {
        Bundle bundle = new Bundle();
        bundle.putInt(DownloadService.PAGE_KEY, page);
        bundle.putInt(DownloadService.PER_PAGE_KEY, perpage);
        DownloadService.startDownload(this, mReceiver, bundle, DownloadService.HOTLIST);
    }

    @Override
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() > 0) {
            getFragmentManager().popBackStack();
        } else {
            Intent intent = new Intent(this, ParentIndexHome.class);
            this.startActivity(intent);
            this.finish();
        }
    }

    @Override
    protected void onFinishFetechedDepartment() {
        super.onFinishFetechedDepartment();
        Log.i("HADES TAG", "DO SOMETHING AFTER FINISH");
        dissmisProgressService();
        if (uriData != null) presenter.processDeepLinkAction(uriData);
    }

    private void initDeepLink() {
        Log.i("HADES TAG", "HADES RUNNING??? " + HadesService.getIsHadesRunning());
        if (verifyFetchDepartment() || HadesService.getIsHadesRunning()) {
            showProgressService();
        } else {
            if (uriData != null) presenter.processDeepLinkAction(uriData);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        //No call for super(). Bug on API Level > 11.
    }

    @Override
    public void onResume() {
        super.onResume();
        isAllowFetchDepartmentView = true;
        sendNotifLocalyticsCallback();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
    }

    private void sendNotifLocalyticsCallback() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            if (bundle.containsKey(AppEventTracking.LOCA.NOTIFICATION_BUNDLE)){
                TrackingUtils.eventLocaNotificationCallback(getIntent());
            }
        }
    }

    @Override
    public void navigateToCatalogProductList(String catalogId) {
        getFragmentManager().beginTransaction().replace(R.id.main_view,
                DiscoveryRouter.getCatalogDetailListFragment(this, catalogId))
                .addToBackStack(null).commit();
    }

    @Override
    public void deliverCatalogShareData(ShareData shareData) {

    }
}
