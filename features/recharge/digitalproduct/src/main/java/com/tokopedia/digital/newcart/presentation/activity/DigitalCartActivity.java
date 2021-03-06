package com.tokopedia.digital.newcart.presentation.activity;

import android.content.Context;
import android.net.Uri;

import androidx.fragment.app.Fragment;

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.abstraction.common.di.component.HasComponent;
import com.tokopedia.applink.constant.DeeplinkConstant;
import com.tokopedia.authentication.AuthHelper;
import com.tokopedia.common_digital.cart.view.model.DigitalCheckoutPassData;
import com.tokopedia.common_digital.common.constant.DigitalExtraParam;
import com.tokopedia.digital.R;
import com.tokopedia.digital.newcart.di.DigitalCartComponent;
import com.tokopedia.digital.newcart.di.DigitalCartComponentInstance;
import com.tokopedia.digital.newcart.presentation.fragment.DigitalCartDealsFragment;
import com.tokopedia.digital.newcart.presentation.fragment.DigitalCartDefaultFragment;
import com.tokopedia.digital.newcart.presentation.fragment.DigitalCartMyBillsFragment;
import com.tokopedia.digital.newcart.presentation.fragment.listener.DigitalDealNatigationListener;
import com.tokopedia.digital.newcart.presentation.model.DigitalSubscriptionParams;
import com.tokopedia.digital.newcart.presentation.model.cart.CartDigitalInfoData;
import com.tokopedia.user.session.UserSession;

import java.util.HashMap;
import java.util.Set;

/**
 * applink
 * tokopedia://digital/cart
 * tokopedia-android-internal://digital/cart
 */

public class DigitalCartActivity extends BaseSimpleActivity implements HasComponent<DigitalCartComponent>,
        DigitalCartDefaultFragment.InteractionListener,
        DigitalCartDealsFragment.InteractionListener,
        DigitalCartMyBillsFragment.InteractionListener {

    private DigitalCheckoutPassData processIntentDataCheckoutFromApplink(Context context, Uri uriData) {
        DigitalCheckoutPassData passData = new DigitalCheckoutPassData();
        passData.setCategoryId(uriData.getQueryParameter(DigitalCheckoutPassData.Companion.getPARAM_CATEGORY_ID()));
        passData.setOrderId(uriData.getQueryParameter(DigitalCheckoutPassData.Companion.getPARAM_ORDER_ID()));
        passData.setClientNumber(uriData.getQueryParameter(DigitalCheckoutPassData.Companion.getPARAM_CLIENT_NUMBER()));
        passData.setOperatorId(uriData.getQueryParameter(DigitalCheckoutPassData.Companion.getPARAM_OPERATOR_ID()));
        passData.setProductId(uriData.getQueryParameter(DigitalCheckoutPassData.Companion.getPARAM_PRODUCT_ID()));
        passData.setPromo(uriData.getQueryParameter(DigitalCheckoutPassData.Companion.getPARAM_IS_PROMO()));
        String instantCheckoutParam = uriData.getQueryParameter(DigitalCheckoutPassData.Companion.getPARAM_INSTANT_CHECKOUT());
        passData.setInstantCheckout(instantCheckoutParam != null ? instantCheckoutParam : "0");
        passData.setIdemPotencyKey(generateATokenRechargeCheckout(context));

        HashMap<String, String> fields = new HashMap<>();
        Set<String> parameters = uriData.getQueryParameterNames();
        if (!parameters.isEmpty()) {
            for (String param : parameters) {
                if (param.startsWith(DigitalCheckoutPassData.Companion.getPARAM_FIELD_LABEL_PREFIX())) {
                    String value = uriData.getQueryParameter(param);
                    String key = param.replaceFirst(DigitalCheckoutPassData.Companion.getPARAM_FIELD_LABEL_PREFIX(), "");
                    fields.put(key, value);
                }
            }
        }
        passData.setFields(fields);
        return passData;
    }

    private DigitalSubscriptionParams processIntentDataSubscription(Uri uriData) {
        DigitalSubscriptionParams subParams = new DigitalSubscriptionParams();
        String showSubscribePopUpArg = uriData.getQueryParameter(DigitalSubscriptionParams.ARG_SHOW_SUBSCRIBE_POP_UP);
        String autoSubscribeArg = uriData.getQueryParameter(DigitalSubscriptionParams.ARG_AUTO_SUBSCRIBE);
        if (showSubscribePopUpArg != null) {
            subParams.setShowSubscribePopUp(Boolean.parseBoolean(showSubscribePopUpArg));
        }
        if (autoSubscribeArg != null) {
            subParams.setAutoSubscribe(Boolean.parseBoolean(autoSubscribeArg));
        }
        return subParams;
    }

    private String generateATokenRechargeCheckout(Context context) {
        String timeMillis = String.valueOf(System.currentTimeMillis());
        String token = AuthHelper.getMD5Hash(timeMillis);
        UserSession userSession = new UserSession(context);
        String tokenRecharge = String.format(getString(R.string.digital_generate_token_checkout),
                userSession.getUserId(), (token.isEmpty() ? timeMillis : token));
        return tokenRecharge;
    }

    @Override
    protected Fragment getNewFragment() {
        Uri uriData = getIntent().getData();
        DigitalCheckoutPassData cartPassData = null;
        DigitalSubscriptionParams subParams = null;

        if (uriData.getQueryParameterNames().size() > 0) {
            cartPassData = processIntentDataCheckoutFromApplink(getApplicationContext(), uriData);
            subParams = processIntentDataSubscription(uriData);
        } else if (uriData.getScheme().equals(DeeplinkConstant.SCHEME_INTERNAL)) {
            cartPassData = getIntent().getParcelableExtra(DigitalExtraParam.EXTRA_PASS_DIGITAL_CART_DATA);
            subParams = getIntent().getParcelableExtra(DigitalExtraParam.EXTRA_PASS_DIGITAL_SUBSCRIPTION_DATA);
        }
        return DigitalCartDefaultFragment.newInstance(cartPassData, subParams);
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
                .replace(com.tokopedia.abstraction.R.id.parent_view, fragment, getTagFragment())
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
