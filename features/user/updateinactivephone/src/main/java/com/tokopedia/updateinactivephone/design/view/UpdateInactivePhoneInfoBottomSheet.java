package com.tokopedia.updateinactivephone.design.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;

import com.tokopedia.design.bottomsheet.BottomSheetView;
import com.tokopedia.design.component.BottomSheets;
import com.tokopedia.updateinactivephone.R;

public class UpdateInactivePhoneInfoBottomSheet extends BottomSheetView {

    public UpdateInactivePhoneInfoBottomSheet(@NonNull Context context) {
        super(context);
    }

    @Override
    protected int getLayout() {
        return R.layout.bottom_sheet_info_layout;
    }

}
