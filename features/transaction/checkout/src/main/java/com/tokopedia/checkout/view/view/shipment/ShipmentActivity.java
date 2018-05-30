package com.tokopedia.checkout.view.view.shipment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;

import com.tokopedia.abstraction.AbstractionRouter;
import com.tokopedia.abstraction.common.di.component.HasComponent;
import com.tokopedia.checkout.R;
import com.tokopedia.checkout.domain.datamodel.cartlist.CartPromoSuggestion;
import com.tokopedia.checkout.domain.datamodel.cartshipmentform.CartShipmentAddressFormData;
import com.tokopedia.checkout.domain.datamodel.voucher.PromoCodeAppliedData;
import com.tokopedia.checkout.view.base.BaseCheckoutActivity;
import com.tokopedia.checkout.view.di.component.CartComponent;
import com.tokopedia.checkout.view.di.component.CartComponentInjector;
import com.tokopedia.design.component.Dialog;
import com.tokopedia.transactionanalytics.CheckoutAnalyticsCartShipmentPage;

/**
 * @author Irfan Khoirul on 23/04/18.
 */

public class ShipmentActivity extends BaseCheckoutActivity implements HasComponent<CartComponent> {

    public static final int REQUEST_CODE = 983;
    public static final int RESULT_CODE_ACTION_TO_MULTIPLE_ADDRESS_FORM = 1;
    public static final int RESULT_CODE_FORCE_RESET_CART_FROM_SINGLE_SHIPMENT = 2;
    public static final int RESULT_CODE_FORCE_RESET_CART_FROM_MULTIPLE_SHIPMENT = 3;
    public static final int RESULT_CODE_CANCEL_SHIPMENT_PAYMENT = 4;

    public static final String EXTRA_SHIPMENT_FORM_DATA = "EXTRA_SHIPMENT_FORM_DATA";
    public static final String EXTRA_SELECTED_ADDRESS_RECIPIENT_DATA = "EXTRA_DEFAULT_ADDRESS_RECIPIENT_DATA";
    public static final String EXTRA_CART_PROMO_SUGGESTION = "EXTRA_CART_PROMO_SUGGESTION";
    public static final String EXTRA_PROMO_CODE_APPLIED_DATA = "EXTRA_PROMO_CODE_APPLIED_DATA";
    private CheckoutAnalyticsCartShipmentPage checkoutAnalyticsCartShipmentPage;


    public static Intent createInstance(Context context,
                                        CartShipmentAddressFormData cartShipmentAddressFormData,
                                        PromoCodeAppliedData promoCodeCartListData,
                                        CartPromoSuggestion cartPromoSuggestion) {
        Intent intent = new Intent(context, ShipmentActivity.class);
        intent.putExtra(EXTRA_PROMO_CODE_APPLIED_DATA, promoCodeCartListData);
        intent.putExtra(EXTRA_SHIPMENT_FORM_DATA, cartShipmentAddressFormData);
        intent.putExtra(EXTRA_CART_PROMO_SUGGESTION, cartPromoSuggestion);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        checkoutAnalyticsCartShipmentPage = new CheckoutAnalyticsCartShipmentPage(
                ((AbstractionRouter) getApplication()).getAnalyticTracker()
        );
    }

    @Override
    protected void setActionVar() {

    }

    @Override
    protected Fragment getNewFragment() {
        return ShipmentFragment.newInstance(
                (CartShipmentAddressFormData) getIntent().getParcelableExtra(EXTRA_SHIPMENT_FORM_DATA),
                (PromoCodeAppliedData) getIntent().getParcelableExtra(EXTRA_PROMO_CODE_APPLIED_DATA),
                (CartPromoSuggestion) getIntent().getParcelableExtra(EXTRA_CART_PROMO_SUGGESTION)
        );
    }

    @Override
    public CartComponent getComponent() {
        return CartComponentInjector.newInstance(getApplication()).getCartApiServiceComponent();
    }

    @Override
    public void onBackPressed() {
        checkoutAnalyticsCartShipmentPage.eventClickShipmentClickBackArrow();
        showResetDialog();
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
                checkoutAnalyticsCartShipmentPage
                        .eventClickShipmentClickKembaliDanHapusPerubahanFromBackArrow();
                setResult(RESULT_CODE_FORCE_RESET_CART_FROM_SINGLE_SHIPMENT);
                finish();
            }
        });
        dialog.setOnOkClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkoutAnalyticsCartShipmentPage
                        .eventClickShipmentClickTetapDiHalamanIniFromBackArrow();
                dialog.dismiss();
            }
        });
        dialog.getAlertDialog().setCancelable(true);
        dialog.getAlertDialog().setCanceledOnTouchOutside(true);
        dialog.show();
    }

}
