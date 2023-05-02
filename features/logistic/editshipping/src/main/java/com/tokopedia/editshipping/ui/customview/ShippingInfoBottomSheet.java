package com.tokopedia.editshipping.ui.customview;

import android.content.Context;
import android.view.LayoutInflater;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.tokopedia.editshipping.R;
import com.tokopedia.editshipping.databinding.ShippingInfoBottomSheetBinding;
import com.tokopedia.unifyprinciples.Typography;

/**
 * Created by kris on 8/25/16. Tokopedia
 */
public class ShippingInfoBottomSheet {

    public ShippingInfoBottomSheet(String information, String serviceName, Context activity) {
        BottomSheetDialog dialog = new BottomSheetDialog(activity);
        ShippingInfoBottomSheetBinding binding = ShippingInfoBottomSheetBinding.inflate(LayoutInflater.from(activity));
        dialog.setContentView(binding.getRoot());

        binding.courierInformation.setText(information);
        binding.courierNameService.setText(serviceName);
        dialog.show();
    }

}
