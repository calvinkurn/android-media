package com.tokopedia.checkout.view.view.shippingoptions;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.tokopedia.checkout.domain.datamodel.shipmentrates.ShipmentDetailData;
import com.tokopedia.checkout.view.base.BaseCheckoutActivity;

/**
 * Created by Irfan Khoirul on 26/01/18.
 */

public class ShipmentDetailActivity extends BaseCheckoutActivity
        implements ShipmentDetailFragment.FragmentListener {

    public static final String EXTRA_SHIPMENT_DETAIL_DATA = "shipmentDetailData";
    public static final String EXTRA_POSITION = "position";

    public static Intent createInstance(Activity activity, ShipmentDetailData shipmentDetailData,
                                        int position) {
        Intent intent = new Intent(activity, ShipmentDetailActivity.class);
        intent.putExtra(EXTRA_SHIPMENT_DETAIL_DATA, shipmentDetailData);
        intent.putExtra(EXTRA_POSITION, position);
        return intent;
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
    public void onCourierSelected(ShipmentDetailData shipmentDetailData) {
        Intent intentResult = new Intent();
        intentResult.putExtra(EXTRA_SHIPMENT_DETAIL_DATA, shipmentDetailData);
        intentResult.putExtra(EXTRA_POSITION, getIntent().getIntExtra(EXTRA_POSITION, 0));
        setResult(Activity.RESULT_OK, intentResult);
        finish();
    }

    @Override
    protected Fragment getNewFragment() {
        return ShipmentDetailFragment.newInstance(
                (ShipmentDetailData) getIntent().getParcelableExtra(EXTRA_SHIPMENT_DETAIL_DATA)
        );
    }
}
