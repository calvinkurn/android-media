package com.tokopedia.posapp.product.productlist.view;

import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.airbnb.deeplinkdispatch.DeepLink;
import com.tkpd.library.utils.LocalCacheHandler;
import com.tokopedia.core.drawer2.di.DrawerInjector;
import com.tokopedia.core.drawer2.view.DrawerHelper;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.posapp.R;
import com.tokopedia.posapp.base.activity.ReactDrawerPresenterActivity;
import com.tokopedia.posapp.applink.PosAppLink;
import com.tokopedia.posapp.cart.view.LocalCartActivity;
import com.tokopedia.posapp.payment.process.ReactInstallmentActivity;
import com.tokopedia.posapp.product.productlist.view.fragment.ReactProductListFragment;

/**
 * Created by okasurya on 8/24/17.
 */

public class ProductListActivity extends ReactDrawerPresenterActivity {
    LocalCacheHandler drawerCache;
    DrawerHelper drawerHelper;

    @DeepLink(PosAppLink.PRODUCT_LIST)
    public static Intent getApplinkIntent(Context context, Bundle extras) {
        Uri uri = Uri.parse(extras.getString(DeepLink.URI)).buildUpon().build();
        Intent intent = new Intent(context, ProductListActivity.class)
                .setData(uri);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        return intent;
    }

    public static Intent newTopIntent(Context context) {
        Intent intent = new Intent(context, ProductListActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initDrawer();
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
        ReactProductListFragment fragment = ReactProductListFragment.newInstance(SessionHandler.getShopID(this), "etalase");
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        if (getFragmentManager().findFragmentById(R.id.container) == null) {
            fragmentTransaction.add(R.id.container, fragment, fragment.getClass().getSimpleName());
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
        getMenuInflater().inflate(R.menu.menu_product_main, menu);
        final Menu m = menu;
        final MenuItem item = menu.findItem(R.id.action_cart);
        vCart = item.getActionView();
        vCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                m.performIdentifierAction(item.getItemId(), 0);
            }
        });
        initCartInjector();
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.action_credit_card) {
            startActivity(new Intent(this, ReactInstallmentActivity.class));
            return true;
        } else if(item.getItemId() == R.id.action_cart) {
            startActivity(new Intent(this, LocalCartActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initDrawer() {
        sessionHandler = new SessionHandler(this);
        drawerCache = new LocalCacheHandler(this, DrawerHelper.DRAWER_CACHE);
        drawerHelper = DrawerInjector.getDrawerHelper(this, sessionHandler, drawerCache);
        drawerHelper.initDrawer(this);
        drawerHelper.setEnabled(true);
        drawerHelper.setSelectedPosition(0);
    }
}
