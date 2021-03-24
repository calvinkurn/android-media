package com.tokopedia.checkout.view;

import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import com.tokopedia.purchase_platform.common.analytics.CheckoutAnalyticsCourierSelection;
import com.tokopedia.purchase_platform.common.base.BaseCheckoutActivity;
import com.tokopedia.purchase_platform.common.constant.CartConstant;

import static com.tokopedia.purchase_platform.common.constant.CheckoutConstant.EXTRA_IS_ONE_CLICK_SHIPMENT;

/**
 * @author Irfan Khoirul on 23/04/18.
 */

public class ShipmentActivity extends BaseCheckoutActivity {

    private ShipmentFragment shipmentFragment;

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
                getIntent().getBooleanExtra(EXTRA_IS_ONE_CLICK_SHIPMENT, false),
                leasingId,
                getIntent().getExtras()
        );

        return shipmentFragment;
    }

    @Override
    public void onBackPressed() {
        if (shipmentFragment != null) {
            shipmentFragment.onBackPressed();
            setResult(shipmentFragment.getResultCode());
            finish();
        } else {
            super.onBackPressed();
        }
    }
}
