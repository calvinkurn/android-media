package com.tokopedia.nps.presentation.view.dialog;

import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.support.v4.app.FragmentManager;
import android.view.View;

import com.tokopedia.design.component.BottomSheets;
import com.tokopedia.nps.NpsConstant;
import com.tokopedia.nps.R;

public class AppFeedbackThankYouBottomSheet extends BottomSheets {

    private float appRating;

    public void showDialog(FragmentManager manager, float appRating, String tag) {
        super.show(manager, tag);
        this.appRating = appRating;
    }

    @Override
    public int getBaseLayoutResourceId() {
        return R.layout.dialog_feedback_base;
    }

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
        if ((int) appRating > NpsConstant.Feedback.GOOD_RATING_THRESHOLD) {
            try {
                getContext().startActivity(new Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse(NpsConstant.Feedback.APPLINK_PLAYSTORE + NpsConstant.Feedback.PACKAGE_CONSUMER_APP)
                ));
            } catch (ActivityNotFoundException exception) {
                getContext().startActivity(new Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse(NpsConstant.Feedback.URL_PLAYSTORE + NpsConstant.Feedback.PACKAGE_CONSUMER_APP)
                ));
            }
        }
    }

    @Override
    public void setupDialog(Dialog dialog, int style) {
        super.setupDialog(dialog, style);
        updateHeight((int) (Resources.getSystem().getDisplayMetrics().heightPixels * 0.75));
    }
}
