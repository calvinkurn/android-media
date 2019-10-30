package com.tokopedia.digital.newcart.presentation.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.text.TextUtils;

import com.airbnb.deeplinkdispatch.DeepLink;
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.abstraction.common.di.component.HasComponent;
import com.tokopedia.authentication.AuthHelper;
import com.tokopedia.common_digital.cart.view.model.DigitalCheckoutPassData;
import com.tokopedia.common_digital.cart.view.model.cart.CartDigitalInfoData;
import com.tokopedia.common_digital.common.constant.DigitalExtraParam;
import com.tokopedia.digital.R;
import com.tokopedia.digital.applink.DigitalApplinkConstant;
import com.tokopedia.digital.newcart.di.DigitalCartComponent;
import com.tokopedia.digital.newcart.di.DigitalCartComponentInstance;
import com.tokopedia.digital.newcart.presentation.fragment.DigitalCartDealsFragment;
import com.tokopedia.digital.newcart.presentation.fragment.DigitalCartDefaultFragment;
import com.tokopedia.digital.newcart.presentation.fragment.DigitalCartMyBillsFragment;
import com.tokopedia.digital.newcart.presentation.fragment.listener.DigitalDealNatigationListener;
import com.tokopedia.digital.newcart.presentation.model.DigitalSubscriptionParams;
import com.tokopedia.user.session.UserSession;

public class DigitalCartActivity extends BaseSimpleActivity implements HasComponent<DigitalCartComponent>,
        DigitalCartDefaultFragment.InteractionListener,
        DigitalCartDealsFragment.InteractionListener,
        DigitalCartMyBillsFragment.InteractionListener {
    private DigitalCheckoutPassData cartPassData;
    private DigitalSubscriptionParams subParams;

    public static Intent newInstance(Context context, DigitalCheckoutPassData passData) {
        return new Intent(context, DigitalCartActivity.class)
                .putExtra(DigitalExtraParam.EXTRA_PASS_DIGITAL_CART_DATA, passData);
    }

    public static Intent newInstance(Context context, Bundle bundle) {
        DigitalCheckoutPassData passData = new DigitalCheckoutPassData();
        passData.setAction(bundle.getString(DigitalCheckoutPassData.Companion.getPARAM_ACTION()));
        passData.setCategoryId(bundle.getString(DigitalCheckoutPassData.Companion.getPARAM_CATEGORY_ID()));
        passData.setClientNumber(bundle.getString(DigitalCheckoutPassData.Companion.getPARAM_CLIENT_NUMBER()));
        passData.setOperatorId(bundle.getString(DigitalCheckoutPassData.Companion.getPARAM_OPERATOR_ID()));
        passData.setProductId(bundle.getString(DigitalCheckoutPassData.Companion.getPARAM_PRODUCT_ID()));
        passData.setPromo(bundle.getString(DigitalCheckoutPassData.Companion.getPARAM_IS_PROMO()));
        String instantCheckoutParam = bundle.getString(DigitalCheckoutPassData.Companion.getPARAM_INSTANT_CHECKOUT());
        passData.setInstantCheckout(instantCheckoutParam != null ? instantCheckoutParam : "0");
        passData.setUtmCampaign(bundle.getString(DigitalCheckoutPassData.Companion.getPARAM_UTM_CAMPAIGN()));
        passData.setUtmMedium(bundle.getString(DigitalCheckoutPassData.Companion.getPARAM_UTM_MEDIUM()));
        passData.setUtmSource(bundle.getString(DigitalCheckoutPassData.Companion.getPARAM_UTM_SOURCE()));
        passData.setUtmContent(bundle.getString(DigitalCheckoutPassData.Companion.getPARAM_UTM_CONTENT()));
        if (!TextUtils.isEmpty(bundle.getString(DigitalCheckoutPassData.Companion.getPARAM_IDEM_POTENCY_KEY(), ""))) {
            passData.setIdemPotencyKey(
                    bundle.getString(DigitalCheckoutPassData.Companion.getPARAM_IDEM_POTENCY_KEY())
            );
        } else {
            passData.setIdemPotencyKey(
                    generateATokenRechargeCheckout(context)
            );
        }

        // Add subscription applink parameter handling
        Intent intent = new Intent(context, DigitalCartActivity.class);
        DigitalSubscriptionParams subParams = new DigitalSubscriptionParams();
        String showSubscribePopUpArg = bundle.getString(DigitalSubscriptionParams.ARG_SHOW_SUBSCRIBE_POP_UP);
        String autoSubscribeArg = bundle.getString(DigitalSubscriptionParams.ARG_AUTO_SUBSCRIBE);
        if (showSubscribePopUpArg != null) {
            subParams.setShowSubscribePopUp(Boolean.parseBoolean(showSubscribePopUpArg));
        }
        if (autoSubscribeArg != null) {
            subParams.setAutoSubscribe(Boolean.parseBoolean(autoSubscribeArg));
        }
        intent.putExtra(DigitalExtraParam.EXTRA_PASS_DIGITAL_SUBSCRIPTION_DATA, subParams);
        intent.putExtra(DigitalExtraParam.EXTRA_PASS_DIGITAL_CART_DATA, passData);
        return intent;
    }

    private static String generateATokenRechargeCheckout(Context context) {
        String timeMillis = String.valueOf(System.currentTimeMillis());
        String token = AuthHelper.getMD5Hash(timeMillis);
        UserSession userSession = new UserSession(context);
        return userSession.getUserId() + "_" + (token.isEmpty() ? timeMillis : token);
    }

    @DeepLink({DigitalApplinkConstant.DIGITAL_CART})
    public static Intent getApplinkInstance(Context context, Bundle extras) {
        Uri.Builder uri = Uri.parse(extras.getString(DeepLink.URI)).buildUpon();
        return DigitalCartActivity.newInstance(context, extras)
                .setData(uri.build())
                .putExtras(extras);
    }


    @Override
    protected Fragment getNewFragment() {
        return DigitalCartDefaultFragment.newInstance(cartPassData, subParams);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        cartPassData = getIntent().getParcelableExtra(DigitalExtraParam.EXTRA_PASS_DIGITAL_CART_DATA);
        subParams = getIntent().getParcelableExtra(DigitalExtraParam.EXTRA_PASS_DIGITAL_SUBSCRIPTION_DATA);
        super.onCreate(savedInstanceState);
    }

    @Override
    public DigitalCartComponent getComponent() {
        return DigitalCartComponentInstance.getDigitalCartComponent(getApplication());
    }

    @Override
    public void inflateDealsPage(CartDigitalInfoData cartDigitalInfoData, DigitalCheckoutPassData passData) {
        inflateFragment(DigitalCartDealsFragment.newInstance(passData, cartDigitalInfoData));
    }

    @Override
    public void inflateMyBillsSubscriptionPage(CartDigitalInfoData cartDigitalInfoData,
                                               DigitalCheckoutPassData cartPassData,
                                               DigitalSubscriptionParams subParams) {
        inflateFragment(DigitalCartMyBillsFragment.Companion.newInstance(cartDigitalInfoData, cartPassData, subParams));
    }

    private void inflateFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.parent_view, fragment, getTagFragment())
                .commit();
    }

    @Override
    public void onBackPressed() {
        Fragment fragment = getFragment();
        if (fragment instanceof DigitalDealNatigationListener) {
            if (!((DigitalDealNatigationListener) fragment).canGoBack()) {
                ((DigitalDealNatigationListener) fragment).goBack();
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
