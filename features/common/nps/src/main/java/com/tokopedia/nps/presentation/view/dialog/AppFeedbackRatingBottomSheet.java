package com.tokopedia.nps.presentation.view.dialog;

import android.support.v4.app.FragmentManager;
import android.view.View;

import com.tokopedia.design.component.BottomSheets;
import com.tokopedia.nps.R;

public class AppFeedbackRatingBottomSheet extends BottomSheets {
    @Override
    public int getLayoutResourceId() {
        return R.layout.dialog_feedback_rating;
    }

    @Override
    protected BottomSheetsState state() {
        return BottomSheetsState.FULL;
    }

    @Override
    public void initView(View view) {

    }

    @Override
    public void show(FragmentManager manager, String tag) {
        super.show(manager, tag);
    }
}
