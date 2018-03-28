package com.tokopedia.posapp.product.productlist.view.activity;

import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.airbnb.deeplinkdispatch.DeepLink;
import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.abstraction.common.di.component.BaseAppComponent;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.posapp.R;
import com.tokopedia.posapp.applink.PosAppLink;
import com.tokopedia.posapp.base.activity.ReactDrawerPresenterActivity;
import com.tokopedia.posapp.cart.di.CartComponent;
import com.tokopedia.posapp.cart.di.DaggerCartComponent;
import com.tokopedia.posapp.cart.view.CartMenu;
import com.tokopedia.posapp.cart.view.activity.LocalCartActivity;
import com.tokopedia.posapp.cart.view.presenter.CartMenuPresenter;
import com.tokopedia.posapp.payment.process.ReactInstallmentActivity;
import com.tokopedia.posapp.product.productlist.view.fragment.ReactProductListFragment;

import javax.inject.Inject;

/**
 * Created by okasurya on 8/24/17.
 */

public class ProductListActivity extends ReactDrawerPresenterActivity implements CartMenu.View {
    private View vCart;
    private TextView tvNotif;

    @Inject
    CartMenuPresenter cartMenuPresenter;

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
        initInjector();
    }

    @Override
    protected void onResume() {
        super.onResume();
        cartMenuPresenter.checkCartItem();
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
        tvNotif = vCart.findViewById(R.id.toggle_notif);
        cartMenuPresenter.checkCartItem();
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_credit_card) {
            startActivity(new Intent(this, ReactInstallmentActivity.class));
            return true;
        } else if (item.getItemId() == R.id.action_cart) {
            startActivity(new Intent(this, LocalCartActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCartFilled(int cartCount) {
        tvNotif.setVisibility(View.VISIBLE);
        tvNotif.setText(Integer.toString(cartCount));
    }

    @Override
    public void onCartEmpty() {
        tvNotif.setVisibility(View.GONE);
    }

    protected void initInjector() {
        BaseAppComponent appComponent = ((BaseMainApplication) this.getApplicationContext()).getBaseAppComponent();
        CartComponent cartComponent = DaggerCartComponent.builder().baseAppComponent(appComponent).build();
        cartComponent.inject(this);
        cartMenuPresenter.attachView(this);
    }
}
