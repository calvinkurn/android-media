package com.tokopedia.gm.subscribe.view.activity;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.tokopedia.core.analytics.AppScreen;
import com.tokopedia.core.app.BasePresenterActivity;
import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.core.base.di.component.HasComponent;
import com.tokopedia.payment.activity.TopPayActivity;
import com.tokopedia.payment.model.PaymentPassData;
import com.tokopedia.gm.R;
import com.tokopedia.gm.subscribe.view.fragment.GmCheckoutFragment;
import com.tokopedia.gm.subscribe.view.fragment.GmCheckoutFragmentCallback;

/**
 * @author sebastianuskh on 1/31/17.
 */

public class GmCheckoutActivity extends BasePresenterActivity implements GmCheckoutFragmentCallback, HasComponent<AppComponent> {

    public static final String CURRENT_SELECTED_PRODUCT = "CURRENT_SELECTED_PRODUCT";
    private static final int CHANGE_SELECTED_PRODUCT = 100;
    private static final int SELECT_AUTO_SUBSCRIBE_PRODUCT = 200;
    private static final int CHANGE_AUTO_SUBSCRIBE_PRODUCT = 300;
    private static final int REQUEST_CODE_PAYMENT_GM = 1;
    private int currentSelected;
    private FragmentManager fragmentManager;

    public static Intent processSelectedProduct(Context context, int selected) {
        Intent intent = new Intent(context, GmCheckoutActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt(CURRENT_SELECTED_PRODUCT, selected);
        intent.putExtras(bundle);
        return intent;
    }

    @Override
    protected void setupURIPass(Uri uri) {

    }

    @Override
    protected void setupBundlePass(Bundle bundle) {
        currentSelected = bundle.getInt(CURRENT_SELECTED_PRODUCT);
    }

    @Override
    protected void initialPresenter() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_gm_subscribe;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void setViewListener() {

    }

    @Override
    protected void initVar() {
        fragmentManager = getFragmentManager();
    }

    @Override
    protected void setActionVar() {
        initCheckoutFragment();
    }


    @Override
    public String getScreenName() {
        return AppScreen.SCREEN_GM_SUBSCRIBE_CHECKOUT;
    }

    @Override
    public void changeCurrentSelected(Integer selectedProduct) {
        Intent intent = GmProductActivity.changeProductSelected(this, selectedProduct);
        startActivityForResult(intent, CHANGE_SELECTED_PRODUCT);
    }

    @Override
    public void selectAutoSubscribePackageFirstTime() {
        Intent intent = GmProductActivity.selectAutoProductFirstTime(this);
        startActivityForResult(intent, SELECT_AUTO_SUBSCRIBE_PRODUCT);
    }

    @Override
    public void changeAutoSubscribePackage(Integer autoExtendSelectedProduct) {
        Intent intent = GmProductActivity.changeAutoProductSelected(this, autoExtendSelectedProduct);
        startActivityForResult(intent, CHANGE_AUTO_SUBSCRIBE_PRODUCT);
    }

    @Override
    public void goToDynamicPayment(String url, String parameter, String callbackUrl, Integer paymentId) {
        PaymentPassData paymentPassData = new PaymentPassData();
        paymentPassData.setRedirectUrl(url);
        paymentPassData.setQueryString(parameter);
        paymentPassData.setCallbackSuccessUrl(callbackUrl);
        paymentPassData.setPaymentId(String.valueOf(paymentId));
        Intent intent = TopPayActivity.createInstance(this, paymentPassData);
        startActivityForResult(intent, REQUEST_CODE_PAYMENT_GM);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case CHANGE_SELECTED_PRODUCT:
                    updateSelectedProduct(data);
                    break;
                case SELECT_AUTO_SUBSCRIBE_PRODUCT:
                case CHANGE_AUTO_SUBSCRIBE_PRODUCT:
                    updateAutoSubscribeSelectedProduct(data);
                    break;
                default:
                    updateSelectedProduct(data);
            }
        }else if(requestCode == REQUEST_CODE_PAYMENT_GM){
            clearCacheShopInfo();
            finish();
        }

    }

    private void clearCacheShopInfo() {
        Fragment fragment = fragmentManager.findFragmentByTag(GmCheckoutFragment.TAG);
        if (fragment != null && fragment instanceof GmCheckoutFragment) {
            ((GmCheckoutFragment) fragment).clearCacheShopInfo();
        }
    }

    private void updateAutoSubscribeSelectedProduct(Intent data) {
        Bundle bundle = data.getExtras();
        int selected = bundle.getInt(GmProductActivity.SELECTED_PRODUCT);
        updateAutoSubscribeSelectedProductOnFragment(selected);
    }

    private void updateSelectedProduct(Intent data) {
        Bundle bundle = data.getExtras();
        int selected = bundle.getInt(GmProductActivity.SELECTED_PRODUCT);
        updateSelectedProductOnFragment(selected);
    }

    private void updateSelectedProductOnFragment(int selected) {
        Fragment fragment = fragmentManager.findFragmentByTag(GmCheckoutFragment.TAG);
        if (fragment != null && fragment instanceof GmCheckoutFragment) {
            ((GmCheckoutFragment) fragment).updateSelectedProduct(selected);
        }
    }

    private void updateAutoSubscribeSelectedProductOnFragment(int selected) {
        Fragment fragment = fragmentManager.findFragmentByTag(GmCheckoutFragment.TAG);
        if (fragment != null && fragment instanceof GmCheckoutFragment) {
            ((GmCheckoutFragment) fragment).updateSelectedAutoProduct(selected);
        }
    }

    private void initCheckoutFragment() {
        if (fragmentManager.findFragmentByTag(GmCheckoutFragment.TAG) == null) {
            inflateFragment(
                    GmCheckoutFragment.createFragment(currentSelected),
                    false,
                    GmCheckoutFragment.TAG);
        }
    }


    private void inflateFragment(Fragment fragment, boolean isAddToBackStack, String tag) {
        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.replace(R.id.parent_view, fragment, tag);
        if (isAddToBackStack) {
            ft.addToBackStack(null);
        }
        ft.commit();
    }

    @Override
    public AppComponent getComponent() {
        return getApplicationComponent();
    }
}
