package com.tokopedia.discovery.catalog.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.airbnb.deeplinkdispatch.DeepLink;
import com.tokopedia.core2.R;
import com.tokopedia.core.analytics.AppScreen;
import com.tokopedia.core.app.BasePresenterActivity;
import com.tokopedia.core.discovery.catalog.listener.ICatalogActionFragment;
import com.tokopedia.core.gcm.Constants;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.core.model.share.ShareData;
import com.tokopedia.core.router.SellerAppRouter;
import com.tokopedia.core.router.discovery.DetailProductRouter;
import com.tokopedia.core.router.home.HomeRouter;
import com.tokopedia.core.share.DefaultShare;
import com.tokopedia.core.util.GlobalConfig;
import com.tokopedia.discovery.catalog.fragment.CatalogDetailFragment;
import com.tokopedia.discovery.catalog.fragment.CatalogDetailListFragment;

/**
 * @author anggaprasetiyo on 10/17/16.
 */

public class CatalogDetailActivity extends BasePresenterActivity implements ICatalogActionFragment {
    public static final String EXTRA_CATALOG_ID = "EXTRA_CATALOG_ID";
    private static final String STATE_CATALOG_SHARE_DATA = "STATE_CATALOG_SHARE_DATA";
    private static final String TAG_FRAGMENT_CATALOG_DETAIL = "TAG_FRAGMENT_CATALOG_DETAIL";

    String catalogId;
    private ShareData shareData;


    @DeepLink(Constants.Applinks.DISCOVERY_CATALOG)
    public static Intent getCallingApplinkCatalogIntent(Context context, Bundle bundle) {
        Intent intent = createIntent(context, bundle.getString(EXTRA_CATALOG_ID));
        return intent
                .putExtras(bundle);
    }

    public static Intent createIntent(Context context, String catalogId) {
        Intent intent = new Intent(context, CatalogDetailActivity.class);
        intent.putExtra(EXTRA_CATALOG_ID, catalogId);
        return intent;
    }

    @Override
    public String getScreenName() {
        return AppScreen.SCREEN_CATALOG;
    }

    @Override
    protected void setupURIPass(Uri data) {
    }

    @Override
    protected void setupBundlePass(Bundle extras) {
        catalogId = extras.getString(EXTRA_CATALOG_ID, "");
    }

    @Override
    protected void initialPresenter() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_catalog;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void setViewListener() {
        if (getFragmentManager().findFragmentByTag(TAG_FRAGMENT_CATALOG_DETAIL) == null) {
            getFragmentManager().beginTransaction().add(R.id.activity_container,
                    CatalogDetailFragment.newInstance(catalogId), TAG_FRAGMENT_CATALOG_DETAIL)
                    .commit();
        }
    }

    @Override
    protected void initVar() {

    }

    @Override
    protected void setActionVar() {

    }

    @Override
    public void navigateToCatalogProductList(String catalogId) {
        getFragmentManager().beginTransaction().replace(R.id.activity_container,
                CatalogDetailListFragment.newInstance(catalogId)).addToBackStack(null).commit();
    }

    @Override
    public void deliverCatalogShareData(ShareData shareData) {
        this.shareData = shareData;
    }

    @Override
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() > 0) {
            getFragmentManager().popBackStack();
        } if (isTaskRoot() && GlobalConfig.isSellerApp()) {
            startActivity(SellerAppRouter.getSellerHomeActivity(this));
            this.finish();
        } else if (isTaskRoot()) {
            startActivity(HomeRouter.getHomeActivity(this));
            this.finish();
        } else {
            this.finish();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(STATE_CATALOG_SHARE_DATA, shareData);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        shareData = savedInstanceState.getParcelable(STATE_CATALOG_SHARE_DATA);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getIntent().getBooleanExtra(DetailProductRouter.EXTRA_ACTIVITY_PAUSED, false)) {
            moveTaskToBack(true);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(com.tokopedia.discovery.R.menu.menu_catalog_detail, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == com.tokopedia.discovery.R.id.action_share_prod) {
            if (shareData != null)
                new DefaultShare(this, shareData).show();
            else NetworkErrorHelper.showSnackbar(this, "Data katalog belum tersedia");
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected boolean isLightToolbarThemes() {
        return true;
    }

}
