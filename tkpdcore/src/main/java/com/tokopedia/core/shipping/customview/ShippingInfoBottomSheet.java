package com.tokopedia.core.shipping.customview;

import android.content.Context;
import android.support.design.widget.BottomSheetDialog;
import android.widget.TextView;

import com.tokopedia.core.R;
import com.tokopedia.core.R2;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by kris on 8/25/16. Tokopedia
 */
public class ShippingInfoBottomSheet {

    @BindView(R2.id.courier_information)
    TextView courierInformation;

    @BindView(R2.id.courier_name_service)
    TextView serviceNameTextView;

    public ShippingInfoBottomSheet(String information, String serviceName, Context activity) {
        BottomSheetDialog dialog = new BottomSheetDialog(activity);
        dialog.setContentView(R.layout.shipping_info_bottom_sheet);
        ButterKnife.bind(this, dialog);
        courierInformation.setText(information);
        serviceNameTextView.setText(serviceName);
        dialog.show();
    }

}
