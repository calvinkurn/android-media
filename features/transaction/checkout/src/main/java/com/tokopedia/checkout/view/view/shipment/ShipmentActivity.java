package com.tokopedia.checkout.view.view.shipment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

import com.tokopedia.abstraction.common.di.component.HasComponent;
import com.tokopedia.checkout.R;
import com.tokopedia.checkout.domain.datamodel.cartlist.CartPromoSuggestion;
import com.tokopedia.checkout.domain.datamodel.cartshipmentform.CartShipmentAddressFormData;
import com.tokopedia.checkout.domain.datamodel.voucher.PromoCodeAppliedData;
import com.tokopedia.checkout.view.base.BaseCheckoutActivity;
import com.tokopedia.checkout.view.di.component.CartComponent;
import com.tokopedia.checkout.view.di.component.CartComponentInjector;
import com.tokopedia.checkout.view.view.shipmentform.MultipleAddressShipmentFragment;
import com.tokopedia.checkout.view.view.shipmentform.ResetShipmentFormDialog;
import com.tokopedia.checkout.view.view.shipmentform.SingleAddressShipmentFragment;

/**
 * @author Irfan Khoirul on 23/04/18.
 */

public class ShipmentActivity extends BaseCheckoutActivity implements HasComponent<CartComponent> {

    public static final int RESULT_CODE_FORCE_RESET_CART_FROM_SINGLE_SHIPMENT = 2;
    public static final int RESULT_CODE_FORCE_RESET_CART_FROM_MULTIPLE_SHIPMENT = 3;

    public static final String EXTRA_SHIPMENT_FORM_DATA = "EXTRA_SHIPMENT_FORM_DATA";
    public static final String EXTRA_CART_PROMO_SUGGESTION = "EXTRA_CART_PROMO_SUGGESTION";
    public static final String EXTRA_PROMO_CODE_APPLIED_DATA = "EXTRA_PROMO_CODE_APPLIED_DATA";

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
        showResetDialog();
    }

    void showResetDialog() {
        DialogFragment dialog = ResetShipmentFormDialog.newInstance(
                new ResetShipmentFormDialog.ResetShipmentFormCallbackAction() {

                    @Override
                    public void onResetCartShipmentForm() {
                        if (getSupportFragmentManager().findFragmentById(R.id.parent_view)
                                instanceof SingleAddressShipmentFragment) {
                            setResult(RESULT_CODE_FORCE_RESET_CART_FROM_SINGLE_SHIPMENT);
                        } else if (getSupportFragmentManager().findFragmentById(R.id.parent_view)
                                instanceof MultipleAddressShipmentFragment) {
                            setResult(RESULT_CODE_FORCE_RESET_CART_FROM_MULTIPLE_SHIPMENT);
                        }
                        finish();
                    }

                    @Override
                    public void onCancelResetCartShipmentForm() {

                    }
                });
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.add(dialog, ResetShipmentFormDialog.DIALOG_FRAGMENT_TAG);
        ft.commitAllowingStateLoss();
    }


}
