package com.tokopedia.updateinactivephone.design.view;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import androidx.annotation.NonNull;
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

    @Override
    protected void init(Context context) {
//        super.init(context);

        if (context instanceof Activity) layoutInflater = ((Activity) context).getLayoutInflater();
        else layoutInflater = ((Activity) ((ContextWrapper) context)
                .getBaseContext()).getLayoutInflater();

        bottomSheetView = layoutInflater.inflate(getLayout(), null);
        setContentView(bottomSheetView);

        bottomSheetView.findViewById(R.id.button_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
    }
}
