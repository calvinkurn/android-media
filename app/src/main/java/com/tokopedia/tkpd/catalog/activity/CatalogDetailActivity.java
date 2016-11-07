package com.tokopedia.tkpd.catalog.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.tokopedia.tkpd.R;
import com.tokopedia.tkpd.R2;
import com.tokopedia.tkpd.app.BasePresenterActivity;
import com.tokopedia.tkpd.catalog.fragment.CatalogDetailFragment;
import com.tokopedia.tkpd.catalog.fragment.CatalogDetailListFragment;
import com.tokopedia.tkpd.catalog.listener.ICatalogActionFragment;
import com.tokopedia.tkpd.network.NetworkErrorHelper;
import com.tokopedia.tkpd.product.model.share.ShareData;
import com.tokopedia.tkpd.share.ShareActivity;

/**
 * @author anggaprasetiyo on 10/17/16.
 */

public class CatalogDetailActivity extends BasePresenterActivity implements ICatalogActionFragment {
    public static final String EXTRA_CATALOG_ID = "EXTRA_CATALOG_ID";
    private static final String STATE_CATALOG_SHARE_DATA = "STATE_CATALOG_SHARE_DATA";

    String catalogId;
    private ShareData shareData;

    public static Intent createIntent(Context context, String catalogId) {
        Intent intent = new Intent(context, CatalogDetailActivity.class);
        intent.putExtra(EXTRA_CATALOG_ID, catalogId);
        return intent;
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
        getFragmentManager().beginTransaction().add(R.id.activity_container,
                CatalogDetailFragment.newInstance(catalogId)).commit();
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
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.shop_info, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R2.id.action_share_prod:
                if (shareData != null) startActivity(ShareActivity.createIntent(this, shareData));
                else NetworkErrorHelper.showSnackbar(this, "Data katalog belum tersedia");
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
