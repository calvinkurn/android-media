package com.tokopedia.nps.presentation.view.dialog;

import android.view.View;

import com.tokopedia.design.component.BottomSheets;
import com.tokopedia.nps.R;

public class AppFeedbackThankYouBottomSheet extends BottomSheets {
    @Override
    public int getLayoutResourceId() {
        return R.layout.dialog_feedback_thank_you;
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
