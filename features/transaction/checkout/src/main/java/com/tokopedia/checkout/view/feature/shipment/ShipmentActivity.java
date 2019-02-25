package com.tokopedia.checkout.view.feature.shipment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.tokopedia.abstraction.AbstractionRouter;
import com.tokopedia.checkout.domain.datamodel.cartlist.CartPromoSuggestion;
import com.tokopedia.checkout.view.common.base.BaseCheckoutActivity;
import com.tokopedia.graphql.data.GraphqlClient;
import com.tokopedia.promocheckout.common.view.model.PromoData;
import com.tokopedia.transactionanalytics.CheckoutAnalyticsCourierSelection;

/**
 * @author Irfan Khoirul on 23/04/18.
 */

public class ShipmentActivity extends BaseCheckoutActivity {

    public static final int REQUEST_CODE = 983;
    public static final int RESULT_CODE_FORCE_RESET_CART_FROM_SINGLE_SHIPMENT = 2;
    public static final int RESULT_CODE_FORCE_RESET_CART_FROM_MULTIPLE_SHIPMENT = 3;
    public static final int RESULT_CODE_COUPON_STATE_CHANGED = 735;

    public static final String EXTRA_IS_ONE_CLICK_SHIPMENT = "EXTRA_IS_ONE_CLICK_SHIPMENT";
    public static final String EXTRA_CART_PROMO_SUGGESTION = "EXTRA_CART_PROMO_SUGGESTION";
    public static final String EXTRA_PROMO_CODE_APPLIED_DATA = "EXTRA_PROMO_CODE_APPLIED_DATA";
    public static final String EXTRA_PROMO_CODE_COUPON_DEFAULT_SELECTED_TAB = "EXTRA_PROMO_CODE_COUPON_DEFAULT_SELECTED_TAB";
    public static final String EXTRA_AUTO_APPLY_PROMO_CODE_APPLIED = "EXTRA_AUTO_APPLY_PROMO_CODE_APPLIED";

    private CheckoutAnalyticsCourierSelection checkoutAnalyticsCourierSelection;
    private ShipmentFragment shipmentFragment;

    public static Intent createInstance(Context context,
                                        PromoData promoData,
                                        CartPromoSuggestion cartPromoSuggestion,
                                        String defaultSelectedTabPromo,
                                        boolean isAutoApplyPromoCodeApplied) {
        Intent intent = new Intent(context, ShipmentActivity.class);
        intent.putExtra(EXTRA_PROMO_CODE_APPLIED_DATA, promoData);
        intent.putExtra(EXTRA_CART_PROMO_SUGGESTION, cartPromoSuggestion);
        intent.putExtra(EXTRA_PROMO_CODE_COUPON_DEFAULT_SELECTED_TAB, defaultSelectedTabPromo);
        intent.putExtra(EXTRA_AUTO_APPLY_PROMO_CODE_APPLIED, isAutoApplyPromoCodeApplied);
        return intent;
    }

    // Used for One Click Shipment
    public static Intent createInstance(Context context) {
        Intent intent = new Intent(context, ShipmentActivity.class);
        intent.putExtra(ShipmentActivity.EXTRA_IS_ONE_CLICK_SHIPMENT, true);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        GraphqlClient.init(this);
    }

    @Override
    protected void initInjector() {

    }

    @Override
    protected void setupURIPass(Uri data) {

    }

    @Override
    protected void setupBundlePass(Bundle extras) {

    }

    @Override
    protected void initView() {

    }

    @Override
    protected void setViewListener() {

    }

    @Override
    protected void initVar() {
        checkoutAnalyticsCourierSelection = new CheckoutAnalyticsCourierSelection(
                ((AbstractionRouter) getApplication()).getAnalyticTracker()
        );
    }

    @Override
    protected void setActionVar() {

    }

    @Override
    protected Fragment getNewFragment() {
        shipmentFragment = ShipmentFragment.newInstance(
                getIntent().getStringExtra(EXTRA_PROMO_CODE_COUPON_DEFAULT_SELECTED_TAB),
                getIntent().getBooleanExtra(EXTRA_AUTO_APPLY_PROMO_CODE_APPLIED, false),
                getIntent().getBooleanExtra(EXTRA_IS_ONE_CLICK_SHIPMENT, false)
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
