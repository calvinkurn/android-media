package com.tokopedia.editshipping.ui.customview;

import android.content.Context;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.tokopedia.editshipping.R;
import com.tokopedia.unifyprinciples.Typography;

/**
 * Created by kris on 8/25/16. Tokopedia
 */
public class ShippingInfoBottomSheet {

    Typography courierInformation;
    Typography serviceNameTextView;

    public ShippingInfoBottomSheet(String information, String serviceName, Context activity) {
        BottomSheetDialog dialog = new BottomSheetDialog(activity);
        dialog.setContentView(R.layout.shipping_info_bottom_sheet);

        courierInformation = (Typography) dialog.findViewById(R.id.courier_information);
        serviceNameTextView =(Typography) dialog.findViewById(R.id.courier_name_service);

        courierInformation.setText(information);
        serviceNameTextView.setText(serviceName);
        dialog.show();
    }

}
