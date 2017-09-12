package com.tokopedia.posapp.view.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.view.MenuItem;

import com.tkpd.library.utils.LocalCacheHandler;
import com.tokopedia.core.app.BasePresenterActivity;
import com.tokopedia.core.app.DrawerPresenterActivity;
import com.tokopedia.core.drawer2.di.DrawerInjector;
import com.tokopedia.core.drawer2.view.DrawerHelper;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.posapp.R;
import com.tokopedia.posapp.view.fragment.ProductListFragment;

/**
 * Created by okasurya on 8/24/17.
 */

public class ProductListActivity extends DrawerPresenterActivity {
    LocalCacheHandler drawerCache;
    DrawerHelper drawerHelper;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        sessionHandler = new SessionHandler(this);
        drawerCache = new LocalCacheHandler(this, DrawerHelper.DRAWER_CACHE);
        drawerHelper = DrawerInjector.getDrawerHelper(this, sessionHandler, drawerCache);
        drawerHelper.initDrawer(this);
        drawerHelper.setEnabled(true);
        drawerHelper.setSelectedPosition(0);
    }

    @Override
    protected void setupURIPass(Uri data) {

    }

    @Override
    protected void setupBundlePass(Bundle extras) {

    }

    @Override
    protected void initialPresenter() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_simple_fragment;
    }

    @Override
    protected void initView() {
        ProductListFragment fragment = ProductListFragment.newInstance(SessionHandler.getShopID(this), "etalase");

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        if (getSupportFragmentManager().findFragmentByTag(
                ProductListFragment.class.getSimpleName()) == null) {
            fragmentTransaction.replace(R.id.container,
                    fragment,
                    fragment.getClass().getSimpleName());
        } else {
            fragmentTransaction.replace(R.id.container,
                    getSupportFragmentManager().findFragmentByTag(
                            ProductListFragment.class.getSimpleName()));
        }

        fragmentTransaction.commit();
    }

    @Override
    protected int setDrawerPosition() {
        return 0;
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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_product_list, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.action_credit_card) {
            startActivity(new Intent(this, SelectBankActivity.class));
            return true;
        } else if(item.getItemId() == R.id.action_cart) {
            startActivity(new Intent(this, LocalCartActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
