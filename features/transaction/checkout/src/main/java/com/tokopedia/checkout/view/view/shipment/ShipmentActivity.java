package com.tokopedia.checkout.view.view.shipment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.tokopedia.checkout.domain.datamodel.cartlist.CartPromoSuggestion;
import com.tokopedia.checkout.domain.datamodel.cartshipmentform.CartShipmentAddressFormData;
import com.tokopedia.checkout.domain.datamodel.voucher.PromoCodeAppliedData;
import com.tokopedia.checkout.view.base.BaseCheckoutActivity;

/**
 * @author Irfan Khoirul on 23/04/18.
 */

public class ShipmentActivity extends BaseCheckoutActivity {

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
}
