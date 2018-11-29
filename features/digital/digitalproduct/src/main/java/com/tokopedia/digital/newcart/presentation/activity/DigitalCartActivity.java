package com.tokopedia.digital.newcart.presentation.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;

import com.airbnb.deeplinkdispatch.DeepLink;
import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.abstraction.common.di.component.HasComponent;
import com.tokopedia.common_digital.cart.view.model.DigitalCheckoutPassData;
import com.tokopedia.common_digital.cart.view.model.cart.CartDigitalInfoData;
import com.tokopedia.common_digital.common.di.DaggerDigitalComponent;
import com.tokopedia.common_digital.common.di.DigitalComponent;
import com.tokopedia.digital.R;
import com.tokopedia.digital.applink.DigitalApplinkConstant;
import com.tokopedia.digital.cart.di.DaggerDigitalCartComponent;
import com.tokopedia.digital.cart.di.DigitalCartComponent;
import com.tokopedia.digital.newcart.presentation.fragment.DigitalCartDealsFragment;
import com.tokopedia.digital.newcart.presentation.fragment.DigitalCartDefaultFragment;
import com.tokopedia.digital.newcart.presentation.fragment.listener.DigitalDealNatigationListener;
import com.tokopedia.network.utils.AuthUtil;
import com.tokopedia.user.session.UserSession;

public class DigitalCartActivity extends BaseSimpleActivity implements HasComponent<DigitalCartComponent>, DigitalCartDefaultFragment.InteractionListener, DigitalCartDealsFragment.InteractionListener {
    private static final String EXTRA_PASS_DIGITAL_CART_DATA = "EXTRA_PASS_DIGITAL_CART_DATA";
    private DigitalCheckoutPassData cartPassData;
    private DigitalCartComponent component;

    public static Intent newInstance(Context context, DigitalCheckoutPassData passData) {
        return new Intent(context, DigitalCartActivity.class)
                .putExtra(EXTRA_PASS_DIGITAL_CART_DATA, passData);
    }

    public static Intent newInstance(Context context, Bundle bundle) {
        DigitalCheckoutPassData passData = new DigitalCheckoutPassData();
        passData.setAction(bundle.getString(DigitalCheckoutPassData.PARAM_ACTION));
        passData.setCategoryId(bundle.getString(DigitalCheckoutPassData.PARAM_CATEGORY_ID));
        passData.setClientNumber(bundle.getString(DigitalCheckoutPassData.PARAM_CLIENT_NUMBER));
        passData.setOperatorId(bundle.getString(DigitalCheckoutPassData.PARAM_OPERATOR_ID));
        passData.setProductId(bundle.getString(DigitalCheckoutPassData.PARAM_PRODUCT_ID));
        passData.setIsPromo(bundle.getString(DigitalCheckoutPassData.PARAM_IS_PROMO));
        passData.setInstantCheckout(bundle.getString(DigitalCheckoutPassData.PARAM_INSTANT_CHECKOUT));
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
                    generateATokenRechargeCheckout(context)
            );
        }
        return new Intent(context, DigitalCartActivity.class)
                .putExtra(EXTRA_PASS_DIGITAL_CART_DATA, passData);
    }

    private static String generateATokenRechargeCheckout(Context context) {
        String timeMillis = String.valueOf(System.currentTimeMillis());
        String token = AuthUtil.md5(timeMillis);
        UserSession userSession = new UserSession(context);
        return userSession.getUserId() + "_" + (token.isEmpty() ? timeMillis : token);
    }

    public static Intent getCallingApplinksTaskStask(Context context, Bundle extras) {
        Uri.Builder uri = Uri.parse(extras.getString(DeepLink.URI)).buildUpon();
        return DigitalCartActivity.newInstance(context, extras)
                .setData(uri.build())
                .putExtras(extras);
    }


    @Override
    protected Fragment getNewFragment() {
        return DigitalCartDefaultFragment.newInstance(cartPassData);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        cartPassData = getIntent().getParcelableExtra(EXTRA_PASS_DIGITAL_CART_DATA);
        initInjector();
        super.onCreate(savedInstanceState);
    }

    private void initInjector() {
        DigitalComponent digitalComponent = DaggerDigitalComponent.builder().baseAppComponent(
                ((BaseMainApplication) getApplication()).getBaseAppComponent()).build();
        component = DaggerDigitalCartComponent.builder()
                .digitalComponent(digitalComponent)
                .build();
        component.inject(this);
    }

    @Override
    public DigitalCartComponent getComponent() {
        if (component == null) initInjector();
        return component;
    }

    @Override
    public void inflateDealsPage(CartDigitalInfoData cartDigitalInfoData, DigitalCheckoutPassData passData) {
        inflateFragment(DigitalCartDealsFragment.newInstance(passData, cartDigitalInfoData));
    }

    private void inflateFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.parent_view, fragment, getTagFragment())
                .commit();
    }

    @Override
    public void onBackPressed() {
        if (getFragment() != null && getFragment() instanceof DigitalDealNatigationListener) {
            if (!((DigitalDealNatigationListener) getFragment()).canGoBack()) {
                ((DigitalDealNatigationListener) getFragment()).goBack();
            } else {
                super.onBackPressed();
            }
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void updateToolbarTitle(String title) {
        updateTitle(title);
    }
}
