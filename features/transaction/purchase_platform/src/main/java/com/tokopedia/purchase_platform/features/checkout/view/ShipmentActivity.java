package com.tokopedia.purchase_platform.features.checkout.view;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.fragment.app.Fragment;

import com.tokopedia.graphql.data.GraphqlClient;
import com.tokopedia.promocheckout.common.view.model.PromoStackingData;
import com.tokopedia.purchase_platform.common.analytics.CheckoutAnalyticsCourierSelection;
import com.tokopedia.purchase_platform.common.base.BaseCheckoutActivity;
import com.tokopedia.purchase_platform.common.feature.promo_suggestion.CartPromoSuggestionHolderData;
import com.tokopedia.purchase_platform.common.constant.CartConstant;
import com.tokopedia.transaction.common.sharedata.ShipmentFormRequest;

import static com.tokopedia.purchase_platform.common.constant.CheckoutConstant.EXTRA_IS_ONE_CLICK_SHIPMENT;

/**
 * @author Irfan Khoirul on 23/04/18.
 */

public class ShipmentActivity extends BaseCheckoutActivity {

    public static final int REQUEST_CODE = 983;
    public static final int RESULT_CODE_FORCE_RESET_CART_FROM_SINGLE_SHIPMENT = 2;
    public static final int RESULT_CODE_FORCE_RESET_CART_FROM_MULTIPLE_SHIPMENT = 3;
    public static final int RESULT_CODE_COUPON_STATE_CHANGED = 735;

    public static final String EXTRA_CART_PROMO_SUGGESTION = "EXTRA_CART_PROMO_SUGGESTION";
    public static final String EXTRA_PROMO_CODE_APPLIED_DATA = "EXTRA_PROMO_CODE_APPLIED_DATA";
    public static final String EXTRA_PROMO_CODE_COUPON_DEFAULT_SELECTED_TAB = "EXTRA_PROMO_CODE_COUPON_DEFAULT_SELECTED_TAB";
    public static final String EXTRA_AUTO_APPLY_PROMO_CODE_APPLIED = "EXTRA_AUTO_APPLY_PROMO_CODE_APPLIED";

    private CheckoutAnalyticsCourierSelection checkoutAnalyticsCourierSelection;
    private ShipmentFragment shipmentFragment;

    public static Intent createInstance(Context context,
                                        PromoStackingData promoData,
                                        CartPromoSuggestionHolderData cartPromoSuggestionHolderData,
                                        String defaultSelectedTabPromo,
                                        boolean isAutoApplyPromoCodeApplied) {
        Intent intent = new Intent(context, ShipmentActivity.class);
        intent.putExtra(EXTRA_PROMO_CODE_APPLIED_DATA, promoData);
        intent.putExtra(EXTRA_CART_PROMO_SUGGESTION, cartPromoSuggestionHolderData);
        intent.putExtra(EXTRA_PROMO_CODE_COUPON_DEFAULT_SELECTED_TAB, defaultSelectedTabPromo);
        intent.putExtra(EXTRA_AUTO_APPLY_PROMO_CODE_APPLIED, isAutoApplyPromoCodeApplied);
        return intent;
    }

    // Used for One Click Shipment
    public static Intent createInstance(Context context, ShipmentFormRequest shipmentFormRequest) {
        Intent intent = new Intent(context, ShipmentActivity.class);
        intent.putExtra(EXTRA_IS_ONE_CLICK_SHIPMENT, true);
        intent.putExtras(shipmentFormRequest.getBundle());
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkoutAnalyticsCourierSelection = new CheckoutAnalyticsCourierSelection();
    }

    @Override
    protected void initInjector() {

    }

    @Override
    protected void setupBundlePass(Bundle extras) {

    }

    @Override
    protected void initView() {

    }

    @Override
    protected Fragment getNewFragment() {
        String leasingId = "";
        if (getIntent().getData() != null) {
            Uri deepLink = getIntent().getData();
            leasingId = deepLink.getQueryParameter(CartConstant.CHECKOUT_LEASING_ID);
        }
        shipmentFragment = ShipmentFragment.newInstance(
                getIntent().getStringExtra(EXTRA_PROMO_CODE_COUPON_DEFAULT_SELECTED_TAB),
                getIntent().getBooleanExtra(EXTRA_AUTO_APPLY_PROMO_CODE_APPLIED, false),
                getIntent().getBooleanExtra(EXTRA_IS_ONE_CLICK_SHIPMENT, false),
                leasingId,
                getIntent().getExtras()
        );

        return shipmentFragment;
    }

    @Override
    public void onBackPressed() {
        checkoutAnalyticsCourierSelection.eventClickAtcCourierSelectionClickBackArrow();
        if (shipmentFragment != null) {
            setResult(shipmentFragment.getResultCode());
            finish();
        } else {
            super.onBackPressed();
        }
    }
}
