package com.tokopedia.nps.presentation.view.dialog;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.FragmentManager;
import android.view.View;

import com.tokopedia.config.GlobalConfig;
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
        return BottomSheetsState.FLEXIBLE;
    }

    @Override
    protected String title() {
        return "";
    }

    @Override
    public void initView(View view) {
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

    private String getAppType() {
        return GlobalConfig.isSellerApp()
                ? GlobalConfig.PACKAGE_SELLER_APP
                : GlobalConfig.PACKAGE_CONSUMER_APP;
    }
}
