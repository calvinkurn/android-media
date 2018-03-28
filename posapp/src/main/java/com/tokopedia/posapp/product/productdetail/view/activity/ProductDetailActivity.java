package com.tokopedia.posapp.product.productdetail.view.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.airbnb.deeplinkdispatch.DeepLink;
import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.abstraction.common.di.component.BaseAppComponent;
import com.tokopedia.core.app.BasePresenterActivity;
import com.tokopedia.core.base.di.component.HasComponent;
import com.tokopedia.core.router.productdetail.passdata.ProductPass;
import com.tokopedia.posapp.R;
import com.tokopedia.posapp.applink.PosAppLink;
import com.tokopedia.posapp.cart.di.CartComponent;
import com.tokopedia.posapp.cart.di.DaggerCartComponent;
import com.tokopedia.posapp.cart.view.CartMenu;
import com.tokopedia.posapp.cart.view.activity.LocalCartActivity;
import com.tokopedia.posapp.cart.view.presenter.CartMenuPresenter;
import com.tokopedia.posapp.payment.process.ReactInstallmentActivity;
import com.tokopedia.posapp.product.productdetail.view.fragment.ProductDetailFragment;

import javax.inject.Inject;

/**
 * Created by okasurya on 8/8/17.
 */

public class ProductDetailActivity extends BasePresenterActivity
        implements HasComponent, ProductDetailFragment.ProductDetailFragmentListener, CartMenu.View {

    @Inject
    CartMenuPresenter cartMenuPresenter;
    private View vCart;
    private TextView tvNotif;

    @DeepLink(PosAppLink.PRODUCT_INFO)
    public static Intent getIntentFromDeeplink(Context context, Bundle extras) {
        Uri uri = Uri.parse(extras.getString(DeepLink.URI)).buildUpon().build();
        extras.putParcelable(
                ProductDetailFragment.PRODUCT_PASS,
                ProductPass.Builder.aProductPass().setProductId(uri.getPathSegments().get(0)).build()
        );

        return new Intent(context, ProductDetailActivity.class)
                .setData(uri)
                .putExtras(extras);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initInjector();
    }

    @Override
    public Object getComponent() {
        return getApplicationComponent();
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
        setupToolbar();
        getSupportActionBar().setTitle(R.string.title_product_detail);

        Bundle bundle = new Bundle();
        if (getIntent().getExtras() != null) {
            bundle.putAll(getIntent().getExtras());
        }

        ProductDetailFragment fragment = ProductDetailFragment.newInstance(bundle);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        if (getSupportFragmentManager().findFragmentByTag(
                ProductDetailFragment.class.getSimpleName()) == null) {
            fragmentTransaction.replace(R.id.container,
                    fragment,
                    fragment.getClass().getSimpleName());
        } else {
            fragmentTransaction.replace(R.id.container,
                    getSupportFragmentManager().findFragmentByTag(
                            ProductDetailFragment.class.getSimpleName()));
        }

        fragmentTransaction.commit();
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
            Intent intent = new Intent(this, ReactInstallmentActivity.class);
            startActivity(intent);
            return true;
        } else if (item.getItemId() == R.id.action_cart) {
            startActivity(new Intent(this, LocalCartActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onAddToCart() {
        cartMenuPresenter.checkCartItem();
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

    private void initInjector() {
        BaseAppComponent appComponent = ((BaseMainApplication) this.getApplicationContext()).getBaseAppComponent();
        CartComponent cartComponent = DaggerCartComponent.builder().baseAppComponent(appComponent).build();
        cartComponent.inject(this);
        cartMenuPresenter.attachView(this);
    }
}
