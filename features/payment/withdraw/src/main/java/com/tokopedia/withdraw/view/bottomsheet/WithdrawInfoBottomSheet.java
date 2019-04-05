package com.tokopedia.withdraw.view.bottomsheet;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.support.annotation.NonNull;

import com.tokopedia.design.bottomsheet.BottomSheetView;
import com.tokopedia.withdraw.R;

public class WithdrawInfoBottomSheet extends BottomSheetView {

    public WithdrawInfoBottomSheet(@NonNull Context context) {
        super(context);
    }

    @Override
    protected int getLayout() {
        return R.layout.withdraw_info_bottom_sheet;
    }

    @Override
    protected void init(Context context) {
        if (context instanceof Activity) layoutInflater = ((Activity) context).getLayoutInflater();
        else layoutInflater = ((Activity) ((ContextWrapper) context)
                .getBaseContext()).getLayoutInflater();

        bottomSheetView = layoutInflater.inflate(getLayout(), null);
        setContentView(bottomSheetView);

        bottomSheetView.findViewById(R.id.close_bottom_sheet).setOnClickListener(view -> dismiss());
    }

}
