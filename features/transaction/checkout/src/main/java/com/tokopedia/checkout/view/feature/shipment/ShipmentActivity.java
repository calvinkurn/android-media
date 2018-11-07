package com.tokopedia.checkout.view.feature.shipment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;

import com.tokopedia.abstraction.AbstractionRouter;
import com.tokopedia.checkout.R;
import com.tokopedia.checkout.domain.datamodel.cartlist.CartPromoSuggestion;
import com.tokopedia.checkout.domain.datamodel.voucher.PromoCodeAppliedData;
import com.tokopedia.checkout.view.common.base.BaseCheckoutActivity;
import com.tokopedia.design.component.Dialog;
import com.tokopedia.graphql.data.GraphqlClient;
import com.tokopedia.transactionanalytics.CheckoutAnalyticsCourierSelection;

/**
 * @author Irfan Khoirul on 23/04/18.
 */

public class ShipmentActivity extends BaseCheckoutActivity {

    public static final int REQUEST_CODE = 983;
    public static final int RESULT_CODE_FORCE_RESET_CART_FROM_SINGLE_SHIPMENT = 2;
    public static final int RESULT_CODE_FORCE_RESET_CART_FROM_MULTIPLE_SHIPMENT = 3;

    public static final String EXTRA_CART_PROMO_SUGGESTION = "EXTRA_CART_PROMO_SUGGESTION";
    public static final String EXTRA_PROMO_CODE_APPLIED_DATA = "EXTRA_PROMO_CODE_APPLIED_DATA";
    public static final String EXTRA_PROMO_CODE_COUPON_DEFAULT_SELECTED_TAB = "EXTRA_PROMO_CODE_COUPON_DEFAULT_SELECTED_TAB";
    public static final String EXTRA_AUTO_APPLY_PROMO_CODE_APPLIED = "EXTRA_AUTO_APPLY_PROMO_CODE_APPLIED";

    private CheckoutAnalyticsCourierSelection checkoutAnalyticsCourierSelection;


    public static Intent createInstance(Context context,
                                        PromoCodeAppliedData promoCodeCartListData,
                                        CartPromoSuggestion cartPromoSuggestion,
                                        String defaultSelectedTabPromo,
                                        boolean isAutoApplyPromoCodeApplied) {
        Intent intent = new Intent(context, ShipmentActivity.class);
        intent.putExtra(EXTRA_PROMO_CODE_APPLIED_DATA, promoCodeCartListData);
        intent.putExtra(EXTRA_CART_PROMO_SUGGESTION, cartPromoSuggestion);
        intent.putExtra(EXTRA_PROMO_CODE_COUPON_DEFAULT_SELECTED_TAB, defaultSelectedTabPromo);
        intent.putExtra(EXTRA_AUTO_APPLY_PROMO_CODE_APPLIED, isAutoApplyPromoCodeApplied);
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
        return ShipmentFragment.newInstance(
                getIntent().getParcelableExtra(EXTRA_PROMO_CODE_APPLIED_DATA),
                getIntent().getParcelableExtra(EXTRA_CART_PROMO_SUGGESTION),
                getIntent().getStringExtra(EXTRA_PROMO_CODE_COUPON_DEFAULT_SELECTED_TAB),
                getIntent().getBooleanExtra(EXTRA_AUTO_APPLY_PROMO_CODE_APPLIED, false)
        );
    }

    @Override
    public void onBackPressed() {
        checkoutAnalyticsCourierSelection.eventClickAtcCourierSelectionClickBackArrow();
        super.onBackPressed();
    }

    void showResetDialog() {
        final Dialog dialog = new Dialog(this, Dialog.Type.LONG_PROMINANCE);
        dialog.setTitle(getString(R.string.dialog_title_back_to_cart));
        dialog.setDesc(getString(R.string.dialog_message_back_to_cart));
        dialog.setBtnCancel(getString(R.string.label_dialog_back_to_cart_button_positive));
        dialog.setBtnOk(getString(R.string.label_dialog_back_to_cart_button_negative));
        dialog.setOnCancelClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkoutAnalyticsCourierSelection
                        .eventClickAtcCourierSelectionClickKembaliDanHapusPerubahanFromBackArrow();
                setResult(RESULT_CODE_FORCE_RESET_CART_FROM_SINGLE_SHIPMENT);
                finish();
                dialog.dismiss();
            }
        });
        dialog.setOnOkClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkoutAnalyticsCourierSelection
                        .eventClickAtcCourierSelectionClickTetapDiHalamanIniFromBackArrow();
                dialog.dismiss();
            }
        });
        dialog.getAlertDialog().setCancelable(true);
        dialog.getAlertDialog().setCanceledOnTouchOutside(true);
        dialog.show();
    }

}
