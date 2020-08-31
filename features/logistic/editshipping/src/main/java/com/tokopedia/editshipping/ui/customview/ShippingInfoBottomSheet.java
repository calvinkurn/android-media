package com.tokopedia.editshipping.ui.customview;

import android.content.Context;
import android.widget.TextView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.tokopedia.editshipping.R;

/**
 * Created by kris on 8/25/16. Tokopedia
 */
public class ShippingInfoBottomSheet {

    TextView courierInformation;
    TextView serviceNameTextView;

    public ShippingInfoBottomSheet(String information, String serviceName, Context activity) {
        BottomSheetDialog dialog = new BottomSheetDialog(activity);
        dialog.setContentView(R.layout.shipping_info_bottom_sheet);

        courierInformation = (TextView) dialog.findViewById(R.id.courier_information);
        serviceNameTextView =(TextView) dialog.findViewById(R.id.courier_name_service);

        courierInformation.setText(information);
        serviceNameTextView.setText(serviceName);
        dialog.show();
    }

}
