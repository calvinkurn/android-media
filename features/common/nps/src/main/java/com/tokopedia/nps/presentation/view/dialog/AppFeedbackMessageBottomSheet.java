package com.tokopedia.nps.presentation.view.dialog;

import android.view.View;

import com.tokopedia.design.component.BottomSheets;

public class AppFeedbackMessageBottomSheet extends BottomSheets {

    @Override
    public int getLayoutResourceId() {
        return 0;
    }

    @Override
    protected BottomSheetsState state() {
        return BottomSheetsState.NORMAL;
    }

    @Override
    protected String title() {
        return "";
    }

    @Override
    public void initView(View view) {

    }
}
