package com.tokopedia.digital.cart.presentation.activity;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.TaskStackBuilder;
import android.text.TextUtils;

import com.airbnb.deeplinkdispatch.DeepLink;
import com.tokopedia.common_digital.common.DigitalRouter;
import com.tokopedia.core.app.BasePresenterActivity;
import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.gcm.Constants;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.core.router.SellerAppRouter;
import com.tokopedia.common_digital.cart.view.model.DigitalCheckoutPassData;
import com.tokopedia.core.router.home.HomeRouter;
import com.tokopedia.core.util.GlobalConfig;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.digital.R;
import com.tokopedia.digital.applink.DigitalApplinkConstant;
import com.tokopedia.digital.cart.di.DigitalCartComponentInstance;
import com.tokopedia.digital.cart.presentation.fragment.CartDigitalFragment;
import com.tokopedia.digital.common.router.DigitalModuleRouter;
import com.tokopedia.digital.newcart.presentation.activity.DigitalCartActivity;

import javax.inject.Inject;

/**
 * @author anggaprasetiyo on 2/21/17.
 */

public class CartDigitalActivity extends BasePresenterActivity implements
        CartDigitalFragment.ActionListener {
    private static final String EXTRA_PASS_DIGITAL_CART_DATA = "EXTRA_PASS_DIGITAL_CART_DATA";
    private static final String EXTRA_PASS_EXTRA_FROM = "EXTRA_PASS_EXTRA_FROM";
    private DigitalCheckoutPassData passData;

    @Inject
    DigitalRouter digitalRouter;

    public static Intent newInstance(Context context, DigitalCheckoutPassData passData) {
        return new Intent(context, CartDigitalActivity.class)
                .putExtra(EXTRA_PASS_DIGITAL_CART_DATA, passData);
    }


    public static Intent newInstance(Activity activity, DigitalCheckoutPassData passData, int from) {
        return new Intent(activity, CartDigitalActivity.class)
                .putExtra(EXTRA_PASS_DIGITAL_CART_DATA, passData)
                .putExtra(EXTRA_PASS_EXTRA_FROM, from);
    }

    @Override
    protected void onResume() {
        super.onResume();
        unregisterShake();
    }

    public static Intent newInstance(Context context, Bundle bundle) {
        DigitalCheckoutPassData passData = new DigitalCheckoutPassData();
        passData.setAction(bundle.getString(DigitalCheckoutPassData.PARAM_ACTION));
        passData.setCategoryId(bundle.getString(DigitalCheckoutPassData.PARAM_CATEGORY_ID));
        passData.setClientNumber(bundle.getString(DigitalCheckoutPassData.PARAM_CLIENT_NUMBER));
        passData.setOperatorId(bundle.getString(DigitalCheckoutPassData.PARAM_OPERATOR_ID));
        passData.setProductId(bundle.getString(DigitalCheckoutPassData.PARAM_PRODUCT_ID));
        passData.setIsPromo(bundle.getString(DigitalCheckoutPassData.PARAM_IS_PROMO));
        passData.setInstantCheckout(bundle.getString(DigitalCheckoutPassData.PARAM_INSTANT_CHECKOUT, "0"));
        passData.setUtmCampaign(bundle.getString(DigitalCheckoutPassData.PARAM_UTM_CAMPAIGN));
        passData.setUtmMedium(bundle.getString(DigitalCheckoutPassData.PARAM_UTM_MEDIUM));
        passData.setUtmSource(bundle.getString(DigitalCheckoutPassData.PARAM_UTM_SOURCE));
        passData.setUtmContent(bundle.getString(DigitalCheckoutPassData.PARAM_UTM_CONTENT));
        if (!TextUtils.isEmpty(bundle.getString(DigitalCheckoutPassData.PARAM_IDEM_POTENCY_KEY, ""))) {
            passData.setIdemPotencyKey(
                    bundle.getString(DigitalCheckoutPassData.PARAM_IDEM_POTENCY_KEY)
            );
        } else {
            passData.setIdemPotencyKey(
                    generateATokenRechargeCheckout()
            );
        }
        return new Intent(context, CartDigitalActivity.class)
                .putExtra(EXTRA_PASS_DIGITAL_CART_DATA, passData);
    }

    @DeepLink({DigitalApplinkConstant.DIGITAL_CART})
    public static Intent getCallingApplinksTaskStask(Context context, Bundle extras) {
        TaskStackBuilder taskStackBuilder = TaskStackBuilder.create(context);
        Uri.Builder uri = Uri.parse(extras.getString(DeepLink.URI)).buildUpon();

        Intent homeIntent;
        if (GlobalConfig.isSellerApp()) {
            homeIntent = SellerAppRouter.getSellerHomeActivity(context);
        } else {
            homeIntent = HomeRouter.getHomeActivity(context);
        }
        homeIntent.putExtra(HomeRouter.EXTRA_INIT_FRAGMENT,
                HomeRouter.INIT_STATE_FRAGMENT_HOME);

        Intent destination = CartDigitalActivity.newInstance(context, extras)
                .setData(uri.build())
                .putExtras(extras);
        destination.putExtra(Constants.EXTRA_FROM_PUSH, true);
        taskStackBuilder.addNextIntent(destination);
        return destination;
    }

    @Override
    protected void setupURIPass(Uri data) {

    }

    @Override
    protected void setupBundlePass(Bundle extras) {
        passData = extras.getParcelable(EXTRA_PASS_DIGITAL_CART_DATA);
    }

    @Override
    protected void initialPresenter() {
        DigitalCartComponentInstance.getDigitalCartComponent(getApplication())
                .inject(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_cart_digital_module;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void setViewListener() {
        Fragment fragment = getFragmentManager().findFragmentById(R.id.container);

        if (digitalRouter.getBooleanRemoteConfig(DigitalRouter.MULTICHECKOUT_CART_REMOTE_CONFIG, true) && !GlobalConfig.isSellerApp() ) {
            Intent newCartIntent = DigitalCartActivity.newInstance(this, passData);
            newCartIntent.addFlags(Intent.FLAG_ACTIVITY_FORWARD_RESULT);
            startActivity(newCartIntent);
            finish();
        } else if (!(fragment instanceof CartDigitalFragment)) {
            getFragmentManager().beginTransaction().replace(R.id.container,
                    CartDigitalFragment.newInstance(
                            passData)
            ).commit();
        }
    }

    @Override
    protected void initVar() {

    }

    @Override
    protected void setActionVar() {

    }


    @Override
    public void setTitleCart(String title) {
        toolbar.setTitle(title);
    }

    private static String generateATokenRechargeCheckout() {
        String timeMillis = String.valueOf(System.currentTimeMillis());
        String token = AuthUtil.md5(timeMillis);
        return SessionHandler.getLoginID(MainApplication.getAppContext()) + "_" + (token.isEmpty() ? timeMillis : token);
    }

    @Override
    protected boolean isLightToolbarThemes() {
        return true;
    }
}
