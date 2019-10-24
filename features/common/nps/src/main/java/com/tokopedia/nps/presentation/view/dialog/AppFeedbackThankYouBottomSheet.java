package com.tokopedia.nps.presentation.view.dialog;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import androidx.fragment.app.FragmentManager;
import android.view.View;

import com.tokopedia.nps.NpsConstant;
import com.tokopedia.nps.R;

public class AppFeedbackThankYouBottomSheet extends AppFeedbackDialog {

    private float appRating;

    @Override
    public int getLayoutResourceId() {
        return R.layout.dialog_feedback_thank_you;
    }

    @Override
    public void initView(View view) {
        super.initView(view);
        Context ctx = getContext();

        if (ctx != null && (int) appRating > NpsConstant.Feedback.GOOD_RATING_THRESHOLD) {
            try {
                ctx.startActivity(new Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse(NpsConstant.Feedback.APPLINK_PLAYSTORE + getAppType())
                ));
            } catch (ActivityNotFoundException exception) {
                ctx.startActivity(new Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse(NpsConstant.Feedback.URL_PLAYSTORE + getAppType())
                ));
            }
        }
    }

    public void showDialog(FragmentManager manager, float appRating, String tag) {
        super.show(manager, tag);
        this.appRating = appRating;
    }
}
