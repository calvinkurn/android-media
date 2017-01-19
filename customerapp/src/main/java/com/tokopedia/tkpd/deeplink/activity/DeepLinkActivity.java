package com.tokopedia.tkpd.deeplink.activity;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.MenuItem;

import com.tkpd.library.utils.CommonUtils;
import com.tokopedia.core.analytics.AppEventTracking;
import com.tokopedia.core.analytics.AppScreen;
import com.tokopedia.core.analytics.TrackingUtils;
import com.tokopedia.core.app.BasePresenterActivity;
import com.tokopedia.core.discovery.catalog.listener.ICatalogActionFragment;
import com.tokopedia.core.product.activity.ProductInfoActivity;
import com.tokopedia.core.product.fragment.ProductDetailFragment;
import com.tokopedia.core.product.model.productdetail.ProductDetailData;
import com.tokopedia.core.product.model.share.ShareData;
import com.tokopedia.core.router.discovery.DetailProductRouter;
import com.tokopedia.core.router.home.HomeRouter;
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
 * @author  by Angga.Prasetiyo on 14/12/2015.
 * modified Alvarisi
 */
public class DeepLinkActivity extends BasePresenterActivity<DeepLinkPresenter> implements
        DeepLinkView, DeepLinkWebViewHandleListener,
        ProductDetailFragment.OnFragmentInteractionListener,
        FragmentGeneralWebView.OnFragmentInteractionListener, ICatalogActionFragment {

    private static final String TAG = DeepLinkActivity.class.getSimpleName();
    private Uri uriData;

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

    @Override
    protected void onFinishFetechedDepartment() {
        super.onFinishFetechedDepartment();
        Log.i("GAv4 HADES TAG", "DO SOMETHING AFTER FINISH");
        dissmisProgressService();
        if (uriData != null) presenter.processDeepLinkAction(uriData);
    }

    private void initDeepLink() {
        if (uriData != null) {
            if (presenter.isLandingPageWebView(uriData)) {
                CommonUtils.dumper("GAv4 Escape HADES webview");
                presenter.processDeepLinkAction(uriData);
            } else {
                if (verifyFetchDepartment() || HadesService.getIsHadesRunning()) {
                    CommonUtils.dumper("GAv4 Entering HADES");
                    showProgressService();
                }else{
                    CommonUtils.dumper("GAv4 Escape HADES non webview");
                    presenter.processDeepLinkAction(uriData);
                }
            }
        } else  {
            if (verifyFetchDepartment() || HadesService.getIsHadesRunning()) {
                CommonUtils.dumper("GAv4 Entering HADES null Uri");
                showProgressService();
            }
        }
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
        sendNotifLocalyticsCallback(intent);
    }

    private void sendNotifLocalyticsCallback(Intent intent) {
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            if (bundle.containsKey(AppEventTracking.LOCA.NOTIFICATION_BUNDLE)){
                TrackingUtils.eventLocaNotificationCallback(getIntent());
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
}
