package com.tokopedia.checkout.view.view.shippingrecommendation;

import android.view.View;

import com.tokopedia.checkout.R;
import com.tokopedia.design.component.BottomSheets;

/**
 * Created by Irfan Khoirul on 06/08/18.
 */

public class ShipmentRecommendationDurationBottomsheet extends BottomSheets {

    @Override
    public int getLayoutResourceId() {
        return R.layout.fragment_shipment_duration_choice;
    }

    @Override
    protected String title() {
        return getString(R.string.title_bottomsheet_shipment_duration);
    }

    @Override
    public void initView(View view) {

    }

}
